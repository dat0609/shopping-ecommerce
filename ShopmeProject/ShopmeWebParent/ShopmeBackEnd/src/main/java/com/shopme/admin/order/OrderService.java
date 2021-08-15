package com.shopme.admin.order;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.entity.Order;

@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepository;

	private static final int ORDERS_PER_PAGE = 10;

	public List<Order> listAll() {
		return (List<Order>) orderRepository.findAll();
	}
	
	public Page<Order> listByPage(int pageNum, String keyword) {
		Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE);
		
		if (keyword != null) {
			return orderRepository.findAll(keyword, pageable);
		}
		return orderRepository.findAll(pageable);
	}
	
	public Order get(Integer id) throws OrderNotFoundException {
		try {
			return orderRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new OrderNotFoundException("Could not find any orders with ID " + id);
		}
	}
}
