package ie.cit.oossp.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import ie.cit.oossp.repository.PersonRepository;

@SuppressWarnings({ "deprecation", "serial" })
@SpringComponent
@UIScope
public class PersonEditor extends Window {

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
			Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	private final PersonRepository peopleRepo;
	private Person person;
	VerticalLayout v;
	TextField firstName;
	TextField lastName;
	TextField email;
	TextField password;
	Button save;
	Button cancel;
	CssLayout actions;

	public PersonEditor(PersonRepository pRepo){

		super("Add a Person");
		center();
		setClosable(false);

		this.peopleRepo = pRepo;
		this.firstName = new TextField("First name");
		firstName.addValueChangeListener(event ->
		Notification.show("Value is: " + event.getValue()));
		this.lastName = new TextField("Last name");
		lastName.addValueChangeListener(event ->
		Notification.show("Value is: " + event.getValue()));
		this.email = new TextField("Email");
		email.addValueChangeListener(event ->
		Notification.show("Value is: " + event.getValue()));
		this.password = new TextField("Password");
		password.addValueChangeListener(event ->
		Notification.show("Value is: " + event.getValue()));
		this.save = new Button("Save", FontAwesome.SAVE);
		this.cancel = new Button("Cancel",  event -> close());
		this.actions = new CssLayout(save, cancel);
		this.v = new VerticalLayout();
		v.addComponents(firstName, lastName, email, password, actions);
		v.setSpacing(true);
		setContent(v);

		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);


		save.addClickListener(event -> {
			try{
				String fn = this.firstName.getValue();
				String ln = this.lastName.getValue();
				String e = this.email.getValue();
				String p = this.password.getValue();
				if (validate(e)){
					person = new Person(fn, ln, e, p);
					System.out.println(person.toString());
					peopleRepo.add(person);	
					close();
				}
				else{
					Notification.show("Please enter a valid email address", Notification.Type.ERROR_MESSAGE);
				}
			}
			catch(Exception e){
				Notification.show("This email address is already in use on this site", Notification.Type.ERROR_MESSAGE);
			}
		});
	}

	

	public static boolean validate(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
		return matcher.find();
	}






}
