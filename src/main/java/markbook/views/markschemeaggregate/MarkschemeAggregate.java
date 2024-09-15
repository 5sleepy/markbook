package markbook.views.markschemeaggregate;

import java.io.*;
import java.util.*;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import markbook.classes.Markscheme;
import markbook.views.MainLayout;

@Route(value = "markschemeaggregate", layout = MainLayout.class) 
@PageTitle("Markscheme List | Markbook")
public class MarkschemeAggregate extends VerticalLayout {
	private ArrayList<Markscheme> markschemes = new ArrayList<Markscheme>();
	
	public MarkschemeAggregate() {
		VerticalLayout vl = new VerticalLayout();
		RadioButtonGroup<String> sortBy = new RadioButtonGroup<>();
		RadioButtonGroup<String> ascending = new RadioButtonGroup<>();
		Button enter = new Button("Confirm");
		Button save = new Button("Save"), delete = new Button("Delete");
		TextField search = new TextField();
		NumberField unit = new NumberField(), paper = new NumberField(), seven = new NumberField(), six = new NumberField(), five = new NumberField(), four = new NumberField(), three = new NumberField(), two = new NumberField(), one = new NumberField();
		markschemes=markschemeArrayList(); // initialized markschemes arraylist with all the data from the file
		
		unit.setLabel("Unit");
		paper.setLabel("Paper");
		seven.setLabel("Seven");
		six.setLabel("Six");
		five.setLabel("Five");
		four.setLabel("Four");
		three.setLabel("Three");
		two.setLabel("Two");
		one.setLabel("One");
		
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.setWidth("270px");
		
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		delete.setWidth("127px");
		
		search.setPlaceholder("Search");
		search.setPrefixComponent(VaadinIcon.SEARCH.create());
		search.setClearButtonVisible(true);
		
		sortBy.setLabel("Sort by");
		sortBy.setItems("Unit","Assessment");
		sortBy.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		
		ascending.setLabel("Order");
		ascending.setItems("Ascending", "Descending");
		ascending.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		
		// initializing the grid and assigning the items and columns
		Grid<Markscheme> grid = new Grid<>();
		grid.setItems(markschemes);
		grid.addColumn(Markscheme::getUnit).setHeader("Unit");
		grid.addColumn(Markscheme::getPaper).setHeader("Paper");
		grid.addColumn(Markscheme::getSeven).setHeader("Seven");
		grid.addColumn(Markscheme::getSix).setHeader("Six");
		grid.addColumn(Markscheme::getFive).setHeader("Five");
		grid.addColumn(Markscheme::getFour).setHeader("Four");
		grid.addColumn(Markscheme::getThree).setHeader("Three");
		grid.addColumn(Markscheme::getTwo).setHeader("Two");
		grid.addColumn(Markscheme::getOne).setHeader("One");
		grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
		
		// if an item is clicked, load it into the side menu
		grid.addSelectionListener(select -> {
			Optional<Markscheme> temp = select.getFirstSelectedItem();
			
			// if there is a value in temp
			if (temp.isPresent()) {
				// load the values into the side menu
				Markscheme s = temp.get();
				unit.setValue((double)s.getUnit());
				paper.setValue((double)s.getPaper());
				seven.setValue((double)s.getSeven());
				six.setValue((double)s.getSix());
				five.setValue((double)s.getFive());
				four.setValue((double)s.getFour());
				three.setValue((double)s.getThree());
				two.setValue((double)s.getTwo());
				one.setValue((double)s.getOne());

			// clear all fields on the side menu
			} else {
				unit.setValue(null);
				paper.setValue(null);
				seven.setValue(null);
				six.setValue(null);
				five.setValue(null);
				four.setValue(null);
				three.setValue(null);
				two.setValue(null);
				one.setValue(null);
			}
			
		});
		
		// delete selected entry
		delete.addClickListener(click -> {
			Optional<Markscheme> temp = grid.getSelectionModel().getFirstSelectedItem();
			
			if (temp.isPresent()) {
				Markscheme markscheme = temp.get();
				
				// remove selected entry from arraylist
				markschemes.remove(markscheme);
				
				// overwrite previous data with new arraylist
				try {
					FileWriter w = new FileWriter("Tests.txt",false);
					for (Markscheme a : markschemes) {
						w.write(String.format("%s,%s,%s\n", a.getUnit(), a.getPaper(), a.getBounds()));
					}
					w.close();
				} catch (IOException e) {
					e.getStackTrace();
				}
				// clear side menu
				unit.setValue(null);
				paper.setValue(null);
				seven.setValue(null);
				six.setValue(null);
				five.setValue(null);
				four.setValue(null);
				three.setValue(null);
				two.setValue(null);
				one.setValue(null);
				grid.select(null);

				// refresh the displayed arraylist
				grid.setItems(markschemes);
			}
		});
		
		// save changes to values
		save.addClickListener(click -> {
			Optional<Markscheme> temp = grid.getSelectionModel().getFirstSelectedItem();

			if (temp.isPresent()) {
				Markscheme markscheme = temp.get();
				// checks if all upper bounds of greater levels are greater than that of those below it
				if (seven.getValue()>six.getValue()&&six.getValue()>five.getValue()&&five.getValue()>four.getValue()&&four.getValue()>three.getValue()&&three.getValue()>two.getValue()&&two.getValue()>one.getValue()) {
					// create new markscheme
					String[] bounds = {Integer.toString(seven.getValue().intValue()), Integer.toString(six.getValue().intValue()), Integer.toString(five.getValue().intValue()),Integer.toString(four.getValue().intValue()),Integer.toString(three.getValue().intValue()),Integer.toString(two.getValue().intValue()),Integer.toString(one.getValue().intValue())};
					Markscheme newMarkscheme = new Markscheme(unit.getValue().intValue(), paper.getValue().intValue(),bounds);
					
					// remove old markscheme
					markschemes.remove(markscheme);
					
					// add new markscheme
					markschemes.add(newMarkscheme);
					
					// rewrite file with new changes
					try {
						FileWriter w = new FileWriter("Tests.txt",false);
						for (Markscheme a : markschemes) {
							w.write(String.format("%s,%s,%s\n", a.getUnit(), a.getPaper(), a.getBounds()));
						}
						w.close();
					} catch (IOException e) {
						e.getStackTrace();
					}
					
					// clear all values
					unit.setValue(null);
					paper.setValue(null);
					seven.setValue(null);
					six.setValue(null);
					five.setValue(null);
					four.setValue(null);
					three.setValue(null);
					two.setValue(null);
					one.setValue(null);
					grid.select(null);

					// refresh grid
					grid.setItems(markschemes);
				} else {
					// raise notification to inform user of erroneous bounds
					Notification n = Notification.show("Invalid bounds.");
					n.addThemeVariants(NotificationVariant.LUMO_ERROR);
					n.setPosition(Notification.Position.BOTTOM_END);
				}
			}
			
		});
		
		// search bar and other related things updater
		enter.addClickListener(click -> {
			// if the search bar isn't empty search
			if (!search.getValue().equals("")) {
				ArrayList<Markscheme> a = search(search.getValue(), markschemes);
				grid.setItems(a);
			
			// otherwise
			} else {
				// make sure the grid has values
				grid.setItems(markschemes);
				
				boolean ascend = true; // default sort ascending		
				// checks if any fields are empty
				if (ascending.getValue()!=null&&sortBy.getValue()!=null){ 
					// determines ascending or descending based on user selection
					if (ascending.getValue().equals("Ascending")) {
						ascend = true;
					} else if (ascending.getValue().equals("Descending")){
						ascend = false;
					}
					
					// determines sort key based on user selection
					if (sortBy.getValue().equals("Unit")) {
						// sort function
						sortUnit(ascend);
						grid.setItems(markschemes);
					} else if (sortBy.getValue().equals("Assessment")) {
						sortAssessment(ascend);
						grid.setItems(markschemes);
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
		HorizontalLayout w = new HorizontalLayout(one, delete);
		w.setAlignItems(Alignment.BASELINE);
		HorizontalLayout otherhl = new HorizontalLayout(grid,new VerticalLayout(new H3("Edit"), new HorizontalLayout(unit, paper), new HorizontalLayout(seven, six), new HorizontalLayout(five, four), new HorizontalLayout(three, two), w, save));
		otherhl.setWidthFull();
		otherhl.setHeightFull();
		add(otherhl);
	}
	
	private void sortAssessment(boolean ascending) { // sorts markschemes by their paper then unit
		// bubble sort
		if (ascending) {
			boolean flag;
			for (int i=0; i<markschemes.size()-1; i++) {
				flag=false;
				for (int j=0; j<markschemes.size()-i-1; j++) {
					if (markschemes.get(j).getPaper()>markschemes.get(j+1).getPaper()) {
						Collections.swap(markschemes, j, j+1);
						flag=true;
					} else if (markschemes.get(j).getPaper()==markschemes.get(j+1).getPaper()) {
						if (markschemes.get(j).getUnit()>markschemes.get(j+1).getUnit()) {
							Collections.swap(markschemes, j, j+1);
							flag=true;
						}
					}
				}
				if (!flag) {
					break;
				}
			}
		} else {
			boolean flag;
			for (int i=0; i<markschemes.size()-1; i++) {
				flag=false;
				for (int j=0; j<markschemes.size()-i-1; j++) {
					if (markschemes.get(j).getPaper()<markschemes.get(j+1).getPaper()) {
						Collections.swap(markschemes, j, j+1);
						flag=true;
					} else if (markschemes.get(j).getPaper()==markschemes.get(j+1).getPaper()) {
						if (markschemes.get(j).getUnit()>markschemes.get(j+1).getUnit()) {
							Collections.swap(markschemes, j, j+1);
							flag=true;
						}
					}
				}
				if (!flag) {
					break;
				}
			}
		}
	}

	private void sortUnit(boolean ascending) { // sorts markschemes by their unit then paper
		// selection sort
		if (ascending) {
			for (int i=0; i<markschemes.size(); i++) {
				int min=i;
				
				for (int j=i+1; j<markschemes.size(); j++) {
					if (markschemes.get(j).compareTo(markschemes.get(min))<0) {
						min = j;
					}
				}
				Collections.swap(markschemes, i, min);
			}
		} else {
			for (int i=0; i<markschemes.size(); i++) {
				int max = i;
				
				for (int j = i+1; j<markschemes.size(); j++) {
					if (markschemes.get(j).compareTo(markschemes.get(max))>0) {
						max = j;
					} 
				}
				

				Collections.swap(markschemes, i, max);

			}
		}
	}

	public ArrayList<Markscheme> markschemeArrayList() { // reads all lines from Tests.txt, initializes them as markschemes, then bubble sorts them
		ArrayList<Markscheme> t = new ArrayList<Markscheme>();
		File f = new File("Tests.txt");
		
		// read all lines from Tests.txt
		try {
			Scanner in = new Scanner(f);
			
			while(in.hasNextLine()) {
				String [] s = in.nextLine().split(",");
				// initialize string from file as markscheme
				t.add(new Markscheme(Integer.parseInt(s[0]),Integer.parseInt(s[1]), s[2].split(" ")));
			}
			in.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		// bubble sort the markscheme arraylist
		boolean flag;
		for (int i=0; i<t.size()-1;i++) {
			flag = false;
			for (int j=0;j<t.size()-i-1;j++) {
				if (t.get(j).compareTo(t.get(j+1))==1) {
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

	public ArrayList<Markscheme> search(String value, ArrayList<Markscheme> range) { // checks all markschemes in the markscheme arraylist for any matching strings
		ArrayList<Markscheme> out = new ArrayList<Markscheme>();
		for (Markscheme m : range) {
			if (m.toString().contains(value)) {
				out.add(m);
			}
		}
		return out;
	}
}
