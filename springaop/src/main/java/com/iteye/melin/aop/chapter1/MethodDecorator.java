package com.iteye.melin.aop.chapter1;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MethodDecorator implements MethodInterceptor {
	public Object invoke(MethodInvocation invocation) throws Throwable {
		System.out.print("Hello ");
		Object retVal = invocation.proceed();
		System.out.println("this is aop !");
		return retVal;
	}
}