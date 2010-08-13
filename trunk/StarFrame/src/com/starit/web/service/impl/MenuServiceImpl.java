package com.starit.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starit.core.web.dao.GenericDao;
import com.starit.core.web.service.BaseServiceImpl;
import com.starit.web.dao.MenuDao;
import com.starit.web.model.Menu;
import com.starit.web.service.MenuService;

/**
 *
 * @datetime 2010-8-8 下午04:44:42
 * @author libinsong1204@gmail.com
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu, Long> implements MenuService {
	//~ Instance fields ================================================================================================
	@Autowired
	private MenuDao menuDao;

	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	@Override
	public GenericDao<Menu, Long> getGenericDao() {
		return this.menuDao;
	}
}
