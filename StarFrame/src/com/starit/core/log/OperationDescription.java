package com.starit.core.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类的方法描述注解
 *
 * @datetime 2010-8-18 上午11:39:36
 * @author libinsong1204@gmail.com
 */
@Target(ElementType.METHOD)      
@Retention(RetentionPolicy.RUNTIME)      
@Documented     
@Inherited 
public @interface OperationDescription {
	/**  
     * 方法描述  
     * @return  
     */   
    public String description() default "no description"; 
    
    /**
     * 操作类型
     * @return
     */
    public OperationType type() default OperationType.NONE; 
    
    /**
     * 操作实体类型
     * @return
     */
    public String entityType() default "";
}
