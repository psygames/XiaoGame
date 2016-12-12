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
import java.util.Map;

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
	
	public static String Game_Server_ip = "127.0.0.1";
	public static int Game_Server_Port = 8889;
	
	public static String Battle_Server_ip = "127.0.0.1";
	public static int Battle_Server_Port = 8888;
	
	public static void main(String[] args) throws Exception {
		sendMsg(DataUtils.number2Bytes(3), Battle_Server_ip, Battle_Server_Port, false);
	}
	/**
	 * 发送报文
	 * @param msg
	 * @throws Exception 
	 */
	public static byte[] sendMsg(byte[] msg, String ip, int port, boolean isRtn) throws Exception{
		Socket socket = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;
		byte[] b = null;
		try {
			logger.info(LOGID + "远程ip：139.196.5.96，远程端口: " + port);
			socket = new Socket(ip, port);
			socket.setSoTimeout(10 * 1000);
			dos = new DataOutputStream(socket.getOutputStream());
			dos.write(msg);
			dos.flush();
			
			if(!isRtn){//是否需要返回值
				return null;
			}
			dis = new DataInputStream(socket.getInputStream());
			//-------------------------------------读取报文长度--------------------------------------
			long start1 = System.currentTimeMillis();
			//先取前2个字节。最多等5 * 1000ms。
			while(dis.available() < 2 && (System.currentTimeMillis()-start1) < 5 * 1000){
				Thread.sleep(10);
			}
			if(dis.available() < 2){
				throw new SocketTimeoutException(LOGID + "未能读取到报文长度");
			}
			
			byte[] strLen = new byte[2]; // 存放返回报文长度
			int t = dis.read(strLen);
			short length = DataUtils.byteArray2T(strLen, Short.class);
			logger.info(LOGID + "报文前2个字节表示报文长度" + new String(length + ""));
			
			if(length == 0){
				return null;
			}
			
			//-------------------------------------读取报文内容--------------------------------------
			b = new byte[length];
			long start2 = System.currentTimeMillis();
			//判断返回流里面的有效字节，如果等于前面获取到的报文长度，说明对方报文已经都发送到了流里面。这时我们才能读取到完整的报文。最多等5000ms。
			while(dis.available() < length && (System.currentTimeMillis()-start2) < 5 * 1000){
				Thread.sleep(10);
			}
			t = dis.read(b);
			logger.info(LOGID + "实际长度" + t);
		} catch (NumberFormatException e) {
			logger.error(LOGID + "数字格式错误", e);
			throw e;
		} catch (UnknownHostException e) {
			logger.error(LOGID + "未知主机", e);
			throw e;
		} catch (SocketException e) {
			logger.error(LOGID + "连接失败", e);
			throw e;
		} catch (UnsupportedEncodingException e) {
			logger.error(LOGID + "编码不支持", e);
			throw e;
		} catch (SocketTimeoutException e) {
			logger.error(LOGID + "等待返回超时10s", e);
			throw e;
		} finally {
			if(dis != null){
				dis.close();
			}
			if(socket != null){
				socket.close();
			}
			if(dos != null){
				dos.close();
			}
		}
		return b;
	}
}
