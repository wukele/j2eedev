package com.iteye.melin.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
   
/**      
 * 用于检测用户是否登陆的过滤器，如果未登录，则重定向到指的登录页面       
 * 配置参数      
 * checkSessionKey 需检查的在 Session 中保存的关键字       
 * redirectURL 如果用户未登录，则重定向到指定的页面，URL不包括 ContextPath       
 * notCheckURLList 不做检查的URL列表，以分号分开，并且 URL 中不包括 ContextPath      
 */   
public class CheckLoginFilter implements Filter {   
    protected FilterConfig filterConfig = null;   
   
    private static final String redirectURL = "/login";   
   
    private static final String sessionKey = "__SESSIONKEY__";   
    
    private final List<String> notCheckURLList = new ArrayList<String>();
   
    public void doFilter(ServletRequest servletRequest,   
            ServletResponse servletResponse, FilterChain filterChain)   
            throws IOException, ServletException {   
        HttpServletRequest request = (HttpServletRequest) servletRequest;   
        HttpServletResponse response = (HttpServletResponse) servletResponse;   
   
        HttpSession session = request.getSession();   
        if (sessionKey == null) {   
            filterChain.doFilter(request, response);   
            return;   
        }   
        if ((!checkRequestURIIntNotFilterList(request))   
                && session.getAttribute(sessionKey) == null) {   
        	request.getSession();
            response.sendRedirect("http://localhost:8080/demo/login");   
            return;   
        }   
        filterChain.doFilter(servletRequest, servletResponse);   
    }   
   
    public void destroy() {   
        notCheckURLList.clear();   
    }   
   
    private boolean checkRequestURIIntNotFilterList(HttpServletRequest request) {   
        String uri = request.getServletPath()   
                + (request.getPathInfo() == null ? "" : request.getPathInfo()); 
        for(String path : notCheckURLList) {
        	if(uri.startsWith(path))
        		return true;
        }
        return false;   
    }   
   
    public void init(FilterConfig filterConfig) throws ServletException {
    	notCheckURLList.add("/login");
    	notCheckURLList.add("/resources");
    }   
}   