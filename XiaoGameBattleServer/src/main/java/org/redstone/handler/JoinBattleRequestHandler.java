
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
import org.redstone.protobuf.msg.JoinBattleReply;
import org.redstone.protobuf.msg.LoginRequest;
import org.redstone.protobuf.util.DataUtils;
import org.redstone.protobuf.util.MsgType;


/**
    * @ClassName: LoginHandler
    * @Description: TODO 
    * @author zmxianlin
    * @date 2016年11月25日
    *
    */

public class JoinBattleRequestHandler extends BaseMsgHandler implements IMsgHandler{
	public ByteBuffer process(byte[] msgBody, String sessionId) {
		try {
			logger.info("玩家" + 1111 + "加入ChinaBattle Gomoku");
			
			Gamer gamer = new Gamer();
			gamer.setId("1111");
			BaseRoom room = ChinaBattleManage.getInstance().joinRoom(GameType.Gomoku, RoomType.Gomoku_Two, gamer);
			
			JoinBattleReply.Builder builder = JoinBattleReply.newBuilder();
			byte[] msgType = DataUtils.numberToBytes(MsgType.JoinBattleReply.getMsgType());
			byte[] reply = builder.build().toByteArray();
			ByteBuffer buff = ByteBuffer.allocate(msgType.length + reply.length);
			buff.put(msgType);
			buff.put(reply);
			
			return buff;
		} catch (Exception e) {
			logger.error("解析登录请求异常", e);
		}
		return null;
	}

	public ByteBuffer processSocket(Map<String, Object> msgBody) {
		try {
			logger.info("玩家" + 1111 + "加入ChinaBattle Gomoku");
			
			Gamer gamer = new Gamer();
			gamer.setId("1111");
			BaseRoom room = ChinaBattleManage.getInstance().joinRoom(GameType.Gomoku, RoomType.Gomoku_Two, gamer);
			
			byte[] msgType = DataUtils.numberToBytes(MsgType.JoinBattleReply.getMsgType());
			byte[] reply = room.getId().getBytes();
			ByteBuffer buff = ByteBuffer.allocate(msgType.length + reply.length);
			buff.put(msgType);
			buff.put(reply);
			
			return buff;
		} catch (Exception e) {
			logger.error("解析登录请求异常", e);
		}
		return null;
	}
	
	
}
