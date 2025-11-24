package com.example.SpringAi_Demo.Security;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtTokenHelper {

	// Token validity = 5 hours
	@Value("${security.jwt.access-ttl-seconds}")
	private long JWT_TOKEN_VALIDITY;
	
	@Value("${security.jwt.secret}")
	private String JWT_SECRET;
	
	private SecretKey secretKey;

	// Must be 256-bit key
	@PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }
	
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
                .expiration(new Date(now + JWT_TOKEN_VALIDITY * 1000))
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
