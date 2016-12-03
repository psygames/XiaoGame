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

import org.redstone.db.model.Gamer;

/**
 * @ClassName: SessionUtils
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月27日
 *
 */
public class SessionUtils {
	private static ConcurrentHashMap<String, Gamer> deviceGamerMap = new ConcurrentHashMap<String, Gamer>();
	private static ConcurrentHashMap<String, String> sessionDeviceMap = new ConcurrentHashMap<String, String>();
	public static String getDevice(String sessionId){
		return sessionDeviceMap.get(sessionId);
	}
	public static String getGamerByDevice(String device){
		return sessionDeviceMap.get(device);
	}
	public static String getGamerBySession(String device){
		return sessionDeviceMap.get(device);
	}
	public static void addSessionDevice(String sessionId, String deviceUID){
		sessionDeviceMap.put(sessionId, deviceUID);
	}
	public static void addDeviceGamer(String sessionId, Gamer deviceUID){
		deviceGamerMap.put(sessionId, deviceUID);
	}
}
