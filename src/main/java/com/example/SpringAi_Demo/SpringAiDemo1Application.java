package com.example.SpringAi_Demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "http://192.168.1.66:3000")
public class SpringAiDemo1Application implements CommandLineRunner{
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringAiDemo1Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//System.out.println(passwordEncoder.encode("234"));
		
	}

}
