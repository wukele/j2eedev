package com.starit.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starit.core.web.dao.GenericDao;
import com.starit.core.web.service.BaseServiceImpl;
import com.starit.web.dao.UserDao;
import com.starit.web.model.User;
import com.starit.web.service.UserService;

/**
 *
 * @datetime 2010-8-8 下午04:44:42
 * @author libinsong1204@gmail.com
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {
	//~ Instance fields ================================================================================================
	@Autowired
	private UserDao userDao;

	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	@Override
	public GenericDao<User, Long> getGenericDao() {
		return this.userDao;
	}
}
