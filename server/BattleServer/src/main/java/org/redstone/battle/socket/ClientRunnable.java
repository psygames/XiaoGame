package org.redstone.battle.socket;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.log4j.Logger;
import org.redstone.handler.IMsgHandler;
import org.redstone.protobuf.util.DataUtils;
import org.redstone.protobuf.util.HandlerUtils;

public class ClientRunnable implements Runnable, Serializable {
	private static final long serialVersionUID = 1L;
	private static final int timeOut = 20 * 1000;

	private static Logger log = Logger.getLogger(ClientRunnable.class);
	
	private Socket client;
	public ClientRunnable(Socket client) {
		this.client = client;
	}
	
	public void run() {
		DataInputStream dis;
		DataOutputStream dos;
		try {
			client.setSoTimeout(timeOut);
			dis = new DataInputStream(client.getInputStream());
			dos = new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			log.error("Client"+client.getRemoteSocketAddress()+" Set timeout exception. timeout="+ timeOut,e);
			return;
		}
		while (true) {
			// 请求数据
			byte[] reqData = null;
			try {
				// 接收报文长度
				long start1 = System.currentTimeMillis();
				//先取前4个字节。最多等3 * 1000ms。
				while(dis.available() < 4 && (System.currentTimeMillis()-start1) < 3 * 1000){
					Thread.sleep(10);
				}
				if(dis.available()<4){
					throw new SocketTimeoutException("未能读取到报文长度");
				}
				byte[] strLen = new byte[2]; // 存放返回报文长度
				int t = dis.read(strLen);
				// 接收报文
				
				Short length = DataUtils.byteArray2T(strLen, Short.class);
				log.info("报文前2个字节表示报文长度" + new String(length + ""));
				reqData = new byte[length];
				long start2 = System.currentTimeMillis();
				//判断返回流里面的有效字节，如果等于前面获取到的报文长度，说明对方报文已经都发送到了流里面。这时我们才能读取到完整的报文。最多等5000ms。
				while(dis.available() < length && (System.currentTimeMillis()-start2) < 5000){
					Thread.sleep(10);
				}
				t = dis.read(reqData);
				log.info("实际长度" + t);
				
			} catch(Exception ee) {
				log.debug("Client"+client.getRemoteSocketAddress()+" recv data end.", ee);
				break;
			} 
			// 执行业务处理
			String reqStr = null;
			ByteBuffer sendBuff = null;
			try {
				reqStr = new String(reqData,"utf-8");
				Map<String,Object> jsonToMap = DataUtils.jsonToMap(reqStr);
				short type = Short.parseShort((String) jsonToMap.get("msgType"));
				IMsgHandler handler = HandlerUtils.getInstance().getHandler(type);
				log.info("msgType=" + type + " handler=" + handler.getClass().getName() + " method=processSocket jsondata=" + jsonToMap);
				sendBuff = handler.processSocket(jsonToMap);
			} catch (Exception e) {
				log.error(e);
			}
			
			// 返回业务处理结果
			try {
				// 发送
				sendBuff.flip();
				dos.write(sendBuff.array());
				dos.flush();
			} catch(IOException e) {
				log.error("Client"+client.getRemoteSocketAddress()+" send data exception.",e);
				break;
			}
		}
		// 关闭
		try {
			// 关闭连接
			dis.close();
			dos.close();
			client.close();
		} catch (IOException e) {
			log.error("Close socket exception.",e);
			return;
		}
		log.info("Closed client"+client.getRemoteSocketAddress());
	}

}
