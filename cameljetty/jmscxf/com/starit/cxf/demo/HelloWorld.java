package com.starit.cxf.demo;


import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface HelloWorld {
	public int plus(int param1, int param2);
	
	public void testCollection(List<String> list);
	
	public String sayHi(@WebParam(name="greet")String greet, @WebParam(name="name")String name);
	
	public void revert(@WebParam(name="num")int num);
	
	public User createUser(User user);
}