package com.starit.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.starit.web.model.Menu;
import com.starit.web.service.MenuService;

/**
 *
 * @datetime 2010-8-8 下午04:47:03
 * @author libinsong1204@gmail.com
 */
@Controller
@RequestMapping("/menu")
public class MenuController {
	//~ Instance fields ================================================================================================
	@Autowired
	private MenuService menuService;
	
	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	@RequestMapping("/index")
	public String index(){
		return "core/menu";
	}
	
	/**
	 * 根据父菜单ID和用户权限，查找所有子菜单信息
	 * 
	 * @param node 父菜单ID
	 * @return 所有子菜单
	 */
	@RequestMapping("/findMenus4User")
	@ResponseBody
	public List<Menu> findMenus4User(Long node) {
		if(node == null)
			node = 0L;
		return menuService.findByProperty("parentId", node);
	}
	
	/**
	 * 根据父菜单ID，查找所有子菜单信息
	 * 
	 * @param node 父菜单ID
	 * @return 所有子菜单
	 */
	@RequestMapping("/findMenus")
	@ResponseBody
	public List<Menu> findMenus(Long node) {
		if(node == null)
			node = 0L;
		return menuService.findByProperty("parentId", node);
	}
}
