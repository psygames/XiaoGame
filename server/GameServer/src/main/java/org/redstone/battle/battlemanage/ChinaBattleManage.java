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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.redstone.battle.constant.GameType;
import org.redstone.battle.constant.GamerConstant;
import org.redstone.battle.constant.RoomType;
import org.redstone.battle.room.BaseRoom;
import org.redstone.battle.room.GomokuRoom;
import org.redstone.db.model.Gamer;
import org.redstone.protobuf.msg.Enums.Camp;
import org.redstone.protobuf.util.DataUtils;
import org.redstone.protobuf.util.MsgType;
import org.redstone.protobuf.util.SessionUtils;
import org.redstone.protobuf.util.SocketUtils;

/**
 * @ClassName: ChinaBattleManage
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月27日
 *
 */
public class ChinaBattleManage implements IBatteManage{
	private static Logger logger = Logger.getLogger(ChinaBattleManage.class);
	private static String Default_Charset = "utf-8";
	
	private static Map<String, Map<String, List<Integer>>> gameMap = new ConcurrentHashMap<String, Map<String, List<Integer>>>();
	private static Map<String, Map<String, List<Gamer>>> tmpGamerMap = new ConcurrentHashMap<String, Map<String, List<Gamer>>>();
	private static Map<Integer, BaseRoom> allRooms = new ConcurrentHashMap<Integer, BaseRoom>();
	public static Map<String, Integer> deviceRooms = new ConcurrentHashMap<String, Integer>();
	
	private static ChinaBattleManage chinaBattleManage;
	public static ChinaBattleManage getInstance(){
		if(chinaBattleManage == null){
			chinaBattleManage = new ChinaBattleManage();
		}
		return chinaBattleManage;
	}
	
