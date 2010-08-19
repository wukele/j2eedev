package com.starit.core.web.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.text.StrBuilder;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;


import org.apache.commons.beanutils.PropertyUtils;

/**
 * 基于Hibernate的Crud DAO基础实现，所有使用Hibernate并支持Crud操作的DAO都继承该类。<BR>
 * 所有方法都必须声明为抛出Spring统一的DataAccessException异常，以便于做统一的异常处理。<BR>
 * 可用的异常类如下：
 *
 * <pre>
 * DataAccessException
 *    CleanupFailureDataAccessException
 *    ConcurrencyFailureException
 *         OptimisticLockingFailureException
 *              ObjectOptimisticLockingFailureException
 *                   HibernateOptimisticLockingFailureException
 *         PessimisticLockingFailureException
 *              CannotAcquireLockException
 *              CannotSerializeTransactionException
 *              DeadlockLoserDataAccessException
 *    DataAccessResourceFailureException
 *         CannotCreateRecordException
 *         CannotGetCciConnectionException
 *         CannotGetJdbcConnectionException
 *    DataIntegrityViolationException
 *    DataRetrievalFailureException
 *         IncorrectResultSetColumnCountException
 *         IncorrectResultSizeDataAccessException
 *              EmptyResultDataAccessException
 *         LobRetrievalFailureException
 *         ObjectRetrievalFailureException
 *              HibernateObjectRetrievalFailureException
 *    DataSourceLookupFailureException
 *    InvalidDataAccessApiUsageException
 *    InvalidDataAccessResourceUsageException
 *         BadSqlGrammarException
 *         CciOperationNotSupportedException
 *         HibernateQueryException
 *         IncorrectUpdateSemanticsDataAccessException
 *              JdbcUpdateAffectedIncorrectNumberOfRowsException
 *         InvalidResultSetAccessException
 *         InvalidResultSetAccessException
 *         RecordTypeNotSupportedException
 *         TypeMismatchDataAccessException
 *    PermissionDeniedDataAccessException
 *    UncategorizedDataAccessException
 *         HibernateJdbcException
 *         HibernateSystemException
 *         SQLWarningException
 *         UncategorizedSQLException
 * </pre>
 *
 * @datetime 2010-7-6 下午11:00:00
 * @author libinsong1204@gmail.com
 */
public class GenericDaoImpl<E, PK extends Serializable> extends HibernateDaoSupport implements GenericDao<E, PK> {
	private SimpleJdbcTemplate simpleJdbcTemplate;

	protected Class<E> clazz;
	
