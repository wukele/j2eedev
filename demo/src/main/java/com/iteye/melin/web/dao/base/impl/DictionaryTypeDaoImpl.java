package com.iteye.melin.web.dao.base.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iteye.melin.core.web.dao.GenericDaoImpl;
import com.iteye.melin.web.dao.base.DictionaryTypeDao;
import com.iteye.melin.web.model.base.DictionaryType;

/**
 *
 * @datetime 2010-8-20 下午10:22:06
 * @author libinsong1204@gmail.com
 */
@Repository
public class DictionaryTypeDaoImpl extends GenericDaoImpl<DictionaryType, Long> implements DictionaryTypeDao {
	//~ Instance fields ================================================================================================
	
	//~ Constructors ===================================================================================================
	@Autowired
    public DictionaryTypeDaoImpl(SessionFactory sessionFactory){
        this.setSessionFactory(sessionFactory);
    }
	
	//~ Methods ========================================================================================================

}
