package com.starit.web.service;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.starit.core.web.dao.Page;
import com.starit.core.web.dao.PageRequest;
import com.starit.core.web.service.BaseService;
import com.starit.web.model.Role;
import com.starit.web.model.User;

/**
 *
 * @datetime 2010-8-9 上午10:34:58
 * @author libinsong1204@gmail.com
 */
public interface UserService extends BaseService<User, Long> {

	//~ Methods ========================================================================================================
	public Set<GrantedAuthority> queryRoles4User(Long operatorId);
	
	public Page<Role> pageQueryRoles4User(PageRequest<Role> pageRequest, Long operatorId);
	
	public void bindRole(Long operatorId, String ids);
	
	public void unBindRole(Long operatorId, String ids);
}
