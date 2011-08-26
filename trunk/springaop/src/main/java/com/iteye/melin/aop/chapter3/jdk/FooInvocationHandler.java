package com.iteye.melin.aop.chapter3.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class FooInvocationHandler implements InvocationHandler {
	private Object target;

	public FooInvocationHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		System.out.println("==========proxy=========");
		return method.invoke(target, args);
	}

	public static Object createProxy(Object target) {
		return Proxy.newProxyInstance(target.getClass().getClassLoader(),
				target.getClass().getInterfaces(), new FooInvocationHandler(target));
	}

}
