/**  
 * @Title: BaseRoom.java
 * @Package org.redstone.battle.room
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月26日
 * @version V1.0  
 */
package org.redstone.battle.room;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.redstone.battle.battlemanage.ChinaBattleManage;
import org.redstone.battle.constant.GamerConstant;
import org.redstone.db.model.Gamer;
import org.redstone.protobuf.msg.AssignRoomReply;
import org.redstone.protobuf.util.DataUtils;
import org.redstone.protobuf.util.MsgType;
import org.redstone.protobuf.util.SessionUtils;
import org.redstone.server.GameServer;

/**
 * @ClassName: BaseRoom
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月26日
 *
 */
public class BaseRoom {
	private static Logger logger = Logger.getLogger(BaseRoom.class);
	private int id;
	private String gameType;
	private String roomType;
	Map<Integer, Map<String, Gamer>> campGamers;//阵营-该阵营玩家(玩家Map  key=deviceUID, value=Gamer)
	private Map<String, Integer> gamersCamp;//每个玩家-阵营
	private Integer gamerCount;//玩家总数
	private int upperLimit;
	public BaseRoom(int roomId, String gameType,String roomType,int campCount, long turnDelay, int upperLimit, int boardX, int boardY){
		this.id = roomId;
		this.gameType = gameType;
		this.roomType = roomType;
		this.campGamers = new HashMap<Integer, Map<String, Gamer>>();
		this.gamersCamp = new HashMap<String, Integer>();
		this.upperLimit = upperLimit;
		gamerCount = 0;
		for(int i = 0; i < campCount; i++){
			campGamers.put(i, new HashMap<String, Gamer>());
		}
	}
	
	/**
	 * 
	 * @Description: 添加玩家到人数最少的阵营 
	 * @param gamer     
	 * @return void 
	 * @throws
	 */
	public boolean addGamer(Gamer gamer){
		synchronized(gamerCount){
			if(this.upperLimit > this.gamerCount){
				Entry<Integer, Map<String, Gamer>> minMap = getMinCamp();
				gamer.setState(GamerConstant.Gamer_State_Joined);
				minMap.getValue().put(gamer.getDeviceUID(), gamer);
				gamersCamp.put(gamer.getDeviceUID(), minMap.getKey());
				gamerCount ++;
				ChinaBattleManage.deviceRooms.put(gamer.getDeviceUID(), id);
				ByteBuffer buff = assignRoomReply();
				Session ss = GameServer.sessionMap.get(SessionUtils.getSessionID(gamer.getDeviceUID()));
				try {
					buff.flip();
					ss.getBasicRemote().sendBinary(buff);
					logger.info("deviceUID=" + gamer.getDeviceUID() + "，加入游戏类型" + gameType + "，房间类型=" + roomType + "，房间id=" + id);
				} catch (IOException e) {
					logger.error("玩家加入房间，通知失败", e);
					return false;
				}
				logger.info("玩家" + gamer.getId() + "，加入游戏类型" + gameType + "，房间类型=" + roomType + "，房间id=" + id);
				return true;
			}
		}
		return false;
	}
	
	public ByteBuffer assignRoomReply(){
		AssignRoomReply.Builder builder = AssignRoomReply.newBuilder();
		builder.setAddress("ws://192.168.10.106:8080/BattleServer/battleServer");
		builder.setRoomId(id);
		byte[] msgType = DataUtils.number2Bytes(MsgType.AsignRoomReply.getMsgType());
		byte[] rspBody = builder.build().toByteArray();
		ByteBuffer buff = DataUtils.genBuff(msgType, rspBody);
		return buff;
	}
	
	
	
	
	/**
	 * 
	 * @Description: 添加玩家到人数最少的阵营 
	 * @param gamerList     
	 * @return void 
	 * @throws
	 */
	public boolean addGamer(List<Gamer> gamerList){
		for(Gamer gamer : gamerList){
			if(!this.addGamer(gamer)){
					return false;		
			}
		}
		return true;
	}
	
	/**
	 * @Description: 得到人数最多的阵营
	 * @return Map<String,Gamer> 
	 * @throws
	 */
	public Entry<Integer, Map<String, Gamer>> getMaxCamp(){
		Iterator<Entry<Integer, Map<String, Gamer>>> it = campGamers.entrySet().iterator();
		Entry<Integer, Map<String, Gamer>>  maxMap = null;
		if(it.hasNext()){
			maxMap = it.next();
		}
		while(it.hasNext()){
			Entry<Integer, Map<String, Gamer>> value = it.next();
			if(value.getValue().size() > maxMap.getValue().size()){
				maxMap = value;
			}
		}
		return maxMap;
	}
	
	/**
	 * @Description: 得到人数最少的阵营 
	 * @return Map<String,Gamer> 
	 * @throws
	 */
	public Entry<Integer, Map<String, Gamer>> getMinCamp(){
		Iterator<Entry<Integer, Map<String, Gamer>>> it = campGamers.entrySet().iterator();
		Entry<Integer, Map<String, Gamer>>  minMap = null;
		if(it.hasNext()){
			minMap = it.next();
		}
		while(it.hasNext()){
			Entry<Integer, Map<String, Gamer>> value = it.next();
			if(value.getValue().size() < minMap.getValue().size()){
				minMap = value;
			}
		}
		return minMap;
	}
	
	public void remove(String deviceUID){
		Integer camp = null;
		if(gamersCamp.containsKey(deviceUID)){
			camp = gamersCamp.get(deviceUID);
			gamersCamp.remove(deviceUID);
			
			Map<String, Gamer> gamers = campGamers.get(camp);
			if(gamers.containsKey(deviceUID)){
				gamers.remove(deviceUID);
				campGamers.put(camp, gamers);
				logger.info("房间id=" + id + "，gamers=" + gamers);
				logger.info("房间id=" + id + "，campMap=" + campGamers);
			}
			gamerCount --;
		}
		logger.info("房间id=" + id + "，当前玩家数量" + gamerCount);
	}

	public Integer getId() {
		return id;
	}

	public String getGameType() {
		return gameType;
	}

	public String getRoomType() {
		return roomType;
	}

	public Map<Integer, Map<String, Gamer>> getCampMap() {
		return campGamers;
	}

	public Map<String, Integer> getGamersCamp() {
		return gamersCamp;
	}

	public int getUpperLimit() {
		return upperLimit;
	}

	public Integer getGamerCount() {
		return gamerCount;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BaseRoom){
			if(((BaseRoom) obj).getId() == (this.getId())){
				return true;
			}
		}
		return false;
	}
	
}
