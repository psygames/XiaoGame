package org.redstone.protobuf.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.redstone.handler.IMsgHandler;

/**
 * 
 * @ClassName: HandlerUtils
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月25日
 *
 */
public class HandlerUtils {
	private static final Logger logger = Logger.getLogger(HandlerUtils.class);
	private static Map<Short, String> handlerMap = new HashMap<Short, String>();
	
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
		for(MsgType msg : MsgType.values()){
			if(!msg.getClsName().toLowerCase().contains("reply")){
				handlerMap.put(msg.getMsgType(), "org.redstone.handler." + msg.getClsName() + "Handler");
			}
		}
	}
	
	/**
	 * @Title: get
	 * @Description: 根据消息类型得到处理类名称
	 * @param @param msgType
	 * @param @return     
	 * @return String 
	 * @throws
	 */
	public  String getHandlerName(short msgType){
		if(handlerMap == null || !handlerMap.containsKey(msgType)){
			refresh();
			if(!handlerMap.containsKey(msgType)){
				logger.error("未找到消息类型" + msgType + "对应的处理类");
				return null;
			}
		}
		return handlerMap.get(msgType);
	}
	
	public  IMsgHandler getHandler(short msgType){
		String name = getHandlerName(msgType);
		try {
			IMsgHandler handler = (IMsgHandler) Class.forName(name).newInstance();
			return handler;
		} catch (Exception e) {
			logger.error("实例化" + name + "失败", e);
		}
		return null;
	}
}
