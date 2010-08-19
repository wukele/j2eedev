package com.starit.core.spring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataAccessException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Ajax 请求产生异常，防止页面出错，进行处理
 *
 * @datetime 2010-8-12 下午04:15:24
 * @author libinsong1204@gmail.com
 */
public class ExceptionHandlerInterceptor implements HandlerInterceptor {
	//~ Instance fields ================================================================================================

	//~ Constructors ===================================================================================================

	//~ Methods ========================================================================================================
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		if(ex != null && DataAccessException.class.isAssignableFrom(ex.getClass()))
			response.getWriter().print("{success:false, msg: '数据库操作错误："+ex.getMessage()+"}'");
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return false;
	}

}
