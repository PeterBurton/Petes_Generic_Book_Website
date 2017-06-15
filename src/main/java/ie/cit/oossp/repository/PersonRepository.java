package ie.cit.oossp.repository;

import java.util.List;

import ie.cit.oossp.domain.Person;

public interface PersonRepository {
	
	List<Person> findAll();
	
	void delete(String email);
	
	void add(Person person);
}
