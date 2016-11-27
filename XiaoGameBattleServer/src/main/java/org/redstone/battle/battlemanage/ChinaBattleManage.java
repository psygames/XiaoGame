/**  
 * @Title: ChinaBattleManage.java
 * @Package org.redstone.battle.battlemanage
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月27日
 * @version V1.0  
 */
package org.redstone.battle.battlemanage;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.redstone.battle.room.BaseRoom;
import org.redstone.battle.room.GomokuRoom;
import org.redstone.battle.socket.ClientRunnable;
import org.redstone.db.model.Gamer;
import org.redstone.protobuf.util.DataUtils;

/**
 * @ClassName: ChinaBattleManage
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月27日
 *
 */
public class ChinaBattleManage implements IBatteManage{
	private static Map<String, Map<String, BaseRoom>> gameMap = new HashMap<String, Map<String, BaseRoom>>();
	private Logger logger = Logger.getLogger(ChinaBattleManage.class);
	private static ChinaBattleManage chinaBattleManage;
	private static volatile short roomId = 0;
	public static ChinaBattleManage getInstance(){
		if(chinaBattleManage == null){
			chinaBattleManage = new ChinaBattleManage();
		}
		return chinaBattleManage;
	}
	
	public  BaseRoom joinRoom(String gameType, String roomType, Gamer gamer) {
		Map<String, BaseRoom> game;//游戏
		BaseRoom room;//游戏房间
		if(!gameMap.containsKey(gameType)){
			game = new HashMap<String, BaseRoom>();
			gameMap.put(gameType, game);
		}else{
			game = gameMap.get(gameType);
		}
		if(!game.containsKey(roomType)){
			room = new GomokuRoom(gameType, roomType, 2);//TODO
			game.put(roomType, room);
		}else{
			room = game.get(roomType);
		}
		room.addGamer(gamer);
		return room;
	}

	public byte[] process(byte[] orgi) {
		byte[] msgType = Arrays.copyOf(orgi, 2);
		byte[] msgBody = Arrays.copyOfRange(orgi, 2, orgi.length);
		short type = DataUtils.byteArray2T(msgType,Short.class);
		logger.info("msgType=" + type);
		
		
		return new byte[0];
	}
}
