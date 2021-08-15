package com.shopme.address;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.entity.Address;
import com.shopme.entity.Customer;

public interface AddressRepository extends CrudRepository<Address, Integer>{

	public List<Address> findByCustomer(Customer customer);

	@Query("SELECT a FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
	public Address findByIdAndCustomer(Integer addressId, Integer customerId);

	@Query("DELETE FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
	@Modifying
	public void deleteByIdAndCustomer(Integer addressId, Integer customerId);	
	
	@Query("UPDATE Address a Set a.defaultForShipping = true WHERE a.id = ?1")
	@Modifying
	public void setDefaultAddress(Integer id);
	
	@Query("UPDATE Address a Set a.defaultForShipping = false WHERE a.id != ?1 AND a.customer.id = ?2")
	@Modifying
	public void setNonDefaultAddress(Integer defaultAddressId,int customerId);
	
	@Query("Select a From Address a Where a.customer.id = ?1 And a.defaultForShipping = true")
	public Address findDefaultByCustomer(int customerId);
}
