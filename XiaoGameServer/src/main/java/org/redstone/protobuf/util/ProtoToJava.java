package org.redstone.protobuf.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProtoToJava {

	public static void genByBat() {
		String batPath = System.getenv("git_path");
		String command = "cmd.exe /c  " + batPath + "/serverGen.bat";
		Process process = null;
		try {
			System.out.println(command);
			process = Runtime.getRuntime().exec(command);

			// 获取进程的标准输入流
			final InputStream is1 = process.getInputStream();
			// 获取进城的错误流
			final InputStream is2 = process.getErrorStream();
			// 启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
			new Thread() {
				public void run() {
					BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
					try {
						String line1 = null;
						while ((line1 = br1.readLine()) != null) {
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
					BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
					try {
						String line2 = null;
						while ((line2 = br2.readLine()) != null) {
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
	}

	public static void main(String[] args) {
		ProtoToJava.genByBat();
		//System.out.println(System.getProperty("user.dir"));
	}

}
