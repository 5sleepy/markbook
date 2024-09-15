package markbook.views.testaggregate;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import markbook.views.MainLayout;
import markbook.classes.Student;
import markbook.classes.Test;

import java.util.*;
import java.io.*;

@Route(value = "testaggregate", layout = MainLayout.class) 
@PageTitle("Test List | Markbook")
public class TestAggregate extends VerticalLayout {
	ArrayList<Test> tests = new ArrayList<Test>();
	public TestAggregate() {
		VerticalLayout vl = new VerticalLayout();
		RadioButtonGroup<String> sortBy = new RadioButtonGroup<>();
		RadioButtonGroup<String> ascending = new RadioButtonGroup<>();
		Button enter = new Button("Confirm");
		Button save = new Button("Save"), delete = new Button("Delete");
		TextField search = new TextField();
		NumberField unit = new NumberField(), paper = new NumberField(), student = new NumberField(), score = new NumberField();
		tests=testArrayList();
		
		unit.setLabel("Unit");
		paper.setLabel("Paper");
		student.setLabel("Student Number");
		score.setLabel("Score");
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		student.setEnabled(false);
		
		search.setPlaceholder("Search");
		search.setPrefixComponent(VaadinIcon.SEARCH.create());
		search.setClearButtonVisible(true);
		
		sortBy.setLabel("Sort by");
		sortBy.setItems("Student Number", "Unit");
		sortBy.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		
		ascending.setLabel("Order");
		ascending.setItems("Ascending", "Descending");
		ascending.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		
		// initialize grid with values
		Grid<Test> grid = new Grid<>();
		grid.setItems(tests);
		grid.addColumn(Test::getId).setHeader("Student Number");
		grid.addColumn(Test::getUnit).setHeader("Unit");
		grid.addColumn(Test::getPaper).setHeader("Paper");
		grid.addColumn(Test::getScore).setHeader("Score");
		grid.addColumn(Test::getLevel).setHeader("Level");
		grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
		
		// if row in grid is selected
		grid.addItemClickListener(select -> {
			Test test = select.getItem();
			
			// if selected
			if (test!=null) {
				// update side menu with info from grid
				student.setValue((double)test.getId());
				unit.setValue((double)test.getUnit());
				paper.setValue((double)test.getPaper());
				score.setValue((double)test.getScore());
			// deselected
			} else {
				// clear fields on side menu
				student.setValue(null);
				unit.setValue(null);
				paper.setValue(null);
				score.setValue(null);
			}
		});
		
		// save changes
		save.addClickListener(click -> {
			Optional<Test> temp = grid.getSelectionModel().getFirstSelectedItem();
			
			if (temp.isPresent()) {
				Test test = temp.get();
				// check for valid inputs
				if (student.getValue().intValue()<1000000&&student.getValue().intValue()>99999&&score.getValue().intValue()<=test.getBounds().get(0)&&score.getValue().intValue()>-1) {
					// create new test
					Test newTest = new Test(unit.getValue().intValue(),paper.getValue().intValue(),student.getValue().intValue(),score.getValue().intValue(),new File("Tests.txt"));
					// remove old test
					tests.remove(test);
					// add new test
					tests.add(newTest);
					
					// write changes into file
					try {
						FileWriter w = new FileWriter("StudentScores.txt",false);
						for (Test a : tests) {
							w.write(String.format("%s,%s,%s,%s\n", a.getUnit(), a.getPaper(), a.getId(), a.getScore()));
						}
						w.close();
					} catch (IOException e) {
						e.getStackTrace();
					}
					// clear side menu
					grid.select(null);
					student.setValue(null);
					unit.setValue(null);
					paper.setValue(null);
					score.setValue(null);
					// refresh grid
					grid.setItems(tests);
				}
			} else if (student.getValue()==null||score.getValue()==null) {
				
			// if some other issue occurs
			} else {
				// notify user of erroneous data
				Notification n = Notification.show("Invalid student number or score.");
				n.addThemeVariants(NotificationVariant.LUMO_ERROR);
				n.setPosition(Notification.Position.BOTTOM_END);
			}
		});
		
		// delete selected test
		delete.addClickListener(click -> {
			
			Optional<Test> temp = grid.getSelectionModel().getFirstSelectedItem();
			
			if (temp.isPresent()) {
				Test test = temp.get();
				// remove from arraylist
				tests.remove(test);
				
				// overwrite old data with new data
				try {
					FileWriter w = new FileWriter("StudentScores.txt",false);
					for (Test a : tests) {
						w.write(String.format("%s,%s,%s,%s\n", a.getUnit(), a.getPaper(), a.getId(), a.getScore()));
					}
					w.close();
				} catch (IOException e) {
					e.getStackTrace();
				}
				
				// clear side menu
				grid.select(null);
				student.setValue(null);
				unit.setValue(null);
				paper.setValue(null);
				score.setValue(null);
				
				// update grid
				grid.setItems(tests);
			}
		});
		// search bar and other related things updater

		enter.addClickListener(click -> {			
			// if the search bar isn't empty search
			if (!search.getValue().equals("")) {
				ArrayList<Test> a = search(search.getValue(), tests);
				grid.setItems(a);
			// otherwise
			} else {
				// make sure the grid has values
				grid.setItems(tests);
				
				boolean ascend = true; // default sort ascending
				// checks for empty fields
				if (ascending.getValue()!=null&&sortBy.getValue()!=null){
					// determines ascending or descending based on user selection
					if (ascending.getValue().equals("Ascending")) {
						ascend = true;
					} else if (ascending.getValue().equals("Descending")){
						ascend = false;
					}
					
					// determines sort key based on user selection
					if (sortBy.getValue().equals("Student Number")) {
						sortStudent(ascend);
						grid.setItems(tests);
					} else if (sortBy.getValue().equals("Unit")) {
						sortAssessment(ascend);
						grid.setItems(tests);
					}
					
				// if empty fields raise error notification related to which fields are empty
				} else {
					String errmsg = "";
					if (ascending.getValue()==null&&sortBy.getValue()==null) {
						errmsg = "Missing sort by and order parameters.";
					} else if (ascending.getValue()==null&&sortBy.getValue()!=null) {
						errmsg = "Missing order parameter.";
					} else if (ascending.getValue()!=null&&sortBy.getValue()==null) {
						errmsg = "Missing sort by parameter.";
					}
					
					Notification n = Notification.show(errmsg);
					n.addThemeVariants(NotificationVariant.LUMO_ERROR);
					n.setPosition(Notification.Position.BOTTOM_END);
					
				}
			}
		});
		
		enter.addClickShortcut(Key.ENTER);
		enter.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		VerticalLayout s = new VerticalLayout(search, enter);
		s.setWidth("400px");
		HorizontalLayout hl = new HorizontalLayout(sortBy,ascending,s);
		hl.setAlignItems(Alignment.START);
		add(hl);
		grid.setHeight("700px");
		grid.setWidth("8000px");
		H3 e = new H3("Edit");

		HorizontalLayout l = new HorizontalLayout(score, delete);
		l.setAlignItems(Alignment.BASELINE);
		save.setWidth("270px");
		delete.setWidth("128px");
		
		HorizontalLayout otherhl = new HorizontalLayout(grid, new VerticalLayout(e, student, new HorizontalLayout(unit, paper), l, save));
		student.setWidthFull();
		otherhl.setWidthFull();
		otherhl.setHeightFull();
		add(otherhl);
		
	}
	
