package com.starit.web.dao;

import org.springframework.dao.DataAccessException;

import com.starit.core.web.dao.GenericDao;
import com.starit.core.web.dao.Page;
import com.starit.core.web.dao.PageRequest;
import com.starit.web.model.Resource;
import com.starit.web.model.Role;

/**
 *
 * @datetime 2010-8-9 上午10:32:56
 * @author libinsong1204@gmail.com
 */
public interface ResourceDao extends GenericDao<Resource, Long> {

	//~ Methods ========================================================================================================
	public Page<Role> queryRoles4Res(PageRequest<Role> pageRequest) throws DataAccessException;
}
