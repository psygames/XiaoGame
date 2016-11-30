/**  
 * @Title: ChinaBattleManage.java
 * @Package org.redstone.battle.battlemanage
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月27日
 * @version V1.0  
 */
package org.redstone.battle.battlemanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.redstone.battle.constant.Constant;
import org.redstone.battle.room.BaseRoom;
import org.redstone.battle.room.GomokuRoom;
import org.redstone.db.model.Gamer;
import org.redstone.protobuf.msg.Enums.Camp;

/**
 * @ClassName: ChinaBattleManage
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月27日
 *
 */
public class ChinaBattleManage implements IBatteManage{
	private static Map<String, Map<String, List<BaseRoom>>> gameMap = new ConcurrentHashMap<String, Map<String, List<BaseRoom>>>();
	private static Map<Integer, BaseRoom> allRoom = new ConcurrentHashMap<Integer, BaseRoom>();
	private Logger logger = Logger.getLogger(ChinaBattleManage.class);
	private static ChinaBattleManage chinaBattleManage;
	private static Integer roomId = 0;
	public static ChinaBattleManage getInstance(){
		if(chinaBattleManage == null){
			chinaBattleManage = new ChinaBattleManage();
		}
		return chinaBattleManage;
	}
	
	public  BaseRoom asignRoom(String gameType, String roomType, Gamer gamer) {
		Map<String, List<BaseRoom>> game;//游戏
		List<BaseRoom> roomList;
		BaseRoom room;//游戏房间
		if(!gameMap.containsKey(gameType)){
			game = new ConcurrentHashMap<String, List<BaseRoom>>();
			gameMap.put(gameType, game);
		}else{
			game = gameMap.get(gameType);
		}
		if(!game.containsKey(roomType)){
			roomList = new ArrayList<BaseRoom>();
			game.put(roomType, roomList);
			synchronized (roomId) {
				roomId ++;
				room = new GomokuRoom(roomId, gameType, roomType, 2);//TODO
				allRoom.put(roomId, room);
			}
			roomList.add(room);
			allRoom.put(room.getId(), room);
		}else{
			roomList = game.get(roomType);
			room = roomList.get(0);
		}
		room.addGamer(gamer);
		logger.info("玩家" + gamer.getId() + "，加入游戏类型" + gameType + "，房间类型=" + roomType + "，房间id=" + room.getId());
		return room;
	}
	
	public  Map<String, Object> joinRoom(Gamer gamer, int roomId) {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		int campId = 0;
		BaseRoom room = allRoom.get(roomId);
		if(room == null){
			logger.info("未找到roomId = " + roomId + "对应的房间");
			return null;
		}
		for(int i : room.getCampMap().keySet()){
			campId = i;
			Map<String, Gamer> campGamers = room.getCampMap().get(i);
			if(campGamers.containsKey(gamer.getId())){
				campGamers.get(gamer.getId()).setState(Constant.Gamer_State_Joined);
				break;
			}
		}
		logger.info("玩家" + gamer.getId() + "，加入游戏，房间id=" + room.getId());
		rtnMap.put("camp", campId == 0 ? Camp.BlackCamp : campId == 1 ? Camp.WhiteCamp : Camp.NoneCamp);
		return rtnMap;
	}

	@Override
	public byte[] process(byte[] reqData) {
		return null;
	}
}
