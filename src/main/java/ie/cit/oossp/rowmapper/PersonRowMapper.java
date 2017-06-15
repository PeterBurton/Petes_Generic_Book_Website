package ie.cit.oossp.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ie.cit.oossp.domain.Person;

public class PersonRowMapper implements RowMapper<Person> {

	@Override
	public Person mapRow(ResultSet rs, int i) throws SQLException {
		return new Person(rs.getString("firstName"),
				rs.getString("lastName"),
				rs.getString("email"),
				rs.getString("password"));
	}

}
