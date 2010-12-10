package com.starit.cxf.service;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebService(endpointInterface = "com.starit.cxf.service.HelloWorld",
            serviceName = "HelloWorld")
public class HelloWorldImpl implements HelloWorld {
	private static Logger logger = LoggerFactory.getLogger(HelloWorldImpl.class);
    
    public HelloWorldImpl() {
	}

	public String sayHi(String greet, String name) {
        System.out.println("sayHi called");
        return greet + " " + name;
    }
    
    public int plus(int param1, int param2) {
    	//System.out.println("sayHi plus");
        return param1 + param2;
    }

    public void revert(int num) {
    	logger.info("total: " + num);
    }
}
