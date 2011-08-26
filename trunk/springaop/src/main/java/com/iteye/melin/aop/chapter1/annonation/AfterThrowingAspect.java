package com.iteye.melin.aop.chapter1.annonation;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class AfterThrowingAspect {
	@AfterThrowing("execution(* errorMethod(..))")
	public void afterGreeting() {
	  System.out.println("+++");
	  System.out.println("Exception !");
	  System.out.println("+++");
	}
}
