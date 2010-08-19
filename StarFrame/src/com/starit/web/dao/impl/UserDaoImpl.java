package com.starit.web.dao.impl;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.starit.core.web.dao.GenericDaoImpl;
import com.starit.core.web.dao.Page;
import com.starit.core.web.dao.PageRequest;
import com.starit.web.dao.UserDao;
import com.starit.web.model.Role;
import com.starit.web.model.User;

/**
 *
 * @datetime 2010-8-8 下午04:42:05
 * @author libinsong1204@gmail.com
 */
@Repository
public class UserDaoImpl extends GenericDaoImpl<User, Long> implements UserDao {
	//~ Instance fields ================================================================================================
	
	//~ Constructors ===================================================================================================
	@Autowired
    public UserDaoImpl(SessionFactory sessionFactory){
        this.setSessionFactory(sessionFactory);
    }
	
	//~ Methods ========================================================================================================
	/**
	 * 查询所有角色信息，同时关联查询指定的用户信息
	 *
	 * @param pageRequest
	 * @return Page<Role>
	 * @throws DataAccessException
	 */
	public Page<Role> queryRoles4User(PageRequest<Role> pageRequest) throws DataAccessException {
		String queryString = "from Role as r where r.enabled='Y'";
		String countQueryString = "select count(r.id) from Role as r where r.enabled='Y'";
		Query query = this.getSession().createQuery(queryString);
		//query.setParameter("roleId", pageRequest.getFilters().get("roleId"));
		Query countQuery = this.getSession().createQuery(countQueryString);
		return this.executeQueryForPage(pageRequest, query, countQuery);
	}
}
