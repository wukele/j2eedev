package com.starit.web.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.starit.core.web.dao.GenericDaoImpl;
import com.starit.web.dao.OperLogDao;
import com.starit.web.model.OperLog;

/**
 *
 * @datetime 2010-8-18 下午03:10:52
 * @author libinsong1204@gmail.com
 */
@Repository
public class OperLogDaoImpl extends GenericDaoImpl<OperLog, Long> implements OperLogDao {
	//~ Instance fields ================================================================================================
	
	//~ Constructors ===================================================================================================
	@Autowired
    public OperLogDaoImpl(SessionFactory sessionFactory){
        this.setSessionFactory(sessionFactory);
    }
	
	//~ Methods ========================================================================================================

}
