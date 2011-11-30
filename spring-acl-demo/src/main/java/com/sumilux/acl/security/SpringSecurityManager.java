package com.sumilux.acl.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.sumilux.acl.security.oauth.OauthAuthenticationToken;

@Component
public class SpringSecurityManager {
	@Autowired
	private AuthenticationManager authenticationManager;

	public void clearSessionContext() {
		SecurityContextHolder.clearContext();
	}

	public void authenticate(String token) {
		Authentication auth = new OauthAuthenticationToken(token);
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
