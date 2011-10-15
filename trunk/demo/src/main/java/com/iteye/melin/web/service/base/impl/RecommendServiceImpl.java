package com.iteye.melin.web.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iteye.melin.core.web.dao.GenericDao;
import com.iteye.melin.core.web.service.BaseServiceImpl;
import com.iteye.melin.web.dao.base.RecommendDao;
import com.iteye.melin.web.model.base.Recommend;
import com.iteye.melin.web.service.base.RecommendService;

/**
 *
 * @datetime 2010-8-8 下午04:44:42
 * @author libinsong1204@gmail.com
 */
@Service
public class RecommendServiceImpl extends BaseServiceImpl<Recommend, Long> implements RecommendService {
	//~ Instance fields ================================================================================================
	@Autowired
	private RecommendDao recommendDao;

	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	@Override
	public GenericDao<Recommend, Long> getGenericDao() {
		return this.recommendDao;
	}
	
}
