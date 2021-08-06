package com.shopme.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.entity.Country;
import com.shopme.entity.Customer;
import com.shopme.setting.CountryRepository;

@Service
public class CustomerService {

	@Autowired
	CountryRepository countryRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	public List<Country> listAllCountries() {
		return countryRepository.findAllByOrderByNameAsc();
	}
	
	public boolean isEmailUnique(String email) {
		Customer customer = customerRepository.findByEmail(email);
		return customer == null;
	}
}
