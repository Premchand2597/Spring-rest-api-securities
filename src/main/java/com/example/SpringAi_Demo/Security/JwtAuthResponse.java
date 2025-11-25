package com.example.SpringAi_Demo.Security;

import com.example.SpringAi_Demo.Entity.Login;

public record JwtAuthResponse(String accessToken, String refreshToken, long expiresIn, String tokenType, Login user) {
	
	public static JwtAuthResponse customResponse(String accessToken, String refreshToken, long expiresIn, Login user)
	{
		return new JwtAuthResponse(accessToken, refreshToken, expiresIn, "Bearer", user);
		
	}
	
}

	
	
