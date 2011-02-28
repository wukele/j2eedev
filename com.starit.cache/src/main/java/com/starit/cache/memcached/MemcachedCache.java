package com.starit.cache.memcached;

import net.rubyeye.xmemcached.MemcachedClient;

import org.springframework.util.Assert;

import com.starit.cache.CacheException;
import com.starit.cache.support.AbstractCache;

/**
 *
 * @datetime 2010-8-20 上午11:00:48
 * @author libinsong1204@gmail.com
 */
public final class MemcachedCache extends AbstractCache {
	private MemcachedClient memcachedClient;
	
	public MemcachedCache(MemcachedClient memcachedClient) {
		Assert.notNull(memcachedClient, "memcachedClient is not null");
		this.memcachedClient = memcachedClient;
	}

	public Object getItem(String key) {
		try {
			return this.memcachedClient.get(key);
        } catch (Throwable t) {
            throw new CacheException("", t);
        }
	}
	
	public void putItem(String key, Object value) {
		try {
            this.memcachedClient.set(key, 0, value);
        } catch (Throwable t) {
            throw new CacheException("", t);
        }
	}

	public Object removeItem(String key) {
		try {
            Object obj = this.getItem(key);
            this.memcachedClient.delete(key);
            return obj;
        } catch (Throwable t) {
            throw new CacheException("", t);
        }
	}

	public void cleanItem(String key) {
		try {
            this.memcachedClient.delete(key);
        } catch (Throwable t) {
            throw new CacheException("", t);
        }
	}
}
