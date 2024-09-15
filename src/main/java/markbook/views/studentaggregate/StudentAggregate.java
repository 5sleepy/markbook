package markbook.views.studentaggregate;

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
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import markbook.classes.Student;
import markbook.views.MainLayout;

@Route(value = "studentaggregate", layout = MainLayout.class) 
@PageTitle("Student List | Markbook")
public class StudentAggregate extends VerticalLayout {
	private ArrayList<String> lines = new ArrayList<String>();
	private ArrayList<Student> students = new ArrayList<Student>();
	
	public StudentAggregate() {
		VerticalLayout vl = new VerticalLayout();
		RadioButtonGroup<String> sortBy = new RadioButtonGroup<>();
		RadioButtonGroup<String> ascending = new RadioButtonGroup<>();
		Button enter = new Button("Confirm");
		TextField search = new TextField();
		Button save = new Button("Save"), delete = new Button("Delete");
		TextField Name = new TextField();
		NumberField StudentNumber = new NumberField();
		
		sortByStudent();
		students=studentArrayList();
		
		Name.setLabel("Name");
		Name.setWidth("190px");
		StudentNumber.setLabel("Student Number");
		StudentNumber.setWidth("190px");
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		save.setWidth("190px");
		delete.setWidth("190px");

		
		search.setPlaceholder("Search");
		search.setPrefixComponent(VaadinIcon.SEARCH.create());
		search.setClearButtonVisible(true);
		
		// initialize grid with data
		Grid<Student> grid = new Grid<>();
		grid.setItems(students);
		grid.addColumn(Student::getName).setHeader("Name");
		grid.addColumn(Student::getId).setHeader("Student Number");
		grid.addColumn(Student::getRaw).setHeader("Raw Average");
		grid.addColumn(Student::getLevel).setHeader("Weighted Level");
		grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
				
		// if row in grid selected
		grid.addSelectionListener(select -> {
			Optional<Student> tempStudent = select.getFirstSelectedItem();
			
			// checks if there is a student actively selected
			if (tempStudent.isPresent()) {
				// load information into side menu
				Student s = tempStudent.get();
				Name.setValue(s.getName());
				StudentNumber.setValue((double)s.getId());
				
			// if no student
			} else {
				// clear fields
				Name.setValue("");
				StudentNumber.setValue(null);
			}
			
		});
		
		// save changes
		save.addClickListener(click -> {
			Optional<Student> temp = grid.getSelectionModel().getFirstSelectedItem();
			
			if (temp.isPresent()) {
				Student student = temp.get();
				// check if student number is six digits
				if (StudentNumber.getValue().intValue()<1000000&&StudentNumber.getValue().intValue()>99999) {
					// apply changes to student
					Student newStudent = new Student(StudentNumber.getValue().intValue(), Name.getValue());
					
					// remove old student
					students.remove(student);
					
					// add new student
					students.add(newStudent);
					
					// remove any scores written by old student number
					try {
						Scanner in = new Scanner(new File("StudentScores.txt"));
						ArrayList<String> check = new ArrayList<String>();
						
						// read all lines into arraylist
						while (in.hasNextLine()) {
							String a = in.nextLine();
							// check for matching student numbers
							if (!a.contains(Integer.toString(student.getId()))) { 
								check.add(a);
							}
						}

						// if student id was changed
						if (student.getId()!=newStudent.getId()) {
							// remove tests written by old student number
							FileWriter w= new FileWriter("StudentScores.txt", false);
							for (String a : check) {
								w.write(a+"\n");
							}
							w.close();
						}
						
						// overwrite old student file with updated student information
						FileWriter c = new FileWriter("Students.txt",false);
						for (Student a : students) {
							c.write(String.format("%s,%s\n", a.getId(), a.getName()));
						}
						c.close();
					} catch (IOException e) {
						e.getStackTrace();
					}
					// clear fields
					grid.select(null);
					Name.setValue("");
					StudentNumber.setValue(null);
					
					// reload grid with changes
					grid.setItems(students);
				// if student number is invalid, raise notification to inform user of invalid student number
				} else {
					Notification n = Notification.show("Invalid student number.");
					n.addThemeVariants(NotificationVariant.LUMO_ERROR);
					n.setPosition(Notification.Position.BOTTOM_END);
				}
			}
			
			
		});
		
		// delete an entry
		delete.addClickListener(click -> {
			Optional<Student> temp = grid.getSelectionModel().getFirstSelectedItem();
			
			if (temp.isPresent()) {
				Student student = temp.get();
				
				students.remove(student);// remove student from arraylist
				
				try { // remove all tests written by deleted student
					Scanner in = new Scanner(new File("StudentScores.txt"));
					ArrayList<String> check = new ArrayList<String>();
					// read all tests into arraylist
					while (in.hasNextLine()) {
						String a = in.nextLine();
						if (!a.contains(Integer.toString(student.getId()))) { // check for matching student number
							check.add(a);
						}
					}
					
					// write back updated information
					FileWriter w= new FileWriter("StudentScores.txt", false);
					for (String a : check) {
						w.write(a+"\n");
					}
					w.close();
					
					// write back updated information
					FileWriter c = new FileWriter("Students.txt",false);
					for (Student a : students) {
						c.write(String.format("%s,%s\n", a.getId(), a.getName()));
					}
					c.close();
				} catch (IOException e) {
					e.getStackTrace();
				}
				
				// clear fields
				grid.select(null);
				Name.setValue("");
				StudentNumber.setValue(null);
				
				// update grid
				grid.setItems(students);
			}			
			
		});
		
		sortBy.setLabel("Sort by");
		sortBy.setItems("Student Number","Name");
		sortBy.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		
		ascending.setLabel("Order");
		ascending.setItems("Ascending", "Descending");
		ascending.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		
		// search and sort bar updater
		enter.addClickListener(click -> {			
			if (!search.getValue().equals("")) { // if search bar isn't empty, search
				ArrayList<Student> a = search(search.getValue(), students);
				
				// update grid with matching values
				grid.setItems(a);
			} else { // otherwise
				// make sure grid has items
				grid.setItems(students);
				boolean ascend = true;	// default sort ascending
				// checks for empty fields
				if (ascending.getValue()!=null&&sortBy.getValue()!=null){
					// determines ascending or descending based on user input
					if (ascending.getValue().equals("Ascending")) {
						ascend = true;
					} else if (ascending.getValue().equals("Descending")){
						ascend = false;
					}
					
					// determines sort key based on user input
					if (sortBy.getValue().equals("Student Number")) {
						// sort function
						sortNumerical(ascend);
						grid.setItems(students); // update grid
					} else if (sortBy.getValue().equals("Name")) {
						// sort function
						sortAlpha(ascend);
						grid.setItems(students); // update grid
					}
				// raise error related to any empty fields
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
		HorizontalLayout otherhl = new HorizontalLayout(grid,new VerticalLayout(new H3("Edit"), Name, StudentNumber, delete, save));
		otherhl.setWidthFull();
		otherhl.setHeightFull();
		add(otherhl);

		
		
	}
	
	public void sortByStudent() { // adds all lines from StudentScores.txt file and sorts them in ascending order based on student number
		File filepath = new File("StudentScores.txt");
		try {
			Scanner reader = new Scanner(filepath);
			// reads all lines into lines arraylist
			while(reader.hasNextLine()) {
				String temp = reader.nextLine();
				lines.add(temp);
			}
			reader.close();
			
			// bubble sort
			boolean flag=true;
			
			for (int i=0; i<lines.size(); i++) {
				flag=false;
				for (int j=0; j<lines.size()-1; j++) {
					String[] line1 = lines.get(j).substring(0,11).split(",");
					String[] line2 = lines.get(j+1).substring(0,11).split(",");
					
					if (Integer.parseInt(line1[2]) > Integer.parseInt(line2[2])) {
						String temp = lines.get(j);
						lines.set(j, lines.get(j+1));
						lines.set(j+1, temp);
						flag = true;
					} else if (Integer.parseInt(line1[2]) == Integer.parseInt(line2[2])) {
						if (Integer.parseInt(line1[0]) > Integer.parseInt(line2[0])) {
							String temp = lines.get(j);
							lines.set(j, lines.get(j+1));
							lines.set(j+1, temp);
							flag = true;
						} else if (Integer.parseInt(line1[0]) == Integer.parseInt(line2[0]) && Integer.parseInt(line1[1]) > Integer.parseInt(line2[1])){
							String temp = lines.get(j);
							lines.set(j, lines.get(j+1));
							lines.set(j+1, temp);
							flag = true;
						}
					}
					
				}
				
				if (!flag) {
					break;
				}
			}
			reader.close();
			FileWriter writer = new FileWriter(filepath, false);
			// write sorted arraylist back into file
			for(String i : lines) {
				writer.write(i+"\n");
			}
			writer.close();
			
		} catch (IOException e) {
			e.getStackTrace();
		}
	}
	
	// initializes all students in Students.txt, calculates their average raw percentage and weighted level, and puts them into an arraylist
	public ArrayList<Student> studentArrayList() { 
		ArrayList<Student> al = new ArrayList<Student>();
		try {
			File f = new File("Students.txt");
			Scanner in = new Scanner(f);
			
			// read in all lines
			while (in.hasNextLine()) {
				String[] s = in.nextLine().split(",");
				Student t = new Student(Integer.parseInt(s[0]), s[1]); // initialize as new student
				t.calcAll(lines); // calculate their level and raw
				al.add(t); // add to student arraylist
			}
			
			in.close();
			return al;
		} catch (IOException e) {
			e.getStackTrace();
		}
		return al;
	}

	public void sortAlpha(boolean ascending) { // sorts by student name in alpha order. ascending/descending based on user decision
		// selection sort
		if (ascending) {
			for (int i=0; i<students.size(); i++) {
				int min = i;
				
				for (int j = i+1; j<students.size(); j++) {
					if (students.get(j).compareTo(students.get(min), true)<0) {
						min = j;
					} 
				}
				
				Collections.swap(students, i, min);

			}

			
		} else {
			for (int i=0; i<students.size(); i++) {
				int max = i;
				
				for (int j = i+1; j<students.size(); j++) {
					if (students.get(j).compareTo(students.get(max), true)>0) {
						max = j;
					} 
				}
				

				Collections.swap(students, i, max);

			}
		}
	}

	public void sortNumerical(boolean ascending) {	// sorts by student number, ascending/descending based on user decision
		if (ascending) {
			for (int i=0; i<students.size(); i++) {
				int min = i;
				for (int j = i+1; j<students.size(); j++) {
					if (students.get(j).getId()<students.get(min).getId()) {
						min = j;
					}
				}
				Collections.swap(students, i, min);
			}
		} else {
			for (int i=0; i<students.size(); i++) {
				int max = i;
				
				for (int j = i+1; j<students.size(); j++) {
					if (students.get(j).getId()>students.get(max).getId()) {
						max = j;
					}
				}
				
				Collections.swap(students, i, max);
				
			}
		}
	}

	public ArrayList<Student> search(String s, ArrayList<Student> in) { // searches for student by looking for identical substrings
		// if the search term is a number
		try {
			int sNum = Integer.parseInt(s);
			ArrayList<Student> out = new ArrayList<Student>();
			
			// linear search using student ids
			for (Student a : in) {
				if (a.getId()==sNum) {
					out.add(a);
				}
			}
			return out;
		// if the search term contains strings	
		} catch (NumberFormatException e) {
			ArrayList<Student> out = new ArrayList<Student>();
			
			// linear search using names
			for (Student a : in) {
				if (a.toString().contains(s)) {
					out.add(a);
				}
			}
			return out;
		}
	}
}