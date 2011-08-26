package com.iteye.melin.aop.chapter2.instroduction;

public class DomainObjectTarget {
	public DomainObjectTarget() {
	}

	public DomainObjectTarget(String description, Integer id) {
		this.description = description;
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isNew() {
		return null == description && null == id;
	}

	private String description;
	private Integer id = null;
}