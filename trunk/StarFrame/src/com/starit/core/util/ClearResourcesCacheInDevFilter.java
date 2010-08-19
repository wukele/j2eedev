package com.starit.core.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 在开发环境下方便测试，清楚Resources的cache信息。
 *
 * @datetime 2010-8-11 下午02:18:35
 * @author libinsong1204@gmail.com
 */
public class ClearResourcesCacheInDevFilter implements Filter {

	//~ Instance fields ================================================================================================

	//~ Constructors ===================================================================================================

	//~ Methods ========================================================================================================
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		
		chain.doFilter(request, response);
		// HTTP 1.0 header
		httpServletResponse.setDateHeader("Expires", -1L);
		httpServletResponse.setDateHeader("Last-Modified", -100000000000L);
		// HTTP 1.1 header
		httpServletResponse.setHeader("Cache-Control", "max-age=-1");
	}

	public void destroy() {
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
}
