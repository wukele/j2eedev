package com.starit.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class RemovreEndpointServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		DefaultListableBeanFactory acf = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
		CamelContext context = (CamelContext)ctx.getBean("camel");
		
		if(ctx.containsBean("helloWorld")) {
			acf.removeBeanDefinition("helloWorld");
			try {
				context.stopRoute("routetest1");
				context.removeRoute("routetest1");
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("contain helloword bean");
		} else {
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
"<beans xmlns=\"http://www.springframework.org/schema/beans\""+
"       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""+
"       xmlns:cxf=\"http://camel.apache.org/schema/cxf\""+
"       xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd"+
"       http://camel.apache.org/schema/cxf http://camel.apache.org/schema/cxf/camel-cxf-2.5.0.xsd\">"+
"	<cxf:cxfEndpoint id=\"helloWorld\" address=\"/helloworld\" endpointName=\"s:HelloWorldPort\" serviceName=\"s:HelloWorldService\""+
"                     wsdlURL=\"helloWorld.wsdl\" xmlns:s=\"http://demo.cxf.starit.com/\"/>"+
"</beans>";
			XmlBeanFactory factory = new XmlBeanFactory(new ByteArrayResource(xml.getBytes()));
			acf.registerBeanDefinition("helloWorld", factory.getMergedBeanDefinition("helloWorld"));
			
			//添加路由规则
	        try {
				context.addRoutes(new RouteBuilder() {
					@Override
					public void configure() throws Exception {
						from("cxf:bean:helloWorld?dataFormat=MESSAGE").id("routetest1").to("bean:enrichBean")
						.to("http://localhost:9000/helloWorld?throwExceptionOnFailure=false");
					}
				});
				context.startRoute("routetest1");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("create new helloword bean ");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(req, response);
	}
}