	private void sortStudent(boolean ascending) { // sorts tests by student number, ascending/descending determined by user input
		// selection sort
		if (ascending) {
			for (int i = 0; i<tests.size()-1; i++) {
				int min = i;
				
				for (int j=i+1; j<tests.size(); j++) {
					if (tests.get(j).compareTo(tests.get(min), false)<0) {
						min = j;
					}
				}
				
				Collections.swap(tests, i, min);
			}
		} else {
			for (int i = 0; i<tests.size()-1; i++) {
				int max = i;
				
				for (int j=i+1; j<tests.size(); j++) {
					if (tests.get(j).compareTo(tests.get(max), false)>0) {
						max = j;
					}
				}
				
				Collections.swap(tests, i, max);
			}
		}
	}
	
	private void sortAssessment(boolean ascending) { // sorts tests by unit then paper, ascending/descending determined by user input
		// selection sort
		if (ascending) {
			for (int i = 0; i<tests.size()-1; i++) {
				int min = i;
				
				for (int j=i+1; j<tests.size(); j++) {
					if (tests.get(j).compareTo(tests.get(min), true)<0) {
						min = j;
					}
				}
				
				Collections.swap(tests, i, min);
			}
		} else {
			for (int i = 0; i<tests.size()-1; i++) {
				int max = i;
				
				for (int j=i+1; j<tests.size(); j++) {
					if (tests.get(j).compareTo(tests.get(max), true)>0) {
						max = j;
					}
				}
				
				Collections.swap(tests, i, max);
			}
		}
	}
	
	public ArrayList<Test> testArrayList(){ // reads lines from StudentScores.txt file and initializes them as tests. sorts with bubble sort.
		ArrayList<Test> t = new ArrayList<Test>();
		try {
			Scanner in = new Scanner(new File("StudentScores.txt"));
			
			// reads all lines from StudentScores.txt
			while(in.hasNextLine()) {
				// initializes as tests and adds them to output arraylist
				String[] s = in.nextLine().split(",");
				t.add(new Test(Integer.parseInt(s[0]), Integer.parseInt(s[1]),Integer.parseInt(s[2]), Integer.parseInt(s[3]), new File("Tests.txt")));
			}
			
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}
		// bubble sort
		boolean flag;
		for (int i=0; i<t.size()-1; i++) {
			flag = false;
			for(int j=0; j<t.size()-i-1;j++) {
				if (t.get(j).compareTo(t.get(j+1), false)==1) {
					Collections.swap(t, j, j+1);
					flag=true;
				}
			}
			
			if (!flag) {
				break;
			}
		}
		
		return t;
	}
	
	private ArrayList<Test> search(String value, ArrayList<Test> range) { // linear search for terms that contain value substring
		ArrayList<Test> out = new ArrayList<Test>();
		for (Test t : range) {
			if (t.toString().contains(value)) {
				out.add(t);
			}
		}
		
		return out;
	}
}
