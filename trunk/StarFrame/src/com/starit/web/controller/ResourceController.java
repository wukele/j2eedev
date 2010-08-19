package com.starit.web.controller;

import java.util.Map;
import java.util.Set;

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
import com.starit.web.service.ResourceService;

/**
 * 资源Controller
 *
 * @datetime 2010-8-8 下午04:47:03
 * @author libinsong1204@gmail.com
 */
@Controller
@RequestMapping("/resource")
public class ResourceController {
	//~ Instance fields ================================================================================================
	@Autowired
	private ResourceService resourceService;
	
	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	@RequestMapping("/index")
	public String index(){
		return "core/resource";
	}
	
	/**
	 * 资源管理，查询资源信息，按照资源优先级降序排序
	 * 
	 * @param pageRequest
	 * @return
	 */
	@RequestMapping("/pageQueryResources")
	@ResponseBody
	public Page<Resource> pageQueryResources(@RequestParam("start")int pageNumber, 
			@RequestParam("limit")int pageSize, Resource resource, String sort, String dir) {
		PageRequest<Resource> pageRequest = new PageRequest<Resource>(pageNumber, pageSize);
		
		if(StringUtils.hasText(sort) && StringUtils.hasText(dir))
			pageRequest.setSortColumns(sort + " " + dir);
		else
			pageRequest.setSortColumns("priority");
		
		Map<String, Object> paramMap = pageRequest.getFilters();
		if(StringUtils.hasText(resource.getType()))
			paramMap.put("type", resource.getType());
		if((int)resource.getEnabled() != 0)
			paramMap.put("enabled", resource.getEnabled());
		
		Map<String, String> likeFilters = pageRequest.getLikeFilters();
		if(StringUtils.hasText(resource.getName()))
			likeFilters.put("name", resource.getName());

		return resourceService.findAllForPage(pageRequest);
	}
	
	/**
	 * 查询菜单类型资源信息，按照资源优先级降序排序
	 * 
	 * @param pageRequest
	 * @return
	 */
	@RequestMapping("/pageQueryResourcesByMenu")
	@ResponseBody
	public Page<Resource> pageQueryResourcesByMenu(@RequestParam("start")int pageNumber, 
			@RequestParam("limit")int pageSize, Resource resource) {
		PageRequest<Resource> pageRequest = new PageRequest<Resource>(pageNumber, pageSize);
		pageRequest.setSortColumns("priority");
		Map<String, Object> paramMap = pageRequest.getFilters();
		paramMap.put("type", Resource.ResourceType.MENU.getType());
		return resourceService.findAllForPage(pageRequest);
	}
	
	/**
	 * 保存资源, 只接受POST请求
	 * 
	 * @param resource
	 * @return ResponseData
	 */
	@RequestMapping(value="/insertResource", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData insertResource(Resource resource) {
		resourceService.insertEntity(resource);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 更新资源, 只接受POST请求
	 * 
	 * @param resource
	 * @return ResponseData
	 */
	@RequestMapping(value="/updateResource", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData updateResource(Resource resource) {
		resourceService.updateEntity(resource);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 加载资源, 只接受POST请求
	 * 
	 * @param id
	 * @return Resource
	 */
	@RequestMapping(value="/loadResource", method=RequestMethod.POST)
	@ResponseBody
	public Resource loadResource(Long id) {
		return resourceService.findEntity(id);
	}
	
	/**
	 * 删除资源, 只接受POST请求
	 * 
	 * @param id
	 * @return ResponseData
	 */
	@RequestMapping(value="/deleteResource", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData deleteResource(Long id) {
		resourceService.deleteEntity(id);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 查询资源关联的所有角色, 只接受POST请求
	 * 
	 * @param resId 资源ID
	 * @return List<Role> 资源关联的所有角色
	 */
	@RequestMapping(value="/queryRoles4Res", method=RequestMethod.POST)
	@ResponseBody
	public Set<Role> queryRoles4Res(Long resId) {
		return resourceService.queryRoles4Res(resId);
	}
	
	/**
	 * 查询所有资源信息，同时关联查询指定的角色信息
	 *
	 * @param roleId
	 */
	@RequestMapping(value="/pageQueryRoles4Res", method=RequestMethod.POST)
	@ResponseBody
	public Page<Role> pageQueryRoles4Res(@RequestParam("start")int pageNumber, 
			@RequestParam("limit")int pageSize, Long resId)  {
		PageRequest<Role> pageRequest = new PageRequest<Role>(pageNumber, pageSize);
		return resourceService.pageQueryRoles4Res(pageRequest, resId);
	}
	
	/**
	 * 给资源绑定角色, 只接受POST请求
	 * 
	 * @param resId 资源ID
	 * @param ids 角色ID，多个值用逗号分隔
	 * @return ResponseData
	 */
	@RequestMapping(value="/bindRole", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData bindRole(Long resId, String ids) {
		resourceService.bindRole(resId, ids);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	/**
	 * 给资源取消绑定角色, 只接受POST请求
	 * 
	 * @param resId 资源ID
	 * @param ids 角色ID，多个值用逗号分隔
	 * @return ResponseData
	 */
	@RequestMapping(value="/unBindRole", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData unBindRole(Long resId, String ids) {
		resourceService.unBindRole(resId, ids);
		return ResponseData.SUCCESS_NO_DATA;
	}
}
