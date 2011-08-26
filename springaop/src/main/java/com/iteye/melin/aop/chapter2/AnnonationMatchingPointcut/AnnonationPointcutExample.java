package com.iteye.melin.aop.chapter2.AnnonationMatchingPointcut;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import com.iteye.melin.aop.chapter2.AdviceExample;

public class AnnonationPointcutExample {
	public static void main(String[] args) {
		AnnonationPointcutTargetExample targetExample = new AnnonationPointcutTargetExample();
		AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(ClassLevel.class, MethodLevel.class);
		
		Advisor advisor = new DefaultPointcutAdvisor(pointcut, new AdviceExample());
		
		ProxyFactory pf = new ProxyFactory();
		pf.setTarget(targetExample);
		pf.addAdvisor(advisor);
		
		AnnonationPointcutTargetExample proxy = (AnnonationPointcutTargetExample) pf.getProxy();
		
		proxy.printAction();
		proxy.printName();
	}
}
