package com.googlecode.protobuf.netty.example;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.googlecode.protobuf.netty.NettyRpcServer;
import com.googlecode.protobuf.netty.example.Calculator.CalcService;

public class CalculatorServer {

	public static void main(String[] args) {

		NettyRpcServer server = new NettyRpcServer(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		server.registerService(CalcService
				.newReflectiveService(new CalculatorServiceImpl()));

		server.registerBlockingService(CalcService
				.newReflectiveBlockingService(new CalculatorServiceImpl()));

		server.serve(new InetSocketAddress(8080));

	}

}
