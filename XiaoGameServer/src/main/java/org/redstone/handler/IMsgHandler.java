
    /**  
    * @Title: IMsgHandler.java
    * @Package org.redstone.protobuf.handler
    * @Description: TODO 
    * @author zmxianlin
    * @date 2016年11月24日
    * @version V1.0  
    */
    
package org.redstone.handler;

import java.nio.ByteBuffer;

/**
    * @ClassName: IMsgHandler
    * @Description: TODO 
    * @author zmxianlin
    * @date 2016年11月24日
    *
    */

public interface IMsgHandler {
	public ByteBuffer process(byte[] msgBody);

}
