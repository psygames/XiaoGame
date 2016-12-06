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
	
	private static Map<String, Map<String, List<BaseRoom>>> gameMap = new ConcurrentHashMap<String, Map<String, List<BaseRoom>>>();
	private static Map<String, Map<String, List<Gamer>>> tmpGamerMap = new ConcurrentHashMap<String, Map<String, List<Gamer>>>();
	private static Map<Integer, BaseRoom> allRoom = new ConcurrentHashMap<Integer, BaseRoom>();
	public static Map<String, BaseRoom> sessionRoom = new ConcurrentHashMap<String, BaseRoom>();
	
	private static ChinaBattleManage chinaBattleManage;
	public static ChinaBattleManage getInstance(){
		if(chinaBattleManage == null){
			chinaBattleManage = new ChinaBattleManage();
		}
		return chinaBattleManage;
	}
	
	public void asignRoom(String gameType, String roomType, Gamer gamer) {
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
			
			if(GameType.Gomoku.equals(gameType)){
				//请求socket得到房间,根据人数计算创建房间的数量
				int roomNum = gamerList.size() % GomokuRoom.UPPER_LIMIT == 0 ? gamerList.size() / GomokuRoom.UPPER_LIMIT : gamerList.size() / GomokuRoom.UPPER_LIMIT + 1;
				int balanceLength = gamerList.size();
				for(int i = 0; i < roomNum; i++){
					int toIndex = balanceLength > GomokuRoom.UPPER_LIMIT ? GomokuRoom.UPPER_LIMIT : balanceLength;
					List<Gamer> segList = gamerList.subList(i, toIndex);
					Integer roomId  = this.assignRoomBySocket(segList);
					room = new GomokuRoom(roomId, gameType, roomType, GomokuRoom.CAMP_COUNT, GomokuRoom.TURN_DELAY,
							GomokuRoom.UPPER_LIMIT, GomokuRoom.Default_MAXX, GomokuRoom.Default_MAXY);
					allRoom.put(roomId, room);
					roomList.add(room);
					room.addGamer(segList);
				}
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
		byte[] socketRsp = null;
		try {
			byte[] reqHeadBytes = DataUtils.number2Bytes((short)socketReq.length());
			byte[] reqBodyBytes = socketReq.getBytes(Default_Charset);
			ByteBuffer reqBuff = DataUtils.genBuff(reqHeadBytes, reqBodyBytes);
			reqBuff.flip();
			socketRsp = SocketUtils.sendMsg(reqBuff.array());
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
			socketRsp = SocketUtils.sendMsg(reqBuff.array());
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
		if(rooms == null){
			gamer.setState(GamerConstant.Gamer_State_Ready);
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
				gamer.setState(GamerConstant.Gamer_State_Ready);
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
			Map<String, List<Gamer>> gamersMap = tmpGamerMap.get(gameType);
			for(String roomType : gamersMap.keySet()){
				if(gamersMap.get(roomType).size() >= 2){
					this.createRoom(gameType, roomType, gamersMap.get(roomType));
				}
			}
		}
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
			
		}else{
			Gamer gamer = new Gamer();
			gamer.setDeviceUID(SessionUtils.getDeviceUID(sessionId));
			
			for(String gameType : tmpGamerMap.keySet()){
				Map<String, List<Gamer>> gamersMap = tmpGamerMap.get(gameType);
				for(String roomType : gamersMap.keySet()){
					List<Gamer> list = gamersMap.get(roomType);
					if(list.contains(gamer)){
						list.remove(gamer);	
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
