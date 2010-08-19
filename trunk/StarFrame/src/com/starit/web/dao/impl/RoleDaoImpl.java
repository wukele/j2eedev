package com.starit.web.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.starit.core.web.dao.GenericDaoImpl;
import com.starit.core.web.dao.Page;
import com.starit.core.web.dao.PageRequest;
import com.starit.web.dao.RoleDao;
import com.starit.web.model.Resource;
import com.starit.web.model.Role;

/**
 *
 * @datetime 2010-8-8 下午04:42:05
 * @author libinsong1204@gmail.com
 */
@Repository
public class RoleDaoImpl extends GenericDaoImpl<Role, Long> implements RoleDao {
	//~ Instance fields ================================================================================================
	
	//~ Constructors ===================================================================================================
	@Autowired
    public RoleDaoImpl(SessionFactory sessionFactory){
        this.setSessionFactory(sessionFactory);
    }
	
	//~ Methods ========================================================================================================
	/**
	 * 查询所有资源信息，同时关联查询指定的角色信息
	 *
	 * @param pageRequest
	 * @return Page<Resource>
	 * @throws DataAccessException
	 */
	public Page<Resource> queryResources4Role(PageRequest<Resource> pageRequest) throws DataAccessException {
		String queryString = "from Resource as res where res.enabled='Y' order by res.priority";
		String countQueryString = "select count(res.id) from Resource as res where res.enabled='Y'";
		Query query = this.getSession().createQuery(queryString);
		//query.setParameter("roleId", pageRequest.getFilters().get("roleId"));
		Query countQuery = this.getSession().createQuery(countQueryString);
		return this.executeQueryForPage(pageRequest, query, countQuery);
	}

	/**
	 * 通过多个角色ID，查询出相应的角色
	 * 
	 * @param idList 多个角色ID。通过List类型参数传入
	 * @return List<Role>
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<Role> queryRolesByIds(List<Long> idList) throws DataAccessException {
		Query query = this.getSession().createQuery("from Role where id in(:idList)");
		query.setParameterList("idList", idList);
		return query.list();
	}
}
