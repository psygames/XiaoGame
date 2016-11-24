package org.redstone.server;

import java.nio.ByteBuffer;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.redstone.handler.IMsgHandler;
import org.redstone.protobuf.util.HandlerUtils;

@ServerEndpoint("/test")
public class TestServlet {
	
	Session session;
	Basic remote;
	private final Logger logger = Logger.getLogger(TestServlet.class);
	@OnOpen
	public void onOpen(Session s){
		session = s;
		remote = session.getBasicRemote();
		logger.info(session.getId() + "登入");
	}
	
	@OnClose
	public void onClose(){
		logger.info(session.getId() + "退出");
	}
	
	@OnMessage
	public void onMessage(String message){
		
	}
	
	@OnMessage
	public void receiveMessage(ByteBuffer reciveBuff) {
		byte[] msgType = new byte[2];
		reciveBuff.get(msgType, 0, 2);
		byte[] msgBody = new byte[reciveBuff.position()-2];
		reciveBuff.get(msgBody, 2, reciveBuff.position());
		IMsgHandler handler = HandlerUtils.getInstance().getHandler(byteArrayToInt(msgType));
		handler.process(msgBody);
	}
	
	public static int byteArrayToInt(byte[] bytes) {  
	    int value = 0;  
	    // 由高位到低位  
	    for (int i = 0; i < bytes.length; i++) {  
	        int shift = (bytes.length - 1 - i) * 8;  
	        value += (bytes[i] & 0xFF) << shift;// 往高位游  
	    }  
	    return value;  
	}
}
