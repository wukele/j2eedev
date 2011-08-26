package com.iteye.melin.aop.chapter3.aspectj;

public class CommandImpl implements Command {
	public void execute() {
		System.out.println(label);
	}

	private final String label = "Goooo !";
}