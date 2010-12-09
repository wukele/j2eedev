package com.starit;

import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * Utilities for SOAP messages processing
 * 
 * Thanks to Tom Fennelly from Jboss Group
 * 
 * @author wilson
 *
 */
public class OGNLUtils {
    public static final String IS_COLLECTION_ATTRIB = "is-collection";

    private enum SOAPNameSpaces {
		SOAP_1_1("http://schemas.xmlsoap.org/soap/envelope/"), SOAP_1_2(
				"http://www.w3.org/2003/05/soap-envelope");

		private String nameSpace;

		SOAPNameSpaces(final String nameSpace) {
			this.nameSpace = nameSpace;
		}

		public String getNameSpace() {
			return nameSpace;
		}
	}

    public static String getOGNLExpression(Element element) {
        StringBuffer ognlExpression = new StringBuffer();
        Node parent = element.getParentNode();
        boolean isInBody = false;

        ognlExpression.append(getOGNLToken(element));

        while (parent != null && parent.getNodeType() == Node.ELEMENT_NODE) {
            Element parentElement = (Element) parent;
            String parentName = DOMUtil.getName(parentElement);

            if (parentName.equalsIgnoreCase("body") &&
                    parent.getNamespaceURI().equalsIgnoreCase("http://schemas.xmlsoap.org/soap/envelope/")) {
                isInBody = true;
                break;
            }


            ognlExpression.insert(0, getOGNLToken(parentElement));
            parent = parent.getParentNode();
        }

        if(!isInBody) {
            return "";
        }

        // Remove the leading '.'
        ognlExpression.deleteCharAt(0);

        return ognlExpression.toString();
    }

    
    public static String getOGNLExpression(Element element, String nameSpace) {
		StringBuffer ognlExpression = new StringBuffer();
		Node parent = element.getParentNode();
		boolean isInBody = false;

		ognlExpression.append(getOGNLToken(element));

		while (parent != null && parent.getNodeType() == Node.ELEMENT_NODE) {
			Element parentElement = (Element) parent;
			String parentName = DOMUtil.getName(parentElement);

			if (parentName.equalsIgnoreCase("body")
					&& checkParentNameSpace(parent.getNamespaceURI(), nameSpace)) {
				isInBody = true;
				break;
			}


			ognlExpression.insert(0, getOGNLToken(parentElement));
			parent = parent.getParentNode();
		}

		if (!isInBody) {
			return "";
		}

		// Remove the leading '.'
		ognlExpression.deleteCharAt(0);

		return ognlExpression.toString();
	}

	/**
	 * Will try to match the argument parentNS to a enum of default SOAP
	 * namespaces. If no match is made for a default SOAP namespace, this method
	 * will try to match the argument namespace.
	 * 
	 * @param parentNS
	 * @param namespace
	 * @return <code>true</code> if a namespace match has been made.
	 */
	protected static boolean checkParentNameSpace(final String parentNS,
			String namespace) {
		if (parentNS == null)
			return false;

		SOAPNameSpaces[] defaultNamespaces = SOAPNameSpaces.values();
		for (SOAPNameSpaces defaultNS : defaultNamespaces) {
			if (parentNS.equalsIgnoreCase(defaultNS.getNameSpace()))
				return true;
		}

		return parentNS.equalsIgnoreCase(namespace);
	}

    
    public static String getOGNLToken(Element element) {
        String localName = element.getLocalName();
        String ognlToken;

        if (assertIsParentCollection(element)) {
            int count = DOMUtil.countElementsBefore(element, element.getTagName());
            ognlToken = "[" + count + "]";
        } else {
            ognlToken = "." + localName;
        }

        return ognlToken;
    }

    private static boolean assertIsCollection(Element element) {
        Comment firstComment = (Comment) DOMUtil.getFirstChildByType(element, Node.COMMENT_NODE);

        // TODO: Get Ole (soapUI) to add an attribute to the collection element - better than looking for this comment.
        if(firstComment != null && firstComment.getNodeValue().indexOf("1 or more repetitions") != -1) {
            return true;
        }

        return false;
    }

    private static boolean assertIsParentCollection(Element element) {
        Node parent = element.getParentNode();
        if (parent != null && parent.getNodeType() == Node.ELEMENT_NODE && assertIsCollection((Element)parent)) {
            return true;
        }
        return false;
    }

}
