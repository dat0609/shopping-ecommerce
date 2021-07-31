package com.shopme.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncodeTest {

	@Test
	public void testPassword() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		
		String rawPassword = "123";
		
		String encoded = encoder.encode(rawPassword);
		
		System.out.println(encoded);
		
		boolean check = encoder.matches(rawPassword, encoded);
		System.out.println(check);
	}
}
