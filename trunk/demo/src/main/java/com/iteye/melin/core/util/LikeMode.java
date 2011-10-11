package com.iteye.melin.core.util;

/**
 * SQL 语句where条件部分，
 * 	如果property值为 NULL，添加条件 property is null;
 *  如果property值为 NOTNULL，添加条件 property is not null;
 *
 * @datetime 2011-7-13 下午01:48:31
 * @author libinsong1204@gmail.com
 */
public enum LikeMode {
	/**
	 * 
	 */
	LEFT("left"),
	
	/**
	 * 
	 */
	RIGHT("right"),
	
	/**
	 * 
	 */
	ALL("all");
	
	private LikeMode(String mode) {
		this.mode = mode;
	}
	
	private String mode;
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
}
