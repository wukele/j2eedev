package com.sumilux.acl.security;

import org.springframework.security.core.Authentication;

public class Session {

	private Authentication authentication;
	private long lastCallTimestamp;

	public Session(Authentication authentication, long lastCallTimestamp) {
		super();
		this.authentication = authentication;
		this.lastCallTimestamp = lastCallTimestamp;
	}

	public Authentication getAuthentication() {
		return authentication;
	}

	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}

	public long getLastCallTimestamp() {
		return lastCallTimestamp;
	}

	public void setLastCallTimestamp(long lastCallTimestamp) {
		this.lastCallTimestamp = lastCallTimestamp;
	}
}