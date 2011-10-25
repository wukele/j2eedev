package com.iteye.melin.web.service.support.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iteye.melin.core.web.dao.GenericDao;
import com.iteye.melin.core.web.service.BaseServiceImpl;
import com.iteye.melin.web.dao.support.AppSnapDao;
import com.iteye.melin.web.model.support.AppSnap;
import com.iteye.melin.web.service.support.AppSnapService;

/**
 *
 * @datetime 2010-8-8 下午04:44:42
 * @author libinsong1204@gmail.com
 */
@Service
public class AppSnapServiceImpl extends BaseServiceImpl<AppSnap, Long> implements AppSnapService {
	//~ Instance fields ================================================================================================
	@Autowired
	private AppSnapDao appSnapDao;

	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	@Override
	public GenericDao<AppSnap, Long> getGenericDao() {
		return this.appSnapDao;
	}
	
}
