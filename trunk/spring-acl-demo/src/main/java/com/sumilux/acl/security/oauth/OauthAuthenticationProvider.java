package com.sumilux.acl.security.oauth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class OauthAuthenticationProvider implements AuthenticationProvider {
	protected final Log logger = LogFactory.getLog(getClass());

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadOauthTokenException("Bad oauth token");
        }
		
		String principal = "melin";
		return createSuccessAuthentication(principal, authentication);
	}
	
	protected Authentication createSuccessAuthentication(Object principal, Authentication authentication) {
		OauthAuthenticationToken result = new OauthAuthenticationToken(principal, authentication.getCredentials());
        result.setDetails(authentication.getDetails());

        return result;
    }

	public boolean supports(Class<? extends Object> authentication) {
		return (OauthAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
