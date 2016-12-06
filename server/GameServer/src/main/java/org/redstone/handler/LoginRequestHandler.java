
    /**  
    * @Title: LoginHandler.java
    * @Package org.redstone.handler
    * @Description: TODO 
    * @author zmxianlin
    * @date 2016年11月25日
    * @version V1.0  
    */
    
package org.redstone.handler;

import java.nio.ByteBuffer;
import java.util.Map;

import org.redstone.battle.constant.GamerConstant;
import org.redstone.db.model.Gamer;
import org.redstone.protobuf.msg.LoginReply;
import org.redstone.protobuf.msg.LoginRequest;
import org.redstone.protobuf.util.DataUtils;
import org.redstone.protobuf.util.MsgType;
import org.redstone.protobuf.util.SessionUtils;

import com.google.protobuf.InvalidProtocolBufferException;

/**
    * @ClassName: LoginHandler
    * @Description: TODO 
    * @author zmxianlin
    * @date 2016年11月25日
    *
    */

public class LoginRequestHandler extends BaseMsgHandler implements IMsgHandler{
	public ByteBuffer process(byte[] msgBody, String sessionId) {
		try {
			LoginRequest bean = LoginRequest.parseFrom(msgBody);
			logger.info("设备" + bean.getDeviceUID() + "登录");
			SessionUtils.addSessionDevice(sessionId, bean.getDeviceUID());
			Gamer gamer = new Gamer();
			gamer.setId(bean.getDeviceUID());
			gamer.setDeviceUID(bean.getDeviceUID());
			gamer.setState(GamerConstant.Gamer_State_LogIn);
			SessionUtils.addDeviceGamer(bean.getDeviceUID(), gamer);
			SessionUtils.addDeviceSession(bean.getDeviceUID(), sessionId);
			
			LoginReply.Builder builder = LoginReply.newBuilder();
			builder.setName("傻吊你好");
			builder.setLevel(1);
			
			byte[] msgType = DataUtils.number2Bytes(MsgType.LoginReply.getMsgType());
			byte[] reply = builder.build().toByteArray();
			ByteBuffer buff = ByteBuffer.allocate(msgType.length + reply.length);
			buff.put(msgType);
			buff.put(reply);
			
			return buff;
		} catch (InvalidProtocolBufferException e) {
			logger.error("解析登录请求异常", e);
		}
		return null;
	}

	public ByteBuffer processSocket(Map<String, Object> msgBody) {
		return null;
	}
}
