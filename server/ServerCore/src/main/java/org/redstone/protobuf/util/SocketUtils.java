/**  
 * @Title: SocketUtils.java
 * @Package org.redstone.protobuf.util
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月27日
 * @version V1.0  
 */
package org.redstone.protobuf.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

/**
 * @ClassName: SocketUtils
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年11月27日
 *
 */
public class SocketUtils {
	private static Logger logger = Logger.getLogger(SocketUtils.class);
	private static final String LOGID = "【socket】";
	/**
	 * 发送报文
	 * @param msg
	 * @throws Exception 
	 */
	public static String sendMsg(byte[] msg) throws Exception{
		Socket socket = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;
		byte[] b = null;
		String reqStr = null;
		try {
			logger.info(LOGID + "远程ip：192.168.10.107，远程端口: 40000");
			socket = new Socket("192.168.10.107", 40000);
			socket.setSoTimeout(30 * 1000);
			dos = new DataOutputStream(socket.getOutputStream());
			dos.write(msg);
			dos.flush();
			dis = new DataInputStream(socket.getInputStream());
			
			long start1 = System.currentTimeMillis();
			//先取前4个字节。最多等5 * 1000ms。
			while(dis.available() < 4 && (System.currentTimeMillis()-start1) < 5 * 1000){
				Thread.sleep(10);
			}
			if(dis.available()<4){
				throw new SocketTimeoutException(LOGID + "未能读取到报文长度");
			}
			
			byte[] strLen = new byte[4]; // 存放返回报文长度
			int t = dis.read(strLen);
			int length = Integer.parseInt(new String(strLen, "utf-8"));
			logger.info(LOGID + "报文前四个字节表示报文长度" + new String(strLen,  "utf-8" + "====" + t));
			b = new byte[length];
			long start2 = System.currentTimeMillis();
			//判断返回流里面的有效字节，如果等于前面获取到的报文长度，说明对方报文已经都发送到了流里面。这时我们才能读取到完整的报文。最多等5000ms。
			while(dis.available() < length && (System.currentTimeMillis()-start2) < 5000){
				Thread.sleep(10);
			}
			t = dis.read(b);
			reqStr = new String(b, "utf-8");
			logger.info(LOGID + "实际长度" + t + "=====响应报文内容" + reqStr);
			dis.close();
			socket.close();
			dos.close();
		} catch (NumberFormatException e) {
			logger.error(LOGID + "数字格式错误", e);
			throw e;
		} catch (UnknownHostException e) {
			logger.error(LOGID + "未知主机", e);
			throw e;
		} catch (SocketException e) {
			logger.error(LOGID + "等待返回超时60s", e);
			throw e;
		} catch (UnsupportedEncodingException e) {
			logger.error(LOGID + "编码不支持", e);
			throw e;
		} catch (SocketTimeoutException e) {
			throw e;
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					socket = null;
				}
			}
			if (dos != null) {
				dos = null;
			}
			if (dis != null) {
				dis = null;
			}
		}
		return reqStr;
	}
}
