/**  
 * @Title: PlaceRequestHandler.java
 * @Package org.redstone.handler
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年12月4日
 * @version V1.0  
 */
package org.redstone.handler;

import java.nio.ByteBuffer;
import java.util.Map;

import org.redstone.battle.battlemanage.ChinaBattleManage;
import org.redstone.battle.room.GomokuRoom;
import org.redstone.protobuf.msg.PlaceReply;
import org.redstone.protobuf.msg.PlaceRequest;
import org.redstone.protobuf.util.DataUtils;
import org.redstone.protobuf.util.MsgType;
import org.redstone.protobuf.util.SessionUtils;

/**
 * @ClassName: PlaceRequestHandler
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年12月4日
 *
 */
public class PlaceRequestHandler  extends BaseMsgHandler implements IMsgHandler{

	@Override
	public ByteBuffer process(byte[] msgBody, String sessionId) {
		try {
			GomokuRoom room = (GomokuRoom)ChinaBattleManage.sessionRoom.get(sessionId);
			
			PlaceRequest placeRequest = PlaceRequest.parseFrom(msgBody);
			
			int chessNum = placeRequest.getChessNum();
			
			room.putStatistics(SessionUtils.getDeviceUID(sessionId), chessNum);
			//room.addChess(chessNum, SessionUtils.getDevice(sessionId));
			
			PlaceReply.Builder builder = PlaceReply.newBuilder();
			builder.setResult(1);
			
			byte[] msgType = DataUtils.numberToBytes(MsgType.PlaceReply.getMsgType());
			byte[] rspBody = builder.build().toByteArray();
			ByteBuffer buff = DataUtils.genBuff(msgType, rspBody);
			return buff;
		} catch (Exception e) {
			logger.error("处理下棋异常", e);
		}
		return null;
	}

	@Override
	public ByteBuffer processSocket(Map<String, Object> msgBody) {
		return null;
	}

}
