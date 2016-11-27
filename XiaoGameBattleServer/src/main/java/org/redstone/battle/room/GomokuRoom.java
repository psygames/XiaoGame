/**  
 * @Title: Room.java
 * @Package org.redstone.battle.room
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月26日
 * @version V1.0  
 */
package org.redstone.battle.room;

import java.util.Map;

import org.redstone.db.model.Gamer;

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
	 * @param gameType
	 * @param roomType
	 * @param groupCount
	 */
	public GomokuRoom(String gameType, String roomType, int groupCount) {
		super(gameType, roomType, groupCount);
	}
	
}
