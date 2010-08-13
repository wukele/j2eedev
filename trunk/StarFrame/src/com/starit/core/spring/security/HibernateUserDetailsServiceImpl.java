package com.starit.core.spring.security;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class HibernateUserDetailsServiceImpl extends HibernateDaoSupport implements UserDetailsService {
	//~ Instance fields ================================================================================================
	
	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	@SuppressWarnings("unchecked")
	public UserDetails loadUserByUsername(String userId)
			throws UsernameNotFoundException, DataAccessException {
		List results = getHibernateTemplate().find("from User where username = ?",
                new Object[] {userId});
		if (results.size() < 1) {
            throw new UsernameNotFoundException(userId + "not found");
        }

		return (UserDetails) results.get(0);
	}
}
