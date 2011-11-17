package com.sumilux.avro;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericData.Array;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.generic.GenericRequestor;
import org.apache.avro.util.Utf8;

public class ClientHandler {
	
	public void search (Protocol protocol) throws Exception {
		GenericRecord requestData= new GenericData.Record(protocol.getType("requestData")  );
		requestData.put("id", 1);
		requestData.put("name","javabloger" );
		
		System.out.println(protocol.getMessages());

		GenericRecord search= new GenericData.Record(protocol.getMessages().get("search").getRequest()  );
		search.put("query",requestData);
	 
		
		Transceiver t = new HttpTransceiver(new URL("http://192.168.1.96:9100"));
		GenericRequestor requestor = new GenericRequestor(protocol, t);
		 
		GenericRecord obj= (GenericRecord) requestor.request("search", search) ;
		
		Map<?, ?> map =(Map<?, ?>) obj.get("Map") ;
		Array<?> list =(Array<?>) obj.get("List") ;
	    Integer id=(Integer) obj.get("id") ;
	    Utf8 username=(Utf8)obj.get("username") ;
	    List<?> list2 =(List<?>) obj.get("List2") ;
		
 		System.out.println("map: "+map);
 		System.out.println("list: "+list);
 		System.out.println("list2: "+list2 );
 		System.out.println("id: "+id);
 		System.out.println("username: "+username);
 		
	}

	public void update (Protocol protocol) throws Exception {
		GenericRecord requestData= new GenericData.Record(protocol.getType("requestData")  );
		requestData.put("id", 1);
		requestData.put("name", "javabloger"  );
		
		
		GenericRecord update= new GenericData.Record(protocol.getMessages().get("update").getRequest()  );
		update.put("delete",requestData);
		
		Transceiver t = new HttpTransceiver(new URL("http://192.168.1.96:9100"));
		GenericRequestor requestor = new GenericRequestor(protocol, t);
		
		Object obj=   requestor.request("update", update) ;
		System.out.println(obj);
	}
	
}
