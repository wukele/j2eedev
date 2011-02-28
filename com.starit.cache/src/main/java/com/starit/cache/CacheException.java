package com.starit.cache;

import org.springframework.core.NestedRuntimeException;

/**
 *
 * @datetime 2010-8-20 上午10:57:11
 * @author libinsong1204@gmail.com
 */
@SuppressWarnings("serial")
public class CacheException extends NestedRuntimeException {

	/**
	 * Constructor for CacheException.
	 * @param msg the detail message
	 */
	public CacheException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for CacheException.
	 * @param msg the detail message
	 * @param cause the root cause 
	 */
	public CacheException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
