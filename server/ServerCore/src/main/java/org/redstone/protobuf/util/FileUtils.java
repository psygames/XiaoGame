/**  
 * @Title: FileUtils.java
 * @Package org.redstone.protobuf.util
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年12月5日
 * @version V1.0  
 */
package org.redstone.protobuf.util;

import java.lang.reflect.Constructor;
import java.util.Properties;

/**
 * @ClassName: FileUtils
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年12月5日
 *
 */
public class FileUtils {
	public <T> T getProperties(String src, String key, T t) throws Exception{
		Properties file = new Properties();
		file.load(FileUtils.class.getClassLoader().getResourceAsStream(src));
		String s = file.getProperty(key);
		Constructor con = t.getClass().getDeclaredConstructor(String.class);
		return (T) con.newInstance(s);
	}
}
