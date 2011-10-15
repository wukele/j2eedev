package com.iteye.melin.core.util;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.iteye.melin.core.spring.SpringApplicationContextHolder;
import com.iteye.melin.web.model.base.Dictionary;
import com.iteye.melin.web.service.base.DictionaryService;

/**
 * 
 * @author  libinsong1204@gmail.com
 * @date    2011-1-18 上午11:03:34
 * @version 
 */
public class DictionaryHolder {
	/** 缓存业务字典项  */
	private static ConcurrentMap<Long, List<Dictionary>> cacheMap = new ConcurrentHashMap<Long, List<Dictionary>>();
	
	private static Logger logger = LoggerFactory.getLogger(DictionaryHolder.class);
	
	private static DictionaryService dictionaryService;
	
	public static void putDictionaries(Long dictTypeId, List<Dictionary> dictionaries) {
		if(dictionaries != null)
			cacheMap.put(dictTypeId, dictionaries);
	}
	
	public static List<Dictionary> getDictionaries(Long dictTypeId) {
		return cacheMap.get(dictTypeId);
	}
	
	public static void cleanDictionaries(Long dictTypeId) {
		cacheMap.remove(dictTypeId);
	}
	
	/**
	 * 转换业务字典项编码对应业务字典项值，设置给field_Name属性。
	 * 
	 * @param <T> 实体
	 * @param list 待装换实体list集合
	 * @param dictTypeId 业务字典项
	 * @param getMethod 字段get方法
	 */
	public static <T> void transfercoder(List<T> list, Long dictTypeId, String getMethod) {
		if(dictionaryService == null)
			dictionaryService = SpringApplicationContextHolder.getBean(DictionaryService.class);
		
		List<Dictionary> dictionaries = cacheMap.get(dictTypeId);
		if(dictionaries == null) {
			logger.info("relead {} data", dictTypeId);
			dictionaries = dictionaryService.queryDictionarys(dictTypeId);
			cacheMap.put(dictTypeId, dictionaries);
		}
		
		String[] args = getMethod.split("\\.");
		int len = args.length;
		getMethod = args[(len-1)];
		for(T t : list) {
			try {
				Object entity = t;
				for(int i=0; i<(len-1); i++) {
					String name = org.apache.commons.lang.StringUtils.capitalize(args[i]);
					Method method = t.getClass().getMethod("get" + name);
					entity = method.invoke(t);
				}
				Method method = entity.getClass().getMethod(getMethod);
				String obj = String.valueOf(method.invoke(entity));
				String value = "";
				for(Dictionary dic : dictionaries) {
					if(dic.getCode().equals(obj)) {
						value = dic.getName();
						break;
					}
				}
				if(StringUtils.hasText(value)) {
					method = entity.getClass().getMethod(getMethod.replace("get", "set")+"_Name", String.class);
					method.invoke(entity, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
}
