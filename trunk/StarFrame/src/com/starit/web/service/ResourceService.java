package com.starit.web.service;

import java.util.Set;

import com.starit.core.web.dao.Page;
import com.starit.core.web.dao.PageRequest;
import com.starit.core.web.service.BaseService;
import com.starit.web.model.Resource;
import com.starit.web.model.Role;

/**
 *
 * @datetime 2010-8-9 上午10:34:58
 * @author libinsong1204@gmail.com
 */
public interface ResourceService extends BaseService<Resource, Long> {

	//~ Methods ========================================================================================================
	public Set<Role> queryRoles4Res(Long resId);
	
	public Page<Role> pageQueryRoles4Res(PageRequest<Role> pageRequest, Long resId);
	
	public void bindRole(Long resId, String ids);
	
	public void unBindRole(Long resId, String ids);
}
