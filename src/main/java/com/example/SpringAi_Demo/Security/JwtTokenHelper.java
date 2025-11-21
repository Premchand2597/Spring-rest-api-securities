package com.example.SpringAi_Demo.Security;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenHelper {

	// Token validity = 5 hours
	private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;

	// Must be 256-bit key
	private final SecretKey secretKey = Keys.hmacShaKeyFor(
	    "ThisIsA512BitSecretKeyForJwtSigningAndValidation12345678901234567890".getBytes()
	);
	
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Extract any claim
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
    	final Claims claims = getAllClaimsFromToken(token);
    	return claimsResolver.apply(claims);
    }
    
    // Extract all claims
    private Claims getAllClaimsFromToken(String token) {
    	JwtParser parser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

        return parser
                .parseSignedClaims(token)
                .getPayload();
    }
    
    private Boolean isTokenExpired(String token) {
    	return getExpirationDateFromToken(token).before(new Date());
    }
    
    // Generate JWT Token
    public String generateToken(UserDetails userdetails) {
    	return generateTokenFromUsername(userdetails.getUsername());
    }
    
    // Create the token
    public String generateTokenFromUsername(String username) {

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(now))
                .expiration(new Date(now + JWT_TOKEN_VALIDITY))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }
    
    // Validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
    	return (
                userDetails.getUsername().equals(getUsernameFromToken(token)) &&
                !isTokenExpired(token)
            );
    }
}
