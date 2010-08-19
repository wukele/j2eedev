package com.starit.core.web.service;

import java.io.Serializable;
import java.util.List;

import com.starit.core.util.SQLOrderMode;
import com.starit.core.web.dao.Page;
import com.starit.core.web.dao.PageRequest;

/**
 *
 * @datetime 2010-8-9 上午09:14:46
 * @author libinsong1204@gmail.com
 */
public interface BaseService<E, PK extends Serializable> {
	
	public E findEntity(PK id);
	
	public List<E> findByProperty(String propertyName, Object value);
	
	public List<E> findByPropertyAndOrder(String propertyName, Object value, String orderCol, SQLOrderMode mode);
	
	Page<E> findAllForPage(PageRequest<E> pageRequest);
	
	public void insertEntity(E entity);
	
	public void updateEntity(E entity);
	
	public void deleteEntity(PK id);
	
    public List<E> find(String queryString, Object... values);
}
