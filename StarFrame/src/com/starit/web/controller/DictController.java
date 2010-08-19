package com.starit.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 业务字典Controller
 * 
 * @datetime 2010-8-19 下午10:01:44
 * @author libinsong1204@gmail.com
 */
@Controller
@RequestMapping("/dict")
public class DictController {

	//~ Instance fields ================================================================================================

	//~ Constructors ===================================================================================================

	//~ Methods ========================================================================================================
	@RequestMapping("/index")
	public String index(){
		return "core/dict";
	}
}
