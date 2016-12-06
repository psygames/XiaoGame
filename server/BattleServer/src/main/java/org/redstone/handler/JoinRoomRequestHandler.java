
    /**  
    * @Title: JoinRoomRequestHandler.java
    * @Package org.redstone.handler
    * @Description: TODO 
    * @author zmxianlin
    * @date 2016年11月25日
    * @version V1.0  
    */
    
package org.redstone.handler;

import java.nio.ByteBuffer;
import java.util.Map;

import org.redstone.battle.battlemanage.ChinaBattleManage;
import org.redstone.battle.constant.GamerConstant;
import org.redstone.db.model.Gamer;
import org.redstone.protobuf.msg.Enums.Camp;
import org.redstone.protobuf.msg.BoardSync;
import org.redstone.protobuf.msg.JoinRoomReply;
import org.redstone.protobuf.msg.JoinRoomRequest;
import org.redstone.protobuf.util.DataUtils;
import org.redstone.protobuf.util.MsgType;
import org.redstone.protobuf.util.SessionUtils;


/**
    * @ClassName: JoinRoomRequestHandler
    * @Description: TODO 
    * @author zmxianlin
    * @date 2016年11月25日
    *
    */

public class JoinRoomRequestHandler extends BaseMsgHandler implements IMsgHandler{
	public ByteBuffer process(byte[] reqBody, String sessionId) {
		try {
			JoinRoomRequest joinRoomRequest = JoinRoomRequest.parseFrom(reqBody);
			Gamer gamer = new Gamer();
			gamer.setDeviceUID(joinRoomRequest.getDeviceUID());
			gamer.setState(GamerConstant.Gamer_State_Joining);
			
			SessionUtils.addSessionDevice(sessionId, joinRoomRequest.getDeviceUID());
			SessionUtils.addDeviceGamer(joinRoomRequest.getDeviceUID(), gamer);
			SessionUtils.addDeviceSession(joinRoomRequest.getDeviceUID(), sessionId);
			logger.info("设备" + joinRoomRequest.getDeviceUID() + "登录battleserver");
			
			
			Map<String, Object> rtnMap = ChinaBattleManage.getInstance().joinRoom(gamer, joinRoomRequest.getRoomId());
			if(rtnMap != null){
				JoinRoomReply.Builder builder = JoinRoomReply.newBuilder();
				builder.setRoomId(joinRoomRequest.getRoomId());
				builder.setCamp((Camp)rtnMap.get("camp"));
				builder.setBoardSync((BoardSync)rtnMap.get("boardSync"));
				
				byte[] msgType = DataUtils.number2Bytes(MsgType.JoinRoomReply.getMsgType());
				byte[] rspBody = builder.build().toByteArray();
				ByteBuffer buff = DataUtils.genBuff(msgType, rspBody);
				return buff;
			}
		} catch (Exception e) {
			logger.error("解析加入房間请求异常", e);
		}
		return null;
	}

	public ByteBuffer processSocket(Map<String, Object> reqMap) {
		return null;
	}
	
	
}
