package com.example.SpringAi_Demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringAi_Demo.Entity.Login;
import com.example.SpringAi_Demo.Service.LoginService;

@RestController
@CrossOrigin(origins = "http://192.168.31.94:3000")
public class LoginController {

	@Autowired
	private LoginService loginService;
	
	@PostMapping("/auth")
	public ResponseEntity<String> validateLogin(@RequestBody Login log) {
		boolean status = loginService.getUserNameAndPasswordLoginData(log);
		if(status) {
			return ResponseEntity.ok("Successfull");
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username or Password");
	    }
	}
}
