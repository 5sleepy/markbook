package markbook.views.markschemeform;

import java.io.*;
import java.util.*;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import markbook.views.MainLayout;

@Route(value = "markschemeform", layout = MainLayout.class) 
@PageTitle("Markscheme Form | Markbook")
public class MarkschemeForm extends VerticalLayout {
	public MarkschemeForm() {
		VerticalLayout vl = new VerticalLayout();
		HorizontalLayout hl = new HorizontalLayout();
		FormLayout layout = new FormLayout();
		NumberField unit = new NumberField(), seven = new NumberField(), six = new NumberField(), five = new NumberField(), four = new NumberField(), three = new NumberField(), two = new NumberField(), one = new NumberField();
		Select<String> paper = new Select<>();
		Button submit = new Button("Save"), clearB = new Button("Clear boundaries"), clearA = new Button("Clear all");
		H2 label = new H2("Test Boundaries");
		
		submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		clearB.addThemeVariants(ButtonVariant.LUMO_ERROR);
		unit.setPlaceholder("ie. 1, 2, 3, 4, ...");
		
		
		paper.setLabel("Assessment");
		paper.setItems("Paper 1","Paper 2","Paper 3","Other");
		paper.setValue("Paper 1");
		
		unit.setLabel("Unit");
		seven.setLabel("Seven");
		six.setLabel("Six");
		five.setLabel("Five");
		four.setLabel("Four");
		three.setLabel("Three");
		two.setLabel("Two");
		one.setLabel("One");
		
		submit.addClickListener(click -> {

			// if any fields are empty
			if (seven.getValue()==null||six.getValue()==null||five.getValue()==null||four.getValue()==null||three.getValue()==null||two.getValue()==null||one.getValue()==null||unit.getValue()==null) {
				// raise a notification to let user know there are empty fields
				Notification n = Notification.show("Please fill in all fields.");
				n.addThemeVariants(NotificationVariant.LUMO_ERROR);
				n.setPosition(Notification.Position.BOTTOM_END);
			
			// if any upper bounds of higher levels are less than upper bounds of lower levels
			} else if (seven.getValue()<=six.getValue()||six.getValue()<=five.getValue()||five.getValue()<=four.getValue()||four.getValue()<=three.getValue()||three.getValue()<=two.getValue()||two.getValue()<=one.getValue()) {
				// raise a notification to let user know that their bounds are invalid
				Notification n = Notification.show("Please make all upper bounds greater than those below it.");
				n.addThemeVariants(NotificationVariant.LUMO_ERROR);
				n.setPosition(Notification.Position.BOTTOM_END);
			} else {
				File f = new File("Tests.txt");
				
				// convert to stored version
				int p = 0;
				if (paper.getValue().equals("Paper 1")) {
					p=1;
				} else if (paper.getValue().equals("Paper 2")) {
					p=2;
				} else if (paper.getValue().equals("Paper 3")) {
					p=3;
				} else {
					p=4;
				}
				try {
					
					Scanner in = new Scanner(f);
					ArrayList<String> lines = new ArrayList<String>();
					
					// read all markschemes in that aren't the same unit and paper as the one being created
					while(in.hasNextLine()) {
						String a = in.nextLine();
						if (Integer.parseInt(a.split(",")[0])==unit.getValue().intValue()&&Integer.parseInt(a.split(",")[1])==p) {
							
						} else {
							lines.add(a);
						}
					}
					
					in.close();
					
					// add the new test
					lines.add(String.format("%s,%s,%s %s %s %s %s %s %s",unit.getValue().intValue(), p, seven.getValue().intValue(), six.getValue().intValue(), five.getValue().intValue(), four.getValue().intValue(), three.getValue().intValue(), two.getValue().intValue(), one.getValue().intValue()));
					
					FileWriter write = new FileWriter(f, false);
					
					// write all tests back into file
					for (String a : lines) {
						write.write(a+"\n");
					}
					write.close();
				} catch (IOException e) {
					e.getStackTrace();
				}
				
				// reset fields
				seven.setValue(null);
				six.setValue(null);
				five.setValue(null);
				four.setValue(null);
				three.setValue(null);
				two.setValue(null);
				one.setValue(null);
				
				
				
				Notification n = Notification.show("Test created!");
				n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				n.setPosition(Notification.Position.BOTTOM_END);
			}
		});
		
		submit.addClickShortcut(Key.ENTER);
		
		// clear all fields
		clearA.addClickListener(click -> {
			unit.setValue(null);
			seven.setValue(null);
			six.setValue(null);
			five.setValue(null);
			four.setValue(null);
			three.setValue(null);
			two.setValue(null);
			one.setValue(null);
			Notification n = Notification.show("All fields cleared.");
			n.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
			n.setPosition(Notification.Position.BOTTOM_END);
		});
		
		// clear all bounds
		clearB.addClickListener(click -> {
			seven.setValue(null);
			six.setValue(null);
			five.setValue(null);
			four.setValue(null);
			three.setValue(null);
			two.setValue(null);
			one.setValue(null);
			Notification n = Notification.show("Boundaries Cleared.");
			n.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
			n.setPosition(Notification.Position.BOTTOM_END);
		});
		
		layout.add(label,
				   unit, paper,
				   seven, six, five, four,
				   three, two, one, clearB,
				   submit, clearA);
		
		layout.setColspan(label, 4);
		layout.setColspan(unit, 2);
		layout.setColspan(paper, 2);
		layout.setColspan(submit, 2);
		layout.setColspan(clearA, 2);
		
		layout.setResponsiveSteps(
				new ResponsiveStep("0", 1),
				new ResponsiveStep("400px", 4));
		
		layout.setWidth("800px");
		
		hl.add(layout);
		hl.setAlignItems(FlexComponent.Alignment.CENTER);
		
		add(hl);
		setAlignItems(FlexComponent.Alignment.CENTER);
	}
}
