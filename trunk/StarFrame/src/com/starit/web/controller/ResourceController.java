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
import com.starit.web.service.ResourceService;

/**
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
	public Page<Resource> pageQueryResources(@RequestParam("start")int pageNumber, 
			@RequestParam("limit")int pageSize, String enabled, Resource resource) {
		PageRequest<Resource> pageRequest = new PageRequest<Resource>(pageNumber, pageSize);
		pageRequest.setSortColumns("priority");
		Map<String, Object> paramMap = pageRequest.getFilters();
		if(StringUtils.hasText(resource.getType()))
			paramMap.put("type", resource.getType());
		if(StringUtils.hasText(enabled))
			paramMap.put("enabled", Boolean.valueOf(enabled));
		
		//添加资源名称很资源类型查询条件
		String extraCondition = "";
		if(StringUtils.hasText(resource.getName()))
			extraCondition = extraCondition + " name like '%" + resource.getName() + "%' ";
			
		pageRequest.setExtraCondition(extraCondition);
		return resourceService.findAllForPage(pageRequest);
	}
	
	@RequestMapping(value="/insertResource", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData insertResource(Resource resource) {
		resourceService.insertEntity(resource);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	@RequestMapping(value="/updateResource", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData updateResource(Resource resource) {
		resourceService.updateEntity(resource);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	@RequestMapping(value="/loadResource", method=RequestMethod.POST)
	public Resource loadResource(Long id) {
		return resourceService.findEntity(id);
	}
	
	@RequestMapping(value="/deleteResource", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData deleteResource(Long id) {
		resourceService.deleteEntity(id);
		return ResponseData.SUCCESS_NO_DATA;
	}
}
