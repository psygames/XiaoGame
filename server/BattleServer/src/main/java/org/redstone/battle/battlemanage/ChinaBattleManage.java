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
import org.redstone.battle.constant.GamerConstant;
import org.redstone.battle.constant.RoomType;
import org.redstone.battle.room.BaseRoom;
import org.redstone.battle.room.GomokuRoom;
import org.redstone.db.model.Gamer;
import org.redstone.protobuf.msg.Enums.Camp;
import org.redstone.protobuf.util.SessionUtils;

/**
 * @ClassName: ChinaBattleManage
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月27日
 *
 */
public class ChinaBattleManage implements IBatteManage{
	private static Logger logger = Logger.getLogger(ChinaBattleManage.class);
	
	private static Map<String, Map<String, List<BaseRoom>>> gameMap = new ConcurrentHashMap<String, Map<String, List<BaseRoom>>>();
	private static Map<Integer, BaseRoom> allRoom = new ConcurrentHashMap<Integer, BaseRoom>();
	public static Map<String, BaseRoom> sessionRoom = new ConcurrentHashMap<String, BaseRoom>();
	private static Integer roomId = 0;
	
	private static ChinaBattleManage chinaBattleManage;
	public static ChinaBattleManage getInstance(){
		if(chinaBattleManage == null){
			chinaBattleManage = new ChinaBattleManage();
		}
		return chinaBattleManage;
	}
	
	public  BaseRoom asignRoomBySocket(String gameType, String roomType, List<Gamer> gamers) {
		return this.createRoom(gameType, roomType, gamers);
	}
	
	/**
	 * @Description: 创建房间方法为同步方法 ，防止同时多个请求导致房间创建过多
	 * @param  gameType
	 * @param  roomType
	 * @param  gamer     
	 * @return void 
	 * @throws
	 */
	synchronized public BaseRoom createRoom(String gameType, String roomType, List<Gamer> gamerList){
		Map<String, List<BaseRoom>> game;//游戏
		List<BaseRoom> roomList;
		BaseRoom room = null;//游戏房间
		if(!gameMap.containsKey(gameType)){
			game = new HashMap<String, List<BaseRoom>>();
			gameMap.put(gameType, game);
		}else {
			game = gameMap.get(gameType);
		}
		if(!game.containsKey(roomType)){
			roomList = new ArrayList<BaseRoom>();
			game.put(roomType, roomList);
			
			roomId ++;
			room = new BaseRoom(roomId, gameType, roomType, 2, 12, 12, 30*1000, 100);//TODO
			allRoom.put(roomId, room);
			roomList.add(room);
			room.addGamer(gamerList);
			room.timerCheckEnd();
		}
		return room;
	}
	
	
	public BaseRoom joinRoomBySocket(Gamer gamer, int roomId) {
		BaseRoom room = allRoom.get(roomId);
		if(room == null){
			logger.info("未找到roomId = " + roomId + "对应的房间");
			return null;
		}
		if(!room.addGamer(gamer)){
			logger.info("玩家" + gamer.getDeviceUID()+ "，加入游戏，房间id=" + room.getId() + "，加入失败");
			return null;
		}
		logger.info("玩家" + gamer.getDeviceUID()+ "，加入游戏，房间id=" + room.getId() + "，加入成功");
		return room;
	}
	
	public Map<String, Object> joinRoom(Gamer gamer, int roomId) {
		BaseRoom room = allRoom.get(roomId);
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		if(room == null){
			logger.info("未找到roomId = " + roomId + "对应的房间");
			return null;
		}
		if(!room.addGamer(gamer)){
			logger.info("玩家" + gamer.getDeviceUID()+ "，加入游戏，房间id=" + room.getId() + "，加入失败");
			return null;
		}
		int camp = room.getGamersCamp().get(gamer.getDeviceUID());
		rtnMap.put("camp", Camp.valueOf(camp));
		rtnMap.put("roomId", room.getId());
		rtnMap.put("boardSync", room.genBoardSync());
		logger.info("玩家" + gamer.getDeviceUID()+ "，加入游戏，房间id=" + room.getId() + "，阵营" + Camp.valueOf(camp)+ "加入成功");
		return rtnMap;
	}
	
	public BaseRoom getRoom(String sessionId){
		Gamer gamer = SessionUtils.getGamerBySession(sessionId);
		BaseRoom room = sessionRoom.get(gamer.getDeviceUID());
		return room;
	}
	
	public static void remove(String sessionId){
		BaseRoom room = sessionRoom.get(sessionId);
		if(room != null){
			room.remove(SessionUtils.getDeviceUID(sessionId));
			if(room.getGamerCount() <= 0){
				if(allRoom.containsKey(room.getId())){
					allRoom.remove(room.getId());
				}
				if(gameMap.containsKey(room.getGameType())){
					Map<String, List<BaseRoom>> roomMaps = gameMap.get(room.getGameType());
					if(roomMaps.containsKey(room.getRoomType())){
						List<BaseRoom> list = roomMaps.get(room.getRoomType());
						if(list.contains(room)){
							list.remove(room);
						}
					}
				}
			}
		}
	}
	

	@Override
	public byte[] process(byte[] reqData) {
		return null;
	}
}
