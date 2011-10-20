package com.sumilux.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class Client {
	public static void main(String[] args) {
		TTransport transport;
		try {
			transport = new TSocket("localhost", 8811);
			TProtocol protocol = new TCompactProtocol(transport);
			IHello.Client client = new IHello.Client(protocol);
			transport.open();
			
			for(int j=0; j<10; j++) {
				long start = System.currentTimeMillis();
				for(int i=0; i<10000; i++)
					client.queryUser(100L);
				System.out.println("total times:" + (System.currentTimeMillis() - start));
			}
			
			transport.close();
		} catch (TTransportException e) {
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		}
	}
}
