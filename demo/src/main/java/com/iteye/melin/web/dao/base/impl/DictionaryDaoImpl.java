package com.iteye.melin.web.dao.base.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iteye.melin.core.web.dao.GenericDaoImpl;
import com.iteye.melin.web.dao.base.DictionaryDao;
import com.iteye.melin.web.model.base.Dictionary;

/**
 *
 * @datetime 2010-8-21 上午08:11:15
 * @author libinsong1204@gmail.com
 */
@Repository
public class DictionaryDaoImpl extends GenericDaoImpl<Dictionary, Long> implements DictionaryDao {
	//~ Instance fields ================================================================================================
	
	//~ Constructors ===================================================================================================
	@Autowired
    public DictionaryDaoImpl(SessionFactory sessionFactory){
        this.setSessionFactory(sessionFactory);
    }
	
	//~ Methods ========================================================================================================
}
