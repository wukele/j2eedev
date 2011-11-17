package com.sumilux.avro.server;

import java.io.IOException;

import org.apache.avro.ipc.HttpServer;
import org.apache.avro.ipc.Responder;

import com.sumilux.avro.tools.AvroUtils;

public class AvroServer {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		try {
			HttpServer server = createServer(true);

			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static HttpServer createServer(boolean withPlugin)
			throws IOException {
		Responder r = new AvroFactory(AvroUtils.getProtocol());
		System.out.println(AvroUtils.getProtocol().toString());
		System.out.println(AvroUtils.getProtocol().toString().length());
		HttpServer server = new HttpServer(r, 9100);

		return server;
	}

}
