package com.stocks.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.stocks.entity.User;
import com.stocks.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserUtil {
	
	 
	private final UserRepository repository;
	
	 public static String getCurrentUserEmail () {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
	            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	            return userDetails.getUsername(); // Returns email or username
	        }
	        return null; // No logged-in user found
	    }
	 
	 
	 public  User getCurrentUser() {
		return repository.findByEmail( getCurrentUserEmail());
 
	 }
	       
}
