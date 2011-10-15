package com.iteye.melin.web.controller.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iteye.melin.core.page.Page;
import com.iteye.melin.core.page.PageRequest;
import com.iteye.melin.core.util.ResponseData;
import com.iteye.melin.core.web.controller.BaseController;
import com.iteye.melin.web.model.base.Application;
import com.iteye.melin.web.service.base.ApplicationService;

/**
 * 用户Controller
 *
 * @datetime 2010-8-8 下午04:47:03
 * @author libinsong1204@gmail.com
 */
@Controller
@RequestMapping("/application")
public class ApplicationController extends BaseController {
	//~ Instance fields ================================================================================================
	@Autowired
	private ApplicationService applicationService;
	
	//~ Methods ========================================================================================================
	@RequestMapping("/index")
	public String index(){
		return "admin/base/application";
	}
	
	@RequestMapping("/pageQueryApplications")
	@ResponseBody
	public Page<Application> pageQueryApplications(@RequestParam("start")int startIndex, 
			@RequestParam("limit")int pageSize, Application application, @RequestParam(required = false)String sort, 
			@RequestParam(required = false)String dir) {
		PageRequest<Application> pageRequest = new PageRequest<Application>(startIndex, pageSize);
		
		if(StringUtils.hasText(sort) && StringUtils.hasText(dir))
			pageRequest.setSortColumns(sort + " " + dir);
		
//		Map<String, String> likeFilteApplicationageRequest.getLikeFilters();
//		if(StringUtils.hasText(application.getTitle()))
//			likeFilters.put("title", application.getTitle());
		
//		if(StringUtils.hasText(user.getUserCode()))
//			pageRequest.getFilters().put("userCode", user.getUserCode());

		Page<Application> page = applicationService.findAllForPage(pageRequest);
		return page;
	}
	
	@RequestMapping(value="/insertApplication", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData insertApplication(Application application) {
        
        applicationService.insertEntity(application);
		return ResponseData.SUCCESS_NO_DATA;
	}
	
	@RequestMapping(value="/loadApplication", method=RequestMethod.POST)
	@ResponseBody
	public Application loadApplication(Long id) {
		return applicationService.findEntity(id);
	}
	
	@RequestMapping(value="/updateApplication", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData updateApplication(Application application) {
		applicationService.updateEntity(application);
		return ResponseData.SUCCESS_NO_DATA;
	}

	@RequestMapping(value="/deleteApplication", method=RequestMethod.POST)
	@ResponseBody
	public ResponseData deleteUser(Long id) {
		applicationService.deleteEntity(id);
		return ResponseData.SUCCESS_NO_DATA;
	}
}
