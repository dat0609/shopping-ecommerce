package com.shopme.admin.order;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.entity.Order;

public interface OrderRepository extends PagingAndSortingRepository<Order, Integer>{

}
