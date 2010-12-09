package com.starit;

import java.util.HashMap;
import java.util.Map;

public class Test {
	public static void main(String[] args) throws Exception {
		//axis2
		String wsdlUrl = "http://192.168.20.14:8080/camelweb/webservices/camel/crmmocktest?wsdl";
		SoapClient client = new SoapClient();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("custOrderConditionInfo", "1234");
		Map<String, String> result = client.sendRequest("businessService", params, wsdlUrl);
		System.out.println(result);
		
		//jax-ws
		System.out.println("--------------------------------------------------------");
		wsdlUrl = "http://192.168.20.140:9081/ws/HelloWorldPort?wsdl";
		client = new SoapClient();
		params.clear();
		params.put("arg0", "melin");
		result = client.sendRequest("sayHello", params, wsdlUrl);
		System.out.println(result);
		
		//cxf
		System.out.println("--------------------------------------------------------");
		wsdlUrl = "http://192.168.20.14:8080/camelweb/webservices/camel/helloworld?wsdl";
		client = new SoapClient();
		params.clear();
		params.put("greet", "hello");
		params.put("name", "melin");
		//params.put("dumpSOAP", "true");
		result = client.sendRequest("sayHi", params, wsdlUrl);
		System.out.println(result);
	}
} 
