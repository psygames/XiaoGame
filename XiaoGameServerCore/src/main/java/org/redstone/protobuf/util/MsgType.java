/**  
 * @Title: MsgType.java
 * @Package org.redstone.handler
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月26日
 * @version V1.0  
 */
package org.redstone.protobuf.util;

/**
 * @ClassName: MsgType
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月26日
 *
 */
public enum MsgType {
	LoginRequest("LoginRequest",(short)1001),
	LoginReply("LoginReply",(short)1002),
	JoinBattleRequest("JoinBattleRequest",(short)1003),
	JoinBattleReply("JoinBattleReply",(short)1004),
	BoardSync("BoardSync",(short)1005),
	PlaceRequest("LoginRequest",(short)1007),
	PlaceReply("PlaceReply",(short)1008),
	NewTurnBroadcast("NewTurnBroadcast",(short)1011);
	
	private String clsName;
	private short msgType;
	MsgType(String clsName, short msgType){
		this.clsName = clsName;
		this.msgType = msgType;
	}
	
	
	
	public String getClsName(short msgType){
		for(MsgType s : MsgType.values()){
			if(s.msgType == msgType){
				return s.clsName;
			}
		}
		return null;
	}


	public String getClsName() {
		return clsName;
	}

	public Short getMsgType() {
		return msgType;
	}
}
