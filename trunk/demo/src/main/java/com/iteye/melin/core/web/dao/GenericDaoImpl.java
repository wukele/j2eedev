package com.iteye.melin.core.web.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.iteye.melin.core.page.Page;
import com.iteye.melin.core.page.PageRequest;
import com.iteye.melin.core.util.BlankMode;
import com.iteye.melin.core.util.LikeMode;
import com.iteye.melin.core.util.SQLOrderMode;

/**
 * 基于Hibernate的Crud DAO基础实现，所有使用Hibernate并支持Crud操作的DAO都继承该类。<BR>
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
	@Autowired(required=false)
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired(required=false)
	protected JdbcTemplate jdbcTemplate;

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

	public E find(PK id) {
		return (E) super.getHibernateTemplate().get(this.getClazz(), id);
	}

	public void delete(PK id) {
		E entity = this.find(id);
		if (entity == null) {
			throw new DataRetrievalFailureException("No entity found for deletion: " + id);
		}
		super.getHibernateTemplate().delete(entity);
	}

	public E findAndLock(PK id, LockMode lockMode) {
		E entity = (E) this.getHibernateTemplate().get(this.getClazz(), id, lockMode);
		return entity;
	}

	public void create(E entity) {
		super.getHibernateTemplate().save(entity);
	}

	public void create(Collection<E> entities) {
		for(E entity : entities) {
			create(entity);
		}
	}

	public void createOrUpdate(E entity) {
		super.getHibernateTemplate().saveOrUpdate(entity);
	}

	public void createOrUpdate(Collection<E> entities) {
		for(E entity : entities) {
			createOrUpdate(entity);
		}
	}

	public void delete(E entity) {
		super.getHibernateTemplate().delete(entity);
	}

	public void delete(Collection<E> entities) {
		for(E entity : entities) {
			delete(entity);
		}
	}
	
	public void bulkDelete(Class<E> clazz, String[] propertyNames, Object[] values) {
		StrBuilder buf = new StrBuilder();
		String entityName = clazz.getSimpleName();
		buf.append("delete " + entityName);// 实体名称
		
		int len = propertyNames.length;
		for(int i=0; i<len; i++) {
			if(i==0)
				buf.append(" WHERE ").append(propertyNames[i]).append(" = :").append(propertyNames[i]);
			else
				buf.append(" and ").append(propertyNames[i]).append(" = :").append(propertyNames[i]);
		}
		
		 Query query = this.getSession().createQuery(buf.toString());
		 for(int i=0; i<len; i++) {
			 query.setParameter(propertyNames[i], values[i]);
		 }
         query.executeUpdate();
	}

	public void merge(E entity) {
		super.getHibernateTemplate().merge(entity);
	}

	public void merge(Collection<E> entities) {
		for(E entity : entities) {
			merge(entity);
		}
	}

	public void update(E entity) {
		super.getHibernateTemplate().update(entity);
	}

	public void update(Collection<E> entities) {
		for(E entity : entities) {
			update(entity);
		}
	}

	public void refresh(Object entity) {
		super.getHibernateTemplate().refresh(entity);
	}

	public List<E> findAll() {
		return this.findByPropertysAndOrder(new String[]{}, new String[]{}, new Object[]{}, null, SQLOrderMode.NOSORT);
	}
	
	public List<E> findByOrder(String orderCol, SQLOrderMode orderMode) {
		Assert.hasText(orderCol, "orderCol not text");
		Assert.notNull(orderMode, "orderMode not null");
		
		return this.findByPropertysAndOrder(new String[]{}, new String[]{}, new Object[]{}, orderCol, orderMode);
	}
	
	public List<E> findByPropertyAndOrder(String propertyName, Object value, String orderCol, SQLOrderMode orderMode) {
		Assert.hasText(propertyName);
		Assert.notNull(value);
		Assert.hasText(orderCol, "orderCol not text");
		Assert.notNull(orderMode, "orderMode not null");

		return this.findByPropertysAndOrder(new String[]{}, new String[]{propertyName}, new Object[]{value}, orderCol, orderMode);
	}
	
	public List<E> findByPropertysAndOrder(String[] propertyNames, Object[] values, String orderCol, SQLOrderMode orderMode) {
		Assert.state(propertyNames.length == values.length);
		Assert.hasText(orderCol, "orderCol not text");
		Assert.notNull(orderMode, "orderMode not null");

		return this.findByPropertysAndOrder(new String[]{}, propertyNames, values, orderCol, orderMode);
	}
	
	@SuppressWarnings("unchecked")
	public List<E> findByPropertysAndOrder(String[] joinEntitys, String[] propertyNames, Object[] values, String orderCol, SQLOrderMode orderMode) {
		StrBuilder buf = new StrBuilder();
		
		String entityName = this.getClazz().getSimpleName();
		String postName = entityName.toLowerCase();
		buf.append("FROM " + entityName + " as " + postName + " ");// 实体名称

		if(joinEntitys != null) {
			for(String je : joinEntitys) {
				buf.append(" left outer join fetch " + postName + "." + je + " " + je + " ");
			}
		}
		
		int len = propertyNames.length;
		List<String> propertyNameList = new ArrayList<String>(propertyNames.length);
		List<Object> valueList = new ArrayList<Object>(values.length);
		if(len>0) {
			for(int i=0; i<len; i++) {
				//如果是级联属性标示（例如：entitytype.id"），需要替换.为下划线
				String old = propertyNames[i];
				propertyNames[i] = propertyNames[i].replaceAll("\\.", "_");
				if(i==0)
					buf.append(" WHERE ");
				else
					buf.append(" and ");
				
				Object value = values[i];
				if(BlankMode.NULL == value)
					buf.append(old).append(" is null ");
				else if(BlankMode.NOTNULL == value)
					buf.append(old).append(" is not null");
				else if(LikeMode.LEFT == value) {
					buf.append(old).append(" like :").append(propertyNames[i]);
					propertyNameList.add(propertyNames[i]);
					valueList.add("%" + values[i]);
				} else if(LikeMode.RIGHT == value) {
					buf.append(old).append(" like :").append(propertyNames[i]);
					propertyNameList.add(propertyNames[i]);
					valueList.add(values[i] + "%");
				} else if(LikeMode.ALL == value) {
					buf.append(old).append(" like :").append(propertyNames[i]);
					propertyNameList.add(propertyNames[i]);
					valueList.add("%" + values[i] + "%");
				} else {
					buf.append(old).append(" = :").append(propertyNames[i]);
					propertyNameList.add(propertyNames[i]);
					valueList.add(values[i]);
				}
			}
		}

		//添加排序字段
		if(StringUtils.hasText(orderCol) &&  SQLOrderMode.NOSORT != orderMode)
			buf.append(" ORDER BY ").append(orderCol).append(" ").append(orderMode.getMode());
		
		String[] properties = new String[propertyNameList.size()];
		for(int i=0, count=propertyNameList.size(); i<count; i++) {
			properties[i] = propertyNameList.get(i);
		}
		return this.getHibernateTemplate().findByNamedParam(buf.toString(), properties, valueList.toArray());
	}

	public List<E> findByProperty(String propertyName, Object value) {
		Assert.hasText(propertyName);
		Assert.notNull(value);

		return this.findByPropertysAndOrder(new String[]{}, new String[]{propertyName}, new Object[]{value}, null, SQLOrderMode.NOSORT);
	}

	public List<E> findByPropertys(String[] propertyNames, Object[] values) {
		Assert.state(propertyNames.length == values.length);

		return this.findByPropertysAndOrder(new String[]{}, propertyNames, values, null, SQLOrderMode.NOSORT);
	}

	public Page<E> findAllForPage(final PageRequest<E> pageRequest) {
		StringBuilder queryString = new StringBuilder(" FROM ");
		StringBuilder countQueryString = new StringBuilder(" FROM ");
		
		String entityName = this.getClazz().getSimpleName();
		String postName = entityName.toLowerCase();
		queryString.append(entityName + " as " + postName + " ");// 实体名称
		countQueryString.append(entityName);// 实体名称
		for(String je : pageRequest.getJoinEntitys()) {
			queryString.append("left outer join fetch " + postName + "." + je + " " + je + " ");
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
		
		//时间范围条件
		String timeField = pageRequest.getTimeField();
		if(StringUtils.hasText(timeField)) {
			if(pageRequest.getStartTime() != null) {
				if(!hasWhere) {
					queryString.append(" WHERE ");
					countQueryString.append(" WHERE ");
					hasWhere = true;
				} else {
					queryString.append(" AND ");
					countQueryString.append(" AND ");
				}
				queryString.append(timeField).append(" >= :startTime");
				countQueryString.append(timeField).append(" >= :startTime");
			}
			
			if(pageRequest.getEndTime() != null) {
				if(!hasWhere) {
					queryString.append(" WHERE ");
					countQueryString.append(" WHERE ");
					hasWhere = true;
				} else {
					queryString.append(" AND ");
					countQueryString.append(" AND ");
				}
				queryString.append(timeField).append(" <= :endTime");
				countQueryString.append(timeField).append(" <= :endTime");
			}
		}

		//排序字段
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
		
		//时间范围条件参数
		if(StringUtils.hasText(timeField)) {
			if(pageRequest.getStartTime() != null) {
				query.setParameter("startTime", pageRequest.getStartTime());
				countQuery.setParameter("startTime", pageRequest.getStartTime());
			}
			
			if(pageRequest.getEndTime() != null) {
				query.setParameter("endTime", pageRequest.getEndTime());
				countQuery.setParameter("endTime", pageRequest.getEndTime());
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
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		return ((Number) criteria.uniqueResult()).intValue() == 1;
	}

	public void execute(String spName, Map<String, Object> parameters) {

	}

	public void execute(String spName) {

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

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
}