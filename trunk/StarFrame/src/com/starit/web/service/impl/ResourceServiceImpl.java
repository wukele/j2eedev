package com.starit.web.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starit.core.util.StringUtils;
import com.starit.core.web.dao.GenericDao;
import com.starit.core.web.dao.Page;
import com.starit.core.web.dao.PageRequest;
import com.starit.core.web.service.BaseServiceImpl;
import com.starit.web.dao.ResourceDao;
import com.starit.web.dao.RoleDao;
import com.starit.web.model.Resource;
import com.starit.web.model.Role;
import com.starit.web.service.ResourceService;

/**
 *
 * @datetime 2010-8-8 下午04:44:42
 * @author libinsong1204@gmail.com
 */
@Service
@Transactional
public class ResourceServiceImpl extends BaseServiceImpl<Resource, Long> implements ResourceService {
	//~ Instance fields ================================================================================================
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private RoleDao roleDao;

	//~ Methods ========================================================================================================
	@Override
	public GenericDao<Resource, Long> getGenericDao() {
		return this.resourceDao;
	}
	
	/**
	 * 查询资源关联的所有角色, 只接受POST请求
	 * 
	 * @param resId 资源ID
	 * @return List<Role> 资源关联的所有角色
	 */
	@Transactional(readOnly=true)
	public Set<Role> queryRoles4Res(Long resId) {
		Resource resource = resourceDao.find(resId);
		return resource.getRoles();
	}
	
	/**
	 * 查询所有角色信息，同时关联查询指定的资源信息
	 *
	 * @param pageRequest
	 * @param roleId
	 * @return Page<Role>
	 */
	@Transactional(readOnly=true)
	public Page<Role> pageQueryRoles4Res(PageRequest<Role> pageRequest, Long resId) {
		Page<Role> page = this.resourceDao.queryRoles4Res(pageRequest);
		List<Role> list = page.getResult();
		List<Role> newList = new ArrayList<Role>();
		List<Long> ids = new ArrayList<Long>();
		for(Role role : list) {
			if(!ids.contains(role.getId())) {
				ids.add(role.getId());
				for(Resource resource : role.getResources()) {
					if(resource.getId().longValue() == resId.longValue()) {
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
	public void bindRole(Long resId, String ids) {
		List<Long> idList = StringUtils.convertStrToLongList(ids);
		List<Role> list = roleDao.queryRolesByIds(idList);
		Resource resource = this.getGenericDao().find(resId);
		for(Role role : list) {
			role.getResources().add(resource);
		}
	}
	
	/**
	 * 给资源取消绑定角色
	 * 
	 * @param roleId 角色ID
	 * @param ids 资源ID，多个值用逗号分隔
	 * @return ResponseData
	 */
	@Transactional
	public void unBindRole(Long resId, String ids) {
		List<Long> idList = StringUtils.convertStrToLongList(ids);
		List<Role> list = roleDao.queryRolesByIds(idList);
		Resource resource = this.getGenericDao().find(resId);
		for(Role role : list) {
			role.getResources().remove(resource);
		}
	}
}
