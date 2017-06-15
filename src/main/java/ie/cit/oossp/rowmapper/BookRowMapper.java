package ie.cit.oossp.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ie.cit.oossp.domain.Book;

public class BookRowMapper implements RowMapper<Book> {

	@Override
	public Book mapRow(ResultSet rs, int i) throws SQLException {
		return(new Book(rs.getString("isbn"), rs.getString("title"), rs.getString("author")));	
	}
	
}
