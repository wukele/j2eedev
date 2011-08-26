package com.iteye.melin.aop.chapter2;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class AdviceExample implements MethodInterceptor {
	public Object invoke(MethodInvocation invocation) throws Throwable {
		System.out.println("===Invoking " + invocation.getMethod().getName());
		Object retVal = invocation.proceed();
		System.out.println("===Invoking Finished");
		return retVal;
	}
}