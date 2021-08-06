package com.shopme.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shopme.entity.Country;

public interface CountryRepository extends CrudRepository<Country, Integer>{
	
	public List<Country> findAllByOrderByNameAsc();
}
