package com.iteye.melin.aop.chapter2.StaticMethodMatcherPointcut;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import com.iteye.melin.aop.chapter2.AdviceExample;

public class StaticPointcutExample {
	public static void main(String[] args) {
		PointcutTargetExample one = new PointcutTargetExample();
		PointcutTargetExampleTwo two = new PointcutTargetExampleTwo();
		
		PointcutTargetExample proxyOne;
		PointcutTargetExampleTwo proxyTwo;
		
		Pointcut pc = new StaticPointcutMatcher();
		Advice advice = new AdviceExample();
		Advisor advisor = new DefaultPointcutAdvisor(pc, advice);
		
		ProxyFactory pf = new ProxyFactory();
		pf.addAdvisor(advisor);
		pf.setTarget(one);
		proxyOne = (PointcutTargetExample) pf.getProxy();
		
		pf = new ProxyFactory();
		pf.addAdvisor(advisor);
		pf.setTarget(two);
		proxyTwo = (PointcutTargetExampleTwo) pf.getProxy();
		
		proxyOne.printName();
		proxyTwo.printAction();
		
		proxyOne.printSpot();
		proxyTwo.printSpot();
	}
}