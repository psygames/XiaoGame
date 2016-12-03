
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
import org.redstone.battle.constant.GamerConstant;
import org.redstone.battle.constant.GameType;
import org.redstone.battle.constant.RoomType;
import org.redstone.battle.room.BaseRoom;
import org.redstone.db.model.Gamer;
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

public class AsignRoomRequestHandler extends BaseMsgHandler implements IMsgHandler{
	public ByteBuffer process(byte[] msgBody, String sessionId) {
		return null;
	}

	public ByteBuffer processSocket(Map<String, Object> reqMap) {
		try {
			
			String jsonGamer = reqMap.get("jsonGamer").toString();
			String gameType = reqMap.get("gameType").toString();
			String roomType = reqMap.get("roomType").toString();
			
			Gamer gamer = DataUtils.json2T(jsonGamer, Gamer.class);
			gamer.setState(GamerConstant.Gamer_State_Joining);
			BaseRoom room = ChinaBattleManage.getInstance().asignRoom(gameType, roomType, gamer);
			logger.info("玩家" + gamer.getDeviceUID() + "加入ChinaBattle Gomoku");
			
			byte[] msgType = DataUtils.numberToBytes(MsgType.JoinRoomReply.getMsgType());
			byte[] reply = DataUtils.numberToBytes(room.getId());
			ByteBuffer buff = DataUtils.genBuff(msgType, reply);
			
			return buff;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
	
	
}
