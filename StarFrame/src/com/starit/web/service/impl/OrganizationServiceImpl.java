package com.starit.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.starit.core.web.dao.GenericDao;
import com.starit.core.web.service.BaseServiceImpl;
import com.starit.web.dao.OrganizationDao;
import com.starit.web.model.Organization;
import com.starit.web.service.OrganizationService;

/**
 *
 * @datetime 2010-8-17 下午09:55:18
 * @author libinsong1204@gmail.com
 */
@Service
public class OrganizationServiceImpl extends BaseServiceImpl<Organization, Long> implements OrganizationService {
	//~ Instance fields ================================================================================================
	@Autowired
	private OrganizationDao organizationDao;

	//~ Methods ========================================================================================================
	@Override
	public GenericDao<Organization, Long> getGenericDao() {
		return this.organizationDao;
	}
	
	/**
	 * 保存指定节点下，所有直接子节点的顺序。
	 * 
	 * @param parentId 父节点
	 * @param childIds 所有子节点ID
	 */
	@Transactional
	public void saveOrganizationOrder(Long parentId, String childIds) {
		String[] childArr = StringUtils.split(childIds, ",");
		int index=1;
		for(String id :  childArr) {
			Organization organization = organizationDao.find(Long.valueOf(id));
			organization.setParentId(parentId);
			organization.setTheSort(index++);
		}
	}
}
