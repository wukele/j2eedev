package com.starit.web.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starit.core.util.StringUtils;
import com.starit.core.web.dao.GenericDao;
import com.starit.core.web.dao.Page;
import com.starit.core.web.dao.PageRequest;
import com.starit.core.web.service.BaseServiceImpl;
import com.starit.web.dao.RoleDao;
import com.starit.web.dao.UserDao;
import com.starit.web.model.Role;
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
	@Autowired
	private RoleDao roleDao;

	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	@Override
	public GenericDao<User, Long> getGenericDao() {
		return this.userDao;
	}
	
	/**
	 * 查询用户关联的所有角色, 只接受POST请求
	 * 
	 * @param resId 资源ID
	 * @return List<Role> 资源关联的所有角色
	 */
	@Transactional(readOnly=true)
	public Set<GrantedAuthority> queryRoles4User(Long operatorId) {
		User user = userDao.find(operatorId);
		return user.getAuthorities();
	}
	
	/**
	 * 查询所有角色信息，同时关联查询指定的用户信息
	 *
	 * @param pageRequest
	 * @param roleId
	 * @return Page<Role>
	 */
	@Transactional(readOnly=true)
	public Page<Role> pageQueryRoles4User(PageRequest<Role> pageRequest, Long operatorId) {
		Page<Role> page = userDao.queryRoles4User(pageRequest);
		List<Role> list = page.getResult();
		List<Role> newList = new ArrayList<Role>();
		List<Long> ids = new ArrayList<Long>();
		for(Role role : list) {
			if(!ids.contains(role.getId())) {
				ids.add(role.getId());
				for(User user : role.getUsers()) {
					if(user.getOperatorId().longValue() == operatorId.longValue()) {
						role.setCounter(1L);
						role.getResources().clear();
						break;
					}
				}
				newList.add(role);
			}
		}
 		
		page.setResult(newList);
		return page;
	}
	
	/**
	 * 给资源绑定角色
	 * 
	 * @param roleId 角色ID
	 * @param ids 资源ID，多个值用逗号分隔
	 * @return ResponseData
	 */
	@Transactional
	public void bindRole(Long operatorId, String ids) {
		List<Long> idList = StringUtils.convertStrToLongList(ids);
		List<Role> list = roleDao.queryRolesByIds(idList);
		User user = this.getGenericDao().find(operatorId);
		user.getAuthorities().addAll(list);
		this.getGenericDao().update(user);
	}
	
	/**
	 * 给资源取消绑定角色
	 * 
	 * @param roleId 角色ID
	 * @param ids 资源ID，多个值用逗号分隔
	 * @return ResponseData
	 */
	@Transactional
	public void unBindRole(Long operatorId, String ids) {
		List<Long> idList = StringUtils.convertStrToLongList(ids);
		List<Role> list = roleDao.queryRolesByIds(idList);
		User user = this.getGenericDao().find(operatorId);
		user.getAuthorities().removeAll(list);
		this.getGenericDao().update(user);
	}
}
