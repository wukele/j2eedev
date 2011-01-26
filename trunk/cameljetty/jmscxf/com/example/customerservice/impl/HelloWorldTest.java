package com.example.customerservice.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.starit.cxf.demo.HelloWorld;

/**
 * 
 * @author  libinsong1204@gmail.com
 * @date    2011-1-26
 * @version 
 */
public class HelloWorldTest {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				new String[] { "client-HelloWorld.xml" });
		HelloWorld helloWorld = (HelloWorld) applicationContext
				.getBean("CustomerService");
		
		String result = helloWorld.sayHi("hello", "Melin");
		System.out.println(result);
		
		System.out.println("finished");
		System.exit(0);
	}
}
