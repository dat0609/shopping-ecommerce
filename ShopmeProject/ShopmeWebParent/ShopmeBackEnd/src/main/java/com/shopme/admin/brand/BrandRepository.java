package com.shopme.admin.brand;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.entity.Brand;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer>{

	public Long countById(Integer id);
	
	@Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
	public List<Brand> findAllBrands();
}
