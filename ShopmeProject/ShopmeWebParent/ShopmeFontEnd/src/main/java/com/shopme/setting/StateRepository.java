package com.shopme.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shopme.entity.Country;
import com.shopme.entity.State;

public interface StateRepository extends CrudRepository<State, Integer>{

	List<State> findByCountryOrderByNameAsc(Country country);

}
