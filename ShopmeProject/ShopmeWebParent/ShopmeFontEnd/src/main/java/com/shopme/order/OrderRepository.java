package com.shopme.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopme.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

	
}
