package markbook.views.login;

import java.util.*;
import java.io.*;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.Router;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.startup.RouteRegistryInitializer;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import markbook.views.studentform.StudentForm;

@Route(value = "") 
@PageTitle("Log In | Markbook")
@Theme(themeFolder = "myapp", variant = Lumo.DARK)
public class LogIn extends VerticalLayout { 

	public LogIn() {
	    VerticalLayout layout = new VerticalLayout();
	    HorizontalLayout hl = new HorizontalLayout();
	    TextField username = new TextField();
	    PasswordField password = new PasswordField();
	    Button submit = new Button("Log in");
	    Image logo = new Image("images/pdsb-logo.png", "logo.png");	    
	    
	    username.setLabel("Username");
	    password.setLabel("Password");
	    submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
	    submit.addClickShortcut(Key.ENTER);
	    
	    username.setPlaceholder("Username");
	    password.setPlaceholder("Password");
	    username.setRequiredIndicatorVisible(true);
	    password.setRequiredIndicatorVisible(true);
	    
	    logo.setWidth("300px");
	    username.setWidth("300px");
	    password.setWidth("300px");
	    submit.setWidth("300px");
	    
	    submit.addClickListener(login -> {	
	    	
	    	// check if login details are correct
	    	try {
	    		File file = new File("ld.txt");
	    		Scanner read = new Scanner(file);
	    		String[] log = read.nextLine().split(",");
	    		
	    		if (log[0].equals(username.getValue())&&log[1].equals(password.getValue())) {
	    			submit.getUI().ifPresent(ui ->
	    	           ui.navigate("studentform"));
	    		// change password admin login
	    		} else if (username.getValue().equals("admin")&& password.getValue().equals("adminview")){
	    			submit.getUI().ifPresent(ui ->
	    	           ui.navigate("changepassword"));
	    		} else {
	    			Notification n = Notification.show("Invalid username or password. If you're having trouble logging in, please contact genos02.ca@gmail.com for further assistance.");
					n.addThemeVariants(NotificationVariant.LUMO_ERROR);
					n.setPosition(Notification.Position.BOTTOM_END);
					
	    		}
	    		read.close();
	    	} catch (IOException e) {
	    		e.getStackTrace();
	    	}
	    });
	    
	    
	    // add stuff to login view screen
	    layout.add(logo,new H2("Log in"), username, password, submit);
	    hl.add(layout);
	    hl.setAlignItems(FlexComponent.Alignment.CENTER);
	    
	    add(hl);
	    setAlignItems(FlexComponent.Alignment.CENTER);
	}
}