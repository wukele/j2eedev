package com.starit.core.log;

/**
 * 日志操作类型
 *
 * @datetime 2010-8-18 上午11:42:36
 * @author libinsong1204@gmail.com
 */
public enum OperationType {
	/**
	 * 查看
	 */
	INSERT("insert"),
	/**
	 * 修改
	 */
	UPDATE("update"),
	/**
	 * 删除
	 * @param type
	 */
	DELETE("delete"),
	/**
	 * 读取
	 */
	READ("read"),
	/**
	 * 排序
	 */
	ORDER("order"),
	
	NONE("");

	private OperationType(String type){
		this.type = type;
	}
	
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
