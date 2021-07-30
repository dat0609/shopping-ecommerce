package com.shopme.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length = 40, nullable = false, unique = true)
	private String name;

	@Column(length = 40, nullable = false)
	private String description;

	public Role() {
		this.name = "";
	}

	public Role(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public Role(int id) {
		this.id = id;
	}

	public Role(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
