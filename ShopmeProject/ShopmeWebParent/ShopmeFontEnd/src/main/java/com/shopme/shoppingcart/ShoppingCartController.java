package com.shopme.shoppingcart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.Utility;
import com.shopme.customer.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import com.shopme.entity.CartItem;
import com.shopme.entity.Customer;

@Controller
public class ShoppingCartController {

	@Autowired
	ShoppingCartService cartService;
	
	@Autowired
	CustomerService customerService;
	
	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest request) throws CustomerNotFoundException {
		Customer customer = getAuthenticatedCustomer(request);
		
		List<CartItem> listCartItems = cartService.listCartItems(customer);
		
		float estimatedTotal = 0.0F;
		for(CartItem item : listCartItems) {
			estimatedTotal += item.getSubtotal();
		}
		
		model.addAttribute("cartItems", listCartItems);
		model.addAttribute("estimatedTotal", estimatedTotal);
		
		return "cart/shopping_cart";
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		
		if (email == null) {
			throw new CustomerNotFoundException("No authenticated");
		}
		
		return customerService.getCustomerByEmail(email);
	}
	
}
