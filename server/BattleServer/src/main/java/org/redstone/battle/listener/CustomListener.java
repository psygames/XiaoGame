package org.redstone.battle.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.redstone.battle.socket.BattleServerSocket;

@WebListener
public class CustomListener implements ServletContextListener {

    public CustomListener() {
        // TODO Auto-generated constructor stub
    }

    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

    public void contextInitialized(ServletContextEvent arg0)  { 
         try {
			Class.forName(BattleServerSocket.class.getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
	
}
