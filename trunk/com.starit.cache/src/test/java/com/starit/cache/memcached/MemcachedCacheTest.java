package com.starit.cache.memcached;

import java.io.IOException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.transcoders.TokyoTyrantTranscoder;

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
		client.setPrimitiveAsString(false);
		client.setTranscoder(new TokyoTyrantTranscoder());
	}
	
	@Test
	public void testPutItem() {
		User user = new User(106L, "melin", "合肥");
		cache.cleanItem(user);
		cache.putItem(user);
		User newUser = cache.getItem(user);
		Assert.assertEquals("合肥", newUser.getAddress());
		Assert.assertNull(newUser.getSdf());
	}
	
	//@Test
	public void testPutItems() {
		User user = new User(106L, "melin", "合肥");
		long start = System.currentTimeMillis();
		for(int i=0; i<10000; i++)
			cache.putItem(user);
		System.out.println("total time: " + (System.currentTimeMillis() - start));
	}
	
	@Test
	public void testPutItemsw() {
		User user = new User(106L, "melin", "合肥");
		long start = System.currentTimeMillis();
		for(int i=0; i<10000; i++)
			cache.getItem(user);
		System.out.println("total time: " + (System.currentTimeMillis() - start));
	}
}
