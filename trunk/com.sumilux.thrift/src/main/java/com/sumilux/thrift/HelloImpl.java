package com.sumilux.thrift;

import org.apache.thrift.TException;

public class HelloImpl implements IHello.Iface {

	@Override
	public void insertUser(User user) throws TException {
		System.out.println("-----insertUser-----");
		System.out.println(user.getUsername());
	}

	@Override
	public User queryUser(long userid) throws TException {
		//System.out.println("-----queryUser-----");
		User user = new User();
		user.setUserid(userid);
		user.setUsername("melin");
		return user;
	}

}
