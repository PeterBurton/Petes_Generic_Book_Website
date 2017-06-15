package ie.cit.oossp.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ie.cit.oossp.domain.Person;
import ie.cit.oossp.rowmapper.PersonRowMapper;

@Repository
public class JdbcPersonRepository implements PersonRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Person> findAll() {
		String sql = "SELECT * FROM people";
		return jdbcTemplate.query(sql, new PersonRowMapper());
	}

	@Override
	public void delete(String email) {
		String sql = "DELETE FROM people WHERE email = ?";
		jdbcTemplate.update(sql, email);
	}

	@Override
	public void add(Person person) {
		String firstName = person.getFirstName();
		String lastName = person.getLastName();
		String email = person.getEmail();
		String password = person.getPassword();
		/*jdbcTemplate.update(
			    "INSERT INTO people (firstName, lastName, email, password) VALUES (?, ?, ?, ?)",
			    new Object[]{firstName, lastName, email, password}, new Object[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR}
			);*/
		String sql = "INSERT INTO people (firstName, lastName, email, password) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] { firstName, lastName, email, password});
		
		
		

	}

}
