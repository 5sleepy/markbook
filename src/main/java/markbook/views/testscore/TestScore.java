package markbook.views.testscore;

import java.io.File;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import markbook.classes.Test;
import markbook.views.MainLayout;

@Route(value = "testscore", layout = MainLayout.class) 
@PageTitle("Test Score | Markbook")
public class TestScore extends VerticalLayout{
	public TestScore() {
		VerticalLayout vl = new VerticalLayout();
		NumberField unit = new NumberField(); 
		NumberField student = new NumberField(), score = new NumberField();
		Select<String> paper = new Select<>();
		FormLayout layout = new FormLayout();
		H2 test = new H2("Test"), stdnt = new H2("Student"), level = new H2();
		Button save = new Button("Save"), clear = new Button("Clear");
		
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		unit.setLabel("Unit");
		student.setLabel("Student Number");
		score.setLabel("Score");
		paper.setLabel("Assessment");
		paper.setItems("Paper 1","Paper 2","Paper 3","Other");
		paper.setValue("Paper 1");
		
		// save button
		save.addClickListener(click -> {
			// remove the level text from the screen
			layout.remove(level);
			// checks if the fields are filled
			if (unit.getValue()!=null&&student.getValue()!=null&&score.getValue()!=null) { 
				// checks if student number is 6 digits
				if (student.getValue()>99999&&student.getValue()<1000000) {
					// assigns j a value based on the type of assessment
					int j;
					if (paper.getValue().equals("Paper 1"))
						j=1;
					else if (paper.getValue().equals("Paper 2"))
						j=2;
					else if (paper.getValue().equals("Paper 3"))
						j=3;
					else
						j=4;
					
					// initializes a new test
					Test t = new Test(unit.getValue().intValue(), j, student.getValue().intValue(), score.getValue().intValue(), new File("Tests.txt"));
					
					
					String l = ""; // level
					
					try {
						l=t.convert(); // turns score into a level
						t.writeToFile(); // writes the test information into the StudentScores.txt file
						level.setText("Level: "+l); // adds level to displayed text
					} catch (IndexOutOfBoundsException e) {
						// handling error for inputs larger than upper bound
						level.setText("Level: NaN");
						Notification n = Notification.show("Score entered is greater than greatest upper bound.");
						n.addThemeVariants(NotificationVariant.LUMO_ERROR);
						n.setPosition(Notification.Position.BOTTOM_END);
					}
					
					
				// if not, raises an error notification to inform the user of erroneous data
				} else {
					level.setText("");
					Notification n = Notification.show("Invalid student number.");
					n.addThemeVariants(NotificationVariant.LUMO_ERROR);
					n.setPosition(Notification.Position.BOTTOM_END);
				}
			// if not, raises an error notification to inform the user of erroneous data
			} else {
				level.setText("");
				Notification n = Notification.show("Please fill in all fields.");
				n.addThemeVariants(NotificationVariant.LUMO_ERROR);
				n.setPosition(Notification.Position.BOTTOM_END);
			}
			// adds the level text to the screen
			layout.add(level);
		});
		
		// clears student fields
		clear.addClickListener(click -> {
			student.setValue(null);
			score.setValue(null);
			
			// removes level text from screen
			layout.remove(level);
			
			// notifies user of change
			Notification n = Notification.show("Fields cleared.");
			n.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
			n.setPosition(Notification.Position.BOTTOM_END);
		});
		
		layout.add(test,
				   unit, paper,
				   stdnt,
				   student, score,
				   save, clear,
				   level);
		
		layout.setResponsiveSteps(
				new ResponsiveStep("0", 1),
				new ResponsiveStep("400px", 2));
		layout.setColspan(test, 2);
		layout.setColspan(stdnt, 2);
		layout.setColspan(level, 2);
		
		layout.setWidth("800px");
		
		HorizontalLayout hl = new HorizontalLayout(layout);
		hl.add(layout);
		hl.setAlignItems(FlexComponent.Alignment.CENTER);
		add(hl);
		setAlignItems(FlexComponent.Alignment.CENTER);
		
	}
}
