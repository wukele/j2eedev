package com.starit.core.util;

/**
 * JSON 数据请求返回内容
 *
 * @datetime 2010-8-12 下午01:45:25
 * @author libinsong1204@gmail.com
 */
public class ResponseData {
	//~ Static fields ==================================================================================================
	public static final ResponseData SUCCESS_NO_DATA = new ResponseData(true);
	
	//~ Instance fields ================================================================================================
	private boolean success;
	private Object message;
	
	//~ Constructors ===================================================================================================
	public ResponseData(boolean success) {
		this.success = success;
	}
	
	public ResponseData(boolean success, Object message) {
		this.success = success;
		this.message = message;
	}

	//~ Methods ========================================================================================================
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}
}
