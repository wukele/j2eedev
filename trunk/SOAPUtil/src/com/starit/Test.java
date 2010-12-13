package com.starit;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试
 * 
 * @author Administrator
 *
 */
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
//		System.out.println("--------------------------------------------------------");
//		wsdlUrl = "http://192.168.20.140:9081/ws/HelloWorldPort?wsdl";
//		client = new SoapClient();
//		params.clear();
//		params.put("arg0", "melin");
//		result = client.sendRequest("sayHello", params, wsdlUrl);
//		System.out.println(result);
		
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
		
		//微软
		//输入参数：theType = 汇率类型：A = 全部汇率；B = 基本汇率；C = 交叉汇率，默认为 A； 
		//返回数据：DataSet，Item(Code) = 代码、Item(Currency) = 名称、Item(ClosePrice) = 最新价、Item(DiffPercent) = 涨跌%、Item(DiffAmount) = 涨跌金额、Item(OpenPrice) = 开盘价、Item(HighPrice) = 最高价、Item(LowPrice) = 最低价、Item(Range) = 震幅%、Item(BuyPrice) = 买入价、Item(SellPrice) = 卖出价、Item(ChangeColor) = 涨跌颜色、Item(DataTime) = 数据时间。
		System.out.println("--------------------------------------------------------");
		wsdlUrl = "http://www.webxml.com.cn/WebServices/ExchangeRateWebService.asmx?wsdl";
		client = new SoapClient();
		params.clear();
		params.put("theType", "A");
		result = client.sendRequest("getExchangeRate", params, wsdlUrl);
		System.out.println(result);
	}
} 
