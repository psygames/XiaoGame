
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

import org.redstone.battle.battlemanage.ChinaBattleManage;
import org.redstone.battle.constant.GameType;
import org.redstone.battle.constant.RoomType;
import org.redstone.battle.room.BaseRoom;
import org.redstone.db.model.Gamer;
import org.redstone.protobuf.msg.Enums.Camp;
import org.redstone.protobuf.msg.BoardSync;
import org.redstone.protobuf.msg.JoinRoomReply;
import org.redstone.protobuf.msg.JoinRoomRequest;
import org.redstone.protobuf.util.DataUtils;
import org.redstone.protobuf.util.MsgType;
import org.redstone.protobuf.util.SessionUtils;


/**
    * @ClassName: LoginHandler
    * @Description: TODO 
    * @author zmxianlin
    * @date 2016年11月25日
    *
    */

public class JoinRoomRequestHandler extends BaseMsgHandler implements IMsgHandler{
	public ByteBuffer process(byte[] reqBody, String sessionId) {
		try {
			JoinRoomRequest joinRoomRequest = JoinRoomRequest.parseFrom(reqBody);
			SessionUtils.addDeviceUID(sessionId, joinRoomRequest.getDeviceUID());
			logger.info("设备" + joinRoomRequest.getDeviceUID() + "登录");
			
			Gamer gamer = new Gamer();
			gamer.setId(joinRoomRequest.getDeviceUID());
			Map<String, Object> rtnMap = ChinaBattleManage.getInstance().joinRoom(gamer, joinRoomRequest.getRoomId());
			
			JoinRoomReply.Builder builder = JoinRoomReply.newBuilder();
			builder.setRoomId(joinRoomRequest.getRoomId());
			builder.setCamp((Camp)rtnMap.get("Camp"));
			builder.setBoardSync(BoardSync.newBuilder().build());
			
			byte[] msgType = DataUtils.numberToBytes(MsgType.JoinRoomReply.getMsgType());
			byte[] rspBody = builder.build().toByteArray();
			ByteBuffer buff = DataUtils.genBuff(msgType, rspBody);
			return buff;
		} catch (Exception e) {
			logger.error("解析登录请求异常", e);
		}
		return null;
	}

	public ByteBuffer processSocket(Map<String, Object> reqMap) {
		return null;
	}
	
	
}
