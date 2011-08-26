package com.iteye.melin.aop.chapter1.annonation;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class AfterFinallyAspect {

	@After("execution(* greeting(..))")
	public void afterGreeting() {
		System.out.println("this is afterAspect !");
	}
}
