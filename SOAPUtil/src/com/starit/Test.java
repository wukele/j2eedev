package com.starit;

import java.util.HashMap;
import java.util.Map;

public class Test {
	public static void main(String[] args) throws Exception {
		//axis2
//		String address = "http://192.168.20.14:8080/camelweb/webservices/camel/crmmocktest";
//		String wsdlUrl = "http://192.168.20.14:8080/camelweb/webservices/camel/crmmocktest?wsdl";
//		SoapClient client = new SoapClient();
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("custOrderConditionInfo", "1234");
//		Map result = client.sendRequest(address, "businessService", params, wsdlUrl);
//		System.out.println(result);
//		
//		//jax-ws
//		System.out.println("--------------------------------------------------------");
//		address = "http://192.168.20.140:9081/ws/HelloWorldPort";
//		wsdlUrl = "http://192.168.20.140:9081/ws/HelloWorldPort?wsdl";
//		client = new SoapClient();
//		params.clear();
//		params = new HashMap<String, String>();
//		params.put("arg0", "melin");
//		result = client.sendRequest(address, "sayHello", params, wsdlUrl);
//		System.out.println(result);
		
		//cxf
		System.out.println("--------------------------------------------------------");
		String wsdlUrl = "http://localhost:9000/helloWorld?wsdl";
		SoapClient client = new SoapClient();
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("greet", "hello");
		params1.put("name", "melin");
		Map result = client.sendRequest("sayHi", params1, wsdlUrl);
		System.out.println(result);
		result = client.sendRequest("sayHi", params1, wsdlUrl);
		System.out.println(result);
	}
} 
