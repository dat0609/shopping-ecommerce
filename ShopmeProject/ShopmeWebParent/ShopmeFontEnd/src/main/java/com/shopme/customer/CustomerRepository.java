package com.shopme.customer;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.entity.AuthenticationType;
import com.shopme.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer , Integer>{

	public Customer findByEmail(String email);

	public Customer findByVerificationCode(String code);

	@Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = null WHERE c.id = ?1")
	@Modifying
	public void enable(Integer id);	
	
	@Query("Update Customer c Set c.authenticationType = ?2 Where c.id = ?1")
	@Modifying
	public void updateAuthenticationType(int customerId, AuthenticationType type);

	Customer findByResetPasswordToken(String resetPasswordToken);
}
