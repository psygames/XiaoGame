/**  
 * @Title: BaseRoom.java
 * @Package org.redstone.battle.room
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月26日
 * @version V1.0  
 */
package org.redstone.battle.room;

import java.util.HashMap;
import java.util.Map;

import org.redstone.db.model.Gamer;

/**
 * @ClassName: BaseRoom
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月26日
 *
 */
public class BaseRoom {
	private int id;
	private String gameType;
	private String roomType;
	private Map<Integer, Map<String, Gamer>> campMap;
	private Map<String, Gamer> gamers;
	
	public BaseRoom(int roomId, String gameType,String roomType,int campCount){
		this.id = roomId;
		this.gameType = gameType;
		this.roomType = roomType;
		this.campMap = new HashMap<Integer, Map<String, Gamer>>();
		this.gamers = new HashMap<String, Gamer>();
		for(int i = 0; i < campCount; i++){
			campMap.put(i, new HashMap<String, Gamer>());
		}
	}
	
	public void addGamer(Gamer gamer){
		Map<String, Gamer> minMap = getMinCamp();
		minMap.put(gamer.getLogName(), gamer);
		gamers.put(gamer.getId(), gamer);
	}
	
	public Map<String, Gamer> getMaxCamp(){
		Map<String, Gamer>  maxMap = campMap.get(0);
		for(Map<String, Gamer> value : campMap.values()){
			if(value.size() > maxMap.size()){
				maxMap = value;
			}
		}
		return maxMap;
	}
	
	public Map<String, Gamer> getMinCamp(){
		Map<String, Gamer>  minMap = campMap.get(0);
		for(Map<String, Gamer> value : campMap.values()){
			if(value.size() < minMap.size()){
				minMap = value;
			}
		}
		return minMap;
	}

	public int getId() {
		return id;
	}

	public String getGameType() {
		return gameType;
	}

	public String getRoomType() {
		return roomType;
	}

	public Map<Integer, Map<String, Gamer>> getCampMap() {
		return campMap;
	}

	public Map<String, Gamer> getGamers() {
		return gamers;
	}
}
