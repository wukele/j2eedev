package com.iteye.melin.web.service.support.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iteye.melin.core.web.dao.GenericDao;
import com.iteye.melin.core.web.service.BaseServiceImpl;
import com.iteye.melin.web.dao.support.AppTypeDao;
import com.iteye.melin.web.model.support.AppType;
import com.iteye.melin.web.service.support.AppTypeService;

/**
 *
 * @datetime 2010-8-8 下午04:44:42
 * @author libinsong1204@gmail.com
 */
@Service
public class AppTypeServiceImpl extends BaseServiceImpl<AppType, Long> implements AppTypeService {
	//~ Instance fields ================================================================================================
	@Autowired
	private AppTypeDao appTypeDao;

	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	@Override
	public GenericDao<AppType, Long> getGenericDao() {
		return this.appTypeDao;
	}
	
}
