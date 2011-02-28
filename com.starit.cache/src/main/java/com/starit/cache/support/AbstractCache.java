package com.starit.cache.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import com.starit.cache.Cache;
import com.starit.cache.CacheException;
import com.starit.cache.JsonTranscoder;
import com.starit.cache.annotation.CacheKey;
import com.starit.cache.annotation.NotNull;

/**
 * 
 * @author  libinsong1204@gmail.com
 * @date    2011-1-15 上午11:56:54
 * @version 
 */
public abstract class AbstractCache implements Cache {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Pattern pattern = Pattern.compile("\\$\\{(\\w+)\\}");  
	
	private final JsonTranscoder transcoder = new JsonTranscoder();

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getItem(T entity) {
		String key = createKey(entity);
		String json = (String) getItem(key);
		try {
			T value = (T)transcoder.deserialize(json, entity.getClass());
			return value;
		} catch (Exception e) {
			throw new CacheException("json transcoder error", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T entityFieldNotNull(Class clazz, T value) {
		Method[] methods = clazz.getMethods();
		for(Method method : methods) {
			NotNull ann = AnnotationUtils.findAnnotation(method, NotNull.class);
			if(ann != null) {
				try {
					Object obj = method.invoke(value);
					//TODO
				} catch (IllegalArgumentException e) {
					throw new CacheException("", e);
				} catch (IllegalAccessException e) {
					throw new CacheException("", e);
				} catch (InvocationTargetException e) {
					throw new CacheException("", e);
				}
			}
		}
		return value;
	}

	@Override
	public <T> void putItem(T entity) {
		String key = createKey(entity);
		try {
			String value = transcoder.serialize(entity);
			putItem(key, value);
		} catch (Exception e) {
			throw new CacheException("json transcoder error", e);
		}
	}
	
	/**
	 * 根据模板生成主键
	 * 
	 * @param entity
	 * @return String
	 */
	private <T> String createKey(T entity) {
		CacheKey cacheKey = findAnnotation(entity.getClass());
		String template = (String)AnnotationUtils.getValue(cacheKey);
		
        Matcher m = pattern.matcher(template);   
        while(m.find()) {   
        	try {
				Method method = ReflectionUtils.findMethod(entity.getClass(), "get"+StringUtils.capitalize(m.group(1)));
				Object result = ReflectionUtils.invokeMethod(method, entity);
				template = template.replaceAll("\\$\\{" + m.group(1) + "\\}", String.valueOf(result));
			} catch (Exception e) {
				throw new CacheException("", e);
			}
        }
        
        if(logger.isDebugEnabled())
        	logger.debug("cache key：{}", template);
        
		return template;
	}

	/**
	 * 查找实体类@CacheKey
	 * 
	 * @param clazz
	 * @return CacheKey
	 */
	@SuppressWarnings("unchecked")
	private CacheKey findAnnotation(Class clazz) {
		CacheKey cacheKey = AnnotationUtils.findAnnotation(clazz, CacheKey.class);
		if(cacheKey == null)
			throw new CacheException(clazz.getName() + " 没有配置@CacheKey，无法生成Key");
		
		return cacheKey;
	}
}
