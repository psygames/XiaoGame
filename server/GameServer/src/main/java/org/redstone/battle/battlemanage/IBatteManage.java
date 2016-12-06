/**  
 * @Title: IBatteManage.java
 * @Package org.redstone.battle.china
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月26日
 * @version V1.0  
 */
package org.redstone.battle.battlemanage;

import org.redstone.battle.room.BaseRoom;
import org.redstone.db.model.Gamer;

/**
 * @ClassName: IBatteManage
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月26日
 *
 */
public interface IBatteManage {
	void asignRoom(String gameType, String roomType, Gamer gamer);
	byte[] process(byte[] reqData);
}
