package com.starit.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.starit.core.util.ResponseData;
import com.starit.core.web.dao.Page;
import com.starit.core.web.dao.PageRequest;
import com.starit.web.model.Resource;
import com.starit.web.model.Role;
import com.starit.web.service.RoleService;

/**
 * 角色Controller
 *
 * @datetime 2010-8-8 下午04:47:03
 * @author libinsong1204@gmail.com
 */
@Controller
@RequestMapping("/role")
public class RoleController {
	//~ Instance fields ================================================================================================
	@Autowired
	private RoleService roleService;
	
	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	@RequestMapping("/index")
	public String index(){
		return "core/role";
	}
	
	/**
	 * 角色管理，查询角色信息
	 * 
	 * @param pageRequest
	 * @return
	 */
	@RequestMapping("/pageQueryRoles")
	@ResponseBody
	public Page<Role> pageQueryRoles(@RequestParam("start")int pageNumber, 
			@RequestParam("limit")int pageSize, Role role, String sort, String dir) {
		PageRequest<Role> pageRequest = new PageRequest<Role>(pageNumber, pageSize);
		
		if(StringUtils.hasText(sort) && StringUtils.hasText(dir))
			pageRequest.setSortColumns(sort + " " + dir);
		
		Map<String, Object> paramMap = pageRequest.getFilters();
		if((int)role.getEnabled() != 0)
			paramMap.put("enabled", role.getEnabled());
		
		Map<String, String> likeFilters = pageRequest.getLikeFilters();
		if(StringUtils.hasText(role.getName()))
			likeFilters.put("name", role.getName());
			
		return roleService.findAllForPage(pageRequest);
	}
	
	/**
	 * 保存角色, 只接受POST请求
	 * 
	 * @param resource
	 * @return ResponseData
	 */
	@RequestMapping(value="/insertRole", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData insertResource(Role role) {
		roleService.insertEntity(role);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 更新角色, 只接受POST请求
	 * 
	 * @param resource
	 * @return ResponseData
	 */
	@RequestMapping(value="/updateRole", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData updateResource(Role role) {
		roleService.updateEntity(role);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 加载角色, 只接受POST请求
	 * 
	 * @param resource
	 * @return ResponseData
	 */
	@RequestMapping(value="/loadRole", method=RequestMethod.POST)
	@ResponseBody
	public Role loadResource(Long id) {
		return roleService.findEntity(id);
	}
	
	/**
	 * 删除角色, 只接受POST请求
	 * 
	 * @param resource
	 * @return ResponseData
	 */
	@RequestMapping(value="/deleteRole", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData deleteRole(Long id) {
		roleService.deleteEntity(id);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 查询所有资源信息，同时关联查询指定的角色信息
	 *
	 * @param roleId
	 */
	@RequestMapping(value="/queryResources4Role", method=RequestMethod.POST)
	@ResponseBody
	public Page<Resource> queryResources4Role(@RequestParam("start")int pageNumber, 
			@RequestParam("limit")int pageSize, Long roleId)  {
		PageRequest<Resource> pageRequest = new PageRequest<Resource>(pageNumber, pageSize);
		return roleService.queryResources4Role(pageRequest, roleId);
	}
	
	/**
	 * 给资源绑定角色, 只接受POST请求
	 * 
	 * @param roleId 角色ID
	 * @param ids 资源ID，多个值用逗号分隔
	 * @return ResponseData
	 */
	@RequestMapping(value="/bindResource", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData bindResource(Long roleId, String ids) {
		roleService.bindResource(roleId, ids);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 给资源取消绑定角色, 只接受POST请求
	 * 
	 * @param roleId 角色ID
	 * @param ids 资源ID，多个值用逗号分隔
	 * @return ResponseData
	 */
	@RequestMapping(value="/unBindResource", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData unBindResource(Long roleId, String ids) {
		roleService.unBindResource(roleId, ids);
		return ResponseData.SUCCESS_NO_DATA;
	}
}
