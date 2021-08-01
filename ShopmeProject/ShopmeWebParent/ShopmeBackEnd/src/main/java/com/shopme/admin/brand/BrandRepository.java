package com.shopme.admin.brand;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.entity.Brand;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer>{

	public Long countById(Integer id);
}
