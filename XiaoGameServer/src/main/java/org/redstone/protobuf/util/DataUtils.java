/**  
 * @Title: DataUtils.java
 * @Package org.redstone.protobuf.util
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月26日
 * @version V1.0  
 */
package org.redstone.protobuf.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @ClassName: DataUtils
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月26日
 *
 */
public class DataUtils {
	private static DataUtils dataUtils;
	public static DataUtils getInstance(){
		if(dataUtils == null){
			dataUtils = new DataUtils();
		}
		return dataUtils;
	}


	public static <T> T byteArray2T(byte[] bytes, Class<T> cls) {
		try {
			Long tmp = 0l;
			// 由低位到高位
			for (int i = 0; i < bytes.length; i++) {
				int shift = i * 8;
				tmp |= (bytes[i] & 0xff) << shift;
			}
			Constructor<T> con = cls.getDeclaredConstructor(String.class);
			return con.newInstance(tmp + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] numberToBytes(Object obj) {
		try {
			Method md = obj.getClass().getMethod("longValue");
			Long tmp = (Long) md.invoke(obj);
			Field field = obj.getClass().getField("BYTES");
			byte[] bs = new byte[field.getInt(obj)];
			for (int i = 0; i < bs.length; i++) {
				bs[i] = new Long((tmp & 0xff)).byteValue();// 低位到高位
				tmp = tmp >> 8;
			}
			return bs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		for (int i = 128; i < 129; i++) {
			byte[] b = numberToBytes(i);
			Integer it = byteArray2T(b, Integer.class);
			if (it != i) {
				System.out.println(it + "  " + i);
			}
		}
	}
}
