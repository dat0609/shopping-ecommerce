package com.shopme.admin.sercurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shopme.admin.user.UserRepository;
import com.shopme.entity.User;

@Service
public class ShopmeUserDetailService implements UserDetailsService{

	@Autowired
	UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = userRepo.getUserByEmail(email);

		if (user != null) {
			return new ShopmeUserDetail(user);
		}
			throw new UsernameNotFoundException("Not found user");
	}

}
