package com.starit.cxf.service;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface HelloWorld {
	public int plus(int param1, int param2);
	
	public String sayHi(@WebParam(name="greet")String greet, @WebParam(name="name")String name);
	
	public void revert(@WebParam(name="num")int num);
}