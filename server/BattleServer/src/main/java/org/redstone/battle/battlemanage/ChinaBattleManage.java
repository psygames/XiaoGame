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
	private static Map<String, Map<String, List<Gamer>>> tmpGamerMap = new ConcurrentHashMap<String, Map<String, List<Gamer>>>();
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
	
	public  BaseRoom asignRoom(String gameType, String roomType, Gamer gamer) {
		Map<String, List<BaseRoom>> game;//游戏
		List<BaseRoom> roomList;
		BaseRoom room = null;//游戏房间
		if(gameMap.containsKey(gameType)){
			game = gameMap.get(gameType);
			if(game.containsKey(roomType)){
				//房间已存在，并且未达到人数上线
				roomList = game.get(roomType);
				for(BaseRoom rm : roomList){
					if(rm.getUpperLimit() > rm.getGamerCount()){
						if(rm.addGamer(gamer)){
							room = rm;
							break;
						}
					}
				}
			}
		}
		if(room == null){
			logger.info("玩家" + gamer.getDeviceUID() + "，加入游戏类型" + gameType + "，房间类型=" + roomType + " 失败，放入临时列表");
			//没有加入房间，放入临时列表
			this.put2TmpMap(gameType, roomType, gamer);
			//检测临时列表，如果某个房间类型的请求人数达到2，创建房间
			this.checkTmpMap();
		}
		return room;
	}
	
	/**
	 * @Description: 创建房间方法为同步方法 ，防止同时多个请求导致房间创建过多
	 * @param  gameType
	 * @param  roomType
	 * @param  gamer     
	 * @return void 
	 * @throws
	 */
	synchronized public void createRoom(String gameType, String roomType, List<Gamer> gamerList){
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
			if(roomType.equals(RoomType.Gomoku_Two)){
				room = new GomokuRoom(roomId, gameType, roomType, 2, 10, 30*1000, 12, 12);//TODO
			}
			allRoom.put(roomId, room);
			roomList.add(room);
			room.addGamer(gamerList);
			room.timerCheckEnd();
		}
	}
	
	public void put2TmpMap(String gameType, String roomType, Gamer gamer){
		Map<String, List<Gamer>> rooms = tmpGamerMap.get(gameType);
		if(rooms == null){
			rooms = new HashMap<String, List<Gamer>>();
			rooms.put(roomType, new ArrayList<Gamer>(){
				private static final long serialVersionUID = 1L;
				{this.add(gamer);}});
			tmpGamerMap.put(gameType, rooms);
		}else{
			List<Gamer> gamerList = rooms.get(roomType);
			if(gamerList == null){
				gamerList = new ArrayList<Gamer>();
			}
			if(!gamerList.contains(gamer)){
				gamerList.add(gamer);
			}
		}
	}
	
	/**
	 * @Description: 检测临时列表，如果某个房间类型的请求玩家数达到2个，创建房间 
	 * @param      
	 * @return void 
	 * @throws
	 */
	public void checkTmpMap(){
		for(String gameType : tmpGamerMap.keySet()){
			Map<String, List<Gamer>> roomsMap = tmpGamerMap.get(gameType);
			for(String roomType : roomsMap.keySet()){
				if(roomsMap.get(roomType).size() >= 2){
					this.createRoom(gameType, roomType, roomsMap.get(roomType));
				}
			}
		}
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
			if(campGamers.containsKey(gamer.getDeviceUID())){
				campGamers.get(gamer.getDeviceUID()).setState(GamerConstant.Gamer_State_Joined);
				break;
			}
		}
		logger.info("玩家" + gamer.getDeviceUID()+ "，加入游戏，房间id=" + room.getId());
		rtnMap.put("camp", campId == 0 ? Camp.BlackCamp : campId == 1 ? Camp.WhiteCamp : Camp.NoneCamp);
		return rtnMap;
	}
	
	public BaseRoom getRoom(String sessionId){
		Gamer gamer = SessionUtils.getGamerBySession(sessionId);
		BaseRoom room = sessionRoom.get(gamer.getDeviceUID());
		return room;
	}
	
	public void remove(String sessionId){
		BaseRoom room = sessionRoom.get(sessionId);
		if(room != null){
			room.remove(SessionUtils.getDeviceUID(sessionId));
		}
	}
	

	@Override
	public byte[] process(byte[] reqData) {
		return null;
	}
}
