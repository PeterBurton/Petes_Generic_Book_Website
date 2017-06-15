package ie.cit.oossp.ui;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

import ie.cit.oossp.domain.PersonEditor;
import ie.cit.oossp.repository.PersonRepository;

import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
@SpringView(name = LoginPage.VIEW_NAME)
public class LoginPage extends VerticalLayout implements View {
	public static final String VIEW_NAME = "";

	PersonEditor pe;
	CssLayout actions;
	VerticalLayout v;

	@Autowired
	PersonRepository peopleRepo;

	@PostConstruct
	void init() {

		v = new VerticalLayout();
		Label loginTitle = new Label("Login Page");
		loginTitle.setStyleName("h2");
		loginTitle.setWidthUndefined();
		TextField username = new TextField("Username");
		PasswordField password = new PasswordField("Password");

		Button register = new Button("Register");
		register.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				pe = new PersonEditor(peopleRepo);
				pe.addCloseListener( new Window.CloseListener() {
					public void windowClose(CloseEvent e) {
						Notification.show("You registered successfully");
					}
				});
				UI.getCurrent().addWindow(pe);
			}
		});

		Button login = new Button("Login");
		login.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if((username.getValue().equals("p.burton@mycit.ie")) && (password.getValue().equals("password"))){
					Notification.show("You logged in, good for you!");
					getUI().getNavigator().navigateTo(UserPage.VIEW_NAME);
				}
				else if((username.getValue().equals("admin")) && (password.getValue().equals("password"))){
					Notification.show("You logged in, good for you!");
					getUI().getNavigator().navigateTo(AdminPage.VIEW_NAME);
				}
				else{
					Notification.show("Invalid credentials", Notification.Type.ERROR_MESSAGE);
				}

			}

		});
		actions =  new CssLayout(register,login);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		Label defaultUser = new Label("Default user is 'p.burton@mycit.ie', default admin username is 'admin'.\nPassword for both is 'password'");
		defaultUser.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		v.addComponents(loginTitle, username, password, actions, defaultUser);
		v.setComponentAlignment(loginTitle,  Alignment.MIDDLE_CENTER);
		v.setComponentAlignment(username, Alignment.MIDDLE_CENTER);
		v.setComponentAlignment(password, Alignment.MIDDLE_CENTER);
		v.setComponentAlignment(actions, Alignment.MIDDLE_CENTER);
		v.setComponentAlignment(defaultUser, Alignment.MIDDLE_CENTER);
		v.setSpacing(true);
		addComponent(v);



	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
}