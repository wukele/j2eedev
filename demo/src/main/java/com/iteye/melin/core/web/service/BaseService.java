package com.iteye.melin.core.web.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.iteye.melin.core.page.Page;
import com.iteye.melin.core.page.PageRequest;
import com.iteye.melin.core.util.SQLOrderMode;

/**
 *
 * @datetime 2010-8-9 上午09:14:46
 * @author libinsong1204@gmail.com
 */
public interface BaseService<E, PK extends Serializable> {
	
	/**
	 * 按照实体类型和实体唯一标识查询实体。
	 * @param id
	 * @return
	 */
	public E findEntity(PK id);
	
	/**
	 * 查询实体所有数据
	 * @return List
	 */
	public List<E> findAllEntity();
	
	/**
	 * 根据属性名称与值，查询出所有满足条件的持久化实体
	 *
	 * @param propertyName 属性名称
	 * @param value 属性值
	 * @return 所有持久化实体的集合
	 */
	public List<E> findByProperty(String propertyName, Object value);
	
	/**
	 * 根据多对属性名称与值，查询出所有满足条件的持久化实体
	 *
	 * @param propertyNames 属性名称
	 * @param values 属性值
	 * @return 所有持久化实体的集合
	 * @throws DataAccessException
	 */
	public List<E> findByPropertys(String[] propertyNames, Object[] values);
	
	/**
	 * 根据属性名称与值，查询出所有满足条件的持久化实体，并排序
	 *
	 * @param propertyName 属性名称
	 * @param value 属性值
	 * @param orderCol 排序字段
	 * @param SQLOrderMode 排序模式（降序或升序）
	 * @return 所有持久化实体的集合
	 */
	public List<E> findByPropertyAndOrder(String propertyName, Object value, String orderCol, SQLOrderMode orderMode);
	
	/**
	 * 根据属性名称与值，查询出所有满足条件的持久化实体，并排序
	 *
	 * @param propertyNames 属性名称
	 * @param values 属性值集合
	 * @param orderCol 排序字段
	 * @param SQLOrderMode 排序模式（降序或升序）
	 * @return 所有持久化实体的集合
	 */

	public List<E> findByPropertysAndOrder(String[] propertyNames, Object[] values, String orderCol, SQLOrderMode orderMode);
	
	/**
	 * 根据属性名称与值，查询出所有满足条件的持久化实体，并排序
	 *
	 * @param joinEntitys 连接查询的实体名称
	 * @param propertyName 属性名称
	 * @param value 属性值
	 * @param orderCol 排序字段
	 * @param SQLOrderMode 排序模式（降序或升序）
	 * @return 所有持久化实体的集合
	 */
	public List<E> findByPropertysAndOrder(String[] joinEntitys, String propertyName, Object value, String orderCol, SQLOrderMode orderMode);
	
	/**
	 * 根据属性名称与值，查询出所有满足条件的持久化实体，并排序
	 *
	 * @param joinEntitys 连接查询的实体名称
	 * @param propertyNames 属性名称
	 * @param values 属性值
	 * @param orderCol 排序字段
	 * @param SQLOrderMode 排序模式（降序或升序）
	 * @return 所有持久化实体的集合
	 */
	public List<E> findByPropertysAndOrder(String[] joinEntitys, String[] propertyNames, Object[] values, String orderCol, SQLOrderMode orderMode);
	
	/**
	 * 按照泛型的实体类型，分页查询得到所有持久化实体。
	 * @return 所有持久化实体的集合
	 */
	Page<E> findAllForPage(PageRequest<E> pageRequest);
	
	/**
	 * 持久化一个实体，同时保存creator和createTime信息
	 *
	 * @param entity 处于临时状态的实体。
	 */
	public void insertEntity(E entity);
	
	/**
	 * 更新处于游离状态的实体，更新后实体对象仍然处于游离状态。
	 * updator 和 updateTime也做相应更新
	 *
	 * @param entity 处于游离状态的实体。
	 */
	public void updateEntity(E entity);
	
	/**
	 * 持久化或者更新实体。
	 *
	 * @param entity 处于临时或者持久化状态的实体。
	 */
	public void createOrUpdate(E entity);
	
	
	/**
	 * 根据实体唯一标识，删除这条记录
	 *
	 * @param entity 处于持久化状态的实体。
	 */
	public E deleteEntity(PK id);
}
