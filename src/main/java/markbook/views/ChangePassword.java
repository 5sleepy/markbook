package markbook.views;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Route(value = "changepassword") 
@PageTitle("Change Password | Markbook")
@Theme(themeFolder = "myapp", variant = Lumo.DARK)
public class ChangePassword extends VerticalLayout {
	public ChangePassword() {
		VerticalLayout layout = new VerticalLayout();
		TextField newID = new TextField();
		PasswordField newP = new PasswordField(), confirmP = new PasswordField();
		Button save = new Button("Save");
		
		newID.setLabel("New Username");
		newP.setLabel("New password");
		confirmP.setLabel("Confirm new password");
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		newID.setPlaceholder("Username");
		newP.setPlaceholder("New password");
		confirmP.setPlaceholder("Confirm new password");
		save.addClickShortcut(Key.ENTER);
		
		// must fill
		newID.setRequiredIndicatorVisible(true);
		newP.setRequiredIndicatorVisible(true);
		confirmP.setRequiredIndicatorVisible(true);
		
		newID.setWidth("300px");
		newP.setWidth("300px");
		confirmP.setWidth("300px");
		save.setWidth("300px");
		
		save.addClickListener(click -> {
			if (newP.getValue().equals(confirmP.getValue())) { // if both passwords are the same
				try {
					File f = new File("ld.txt");
					Writer w = new FileWriter(f, false);
					
					// overwrite previous username and password
					w.write(newID.getValue()+","+confirmP.getValue());
					w.close();
					Notification n = Notification.show("Success!");
					n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					n.setPosition(Notification.Position.BOTTOM_END);
					
					// go to login page
					save.getUI().ifPresent(ui ->
	    	           ui.navigate(""));
				} catch (IOException e) {
					e.getStackTrace();
				}
			} else {
				Notification n = Notification.show("Invalid fields.");
				n.addThemeVariants(NotificationVariant.LUMO_ERROR);
				n.setPosition(Notification.Position.BOTTOM_END);
			}
		});
		
		add(new H2("Change Password"), newID, newP, confirmP, save);
		setAlignItems(FlexComponent.Alignment.CENTER);
	}
}
