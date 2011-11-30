package com.sumilux.acl;

public class Contact {
	private String contactId;
	private String name;
	private String parentId;
	
	public Contact() {
	}

	public Contact(String contactId, String name) {
		this.contactId = contactId;
		this.name = name;
	}

	public String getId() {
		return contactId;
	}
	
	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
}
