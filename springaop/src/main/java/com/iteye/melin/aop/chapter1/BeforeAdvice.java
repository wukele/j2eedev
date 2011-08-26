package com.iteye.melin.aop.chapter1;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;

public class BeforeAdvice implements MethodBeforeAdvice {

	@Override
	public void before(Method method, Object[] args, Object target)
			throws Throwable {
		System.out.println("Good morning");
	}
	
	public static void main(String[] args) {
		Hello target = new Hello();
		// create the proxy
		ProxyFactory pf = new ProxyFactory();
		// add advice
		pf.addAdvice(new BeforeAdvice());
		// setTarget
		pf.setTarget(target);
		Hello proxy = (Hello) pf.getProxy();
		proxy.greeting();
	}
}