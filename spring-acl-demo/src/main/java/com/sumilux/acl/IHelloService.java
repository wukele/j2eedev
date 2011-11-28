package com.sumilux.acl;

import java.util.List;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.model.Permission;

public interface IHelloService {

	@PostFilter("hasPermission(filterObject, 'WRITE')")
	public List<Contact> findContacts();

	public void addContact(Contact contact, Permission permission);
	
	@PreAuthorize("hasPermission(#post, 'WRITE')")
	public Boolean edit(Contact contact);

	@PreAuthorize("hasPermission(#post, 'WRITE')")
	public Boolean delete(Contact contact);
	
	public void deleteAcl(Contact contact, boolean deleteChildren);

}