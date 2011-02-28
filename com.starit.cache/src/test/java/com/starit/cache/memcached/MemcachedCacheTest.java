package com.starit.cache.memcached;

import java.io.IOException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClient;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.starit.cache.Cache;


/**
 * 
 * @author  libinsong1204@gmail.com
 * @date    2011-1-18 上午11:03:34
 * @version 
 */
public class MemcachedCacheTest {
	private static Cache cache;
	
	@BeforeClass
	public static void init() throws IOException {
		MemcachedClient client = new XMemcachedClient("192.168.20.14",11111);
		cache = new MemcachedCache(client);
	}
	
	@Test
	public void testPutItem() {
		User user = new User(2L, "melin", "合肥");
		cache.putItem(user);
		User newUser = cache.getItem(user);
		Assert.assertEquals("合肥", newUser.getAddress());
	}
}
