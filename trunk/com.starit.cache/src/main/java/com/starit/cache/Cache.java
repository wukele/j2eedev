package com.starit.cache;

/**
 * 系统缓存接口，提供memcached实现
 * 
 * @datetime 2010-8-20 上午10:18:23
 * @author libinsong1204@gmail.com
 */
public interface Cache {

	/**
	 * 添加缓存数据
	 * 
	 * @param key
	 * @param value
	 */
	void putItem(String key, Object value);
	
	/**
	 * 根据Entity配置@CacheKey生存缓存key，并把Entity保存到缓存
	 * 
	 * @param entity
	 */
	<T> void putItem(T entity);

	/**
	 * 根据Entity配置@CacheKey生存缓存key，获取缓存数据
	 * 
	 * @param key
	 * @return
	 */
	Object getItem(String key);
	
	/**
	 * 获取缓存数据
	 * 
	 * @param entity
	 * @return
	 */
	<T> T getItem(T entity);
	
	/**
	 * 删除缓存数据，并返回删除的数据
	 * 
	 * @param key
	 * @return
	 */
	Object removeItem(String key);
	
	/**
	 * 删除缓存数据，并返回删除的数据
	 * 
	 * @param entity
	 * @return
	 */
	<T> Object removeItem(T entity);
	
	/**
	 * 删除缓存数据
	 * 
	 * @param key
	 * @return
	 */
	void cleanItem(String key);
	
	/**
	 * 删除缓存数据
	 * 
	 * @param entity 待删除javabean
	 * @return
	 */
	<T> void cleanItem(T entity);
}