package com.starit.core.spring.security;

import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

/**
 *
 * @datetime 2010-8-18 下午07:58:18
 * @author libinsong1204@gmail.com
 */
public class DefaultPostAuthenticationChecks implements UserDetailsChecker {
    public void check(UserDetails user) {
        if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("User credentials have expired", user);
        }
    }
}
