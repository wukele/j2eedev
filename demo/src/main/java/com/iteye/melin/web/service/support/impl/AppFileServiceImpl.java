package com.iteye.melin.web.service.support.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iteye.melin.core.web.dao.GenericDao;
import com.iteye.melin.core.web.service.BaseServiceImpl;
import com.iteye.melin.web.dao.support.AppFileDao;
import com.iteye.melin.web.model.support.AppFile;
import com.iteye.melin.web.service.support.AppFileService;

/**
 *
 * @datetime 2010-8-8 下午04:44:42
 * @author libinsong1204@gmail.com
 */
@Service
public class AppFileServiceImpl extends BaseServiceImpl<AppFile, Long> implements AppFileService {
	//~ Instance fields ================================================================================================
	@Autowired
	private AppFileDao appFileDao;

	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	@Override
	public GenericDao<AppFile, Long> getGenericDao() {
		return this.appFileDao;
	}
	
}
