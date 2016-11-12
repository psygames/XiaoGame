package test.websocket.servlet;

import java.nio.ByteBuffer;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.protobuf.InvalidProtocolBufferException;

import test.websocket.protobuf.msg.CardProto.Card;

@ServerEndpoint("/test")
public class TestServlet {
	
	Session session;
	Async remote;
	@OnOpen
	public void onOpen(Session s){
		session = s;
		remote = session.getAsyncRemote();
		System.out.println(session.getId() + "½ÓÈë");
	}
	
	@OnClose
	public void onClose(){
		System.out.println(session.getId() + "¶Ï¿ª");
	}
	
	@OnMessage
	public void onMessage(String message){
		
	}
	
	@OnMessage
	public void receiveMessage(ByteBuffer reciveBuff) {
		
		try {
			Card c = Card.parseFrom(reciveBuff.array());
			System.out.println(c.getColor());
			System.out.println(c.getMemo());
			System.out.println(c.getNum());
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		
		session.getAsyncRemote().sendBinary(reciveBuff);
	}
}
