package org.redstone.protobuf.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yinlong on 16/11/12.
 */
public class CSharpFix {
	public final String CLASSPATH = this.getClass().getClassLoader().getResource("").toString().replace("file:/", "");
	public final String FROM_DIR = CLASSPATH + "../../../Tools/Protobuf/clientGen";
	public final String DEST_DIR = CLASSPATH + "../../../XiaoGameClient/Assets/Scripts/Network/Protocol";

	public final Map<String, String> replaceMembs = new HashMap<String, String>() {
		private static final long serialVersionUID = -3582164660929036163L;
		{
			put("namespace [\\s\\S]*?\\r\\n", "namespace message\r\n");
			put("org.redstone.protobuf.msg.", "message.");
		}
	};


	public static void start() {
		new CSharpFix().execute();
	}
	public static void main(String[] args) {
		CSharpFix.start();
	}

	private void execute() {
		// Move To Dest Directory
		System.out.println("move...");
		FileTool.moveFiles(FROM_DIR, DEST_DIR, ".cs");

		System.out.println("Format...");
		// Format CS File
		this.formatFiles(DEST_DIR);
	}

	private void formatFiles(String destDir)
	{
		File destFile = new File(destDir);
		for (File f : destFile.listFiles())
        {
			String content = FileTool.readText(f.getPath());
			content = format(content);
			FileTool.writeText(destDir + "/" + f.getName(), content);
        }
	}


	private String format(String content) {
		for (Map.Entry<String, String> kv : replaceMembs.entrySet()) {
			content = content.replaceAll(kv.getKey(), kv.getValue());
		}
		return content;
	}

}
