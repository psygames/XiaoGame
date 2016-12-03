
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
import java.util.HashMap;
import java.util.Map;

import org.redstone.battle.constant.GameType;
import org.redstone.battle.constant.RoomType;
import org.redstone.protobuf.msg.AssignRoomReply;
import org.redstone.protobuf.msg.AssignRoomRequest;
import org.redstone.protobuf.util.DataUtils;
import org.redstone.protobuf.util.MsgType;
import org.redstone.protobuf.util.SessionUtils;
import org.redstone.protobuf.util.SocketUtils;

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
			String deviceUID = SessionUtils.getDevice(sessionId);
			logger.info("玩家" + deviceUID + "加入ChinaBattle Gomoku");
			
			AssignRoomRequest joinBattleRequest = AssignRoomRequest.parseFrom(msgBody);
			
			
			//请求battleserver
			Map<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("msgType", MsgType.AsignRoomRequest);
			reqMap.put("gamer", DataUtils.toGson(SessionUtils.getGamerBySession(sessionId)));
			reqMap.put("sessionId", sessionId);
			reqMap.put("gameType", GameType.Gomoku);
			reqMap.put("roomType", RoomType.Gomoku_Two);
			String socketReq = DataUtils.toGson(reqMap);
			String socketRsp = null;
			try {
				byte[] reqHeadBytes = DataUtils.numberToBytes((short)socketReq.length());
				byte[] reqBodyBytes = socketReq.getBytes("utf-8");
				ByteBuffer reqBuff = DataUtils.genBuff(reqHeadBytes, reqBodyBytes);
				socketRsp = SocketUtils.sendMsg(reqBuff.array());
			} catch (Exception e) {
				logger.error("socket异常", e);
				return null;
			}
			
			
			AssignRoomReply.Builder builder = AssignRoomReply.newBuilder();
			builder.setAddress("http://192.168.10.106:8180/BattleServer/battleServer");
			builder.setRoomId(Integer.parseInt(socketRsp));
			
			byte[] msgType = DataUtils.numberToBytes(MsgType.AsignRoomReply.getMsgType());
			byte[] reply = builder.build().toByteArray();
			ByteBuffer buff = DataUtils.genBuff(msgType, reply);
			
			return buff;
		} catch (Exception e) {
			logger.error("请求处理异常", e);
		}
		return null;
	}

	public ByteBuffer processSocket(Map<String, Object> msgBody) {
		return null;
	}
	
	
}
