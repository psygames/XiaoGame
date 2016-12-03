/**  
 * @Title: GomokuService.java
 * @Package org.redstone.service
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年12月3日
 * @version V1.0  
 */
package org.redstone.service;

import org.redstone.protobuf.msg.Enums.ChessType;

/**
 * @ClassName: GomokuService
 * @Description: TODO 
 * @author zmxianlin
 * @date 2016年12月3日
 *
 */
public class GomokuService {
	private static int num = 5;
	public static boolean check(int x, int y, ChessType chessType, int boardMaxX, int boardMaxY){
		//横向
		int maxX = x + num -1 <= boardMaxX ? x + num -1 : boardMaxX;
		int minX = x - num -1 >= 0 ? x - num -1 : 0;
		int xOffset = 1;
		int maxY = y;
		int minY = y;
		int yOffset = 0;
		if(fiveCheck(maxX, minX, xOffset, maxY, minY, yOffset, chessType)){
			return true;
		}
		
		//纵向
		maxX = x;
		minX = x;
		xOffset = 0;
		maxY = y + num -1 <= boardMaxY ? y + num -1 : boardMaxY;
		minY = y - num -1 >= 0 ? y - num -1 : 0;
		yOffset = 1;
		if(fiveCheck(maxX, minX, xOffset, maxY, minY, yOffset, chessType)){
			return true;
		}
		
		//东北-西南
		maxX = x + num -1 <= boardMaxX ? x + num -1 : boardMaxX;
		minX = x - num -1 >= 0 ? x - num -1 : 0;
		xOffset = 1;
		maxY = y + num -1 <= boardMaxY ? y + num -1 : boardMaxY;
		minY = y - num -1 >= 0 ? y - num -1 : 0;
		yOffset = 1;
		if(fiveCheck(maxX, minX, xOffset, maxY, minY, yOffset, chessType)){
			return true;
		}
		
		//西北-东南
		maxX = x + num -1 <= boardMaxX ? x + num -1 : boardMaxX;
		minX = x - num -1 >= 0 ? x - num -1 : 0;
		xOffset = 1;
		maxY = y + num -1 <= boardMaxY ? y + num -1 : boardMaxY;
		minY = y + num -1 <= boardMaxY ? y + num -1 : boardMaxY;
		yOffset = -1;
		if(fiveCheck(maxX, minX, xOffset, maxY, minY, yOffset, chessType)){
			return true;
		}
		
		return false;
	}
	
	
	public static boolean fiveCheck(int maxX, int minX, int xOffset, int maxY, int minY, int yOffset, ChessType chessType){
		int y = minY;
		int count = 0;
		ChessType[][] cts = new ChessType[12][12];
		for(int x = minX; x <= maxX; x += xOffset){
			//判断x，y点是否是要求的chessType，并计数。如果达到了num个返回true
			if(cts[x][y].compareTo(chessType) != 0){
				count = 0;
			}else{
				count ++;
				if(count == num){
					return true;
				}
			}
			
			//如果递增的方向不可能再组成 num个连续的子，则直接返回false
			if(xOffset != 0 && maxX - x + count < num){
				return false;
			}
			if(yOffset != 0 && maxY - y + count < num){
				return false;
			}
			
			
			//y递增
			if(y < maxY){
				y += yOffset;
				continue;
			}
		}
		return false;
	}
}
