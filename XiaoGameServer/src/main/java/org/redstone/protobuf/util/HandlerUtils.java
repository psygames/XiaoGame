package org.redstone.protobuf.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.redstone.handler.IMsgHandler;
import org.redstone.server.TestServlet;

/**
 * 
 * @ClassName: HandlerUtils
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月25日
 *
 */
public class HandlerUtils {
	private static final Logger logger = Logger.getLogger(TestServlet.class);
	private static Map<Integer, String> handlerMap = new HashMap<Integer, String>();
	
	static{
		refresh();
	}
	
	private HandlerUtils(){}
	
	public static HandlerUtils getInstance(){
		return SingleTonFactory.handlerUtils;
	}
	
	private static class SingleTonFactory {
		private static HandlerUtils handlerUtils;
		static{
			handlerUtils = new HandlerUtils();
		}
	}
	
	/**
	 * @Title: refresh
	 * @Description: 刷新消息处理类hash表  
	 * @param      
	 * @return void 
	 * @throws
	 */
	public static void refresh(){
		handlerMap.put(1001, "org.redstone.handler.LoginHandler");
		handlerMap.put(1003, "org.redstone.handler.JoinBattleHandler");
	}
	
	/**
	 * @Title: get
	 * @Description: 根据消息类型得到处理类名称
	 * @param @param msgType
	 * @param @return     
	 * @return String 
	 * @throws
	 */
	public  String getHandlerName(int msgType){
		if(handlerMap == null || !handlerMap.containsKey(msgType)){
			refresh();
			if(!handlerMap.containsKey(msgType)){
				logger.error("未找到消息类型" + msgType + "对应的处理类");
				return null;
			}
		}
		return handlerMap.get(msgType);
	}
	
	public  IMsgHandler getHandler(int msgType){
		String name = getHandlerName(msgType);
		try {
			IMsgHandler handler = (IMsgHandler) Class.forName(name).newInstance();
			return handler;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			logger.error("实例化" + name + "失败", e);
		}
		return null;
	}
}
