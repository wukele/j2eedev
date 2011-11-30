package com.sumilux.acl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.acls.domain.BasePermission;

import com.sumilux.acl.security.SpringSecurityManager;

public class HelloServiceTest {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		IHelloService helloService = applicationContext.getBean(IHelloService.class);
		SpringSecurityManager securityManager = applicationContext.getBean(SpringSecurityManager.class);
		securityManager.authenticate("melin");
		
		Contact contact1 = new Contact("asdfsd", "libinsong");
		helloService.addContact(contact1, BasePermission.READ);
		try {
			helloService.edit(contact1);
		} catch (AccessDeniedException e) {
			e.printStackTrace();
		}
		try {
			helloService.delete("asdfsd");
		} catch (AccessDeniedException e) {
			e.printStackTrace();
		}
		try {
			helloService.find("asdfsd");
		} catch (AccessDeniedException e) {
			e.printStackTrace();
		}
		
		Contact contact2 = new Contact("123", "melin");
		helloService.addContact(contact2, BasePermission.WRITE);
		helloService.edit(contact2);
		helloService.delete("123");
		helloService.find("123");
		
		System.out.println("count: " + helloService.findContacts().size());

		helloService.deleteAcl(contact1, true);
		helloService.deleteAcl(contact2, true);
	}
}
