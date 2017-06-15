package ie.cit.oossp.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ie.cit.oossp.domain.Book;
import ie.cit.oossp.rowmapper.BookRowMapper;

@Repository
public class JdbcBookRepository implements BookRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Book> findAll() {
		String sql = "SELECT * FROM books";
		return jdbcTemplate.query(sql, new BookRowMapper());
	}

	@Override
	public List<Book> findMyBooks() {
		String sql = "SELECT * FROM books WHERE isbn IN (SELECT isbn FROM ratings WHERE email='p.burton@mycit.ie')";
		return jdbcTemplate.query(sql, new BookRowMapper());
	}
	
	@Override
	public void delete(String isbn) {
		String sql = "DELETE FROM books WHERE isbn = ?";
		jdbcTemplate.update(sql, isbn);
	}
	
	@Override
	public void add(Book book) {
		String isbn = book.getIsbn();
		String title = book.getTitle();
		String author = book.getAuthor();
		String sql = "INSERT INTO books (isbn, title, author) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] {isbn, title, author});
	}

	@Override
	public String getStars(String isbn) {
		String stars = "";
		String sql = "SELECT rating FROM ratings WHERE isbn = ?";
		//jdbcTemplate.queryForObject(sql, isbn, String.class);
		int num =  jdbcTemplate.queryForObject(sql, Integer.class, isbn);
		for (int i = 1; i <= num; i++ ){
			stars += "*";
		}
		return stars;
	}

	@Override
	public void changeRating(String isbn, String email, int rating) {
		String sql = "UPDATE ratings SET rating = ? WHERE isbn=?";
		jdbcTemplate.update(sql, rating, isbn);
		
	}

	@Override
	public void addRating(String isbn) {
		String sql ="INSERT INTO ratings (isbn, email, rating) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] {isbn, "p.burton@mycit.ie", 1});
		
	}
}
