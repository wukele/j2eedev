package com.starit.web.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	//~ Methods ========================================================================================================
	@Override
	public GenericDao<Menu, Long> getGenericDao() {
		return this.menuDao;
	}
	
	/**
	 * 保存指定节点下，所有直接子节点的顺序。
	 * 
	 * @param parentId 父节点
	 * @param childIds 所有子节点ID
	 */
	@Transactional
	public void saveMenuOrder(Long parentId, String childIds) {
		String[] childArr = StringUtils.split(childIds, ",");
		int index=1;
		for(String id :  childArr) {
			Menu menu = menuDao.find(Long.valueOf(id));
			menu.setParentId(parentId);
			menu.setTheSort(index++);
		}
	}
}
