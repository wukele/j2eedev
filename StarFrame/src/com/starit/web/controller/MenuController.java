package com.starit.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.starit.core.log.OperationDescription;
import com.starit.core.log.OperationType;
import com.starit.core.util.ResponseData;
import com.starit.core.util.SQLOrderMode;
import com.starit.web.model.Menu;
import com.starit.web.service.MenuService;

/**
 * 菜单Controller
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
		return menuService.findByPropertyAndOrder("parentId", node, "theSort", SQLOrderMode.ASC);
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
		return menuService.findByPropertyAndOrder("parentId", node, "theSort", SQLOrderMode.ASC);
	}
	
	/**
	 * 保存指定节点下，所有直接子节点的顺序。
	 * 
	 * @param parentId 父节点
	 * @param childIds 所有子节点ID有逗号连接的字符串
	 * @return
	 */
	@RequestMapping("/saveMenuOrder")
	@ResponseBody
	@OperationDescription(description="菜单排序", type=OperationType.ORDER)
	public ResponseData saveMenuOrder(Long parentId, String childIds) {
		menuService.saveMenuOrder(parentId, childIds);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 插入机构信息
	 * 
	 * @param menu
	 * @return
	 */
	@RequestMapping(value="/insertMenu", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData insertMenu(Menu menu) {
		if(menu.getResource().getId() == null)
			menu.setResource(null);
		menuService.insertEntity(menu);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 更新菜单信息
	 * 
	 * @param menu
	 * @return
	 */
	@RequestMapping(value="/updateMenu", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData updateMenu(Menu menu) {
		if(menu.getResource().getId() == null)
			menu.setResource(null);
		menuService.updateEntity(menu);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 加载菜单信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/loadMenu", method=RequestMethod.POST)
	@ResponseBody
	public Menu loadMenu(Long id) {
		return menuService.findEntity(id);
	}
	
	/**
	 * 删除菜单
	 * 
	 * @param parentId 父节点
	 * @param childIds 所有子节点ID有逗号连接的字符串
	 * @return
	 */
	@RequestMapping("/deleteMenu")
	@ResponseBody
	public ResponseData deleteMenu(long id) {
		menuService.deleteEntity(id);
		return ResponseData.SUCCESS_NO_DATA;
	}
}
