package com.shopme.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopme.entity.Address;
import com.shopme.entity.Customer;

@Service
@Transactional
public class AddressService {

	@Autowired
	AddressRepository addressRepository;

	public List<Address> listAddressBook(Customer customer) {
		return addressRepository.findByCustomer(customer);
	}
	
	public void save(Address address) {
		addressRepository.save(address);
	}

	public Address get(Integer addressId, Integer customerId) {
		return addressRepository.findByIdAndCustomer(addressId, customerId);
	}

	public void delete(Integer addressId, Integer customerId) {
		addressRepository.deleteByIdAndCustomer(addressId, customerId);
	}
	
	public void setDefaultAddress(Integer defaultAddressId, Integer customerId) {
		addressRepository.setDefaultAddress(defaultAddressId);
		addressRepository.setNonDefaultAddress(defaultAddressId, customerId);
	}
	
	public Address getDefaultAddress(Customer customer) {
		return addressRepository.findDefaultByCustomer(customer.getId());
	}
}
