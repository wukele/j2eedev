package com.iteye.melin.web.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iteye.melin.core.web.dao.GenericDao;
import com.iteye.melin.core.web.service.BaseServiceImpl;
import com.iteye.melin.web.dao.base.ApplicationDao;
import com.iteye.melin.web.model.base.Application;
import com.iteye.melin.web.service.base.ApplicationService;

/**
 *
 * @datetime 2010-8-8 下午04:44:42
 * @author libinsong1204@gmail.com
 */
@Service
public class ApplicationServiceImpl extends BaseServiceImpl<Application, Long> implements ApplicationService {
	//~ Instance fields ================================================================================================
	@Autowired
	private ApplicationDao applicationDao;

	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	@Override
	public GenericDao<Application, Long> getGenericDao() {
		return this.applicationDao;
	}
	
}
