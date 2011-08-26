package com.iteye.melin.aop.chapter3.ProxyFactoryBean;

public class PersonUser {
	public String getName() {
		return user.getName();
	}

	public String getSurname() {
		return user.getSurname();
	}

	public void setUser(User user) {
		this.user = user;
	}

	private User user;
}