package com.sumilux.acl.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityManager {
	@Autowired
	private AuthenticationManager authenticationManager;

	private static final Logger log = LoggerFactory.getLogger(SpringSecurityManager.class);

	public void clearSessionContext() {
		SecurityContextHolder.clearContext();
	}

	public void authenticate(String login, String password) {
		log.info("trying to authenticate " + login);
		Authentication auth = new UsernamePasswordAuthenticationToken(login,
				password);
		try {
			auth = authenticationManager.authenticate(auth);
		} catch (AuthenticationException e) {
		}
		SecurityContextHolder.getContext().setAuthentication(auth);
		log.info("new session initialized for " + login);
	}

	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
}
