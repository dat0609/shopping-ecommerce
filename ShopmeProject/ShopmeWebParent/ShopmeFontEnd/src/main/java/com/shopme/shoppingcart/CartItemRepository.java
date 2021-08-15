package com.shopme.shoppingcart;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.entity.CartItem;
import com.shopme.entity.Customer;
import com.shopme.entity.product.Product;

public interface CartItemRepository extends CrudRepository<CartItem, Integer>{

	public List<CartItem> findByCustomer(Customer customer);
	
	public CartItem findByCustomerAndProduct(Customer customer, Product product);
	
	@Query("Update CartItem c Set c.quantity = ?1 Where c.customer.id = ?2 And c.product.id = ?3")
	@Modifying
	public void updateQuantity(Integer quantity, Integer customerId, Integer productId);
	
	@Modifying
	@Query("Delete From CartItem c Where c.customer.id = ?1 And c.product.id = ?2")
	public void deleteByCustomerAndProduct(Integer customerId, Integer productId);
}
