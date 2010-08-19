package com.starit.web.service;

import com.starit.core.web.service.BaseService;
import com.starit.web.model.Menu;

/**
 *
 * @datetime 2010-8-9 上午09:14:05
 * @author libinsong1204@gmail.com
 */
public interface MenuService extends BaseService<Menu, Long> {

	public void saveMenuOrder(Long parentId, String childIds);
}