	@SuppressWarnings("unchecked")
	public GenericDaoImpl() {
		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			clazz = (Class<E>) ((ParameterizedType) type).getActualTypeArguments()[0];
		}
	}

	/**
	 * 获得DAO类对应的实体类型
	 */
	protected Class<E> getClazz() {
		return clazz;
	}

	public E find(PK id) throws DataAccessException {
		return (E) super.getHibernateTemplate().get(this.getClazz(), id);
	}

	public void delete(PK id) throws DataAccessException {
		E entity = this.find(id);
		if (entity == null) {
			throw new DataRetrievalFailureException("No entity found for deletion: " + id);
		}
		super.getHibernateTemplate().delete(entity);
	}

	public E findAndLock(PK id, LockMode lockMode) throws DataAccessException {
		E entity = (E) this.getHibernateTemplate().get(this.getClazz(), id, lockMode);
		return entity;
	}

	public void create(E entity) throws DataAccessException {
		super.getHibernateTemplate().save(entity);
	}

	public void create(Collection<E> entities) throws DataAccessException {
		for(E entity : entities) {
			create(entity);
		}
	}

	public void createOrUpdate(E entity) throws DataAccessException {
		super.getHibernateTemplate().saveOrUpdate(entity);
	}

	public void createOrUpdate(Collection<E> entities) throws DataAccessException {
		for(E entity : entities) {
			createOrUpdate(entity);
		}
	}

	public void delete(E entity) throws DataAccessException {
		super.getHibernateTemplate().delete(entity);
	}

	public void delete(Collection<E> entities) throws DataAccessException {
		for(E entity : entities) {
			delete(entity);
		}
	}

	public void merge(E entity) throws DataAccessException {
		super.getHibernateTemplate().merge(entity);
	}

	public void merge(Collection<E> entities) throws DataAccessException {
		for(E entity : entities) {
			merge(entity);
		}
	}

	public void update(E entity) throws DataAccessException {
		super.getHibernateTemplate().update(entity);
	}

	public void update(Collection<E> entities) throws DataAccessException {
		for(E entity : entities) {
			update(entity);
		}
	}

	public void refresh(Object entity) throws DataAccessException {
		super.getHibernateTemplate().refresh(entity);
	}

	public List<E> findAll() throws DataAccessException {
		return super.getHibernateTemplate().loadAll(this.getClazz());
	}
	
	@SuppressWarnings("unchecked")
	public List<E> findByPropertyAndOrder(String orderCol, String orderMode) throws DataAccessException {
		Assert.hasText(orderCol, "orderCol not text");
		Assert.hasText(orderMode, "orderMode not text");

		String hql = "FROM " + this.getClazz().getSimpleName() + " ORDER BY " + orderCol + " " + orderMode;
		List<E> entities = this.getHibernateTemplate().find(hql);
		return entities;
	}
	
	@SuppressWarnings("unchecked")
	public List<E> findByPropertyAndOrder(String propertyName, Object value, String orderCol, String orderMode) 
		throws DataAccessException {
		Assert.hasText(propertyName);
		Assert.notNull(value);
		Assert.hasText(orderCol, "orderCol not text");
		Assert.hasText(orderMode, "orderMode not text");

		String hql = "FROM " + this.getClazz().getSimpleName() + " WHERE " + propertyName + " = :condition ";
		hql = hql + " ORDER BY " + orderCol + " " + orderMode;
		List<E> entities = this.getHibernateTemplate().findByNamedParam(hql, "condition", value);
		return entities;
	}
	
	@SuppressWarnings("unchecked")
	public List<E> findByPropertyAndOrder(String[] propertyNames, Object[] values, String orderCol, String orderMode) 
	 	throws DataAccessException {
		Assert.state(propertyNames.length == values.length);
		Assert.hasText(orderCol, "orderCol not text");
		Assert.hasText(orderMode, "orderMode not text");

		StrBuilder buf = new StrBuilder();
		buf.append("FROM ").append(this.getClazz().getSimpleName());
		int len = propertyNames.length;
		if(len>0) {
			for(int i=0; i<len; i++) {
				if(i==0)
					buf.append(" WHERE ").append(propertyNames[i]).append(" = :").append(propertyNames[i]);
				else
					buf.append(" and ").append(propertyNames[i]).append(" = :").append(propertyNames[i]);
			}
		}

		buf.append(" ORDER BY ").append(orderCol).append(" ").append(orderMode);
		
		List<E> entities = this.getHibernateTemplate().findByNamedParam(buf.toString(), propertyNames, values);
		return entities;
	}

	@SuppressWarnings("unchecked")
	public List<E> findByProperty(String propertyName, Object value) throws DataAccessException {
		Assert.hasText(propertyName);
		Assert.notNull(value);

		StrBuilder buf = new StrBuilder();
		buf.append("FROM ").append(this.getClazz().getSimpleName()).append(" WHERE ").append(propertyName).append(" = :condition");
		List<E> entities = this.getHibernateTemplate().findByNamedParam(buf.toString(), "condition", value);
		return entities;
	}

	@SuppressWarnings("unchecked")
	public List<E> findByProperty(String[] propertyNames, Object[] values) throws DataAccessException {
		Assert.state(propertyNames.length == values.length);

		StrBuilder buf = new StrBuilder();
		buf.append("FROM ").append(this.getClazz().getSimpleName());
		int len = propertyNames.length;
		if(len>0) {
			for(int i=0; i<len; i++) {
				if(i==0)
					buf.append(" WHERE ").append(propertyNames[i]).append(" = :").append(propertyNames[i]);
				else
					buf.append(" and ").append(propertyNames[i]).append(" = :").append(propertyNames[i]);
			}
		}

		List<E> entities = this.getHibernateTemplate().findByNamedParam(buf.toString(), propertyNames, values);
		return entities;
	}

	public Page<E> findAllForPage(final PageRequest<E> pageRequest) throws DataAccessException {
		StringBuilder queryString = new StringBuilder(" FROM ");
		StringBuilder countQueryString = new StringBuilder(" FROM ");
		
		String entityName = this.getClazz().getSimpleName();
		String postName = entityName.toLowerCase();
		queryString.append(entityName + " as " + postName + " ");// 实体名称
		countQueryString.append(entityName);// 实体名称
		for(String je : pageRequest.getJoinEntitys()) {
			queryString.append("left outer join fetch " + postName + "." + je);
		}
		
		//等于查询条件
		Map<String, Object> paramMap = pageRequest.getFilters();
		boolean hasWhere = false;
		if (paramMap != null && paramMap.size() > 0) {
			queryString.append(" WHERE ");
			countQueryString.append(" WHERE ");
			Iterator<String> keys = paramMap.keySet().iterator();
			for (int i = 0; keys.hasNext(); i++) {
				if (hasWhere) {
					queryString.append(" AND ");
					countQueryString.append(" AND ");
				} else
					hasWhere = true;
				String paramName = keys.next();
				queryString.append(paramName).append(" =:").append(paramName);
				countQueryString.append(paramName).append(" =:").append(paramName);
			}
		}
		
		//Like查询条件
		Map<String, String> likeParamMap = pageRequest.getLikeFilters();
		if (likeParamMap != null && likeParamMap.size() > 0) {
			// 增加查询条件
			if(!hasWhere) {
				queryString.append(" WHERE ");
				countQueryString.append(" WHERE ");
			}
			Iterator<String> keys = likeParamMap.keySet().iterator();
			for (int i = 0; keys.hasNext(); i++) {
				if (hasWhere) {
					queryString.append(" AND ");
					countQueryString.append(" AND ");
				} else
					hasWhere = true;
				String paramName = keys.next();
				queryString.append(paramName).append(" like :").append(paramName);
				countQueryString.append(paramName).append(" like :").append(paramName);
			}
		}

		if (org.springframework.util.StringUtils.hasText(pageRequest.getSortColumns())) {
			queryString.append(" ORDER BY " + pageRequest.getSortColumns());
		}


		//创建查询Query
		Query query = this.getSession().createQuery(queryString.toString());
		Query countQuery = this.getSession().createQuery("SELECT count(*) " + countQueryString.toString());
		
		//设置参数
		if (paramMap != null && paramMap.size() > 0) {
			Iterator<String> keys = paramMap.keySet().iterator();
			for (int i = 0; keys.hasNext(); i++) {
				String key = keys.next();
				query.setParameter(key, paramMap.get(key));
				countQuery.setParameter(key, paramMap.get(key));
			}
		}
		
		if (likeParamMap != null && likeParamMap.size() > 0) {
			Iterator<String> keys = likeParamMap.keySet().iterator();
			for (int i = 0; keys.hasNext(); i++) {
				String key = keys.next();
				String value = "%" + likeParamMap.get(key) + "%";
				query.setParameter(key, value);
				countQuery.setParameter(key, value);
			}
		}

		return executeQueryForPage(pageRequest, query, countQuery);
	}

	public int bulkUpdate(String queryString, Object... values) {
		return super.getHibernateTemplate().bulkUpdate(queryString, values);
	}

	public int bulkUpdate(String queryString) {
		return super.getHibernateTemplate().bulkUpdate(queryString);
	}

	public List<?> find(String queryString, Object... values) {
		return super.getHibernateTemplate().find(queryString, values);
	}

	public List<?> find(String queryString) {
		return super.getHibernateTemplate().find(queryString);
	}

	public List<?> findByNamedParam(String queryString, String[] paramNames, Object[] values) {
		return super.getHibernateTemplate().findByNamedParam(queryString, paramNames, values);
	}

	public List<?> findByNamedQuery(String queryName, Object[] values) {
		return super.getHibernateTemplate().findByNamedQuery(queryName, values);
	}

	public List<?> findByNamedQuery(String queryName) {
		return super.getHibernateTemplate().findByNamedQuery(queryName);
	}

	public List<?> findByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object[] values) {
		return super.getHibernateTemplate().findByNamedQueryAndNamedParam(queryName, paramNames, values);
	}

	public Long queryForLong(String queryString, Object... values) {
		return DataAccessUtils.longResult(getHibernateTemplate().find(queryString, values));
	}

	public Long queryForLong(String queryString) {
		return queryForLong(queryString, new Object[] {});
	}

	public <T> T queryForObject(Class<T> requiredType, String queryString) {
		return queryForObject(requiredType, queryString, new Object[] {});
	}

	public <T> T queryForObject(Class<T> requiredType, String queryString, Object... values) {
		return DataAccessUtils.objectResult(getHibernateTemplate().find(queryString, values), requiredType);
	}

	public boolean isUnique(E entity, String uniquePropertyNames) {
		Assert.hasText(uniquePropertyNames);
		Criteria criteria = getSession().createCriteria(this.getClazz()).setProjection(Projections.rowCount());
		String[] nameList = uniquePropertyNames.split(",");
		try {
			// 循环加入唯一列
			for (int i = 0; i < nameList.length; i++) {
				criteria.add(Restrictions.eq(nameList[i], PropertyUtils.getProperty(entity, nameList[i])));
			}

			// 以下代码为了如果是update的情况,排除entity自身.

			String idName = getSessionFactory().getClassMetadata(entity.getClass()).getIdentifierPropertyName();
			if (idName != null) {
				// 取得entity的主键值
				Serializable id = (Serializable) PropertyUtils.getProperty(entity, idName);

				// 如果id!=null,说明对象已存在,该操作为update,加入排除自身的判断
				if (id != null)
					criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
			}
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		return ((Number) criteria.uniqueResult()).intValue() == 0;
	}

	public void execute(String spName, Map<String, Object> parameters) throws DataAccessException {

	}

	public void execute(String spName) throws DataAccessException {

	}

	//--------------------------------------------private methods--------------------------------------------------

	@SuppressWarnings("unchecked")
	public <T> Page<T> executeQueryForPage(final PageRequest<T> pageRequest, Query query, Query countQuery) {
		Page<T> page = new Page<T>(pageRequest, ((Number) countQuery.uniqueResult()).intValue());
		if (page.getTotalCount() <= 0) {
			page.setResult(new ArrayList<T>(0));
		} else {
			page.setResult(query.setFirstResult(page.getFirstResult()).setMaxResults(page.getPageSize()).list());
		}
		return page;
	}

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

}