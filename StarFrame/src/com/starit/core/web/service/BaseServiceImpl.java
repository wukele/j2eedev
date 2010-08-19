package com.starit.core.web.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import com.starit.core.util.SQLOrderMode;
import com.starit.core.util.SecurityContextUtil;
import com.starit.core.web.dao.GenericDao;
import com.starit.core.web.dao.Page;
import com.starit.core.web.dao.PageRequest;
import com.starit.core.web.model.BaseEntity;

/**
 * 服务层，公共类
 * 
 * @datetime 2010-8-9 上午09:15:19
 * @author libinsong1204@gmail.com
 */
abstract public class BaseServiceImpl<E, PK extends Serializable> implements BaseService<E, PK> {
	//~ Instance fields ================================================================================================
	public Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	abstract public GenericDao<E, PK> getGenericDao();
	
	/**
	 * 按照实体类型和实体唯一标识查询实体。
	 * @param id
	 * @return
	 */
	@Transactional(readOnly=true)
	public E findEntity(PK id) {
		return this.getGenericDao().find(id);
	}
	
	/**
	 * 根据属性名称与值，查询出所有满足条件的持久化实体
	 *
	 * @param propertyName 属性名称
	 * @param value 属性值
	 * @return 所有持久化实体的集合
	 */
	@Transactional(readOnly=true)
	public List<E> findByProperty(String propertyName, Object value) {
		return this.getGenericDao().findByProperty(propertyName, value);
	}
	
	/**
	 * 根据属性名称与值，查询出所有满足条件的持久化实体，并排序
	 *
	 * @param propertyName 属性名称
	 * @param value 属性值
	 * @param orderCol 排序字段
	 * @param SQLOrderMode 排序模式（降序或升序）
	 * @return 所有持久化实体的集合
	 * @throws DataAccessException
	 */
	@Transactional(readOnly=false)
	public List<E> findByPropertyAndOrder(String propertyName, Object value, String orderCol, SQLOrderMode mode) {
		return this.getGenericDao().findByPropertyAndOrder(propertyName, value, orderCol, mode.getMode());
	}
	
	/**
	 * 按照泛型的实体类型，分页查询得到所有持久化实体。
	 * @return 所有持久化实体的集合
	 */
	@Transactional(readOnly=true)
	public Page<E> findAllForPage(PageRequest<E> pageRequest) {
		return this.getGenericDao().findAllForPage(pageRequest);
	}
	
	/**
	 * 持久化一个实体，同时保存creator和createTime信息
	 *
	 * @param entity 处于临时状态的实体。
	 */
	@Transactional
	public void insertEntity(E entity) {
		if(ClassUtils.isAssignable(BaseEntity.class, entity.getClass())) {
			String creator = SecurityContextUtil.getLoginUserId();
			BaseEntity baseEntity = (BaseEntity)entity;
			baseEntity.setCreator(creator);
			baseEntity.setCreateTime(new Date());
		}
		
		this.getGenericDao().create(entity);
	}
	
	/**
	 * 更新处于游离状态的实体，更新后实体对象仍然处于游离状态。
	 * updator 和 updateTime也做相应更新
	 *
	 * @param entity 处于游离状态的实体。
	 */
	@Transactional
	public void updateEntity(E entity) {
		if(ClassUtils.isAssignable(BaseEntity.class, entity.getClass())) {
			String updator = SecurityContextUtil.getLoginUserId();
			BaseEntity baseEntity = (BaseEntity)entity;
			baseEntity.setUpdator(updator);
			baseEntity.setUpdateTime(new Date());
		}
		
		this.getGenericDao().merge(entity);
	}
	
	/**
	 * 根据实体唯一标识，删除这条记录
	 *
	 * @param entity 处于持久化状态的实体。
	 */
	@Transactional
	public void deleteEntity(PK id) {
		E entity = this.getGenericDao().find(id);
		
		if (entity != null) {
			this.getGenericDao().delete(entity);
		} else {
			logger.info("delete PK={}, but not exist", id);
		}
	}
	
	/**
     * 使用带参数的HSQL语句检索数据
     *
     * @param queryString HQL语句
     * @param values HQL语句参数
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<E> find(String queryString, Object... values) {
    	return (List<E>) this.getGenericDao().find(queryString, values);
    }
}
