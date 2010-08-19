package com.starit.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starit.core.web.dao.GenericDao;
import com.starit.core.web.service.BaseServiceImpl;
import com.starit.web.dao.OperLogDao;
import com.starit.web.model.OperLog;
import com.starit.web.service.OperLogService;

/**
 *
 * @datetime 2010-8-18 下午03:12:48
 * @author libinsong1204@gmail.com
 */
@Service
public class OperLogServiceImpl extends BaseServiceImpl<OperLog, Long> implements OperLogService {
	//~ Instance fields ================================================================================================
	@Autowired
	private OperLogDao operLogDao;

	//~ Methods ========================================================================================================
	@Override
	public GenericDao<OperLog, Long> getGenericDao() {
		return this.operLogDao;
	}
}
