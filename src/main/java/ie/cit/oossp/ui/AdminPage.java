package ie.cit.oossp.ui;

import java.io.File;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

import ie.cit.oossp.domain.Book;
import ie.cit.oossp.domain.BookEditor;
import ie.cit.oossp.domain.Person;
import ie.cit.oossp.domain.PersonEditor;
import ie.cit.oossp.repository.BookRepository;
import ie.cit.oossp.repository.PersonRepository;

@SuppressWarnings({ "deprecation", "serial" })
@SpringView(name = AdminPage.VIEW_NAME)
public class AdminPage extends VerticalLayout implements View{
	public static final String VIEW_NAME = "admin";

	@Autowired
	BookRepository bookRepo;

	@Autowired
	PersonRepository peopleRepo;

	Grid<Book> bookGrid;
	Grid<Person> personGrid;
	HorizontalLayout h1, h2;
	VerticalLayout v1, vButtons, vBooks;
	Button delButton;
	Button addButton;
	Button delPersonButton;
	Button addPersonButton;
	PersonEditor pe;
	BookEditor be;
	Image bookCover;

	@PostConstruct
	void init() {
		
		this.bookGrid = new Grid<>();
		this.personGrid = new Grid<>(Person.class);
		this.h1 = new HorizontalLayout();
		this.h2 = new HorizontalLayout();
		this.v1 = new VerticalLayout();
		this.vButtons = new VerticalLayout();
		this.vBooks = new VerticalLayout();
		this.delButton = new Button("");
		delButton.setIcon(FontAwesome.MINUS_SQUARE);
		delButton.addStyleName(ValoTheme.BUTTON_DANGER);
		this.addButton = new Button("");
		addButton.setIcon(FontAwesome.PLUS_SQUARE);
		addButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		this.delPersonButton = new Button("");
		delPersonButton.setIcon(FontAwesome.MINUS_SQUARE);
		delPersonButton.addStyleName(ValoTheme.BUTTON_DANGER);
		this.addPersonButton = new Button("");
		addPersonButton.setIcon(FontAwesome.PLUS_SQUARE);
		addPersonButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

		//setup the image as the stock no book selected image
		ExternalResource er = new ExternalResource("img/noBook.png");
		bookCover = new Image("No book image", er);

		List<Book> books = bookRepo.findAll();
		bookGrid.setItems(books);
		bookGrid.addColumn(Book::getIsbn).setCaption("ISBN");
		bookGrid.addColumn(Book::getTitle).setCaption("Title");
		bookGrid.addColumn(Book::getAuthor).setCaption("Author");
		delButton.setEnabled(false);

		bookGrid.setSelectionMode(SelectionMode.SINGLE);
		bookGrid.addSelectionListener(event ->{
			Set<Book> selected = event.getAllSelectedItems();
			delButton.setEnabled(selected.size() > 0);
			for (Book b: selected) {
				Notification.show(b.toString());
				ExternalResource resource = new ExternalResource("img/" + b.getIsbn() +".jpg");
				File f = new File("src\\main\\resources\\static\\img\\" + b.getIsbn() +".jpg");
				if(f.exists()){
					bookCover = new Image(b.getTitle(), resource);
				}
				else{
					bookCover = new Image("No book image", er);
				}
				h1.removeAllComponents();
				h1.setWidth("80%");
				h1.setSpacing(true);
				h1.addComponents(bookCover, bookGrid, vBooks);
				h1.setExpandRatio(bookGrid, 1);
			}
		});

		// Delete method 1
		delButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Set<Book> selected = bookGrid.getSelectedItems();
				for (Book b: selected) {
					bookRepo.delete(b.getIsbn());
				}
				bookGrid.setItems(bookRepo.findAll());
				Notification.show("Book Deleted");
			}
		});

		addButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				be = new BookEditor(bookRepo);
				be.addCloseListener( new Window.CloseListener() {
					public void windowClose(CloseEvent e) {
						bookGrid.setItems(bookRepo.findAll());
						bookCover = new Image("No book image", er);
					}
				});
				UI.getCurrent().addWindow(be);
			}
		});


		List<Person> people = peopleRepo.findAll();
		personGrid.setItems(people);
		delPersonButton.setEnabled(false);
		// Change the caption for a single column
		personGrid.getColumn("email").setCaption("EMAIL");
		// Rearrange columns by column name
		personGrid.setColumnOrder("firstName", "lastName", "email", "password");
		// Multi-selection
		personGrid.setSelectionMode(SelectionMode.MULTI);
		personGrid.addSelectionListener(event ->{
			Set<Person> selected = event.getAllSelectedItems();
			Notification.show(selected.size() + " items selected");
			delPersonButton.setEnabled(selected.size() > 0);
		});
		// Delete method 1
		delPersonButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Set<Person> selected = personGrid.getSelectedItems();
				for (Person p: selected) {
					peopleRepo.delete(p.getEmail());
				}
				personGrid.setItems(peopleRepo.findAll());
				Notification.show("Person deleted Succesfully");
			}
		});

		addPersonButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				pe = new PersonEditor(peopleRepo);
				pe.addCloseListener( new Window.CloseListener() {
					public void windowClose(CloseEvent e) {
						personGrid.setItems(peopleRepo.findAll());
						Notification.show("Person added successfully");
					}
				});
				UI.getCurrent().addWindow(pe);
			}
		});



		vBooks.addComponents(delButton, addButton);
		vBooks.setMargin(false);
		vBooks.setWidthUndefined();
		h1.setWidth("80%");
		h1.setSpacing(true);
		h1.addComponents(bookCover, bookGrid, vBooks);
		h1.setExpandRatio(bookGrid, 1);
		bookGrid.setWidth("100%");
		bookGrid.setHeightByRows(5);

		vButtons.addComponents(delPersonButton, addPersonButton);
		vButtons.setMargin(false);
		vButtons.setWidthUndefined();
		h2.setWidth("80%");
		h2.setSpacing(true);
		h2.addComponents(personGrid, vButtons);
		h2.setExpandRatio(personGrid, 1);
		personGrid.setWidth("100%");
		personGrid.setHeightByRows(5);
		
		Label adminTitle = new Label("Administration Page");
		adminTitle.setStyleName("h2");
		adminTitle.setWidthUndefined();
		adminTitle.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		Label addDeleteBooks = new Label("Add or delete books from the database");
		addDeleteBooks.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		Label addDeletePeople = new Label("Add or delete users from the database");
		addDeletePeople.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		v1.addComponents(adminTitle,addDeleteBooks, h1, addDeletePeople, h2);
		v1.setComponentAlignment(adminTitle,  Alignment.TOP_CENTER);
		v1.setComponentAlignment(addDeleteBooks,  Alignment.TOP_CENTER);
		v1.setComponentAlignment(h1, Alignment.TOP_CENTER);
		v1.setComponentAlignment(addDeletePeople, Alignment.MIDDLE_CENTER);
		v1.setComponentAlignment(h2, Alignment.MIDDLE_CENTER);
		v1.setSpacing(true);
		v1.setMargin(false);
		addComponent(v1);

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
