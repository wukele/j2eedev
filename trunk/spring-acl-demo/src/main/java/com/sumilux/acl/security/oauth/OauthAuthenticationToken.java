package com.sumilux.acl.security.oauth;

import org.springframework.security.authentication.AbstractAuthenticationToken;

@SuppressWarnings("serial")
public class OauthAuthenticationToken extends AbstractAuthenticationToken {
	private Object oauth;
    private Object token;

    public OauthAuthenticationToken(Object token) {
        super(null);
        this.token = token;
        setAuthenticated(false);
    }
    
    public OauthAuthenticationToken(Object oauth, Object token) {
        super(null);
        this.oauth = oauth;
        this.token = token;
        super.setAuthenticated(true);
    }

    public Object getCredentials() {
        return this.token;
    }

    public Object getPrincipal() {
        return this.oauth;
    }
    
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }
}
