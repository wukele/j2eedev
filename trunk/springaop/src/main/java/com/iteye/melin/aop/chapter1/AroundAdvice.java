package com.iteye.melin.aop.chapter1;

import org.springframework.aop.framework.ProxyFactory;

public class AroundAdvice {
	public static void main(String[] args) {
		Hello hello = new Hello();
		
		ProxyFactory factory = new ProxyFactory();
		factory.addAdvice(new MethodDecorator());
		
		factory.setTarget(hello);
		
		Hello proxy = (Hello) factory.getProxy();
		proxy.greeting();
	}
}
