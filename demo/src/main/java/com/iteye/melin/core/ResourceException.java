package com.iteye.melin.core;

import org.springframework.core.NestedRuntimeException;

/**
 *　系统异常运行时异常类。
 *
 * @author   libinsong1204@gmail.com
 * @Date	 2011-6-14 下午08:47:36
 */
public class ResourceException extends NestedRuntimeException{
	private String errCode;

	private static final long serialVersionUID = 1L;
	
	public ResourceException(String msg) {
		super(msg);
	}
	
	public ResourceException(String errCode, Throwable cause) {
		super("", cause);
		this.errCode = errCode;
	}

	public String getErrCode() {
		return errCode;
	}
}

