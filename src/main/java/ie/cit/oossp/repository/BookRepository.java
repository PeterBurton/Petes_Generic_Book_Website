package ie.cit.oossp.repository;

import java.util.List;

import ie.cit.oossp.domain.Book;

public interface BookRepository {

	List<Book> findAll();
	
	void delete(String isbn);

	void add(Book book);

	List<Book> findMyBooks();

	String getStars(String isbn);
	
	void changeRating(String isbn, String email, int num);

	void addRating(String isbn);
	
}
