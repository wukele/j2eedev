package com.starit;

import org.apache.camel.Exchange;
import org.apache.camel.Header;

public class DynamicRouterTest {
	/**
	 * Use this method to compute dynamic where we should route next.
	 *
	 * @param body the message body
	 * @return endpoints to go, or <tt>null</tt> to indicate the end
	 */
	public String slip(Object body, @Header(Exchange.SLIP_ENDPOINT) String previous) {
		if(previous == null)
			return "cxf:bean:crmmocktest?dataFormat=MESSAGE&amp;headerFilterStrategy=#cxfHeaderFilterStrategy";
		else
			return null;
	}
}
