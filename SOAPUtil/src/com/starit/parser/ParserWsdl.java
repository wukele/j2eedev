package com.starit.parser;

import java.util.List;
import java.util.Properties;

import javax.naming.ConfigurationException;

import org.apache.commons.httpclient.HttpClient;

import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlLoader;
import com.eviware.soapui.model.iface.Operation;
import com.starit.ClientWsdlLoader;

/**
 * Ω‚ŒˆWSDL
 * 
 * @author  libinsong1204@gmail.com
 * @date    2010-12-14
 * @version 
 */
public class ParserWsdl {
	public static void main(String[] args) throws Exception {
		WsdlProject wsdlProject = new WsdlProject();
		String wsdl = "http://webservice.webxml.com.cn/WebServices/StockInfoWS.asmx?wsdl";
		WsdlInterface[] wsdlInterfaces = wsdlProject.importWsdl(wsdl, true, createWsdlLoader(wsdl, null));
		
		for (WsdlInterface wsdlInterface : wsdlInterfaces) {
            System.out.println("=============================="+wsdlInterface.getName()+"=================================");
            List<Operation> operationInsts = wsdlInterface.getOperations();
            for(Operation operationInst : operationInsts) {
            	String requestTemplate = operationInst.getRequestAt(0).getRequestContent();
            	//System.out.println("Action: "+operationInst.getAction());
            	System.out.println(operationInst.getName());
            }
        }
	}
	
	private static WsdlLoader createWsdlLoader(String wsdl, Properties httpClientProps) throws ConfigurationException {
        HttpClient httpClient = new HttpClient();
        return new ClientWsdlLoader(wsdl, httpClient);
    }
}
