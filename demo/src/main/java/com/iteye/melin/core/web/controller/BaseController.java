package com.iteye.melin.core.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.iteye.melin.core.util.ResponseData;

/**
 *
 * @datetime 2010-8-9 下午01:17:20
 * @author libinsong1204@gmail.com
 */
abstract public class BaseController {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	public final static String EXCEPTION_MESSAGE = "EXCEPTION_MESSAGE";

	//~ Instance fields ================================================================================================
	
	//~ Methods ========================================================================================================
	@ExceptionHandler()
	public @ResponseBody String handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) {
		//logger.error(request.getRequestURI() + " 请求失败", exception);
		
		if(!(request.getRequestURI().endsWith(".json") || request.getRequestURI().endsWith("Json")))
			throw new RuntimeException(exception);
		
		ResponseData data = new ResponseData(false, exception.getClass() + ": " + exception.getMessage());
		data.setRequestURI(request.getRequestURI());
		data.setExecptionTrace(ExceptionUtils.getFullStackTrace(exception));
		request.setAttribute(EXCEPTION_MESSAGE, data.getExecptionTrace());
		
		String json = JSON.toJSONString(data);
		
		response.setStatus(500);//服务端处理失败
		response.setContentType("application/json;charset=UTF-8");
		return json;
	}
}
