package com.iteye.melin.aop.chapter2.instroduction;

import org.springframework.aop.support.DefaultIntroductionAdvisor;

public class IsModifiableAdvisor extends DefaultIntroductionAdvisor {
	public IsModifiableAdvisor() {
		super(new IsModifiableMixin());
	}
}