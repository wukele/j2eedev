package com.iteye.melin.web.dao.base.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iteye.melin.core.web.dao.GenericDaoImpl;
import com.iteye.melin.web.dao.base.NoticeDao;
import com.iteye.melin.web.model.base.Notice;

/**
 *
 * @datetime 2010-8-8 下午04:42:05
 * @author libinsong1204@gmail.com
 */
@Repository
public class NoticeDaoImpl extends GenericDaoImpl<Notice, Long> implements NoticeDao {
	//~ Instance fields ================================================================================================
	
	//~ Constructors ===================================================================================================
	@Autowired
    public NoticeDaoImpl(SessionFactory sessionFactory){
        this.setSessionFactory(sessionFactory);
    }
	
	//~ Methods ========================================================================================================
}
