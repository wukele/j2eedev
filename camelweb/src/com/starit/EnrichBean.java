/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.starit;

import java.util.List;

import javax.xml.soap.SOAPMessage;

import org.apache.camel.Exchange;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * A bean to enrich the proxied web service to ensure the input is valid and add additional information
 *
 * @version $Revision: 1001408 $
 */
// START SNIPPET: e1
public class EnrichBean {

    public Document enrich(Exchange exchange, Document document) {
    	
    	Node node = document.getElementsByTagName("arg0").item(0);
        String arg0 = node.getTextContent();
        node.setTextContent("456");
        System.out.println("arg0 was " + arg0 + ", changed to 456");
    	

        return document;
        //http://camel.apache.org/advanced-configuration-of-camelcontext-using-spring.html
    }
}
// END SNIPPET: e1
