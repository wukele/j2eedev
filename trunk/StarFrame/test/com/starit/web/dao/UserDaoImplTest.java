package com.starit.web.dao;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.starit.web.model.Role;
import com.starit.web.model.User;

/**
 *
 * @datetime 2010-8-9 上午10:31:39
 * @author libinsong1204@gmail.com
 */
public class UserDaoImplTest extends BaseServiceTestCase {
	//~ Instance fields ================================================================================================
	private UserDao userDao;
	private RoleDao roleDao;

	//~ Constructors ===================================================================================================

	//~ Methods ========================================================================================================
	@Before public void beforeClass() {
		userDao = (UserDao)applicationContext.getBean("userDaoImpl");
		roleDao = (RoleDao)applicationContext.getBean("roleDaoImpl");
	}
	
	//@Test
	public void testSave() {
		User user = new User();
		user.setUserId("melin");
		user.setOperatorName("Libinsong");
		user.setPassword("000000");
		
		Role role = new Role();
		role.setCode("ROLE_ADMIN");
		role.setName("管理员角色");
		user.getAuthorities().add(role);
		role.getUsers().add(user);
		
		userDao.create(user);
		roleDao.create(role);
	}
	
	@Test
	public void testFind$Eager() {
		List<User> results = (List<User>) userDao.find("SELECT count(*)  from User user left outer join fetch user.employee where userId = ?", "admin");
		System.out.println(results.get(0));
	}
}
