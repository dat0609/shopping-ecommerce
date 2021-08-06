package com.shopme.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "countries")
@Data
public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 45)
	private String name;

	@Column(nullable = false, length = 5)
	private String code;

	@JsonIgnore
	@OneToMany(mappedBy = "country")
	private Set<State> states;
	
	public Country() {

	}

	public Country(String name, String code) {
		this.name = name;
		this.code = code;
	}
	
	public Country(Integer id) {
		this.id = id;
	}

}
