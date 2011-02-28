package com.starit.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author  libinsong1204@gmail.com
 * @date    2011-1-15 上午11:30:05
 * @version 
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheKey {
	
	/**
	 * 生成缓存key模板
	 */
	String value();
}
