package com.shopme.admin.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.user.UserService;

@RestController
public class UserRestController {
	@Autowired
	UserService userService;

	@PostMapping("/users/check_email")
	public String checkDuplicateEmail(@RequestParam("email") String email, @RequestParam("id") Integer id) {
		return userService.isEmailUnique(id, email) ? "OK" : "Duplicated";
	}
}
