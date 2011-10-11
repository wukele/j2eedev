package com.iteye.melin.core.web.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.jdbc.util.BasicFormatterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.iteye.melin.core.page.Page;
import com.iteye.melin.core.page.PageRequest;
import com.iteye.melin.core.util.SQLOrderMode;
import com.iteye.melin.core.web.dao.GenericDao;

/**
 * 服务层，公共类
 * 
 * @datetime 2010-8-9 上午09:15:19
 * @author libinsong1204@gmail.com
 */
abstract public class BaseServiceImpl<E, PK extends Serializable> implements BaseService<E, PK> {
	//~ Instance fields ================================================================================================
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	protected BasicFormatterImpl formatter;
	
	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	abstract public GenericDao<E, PK> getGenericDao();

	@Transactional(readOnly=true)
	public E findEntity(PK id) {
		return this.getGenericDao().find(id);
	}
	
	@Transactional(readOnly=true)
	public List<E> findAllEntity() {
		return this.getGenericDao().findAll();
	}
	
	@Transactional(readOnly=true)
	public List<E> findByProperty(String propertyName, Object value) {
		return this.getGenericDao().findByProperty(propertyName, value);
	}
	
	@Transactional(readOnly=true)
	public List<E> findByPropertys(String[] propertyNames, Object[] values) {
		return this.getGenericDao().findByPropertys(propertyNames, values);
	}
	
	@Transactional(readOnly=false)
	public List<E> findByPropertyAndOrder(String propertyName, Object value, String orderCol, SQLOrderMode mode) {
		return this.getGenericDao().findByPropertyAndOrder(propertyName, value, orderCol, mode);
	}
	
	@Transactional(readOnly=false)
	public List<E> findByPropertysAndOrder(String[] propertyNames, Object[] values, String orderCol, SQLOrderMode orderMode) {
		return this.getGenericDao().findByPropertysAndOrder(propertyNames, values, orderCol, orderMode);
	}
	
	@Transactional(readOnly=true)
	public List<E> findByPropertysAndOrder(String[] joinEntitys, String propertyName, Object value, String orderCol, SQLOrderMode orderMode) {
		return this.getGenericDao().findByPropertysAndOrder(joinEntitys, new String[]{propertyName}, new Object[]{value}, orderCol, orderMode);
	}
	
	@Transactional(readOnly=true)
	public List<E> findByPropertysAndOrder(String[] joinEntitys, String[] propertyNames, Object[] values, String orderCol, SQLOrderMode orderMode) {
		return this.getGenericDao().findByPropertysAndOrder(joinEntitys, propertyNames, values, orderCol, orderMode);
	}
	
	@Transactional(readOnly=true)
	public Page<E> findAllForPage(PageRequest<E> pageRequest) {
		return this.getGenericDao().findAllForPage(pageRequest);
	}
	
	@Transactional
	public void insertEntity(E entity) {
		this.getGenericDao().create(entity);
	}
	
	@Transactional
	public void updateEntity(E entity) {
		this.getGenericDao().merge(entity);
	}
	
	@Transactional
	public void createOrUpdate(E entity) {
		this.getGenericDao().createOrUpdate(entity);
	}
	
	@Transactional
	public E deleteEntity(PK id) {
		E entity = this.getGenericDao().find(id);
		
		if (entity != null) {
			this.getGenericDao().delete(entity);
		} else {
			logger.info("delete PK={}, but not exist", id);
		}
		return entity;
	}
}
