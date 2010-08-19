package com.starit.core.spring;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Servlet 监听器，启动服务器时，获取ApplicationContext 存放到ApplicationContextProxy中，
 * 方便在获取ApplicationContext
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ApplicationContextInitListener implements ServletContextListener {
	//~ Instance fields ================================================================================================
	private static Log logger = LogFactory.getLog(ApplicationContextInitListener.class); 
	
	//~ Constructors ===================================================================================================
	
	//~ Methods ========================================================================================================
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("ApplicationContextHolder get Spring ApplicationContext success");
		//ApplicationContextHolder.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext()));		
	}

}