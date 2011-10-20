package com.sumilux.thrift;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TCompactProtocol.Factory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

public class Server {
	public static void main(String[] args) {
		try {
			TServerSocket serverTransport = new TServerSocket(8811);
			IHello.Processor<HelloImpl> processor = new IHello.Processor<HelloImpl>(new HelloImpl());
			
			Factory portFactory = new TCompactProtocol.Factory();
			Args args1 = new Args(serverTransport);
			args1.processor(processor);
			args1.protocolFactory(portFactory);

			TServer server = new TThreadPoolServer(args1);
			server.serve();
			System.out.println("Starting server on port 8811 ...");
		} catch (TTransportException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
