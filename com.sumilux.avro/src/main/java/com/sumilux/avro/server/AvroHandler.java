package com.sumilux.avro.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.AvroRemoteException;
import org.apache.avro.Protocol.Message;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

public class AvroHandler {

	public Object updateRespond(Message message, Object request)
			throws AvroRemoteException {
		GenericRecord req = (GenericRecord) request;
		System.out.println("from client request data : " + req.get("delete"));

		GenericRecord responseUpdate = new GenericData.Record(
				message.getResponse());
		responseUpdate.put("status", true);
		responseUpdate.put("number", 10.10);

		GenericRecord update = new GenericData.Record(message.getRequest());
		update.put("delete", responseUpdate);

		return update.get("delete");
	}

	public Object searchRespond(Message message, Object request)
			throws AvroRemoteException {
		GenericRecord req = (GenericRecord) request;
		System.out.println("from client request data : " + req.get("query"));
		System.out.println("from client request message : " + message);

		Schema msgResp = message.getResponse();

		Map<String, String> map = new HashMap<String, String>();
		map.put("k1", "v1");
		map.put("k2", "v2");

		List<String> list = new ArrayList<String>();
		list.add("list1");
		list.add("list2");
		list.add("list4");
		list.add("list3");
		list.add("list6");
		list.add("list5");

		List<String> list2 = new ArrayList<String>();
		list2.add("111");
		list2.add("222");

		GenericRecord responseData = new GenericData.Record(msgResp);
		responseData.put("id", 1);
		responseData.put("username", "http://www.javabloger.com");
		responseData.put("List", list);
		responseData.put("List2", list2);
		responseData.put("Map", map);

		GenericRecord search = new GenericData.Record(message.getRequest());
		search.put("query", responseData);

		return search.get("query");

	}

}
