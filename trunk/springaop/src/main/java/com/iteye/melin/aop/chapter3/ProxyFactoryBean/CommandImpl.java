package com.iteye.melin.aop.chapter3.ProxyFactoryBean;

public class CommandImpl implements Command {
	public Object execute() {
		for (int x = 0; x < 1000; x++) {
			action();
		}
		return null;
	}

	private void action() {
		System.out.println("\n .");
	}
}