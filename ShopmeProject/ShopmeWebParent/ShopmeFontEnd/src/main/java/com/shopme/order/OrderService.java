package com.shopme.order;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.checkout.CheckoutInfo;
import com.shopme.entity.Address;
import com.shopme.entity.CartItem;
import com.shopme.entity.Customer;
import com.shopme.entity.order.Order;
import com.shopme.entity.order.OrderDetail;
import com.shopme.entity.order.OrderStatus;
import com.shopme.entity.order.PaymentMethod;
import com.shopme.entity.product.Product;

@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepository;
	
	public Order createOrder(Customer customer, Address address, List<CartItem> cartItems, PaymentMethod paymentMethod
			, CheckoutInfo checkoutInfo) {
		Order newOrder = new Order();
		
		newOrder.setOrderTime(new Date());
		newOrder.setStatus(OrderStatus.NEW);
		newOrder.setCustomer(customer);
		newOrder.setProductCost(checkoutInfo.getProductCost());
		newOrder.setSubtotal(checkoutInfo.getProductTotal());
		newOrder.setShippingCost(checkoutInfo.getShippingCostTotal());
		newOrder.setTax(0);
		newOrder.setTotal(checkoutInfo.getPaymentTotal());
		newOrder.setPaymentMethod(paymentMethod);
		newOrder.setDeliverDays(checkoutInfo.getDeliverDays());
		newOrder.setDeliverDate(checkoutInfo.getDeliverDate());
		
		if (address == null) {
			newOrder.copyAddressFromCustomer();
		}else {
			newOrder.copyShippingAddress(address);
		}
		
		java.util.Set<OrderDetail> orderDetails = newOrder.getOrderDetails();
		
		for (CartItem cartItem : cartItems) {
			Product product = cartItem.getProduct();
			
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setOrder(newOrder);
			orderDetail.setProduct(product);
			orderDetail.setQuantity(cartItem.getQuantity());
			orderDetail.setUnitPrice(product.getDiscountPrice());
			orderDetail.setProductCost(product.getCost() * cartItem.getQuantity());
			orderDetail.setSubtotal(cartItem.getSubtotal());
			orderDetail.setShippingCost(cartItem.getShippingCost());
			
			orderDetails.add(orderDetail);
		}
		
		return orderRepository.save(newOrder);
		
	}
}
