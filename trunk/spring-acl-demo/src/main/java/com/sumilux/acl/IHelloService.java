package com.sumilux.acl;

import java.util.List;

import org.springframework.security.acls.model.Permission;

public interface IHelloService {

	public List<Contact> findContacts();

	public void addContact(Contact contact, Permission permission);
	
	public Boolean edit(Contact contact);

	public Boolean delete(Contact contact);
	
	public void deleteAcl(Contact contact, boolean deleteChildren);

}