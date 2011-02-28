package com.starit.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author  libinsong1204@gmail.com
 * @date    2011-1-18 上午11:03:34
 * @version 
 */
public class SamplMinaServerHandler extends IoHandlerAdapter{  
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	  
    private int count = 0;   
       
    /**  
     * 当一个客户端连接进入时  
     */   
    @Override   
    public void sessionOpened(IoSession session) throws Exception {   
    	logger.info("incoming client: " + session.getRemoteAddress());   
    }   
  
    /**  
     * 当一个客户端关闭时  
     */   
    @Override   
    public void sessionClosed(IoSession session) throws Exception {   
    	logger.info(session.getRemoteAddress() + " is Disconnection");   
    }   
  
    @Override   
    public void messageReceived(IoSession session, Object message)   
            throws Exception {   
        //我们已设定了服务器解析的规则是一行一行读取，这里就可以转为String:   
        String str = (String)message;   
        //Write the received data back to remote perr   
        logger.info("收到客户端发来的消息为 " + str);   
        //将测试消息会送给客户端   
        session.write("response " + str + " " + count);   
        ++count;   
    }   
}   