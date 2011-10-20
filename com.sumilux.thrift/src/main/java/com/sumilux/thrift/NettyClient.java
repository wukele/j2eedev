package com.sumilux.thrift;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import se.cgbystrom.netty.thrift.TNettyTransport;
import se.cgbystrom.netty.thrift.ThriftClientHandler;
import se.cgbystrom.netty.thrift.ThriftPipelineFactory;

public class NettyClient {
	public static void main(String[] args) {
		try {
			ChannelFactory factory1 = new NioClientSocketChannelFactory(
					Executors.newCachedThreadPool(),
					Executors.newCachedThreadPool(), 3);
			
			ClientBootstrap bootstrap = new ClientBootstrap(factory1);
			ThriftClientHandler handler = new ThriftClientHandler();
			ChannelPipelineFactory factory = new ThriftPipelineFactory(handler);
			bootstrap.setPipelineFactory(factory);
			Channel channel = bootstrap
					.connect(new InetSocketAddress("localhost", 8811))
					.awaitUninterruptibly().getChannel();
			TTransport transport = new TNettyTransport(channel, handler);
			TProtocol protocol = new TBinaryProtocol(transport);

			IHello.Client client = new IHello.Client(protocol);

			for (int j = 0; j < 10; j++) {
				long start = System.currentTimeMillis();
				for (int i = 0; i < 10000; i++)
					client.queryUser(100L);
				System.out.println("total times:"
						+ (System.currentTimeMillis() - start));
			}

			transport.close();
			channel.close().awaitUninterruptibly();
			bootstrap.releaseExternalResources();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
