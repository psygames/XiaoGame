
    /**  
    * @Title: JoinBattleRequestHandler.java
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
import org.redstone.db.model.Gamer;
import org.redstone.protobuf.msg.AssignRoomRequest;
import org.redstone.protobuf.util.SessionUtils;

/**
    * @ClassName: JoinBattleRequestHandler
    * @Description: TODO 
    * @author zmxianlin
    * @date 2016年11月25日
    *
    */

public class AsignRoomRequestHandler extends BaseMsgHandler implements IMsgHandler{
	public ByteBuffer process(byte[] msgBody, String sessionId) {
		try {
			String deviceUID = SessionUtils.getDeviceUID(sessionId);
			logger.info("玩家" + deviceUID + "加入ChinaBattle Gomoku");
			
			AssignRoomRequest joinBattleRequest = AssignRoomRequest.parseFrom(msgBody);
			
			Gamer gamer = SessionUtils.getGamerBySession(sessionId);
			ChinaBattleManage.getInstance().asignRoom(GameType.Gomoku, RoomType.Gomoku_Two, gamer);
			return null;
		} catch (Exception e) {
			logger.error("请求处理异常", e);
		}
		return null;
	}

	public ByteBuffer processSocket(Map<String, Object> msgBody) {
		return null;
	}
	
	
}
