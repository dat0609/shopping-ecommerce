package com.shopme.shipping;

import org.springframework.data.repository.CrudRepository;

import com.shopme.entity.Country;
import com.shopme.entity.ShippingRate;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer>{

	public ShippingRate findByCountryAndState(Country country, String state);	
}
