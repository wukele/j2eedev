package com.starit;

import java.io.File;
import java.io.InputStream;

import org.apache.camel.CamelContext;
import org.apache.camel.TypeConverter;
import org.apache.camel.converter.stream.InputStreamCache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Node;

public class JettyMain {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("camel-jetty.xml");
		CamelContext context = applicationContext.getBean(CamelContext.class);
	}
}
