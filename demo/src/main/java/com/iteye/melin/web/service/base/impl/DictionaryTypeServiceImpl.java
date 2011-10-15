package com.iteye.melin.web.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iteye.melin.core.web.dao.GenericDao;
import com.iteye.melin.core.web.service.BaseServiceImpl;
import com.iteye.melin.web.dao.base.DictionaryTypeDao;
import com.iteye.melin.web.model.base.DictionaryType;
import com.iteye.melin.web.service.base.DictionaryTypeService;

/**
 *
 * @datetime 2010-8-20 下午10:20:41
 * @author libinsong1204@gmail.com
 */
@Service
public class DictionaryTypeServiceImpl extends BaseServiceImpl<DictionaryType, Long> implements DictionaryTypeService {
	//~ Instance fields ================================================================================================
	@Autowired
	private DictionaryTypeDao businTypeDao;

	//~ Methods ========================================================================================================
	@Override
	public GenericDao<DictionaryType, Long> getGenericDao() {
		return this.businTypeDao;
	}

}
