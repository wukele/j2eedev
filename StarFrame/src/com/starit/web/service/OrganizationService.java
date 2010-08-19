package com.starit.web.service;

import com.starit.core.web.service.BaseService;
import com.starit.web.model.Organization;

/**
 *
 * @datetime 2010-8-17 下午09:54:14
 * @author libinsong1204@gmail.com
 */
public interface OrganizationService extends BaseService<Organization, Long> {
	public void saveOrganizationOrder(Long parentId, String childIds);
}
