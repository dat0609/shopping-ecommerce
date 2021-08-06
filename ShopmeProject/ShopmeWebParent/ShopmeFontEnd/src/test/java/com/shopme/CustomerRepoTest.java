package com.shopme;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.customer.CustomerRepository;
import com.shopme.entity.Country;
import com.shopme.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepoTest {


	@Autowired private CustomerRepository repo;
	@Autowired private TestEntityManager entityManager;

	@Test
	public void testCreateCustomer1() {
		Integer countryId = 234; // USA
		Country country = entityManager.find(Country.class, countryId);

		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Lala");
		customer.setLastName("Fountaine");
		customer.setPassword("password123");
		customer.setEmail("lalafountaine@gmail.com");
		customer.setPhoneNumber("312-462-7518");
		customer.setAddressLine1("1927  West Drive");
		customer.setCity("Sacramento");
		customer.setState("California");
		customer.setPostalCode("95867");
		customer.setCreatedTime(new Date());

		Customer savedCustomer = repo.save(customer);

		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindByEmail() {
		String email = "david.s.fountaine@gmail.com";
		Customer customer = repo.findByEmail(email);

		assertThat(customer).isNotNull();
		System.out.println(customer);		
	}

}
