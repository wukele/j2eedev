package com.iteye.melin.aop.chapter2.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

public class StaticPointcutMatcher extends StaticMethodMatcherPointcut {

	public boolean matches(Method method, Class clazz) {
        return ("printSpot".equals(method.getName()));
    }
	
    public ClassFilter getClassFilter() {
        return new ClassFilter() {
            public boolean matches(Class clazz) {
                return (clazz == PointcutTargetExample.class);
            }
        };
    }
}
