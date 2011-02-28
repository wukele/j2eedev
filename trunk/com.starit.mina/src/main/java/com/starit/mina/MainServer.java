package com.starit.mina;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author  libinsong1204@gmail.com
 * @date    2011-1-18 上午11:03:34
 * @version 
 */
public class MainServer {
	private static final Logger logger = LoggerFactory.getLogger(MainServer.class);
	  
    public static void main(String []args)throws Exception{   
        //创建一个非阻塞的Server端Socket，用NIO   
        SocketAcceptor acceptor = new NioSocketAcceptor();   
        //创建接受数据的过滤器   
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();   
       
        //设定这个过滤器将一行一行（/r/n）的读取数据   
        chain.addLast("myChain", new ProtocolCodecFilter(new TextLineCodecFactory()));   
        //设定服务器端的消息处理器:一个SamplMinaServerHandler对象   
        acceptor.setHandler(new SamplMinaServerHandler());   
           
        //服务器端绑定的端口   
        int bindPort = 8189;
        //绑定端口，启动服务器   
        try {   
            acceptor.bind(new InetSocketAddress(bindPort));   
        } catch (IOException e) {   
            logger.error("", e);
        }   
        logger.info("Mina Server is Listing on:=" + bindPort);   
    }   
} 
