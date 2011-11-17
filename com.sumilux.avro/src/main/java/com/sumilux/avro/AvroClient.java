package com.sumilux.avro;

import org.apache.avro.Protocol;

import com.sumilux.avro.tools.AvroUtils;

public class AvroClient {

	/**
	 * @param args
	 */
	static Protocol protocol =AvroUtils.getProtocol();
	
	public static void main(String[] args) throws Exception {
//		args[0]="search";
		String opt ="update";
		ClientHandler  client= new ClientHandler();
		
		if (opt.equals("update")){
			client.update(protocol);
		}
		else if (opt.equals("search")) {
			client.search(protocol);
		}
		
	}

}
