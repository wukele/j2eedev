package com.iteye.melin.aop.chapter1.annonation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class AroundAspect {
	
	@Around("execution(* greeting(..))")
	public Object aroundGreeting(ProceedingJoinPoint pjp) throws Throwable {
		System.out.print("Hello ");
		try {
			return pjp.proceed();
		} finally {
			System.out.println("this is around aop !");
		}
	}
}
