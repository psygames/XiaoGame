/**  
 * @Title: BaseRoom.java
 * @Package org.redstone.battle.room
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月26日
 * @version V1.0  
 */
package org.redstone.battle.room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.redstone.db.model.Gamer;

/**
 * @ClassName: BaseRoom
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月26日
 *
 */
public class BaseRoom {
	private String id;
	private String gameType;
	private String roomType;
	private volatile int groupId = 0;
	private Map<Integer, Map<String, Gamer>> groupMap;
	private List<Gamer> gamers;
	
	public BaseRoom(String gameType,String roomType,int groupCount){
		this.id = UUID.randomUUID().toString();
		this.gameType = gameType;
		this.roomType = roomType;
		this.groupMap = new HashMap<Integer, Map<String, Gamer>>();
		this.gamers = new ArrayList<Gamer>();
		for(int i = 0; i < groupCount; i++){
			groupMap.put(i, new HashMap<String, Gamer>());
		}
	}
	
	public void addGamer(Gamer gamer){
		Map<String, Gamer> minMap = getMinGroup();
		minMap.put(gamer.getLogName(), gamer);
		gamers.add(gamer);
	}
	
	public Map<String, Gamer> getMaxGroup(){
		Map<String, Gamer>  maxMap = groupMap.get(0);
		for(Map<String, Gamer> value : groupMap.values()){
			if(value.size() > maxMap.size()){
				maxMap = value;
			}
		}
		return maxMap;
	}
	
	public Map<String, Gamer> getMinGroup(){
		Map<String, Gamer>  minMap = groupMap.get(0);
		for(Map<String, Gamer> value : groupMap.values()){
			if(value.size() < minMap.size()){
				minMap = value;
			}
		}
		return minMap;
	}

	public String getId() {
		return id;
	}

	public String getGameType() {
		return gameType;
	}

	public String getRoomType() {
		return roomType;
	}

	public int getGroupId() {
		return groupId;
	}

	public Map<Integer, Map<String, Gamer>> getGroupMap() {
		return groupMap;
	}

	public List<Gamer> getGamers() {
		return gamers;
	}
}
