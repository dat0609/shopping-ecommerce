package com.shopme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shopme.entity"})
public class ShopmeFontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmeFontEndApplication.class, args);
	}

}
