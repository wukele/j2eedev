package com.iteye.melin.web.service.base;

import java.util.List;

import com.iteye.melin.core.web.service.BaseService;
import com.iteye.melin.web.model.base.Dictionary;

/**
 *
 * @datetime 2010-8-20 下午10:19:47
 * @author libinsong1204@gmail.com
 */
public interface DictionaryService extends BaseService<Dictionary, Long> {

	//~ Methods ========================================================================================================
	public List<Dictionary> queryDictionarys(Long dictTypeId);
}
