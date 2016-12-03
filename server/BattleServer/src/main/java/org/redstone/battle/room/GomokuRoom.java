/**  
 * @Title: Room.java
 * @Package org.redstone.battle.room
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月26日
 * @version V1.0  
 */
package org.redstone.battle.room;

/**
 * @ClassName: Room
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月26日
 *
 */
public class GomokuRoom extends BaseRoom{

	/**
	 * GomokuRoom.
	 *
	 * @param roomId
	 * @param gameType
	 * @param roomType
	 * @param campCount
	 * @param turnDelay
	 * @param upperLimit
	 */
	public GomokuRoom(int roomId, String gameType, String roomType, int campCount, int boardX, int boardY, long turnDelay, int upperLimit) {
		super(roomId, gameType, roomType, campCount, boardX, boardY, turnDelay, upperLimit);
	}
	
	
}
