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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.redstone.battle.battlemanage.ChinaBattleManage;
import org.redstone.battle.constant.GamerConstant;
import org.redstone.db.model.Gamer;
import org.redstone.protobuf.msg.AssignRoomReply;
import org.redstone.protobuf.msg.BattleResult;
import org.redstone.protobuf.msg.BoardSync;
import org.redstone.protobuf.msg.ChessRow;
import org.redstone.protobuf.msg.Enums.Camp;
import org.redstone.protobuf.msg.Enums.ChessType;
import org.redstone.protobuf.msg.NewTurnBroadcast;
import org.redstone.protobuf.util.DataUtils;
import org.redstone.protobuf.util.MsgType;
import org.redstone.protobuf.util.SessionUtils;
import org.redstone.server.BattleServer;
import org.redstone.service.GomokuService;

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
	Map<Integer, Integer> chessesCount;//每回合所下棋子-每个棋子对应的人数
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
		
		cts = new ChessType[this.boardX][this.boardY];
		for(int i = 0; i < this.boardX; i ++){
			for(int j = 0; j< this.boardY; j ++){
				cts[i][j] = ChessType.None;
			}
		}
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
				
				ByteBuffer buff = assignRoomReply();
				Session ss = BattleServer.sessionMap.get(SessionUtils.getSessionID(gamer.getDeviceUID()));
				try {
					ss.getBasicRemote().sendBinary(buff);
				} catch (IOException e) {
					logger.error("新回合通知所有玩家失败", e);
					return false;
				}
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
	public boolean turnCts() {
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
		
		
		//计算是否赢了
		int x = getX(max.getKey());
		int y = getY(max.getKey());
		ChessType ct = getChessType(turnCamp);
		boolean isWin = GomokuService.check(x, y, ct, boardX, boardY);
		return isWin;
	}
	
	public void clearStatistics() {
		chessesCount.clear();
		gamerChesses.clear();
	}
	
	public void addChess(int chessNum){
		int x = getX(chessNum);
		int y = getY(chessNum);
		ChessType ct = getChessType(turnCamp);
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
	
	public BoardSync genBoardSync(){
		//按行转换
		BoardSync.Builder boardBuilder = BoardSync.newBuilder();
		for(int y = 0; y < boardY; y++){
			ChessRow.Builder rowBuilder = ChessRow.newBuilder();
			for(int x = 0; x < boardX; x++){
				rowBuilder.addTypes(cts[x][y]);
			}
			boardBuilder.addRows(rowBuilder);
		}
		return boardBuilder.build();
	}
	
	public ByteBuffer boardSync2Buff(){
		BoardSync boardSync= genBoardSync();
		byte[] msgType = DataUtils.number2Bytes(MsgType.BoardSync.getMsgType());
		byte[] rspBody = boardSync.toByteArray();
		ByteBuffer buff = DataUtils.genBuff(msgType, rspBody);
		return buff;
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
	
	public ChessType getChessType(int camp){
		return ChessType.valueOf(camp);
	}
	
	public Camp getCampType(int camp){
		return Camp.valueOf(camp);
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
				if(count != 0 && (count == campMap.get(turnCamp).size() || System.currentTimeMillis() >= (turnBegin + turnDelay))){
					notifyAllGamers();
					newTurn();
				}else{
					//只通知本回合在下棋的一方，同步队友的下棋状况
					notifyTurnCampGamers();
				}
			}
		}, 100);
	}
	
	
	public boolean notifyAllGamers(){
		//计算本回合最终的落子
		boolean isWin = turnCts();
		
		ByteBuffer buff = null;
		if(isWin){
			//如果赢了，广播所有人
			buff = this.genBattleResultBuff(isWin);
		}else{
			//如果没赢，继续下一回合。广播下一回合获得下棋机会的阵营
			buff = this.genNewTurnBroadcastBuff();
		}
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
		return true;
	}
	
	public ByteBuffer genBattleResultBuff(boolean isWin){
		BattleResult.Builder builder = BattleResult.newBuilder();
		builder.setIsWin(isWin);
		byte[] msgType = DataUtils.number2Bytes(MsgType.BattleResult.getMsgType());
		byte[] rspBody = builder.build().toByteArray();
		ByteBuffer buff = DataUtils.genBuff(msgType, rspBody);
		return buff;
	}
	
	public ByteBuffer genNewTurnBroadcastBuff(){
		NewTurnBroadcast.Builder builder = NewTurnBroadcast.newBuilder();
		builder.setCamp(this.getCampType(turnCamp));
		byte[] msgType = DataUtils.number2Bytes(MsgType.NewTurnBroadcast.getMsgType());
		byte[] rspBody = builder.build().toByteArray();
		ByteBuffer buff = DataUtils.genBuff(msgType, rspBody);
		return buff;
	}
	
	/**
	 * 
	 * @Description: 通知当前回合下棋方所有玩家 
	 * @param   
	 * @return boolean 
	 * @throws
	 */
	public boolean notifyTurnCampGamers(){
		//通知信息
		ByteBuffer buff = boardSync2Buff();
		//通知本回合所属阵营的玩家
		Map<String, Gamer> gamersMap = campMap.get(turnCamp);
		for(String deviceUID : gamersMap.keySet()){
			Session ss = BattleServer.sessionMap.get(SessionUtils.getSessionID(deviceUID));
			try {
				ss.getBasicRemote().sendBinary(buff);
				logger.info("通知当前回合下棋方玩家" + deviceUID + "成功");
			} catch (IOException e) {
				logger.error("通知当前回合下棋方玩家失败" + deviceUID + "失败", e);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @Description: 新的回合，清空回合棋子暂存map，下棋机会流转到下一阵营，回合开始时间重置
	 * @param      
	 * @return void 
	 * @throws
	 */
	public void newTurn(){
		//清空上回合的统计信息
		clearStatistics();
		
		//下棋方
		turnCamp = (turnCamp + 1) / campMap.keySet().size();
		
		turnBegin = System.currentTimeMillis();
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
