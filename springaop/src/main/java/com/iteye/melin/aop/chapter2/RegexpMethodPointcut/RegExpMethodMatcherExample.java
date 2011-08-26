package com.iteye.melin.aop.chapter2.RegexpMethodPointcut;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;

import com.iteye.melin.aop.chapter2.AdviceExample;

public class RegExpMethodMatcherExample {
	public static void main(String[] args) {
		RegExpTargetExample target = new RegExpTargetExample();
		
		JdkRegexpMethodPointcut pc = new JdkRegexpMethodPointcut();
		String[] patterns = { ".*Spot.*", ".*Action.*" };
		pc.setPatterns(patterns);
		pc.setPatterns(patterns);
		
		Advisor advisor = new DefaultPointcutAdvisor(pc, new AdviceExample());
		
		ProxyFactory pf = new ProxyFactory();
		pf.setTarget(target);
		pf.addAdvisor(advisor);
		
		RegExpTargetExample proxy = (RegExpTargetExample) pf.getProxy();
		proxy.printName();
		proxy.printAction();
		proxy.printSpot();
	}
}