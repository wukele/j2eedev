package com.iteye.melin.aop.chapter3.ProxyFactoryBean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProxyFactoryBeanTest {
	public static void main(String[] args) {
		String[] paths = { "com/iteye/melin/aop/chapter3/ProxyFactoryBean/aop.xml" };
		ApplicationContext ctx = new ClassPathXmlApplicationContext(paths);
		User user = (User) ctx.getBean("user");
		user.getName();
		Command command = (Command) ctx.getBean("command");
		command.execute();
		PersonUser person = (PersonUser) ctx.getBean("personUser");
		person.getName();
	}
}