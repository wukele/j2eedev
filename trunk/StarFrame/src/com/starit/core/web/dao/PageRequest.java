package com.starit.core.web.dao;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @datetime 2010-7-6 下午06:01:46
 * @author libinsong1204@gmail.com
 */
public class PageRequest<E> {

	/**
	 * 过滤参数
	 */
	private Map<String, Object> filters = new HashMap<String, Object>();

	/**
	 * 复杂条件
	 */
	private String extraCondition = "";

	/**
	 * 复杂条件参数
	 */
	private Map<String, Object> extraParams = new HashMap<String, Object>();

	/**
	 * 页号码,页码从1开始
	 */
	private int pageNumber;
	/**
	 * 分页大小
	 */
	private int pageSize;
	/**
	 * 排序的多个列,如: username desc
	 */
	private String sortColumns;

	public PageRequest(int pageNumber, int pageSize) {
		this(pageNumber, pageSize, new HashMap<String, Object>());
	}

	public PageRequest(int pageNumber, int pageSize, Map<String, Object> filters) {
		this(pageNumber, pageSize, filters, null, new HashMap<String, Object>(), null);
	}

	public PageRequest(int pageNumber, int pageSize, String extraCondition, Map<String, Object> extraParams, String sortColumns) {
		this(pageNumber, pageSize, new HashMap<String, Object>(), extraCondition, extraParams, sortColumns);
	}

	public PageRequest(int pageNumber, int pageSize, Map<String, Object> filters, String extraCondition, Map<String, Object> extraParams, String sortColumns) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.filters = filters;
		this.extraCondition = extraCondition;
		this.extraParams = extraParams;
		setSortColumns(sortColumns);
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortColumns() {
		return sortColumns;
	}

	public Map<String, Object> getFilters() {
		return filters;
	}

	public String getExtraCondition() {
		return extraCondition;
	}

	public void setExtraCondition(String extraCondition) {
		this.extraCondition = extraCondition;
	}

	public Map<String, Object> getExtraParams() {
		return extraParams;
	}

	/**
	 * 排序的列,可以同时多列,使用逗号分隔,如 username desc,age asc
	 * @return
	 */
	public void setSortColumns(String sortColumns) {
		this.sortColumns = sortColumns;
	}

}
