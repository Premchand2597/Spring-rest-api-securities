package com.example.SpringAi_Demo.Config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.SpringAi_Demo.Security.CustomUserDetailService;
import com.example.SpringAi_Demo.Security.JwtAuthenticationEntryPoint;
import com.example.SpringAi_Demo.Security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	@Autowired
	private CustomUserDetailService customUserDetailService;

	@Autowired
    private JwtAuthenticationEntryPoint point;
	
    @Autowired
    private JwtAuthenticationFilter filter;
    
    public static final String[] PUBLIC_URLS = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"
    };
	
	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		 
    	/*http
         .csrf(csrf -> csrf.disable())
         .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
         .httpBasic(Customizer.withDefaults());
		 
		 return http.build(); */
    	
    	/*http.csrf(csrf -> csrf.disable())
    	.authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_URLS).permitAll().anyRequest().authenticated())
        .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    	http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    	return http.build();*/
		
		http.csrf(csrf -> csrf.disable())
			.cors(Customizer.withDefaults())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	    	.authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_URLS).permitAll().anyRequest().authenticated())
	    	.exceptionHandling(ex -> ex.authenticationEntryPoint(point))
	    	.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

    	return http.build();
    }
    
    /*protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
	}*/
    
    @Bean
    PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
}
