package com.iteye.melin.aop.chapter3.ProxyFactoryBean;

public class UserImpl implements User, Address {
	public String getAddress() {
		return address;
	}

	public String getNation() {
		return nation;
	}

	public String getState() {
		return state;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	private String address, nation, state, name, surname;
}