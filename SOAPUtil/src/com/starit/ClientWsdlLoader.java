package com.starit;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import org.apache.log4j.Logger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlLoader;

public class ClientWsdlLoader extends WsdlLoader{
	private static Logger logger = Logger.getLogger(ClientWsdlLoader.class);

    private boolean isAborted = false;
    private HttpClient httpClient;
    
    public ClientWsdlLoader(String url, HttpClient httpClient) {
        super(url);
        this.httpClient = httpClient;
    }

    public InputStream load(String url) throws Exception {
        GetMethod httpGetMethod;

        if(url.startsWith("file")) {
            return new URL(url) .openStream();
        }

        // Authentication is not being overridden on the method.  It needs
        // to be present on the supplied HttpClient instance!
        httpGetMethod = new GetMethod(url);
        httpGetMethod.setDoAuthentication(true);

        try {
            int result = httpClient.executeMethod(httpGetMethod);

            if(result != HttpStatus.SC_OK) {
                if(result < 200 || result > 299) {
                    throw new HttpException("Received status code '" + result + "' on WSDL HTTP (GET) request: '" + url + "'.");
                } else {
                    logger.warn("Received status code '" + result + "' on WSDL HTTP (GET) request: '" + url + "'.");
                }
            }

            return new ByteArrayInputStream(httpGetMethod.getResponseBody());
        } finally {
            httpGetMethod.releaseConnection();
        }
    }

    public boolean abort() {
        isAborted = true;
        return true;
    }

    public boolean isAborted() {
        return isAborted;
    }

    public void close() {
        
    }

}
