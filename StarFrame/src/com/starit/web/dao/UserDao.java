package com.starit.web.dao;

import org.springframework.dao.DataAccessException;

import com.starit.core.web.dao.GenericDao;
import com.starit.core.web.dao.Page;
import com.starit.core.web.dao.PageRequest;
import com.starit.web.model.Role;
import com.starit.web.model.User;

/**
 *
 * @datetime 2010-8-9 上午10:32:56
 * @author libinsong1204@gmail.com
 */
public interface UserDao extends GenericDao<User, Long> {

	//~ Methods ========================================================================================================
	public Page<Role> queryRoles4User(PageRequest<Role> pageRequest) throws DataAccessException;
}
