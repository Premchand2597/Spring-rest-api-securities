package com.example.SpringAi_Demo.Security;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.SpringAi_Demo.Entity.Login;
import com.example.SpringAi_Demo.Repo.LoginRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
    
	@Autowired
    private JwtTokenHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private LoginRepo loginRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        //Authorization

        String requestHeader = request.getHeader("Authorization");
        //Bearer 2352345235sdfrsfgsdfsdf
        logger.info(" Header :  {}", requestHeader);
        String token = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            //looking good
            token = requestHeader.substring(7);
            
            // Check for access token
        	if(!jwtHelper.isAccessToken(token)) {
        		filterChain.doFilter(request, response);
        		return;
        	}
            
            try {

            	Jws<Claims> parsed = this.jwtHelper.parse(token);
            	Claims payload = parsed.getPayload();

            	// Extract user ID
            	String userId = payload.getSubject();
            	int id = Integer.parseInt(userId);

            	// Fetch user
            	Optional<Login> userOpt = loginRepo.findById(id);

            	userOpt.ifPresent(user -> {
            	    UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getEmail());

            	    UsernamePasswordAuthenticationToken authentication =
            	            new UsernamePasswordAuthenticationToken(
            	                    userDetails,
            	                    null,
            	                    userDetails.getAuthorities()
            	            );

            	    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            	    if (SecurityContextHolder.getContext().getAuthentication() == null) {
            	        SecurityContextHolder.getContext().setAuthentication(authentication);
            	    }
            	});

            } catch (IllegalArgumentException e) {
                logger.info("Illegal Argument while fetching the username !!");
                e.printStackTrace();
            } catch (ExpiredJwtException e) {
                logger.info("Given jwt token is expired !!");
                e.printStackTrace();
            } catch (MalformedJwtException e) {
                logger.info("Some changed has done in token !! Invalid Token");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();

            }


        } else {
            logger.info("Invalid Header Value !! ");
        }


        /*if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {


            //fetch user detail from username
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
            if (validateToken) {

                //set the authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);


            } else {
                logger.info("Validation fails !!");
            }


        }*/

        filterChain.doFilter(request, response);


    }
}