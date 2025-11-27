package com.example.SpringAi_Demo.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringAi_Demo.Config.CookieService;
import com.example.SpringAi_Demo.Entity.Login;
import com.example.SpringAi_Demo.Entity.RefreshTokenRequest;
import com.example.SpringAi_Demo.Entity.RefreshToken_Entity;
import com.example.SpringAi_Demo.Repo.LoginRepo;
import com.example.SpringAi_Demo.Repo.RefreshToken_Repo;
import com.example.SpringAi_Demo.Security.JwtAuthRequest;
import com.example.SpringAi_Demo.Security.JwtAuthResponse;
import com.example.SpringAi_Demo.Security.JwtTokenHelper;
import com.example.SpringAi_Demo.Service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
    
    @Autowired
    private RefreshToken_Repo refreshToken_Repo;
    
    @Autowired
    private CookieService cookieService;

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody JwtAuthRequest request, HttpServletResponse res) {

        authenticate(request.getEmail(), request.getPassword());
        
        // UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        Login user = loginRepo.findByEmail(request.getEmail());
        
        // Generate Unique JTI
        String jti = UUID.randomUUID().toString();
        
        // Create RefreshToken Object
        RefreshToken_Entity refreshToken = new RefreshToken_Entity(
                jti,
                user,
                LocalDateTime.now(),
                LocalDateTime.now().plusSeconds(helper.getRefreshTTLSeconds()),
                false,
                null
        );
        
        refreshToken_Repo.save(refreshToken);
        
        // Generate Tokens
        String accessToken = helper.generateAccessToken(user);
        String refreshTokenString = helper.generateRefreshToken(user, jti);
        
        // use cookie service to attach refresh token in cookie
        cookieService.attachRefreshCookie(res, refreshTokenString, (int) helper.getRefreshTTLSeconds());
        cookieService.addNoStoreHeader(res);
        
        JwtAuthResponse response = JwtAuthResponse.customResponse(accessToken, refreshTokenString, helper.getAccessTTLSeconds(), user);
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
    
    
    // Access and refresh token renew
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthResponse> refreshToken(
    		@RequestBody(required = false) RefreshTokenRequest body,
    		HttpServletResponse res, HttpServletRequest req
    		){
    	
    	String refreshToken = readRequestTokenFromRequest(body, req).orElseThrow(()->new BadCredentialsException("Refresh Token is missing !"));
    	if(!helper.isRefreshToken(refreshToken)) {
    		throw new BadCredentialsException("Invalid refresh token type");
    	}
    	String jti = helper.getJti(refreshToken);
    	int userId = helper.getUserId(refreshToken);
    	RefreshToken_Entity storedRefreshToken = refreshToken_Repo.findByJti(jti).orElseThrow(()->new BadCredentialsException("Refresh token not recognized"));
    	
    	if(storedRefreshToken.isRevoked()) {
    		throw new BadCredentialsException("Refresh token is expired or revoked");
    	}
    	
    	if(storedRefreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
    		throw new BadCredentialsException("Refresh token expired");
    	}
    	
    	if(storedRefreshToken.getLogin().getId() != userId) {
    		throw new BadCredentialsException("Refresh token not belongs to this user");
    	}
    	
    	// Rotate refresh token
    	storedRefreshToken.setRevoked(true);
    	String newJti = UUID.randomUUID().toString();
    	storedRefreshToken.setReplacedByToken(newJti);
    	refreshToken_Repo.save(storedRefreshToken);
    	
    	Login user = storedRefreshToken.getLogin();
    	RefreshToken_Entity newRefreshTokenOb = new RefreshToken_Entity(
    			newJti,
                user,
                LocalDateTime.now(),
                LocalDateTime.now().plusSeconds(helper.getRefreshTTLSeconds()),
                false,
                null
        );
    	
    	refreshToken_Repo.save(newRefreshTokenOb);
    	String newAccessToken = helper.generateAccessToken(user);
    	String newRefreshToken = helper.generateRefreshToken(user, newRefreshTokenOb.getJti());
    	
    	cookieService.attachRefreshCookie(res, newRefreshToken, (int) helper.getRefreshTTLSeconds());
    	cookieService.addNoStoreHeader(res);
    	return ResponseEntity.ok(JwtAuthResponse.customResponse(newAccessToken, newRefreshToken, helper.getAccessTTLSeconds(), user));
    }

    // This method will read refresh token from request header or body
	private Optional<String> readRequestTokenFromRequest(RefreshTokenRequest body, HttpServletRequest req) {
		
		// 1. Prefer reading refresh token from cookie
		if(req.getCookies()!=null) {
			Optional<String> fromCookie = Arrays.stream(req.getCookies()).filter(c-> cookieService.getRefreshTokenCookieName().equals(c.getName()))
						.map(c->c.getValue()).filter(v-> !v.isBlank()).findFirst();
			
			if(fromCookie.isPresent()) {
				return fromCookie;
			}
		
		}
		
		// 2. body
		if(body!=null && body.refreshToken() != null && !body.refreshToken().isBlank()) {
			return Optional.of(body.refreshToken());
		}
		
		// 3. custom header
		String refreshHeader = req.getHeader("X-Refresh-Token");
		if(refreshHeader != null && !refreshHeader.isBlank()) {
			return Optional.of(refreshHeader.trim());
		}
		
		// 4. Authorization = Bearer <token>
		String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
		if(authHeader != null && authHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
			String candidate = authHeader.substring(7).trim();
			if(!candidate.isEmpty()) {
				try {
					if(helper.isRefreshToken(candidate)) {
						return Optional.of(candidate);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return Optional.empty();
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest req, HttpServletResponse res){
		readRequestTokenFromRequest(null, req).ifPresent(token->{
			try {
				if(helper.isRefreshToken(token)) {
					String jti = helper.getJti(token);
					refreshToken_Repo.findByJti(jti).ifPresent(rt->{
						rt.setRevoked(true);
						refreshToken_Repo.save(rt);
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		cookieService.clearRefreshCookie(res);
		cookieService.addNoStoreHeader(res);
		SecurityContextHolder.clearContext();
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}