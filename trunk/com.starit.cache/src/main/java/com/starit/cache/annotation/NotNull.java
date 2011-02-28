package com.starit.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置在field的get方法上，如果get方法返回为空。需要清空缓存，缓存值返回为null
 * 
 * @author  libinsong1204@gmail.com
 * @date    2011-1-15 上午11:30:05
 * @version 
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotNull {
}
