package com.iteye.melin.aop.chapter2.DynamicMethodMatcherPointcut;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import com.iteye.melin.aop.chapter2.AdviceExample;

public class DynamicPointcutExample {
	public static void main(String[] args) {
		DynamicPointcutTargetExample target = new DynamicPointcutTargetExample();
		Advisor advisor = new DefaultPointcutAdvisor(new DynamicMethodMatcher(), new AdviceExample());
		
		ProxyFactory pf = new ProxyFactory();
		pf.setTarget(target);
		pf.addAdvisor(advisor);
		
		DynamicPointcutTargetExample proxy = (DynamicPointcutTargetExample) pf.getProxy();
		proxy.setSpot("Pacific Ocean");
		proxy.setSpot("Mediterranean Sea");
		proxy.setSpot("Atlantic Ocean");
		
		proxy.printSpot();
		proxy.printSpot();
		proxy.printSpot();
	}
}