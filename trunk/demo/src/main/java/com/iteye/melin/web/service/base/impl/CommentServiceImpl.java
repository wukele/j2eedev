package com.iteye.melin.web.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iteye.melin.core.web.dao.GenericDao;
import com.iteye.melin.core.web.service.BaseServiceImpl;
import com.iteye.melin.web.dao.base.CommentDao;
import com.iteye.melin.web.model.base.Comment;
import com.iteye.melin.web.service.base.CommentService;

/**
 *
 * @datetime 2010-8-8 下午04:44:42
 * @author libinsong1204@gmail.com
 */
@Service
public class CommentServiceImpl extends BaseServiceImpl<Comment, Long> implements CommentService {
	//~ Instance fields ================================================================================================
	@Autowired
	private CommentDao commentDao;

	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	@Override
	public GenericDao<Comment, Long> getGenericDao() {
		return this.commentDao;
	}
	
}
