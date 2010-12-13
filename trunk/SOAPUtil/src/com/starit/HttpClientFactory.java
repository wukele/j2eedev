package com.starit;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

public class HttpClientFactory {
	private static final String HTTP_CONNECTION_MANAGER_BEAN = "httpConnectionManager";
	
	/**
	 * Factory method used by producers and consumers to create a new
	 * {@link HttpClient} instance
	 */
	public static HttpClient createHttpClient() {
		HttpClientParams clientParams = new HttpClientParams();
		HttpConnectionManagerParams managerParams = new HttpConnectionManagerParams();
		managerParams.setConnectionTimeout(1000); //设置连接超时时间
		managerParams.setDefaultMaxConnectionsPerHost(2);
		managerParams.setSoTimeout(1000); //设置读取数据超时时间
		HttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
		httpConnectionManager.setParams(managerParams);

		HttpClient answer = new HttpClient(clientParams);
		answer.setHttpConnectionManager(httpConnectionManager);

		return answer;
	}
}
