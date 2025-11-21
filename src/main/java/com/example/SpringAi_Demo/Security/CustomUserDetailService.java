package com.example.SpringAi_Demo.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.SpringAi_Demo.Entity.Login;
import com.example.SpringAi_Demo.Repo.LoginRepo;

@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private LoginRepo loginRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//get the user details from DB using username
		Login fetchedDetails = null;
		try {
			fetchedDetails = loginRepo.findByUsername(username);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return fetchedDetails;
	}

}
