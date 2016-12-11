/**  
 * @Title: GomokuService.java
 * @Package org.redstone.service
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年12月3日
 * @version V1.0  
 */
package org.redstone.service;

import org.apache.log4j.Logger;
import org.redstone.protobuf.msg.Enums.ChessType;


/**
 * @ClassName: GomokuService
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年12月3日
 *
 */
public class GomokuService {
	private static Logger logger = Logger.getLogger(GomokuService.class);
	private static int num = 5;
	public static boolean check(int x, int y, ChessType chessType, int boardMaxX, int boardMaxY, ChessType[][] cts){
		boardMaxX = boardMaxX -1;
		boardMaxY = boardMaxY -1;
		
		//横向
		int endX = x + num - 1 <= boardMaxX ? x + num -1 : boardMaxX;
		int startX = x - num + 1 >= 0 ? x - num + 1 : 0;
		int xOffset = 1;
		int endY = y;
		int startY = y;
		int yOffset = 0;
		if(fiveCheck(endX, startX, xOffset, endY, startY, yOffset, chessType, cts)){
			return true;
		}
		
		//纵向
		endX = x;
		startX = x;
		xOffset = 0;
		endY = y + num -1 <= boardMaxY ? y + num -1 : boardMaxY;
		startY = y - num + 1 >= 0 ? y - num + 1 : 0;
		yOffset = 1;
		if(fiveCheck(endX, startX, xOffset, endY, startY, yOffset, chessType, cts)){
			return true;
		}
		
		//东北-西南
		endX = x + num -1 <= boardMaxX ? x + num -1 : boardMaxX;
		startX = x - num + 1 >= 0 ? x - num + 1 : 0;
		xOffset = 1;
		endY = y + num -1 <= boardMaxY ? y + num -1 : boardMaxY;
		startY = y - num + 1 >= 0 ? y - num + 1 : 0;
		yOffset = 1;
		if(fiveCheck(endX, startX, xOffset, endY, startY, yOffset, chessType, cts)){
			return true;
		}
		
		//西北-东南
		endX = x + num -1 <= boardMaxX ? x + num -1 : boardMaxX;
		startX = x - num + 1 >= 0 ? x - num + 1 : 0;
		xOffset = 1;
		endY = y - num + 1 >= 0 ? y - num + 1 : 0;
		startY = y + num - 1 <= boardMaxY ? y + num - 1 : boardMaxY;
		yOffset = -1;
		if(fiveCheck(endX, startX, xOffset, endY, startY, yOffset, chessType, cts)){
			return true;
		}
		
		return false;
	}
	
	public static int getMin(int p, int n){
		return p - n + 1 >= 0 ? p - n + 1 : 0;
	}
	public static int getMax(int p, int n, int max){
		return p + n -1 <= max ? p + n -1 : max;
	}
	
	
	public static boolean fiveCheck(int endX, int startX, int xOffset, int endY, int startY, int yOffset, ChessType chessType, ChessType[][] cts){
		logger.info("maxX=" + endX + ",maxY=" + endY + ",xOffset=" + xOffset + ",maxY=" + endY + ",minY=" + startY
				+ ",yOffset=" + yOffset + ",chessType=" + chessType);
		int y = startY;
		int count = 0;
		for(int x = startX; x <= endX; x += xOffset){
			//判断x，y点是否是要求的chessType，并计数。如果达到了num个返回true
			if(!cts[x][y].equals(chessType)){
				count = 0;
			}else{
				count ++;
				if(count == num){
					return true;
				}
			}
			
			//如果递增的方向不可能再组成 num个连续的子，则直接返回false
			if(xOffset != 0 && endX - x + count < num){
				return false;
			}
			if(yOffset != 0 && endY - y + count < num){
				return false;
			}
			
			
			//y递增
			if(yOffset < 0){
				if(y >= endY){
					y += yOffset;
					continue;
				}else{
					return false;
				}
			}else{
				if(y <= endY){
					y += yOffset;
					continue;
				}else{
					return false;
				}
			}
		}
		return false;
	}
}
