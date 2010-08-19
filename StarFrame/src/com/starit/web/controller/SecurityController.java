package com.starit.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 安全认证和授权Controller。
 *
 * @datetime 2010-8-8 上午10:38:27
 * @author libinsong1204@gmail.com
 */
@Controller
@RequestMapping("/security")
public class SecurityController {
	//~ Instance fields ================================================================================================
	
	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	/**
	 * Spring Security认证成功后，继续调用该方法
	 * 
	 * @return
	 */
	@RequestMapping("/loginSuccess")
	@ResponseBody public String loginSuccess() {
		return "{success: true}";
	}
	
	/**
	 * Spring Security认证失败后，继续调用该方法
	 * 
	 * @return
	 */
	@RequestMapping("/loginFailure")
	@ResponseBody public String loginFailure() {
		return "{success: false}";
	}
}
