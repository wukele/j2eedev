package com.iteye.melin.web.service.support.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iteye.melin.core.web.dao.GenericDao;
import com.iteye.melin.core.web.service.BaseServiceImpl;
import com.iteye.melin.web.dao.support.AppRecoRsDao;
import com.iteye.melin.web.model.support.AppRecoRs;
import com.iteye.melin.web.service.support.AppRecoRsService;

/**
 *
 * @datetime 2010-8-8 下午04:44:42
 * @author libinsong1204@gmail.com
 */
@Service
public class AppRecoRsServiceImpl extends BaseServiceImpl<AppRecoRs, Long> implements AppRecoRsService {
	//~ Instance fields ================================================================================================
	@Autowired
	private AppRecoRsDao appRecoRsDao;

	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	@Override
	public GenericDao<AppRecoRs, Long> getGenericDao() {
		return this.appRecoRsDao;
	}
	
}
