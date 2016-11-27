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
			new Thread(new StreamClear(is1, process)).start();
			new Thread(new StreamClear(is2, process)).start();
			process.waitFor();
			System.out.println("运行结束...");
		} catch (Exception e) {
			try {
				process.getErrorStream().close();
				process.getInputStream().close();
				process.getOutputStream().close();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}finally{
			if(process != null){
				process.destroy();
			}
		}
		System.out.println(separatorLine + type.toUpperCase() +"生成END" + separatorLine);
	}
	
	public static void main(String[] args) {
		Proto2Code.genByBat("java");
		Proto2Code.genByBat("cs");
		CSharpFix.start();
		System.out.println("finished!!!");
	}

}

class StreamClear implements Runnable{
	private InputStream stream;
	private Process process;
	public StreamClear(InputStream stream, Process process){
		this.stream = stream;
		this.process = process;
	}
	@Override
	public void run() {
		BufferedReader br2 = null;
		try {
			br2 = new BufferedReader(new InputStreamReader(stream, "gbk"));
			String line2 = null;
			while ((line2 = br2.readLine()) != null && br2 != null) {
				if (line2 != null) {
					System.out.println(line2);
					if(line2.equals("协议生成完毕")){
						//process.destroy();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
