package com.iteye.melin.core.web.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.LockMode;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.iteye.melin.core.page.Page;
import com.iteye.melin.core.page.PageRequest;
import com.iteye.melin.core.util.SQLOrderMode;

/**
 * 提供了常用增删改查(CRUD)功能的DAO基础接口。<BR>
 * 实体状态说明：
 *
 * <pre>
 *     持久化状态：已经被持久化了且与当前Session关联的实体状态。
 *     临时状态：没有被持久化过的实体状态。
 *     游离状态：已经被持久化，但是没有与当前Session关联的实体对象，且有相同标识的对象与当前Session关联。
 * </pre>
 *
 * @datetime 2010-7-6 下午08:19:49
 * @author libinsong1204@gmail.com
 */
public interface GenericDao<E, PK extends Serializable> {
	/**
	 * 按照实体类型和实体唯一标识查询实体。
	 * @param id
	 * @return
	 */
	public E find(PK id);

	/**
	 * 按照实体类型和实体唯一标识查询实体，并锁定该实体对象，直到事务结束。
	 * @param id
	 * @param lockMode
	 * @return
	 */
	public E findAndLock(PK id, LockMode lockMode);

	/**
	 * 删除实体主键id标识的实体。
	 *
	 * @param id
	 */
	public void delete(PK id);

	/**
	 * 持久化一个实体。
	 *
	 * @param entity 处于临时状态的实体。
	 */
	public void create(E entity);

	/**
	 * 持久化多个实体。
	 *
	 * @param entities 处于临时状态的实体的集合。
	 */
	public void create(Collection<E> entities);

	/**
	 * 更新实体。
	 *
	 * @param entity 处于持久化状态的实体。
	 */
	public void update(E entity);

	/**
	 * 更新多个实体。
	 *
	 * @param entities 处于持久化状态的实体的集合。
	 */
	public void update(Collection<E> entities);

	/**
	 * 持久化或者更新实体。
	 *
	 * @param entity 处于临时或者持久化状态的实体。
	 */
	public void createOrUpdate(E entity);

	/**
	 * 持久化或者更新多个实体。
	 *
	 * @param entities 处于临时或者持久化状态的实体的集合。
	 */
	public void createOrUpdate(Collection<E> entities);

	/**
	 * 更新处于游离状态的实体，更新后实体对象仍然处于游离状态。
	 *
	 * @param entity 处于游离状态的实体。
	 */
	public void merge(E entity);

	/**
	 * 保存处于游离状态的多个实体，更新后实体对象仍然处于游离状态。
	 *
	 * @param entities 处于游离状态的实体的集合。
	 */
	public void merge(Collection<E> entities);

	/**
	 * 删除一个持久化的实体。
	 *
	 * @param entity 处于持久化状态的实体。
	 */
	public void delete(E entity);

	/**
	 * 删除多个持久化的实体。
	 *
	 * @param entities 处于持久化状态的实体的集合。
	 */
	public void delete(Collection<E> entities);
	
	/**
	 * 批量删除
	 * 
	 * @param entity
	 * @param propertyNames
	 * @param values
	 */
	public void bulkDelete(Class<E> clazz, String[] propertyNames, Object[] values);

	/**
	 * 刷新持久化实体到数据库
	 *
	 * @param entity 处于持久化状态的实体。
	 */
	public void refresh(Object entity);

	/**
	 * 按照泛型的实体类型查询得到所有持久化实体。
	 *
	 * @return 所有持久化实体的集合
	 */
	public List<E> findAll();
	
	/**
	 * 查询出所有的持久化实体，并排序
	 *
	 * @param orderCol 排序字段
	 * @param orderMode 排序模式（降序或升序）
	 * @return 所有持久化实体的集合
	 */
	public List<E> findByOrder(String orderCol, SQLOrderMode orderMode);
	
	/**
	 * 根据属性名称与值，查询出所有满足条件的持久化实体，并排序
	 *
	 * @param propertyName 属性名称
	 * @param value 属性值
	 * @param orderCol 排序字段
	 * @param orderMode 排序模式（降序或升序）
	 * @return 所有持久化实体的集合
	 */
	public List<E> findByPropertyAndOrder(String propertyName, Object value, String orderCol, SQLOrderMode orderMode);
	
