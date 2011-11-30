package com.sumilux.acl.security.oauth;

import org.springframework.security.core.AuthenticationException;

@SuppressWarnings("serial")
public class BadOauthTokenException extends AuthenticationException {
    public BadOauthTokenException(String msg) {
        super(msg);
    }

    public BadOauthTokenException(String msg, Object extraInformation) {
        super(msg, extraInformation);
    }

    public BadOauthTokenException(String msg, Throwable t) {
        super(msg, t);
    }
}
