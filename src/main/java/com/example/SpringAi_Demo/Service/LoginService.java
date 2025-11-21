package com.example.SpringAi_Demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.SpringAi_Demo.Entity.Login;
import com.example.SpringAi_Demo.Repo.LoginRepo;

@Service
public class LoginService {
	
	@Autowired
	private LoginRepo loginRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public boolean getUserNameAndPasswordLoginData(Login log) {
		Login fetchedData = loginRepo.findByUsername(log.getUsername());
		if(fetchedData != null) {
			return log.getPassword().equals(fetchedData.getPassword());
		}
		return false;
	}
	
	public String createNewUser(Login log) {
		log.setPassword(passwordEncoder.encode(log.getPassword()));
		Login savedUser = loginRepo.save(log);
		if(savedUser != null) {
			return "success";
		}else {
			return "failed";
		}
	}
}
