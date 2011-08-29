package com.iteye.melin.aop.chapter3.TargetSource;

public class AnimalImpl implements Animal {
	public Integer getNumberPaws() {
		return paws;
	}

	public void setPaws(Integer paws) {
		this.paws = paws;
	}

	private Integer paws;
}