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
import java.util.Map.Entry;
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
	
	private static Map<String, Map<String, List<Integer>>> gameMap = new ConcurrentHashMap<String, Map<String, List<Integer>>>();
	private static Map<Integer, BaseRoom> allRooms = new ConcurrentHashMap<Integer, BaseRoom>();
	private static Map<String, Integer> deviceRooms = new ConcurrentHashMap<String, Integer>();
	private static Integer roomId = 0;
	
	
	public static BaseRoom getRoomById(Integer roomId){
		return allRooms.get(roomId);
	}
	
	public  BaseRoom getRoomBySessionId(String sessionId){
		String deviceUID = SessionUtils.getDeviceUID(sessionId);
		Integer roomId = deviceRooms.get(deviceUID);
		BaseRoom room = allRooms.get(roomId);
		return room;
	}
	
	public static void put2DeviceRooms(String deviceUID, Integer roomId){
		deviceRooms.put(deviceUID, roomId);
	}
	
	
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
		logger.info("battleserver开始创建房间，gameType=" + gameType + "，roomType=" + roomType + ",gamerList=" + gamerList.size());
		Map<String, List<Integer>> game;//游戏
		List<Integer> roomList;
		BaseRoom room = null;//游戏房间
		if(!gameMap.containsKey(gameType)){
			game = new HashMap<String, List<Integer>>();
			gameMap.put(gameType, game);
			logger.info("createRoom:战场中不存在gameType=" + gameType + ",创建");
		}else {
			game = gameMap.get(gameType);
		}
		if(!game.containsKey(roomType)){
			roomList = new ArrayList<Integer>();
			game.put(roomType, roomList);
			logger.info("createRoom:gameType=" + gameType + ",不存在roomType=" + roomType + ",创建");
		}else{
			roomList = game.get(roomType);
		}
		
		roomId ++;
		room = new BaseRoom(roomId, gameType, roomType, 2, 12, 12, 30*1000, 100);//TODO
		roomList.add(room.getId());
		room.addGamer(gamerList);
		room.startTimer();
		allRooms.put(roomId, room);
		logger.info("createRoom:创建房间成功,room=" + room.toString());
		return room;
	}
	
	
	synchronized public BaseRoom joinRoomBySocket(Gamer gamer, int roomId) {
		BaseRoom room = allRooms.get(roomId);
		if(room == null){
			logger.info("joinRoomBySocket:未找到roomId = " + roomId + "对应的房间");
			return null;
		}
		if(!room.addGamer(gamer)){
			logger.info("joinRoomBySocket:玩家" + gamer.getDeviceUID()+ "，房间id=" + room.getId() + "，加入失败");
			return null;
		}
		logger.info("joinRoomBySocket:玩家" + gamer.getDeviceUID()+ "，房间id=" + room.getId() + "，加入成功");
		
		allRooms.put(roomId, room);
		return room;
	}
	
	public Map<String, Object> joinRoom(Gamer gamer, int roomId) {
		BaseRoom room = allRooms.get(roomId);
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		if(room == null){
			logger.info("未找到roomId = " + roomId + "对应的房间");
			return null;
		}
		if(!room.addGamerByClient(gamer)){
			logger.info("玩家" + gamer.getDeviceUID()+ "，加入游戏，房间id=" + room.getId() + "，加入失败");
			return null;
		}
		Camp camp = room.getGamersCamp().get(gamer.getDeviceUID());
		rtnMap.put("camp", camp);
		rtnMap.put("roomId", room.getId());
		rtnMap.put("newTurn", room.genNewTurnBroadcastBuff());
		logger.info("玩家" + gamer.getDeviceUID()+ "，加入游戏，房间id=" + room.getId() + "，阵营" + camp + "加入成功");
		allRooms.put(roomId, room);
		
		room.gameStartNotifyGamer(gamer);//开始游戏通知所有人
		return rtnMap;
	}
	
	/**
	 * @Description: 从战场中清空sessionId对应的玩家
	 * @param @param sessionId     
	 * @return void 
	 * @throws
	 */
	public  void remove(String sessionId){
		BaseRoom room = getRoomBySessionId(sessionId);
		if(room != null){
			room.remove(SessionUtils.getDeviceUID(sessionId));
			if(room.getGamerCount() <= 0){
				if(allRooms.containsKey(room.getId())){
					allRooms.remove(room.getId());
				}
				
				if(gameMap.containsKey(room.getGameType())){
					Map<String, List<Integer>> roomMaps = gameMap.get(room.getGameType());
					if(roomMaps.containsKey(room.getRoomType())){
						List<Integer> list = roomMaps.get(room.getRoomType());
						if(list.contains(room)){
							list.remove(room);
							roomMaps.put(room.getRoomType(), list);
							gameMap.put(room.getGameType(), roomMaps);
						}
					}
				}
				
				if(deviceRooms.containsValue(room.getId())){
					for(Entry<String, Integer> entry : deviceRooms.entrySet()){
						if(entry.getValue().intValue() == room.getId().intValue()){
							deviceRooms.remove(entry.getKey());
							break;
						}
					}
				}
				room = null;
			}
		}
	}
	
	public static void syncRoom(BaseRoom room){
		allRooms.put(room.getId(), room);
	}

	@Override
	public byte[] process(byte[] reqData) {
		return null;
	}
}
