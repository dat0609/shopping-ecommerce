package com.shopme.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "states")
@Data
public class State extends IdBasedEntity{

	@Column(nullable = false, length = 45)
	private String name;

	@ManyToOne()
	@JoinColumn(name = "country_id")
	private Country country;
	
	public State() {

	}

	public State(String name, Country country) {
		this.name = name;
		this.country = country;
	}
	
}
