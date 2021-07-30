package com.shopme.admin.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.shopme.entity.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

	@Query("Select u From User u Where u.email = :email")
	public User getUserByEmail(@Param("email")String email);
	
	@Query("Update User u Set u.enabled = ?2 Where u.id = ?1")
	@Modifying
	public void enableUser(Integer id,boolean enable);
	
	@Query("Select u From User u Where u.firstName LIKE %?1% or u.lastName LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);
	
	public Long countById(int id);
}
