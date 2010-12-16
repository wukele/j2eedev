package com.starit;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author  libinsong1204@gmail.com
 * @date    2010-12-16
 * @version 
 */
public class JavaBeanTest {
	public static void main(String[] args) throws Exception {
		String wsdlUrl = "http://localhost:9000/helloWorld?wsdl";
		SoapClient client = new SoapClient();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("createUser.arg0.address", "Anhui Hefei");
		params.put("createUser.arg0.name", "melin");
		params.put("dumpSOAP", "");
		Map<String, String> result = client.sendRequest("createUser", params, wsdlUrl);
		System.out.println(result);
	}
}
