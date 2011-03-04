package com.starit.cache.memcached;

import java.io.Serializable;

import com.starit.cache.annotation.CacheKey;

/**
 * 
 * @author  libinsong1204@gmail.com
 * @date    2011-1-18 上午11:03:34
 * @version 
 */
@CacheKey("user-${id}")
public class User implements Serializable {
	//private static final long serialVersionUID = 918825037296130599L;
	private Long id;
	private String name;
	private String address;
	private String sdf;
	public String getSdf() {
		return sdf;
	}

	public void setSdf(String sdf) {
		this.sdf = sdf;
	}

	public User() {}
	
	public User(Long id, String name, String address) {
		this.id = id;
		this.name = name;
		this.address = address;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
