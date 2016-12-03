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
import java.util.Timer;
import java.util.TimerTask;

import javax.websocket.Session;

import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.redstone.battle.battlemanage.ChinaBattleManage;
import org.redstone.battle.constant.GamerConstant;
import org.redstone.db.model.Gamer;
import org.redstone.protobuf.msg.BoardSync;
import org.redstone.protobuf.msg.ChessRow;
import org.redstone.protobuf.msg.Enums.ChessType;
import org.redstone.protobuf.util.DataUtils;
import org.redstone.protobuf.util.MsgType;
import org.redstone.protobuf.util.SessionUtils;
import org.redstone.server.BattleServer;

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
	Map<Integer, Map<String, Gamer>> campMap;//阵营-该阵营玩家(玩家Map  key=deviceUID, value=Gamer)
	private Map<String, Integer> gamersCamp;//每个玩家-阵营
	private Integer gamerCount;//玩家总数
	Map<Integer, Integer> chessesCount;//回合棋子-人数
	Map<String, Integer> gamerChesses;//玩家-棋子
	int turnCamp;
	long turnDelay;
	long turnBegin;
	private int upperLimit;
	private ChessType[][] cts;
	private int boardX;
	private int boardY;
	public BaseRoom(int roomId, String gameType,String roomType,int campCount, int boardX, int boardY, long turnDelay, int upperLimit){
		this.id = roomId;
		this.gameType = gameType;
		this.roomType = roomType;
		this.campMap = new HashMap<Integer, Map<String, Gamer>>();
		this.gamersCamp = new HashMap<String, Integer>();
		this.turnDelay = turnDelay;
		this.boardX = boardX;
		this.boardY = boardY;
		
		cts = new ChessType[boardX][boardY];
		gamerCount = 0;
		for(int i = 0; i < campCount; i++){
			campMap.put(i, new HashMap<String, Gamer>());
		}
		chessesCount = new HashMap<Integer, Integer>();
		gamerChesses = new HashMap<String, Integer>();
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
				ChinaBattleManage.sessionRoom.put(gamer.getDeviceUID(), this);
				logger.info("玩家" + gamer.getId() + "，加入游戏类型" + gameType + "，房间类型=" + roomType + "，房间id=" + id);
				return true;
			}
		}
		return false;
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
			if(this.addGamer(gamer)){
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
		Iterator<Entry<Integer, Map<String, Gamer>>> it = campMap.entrySet().iterator();
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
		Iterator<Entry<Integer, Map<String, Gamer>>> it = campMap.entrySet().iterator();
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
	
	
	
	/**
	 * @Description:  统计落子  
	 * @param chessNum     
	 * @return void 
	 * @throws
	 */
	public void putStatistics(String deviceUID, int chessNum) {
		
		gamerChesses.put(deviceUID, chessNum);
		
		Integer count = chessesCount.get(chessNum);
		count = count == null || count == 0 ? 1 : count + 1;
		chessesCount.put(chessNum, count);
		addChess(chessNum);
	}
	
	
	/**
	 * @Description:  计算该回合落子 ，将之前落子还原
	 * @return void 
	 * @throws
	 */
	public void turnCts() {
		Iterator<Entry<Integer, Integer>> it = chessesCount.entrySet().iterator();
		Entry<Integer, Integer> max = it.next();
		this.chess2None(max.getKey());
		while(it.hasNext()){
			Entry<Integer, Integer> entry = it.next();
			if(entry.getValue() > max.getValue()){
				max = entry;
			}
			this.chess2None(entry.getKey());
		}
		//将选择次数最多的棋子放入棋盘
		this.addChess(max.getKey());
	}
	
	public void clearStatistics() {
		chessesCount.clear();
		gamerChesses.clear();
	}
	
	public void addChess(int chessNum){
		int x = getX(chessNum);
		int y = getY(chessNum);
		ChessType ct = getCampType(turnCamp);
		cts[x][y] = ct;
	}
	
	public void chess2None(int chessNum){
		int x = getX(chessNum);
		int y = getY(chessNum);
		cts[x][y] = ChessType.None;
	}
	
	public int getX(int chessNum){
		return chessNum % boardY;
	}
	
	public int getY(int chessNum){
		return chessNum / boardY;
	}
	
	public BoardSync array2List(){
		//按行转换
		BoardSync.Builder boardBuilder = BoardSync.newBuilder();
		for(int y = 0; y < boardY; y++){
			ChessRow.Builder rowBuilder = ChessRow.newBuilder();
			for(int x = 0; x < boardX; x++){
				rowBuilder.setTypes(x, cts[x][y]);
			}
			boardBuilder.setRows(y, rowBuilder);
		}
		return boardBuilder.build();
	}
	
	public ByteBuffer boardSync(){
		BoardSync boardSync= array2List();
		byte[] msgType = DataUtils.numberToBytes(MsgType.BoardSync.getMsgType());
		byte[] rspBody = boardSync.toByteArray();
		ByteBuffer buff = DataUtils.genBuff(msgType, rspBody);
		return buff;
	}
	
	public ChessType getCampType(int camp){
		return ChessType.valueOf(camp);
	}
	
	/**
	 * 
	 * @Description: 游戏开始，启动定时器计时
	 * @param      
	 * @return void 
	 * @throws
	 */
	public void timerCheckEnd(){
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				
				int count = gamerChesses.size();
				//如果本回合下棋方所有人都下过了棋，下一回合；如果超时，下一回合
				if(count == campMap.get(turnCamp).size() || System.currentTimeMillis() >= (turnBegin + turnDelay)){
					if(!notifyAllGamers()){//通知失败
						
					}
					newTurn();
				}else{
					notifyTurnCampGamers();
				}
			}
		}, 100);
	}
	
	
	public boolean notifyAllGamers(){
		//计算本回合最终的落子
		turnCts();
		
		//通知信息
		ByteBuffer buff = boardSync();
		//房间中所有玩家
		for(Integer camp : campMap.keySet()){
			Map<String, Gamer> gamersMap = campMap.get(camp);
			for(String deviceUID : gamersMap.keySet()){
				Session ss = BattleServer.sessionMap.get(SessionUtils.getSessionID(deviceUID));
				try {
					ss.getBasicRemote().sendBinary(buff);
				} catch (IOException e) {
					logger.error("新回合通知所有玩家失败", e);
					return false;
				}
			}
		}
		
		//回合开始时间
		turnBegin = System.currentTimeMillis();
		return true;
	}
	
	/**
	 * 
	 * @Description: 通知本回合下棋方所有玩家 
	 * @param   
	 * @return boolean 
	 * @throws
	 */
	public boolean notifyTurnCampGamers(){
		//通知信息
		ByteBuffer buff = boardSync();
		//通知本回合所属阵营的玩家
		Map<String, Gamer> gamersMap = campMap.get(turnCamp);
		for(String deviceUID : gamersMap.keySet()){
			Session ss = BattleServer.sessionMap.get(SessionUtils.getSessionID(deviceUID));
			try {
				ss.getBasicRemote().sendBinary(buff);
			} catch (IOException e) {
				logger.error("新回合通知所有玩家失败", e);
				return false;
			}
		}
		return true;
	}
	
	public void newTurn(){
		//清空上回合的统计信息
		clearStatistics();
		
		//下棋方
		turnCamp = (turnCamp + 1) / campMap.keySet().size();
	}
	
	
	public void remove(String deviceUID){
		Integer camp = null;
		if(gamersCamp.containsKey(deviceUID)){
			camp = gamersCamp.get(deviceUID);
			gamersCamp.remove(deviceUID);
			
			Map<String, Gamer> gamers = campMap.get(camp);
			if(gamers.containsKey(deviceUID)){
				gamers.remove(deviceUID);
			}
			
			if(gamerChesses.containsKey(deviceUID)){
				Integer chessNum = gamerChesses.get(deviceUID);
				if(chessesCount.containsKey(chessNum)){
					chessesCount.put(chessNum, chessesCount.get(chessNum)-1);
				}
				gamerChesses.remove(deviceUID);
			}
			gamerCount --;
		}
		
	}
	
	
	public Map<Integer, Integer> getChessesCount() {
		return chessesCount;
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

	public Map<String, Integer> getGamersCamp() {
		return gamersCamp;
	}

	public int getUpperLimit() {
		return upperLimit;
	}

	public Integer getGamerCount() {
		return gamerCount;
	}
	
}
