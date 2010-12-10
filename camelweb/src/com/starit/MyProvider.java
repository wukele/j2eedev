package com.starit;

import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Provider;

public class MyProvider implements Provider<SOAPMessage> {

	public SOAPMessage invoke(SOAPMessage request) {
		// Requests should not come here as the Camel route will
        // intercept the call before this is invoked.
        throw new UnsupportedOperationException("Placeholder method");
	}

}
