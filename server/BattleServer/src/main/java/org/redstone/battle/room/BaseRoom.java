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
	private Map<Integer, Integer> statistics;	
	private int turnCamp;
	private long turnStart;
	private long turnTime;
	public BaseRoom(int roomId, String gameType,String roomType,int campCount){
		this.id = roomId;
		this.gameType = gameType;
		this.roomType = roomType;
		this.campMap = new HashMap<Integer, Map<String, Gamer>>();
		this.gamers = new HashMap<String, Gamer>();
		for(int i = 0; i < campCount; i++){
			campMap.put(i, new HashMap<String, Gamer>());
		}
		
		statistics = new HashMap<Integer, Integer>();
	}
	
	/**
	 * 
	 * @Description: 添加玩家到人数最少的阵营 
	 * @param gamer     
	 * @return void 
	 * @throws
	 */
	public void addGamer(Gamer gamer){
		Map<String, Gamer> minMap = getMinCamp();
		minMap.put(gamer.getLogName(), gamer);
		gamers.put(gamer.getId(), gamer);
	}
	
	//得到人数最多的阵营
	public Map<String, Gamer> getMaxCamp(){
		Map<String, Gamer>  maxMap = campMap.get(0);
		for(Map<String, Gamer> value : campMap.values()){
			if(value.size() > maxMap.size()){
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
	public Map<String, Gamer> getMinCamp(){
		Map<String, Gamer>  minMap = campMap.get(0);
		for(Map<String, Gamer> value : campMap.values()){
			if(value.size() < minMap.size()){
				minMap = value;
			}
		}
		return minMap;
	}
	
	
	
	/**
	 * @Description:  统计该回合落子  
	 * @param chessNum     
	 * @return void 
	 * @throws
	 */
	public void putStatistics(int chessNum) {
		Integer count = statistics.get(chessNum);
		count = count == null || count == 0 ? 1 : count + 1;
		statistics.put(chessNum, count);
	}
	
	public void clearStatistics() {
		statistics.clear();
	}
	
	
	/**
	 * 
	 * @Description: 新的回合，计算出下棋的一方 
	 * @param @return     
	 * @return int 
	 * @throws
	 */
	public int newTurn(){
		//下棋方
		turnCamp = (turnCamp + 1) / campMap.keySet().size();
		//清空上回合的统计信息
		clearStatistics();
		
		return turnCamp;
	}
	
	
	
	
	
	
	
	public Map<Integer, Integer> getStatistics() {
		return statistics;
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
