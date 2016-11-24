
    /**  
    * @Title: LoginHandler.java
    * @Package org.redstone.handler
    * @Description: TODO 
    * @author zmxianlin
    * @date 2016年11月25日
    * @version V1.0  
    */
    
package org.redstone.handler;

import org.redstone.protobuf.msg.LoginRequest;

import com.google.protobuf.InvalidProtocolBufferException;

/**
    * @ClassName: LoginHandler
    * @Description: TODO 
    * @author zmxianlin
    * @date 2016年11月25日
    *
    */

public class LoginHandler extends MsgHandler implements IMsgHandler{
	@Override
	public void process(byte[] msgBody) {
		try {
			LoginRequest bean = LoginRequest.parseFrom(msgBody);
			logger.info("设备" + bean.getDeviceUID() + "登录");
			logger.info("设备" + bean.getDeviceUID() + "登录");
			logger.info("设备" + bean.getDeviceUID() + "登录");
		} catch (InvalidProtocolBufferException e) {
			logger.error("解析登录请求异常", e);
		}
	}

}
