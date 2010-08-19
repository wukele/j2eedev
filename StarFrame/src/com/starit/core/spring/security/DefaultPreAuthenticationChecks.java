package com.starit.core.spring.security;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

/**
 *
 * @datetime 2010-8-18 下午07:55:22
 * @author libinsong1204@gmail.com
 */
public class DefaultPreAuthenticationChecks implements UserDetailsChecker {
    public void check(UserDetails user) {
        if (!user.isAccountNonLocked()) {
            throw new LockedException("User account is locked", user);
        }

        if (!user.isEnabled()) {
            throw new DisabledException("User is disabled", user);
        }

        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("User account has expired", user);
        }
    }
}