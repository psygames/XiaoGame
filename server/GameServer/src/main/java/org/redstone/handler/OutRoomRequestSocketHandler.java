/**  
 * @Title: OutRoomRequestSocketHandler.java
 * @Package org.redstone.handler
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年12月11日
 * @version V1.0  
 */
package org.redstone.handler;

import java.nio.ByteBuffer;
import java.util.Map;

import org.redstone.battle.battlemanage.ChinaBattleManage;

/**
 * @ClassName: OutRoomRequestSocketHandler
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年12月11日
 *
 */
public class OutRoomRequestSocketHandler  extends BaseMsgHandler implements IMsgHandler{

	@Override
	public ByteBuffer process(byte[] msgBody, String sessionId) {
		return null;
	}

	@Override
	public ByteBuffer processSocket(Map<String, Object> msgBody) {
		Integer roomId = Integer.parseInt(msgBody.get("roomId").toString());
		String deviceUIDs = msgBody.get("deviceUIDs").toString();
		ChinaBattleManage.getInstance().removeBySocket(roomId, deviceUIDs);
		return null;
	}
	
}
