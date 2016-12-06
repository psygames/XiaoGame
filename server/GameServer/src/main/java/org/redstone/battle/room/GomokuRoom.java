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
	public static int Default_MAXX = 12;
	public static int Default_MAXY = 12;
	public static int UPPER_LIMIT = 100;
	public static int LOWER_LIMIT = 2;
	public static int CAMP_COUNT = 2;
	public static int TURN_DELAY = 30 * 1000;

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
	public GomokuRoom(int roomId, String gameType, String roomType, int campCount, long turnDelay, int upperLimit, int boardX, int boardY) {
		super(roomId, gameType, roomType, campCount, turnDelay, upperLimit , boardX, boardY);
	}
	
	
}
