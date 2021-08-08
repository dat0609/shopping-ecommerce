package com.shopme.shoppingcart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopme.entity.CartItem;
import com.shopme.entity.Customer;
import com.shopme.entity.Product;
import com.shopme.product.ProductRepository;

@Service
@Transactional
public class ShoppingCartService {

	@Autowired
	CartItemRepository cartRepository;
	
	@Autowired
	ProductRepository productRepository;

	public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {
		Integer updatedQuantity = quantity;

		Product product = new Product(productId);
		CartItem cartItem = cartRepository.findByCustomerAndProduct(customer, product);

		if (cartItem != null) {
			updatedQuantity = cartItem.getQuantity() + quantity;

			if (updatedQuantity > 5) {
				throw new ShoppingCartException("Could not add more than 5 " + " item. Already have "
						+ cartItem.getQuantity() + " in your cart");
			}
		} else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}
		cartItem.setQuantity(updatedQuantity);

		cartRepository.save(cartItem);

		return updatedQuantity;
	}

	public List<CartItem> listCartItems(Customer customer) {
		return cartRepository.findByCustomer(customer);
	}
	
	public float updateQuantity(Integer productId, Integer quantity, Customer customer) {
		cartRepository.updateQuantity(quantity, customer.getId(), productId);
		
		Product product = productRepository.findById(productId).get();
		
		float subtotal = product.getDiscountPrice() * quantity;
		
		return subtotal;
	}
}
