package com.iteye.melin.aop.chapter2.AnnonationMatchingPointcut;

@ClassLevel
public class AnnonationPointcutTargetExample {
	@MethodLevel
	public void printName() {
		System.out.println("Max");
	}

	public void printAction() {
		System.out.println("runs");
	}
}
