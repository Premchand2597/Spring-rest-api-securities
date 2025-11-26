package com.example.SpringAi_Demo.Security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.SpringAi_Demo.Entity.Login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenHelper {
	
	private SecretKey key;
	private long accessTTLSeconds;
	private long refreshTTLSeconds;
	private String issuer;
	
	public SecretKey getKey() {
		return key;
	}

	public void setKey(SecretKey key) {
		this.key = key;
	}

	public long getAccessTTLSeconds() {
		return accessTTLSeconds;
	}

	public void setAccessTTLSeconds(long accessTTLSeconds) {
		this.accessTTLSeconds = accessTTLSeconds;
	}

	public long getRefreshTTLSeconds() {
		return refreshTTLSeconds;
	}

	public void setRefreshTTLSeconds(long refreshTTLSeconds) {
		this.refreshTTLSeconds = refreshTTLSeconds;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public JwtTokenHelper(
							@Value("${security.jwt.secret}") String key, 
							@Value("${security.jwt.access-ttl-seconds}") long accessTTLSeconds, 
							@Value("${security.jwt.refresh-ttl-seconds}") long refreshTTLSeconds, 
							@Value("${security.jwt.issuer}") String issuer
						) {
		this.key = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
		this.accessTTLSeconds = accessTTLSeconds;
		this.refreshTTLSeconds = refreshTTLSeconds;
		this.issuer = issuer;
	}

	// Generate Token
	public String generateAccessToken(Login user) {
		Instant now = Instant.now();
		return Jwts.builder()
				.id(UUID.randomUUID().toString())
				.subject(String.valueOf(user.getId()))
				.issuer(issuer)
				.issuedAt(Date.from(now))
				.expiration(Date.from(now.plusSeconds(accessTTLSeconds)))
				.claims(Map.of(
						"email", user.getEmail(),
						"roles", user.getRole(),
						"typ", "access"
				))
//				.signWith(key, SignatureAlgorithm.HS512) // Deprecated
				.signWith(key)
				.compact();
	}
	
	// Generate Refresh Token
	public String generateRefreshToken(Login user, String jti) {
		Instant now = Instant.now();
		return Jwts.builder()
				.id(jti)
				.subject(String.valueOf(user.getId()))
				.issuer(issuer)
				.issuedAt(Date.from(now))
				.expiration(Date.from(now.plusSeconds(refreshTTLSeconds)))
				.claim("typ", "refresh")
//				.signWith(key, SignatureAlgorithm.HS512) // Deprecated
				.signWith(key)
				.compact();
	}
	
	
	// Parse the token
	public Jws<Claims> parse(String token){
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
	}
	
	public boolean isAccessToken(String token) {
		Claims c = parse(token).getPayload();
		return "access".equals(c.get("typ"));
	}
	
	public boolean isRefreshToken(String token) {
		Claims c = parse(token).getPayload();
		return "refresh".equals(c.get("typ"));
	}
	
	public UUID getUserId(String token) {
		Claims c = parse(token).getPayload();
		return UUID.fromString(c.getSubject());
	}
	
	public String getJti(String token) {
		return parse(token).getPayload().getId();
	}
	
	
	
	


	// Token validity = 5 hours
	/*@Value("${security.jwt.access-ttl-seconds}")
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
    }*/
}
