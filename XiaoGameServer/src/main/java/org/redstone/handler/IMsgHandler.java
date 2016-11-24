
    /**  
    * @Title: IMsgHandler.java
    * @Package org.redstone.protobuf.handler
    * @Description: TODO 
    * @author zmxianlin
    * @date 2016年11月24日
    * @version V1.0  
    */
    
package org.redstone.handler;

import org.apache.log4j.Logger;
import org.redstone.server.TestServlet;

/**
    * @ClassName: IMsgHandler
    * @Description: TODO 
    * @author zmxianlin
    * @date 2016年11月24日
    *
    */

public interface IMsgHandler {
	public void process(byte[] msgBody);

}
