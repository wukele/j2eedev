package com.googlecode.doce.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 
 * @author binsongl
 *
 */
public class SpringSecurityManager {
	private AuthenticationManager authenticationManager;
	
	public SpringSecurityManager(AuthenticationManager authenticationManager) {
		super();
		this.authenticationManager = authenticationManager;
	}

	public void clearSessionContext() {
		SecurityContextHolder.clearContext();
	}

	public void authenticate(String username, String password) {
		Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
		try {
			auth = authenticationManager.authenticate(auth);
		} catch (AuthenticationException e) {
		}
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
}
