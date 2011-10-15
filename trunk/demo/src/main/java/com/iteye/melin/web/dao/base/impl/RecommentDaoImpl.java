package com.iteye.melin.web.dao.base.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iteye.melin.core.web.dao.GenericDaoImpl;
import com.iteye.melin.web.dao.base.RecommendDao;
import com.iteye.melin.web.model.base.Recommend;

/**
 *
 * @datetime 2010-8-8 下午04:42:05
 * @author libinsong1204@gmail.com
 */
@Repository
public class RecommentDaoImpl extends GenericDaoImpl<Recommend, Long> implements RecommendDao {
	//~ Instance fields ================================================================================================
	
	//~ Constructors ===================================================================================================
	@Autowired
    public RecommentDaoImpl(SessionFactory sessionFactory){
        this.setSessionFactory(sessionFactory);
    }
	
	//~ Methods ========================================================================================================
}
