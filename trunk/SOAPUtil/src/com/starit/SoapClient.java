package com.starit;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.naming.ConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.w3c.dom.Comment;
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
	
	private static final String IS_CLONE_ATTRIB = "is-clone";
    private static final String CLONED_POSTFIX = " - cloned";
    private static final String SOAPUI_CLONE_COMMENT = " repetitions:";
	
	private DocumentBuilderFactory docBuilderFactory ;
	private Map<String, WsdlInterface[]> wsdls = new HashMap<String, WsdlInterface[]>();
	
	public SoapClient() {
        docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setNamespaceAware(true);
	}
	
	public Map sendRequest(String operation, Map params, String wsdlUrl) throws Exception{
		int index = wsdlUrl.indexOf("?wsdl");
		String address = wsdlUrl.substring(0, index);
    	return sendRequest(address, operation, params, wsdlUrl);
    }
	
	public Map sendRequest(String address, String operation, Map params, String wsdlUrl) throws Exception{
        Operation operationInst = getOperation(wsdlUrl, operation, null);
    	
    	String message = buildRequest(wsdlUrl, operationInst, params, null, null, null);
    	
    	Map responseMap = populateResponseOgnlMap(sendRequest(address, message, operationInst.getAction()));
    	
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
	
	private String buildRequest(String wsdl, Operation operationInst, Map params, Properties httpClientProps, String smooksResource, String soapNs) throws IOException, UnsupportedOperationException, SAXException {
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
	
	private String buildSOAPMessage(String soapMessageTemplate, Map params, String smooksResource, String soapNs) throws IOException, SAXException {
        Document messageDoc = getDocBuilder().parse(new InputSource(new StringReader(soapMessageTemplate)));

        Element docRoot = messageDoc.getDocumentElement();

        boolean dumpSOAP = params.containsKey("dumpSOAP");
        if(dumpSOAP) {
            dumpSOAP("SOAP Template (Unexpanded):", docRoot);
        }

        expandMessage(docRoot, params);

        if(dumpSOAP) {
            dumpSOAP("SOAP Template (Expanded):", docRoot);
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
	
	/**
     * Expand the message to accommodate data collections.
     * <p/>
     * It basically just clones the message where appropriate.
     *
     * @param element The element to be processed.
     * @param params  The message params.  Uses the message params to
     *                decide whether or not cloning is required.
     */
    private void expandMessage(Element element, Map params) {

        // If this element is not a cloned element, check does it need to be cloned...
        if (!element.hasAttributeNS(OGNLUtils.JBOSSESB_SOAP_NS, IS_CLONE_ATTRIB)) {
            String ognl = OGNLUtils.getOGNLExpression(element);
            Element clonePoint = getClonePoint(element);

            if(clonePoint != null) {
                int collectionSize;

                collectionSize = calculateCollectionSize(ognl, params);

                if(collectionSize == -1) {
                    // It's a collection, but has no entries that match the OGNL expression for this element...
                    if(clonePoint == element) {
                        // If the clonePoint is the element itself, we remove it... we're done with it...
                        clonePoint.getParentNode().removeChild(clonePoint);
                    } else {
                        // If the clonePoint is not the element itself (it's a child element), leave it
                        // and check it again when we get to it...
                        resetClonePoint(clonePoint);
                    }
                } else if(collectionSize == 0) {
                    // It's a collection, but has no entries, remove it...
                    clonePoint.getParentNode().removeChild(clonePoint);
                } else if(collectionSize == 1) {
                    // It's a collection, but no need to clone coz we
                    // already have an entry for it...
                    clonePoint.setAttributeNS(OGNLUtils.JBOSSESB_SOAP_NS, OGNLUtils.JBOSSESB_SOAP_NS_PREFIX + OGNLUtils.OGNL_ATTRIB, ognl + "[0]");
                } else {
                    // It's a collection and we need to do some cloning
                    if(clonePoint != null) {
                        // We already have one, so decrement by one...
                        cloneCollectionTemplateElement(clonePoint, (collectionSize - 1), ognl);
                    } else {
                        LOGGER.warn("Collection/array template element <" + element.getLocalName() + "> would appear to be invalid.  It doesn't contain any child elements.");
                    }
                }
            }
        }

        // Now do the same for the child elements...
        List<Node> children = DOMUtil.copyNodeList(element.getChildNodes());
        for (Node node : children) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                expandMessage((Element) node, params);
            }
        }
    }
    
    private Element getClonePoint(Element element) {
        Comment comment;

        // Is it this element...
        comment = getCommentBefore(element);
        if(comment != null && comment.getNodeValue().endsWith(SOAPUI_CLONE_COMMENT)) {
            comment.setNodeValue(comment.getNodeValue() + CLONED_POSTFIX);
            return element;
        }

        // Is it the first child element of this element...
        Element firstChildElement = getFirstChildElement(element);
        if(firstChildElement != null) {
            comment = getCommentBefore(firstChildElement);
            if(comment != null && comment.getNodeValue().endsWith(SOAPUI_CLONE_COMMENT)) {
                comment.setNodeValue(comment.getNodeValue() + CLONED_POSTFIX);
                return firstChildElement;
            }
        }

        return null;
    }
    
    private Comment getCommentBefore(Element element) {
        Node sibling = element.getPreviousSibling();

        while(sibling != null) {
            if(sibling.getNodeType() == Node.COMMENT_NODE) {
                return (Comment) sibling;
            } else if(sibling.getNodeType() == Node.TEXT_NODE) {
                // continue...
                sibling = sibling.getPreviousSibling();
            } else {
                // It's an Element, CData, PI etc
                return null;
            }
        }

        return null;
    }
    
    private Element getFirstChildElement(Element element) {
        NodeList children = element.getChildNodes();
        int childCount = children.getLength();

        for(int i = 0; i < childCount; i++) {
            Node child = children.item(i);
            if(child.getNodeType() == Node.ELEMENT_NODE) {
                return (Element) child;
            }
        }

        return null;
    }
    
    private int calculateCollectionSize(String ognl, Map params) {
        // Try for an Object graph based collection...
        Object param = OGNLUtils.getParameter(ognl, params);
        if (param != null) {
            Class paramRuntime = param.getClass();
            if (paramRuntime.isArray()) {
                return ((Object[]) param).length;
            } else if (Collection.class.isAssignableFrom(paramRuntime)) {
                return ((Collection) param).size();
            }
        }

        // Try for <String, Object> collection based map entries...
        Set<Map.Entry> entries = params.entrySet();
        String collectionPrefix = ognl + "[";
        int maxIndex = -1;
        for (Map.Entry entry : entries) {
            Object keyObj = entry.getKey();
            if(keyObj instanceof String) {
                String key = (String)keyObj;
                if(key.startsWith(collectionPrefix)) {
                    int endIndex = key.indexOf(']', collectionPrefix.length());
                    String ognlIndexValString = key.substring(collectionPrefix.length(), endIndex);
                    try {
                        int ognlIndexVal = Integer.valueOf(ognlIndexValString);
                        maxIndex = Math.max(maxIndex, ognlIndexVal);
                    } catch(NumberFormatException e) {}
                }
            }
        }

        if(maxIndex != -1) {
            return maxIndex + 1;
        }

        // It's a collection, but nothing in this message for it collection...
        return -1;
    }
    
    private void resetClonePoint(Element clonePoint) {
        Comment comment = getCommentBefore(clonePoint);

        if(comment == null) {
            throw new IllegalStateException("Call to reset a 'clonePoint' that doesn't have a comment before it.");
        }

        String commentText = comment.getNodeValue();
        if(!commentText.endsWith(CLONED_POSTFIX)) {
            throw new IllegalStateException("Call to reset a 'clonePoint' that doesn't have a proper clone comment before it.");
        }

        comment.setNodeValue(commentText.substring(0, commentText.length() - CLONED_POSTFIX.length()));
    }

    /**
     * Clone a collection node.
     * <p/>
     * Note we have to frig with the OGNL expressions for collections/arrays because the
     * collection entry is represented by [0], [1] etc in the OGNL expression, not the actual
     * element name on the DOM e.g. collection node "order/items/item" (where "item" is the
     * actual collection entry) maps to the OGNL expression "order.items[0]" etc.
     *
     * @param element    The collection/array "entry" sub-branch.
     * @param cloneCount The number of times it needs to be cloned.
     * @param ognl       The OGNL expression for the collection/array. Not including the
     *                   indexing part.
     */
    private void cloneCollectionTemplateElement(Element element, int cloneCount, String ognl) {
        if (element == null) {
            return;
        }

        Node insertPoint = element.getNextSibling();
        Node parent = element.getParentNode();

        element.setAttributeNS(OGNLUtils.JBOSSESB_SOAP_NS, OGNLUtils.JBOSSESB_SOAP_NS_PREFIX + OGNLUtils.OGNL_ATTRIB, ognl + "[0]");
        for (int i = 0; i < cloneCount; i++) {
            Element clone = (Element) element.cloneNode(true);

            clone.setAttributeNS(OGNLUtils.JBOSSESB_SOAP_NS, OGNLUtils.JBOSSESB_SOAP_NS_PREFIX + IS_CLONE_ATTRIB, "true");
            clone.setAttributeNS(OGNLUtils.JBOSSESB_SOAP_NS, OGNLUtils.JBOSSESB_SOAP_NS_PREFIX + OGNLUtils.OGNL_ATTRIB, ognl + "[" + Integer.toString(i + 1) + "]");
            if (insertPoint == null) {
                parent.appendChild(clone);
            } else {
                parent.insertBefore(clone, insertPoint);
            }
        }
    }
    
    private void injectParameters(Element element, Map params, String soapNs) {
        NodeList children = element.getChildNodes();
        int childCount = children.getLength();

        for (int i = 0; i < childCount; i++) {
            Node node = children.item(i);

            if (childCount == 1 && node.getNodeType() == Node.TEXT_NODE) {
                if (node.getNodeValue().equals("?")) {
                    //String ognl = OGNLUtils.getOGNLExpression(element, soapNs);
                    Object param = params.get(element.getTagName());

                    //param = OGNLUtils.getParameter(ognl, params);

                    element.removeChild(node);
                    element.appendChild(element.getOwnerDocument().createTextNode(param.toString()));
                }
            } else if (node.getNodeType() == Node.ELEMENT_NODE) {
                injectParameters((Element) node, params, soapNs);
            }
        }

        element.removeAttributeNS(OGNLUtils.JBOSSESB_SOAP_NS, IS_CLONE_ATTRIB);
        element.removeAttributeNS(OGNLUtils.JBOSSESB_SOAP_NS, OGNLUtils.OGNL_ATTRIB);
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
                String ognl = OGNLUtils.getOGNLExpression(element);
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
