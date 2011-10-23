package com.iteye.melin.web.controller.support;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iteye.melin.core.web.controller.BaseController;
import com.iteye.melin.web.model.support.AppType;
import com.iteye.melin.web.service.support.AppTypeService;

/**
 * 应用分类Controller
 *
 * @datetime 2010-8-8 下午04:47:03
 * @author libinsong1204@gmail.com
 */
@Controller
@RequestMapping("/apptype")
public class AppTypeController extends BaseController {
	//~ Instance fields ================================================================================================
	@Autowired
	private AppTypeService appTypeService;
	
	//~ Methods ========================================================================================================
	@RequestMapping(value="/queryAllAppType", method=RequestMethod.POST)
	@ResponseBody
	public List<AppType> queryAllAppType() {
		return appTypeService.findAllEntity();
	}
}
