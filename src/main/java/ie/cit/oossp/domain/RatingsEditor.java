package ie.cit.oossp.domain;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import ie.cit.oossp.repository.BookRepository;

@SuppressWarnings({ "deprecation", "serial" })
@SpringComponent
@UIScope
public class RatingsEditor extends Window{

	private final BookRepository bookRepo;
	VerticalLayout v;
	Label title;
	TextField rating;
	Button save;
	Button cancel;
	CssLayout actions;
	String isbn;

	public RatingsEditor(BookRepository bRepo, String isbn){

		super("Rate this book");
		center();
		setClosable(false);

		this.bookRepo = bRepo;
		this.isbn = isbn;

		this.title = new Label("Title:" );


		this.rating = new TextField("Enter 1-5");
		rating.addValueChangeListener(event ->
		Notification.show("Value is: " + event.getValue()));


		this.save = new Button("Save", FontAwesome.SAVE);
		this.cancel = new Button("Cancel",  event -> close());
		this.actions = new CssLayout(save, cancel);

		this.v = new VerticalLayout();
		v.addComponents(title, rating, actions);
		v.setSpacing(true);
		setContent(v);

		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		save.addClickListener(event -> {
			try{
				String r = rating.getValue();
				int num = Integer.parseInt(r);
				if ((num > 0) && (num < 6)){
					bookRepo.changeRating(isbn, "p,burton@mycit.ie", num);	
					close();
					Notification.show("Rating changed");
				}
				else{
					Notification.show("Choose a number between 1 and 5", Notification.Type.ERROR_MESSAGE);
				}
			}
			catch(Exception e){
				Notification.show("Choose a number between 1 and 5", Notification.Type.ERROR_MESSAGE);
			}
		});


	}
}
