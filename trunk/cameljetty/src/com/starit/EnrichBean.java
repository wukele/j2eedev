package com.starit;

import org.apache.camel.Exchange;
import org.apache.camel.util.ExchangeHelper;
import org.w3c.dom.Document;

public class EnrichBean {

    public Document enrich(Exchange exchange, Document document) {
        try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        return document;
    }
    
    public Document enrich1(Exchange exchange, Document document) {
        exchange.getIn().removeHeader("CamelHttpUri");
        return document;
    }
}