package com.starit;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.ConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlLoader;
import com.eviware.soapui.model.iface.Operation;

public class SoapClient {
	private static final Logger LOGGER = Logger.getLogger(SoapClient.class);
	
	private DocumentBuilderFactory docBuilderFactory ;
	private Map<String, WsdlInterface[]> wsdls = new LRULinkedHashMap<String, WsdlInterface[]>(256);
	
	public SoapClient() {
        docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setNamespaceAware(true);
	}
	
	public Map<String, String> sendRequest(String operation, Map<String, Object> params, String wsdlUrl) throws Exception{
		int index = wsdlUrl.indexOf("?wsdl");
		String address = wsdlUrl.substring(0, index);
    	return sendRequest(address, operation, params, wsdlUrl);
    }
	
	public Map<String, String> sendRequest(String address, String operation, Map<String, Object> params, String wsdlUrl) throws Exception{
        Operation operationInst = getOperation(wsdlUrl, operation, null);
    	
    	String message = buildRequest(wsdlUrl, operationInst, params, null, null, null);
    	
    	Map<String, String> responseMap = populateResponseOgnlMap(sendRequest(address, message, operationInst.getAction()));
    	
    	return responseMap;
    }
	
	private String sendRequest(String address, String message, String action) throws Exception{
    	String responseBodyAsString;
		PostMethod postMethod = new PostMethod(address);
		try {
			HttpClient client = new HttpClient();
			postMethod.setRequestHeader("SOAPAction", action);
			postMethod.setRequestEntity(new InputStreamRequestEntity(
					new ByteArrayInputStream(message.getBytes("UTF-8")),
					"text/xml")

			);
			client.executeMethod(postMethod);
			responseBodyAsString = postMethod.getResponseBodyAsString();
		} finally {
			postMethod.releaseConnection();
		}
    	
		return responseBodyAsString;
    }
	
	private String buildRequest(String wsdl, Operation operationInst, Map<String, Object> params, Properties httpClientProps, String smooksResource, String soapNs) throws IOException, UnsupportedOperationException, SAXException {
        String requestTemplate = operationInst.getRequestAt(0).getRequestContent();
        
        return buildSOAPMessage(requestTemplate, params, smooksResource, soapNs);
    }
	
	private Operation getOperation(String wsdl, String operation, Properties httpClientProps) throws IOException, UnsupportedOperationException {
        WsdlInterface[] wsdlInterfaces = getWsdlInterfaces(wsdl, httpClientProps);
        
        for (WsdlInterface wsdlInterface : wsdlInterfaces) {
            Operation operationInst = wsdlInterface.getOperationByName(operation);
            
            if (operationInst != null) {
                return operationInst;
            }
        }
        
        wsdls.remove(wsdl);
        wsdlInterfaces = getWsdlInterfaces(wsdl, httpClientProps);

        for (WsdlInterface wsdlInterface : wsdlInterfaces) {
            Operation operationInst = wsdlInterface.getOperationByName(operation);

            if (operationInst != null) {
                return operationInst;
            }
        }
        
        throw new UnsupportedOperationException("Operation '" + operation + "' not supported by WSDL '" + wsdl + "'.");
    }
	
	private WsdlInterface[] getWsdlInterfaces(String wsdl, Properties httpClientProps) throws IOException {
        try {
            WsdlInterface[] wsdlInterfaces = wsdls.get(wsdl);
            if (wsdlInterfaces == null) {
                WsdlProject wsdlProject = new WsdlProject();
                wsdlInterfaces = wsdlProject.importWsdl(wsdl, true, createWsdlLoader(wsdl, httpClientProps));
                wsdls.put(wsdl, wsdlInterfaces);
            }
            return wsdlInterfaces;
        } catch (Exception e) {
            IOException ioe = new IOException("Failed to import WSDL '" + wsdl + "'.");
            ioe.initCause(e);
            throw ioe;
        }
    }
	
	private WsdlLoader createWsdlLoader(String wsdl, Properties httpClientProps) throws ConfigurationException {
        HttpClient httpClient = new HttpClient();
        return new ClientWsdlLoader(wsdl, httpClient);
    }
	
