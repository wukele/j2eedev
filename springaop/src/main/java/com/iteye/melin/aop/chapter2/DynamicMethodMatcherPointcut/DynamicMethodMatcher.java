package com.iteye.melin.aop.chapter2.DynamicMethodMatcherPointcut;

import java.lang.reflect.Method;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.DynamicMethodMatcherPointcut;

public class DynamicMethodMatcher extends DynamicMethodMatcherPointcut {
	public boolean matches(Method method, Class cls) {
		System.out.println("Static check for " + method.getName());
		return ("setSpot".equals(method.getName()));
	}

	public boolean matches(Method method, Class cls, Object[] args) {
		System.out.println("Dynamic check for " + method.getName());
		String spot = ((String) args[0]);
		return spot.endsWith("Ocean");
	}

	public ClassFilter getClassFilter() {
		return new ClassFilter() {
			public boolean matches(Class cls) {
				return (cls == DynamicPointcutTargetExample.class);
			}
		};
	}
}