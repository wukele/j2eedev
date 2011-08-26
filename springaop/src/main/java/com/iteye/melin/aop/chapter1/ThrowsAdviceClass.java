package com.iteye.melin.aop.chapter1;

import java.lang.reflect.Method;

import org.springframework.aop.ThrowsAdvice;
import org.springframework.aop.framework.ProxyFactory;

public class ThrowsAdviceClass implements ThrowsAdvice {
	public static void main(String[] args) {
		// target class
		ExceptionTarget errorBean = new ExceptionTarget();
		// create the proxy
		ProxyFactory pf = new ProxyFactory();
		// add advice
		pf.addAdvice(new ThrowsAdviceClass());
		// setTarget
		pf.setTarget(errorBean);

		ExceptionTarget proxy = (ExceptionTarget) pf.getProxy();
		try {
			proxy.errorMethod();
		} catch (Exception ignored) {
		}
		try {
			proxy.otherErrorMethod();
		} catch (Exception ignored) {
		}
	}

	public void afterThrowing(Exception ex) throws Throwable {
		System.out.println("+++");
		System.out.println("Exception Capture:" + ex.getClass().getName());
		System.out.println("+++\n");
	}

	public void afterThrowing(Method method, Object[] args, Object target,
			NullPointerException ex) throws Throwable {
		System.out.println("+++");
		System.out.println("NullPointerException Capture: "
				+ ex.getClass().getName());
		System.out.println("Method: " + method.getName());
		System.out.println("+++\n");
	}
}