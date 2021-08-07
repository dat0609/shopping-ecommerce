package com.shopme.customer;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.entity.Customer;
import com.shopme.setting.SettingService;

@Controller
public class ForgotPasswordController {

	@Autowired
	CustomerService customerService;
	
	@Autowired
	SettingService settingService;
	
	@GetMapping("/forgot_password")
	public String showRequestForm() {
		return "customer/forgot_password_form";
	}
	
	@PostMapping("/forgot_password")
	public String processRequestForm(HttpServletRequest request, Model model) throws UnsupportedEncodingException, MessagingException {
		String email = request.getParameter("email");
		
		String token;
		try {
			token = customerService.updateResetPasswordToken(email);
			customerService.sendMail(token, email, request);
			
			model.addAttribute("message", "We have sent a reset password link to your email.");
			
		} catch (CustomerNotFoundException e) {
			model.addAttribute("message", e.getMessage());
		}
	
		return "customer/forgot_password_form";
	}
	
	@GetMapping("/reset_password")
	public String showResetForm(Model model, @Param("token") String token) {
		Customer customer = customerService.getByResetPasswordToken(token);
		if (customer != null) {
			model.addAttribute("token", token);
		}else {
			model.addAttribute("pageTile", "Invalid token");
			model.addAttribute("message", "Invalid token");
			return "message";
		}
		
		return "customer/reset_password_form";
	}
		

	@PostMapping("/reset_password")
	public String processResetForm(HttpServletRequest request, Model model) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");

		try {
			customerService.updatePassword(token, password);

			model.addAttribute("pageTitle", "Reset Your Password");
			model.addAttribute("title", "Reset Your Password");
			model.addAttribute("message", "You have successfully changed your password.");

		} catch (CustomerNotFoundException e) {
			model.addAttribute("pageTitle", "Invalid Token");
			model.addAttribute("message", e.getMessage());
		}	

		return "message";		
	}
}