	/**
	 * 根据多对属性名称与值，查询出所有满足条件的持久化实体，并排序
	 *
	 * @param propertyNames 属性名称
	 * @param values 属性值
	 * @param orderCol 排序字段
	 * @param orderMode 排序模式（降序或升序）
	 * @return 所有持久化实体的集合
	 */
	public List<E> findByPropertysAndOrder(String[] propertyNames, Object[] values, String orderCol, SQLOrderMode orderMode);
	
	/**
	 * 根据多对属性名称与值，查询出所有满足条件的持久化实体，并排序
	 *
	 * @param joinEntitys 连接查询的实体名称
	 * @param propertyNames 属性名称
	 * @param values 属性值
	 * @param orderCol 排序字段
	 * @param orderMode 排序模式（降序或升序）
	 * @return 所有持久化实体的集合
	 */
	public List<E> findByPropertysAndOrder(String[] joinEntitys, String[] propertyNames, Object[] values, String orderCol, SQLOrderMode orderMode);

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
	 * 按照泛型的实体类型，分页查询得到所有持久化实体。
	 * @return 所有持久化实体的集合
	 */
	Page<E> findAllForPage(PageRequest<E> pageRequest);

	/**
	 * 查询结果返回为长整形
	 *
	 * @param queryString HQL语句
	 * @return
	 */
	public Long queryForLong(final String queryString);

	/**
	 * 查询结果返回为长整形
	 *
	 * @param queryString HQL语句
	 * @param values HQL语句参数
	 * @return
	 */
	public Long queryForLong(final String queryString, Object... values);

	/**
	 * 查询结果返回为类型T
	 *
	 * @param requiredType 返回数据类型
	 * @param queryString HQL语句
	 * @return
	 */
	public <T> T queryForObject(Class<T> requiredType, final String queryString);

	/**
	 * 查询结果返回为类型T
	 *
	 * @param requiredType 返回数据类型
	 * @param queryString HQL语句
	 * @param values HQL语句参数
	 * @return
	 */
	public <T> T queryForObject(Class<T> requiredType, final String queryString, Object... values);

	/**
	 * 使用HSQL语句直接增加、更新、删除实体
	 *
	 * @param queryString HQL语句
	 * @return
	 */
	public int bulkUpdate(String queryString);

	/**
	 * 使用带参数HSQL语句直接增加、更新、删除实体
	 *
	 * @param queryString HQL语句
	 * @param values HQL语句参数
	 * @return
	 */
    public int bulkUpdate(String queryString, Object... values);

    /**
     * 使用HSQL语句检索数据
     *
     * @param queryString HQL语句
     * @return
     */
    public List<?> find(String queryString);

    /**
     * 使用带参数的HSQL语句检索数据
     *
     * @param queryString HQL语句
     * @param values HQL语句参数
     * @return
     */
    public List<?> find(String queryString, Object... values);

    /**
     * 使用带命名的参数的HSQL语句检索数据
     *
     * @param queryString
     * @param paramNames
     * @param values
     * @return
     */
    public List<?> findByNamedParam(String queryString, String[] paramNames,
            Object[] values);

    /**
     * 使用命名的HSQL语句检索数据
     *
     * @param queryName
     * @return
     */
    public List<?> findByNamedQuery(String queryName);

    /**
     * 使用带参数的命名HSQL语句检索数据
     *
     * @param queryName
     * @param values
     * @return
     */
    public List<?> findByNamedQuery(String queryName, Object[] values);

    /**
     * 使用带命名参数的命名HSQL语句检索数据
     *
     * @param queryName
     * @param paramNames
     * @param values
     * @return
     */
    public List<?> findByNamedQueryAndNamedParam(String queryName,
            String[] paramNames, Object[] values);

    /**
	 * 判断对象某些属性的值在数据库中是否唯一.
	 *
	 * @param uniquePropertyNames
	 *            在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
	 */
	public boolean isUnique(E entity, String uniquePropertyNames);

	/**
	 * 执行存储过程
	 * 使用Spring JDBC 来调用存储过程
	 *
	 * @param spName 存储过程名称
	 * @param parameters 存储过程参数
	 */
	public void execute(String spName, Map<String, Object> parameters);

	/**
	 * 执行存储过程
	 * 使用Spring JDBC 来调用存储过程
	 *
	 * @param spName 存储过程名称
	 * @throws DataAccessException
	 */
	public void execute(String spName);
	
	/**
	 * 
	 * @return NamedParameterJdbcTemplate
	 */
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate();
	
	/**
	 * 
	 * @return JdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate();
}
