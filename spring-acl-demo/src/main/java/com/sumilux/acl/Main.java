package com.sumilux.acl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.acls.domain.BasePermission;

import com.sumilux.acl.security.SpringSecurityManager;

public class Main {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		ContactPermissionService permissionService = applicationContext.getBean(ContactPermissionService.class);
		SpringSecurityManager securityManager = applicationContext.getBean(SpringSecurityManager.class);
		securityManager.authenticate("melin");
		
		Contact contact = new Contact();
		contact.setContactId("asdfsd");
		contact.setName("libinsong");
		
		permissionService.create(contact);
		permissionService.addPermission("melin", BasePermission.READ.getMask(), contact);
		
		//permissionService.deletePermission(contact, "melin", BasePermission.READ.getMask());
		//permissionService.deletePermission(contact, "melin", BasePermission.ADMINISTRATION.getMask());
		
		permissionService.deleteAcl(contact, true);
	}
}
