package com.starit.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.starit.web.service.UserService;

/**
 *
 * @datetime 2010-8-8 下午04:47:03
 * @author libinsong1204@gmail.com
 */
@Controller
@RequestMapping("/menu")
public class UserController {
	//~ Instance fields ================================================================================================
	@Autowired
	private UserService userService;
	
	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
}
