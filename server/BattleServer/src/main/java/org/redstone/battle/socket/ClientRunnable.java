package org.redstone.battle.socket;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
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
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				// 接收报文长度
				int dataLen = dis.readShort();
				// 接收报文
				byte[] buff = new byte[1024];
				int totalLen = 0;
				do {
					int recvLen = dis.read(buff);
					baos.write(buff, 0, recvLen);
					totalLen += recvLen;
				} while(totalLen<dataLen);
			} catch(EOFException ee) {
				log.debug("Client"+client.getRemoteSocketAddress()+" recv data end.", ee);
				break;
			} catch(IOException e) {
				log.error("Client"+client.getRemoteSocketAddress()+" recv data exception.", e);
				break;
			}
			// 请求数据
			byte[] reqData = baos.toByteArray();
			// 执行业务处理
			String reqStr = null;
			byte[] resData = null;
			try {
				reqStr = new String(reqData,"utf-8");
				Map<String,Object> jsonToMap = DataUtils.jsonToMap(reqStr);
				short type = (Short)jsonToMap.get("msgType");
				IMsgHandler handler = HandlerUtils.getInstance().getHandler(type);
				log.info("msgType=" + type + " handler=" + handler.getClass().getName() + " method=processSocket jsondata=" + jsonToMap);
				ByteBuffer sendBuff = handler.processSocket(jsonToMap);
				sendBuff.flip();
				resData = sendBuff.array();
			} catch (Exception e) {
				log.error(e);
			}
			
			// 返回业务处理结果
			try {
				ByteArrayOutputStream resBaos = new ByteArrayOutputStream();
				DataOutputStream resDos = new DataOutputStream(resBaos);
				// 报文头 2字节
				resDos.writeShort(resData.length);
				// 报文
				resDos.write(resData);
				// 发送
				dos.write(resBaos.toByteArray());
				dos.flush();
			} catch(IOException e) {
				log.error("Client"+client.getRemoteSocketAddress()+" send data exception.",e);
				break;
			}
		}
		// 等待2秒
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
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
