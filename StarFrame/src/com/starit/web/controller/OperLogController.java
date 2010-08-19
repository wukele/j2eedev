package com.starit.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.starit.core.web.dao.Page;
import com.starit.core.web.dao.PageRequest;
import com.starit.web.model.OperLog;
import com.starit.web.service.OperLogService;

/**
 * 操作日志Controller
 *
 * @datetime 2010-8-18 下午03:06:36
 * @author libinsong1204@gmail.com
 */
@Controller
@RequestMapping("/operLog")
public class OperLogController {
	//~ Instance fields ================================================================================================
	@Autowired
	private OperLogService operLogService;
	
	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	@RequestMapping("/index")
	public String index(){
		return "core/operLog";
	}
	
	/**
	 * 角色管理，查询角色信息
	 * 
	 * @param pageRequest
	 * @return
	 */
	@RequestMapping("/pageQueryOperLogs")
	@ResponseBody
	public Page<OperLog> pageQueryRoles(@RequestParam("start")int pageNumber, 
			@RequestParam("limit")int pageSize, OperLog operLog, String sort, String dir) {
		PageRequest<OperLog> pageRequest = new PageRequest<OperLog>(pageNumber, pageSize);
		
		if(StringUtils.hasText(sort) && StringUtils.hasText(dir))
			pageRequest.setSortColumns(sort + " " + dir);
		
		Map<String, String> likeFilters = pageRequest.getLikeFilters();
		if(StringUtils.hasText(operLog.getOperatorName()))
			likeFilters.put("userName", operLog.getOperatorName());
		
		return operLogService.findAllForPage(pageRequest);
	}
}
