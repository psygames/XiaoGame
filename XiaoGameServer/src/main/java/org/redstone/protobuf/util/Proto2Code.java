package org.redstone.protobuf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
    * @ClassName: Proto2Code
    * @Description: TODO 
    * @author zmxianlin
    * @date 2016年11月13日
    *
 */
public class Proto2Code {
	public final static String CLASSPATH = Proto2Code.class.getClassLoader().getResource("").toString().replace("file:/", "");
	public final static String SERVER_BAT_PATH = CLASSPATH + "../../../Tools/Protobuf/serverGen";
	public final static String CLIENT_BAT_PATH = CLASSPATH + "../../../Tools/Protobuf/clientGen";
	public final static String separatorLine = "#####################################";

	/**
	    * @Title: genByBat
	    * @Description: TODO 
	    * @param @param type     
	    * @return void 
	    * @throws
	 */
	public static void genByBat(String type) {
		System.out.println(separatorLine + type.toUpperCase() +"生成START" + separatorLine);
		Process process = null;
		try {
			if("cs".equalsIgnoreCase(type)){
				process = Runtime.getRuntime().exec("cmd.exe /c clientGen.bat", null, new File(CLIENT_BAT_PATH));
			}else if("java".equalsIgnoreCase(type)){
				process = Runtime.getRuntime().exec("cmd.exe /c serverGen.bat", null, new File(SERVER_BAT_PATH));
			}else{
				return;
			}
			// 获取进程的标准输入流
			final InputStream is1 = process.getInputStream();
			// 获取进城的错误流
			final InputStream is2 = process.getErrorStream();
			// 启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
			new Thread() {
				public void run() {
					BufferedReader br1 = null;
					try {
						br1 = new BufferedReader(new InputStreamReader(is1, "gbk"));
						String line1 = null;
						while ((line1 = br1.readLine()) != null && br1 != null) {
							if (line1 != null) {
								System.out.println(line1);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							is1.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();

			new Thread() {
				public void run() {
					BufferedReader br2 = null;
					try {
						br2 = new BufferedReader(new InputStreamReader(is1, "gbk"));
						String line2 = null;
						while ((line2 = br2.readLine()) != null && br2 != null) {
							if (line2 != null) {
								System.out.println(line2);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							is2.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();

			if(process.waitFor() !=0){
				if (process.exitValue() == 1) {//p.exitValue()==0表示正常结束，1：非正常结束     
	                System.err.println("命令执行失败!");  
	                System.exit(1);  
	            } 
			}
			process.destroy();
			System.out.println("运行结束...");
		} catch (Exception e) {
			try {
				process.getErrorStream().close();
				process.getInputStream().close();
				process.getOutputStream().close();
			} catch (Exception ee) {
			}
		}
		System.out.println(separatorLine + type.toUpperCase() +"生成END" + separatorLine);
	}

	public static void main(String[] args) {
//		Proto2Code.genByBat("java");
		//Proto2Code.genByBat("cs");
		CSharpFix.start();
		System.out.println("finished!!!");
	}

}
