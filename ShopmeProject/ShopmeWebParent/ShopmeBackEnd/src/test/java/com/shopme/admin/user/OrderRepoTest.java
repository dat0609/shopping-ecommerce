package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.order.OrderRepository;
import com.shopme.entity.Customer;
import com.shopme.entity.Order;
import com.shopme.entity.OrderDetail;
import com.shopme.entity.OrderStatus;
import com.shopme.entity.PaymentMethod;
import com.shopme.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderRepoTest {
	@Autowired private OrderRepository repo;
	@Autowired private TestEntityManager entityManager;

	@Test
	public void testCreateNewOrderWithSingleProduct() {
		Customer customer = entityManager.find(Customer.class, 1);
		Product product = entityManager.find(Product.class, 1);

		Order mainOrder = new Order();
		mainOrder.setOrderTime(new java.util.Date());
		mainOrder.setCustomer(customer);
		mainOrder.copyAddressFromCustomer();

		mainOrder.setShippingCost(10);
		mainOrder.setProductCost(product.getCost());
		mainOrder.setTax(0);
		mainOrder.setSubtotal(product.getPrice());
		mainOrder.setTotal(product.getPrice() + 10);

		mainOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
		mainOrder.setStatus(OrderStatus.NEW);
		mainOrder.setDeliverDate(new java.util.Date());
		mainOrder.setDeliverDays(1);

		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setProduct(product);
		orderDetail.setOrder(mainOrder);
		orderDetail.setProductCost(product.getCost());
		orderDetail.setShippingCost(10);
		orderDetail.setQuantity(1);
		orderDetail.setSubtotal(product.getPrice());
		orderDetail.setUnitPrice(product.getPrice());

		mainOrder.getOrderDetails().add(orderDetail);

		Order savedOrder = repo.save(mainOrder);

		assertThat(savedOrder.getId()).isGreaterThan(0);		
	}

}
