package org.redstone.battle.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

public class BattleServerSocket {
	private static final Logger logger = Logger.getLogger(BattleServerSocket.class);
	static {
		init();
	}
	
	@SuppressWarnings("resource")
	private static void init() {
		// 建新线程的线程池，如果之前构造的线程可用则重用它们
		ExecutorService executorService = Executors.newCachedThreadPool();
		try {
			ServerSocket server = new ServerSocket(8888);
			logger.info("BattleServerSocket [" +8888 + "] started.");
			while (true) {
				Socket client = server.accept();
				logger.info("Client[" + client.getRemoteSocketAddress() + "] connected.");
				ClientRunnable pcr = new ClientRunnable(client);
				executorService.execute(pcr);
			}
		} catch (IOException e) {
			logger.error("BattleServerSocket" + 8888 + "] stoped. exception", e);
		}
	}
}