	private String buildSOAPMessage(String soapMessageTemplate, Map<String, Object> params, String smooksResource, String soapNs) throws IOException, SAXException {
        Document messageDoc = getDocBuilder().parse(new InputSource(new StringReader(soapMessageTemplate)));

        Element docRoot = messageDoc.getDocumentElement();

        boolean dumpSOAP = params.containsKey("dumpSOAP");
        if(dumpSOAP) {
            dumpSOAP("SOAP Template (Unexpanded):", docRoot);
        }

        injectParameters(docRoot, params, soapNs);

        if(dumpSOAP) {
            dumpSOAP("SOAP Message (Populated Template):", docRoot);
        }
        
        return XmlUtil.serialize(messageDoc.getChildNodes());
    }
	
	private synchronized DocumentBuilder getDocBuilder() throws IOException {
        try {
            return docBuilderFactory.newDocumentBuilder();
        } catch (final ParserConfigurationException pce) {
            final IOException ioe = new IOException("Could not create document builder") ;
            ioe.initCause(pce) ;
            throw ioe ;
        }
    }
	
	private void dumpSOAP(String message, Element docRoot) {
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(message + "\n");
        try {
            DOMUtil.serialize(docRoot, new StreamResult(System.out), true);
        } catch (ConfigurationException e) {
            LOGGER.error("Unable to dump SOAP.", e);
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
    }
    

    private void injectParameters(Element element, Map<String, Object> params, String soapNs) {
        NodeList children = element.getChildNodes();
        int childCount = children.getLength();

        for (int i = 0; i < childCount; i++) {
            Node node = children.item(i);

            if (childCount == 1 && node.getNodeType() == Node.TEXT_NODE) {
                if (node.getNodeValue().equals("?")) {
                    //String ognl = OGNLUtils.getOGNLExpression(element, soapNs);
                    Object param = params.get(DOMUtil.getName(element));

                    //param = OGNLUtils.getParameter(ognl, params);

                    element.removeChild(node);
                    element.appendChild(element.getOwnerDocument().createTextNode(param.toString()));
                }
            } else if (node.getNodeType() == Node.ELEMENT_NODE) {
                injectParameters((Element) node, params, soapNs);
            }
        }
    }
    
    private Map<String, String> populateResponseOgnlMap(String response) {
        Map<String, String> map = new LinkedHashMap<String, String>();

        try {
            DocumentBuilder docBuilder = getDocBuilder() ;
            Document doc = docBuilder.parse(new InputSource(new StringReader(response)));
            Element graphRootElement = getGraphRootElement(doc.getDocumentElement());

            populateResponseOgnlMap(map, graphRootElement);
        } catch (SAXException e) {
            throw new RuntimeException("Error parsing SOAP response.", e);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected error reading SOAP response.", e);
        }

        return map;
    }
    
    private void populateResponseOgnlMap(Map<String, String> map, Element element) {
        NodeList children = element.getChildNodes();
        int childCount = children.getLength();

        // If the element has a solitary TEXT child, add the text value
        // against a map key of the elements OGNL expression value.
        if(childCount == 1) {
            Node childNode = children.item(0);
            if(childNode.getNodeType() == Node.TEXT_NODE) {
                String ognl = DOMUtil.getName(element); //OGNLUtils.getOGNLExpression(element);
                map.put(ognl, childNode.getNodeValue());
                return;
            }
        }

        // So the element doesn't contain a solitary TEXT node.  Drill down...
        for(int i = 0; i < childCount; i++) {
            Node childNode = children.item(i);
            if(childNode.getNodeType() == Node.ELEMENT_NODE) {
                populateResponseOgnlMap(map, (Element)childNode);
            }
        }
    }
    
    private Element getGraphRootElement(Element element) {
        String ognl = OGNLUtils.getOGNLExpression(element);

        if(ognl != null && !ognl.equals("")) {
            return element;
        }

        NodeList children = element.getChildNodes();
        int childCount = children.getLength();
        for(int i = 0; i < childCount; i++) {
            Node node = children.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element graphRootElement = getGraphRootElement((Element)node);
                if(graphRootElement != null) {
                    return graphRootElement;
                }
            }
        }

        return null;
    }
}
