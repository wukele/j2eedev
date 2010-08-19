package com.starit.web.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.starit.core.web.dao.GenericDaoImpl;
import com.starit.web.dao.OrganizationDao;
import com.starit.web.model.Organization;

/**
 *
 * @datetime 2010-8-17 下午09:52:20
 * @author libinsong1204@gmail.com
 */
@Repository
public class OrganizationDaoImpl extends GenericDaoImpl<Organization, Long> implements OrganizationDao {
	//~ Instance fields ================================================================================================
	
	//~ Constructors ===================================================================================================
	@Autowired
    public OrganizationDaoImpl(SessionFactory sessionFactory){
        this.setSessionFactory(sessionFactory);
    }
	
	//~ Methods ========================================================================================================
}

