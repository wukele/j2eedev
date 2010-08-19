package com.starit.web.dao;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.starit.web.model.Resource;

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
	
	//@Test
	//@Rollback(false)
	public void testSave() {
		Resource resource = new Resource();
		resource.setAction("/resource");
		resource.setType(Resource.ResourceType.URL.toString());
		resource.setEnabled('N');
		resource.setPriority(5);
		resourceDao.create(resource);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testFind() {
		List<Resource> list = (List<Resource>) resourceDao.find("from Resource as res left join res.roles where res.id=1");
		System.out.println(list);
	}

}
