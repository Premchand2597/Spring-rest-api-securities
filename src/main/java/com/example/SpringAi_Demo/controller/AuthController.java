package com.example.SpringAi_Demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringAi_Demo.Entity.Login;
import com.example.SpringAi_Demo.Repo.LoginRepo;
import com.example.SpringAi_Demo.Security.JwtAuthRequest;
import com.example.SpringAi_Demo.Security.JwtAuthResponse;
import com.example.SpringAi_Demo.Security.JwtTokenHelper;
import com.example.SpringAi_Demo.Service.LoginService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private LoginService loginService;

    @Autowired
    private JwtTokenHelper helper;
    
    @Autowired
	private LoginRepo loginRepo;

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody JwtAuthRequest request) {

        Authentication authenticate = authenticate(request.getEmail(), request.getPassword());
        //UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        Login user = loginRepo.findByEmail(request.getEmail());
        // Generate Token
        String token = helper.generateAccessToken(user				);

//        JwtAuthResponse response = new JwtAuthResponse();
//        response.setToken(token);
        
        JwtAuthResponse response = JwtAuthResponse.customResponse(token, "", helper.getAccessTTLSeconds(), user);

        return ResponseEntity.ok(response);
    }

    private Authentication authenticate(String username, String password) {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid Username or Password !!");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials() {
        return new ResponseEntity<String>("Credentials Invalid !!", HttpStatus.BAD_REQUEST);
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> registerNewUser(@RequestBody Login log) {

    	String status = loginService.createNewUser(log);
    	
    	if("success".equals(status)) {
    		return new ResponseEntity<String>("User Created Successfully", HttpStatus.CREATED);
    	}

        return new ResponseEntity<String>("User not Created Successfully", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
