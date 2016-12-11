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
import java.util.Timer;
import java.util.TimerTask;

import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.apache.log4j.net.SyslogAppender;
import org.redstone.battle.battlemanage.ChinaBattleManage;
import org.redstone.battle.constant.GamerConstant;
import org.redstone.db.model.Gamer;
import org.redstone.protobuf.msg.BattleResult;
import org.redstone.protobuf.msg.BoardSync;
import org.redstone.protobuf.msg.ChessPlaceStatistics;
import org.redstone.protobuf.msg.ChessRow;
import org.redstone.protobuf.msg.Enums.Camp;
import org.redstone.protobuf.msg.Enums.ChessType;
import org.redstone.protobuf.msg.NewTurnBroadcast;
import org.redstone.protobuf.msg.PlaceStatisticsSync;
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
	private Timer timer = new Timer();
	private int id;
	private String gameType;
	private String roomType;
	Map<Camp, Map<String, Gamer>> campGamers;//阵营-该阵营玩家(玩家Map  key=deviceUID, value=Gamer)
	private Map<String, Camp> gamersCamp;//每个玩家-阵营
	private Integer gamerCount;//玩家总数
	Map<Integer, Integer> chessesCount;//每回合所下棋子-每个棋子对应的人数
	Map<String, Integer> gamerChesses;//玩家-棋子
	Camp turnCamp = Camp.WhiteCamp;//默认白方先手
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
		this.campGamers = new HashMap<Camp, Map<String, Gamer>>();
		this.gamersCamp = new HashMap<String, Camp>();
		this.turnDelay = turnDelay;
		this.boardX = boardX;
		this.boardY = boardY;
		this.upperLimit = upperLimit;
		
		cts = new ChessType[this.boardX][this.boardY];
		for(int i = 0; i < this.boardX; i ++){
			for(int j = 0; j< this.boardY; j ++){
				cts[i][j] = ChessType.None;
			}
		}
		gamerCount = 0;
		for(int i = 0; i < campCount; i++){
			campGamers.put(Camp.valueOf(i + 2), new HashMap<String, Gamer>());
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
				Entry<Camp, Map<String, Gamer>> minMap = getMinCamp();
				Camp key = minMap.getKey();
				Map<String, Gamer> value = minMap.getValue();
				
				gamer.setState(GamerConstant.Gamer_State_Joined);
				value.put(gamer.getDeviceUID(), gamer);
				
				campGamers.put(key, value);
				gamersCamp.put(gamer.getDeviceUID(), key);
				gamerCount ++;
				ChinaBattleManage.put2DeviceRooms(gamer.getDeviceUID(), id);
				logger.info("玩家" + gamer.getDeviceUID() + "，游戏类型" + gameType + "，房间类型=" + roomType + "，房间id=" + id + "，加入成功");
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * @Description: 添加玩家到人数最少的阵营 
	 * @param gamer     
	 * @return void 
	 * @throws
	 */
	public boolean addGamerByClient(Gamer gamer){
		Camp camp = gamersCamp.get(gamer.getDeviceUID());
		if(camp != null){
			Map<String, Gamer> gamers = campGamers.get(camp);
			if(gamers != null){
				Gamer gamer2 = gamers.get(gamer.getDeviceUID());
				if(gamer2 != null){
					gamer2.setState(GamerConstant.Gamer_State_Joined);
					gamers.put(gamer.getDeviceUID(), gamer2);
					campGamers.put(camp, gamers);
					ChinaBattleManage.put2DeviceRooms(gamer.getDeviceUID(), id);
					logger.info("玩家" + gamer.getDeviceUID() + "，游戏类型" + gameType + "，房间类型=" + roomType + "，房间id=" + id + "，加入成功");
					return true;
				}
			}
		}
		logger.info("玩家" + gamer.getDeviceUID() + "，游戏类型" + gameType + "，房间类型=" + roomType + "，房间id=" + id + "，加入失败");
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
//			if(!this.addGamer(gamer)){
//					return false;		
//			}
			this.addGamer(gamer);
		}
		return true;
	}
	
	/**
	 * @Description: 得到人数最多的阵营
	 * @return Map<String,Gamer> 
	 * @throws
	 */
	public Entry<Camp, Map<String, Gamer>> getMaxCamp(){
		Iterator<Entry<Camp, Map<String, Gamer>>> it = campGamers.entrySet().iterator();
		Entry<Camp, Map<String, Gamer>>  maxMap = null;
		if(it.hasNext()){
			maxMap = it.next();
		}
		while(it.hasNext()){
			Entry<Camp, Map<String, Gamer>> value = it.next();
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
	public Entry<Camp, Map<String, Gamer>> getMinCamp(){
		Iterator<Entry<Camp, Map<String, Gamer>>> it = campGamers.entrySet().iterator();
		Entry<Camp, Map<String, Gamer>>  minMap = null;
		if(it.hasNext()){
			minMap = it.next();
		}
		while(it.hasNext()){
			Entry<Camp, Map<String, Gamer>> value = it.next();
			logger.info("min:key=" + minMap.getKey() + ",value=" + minMap.getValue());
			logger.info("compared:key=" + value.getKey() + ",value=" + value.getValue());
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
		
		for(int i = 0; i < cts.length; i ++){
			StringBuffer tmp = new StringBuffer();
			for(int j = 0; j < cts[i].length; j ++){
				tmp.append(",").append(cts[i][j]);
			}
			logger.info("i=" + i + tmp.toString());
		}
		
		
		//计算是否赢了
		int x = getX(max.getKey());
		int y = getY(max.getKey());
		ChessType ct = getChessType(turnCamp.getNumber());
		boolean isWin = GomokuService.check(x, y, ct, boardX, boardY, cts);
		return isWin;
	}
	
	public void clearStatistics() {
		chessesCount.clear();
		gamerChesses.clear();
	}
	
	public void addChess(int chessNum){
		int x = getX(chessNum);
		int y = getY(chessNum);
		ChessType ct = getChessType(turnCamp.getNumber());
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
			boardBuilder.addRows(rowBuilder.build());
		}
		return boardBuilder.build();
	}
	
	
	public PlaceStatisticsSync genPlaceStatisticsSync(){
		//按行转换
		PlaceStatisticsSync.Builder placeBuilder = PlaceStatisticsSync.newBuilder();
		for(Entry<Integer, Integer> entry : chessesCount.entrySet()){
			ChessPlaceStatistics.Builder chessBuilder = ChessPlaceStatistics.newBuilder();
			chessBuilder.setNum(entry.getKey());
			chessBuilder.setRatio(1f * entry.getValue()/ campGamers.get(turnCamp).size());
			placeBuilder.addStatistics(chessBuilder.build());
		}
		return placeBuilder.build();
		
	}
	
	public ByteBuffer placeStatisticsSync2Buff(){
		PlaceStatisticsSync placeSync= genPlaceStatisticsSync();
		byte[] msgType = DataUtils.number2Bytes(MsgType.PlaceStatisticsSync.getMsgType());
		byte[] rspBody = placeSync.toByteArray();
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
	public void startTimer(){
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				int count = gamerChesses.size();
				logger.info("该回合当前已选择的棋子数量" + count);
				//如果本回合下棋方所有人都下过了棋，下一回合；如果超时，下一回合
				if(count != 0 && (count == campGamers.get(turnCamp).size() || System.currentTimeMillis() >= (turnBegin + turnDelay))){
					logger.info("该回合结束，进入下一回合");
					notifyAllGamers();
					newTurn();
				}else{
					//只通知本回合在下棋的一方，同步队友的下棋状况
					logger.info("通知当前下棋方，" + turnCamp);
					notifyTurnCampGamers();
				}
			}
		}, 1 * 1000, 1 * 1000);
	}
	
	public void stopTimer(){
		this.timer.cancel();
	}
	
	
	public boolean notifyAllGamers(){
		//计算本回合最终的落子
		boolean isWin = turnCts();
		ByteBuffer buff = null;
		logger.info("##################################################### isWin=" + isWin);
		if(isWin){
			//如果赢了，广播所有人
			buff = this.genBattleResultBuff();
		}else{
			//如果没赢，继续下一回合。广播下一回合获得下棋机会的阵营
			turnCamp = turnCamp.equals(Camp.BlackCamp) ? Camp.WhiteCamp : Camp.BlackCamp;
			buff = this.genNewTurnBroadcastBuff();
//			logger.info("#####################################################newturnBoardCast    " + buff.array().length);
//			for(int i = 0; i < buff.array().length; i ++){
//				logger.info("#####################################################i=" + i + "，" + buff.array()[i]);
//			}
		}
		buff.flip();
		//房间中所有玩家
		for(Camp camp : campGamers.keySet()){
			Map<String, Gamer> gamersMap = campGamers.get(camp);
			for(String deviceUID : gamersMap.keySet()){
				Session ss = BattleServer.sessionMap.get(SessionUtils.getSessionID(deviceUID));
				try {
					if(ss == null){
						logger.info("通知玩家" + deviceUID + "，未找到对应的session，当前sessionMap=" + BattleServer.sessionMap);
						return false;
					}
					logger.info("新回合，通知玩家" + deviceUID + "，字节长度" + buff.array().length);
					ss.getBasicRemote().sendBinary(buff);
				} catch (IOException e) {
					logger.error("通知所有玩家失败", e);
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean gameStartNotifyGamer(Gamer gamer){
		ByteBuffer buff = this.genNewTurnBroadcastBuff();
		logger.info("游戏开始通知玩家" + gamer.getDeviceUID() + "，字节长度"+ buff.array().length);
		//房间中所有玩家
		Session ss = BattleServer.sessionMap.get(SessionUtils.getSessionID(gamer.getDeviceUID()));
		try {
			if(ss == null){
				logger.info("游戏开始通知玩家" + gamer.getDeviceUID() + "，未找到对应的session，当前sessionMap=" + BattleServer.sessionMap);
				return false;
			}
			buff.flip();
			ss.getBasicRemote().sendBinary(buff);
		} catch (Exception e) {
			logger.error("游戏开始通知玩家" + gamer.getDeviceUID() + "失败", e);
			return false;
		}
		
		return true;
	}
	
	public ByteBuffer genBattleResultBuff(){
		BattleResult.Builder builder = BattleResult.newBuilder();
		builder.setCamp(turnCamp);
		byte[] msgType = DataUtils.number2Bytes(MsgType.BattleResult.getMsgType());
		byte[] rspBody = builder.build().toByteArray();
		ByteBuffer buff = DataUtils.genBuff(msgType, rspBody);
		return buff;
	}
	
	public ByteBuffer genNewTurnBroadcastBuff(){
		NewTurnBroadcast.Builder builder = NewTurnBroadcast.newBuilder();
		builder.setCamp(turnCamp);
		builder.setBoardSync(this.genBoardSync());
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
		//ByteBuffer buff = boardSync2Buff();
		ByteBuffer buff = placeStatisticsSync2Buff();
		buff.flip();
		//通知本回合所属阵营的玩家
		Map<String, Gamer> gamersMap = campGamers.get(turnCamp);
		for(String deviceUID : gamersMap.keySet()){
			String sessionId = SessionUtils.getSessionID(deviceUID);
			Session ss = BattleServer.sessionMap.get(sessionId);
			try {
				if(ss == null){
					logger.info("通知当前回合下棋方玩家" + deviceUID + "，对应的sessionId=" + sessionId + "，未找到session，当前sessionMap=" + BattleServer.sessionMap);
					return false;
				}
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
		
		turnBegin = System.currentTimeMillis();
	}
	
	
	synchronized public void remove(String deviceUID){
		Camp camp = null;
		
		if(gamersCamp.containsKey(deviceUID)){
			camp = gamersCamp.get(deviceUID);
			gamersCamp.remove(deviceUID);
			logger.info("房间id=" + id + "，gamersCamp=" + gamersCamp);
			
			Map<String, Gamer> gamers = campGamers.get(camp);
			if(gamers.containsKey(deviceUID)){
				gamers.remove(deviceUID);
				campGamers.put(camp, gamers);
				logger.info("房间id=" + id + "，gamers=" + gamers);
				logger.info("房间id=" + id + "，campMap=" + campGamers);
			}
			
			if(gamerChesses.containsKey(deviceUID)){
				Integer chessNum = gamerChesses.get(deviceUID);
				if(chessesCount.containsKey(chessNum)){
					chessesCount.put(chessNum, chessesCount.get(chessNum)-1);
				}
				gamerChesses.remove(deviceUID);
				logger.info("房间id=" + id + "，gamerChesses=" + gamerChesses);
			}
			gamerCount --;
		}
		logger.info("房间id=" + id + "，当前玩家数量" + gamerCount);
		if(gamerCount <= 0){
			this.stopTimer();
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

	public Map<Camp, Map<String, Gamer>> getCampMap() {
		return campGamers;
	}

	public Map<String, Camp> getGamersCamp() {
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
	
	public String toString(){
		return "id=" + id + ",gameType=" + gameType + ",roomType=" + roomType + ",gamerCount=" + gamerCount;
		
	}
	
}
