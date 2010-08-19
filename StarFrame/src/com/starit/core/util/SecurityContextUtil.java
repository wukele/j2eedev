package com.starit.core.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.starit.web.model.User;
/**
 * SecurityContextHolder 帮助类
 *
 * @datetime 2010-8-12 下午01:45:25
 * @author libinsong1204@gmail.com
 */
public class SecurityContextUtil {
	//~ Instance fields ================================================================================================
	public static final String SPRING_SECURITY_LAST_USERNAME_KEY = 
		UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY;

	//~ Methods ========================================================================================================
	/**
	 * 获取当前登录用户ID
	 * 
	 * @return String 用户ID
	 */
	public static String getLoginUserId() {
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		return userId;
	}
	
	public static User getCurrentUser() {
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user;
	}
}
