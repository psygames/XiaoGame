
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.redstone.battle.battlemanage.ChinaBattleManage;
import org.redstone.battle.constant.GamerConstant;
import org.redstone.battle.room.BaseRoom;
import org.redstone.db.model.Gamer;
import org.redstone.protobuf.util.DataUtils;

import com.google.gson.reflect.TypeToken;


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
			
			String jsonGamers = reqMap.get("gamers").toString();
			String gameType = reqMap.get("gameType").toString();
			String roomType = reqMap.get("roomType").toString();
			String operType = reqMap.get("operType").toString();
			
			List<Gamer> gamers = (List<Gamer>) DataUtils.jsonToType(jsonGamers, new TypeToken<ArrayList<Gamer>>(){}.getType());
			for(Gamer gamer : gamers){
				gamer.setState(GamerConstant.Gamer_State_Joining);
				logger.info("玩家" + gamer.getDeviceUID() + "加入ChinaBattle Gomoku");
			}
			
			BaseRoom room = null;
			Integer roomId = null;
			if(GamerConstant.Room_Join.equals(operType)){
				roomId = Integer.parseInt(reqMap.get("roomId").toString());
				room = ChinaBattleManage.getInstance().joinRoomBySocket(gamers.get(0), roomId);
			}else{
				room = ChinaBattleManage.getInstance().asignRoomBySocket(gameType, roomType, gamers);
				roomId = room.getId();
			}
			
			if(room != null){
				byte[] reply = DataUtils.number2Bytes(roomId);
				byte[] length = DataUtils.number2Bytes(new Integer(reply.length).shortValue());
				ByteBuffer buff = DataUtils.genBuff(length, reply);
				logger.info("加入房间成功，返回gameserver，报文内容 " + reply.length + "," + roomId);
				return buff;
			}else{
				byte[] reply = new byte[0];
				byte[] length = DataUtils.number2Bytes(new Integer(reply.length).shortValue());
				ByteBuffer buff = DataUtils.genBuff(length, reply);
				logger.info("加入房间失败，返回gameserver，报文内容 " + reply.length);
				return buff;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
	
}
