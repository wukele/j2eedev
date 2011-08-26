package com.iteye.melin.aop.chapter1;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.framework.ProxyFactory;

public class AfterRetuningAdvice implements AfterReturningAdvice {
	public static void main(String[] args) {
		// target class
		Hello target = new Hello();
		// create the proxy
		ProxyFactory pf = new ProxyFactory();
		// add advice
		pf.addAdvice(new AfterRetuningAdvice());
		// setTarget
		pf.setTarget(target);
		Hello proxy = (Hello) pf.getProxy();
		proxy.greeting();
	}

	public void afterReturning(Object returnValue, Method method,
			Object[] args, Object target) throws Throwable {
		System.out.println(",this is a afterReturningAdvice message");
	}
}