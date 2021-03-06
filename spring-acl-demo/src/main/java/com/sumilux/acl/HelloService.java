package com.sumilux.acl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;


@Service
public class HelloService implements IHelloService {
	private List<Contact> list = new ArrayList<Contact>();
	
	@Autowired
	private ContactPermissionService permissionService;
	
	/* (non-Javadoc)
	 * @see com.sumilux.acl.IHelloService#findContacts()
	 */
	@PostFilter("hasPermission(filterObject, 'WRITE')")
	public List<Contact> findContacts() {
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.sumilux.acl.IHelloService#addContact(com.sumilux.acl.Contact, org.springframework.security.acls.model.Permission)
	 */
	public void addContact(Contact contact, Permission permission) {
		list.add(contact);
		permissionService.addPermission(permissionService.getUsername(), permission.getMask(), contact);
	}

	@PreAuthorize("hasPermission(#contact, 'WRITE')")
	public Boolean edit(Contact contact) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@PostAuthorize("hasPermission(returnObject, 'WRITE') or hasPermission(returnObject, 'READ')")
	public Contact find(String id) {
		Contact contact1 = new Contact();
		contact1.setContactId(id);
		contact1.setName("libinsong");
		return contact1;
	}

	@PreAuthorize("hasPermission(#id, 'com.sumilux.acl.Contact', 'WRITE')")
	public Boolean delete(String id) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void deleteAcl(Contact contact, boolean deleteChildren) {
		permissionService.deleteAcl(contact, true);
	}
}
