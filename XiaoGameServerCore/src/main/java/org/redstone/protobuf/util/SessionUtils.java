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

/**
 * @ClassName: SessionUtils
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月27日
 *
 */
public class SessionUtils {
	private static ConcurrentHashMap<String, String> sessionMap = new ConcurrentHashMap<String, String>();
	public static String getDeviceUID(String sessionId){
		return sessionMap.get(sessionId);
	}
	public static String addDeviceUID(String sessionId, String deviceUID){
		return sessionMap.put(sessionId, deviceUID);
	}
}
