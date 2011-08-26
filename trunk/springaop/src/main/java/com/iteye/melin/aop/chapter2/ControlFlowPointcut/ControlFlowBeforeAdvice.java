package com.iteye.melin.aop.chapter2.ControlFlowPointcut;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

public class ControlFlowBeforeAdvice implements MethodBeforeAdvice {
	public void before(Method method, Object[] args, Object target)
			throws Throwable {
		System.out.println("ControlFlow beforeAdvice ");
	}
}