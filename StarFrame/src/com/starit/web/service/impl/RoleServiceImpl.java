package com.starit.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starit.core.web.dao.GenericDao;
import com.starit.core.web.dao.Page;
import com.starit.core.web.dao.PageRequest;
import com.starit.core.web.service.BaseServiceImpl;
import com.starit.web.dao.RoleDao;
import com.starit.web.model.Resource;
import com.starit.web.model.Role;
import com.starit.web.service.RoleService;

/**
 *
 * @datetime 2010-8-8 下午04:44:42
 * @author libinsong1204@gmail.com
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, Long> implements RoleService {
	//~ Instance fields ================================================================================================
	@Autowired
	private RoleDao roleDao;

	//~ Methods ========================================================================================================
	@Override
	public GenericDao<Role, Long> getGenericDao() {
		return this.roleDao;
	}
	
	/**
	 * 查询所有资源信息，同时关联查询指定的角色信息
	 *
	 * @param pageRequest
	 * @param roleId
	 */
	@Transactional(readOnly=true)
	public Page<Resource> queryResources4Role(PageRequest<Resource> pageRequest, Long roleId) {
		Page<Resource> page = this.roleDao.queryResources4Role(pageRequest);
		List<Resource> list = page.getResult();
		List<Resource> newList = new ArrayList<Resource>();
		List<Long> ids = new ArrayList<Long>();
		for(Resource resource : list) {
			if(!ids.contains(resource.getId())) {
				ids.add(resource.getId());
				for(Role role : resource.getRoles()) {
					if(role.getId().longValue() == roleId.longValue()) {
						resource.setCounter(1L);
						resource.getRoles().clear();
						break;
					}
				}
				newList.add(resource);
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
	@SuppressWarnings("unchecked")
	@Transactional
	public void bindResource(Long roleId, String ids) {
		List<Resource> list = (List<Resource>) this.getGenericDao().find("from Resource r where r.id in (" + ids + ")");
		Role role = this.getGenericDao().find(roleId);
		role.getResources().addAll(list);
		this.getGenericDao().update(role);
	}
	
	/**
	 * 给资源取消绑定角色
	 * 
	 * @param roleId 角色ID
	 * @param ids 资源ID，多个值用逗号分隔
	 * @return ResponseData
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void unBindResource(Long roleId, String ids) {
		List<Resource> list = (List<Resource>) this.getGenericDao().find("from Resource r where r.id in (" + ids + ")");
		Role role = this.getGenericDao().find(roleId);
		role.getResources().removeAll(list);
		this.getGenericDao().update(role);
	}
}
