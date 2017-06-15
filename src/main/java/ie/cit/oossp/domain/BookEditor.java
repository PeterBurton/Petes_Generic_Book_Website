package ie.cit.oossp.domain;

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

import ie.cit.oossp.repository.BookRepository;

@SuppressWarnings({ "deprecation", "serial" })
@SpringComponent
@UIScope
public class BookEditor extends Window{

	private final BookRepository bookRepo;
	private Book book;
	VerticalLayout v;
	TextField isbn;
	TextField title;
	TextField author;
	Button save;
	Button cancel;
	CssLayout actions;

	public BookEditor(BookRepository bRepo){

		super("Add a Book");
		center();
		setClosable(false);

		this.bookRepo = bRepo;
		this.isbn = new TextField("ISBN");
		isbn.addValueChangeListener(event ->
		Notification.show("Value is: " + event.getValue()));
		this.title = new TextField("Title");
		title.addValueChangeListener(event ->
		Notification.show("Value is: " + event.getValue()));
		this.author = new TextField("Author");
		author.addValueChangeListener(event ->
		Notification.show("Value is: " + event.getValue()));
		this.save = new Button("Save", FontAwesome.SAVE);
		this.cancel = new Button("Cancel",  event -> close());
		this.actions = new CssLayout(save, cancel);
		this.v = new VerticalLayout();
		v.addComponents(isbn, title, author, actions);
		v.setSpacing(true);
		setContent(v);

		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		save.addClickListener(event -> {
			try{
				String i = this.isbn.getValue();
				String t = this.title.getValue();
				String a = this.author.getValue();
				book = new Book(i, t, a);
				int num = Integer.parseInt(i);
				if (num > 13){
					bookRepo.add(book);	
					bookRepo.addRating(i);
					close();
					Notification.show("Book added");
				}
				else{
					Notification.show("ISBN must less than 13 characters", Notification.Type.ERROR_MESSAGE);
				}
			}
			catch(Exception e){
				Notification.show("ISBN must less than 13 characters", Notification.Type.ERROR_MESSAGE);
			}
		});
	}
}
