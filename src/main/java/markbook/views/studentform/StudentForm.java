package markbook.views.studentform;

import com.vaadin.flow.component.Key;

import markbook.classes.Student;
import markbook.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

@Route(value = "studentform", layout = MainLayout.class) 
@PageTitle("Student Form | Markbook")

public class StudentForm extends VerticalLayout{
	public StudentForm() {
		VerticalLayout vl = new VerticalLayout();
		HorizontalLayout hl = new HorizontalLayout();
		FormLayout layout = new FormLayout();
		Button addStudent = new Button("Save");
		Button clearFields = new Button("Clear");
		TextField name = new TextField();
		H2 label = new H2("Student Information");
		name.setLabel("Full Name");
		
		NumberField studentNum = new NumberField();
		studentNum.setLabel("Student Number");
		
		// confirm button
		addStudent.addClickListener(click -> {
			// checks that student number is six digits
			if (studentNum.getValue()!= null&&(studentNum.getValue()<=999999&&studentNum.getValue()>99999&&!"".equals(name.getValue()))) {
				// create new student and write details to file
				Student student = new Student(studentNum.getValue().intValue(), name.getValue());
				student.writeToFile();
				
				// clear fields
				name.setValue("");
				studentNum.setValue(null);
				
				// inform user of successful operation
				Notification n = Notification.show("Student added!");
				n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				n.setPosition(Notification.Position.BOTTOM_END);
				
			// if student number isn't six digits raise error to inform user
			} else {
				Notification n = Notification.show("Invalid student number or name.");
				n.addThemeVariants(NotificationVariant.LUMO_ERROR);
				n.setPosition(Notification.Position.BOTTOM_END);
			}
			
		});
		addStudent.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		// clear fields button
		clearFields.addClickListener(click -> {
			// clear fieldss
			name.setValue("");
			studentNum.setValue(null);
			
			// inform user of cleared fields
			Notification n = Notification.show("Fields cleared.");
			n.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
			n.setPosition(Notification.Position.BOTTOM_END);
			
		});
		
		addStudent.addClickShortcut(Key.ENTER);
		
		studentNum.setWidth("400px");
		addStudent.setWidth("200px");
		clearFields.setWidth("200px");
		name.setWidth("400px");
		
		
		layout.add(label,
				   name, studentNum,
				   addStudent, clearFields);
		
		layout.setResponsiveSteps(
				new ResponsiveStep("0", 1),
				new ResponsiveStep("400px", 2));
		layout.setColspan(label, 2);
		
		
		layout.setWidth("800px");

		
		hl.add(layout);
		hl.setAlignItems(FlexComponent.Alignment.CENTER);
		add(hl);
		setAlignItems(FlexComponent.Alignment.CENTER);
	}
}
