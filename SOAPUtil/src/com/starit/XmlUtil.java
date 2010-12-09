/*
 Milyn - Copyright (C) 2006

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License (version 2.1) as published by the Free Software 
 Foundation.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 
 See the GNU Lesser General Public License for more details:    
 http://www.gnu.org/licenses/lgpl.txt
 */

package com.starit;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XMl utility methods.
 *
 * @author Tom Fennelly
 */

public class XmlUtil {


    /**
     * Serialise the supplied W3C DOM subtree.
     * <p/>
     * The output is unformatted.
     *
     * @param nodeList The DOM subtree as a NodeList.
     * @return The subtree in serailised form.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static String serialize(NodeList nodeList) throws DOMException {
        return serialize(nodeList, false);
    }

    /**
     * Serialise the supplied W3C DOM subtree.
     *
     * @param node The DOM node to be serialized.
     * @param format Format the output.
     * @return The subtree in serailised form.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static String serialize(final Node node, boolean format) throws DOMException {
        StringWriter writer = new StringWriter();
        serialize(node, format, writer);
        return writer.toString();
    }

    /**
     * Serialise the supplied W3C DOM subtree.
     *
     * @param node The DOM node to be serialized.
     * @param format Format the output.
     * @param writer The target writer for serialization.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static void serialize(final Node node, boolean format, Writer writer) throws DOMException {
        if(node.getNodeType() == Node.DOCUMENT_NODE) {
            serialize(node.getChildNodes(), format, writer);
        } else {
            serialize(new NodeList() {
                public Node item(int index) {
                    return node;
                }

                public int getLength() {
                    return 1;
                }
            }, format, writer);
        }
    }

    /**
     * Serialise the supplied W3C DOM subtree.
     *
     * @param nodeList The DOM subtree as a NodeList.
     * @param format Format the output.
     * @return The subtree in serailised form.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static String serialize(NodeList nodeList, boolean format) throws DOMException {
        StringWriter writer = new StringWriter();
        serialize(nodeList, format, writer);
        return writer.toString();
    }

    /**
     * Serialise the supplied W3C DOM subtree.
     *
     * @param nodeList The DOM subtree as a NodeList.
     * @param format Format the output.
     * @param writer The target writer for serialization.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static void serialize(NodeList nodeList, boolean format, Writer writer) throws DOMException {

        if (nodeList == null) {
            throw new IllegalArgumentException(
                    "null 'subtree' NodeIterator arg in method call.");
        }

        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer;

            if(format) {
                try {
                    factory.setAttribute("indent-number", new Integer(4));
                } catch(Exception e) {
                    // Ignore... Xalan may throw on this!!
                    // We handle Xalan indentation below (yeuckkk) ...
                }
            }
            transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            if(format) {
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4");
            }

            int listLength = nodeList.getLength();

            // Iterate through the Node List.
            for (int i = 0; i < listLength; i++) {
                Node node = nodeList.item(i);

                if (XmlUtil.isTextNode(node)) {
                    writer.write(node.getNodeValue());
                } else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                    writer.write(((Attr) node).getValue());
                } else if (node.getNodeType() == Node.ELEMENT_NODE) {
                    transformer.transform(new DOMSource(node), new StreamResult(writer));
                }
            }
        } catch (Exception e) {
            DOMException domExcep = new DOMException(
                    DOMException.INVALID_ACCESS_ERR,
                    "Unable to serailise DOM subtree.");
            domExcep.initCause(e);
            throw domExcep;
        }
    }

    /**
     * Is the supplied W3C DOM Node a text node.
     *
     * @param node The node to be tested.
     * @return True if the node is a text node, otherwise false.
     */
    public static boolean isTextNode(Node node) {
        short nodeType;

        if (node == null) {
            return false;
        }
        nodeType = node.getNodeType();

        return nodeType == Node.CDATA_SECTION_NODE
                || nodeType == Node.TEXT_NODE;
    }
}
