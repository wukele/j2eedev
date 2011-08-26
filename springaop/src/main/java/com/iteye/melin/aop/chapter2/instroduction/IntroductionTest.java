package com.iteye.melin.aop.chapter2.instroduction;

import org.springframework.aop.IntroductionAdvisor;
import org.springframework.aop.framework.ProxyFactory;

public class IntroductionTest {
	public static void main(String[] args) {
		DomainObjectTarget target = new DomainObjectTarget();

		IntroductionAdvisor advisor = new IsModifiableAdvisor();
		ProxyFactory pf = new ProxyFactory();
		pf.setTarget(target);
		pf.addAdvisor(advisor);
		pf.setOptimize(true);
		DomainObjectTarget proxy = (DomainObjectTarget) pf.getProxy();
		IsModifiable proxyInterface = (IsModifiable) proxy;
		System.out.println("Is TargetBean?: "
				+ (proxy instanceof DomainObjectTarget));
		System.out.println("Is IsModifiable?: "
				+ (proxy instanceof IsModifiable));
		System.out.println("Has been modified?: "
				+ proxyInterface.isModifiable());
		proxy.setDescription("One for the money");
		System.out.println("Has been modified?: "
				+ proxyInterface.isModifiable());
		proxy.setDescription("Two for the show");
		System.out.println("Has been modified?: "
				+ proxyInterface.isModifiable());
	}
}