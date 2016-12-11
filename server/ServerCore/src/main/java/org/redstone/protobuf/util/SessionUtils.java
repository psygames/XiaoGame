/**  
 * @Title: SessionUtils.java
 * @Package org.redstone.protobuf.util
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月27日
 * @version V1.0  
 */
package org.redstone.protobuf.util;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.redstone.db.model.Gamer;

/**
 * @ClassName: SessionUtils
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月27日
 *
 */
public class SessionUtils {
	private static Logger logger = Logger.getLogger(SessionUtils.class);
	private static ConcurrentHashMap<String, Gamer> deviceGamerMap = new ConcurrentHashMap<String, Gamer>();
	private static ConcurrentHashMap<String, String> sessionDeviceMap = new ConcurrentHashMap<String, String>();
	private static ConcurrentHashMap<String, String> deviceSessionMap = new ConcurrentHashMap<String, String>();
	public static String getDeviceUID(String sessionId){
		logger.info("当前sessionDeviceMap=" + sessionDeviceMap);
		return sessionDeviceMap.get(sessionId);
	}
	public static String getSessionID(String deviceUID){
		logger.info("当前deviceSessionMap=" + deviceSessionMap);
		return deviceSessionMap.get(deviceUID);
	}
	public static Gamer getGamerByDevice(String deviceUID){
		logger.info("当前deviceGamerMap=" + deviceGamerMap);
		return deviceGamerMap.get(deviceUID);
	}
	public static Gamer getGamerBySession(String sessionId){
		String deviceUID = getDeviceUID(sessionId);
		if(deviceUID == null || deviceUID.trim().isEmpty()){
			return null;
		}
		return deviceGamerMap.get(deviceUID);
	}
	
	
	public static void addSessionDevice(String sessionId, String deviceUID){
		sessionDeviceMap.put(sessionId, deviceUID);
		logger.info("当前sessionDeviceMap=" + sessionDeviceMap);
	}
	public static void addDeviceGamer(String deviceUID, Gamer gamer){
		deviceGamerMap.put(deviceUID, gamer);
		logger.info("当前deviceGamerMap=" + deviceGamerMap);
	}
	public static void addDeviceSession(String deviceUID, String sessionId){
		deviceSessionMap.put(deviceUID, sessionId);
		logger.info("当前deviceSessionMap=" + deviceSessionMap);
	}
	
	public static void remove(String sessionId){
		String deviceUID = sessionDeviceMap.get(sessionId);
		sessionDeviceMap.remove(sessionId);
		deviceGamerMap.remove(deviceUID);
		deviceSessionMap.remove(deviceUID);
		logger.info("设备" + deviceUID + "退出战场，清除session信息");

		logger.info("当前sessionDeviceMap=" + sessionDeviceMap);
		logger.info("当前deviceGamerMap=" + deviceGamerMap);
		logger.info("当前deviceSessionMap=" + deviceSessionMap);
	}
}
