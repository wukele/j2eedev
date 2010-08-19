package com.starit.web.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.starit.web.model.Resource;
import com.starit.web.model.Role;
import com.starit.web.model.User;

/**
 *
 * @datetime 2010-8-9 上午10:31:39
 * @author libinsong1204@gmail.com
 */
public class ResourceDaoImplTest extends BaseServiceTestCase {
	//~ Instance fields ================================================================================================
	private ResourceDao resourceDao;

	//~ Constructors ===================================================================================================

	//~ Methods ========================================================================================================
	@Before public void beforeClass() {
		resourceDao = (ResourceDao)applicationContext.getBean("resourceDaoImpl");
	}
	
	@Test
	//@Rollback(false)
	public void testSave() {
		Resource resource = new Resource();
		resource.setAction("/resource");
		resource.setType(Resource.ResourceType.URL.toString());
		resource.setEnabled(false);
		resource.setPriority(5);
		resourceDao.create(resource);
	}

}
