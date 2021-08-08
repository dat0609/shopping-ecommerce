package com.shopme.shoppingcart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.Utility;
import com.shopme.customer.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import com.shopme.entity.Customer;

@RestController
public class ShoppingCartRestController {

	@Autowired
	ShoppingCartService cartService;
	
	@Autowired
	CustomerService customerService;
	
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addToCart(@PathVariable("productId")Integer productId, 
			HttpServletRequest request, @PathVariable("quantity")Integer quantity) {
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
			cartService.addProduct(productId, quantity, customer);
			
			return quantity + " added to your cart";
			
		} catch (CustomerNotFoundException e) {
			return "You must login to add product";
		} catch (ShoppingCartException e) {
			return e.getMessage();
		}
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		
		if (email == null) {
			throw new CustomerNotFoundException("No authenticated");
		}
		
		return customerService.getCustomerByEmail(email);
	}
	
}
