package com.sumilux.acl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.acls.domain.BasePermission;

import com.sumilux.acl.security.SpringSecurityManager;

public class ParentHelloServiceTest {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		IHelloService helloService = applicationContext.getBean(IHelloService.class);
		SpringSecurityManager securityManager = applicationContext.getBean(SpringSecurityManager.class);
		securityManager.authenticate("melin");
		
		Contact pcon = new Contact("asdfsd", "libinsong");
		Contact ccon = new Contact("12312", "melin");
		ccon.setParentId("asdfsd");
		helloService.addContact(pcon, BasePermission.READ);
		helloService.addContact(ccon, BasePermission.WRITE);
		
		helloService.deleteAcl(pcon, true);
		helloService.deleteAcl(ccon, true);
	}
}
