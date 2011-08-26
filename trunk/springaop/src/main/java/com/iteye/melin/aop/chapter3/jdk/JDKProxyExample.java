package com.iteye.melin.aop.chapter3.jdk;

public class JDKProxyExample {
	public static void main(String[] args) {
		   Info info = new DefaultInfo();
		   Info information = (Info) FooInvocationHandler.createProxy(info);
		   information.printName();
		}
}

interface  Info {
	public void printName();
}

class DefaultInfo implements Info {
	public void printName() {
		System.out.println("meliin");
	}
}