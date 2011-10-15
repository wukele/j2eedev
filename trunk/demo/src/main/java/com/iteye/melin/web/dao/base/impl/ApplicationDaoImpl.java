package com.iteye.melin.web.dao.base.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iteye.melin.core.web.dao.GenericDaoImpl;
import com.iteye.melin.web.dao.base.ApplicationDao;
import com.iteye.melin.web.model.base.Application;

/**
 *
 * @datetime 2010-8-8 下午04:42:05
 * @author libinsong1204@gmail.com
 */
@Repository
public class ApplicationDaoImpl extends GenericDaoImpl<Application, Long> implements ApplicationDao {
	//~ Instance fields ================================================================================================
	
	//~ Constructors ===================================================================================================
	@Autowired
    public ApplicationDaoImpl(SessionFactory sessionFactory){
        this.setSessionFactory(sessionFactory);
    }
	
	//~ Methods ========================================================================================================
}
