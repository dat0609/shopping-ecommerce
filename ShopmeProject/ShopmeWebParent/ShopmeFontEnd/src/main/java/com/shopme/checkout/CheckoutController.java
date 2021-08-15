package com.shopme.checkout;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.Utility;
import com.shopme.address.AddressService;
import com.shopme.customer.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import com.shopme.entity.Address;
import com.shopme.entity.CartItem;
import com.shopme.entity.Customer;
import com.shopme.entity.ShippingRate;
import com.shopme.entity.order.PaymentMethod;
import com.shopme.order.OrderService;
import com.shopme.shipping.ShippingRateService;
import com.shopme.shoppingcart.ShoppingCartService;

@Controller
public class CheckoutController {

	@Autowired
	CheckoutService checkoutService;

	@Autowired
	CustomerService customerService;

	@Autowired
	AddressService addressService;

	@Autowired
	ShippingRateService shipService;

	@Autowired
	ShoppingCartService cartService;
	
	@Autowired
	OrderService orderService;

	@GetMapping("/checkout")
	public String showCheckoutPage(Model model, HttpServletRequest request) throws CustomerNotFoundException {
		Customer customer = getAuthenticatedCustomer(request);

		Address defaultAddress = addressService.getDefaultAddress(customer);

		ShippingRate shippingRate = null;

		if (defaultAddress != null) {
			model.addAttribute("shippingAddress", defaultAddress.toString());
			shippingRate = shipService.getShippingRateForAddress(defaultAddress);
		} else {
			model.addAttribute("shippingAddress", customer.getAddress());
			shippingRate = shipService.getShippingRateForCustomer(customer);
		}

		if (shippingRate == null) {
			return "redirect:/cart";
		}

		List<CartItem> listCartItems = cartService.listCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(listCartItems, shippingRate);

		model.addAttribute("checkoutInfo", checkoutInfo);
		model.addAttribute("listCartItems", listCartItems);

		return "checkout/checkout";
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);

		if (email == null) {
			throw new CustomerNotFoundException("No authenticated");
		}

		return customerService.getCustomerByEmail(email);
	}

	@PostMapping("/place_order")
	public String placeOrder(HttpServletRequest request) throws CustomerNotFoundException {
		String paymentType = request.getParameter("paymentMethod");
		PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);

		Customer customer = getAuthenticatedCustomer(request);

		Address defaultAddress = addressService.getDefaultAddress(customer);

		ShippingRate shippingRate = null;

		if (defaultAddress != null) {
			shippingRate = shipService.getShippingRateForAddress(defaultAddress);
		} else {
			shippingRate = shipService.getShippingRateForCustomer(customer);
		}

		List<CartItem> listCartItems = cartService.listCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(listCartItems, shippingRate);

		orderService.createOrder(customer, defaultAddress, listCartItems, paymentMethod, checkoutInfo);
		cartService.deleteByCustomer(customer);
		
		return "checkout/order_completed";
	}
}
