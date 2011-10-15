package com.iteye.melin.web.dao.base.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iteye.melin.core.web.dao.GenericDaoImpl;
import com.iteye.melin.web.dao.base.CommentDao;
import com.iteye.melin.web.model.base.Comment;

/**
 *
 * @datetime 2010-8-8 下午04:42:05
 * @author libinsong1204@gmail.com
 */
@Repository
public class CommentDaoImpl extends GenericDaoImpl<Comment, Long> implements CommentDao {
	//~ Instance fields ================================================================================================
	
	//~ Constructors ===================================================================================================
	@Autowired
    public CommentDaoImpl(SessionFactory sessionFactory){
        this.setSessionFactory(sessionFactory);
    }
	
	//~ Methods ========================================================================================================
}
