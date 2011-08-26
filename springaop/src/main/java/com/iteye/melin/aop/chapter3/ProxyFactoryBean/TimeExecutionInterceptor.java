package com.iteye.melin.aop.chapter3.ProxyFactoryBean;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StopWatch;

public class TimeExecutionInterceptor implements MethodInterceptor {
	public Object invoke(MethodInvocation invocation) throws Throwable {
		long timeBeforeMethodExecution = 0;
		long timeAfterMethodExecution = 0;
		// Spring util class
		StopWatch sw = new StopWatch();
		System.out.println(new StringBuilder("\n Time before execution:")
				.append(timeBeforeMethodExecution));
		sw.start(invocation.getMethod().getName());
		Object returnValue = invocation.proceed();
		sw.stop();
		timeAfterMethodExecution = System.currentTimeMillis();
		System.out.println(new StringBuilder("\n Time after execution:")
				.append((timeAfterMethodExecution - timeBeforeMethodExecution))
				.append(" ms").append("\n result:").append(returnValue));
		dumpInfo(invocation, sw.getTotalTimeMillis());
		return returnValue;
	}

	private void dumpInfo(MethodInvocation invocation, long ms) {
		Method m = invocation.getMethod();
		Object[] args = invocation.getArguments();
		System.out.println(new StringBuilder("\n Method :").append(m.getName())
				.append("\n On object type: :")
				.append(invocation.getThis().getClass().getName())
				.append((" \n With arguments : \n")));
		for (int x = 0; x < args.length; x++) {
			System.out.println(new StringBuilder("    > ").append(args[x]));
		}
		System.out.println(new StringBuilder(" \n Time of execution: ").append(ms)
				.append(" ms"));
	}
}
