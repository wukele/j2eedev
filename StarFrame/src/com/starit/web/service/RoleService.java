package com.starit.web.service;

import com.starit.core.web.dao.Page;
import com.starit.core.web.dao.PageRequest;
import com.starit.core.web.service.BaseService;
import com.starit.web.model.Resource;
import com.starit.web.model.Role;

/**
 *
 * @datetime 2010-8-9 上午09:14:05
 * @author libinsong1204@gmail.com
 */
public interface RoleService extends BaseService<Role, Long> {
	
	public Page<Resource> queryResources4Role(PageRequest<Resource> pageRequest, Long roleId);
	
	public void bindResource(Long roleId, String ids);
	
	public void unBindResource(Long roleId, String ids);
}
