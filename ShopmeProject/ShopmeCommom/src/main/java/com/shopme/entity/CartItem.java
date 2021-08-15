package com.shopme.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

import com.shopme.entity.product.Product;

import lombok.Data;

@Entity
@Table(name = "cart_items")
@Data
public class CartItem extends IdBasedEntity{

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "product_id")	
	private Product product;

	private int quantity;
	
	@javax.persistence.Transient
	private float shippingCost;
	
	@Transient
	public float getSubtotal() {
		return product.getDiscountPrice() * quantity;
	}
}
