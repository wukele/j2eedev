package com.sumilux.avro.server;

import org.apache.avro.Protocol;
import org.apache.avro.Protocol.Message;
import org.apache.avro.ipc.generic.GenericResponder;

public class AvroFactory extends GenericResponder {

	static Protocol protocol = null;

	public AvroFactory(Protocol protocol) {
		super(protocol);
		AvroFactory.protocol = protocol;
	}

	public Object respond(Message message, Object request) throws Exception {
		System.out
				.println("from client request message : " + message.getName());

		if (message.getName().equals("search")) {
			return new AvroHandler().searchRespond(message, request);
			// search
		}

		else if (message.getName().equals("update")) {
			return new AvroHandler().updateRespond(message, request);
			// update
		}

		return request;

	}

}
