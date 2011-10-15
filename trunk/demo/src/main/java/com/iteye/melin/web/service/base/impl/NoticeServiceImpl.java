package com.iteye.melin.web.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iteye.melin.core.web.dao.GenericDao;
import com.iteye.melin.core.web.service.BaseServiceImpl;
import com.iteye.melin.web.dao.base.NoticeDao;
import com.iteye.melin.web.model.base.Notice;
import com.iteye.melin.web.service.base.NoticeService;

/**
 *
 * @datetime 2010-8-8 下午04:44:42
 * @author libinsong1204@gmail.com
 */
@Service
public class NoticeServiceImpl extends BaseServiceImpl<Notice, Long> implements NoticeService {
	//~ Instance fields ================================================================================================
	@Autowired
	private NoticeDao noticeDao;

	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	@Override
	public GenericDao<Notice, Long> getGenericDao() {
		return this.noticeDao;
	}
	
}
