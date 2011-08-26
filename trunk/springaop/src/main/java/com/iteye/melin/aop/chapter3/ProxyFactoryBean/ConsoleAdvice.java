package com.iteye.melin.aop.chapter3.ProxyFactoryBean;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ConsoleAdvice implements MethodInterceptor {
	public Object invoke(MethodInvocation invocation) throws Throwable {
		StringBuilder sb = new StringBuilder();
		sb.append("ConsoleAdvice");
		sb.append("\n Method:").append(invocation.getMethod())
				.append("\n on class:").append(invocation.getClass())
				.append("\n with arguments:").append(invocation.getArguments());
		System.out.println(sb.toString());
		Object retVal = invocation.proceed();
		System.out.println("\n return value:" + retVal);
		System.out.println();
		return retVal;
	}
}