package com.iteye.melin.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @datetime 2010-7-6 下午06:00:13
 * @author libinsong1204@gmail.com
 */
@SuppressWarnings("serial")
public class Page<E> implements Serializable, Iterable<E> {

	protected List<E> result;

	protected int pageSize;

	protected int pageNumber;

	protected int totalCount = 0;

	public Page(PageRequest<E> p, int totalCount) {
		this(p.getPageNumber(),p.getPageSize(),totalCount);
	}

	public Page(int pageNumber,int pageSize,int totalCount) {
		this(pageNumber,pageSize,totalCount,new ArrayList<E>(0));
	}

	public Page(int pageNumber,int pageSize,int totalCount,List<E> result) {
		if(pageSize <= 0) throw new IllegalArgumentException("[pageSize] must great than zero");
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
		this.totalCount = totalCount;
		setResult(result);
	}

	public void setResult(List<E> elements) {
		if (elements == null)
			throw new IllegalArgumentException("'result' must be not null");
		this.result = elements;
	}

	/**
     * 每一页显示的条目数
     *
     * @return 每一页显示的条目数
     */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 当前页包含的数据
	 *
	 * @return 当前页数据源
	 */
	public List<E> getResult() {
		return result;
	}

	/**
	 * 得到数据库的第一条记录号
	 * @return
	 */
	public int getFirstResult() {
		return pageNumber * pageSize;
	}

	/**
	 * 总的数据条目数量，0表示没有数据
	 *
	 * @return 总数量
	 */
	public int getTotalCount() {
		return totalCount;
	}

	@SuppressWarnings("unchecked")
	public Iterator<E> iterator() {
		return (Iterator<E>) (result == null ? Collections.emptyList().iterator() : result.iterator());
	}
}