	public void asignRoom(String gameType, String roomType, Gamer gamer) {
		Map<String, List<Integer>> game;//游戏
		List<Integer> roomList;
		BaseRoom room = null;//游戏房间
		if(gameMap.containsKey(gameType)){
			game = gameMap.get(gameType);
			if(game.containsKey(roomType)){
				//房间已存在，并且未达到人数上线
				roomList = game.get(roomType);
				for(Integer rmId : roomList){
					BaseRoom rm = allRooms.get(rmId);
					if(rm != null && rm.getUpperLimit() > rm.getGamerCount()){
						List<Gamer> gamerList = new ArrayList<Gamer>();
						gamerList.add(gamer);
						Integer roomId = this.joinRoomBySocket(gamerList, rm.getId());
						
						if(roomId != null){
							if(rm.addGamer(gamer)){
								room = rm;
								break;
							}
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
		logger.info("创建房间");
		Map<String, List<Integer>> game;//游戏
		List<Integer> roomList;
		BaseRoom room = null;//游戏房间
		if(!gameMap.containsKey(gameType)){
			logger.info("战场中未找到 " + gameType + "，新增该类型加入战场");
			game = new HashMap<String, List<Integer>>();
			gameMap.put(gameType, game);
		}else {
			logger.info("战场中已存在游戏 " + gameType + "，直接取出");
			game = gameMap.get(gameType);
		}
		if(!game.containsKey(roomType)){
			logger.info("游戏 " + gameType + "，不存在房间类型为" + roomType + "，新增");
			roomList = new ArrayList<Integer>();
			game.put(roomType, roomList);
		}else{
			logger.info("游戏 " + gameType + "，存在房间类型为" + roomType + "，取出");
			roomList = game.get(roomType);
		}
		
		if(GameType.Gomoku.equals(gameType)){
			//请求socket得到房间,根据人数计算创建房间的数量
			int roomNum = gamerList.size() % GomokuRoom.UPPER_LIMIT == 0 ? gamerList.size() / GomokuRoom.UPPER_LIMIT : gamerList.size() / GomokuRoom.UPPER_LIMIT + 1;
			int balanceLength = gamerList.size();
			logger.info("需要创建的房间数量" + roomNum + ", 总人数" + balanceLength);
			for (int i = 0; i < roomNum; i++) {
				int toIndex = balanceLength > GomokuRoom.UPPER_LIMIT ? GomokuRoom.UPPER_LIMIT : balanceLength;
				List<Gamer> segList = gamerList.subList(i, toIndex);
				Integer roomId  = this.assignRoomBySocket(segList);
				room = new GomokuRoom(roomId, gameType, roomType, GomokuRoom.CAMP_COUNT, GomokuRoom.TURN_DELAY,
						GomokuRoom.UPPER_LIMIT, GomokuRoom.Default_MAXX, GomokuRoom.Default_MAXY);
				roomList.add(room.getId());
				room.addGamer(segList);
				allRooms.put(roomId, room);
				logger.info("第" + (i + 1) + "个房间创建成功，房间id=" + roomId);
			}
		}
	}
	
	public Integer assignRoomBySocket(List<Gamer> gamers){
		//请求battleserver
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("msgType", MsgType.AsignRoomRequest.getMsgType());
		reqMap.put("gamers", DataUtils.toGson(gamers));
		reqMap.put("gameType", GameType.Gomoku);
		reqMap.put("roomType", RoomType.Gomoku_Two);
		reqMap.put("operType", GamerConstant.Room_Crt);
		String socketReq = DataUtils.toGson(reqMap);
		logger.info("请求创建房间，参数：" + socketReq);
		byte[] socketRsp = null;
		try {
			byte[] reqHeadBytes = DataUtils.number2Bytes((short)socketReq.length());
			byte[] reqBodyBytes = socketReq.getBytes(Default_Charset);
			ByteBuffer reqBuff = DataUtils.genBuff(reqHeadBytes, reqBodyBytes);
			reqBuff.flip();
			socketRsp = SocketUtils.sendMsg(reqBuff.array(), SocketUtils.Battle_Server_ip, SocketUtils.Battle_Server_Port, true);
			if(socketRsp == null){
				return null;
			}
			return DataUtils.byteArray2T(socketRsp, Integer.class);
		} catch (Exception e) {
			logger.error("socket异常", e);
			return null;
		}
	}
	
	/**
	 * 
	 * @Description: 加入现有房间
	 * @param @param gamers
	 * @param @return     
	 * @return Integer 
	 * @throws
	 */
	public Integer joinRoomBySocket(List<Gamer> gamers, int roomId){
		//请求battleserver
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("msgType", MsgType.AsignRoomRequest.getMsgType());
		reqMap.put("gamers", DataUtils.toGson(gamers));
		reqMap.put("gameType", GameType.Gomoku);
		reqMap.put("operType", GamerConstant.Room_Join);
		reqMap.put("roomId", roomId);
		reqMap.put("roomType", RoomType.Gomoku_Two);
		String socketReq = DataUtils.toGson(reqMap);
		byte[] socketRsp = null;
		try {
			byte[] reqHeadBytes = DataUtils.number2Bytes((short)socketReq.length());
			byte[] reqBodyBytes = socketReq.getBytes(Default_Charset);
			ByteBuffer reqBuff = DataUtils.genBuff(reqHeadBytes, reqBodyBytes);
			reqBuff.flip();
			socketRsp = SocketUtils.sendMsg(reqBuff.array(), SocketUtils.Battle_Server_ip, SocketUtils.Battle_Server_Port, true);
			if(socketRsp == null){
				return null;
			}
			return DataUtils.byteArray2T(socketRsp, Integer.class);
		} catch (Exception e) {
			logger.error("socket异常", e);
			return null;
		}
	}
	
	/**
	 * @Description: 将玩家放入临时列表，状态为已准备
	 * @param gameType
	 * @param roomType
	 * @param gamer     
	 * @return void 
	 * @throws
	 */
	synchronized public void put2TmpMap(String gameType, String roomType, Gamer gamer){
		Map<String, List<Gamer>> rooms = tmpGamerMap.get(gameType);
		logger.info("rooms=" + rooms);
		if(rooms == null){
			gamer.setState(GamerConstant.Gamer_State_Ready);
			rooms = new HashMap<String, List<Gamer>>();
			rooms.put(roomType, new ArrayList<Gamer>(){
				private static final long serialVersionUID = 1L;
				{this.add(gamer);}});
		}else{
			List<Gamer> gamerList = rooms.get(roomType);
			if(gamerList == null){
				gamerList = new ArrayList<Gamer>();
			}
			if(!gamerList.contains(gamer)){
				gamer.setState(GamerConstant.Gamer_State_Ready);
				gamerList.add(gamer);
			}
			rooms.put(roomType, gamerList);
		}
		tmpGamerMap.put(gameType, rooms);
	}
	
	/**
	 * @Description: 检测临时列表，如果某个房间类型的请求玩家数达到2个，创建房间 
	 * @param      
	 * @return void 
	 * @throws
	 */
	public void checkTmpMap(){
		for(String gameType : tmpGamerMap.keySet()){
			Map<String, List<Gamer>> gamersMap = tmpGamerMap.get(gameType);
			for(String roomType : gamersMap.keySet()){
				if(gamersMap.get(roomType).size() >= 2){
					this.createRoom(gameType, roomType, gamersMap.get(roomType));
					gamersMap.remove(roomType);
					tmpGamerMap.put(gameType, gamersMap);
					logger.info("创建完房间，清空临时列表gameType=" + gameType + ",gamersMap=" + gamersMap);
				}
			}
		}
	}
	
	public  BaseRoom getRoomBySessionId(String sessionId){
		String deviceUID = SessionUtils.getDeviceUID(sessionId);
		Integer roomId = deviceRooms.get(deviceUID);
		BaseRoom room = allRooms.get(roomId);
		return room;
	}
	
	public  void remove(String sessionId){
		BaseRoom room = getRoomBySessionId(sessionId);
		if(room != null){
			room.remove(SessionUtils.getDeviceUID(sessionId));
			
			if(room.getGamerCount() <= 0){
				logger.info("roomId=" + room.getId() + "，房间人数为0，回收房间");
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
			}
			
		}else{
			Gamer gamer = new Gamer();
			gamer.setDeviceUID(SessionUtils.getDeviceUID(sessionId));
			
			for(String gameType : tmpGamerMap.keySet()){
				Map<String, List<Gamer>> gamersMap = tmpGamerMap.get(gameType);
				for(String roomType : gamersMap.keySet()){
					List<Gamer> list = gamersMap.get(roomType);
					if(list.contains(gamer)){
						list.remove(gamer);	
						gamersMap.put(roomType, list);
						tmpGamerMap.put(gameType, gamersMap);
						break;
					}
				}
			}
		}
		room = null;
	}
	
	
	public  void removeBySocket(Integer roomId, String deviceUIDs){
		BaseRoom room = allRooms.get(roomId);
		String[] devArr = deviceUIDs.split("\\|");
		logger.info("deviceUIDs=" + deviceUIDs);
		logger.info("游戏结束，玩家退出战场，清理gameserver房间信息，roomId=" + roomId + "，退出人数为" + devArr.length);
		for(String deviceUID : devArr){
			room.remove(deviceUID);
		}
		
		if(room.getGamerCount() <= 0){
			logger.info("roomId=" + roomId + "，房间人数为0，回收房间，清理战场中信息");
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
		}
	}
	
	

	@Override
	public byte[] process(byte[] reqData) {
		return null;
	}
}
