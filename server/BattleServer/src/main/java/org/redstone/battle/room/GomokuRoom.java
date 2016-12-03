/**  
 * @Title: Room.java
 * @Package org.redstone.battle.room
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月26日
 * @version V1.0  
 */
package org.redstone.battle.room;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.redstone.protobuf.msg.BoardSync;
import org.redstone.protobuf.msg.ChessRow;
import org.redstone.protobuf.msg.Enums.ChessType;

/**
 * @ClassName: Room
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月26日
 *
 */
public class GomokuRoom extends BaseRoom{
	private ChessType[][] cts;
	private int boardX;
	private int boardY;

	/**
	 * GomokuRoom.
	 *
	 * @param gameType
	 * @param roomType
	 * @param groupCount
	 */
	public GomokuRoom(int roomId, String gameType, String roomType, int groupCount, int boardX, int boardY) {
		super(roomId, gameType, roomType, groupCount);
		cts = new ChessType[boardX][boardY];
		this.boardX = boardX;
		this.boardY = boardY;
	}
	
	public void addChess(int x, int y, ChessType ct){
		cts[x][y] = ct;
	}
	
	public void array2List(){
		
		//按行转换
		BoardSync.Builder boardBuilder = BoardSync.newBuilder();
		for(int y = 0; y < boardY; y++){
			ChessRow.Builder rowBuilder = ChessRow.newBuilder();
			for(int x = 0; x < boardX; x++){
				rowBuilder.setTypes(x, cts[x][y]);
			}
			boardBuilder.setRows(y, rowBuilder);
		}
		
		
	}
}
