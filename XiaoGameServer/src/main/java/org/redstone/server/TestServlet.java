package org.redstone.server;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.redstone.protobuf.msg.CardProto.Card;

import com.google.protobuf.InvalidProtocolBufferException;

@ServerEndpoint("/test")
public class TestServlet {
	
	Session session;
	Basic remote;
	Async async;
	@OnOpen
	public void onOpen(Session s){
		session = s;
		remote = session.getBasicRemote();
		System.out.println(session.getId() + "登入");
	}
	
	@OnClose
	public void onClose(){
		System.out.println(session.getId() + "退出");
	}
	
	@OnMessage
	public void onMessage(String message){
		
	}
	
	@OnMessage
	public void receiveMessage(ByteBuffer reciveBuff) {
		Card c = null;
		try {
			c = Card.parseFrom(reciveBuff.array());
			System.out.println(c.getColor());
			System.out.println(c.getMemo());
			System.out.println(c.getNum());
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		
		ByteBuffer sendBuffer = ByteBuffer.wrap(c.toByteArray());
		try {
			session.getBasicRemote().sendBinary(reciveBuff);
			session.getBasicRemote().sendBinary(sendBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
