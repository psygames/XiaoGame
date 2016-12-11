package org.redstone.battle.socket;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.redstone.protobuf.util.SocketUtils;

public class GameServerSocket {
	static{
		init();
	}
	
	public  static void init() {
		new ServerThread().start();
	}
}

class ServerThread extends Thread{
	private static final Logger logger = Logger.getLogger(GameServerSocket.class);
	private static int port = SocketUtils.Game_Server_Port;
	ServerSocket server;
	@Override
	public void run() {
		ExecutorService executorService = Executors.newCachedThreadPool();
		try {
			logger.info("GameServerSocket [" + port + "] started.");
			server = new ServerSocket(port);
			while (true) {
				Socket client;
				client = server.accept();
				logger.info("Client[" + client.getRemoteSocketAddress() + "] connected.");
				ClientRunnable pcr = new ClientRunnable(client);
				executorService.execute(pcr);
			}
		} catch (Exception e) {
			logger.error("BattleServerSocket" + port + "] stoped. exception", e);
		}
	}
}
