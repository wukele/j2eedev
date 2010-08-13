package com.starit.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starit.core.web.dao.GenericDao;
import com.starit.core.web.service.BaseServiceImpl;
import com.starit.web.dao.ResourceDao;
import com.starit.web.model.Resource;
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

	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	@Override
	public GenericDao<Resource, Long> getGenericDao() {
		return this.resourceDao;
	}
}
