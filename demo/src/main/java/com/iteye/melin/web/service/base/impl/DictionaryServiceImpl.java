package com.iteye.melin.web.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iteye.melin.core.util.DictionaryHolder;
import com.iteye.melin.core.web.dao.GenericDao;
import com.iteye.melin.core.web.service.BaseServiceImpl;
import com.iteye.melin.web.dao.base.DictionaryDao;
import com.iteye.melin.web.dao.base.DictionaryTypeDao;
import com.iteye.melin.web.model.base.Dictionary;
import com.iteye.melin.web.model.base.DictionaryType;
import com.iteye.melin.web.service.base.DictionaryService;

/**
 *
 * @datetime 2010-8-21 上午08:09:41
 * @author libinsong1204@gmail.com
 */
@Service
public class DictionaryServiceImpl extends BaseServiceImpl<Dictionary, Long> implements DictionaryService {
	//~ Instance fields ================================================================================================
	@Autowired
	private DictionaryDao dictionaryDao;
	@Autowired
	private DictionaryTypeDao businTypeDao;
	
	//~ Methods ========================================================================================================
	@Override
	public GenericDao<Dictionary, Long> getGenericDao() {
		return this.dictionaryDao;
	}
	
	/**
	 * 根据字典业务类型，查询指定字典业务类型的所有子字典项
	 * 为了提高效率，增加缓存处理。
	 * 
	 * @param dictTypeId
	 * @return List<Dictionary>
	 */
	@Transactional(readOnly=true)
	public List<Dictionary> queryDictionarys(Long dictTypeId) {
		List<Dictionary> dictionaries = DictionaryHolder.getDictionaries(dictTypeId);
		if(dictionaries == null) {
			DictionaryType dictionaryType = this.businTypeDao.find(dictTypeId);
			if(dictionaryType != null) {
				dictionaries = dictionaryType.getDictionaries();
			}
			DictionaryHolder.putDictionaries(dictTypeId, dictionaries);
		}
		return dictionaries;
	}
}
