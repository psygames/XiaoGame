package org.redstone.server;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.redstone.handler.IMsgHandler;
import org.redstone.protobuf.util.DataUtils;
import org.redstone.protobuf.util.HandlerUtils;

@ServerEndpoint("/gameServer")
public class GameServer {
	
	Session session;
	static Map<String, String> sessionMap = new HashMap<String, String>();
	Basic remote;
	private final Logger logger = Logger.getLogger(GameServer.class);
	@OnOpen
	public void onOpen(Session s){
		session = s;
		remote = session.getBasicRemote();
		sessionMap.put(session.getId(), "");
		logger.info(session.getId() + "登入");
	}
	
	@OnClose
	public void onClose(){
		sessionMap.remove(session.getId());
		logger.info(session.getId() + "退出");
	}
	
	@OnMessage
	public void onMessage(String message){
		
	}
	
	@OnMessage
	public void receiveMessage(ByteBuffer reciveBuff) {
		byte[] orgi = reciveBuff.array();
		byte[] msgType = Arrays.copyOf(orgi, 2);
		byte[] msgBody = Arrays.copyOfRange(orgi, 2, orgi.length);
		short type = DataUtils.byteArray2T(msgType,Short.class);
		logger.info("msgType=" + type);
		IMsgHandler handler = HandlerUtils.getInstance().getHandler(type);
		ByteBuffer sendBuff = handler.process(msgBody, session.getId());
		sendBuff.flip();
		session.getAsyncRemote().sendBinary(sendBuff);
	}
}
