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
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import ie.cit.oossp.domain.Book;
import ie.cit.oossp.domain.BookEditor;
import ie.cit.oossp.domain.RatingsEditor;
import ie.cit.oossp.repository.BookRepository;


@SuppressWarnings({ "deprecation", "serial" })
@SpringView(name = UserPage.VIEW_NAME)
public class UserPage extends VerticalLayout implements View{
	public static final String VIEW_NAME = "user";

	@Autowired
	BookRepository bookRepo;

	Grid<Book> bookGrid;
	HorizontalLayout h1;
	VerticalLayout v1,vBooks, v2;
	Button delButton;
	Button addButton;
	BookEditor be;
	RatingsEditor re;
	Image bookCover;

	@PostConstruct
	void init() {
		this.bookGrid = new Grid<>();
		this.h1 = new HorizontalLayout();
		this.v1 = new VerticalLayout();
		this.v2 = new VerticalLayout();
		this.vBooks = new VerticalLayout();
		this.delButton = new Button("");
		delButton.setIcon(FontAwesome.MINUS_SQUARE);
		delButton.addStyleName(ValoTheme.BUTTON_DANGER);
		this.addButton = new Button("");
		addButton.setIcon(FontAwesome.PLUS_SQUARE);
		addButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

		//setup the image as the stock no book selected image
		ExternalResource er = new ExternalResource("img/noBook.png");
		bookCover = new Image("No book image", er);
		Label rating = new Label("");
		Label stars = new Label("");
		Button changeRating = new Button("Change Rating");
		List<Book> books = bookRepo.findMyBooks();
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
				ExternalResource resource = new ExternalResource("img/" + b.getIsbn() +".jpg");
				File f = new File("src\\main\\resources\\static\\img\\" + b.getIsbn() +".jpg");
				if(f.exists()){
					bookCover = new Image(b.getTitle(), resource);
				}
				else{
					bookCover = new Image("No book image", er);
				}
				rating.setCaption("YourRating");
				stars.setCaption(bookRepo.getStars(b.getIsbn()));
				v2.removeAllComponents();
				v2.addComponents(bookCover, rating, stars, changeRating);
				v2.setMargin(false);
				v2.setWidthUndefined();
				h1.removeAllComponents();
				h1.setWidth("80%");
				h1.setSpacing(true);
				h1.addComponents(v2, bookGrid, vBooks);
				h1.setExpandRatio(bookGrid, 1);

			}
		});

		changeRating.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Set<Book> selected = bookGrid.getSelectedItems();
				for (Book b: selected) {
					re = new RatingsEditor(bookRepo, b.getIsbn());
					re.addCloseListener( new Window.CloseListener() {
						public void windowClose(CloseEvent e) {
							stars.setCaption(bookRepo.getStars(b.getIsbn()));
							bookGrid.setItems(bookRepo.findMyBooks());
						}
					});
				}
				UI.getCurrent().addWindow(re);
			}
		});



		delButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Set<Book> selected = bookGrid.getSelectedItems();
				for (Book b: selected) {
					bookRepo.delete(b.getIsbn());
				}
				bookGrid.setItems(bookRepo.findMyBooks());
				Notification.show("Book Deleted");
			}
		});

		addButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				be = new BookEditor(bookRepo);
				be.addCloseListener( new Window.CloseListener() {
					public void windowClose(CloseEvent e) {
						bookGrid.setItems(bookRepo.findMyBooks());
					}
				});
				UI.getCurrent().addWindow(be);
			}
		});
		vBooks.addComponents(delButton, addButton);
		vBooks.setMargin(false);
		vBooks.setWidthUndefined();
		v2.addComponents(bookCover, rating, stars);
		v2.setMargin(false);
		v2.setWidthUndefined();
		h1.setWidth("80%");
		h1.setSpacing(true);
		h1.addComponents(v2, bookGrid, vBooks);
		h1.setExpandRatio(bookGrid, 1);
		bookGrid.setWidth("100%");
		bookGrid.setHeightByRows(10);

		Label pageTitle = new Label("Peter's Page");
		pageTitle.setStyleName("h2");
		pageTitle.setWidthUndefined();
		pageTitle.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		Label myBooks = new Label("All my Books");
		myBooks.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		v1.addComponents(pageTitle,myBooks, h1);
		v1.setComponentAlignment(pageTitle,  Alignment.TOP_CENTER);
		v1.setComponentAlignment(myBooks,  Alignment.TOP_CENTER);
		v1.setComponentAlignment(h1, Alignment.TOP_CENTER);
		v1.setSpacing(true);
		v1.setMargin(false);
		addComponent(v1);


	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
}


