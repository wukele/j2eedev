package com.googlecode.protobuf.netty.example;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.googlecode.protobuf.netty.NettyRpcChannel;
import com.googlecode.protobuf.netty.NettyRpcClient;
import com.googlecode.protobuf.netty.example.Calculator.CalcRequest;
import com.googlecode.protobuf.netty.example.Calculator.CalcResponse;
import com.googlecode.protobuf.netty.example.Calculator.CalcService;
import com.googlecode.protobuf.netty.example.Calculator.CalcService.BlockingInterface;
import com.googlecode.protobuf.netty.example.Calculator.CalcService.Stub;

public class CalculatorClient {

	public static void main(String[] args) {

		NettyRpcClient client = new NettyRpcClient(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		NettyRpcChannel channel = client.blockingConnect(new InetSocketAddress(
				"localhost", 8080));

		Stub calcService = CalcService.newStub(channel);

		BlockingInterface blockingCalcService = CalcService
				.newBlockingStub(channel);

		// Get a new RpcController to use for this rpc call
		final RpcController controller = channel.newRpcController();

		// Create the request
		CalcRequest request = CalcRequest.newBuilder().setOp1(15).setOp2(35)
				.build();

		// Make the (asynchronous) RPC request
		calcService.add(controller, request, new RpcCallback<CalcResponse>() {
			public void run(CalcResponse response) {
				if (response != null) {
					System.out.println("The answer is: " + response.getResult());
				} else {
					System.out.println("Oops, there was an error: "
							+ controller.errorText());
				}
			}
		});

		// Do other important things now, while your RPC is hard at work
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			// Ignore
		}

		// Reset the controller
		controller.reset();

		// Now try a blocking RPC request
		try {
			CalcResponse response = blockingCalcService.multiply(controller,
					request);
			if (response != null) {
				System.out.println("The answer is: " + response.getResult());
			} else {
				System.out.println("Oops, there was an error: "
						+ controller.errorText());
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		controller.reset();

		// Make a bad request
		CalcRequest badRequest = CalcRequest.newBuilder().setOp1(20).setOp2(0)
				.build();

		// Let's see if we can trigger the exception
		try {
			blockingCalcService.divide(controller, badRequest);
			System.out.println("Should not be here");
		} catch (ServiceException e) {
			System.out.println("Good! Error is: " + e.getMessage());
		}

		controller.reset();

		// Asynchronous error
		calcService.divide(controller, badRequest,
				new RpcCallback<CalcResponse>() {
					public void run(CalcResponse response) {
						if (response != null) {
							System.out.println("Shouldn't happen");
						} else {
							System.out.println("Good! Error is: "
									+ controller.errorText());
						}
					}
				});

		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			// Ignore
		}

		// Close the channel
		channel.close();

		// Close the client
		client.shutdown();

		System.out.println("Done!");
	}

}
