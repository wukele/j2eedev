package com.iteye.melin.aop.chapter2.ControlFlowPointcut;

import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.ControlFlowPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class ControlFlowPointcutExample {
	public static void main(String[] args) {
		ControlFlowPointcutExample ex = new ControlFlowPointcutExample();
		ex.run();
	}

	public void run() {
		ControlFlowTargetExample target = new ControlFlowTargetExample();
		Pointcut pc = new ControlFlowPointcut(ControlFlowPointcutExample.class, "test");
		Advisor advisor = new DefaultPointcutAdvisor(pc, new ControlFlowBeforeAdvice());
		
		ProxyFactory pf = new ProxyFactory();
		pf.setTarget(target);
		pf.addAdvisor(advisor);
		
		ControlFlowTargetExample proxy = (ControlFlowTargetExample) pf.getProxy();
		
		System.out.println("Trying normal invoke");
		proxy.greeting();
		System.out.println("Trying under ControlFlowPointcutExample.test()");
		test(proxy);
	}

	private void test(ControlFlowTargetExample bean) {
		bean.greeting();
	}
}
