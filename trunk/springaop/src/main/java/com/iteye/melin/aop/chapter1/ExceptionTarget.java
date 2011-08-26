package com.iteye.melin.aop.chapter1;

public class ExceptionTarget {
	public void errorMethod() throws Exception {
		throw new Exception("Fake exception");
	}

	public void otherErrorMethod() throws IllegalArgumentException {
		throw new NullPointerException("Other Fake exception");
	}
}