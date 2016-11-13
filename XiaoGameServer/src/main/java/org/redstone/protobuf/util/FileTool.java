package org.redstone.protobuf.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.commons.io.FileUtils;

/**
 * Created by yinlong on 16/11/12.
 */
public class FileTool
{
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String DEFULT_ENCODING = "utf8";


    public static void clearDir(String dir)
    {
        File destFile = new File(dir);

        if (destFile.exists())
            destFile.delete();
        destFile.mkdirs();

    }

    public static void moveFiles(String fromDir, String destDir, String ext)
    {
        // Clear
        clearDir(destDir);

        // Move
        File fromFile = new File(fromDir);
        for (File f : fromFile.listFiles())
        {
            if (f.getName().endsWith(ext))
            {
            	System.out.println("move file " + fromDir + "/" + f.getName() + " -----> " + destDir + "/" + f.getName() );
                File destFile = new File(destDir + "/" + f.getName());
                f.renameTo(destFile);
                try {
					FileUtils.copyFile(f, destFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
    }


    public static void moveDir(String from, String dest)
    {
        // Clear
        clearDir(dest);

        // Move
        File fromFile = new File(from);
        File destFile = new File(dest);
        fromFile.renameTo(destFile);
    }

    public static String readText(String path)
    {
        return readText(path, DEFULT_ENCODING);
    }

    public static String readText(String filePath, String encoding)
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            File file = new File(filePath);
            if (file.isFile() && file.exists())
            {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null)
                {
                    sb.append(lineTxt);
                    sb.append("\r\n");
                }
                read.close();
            } else
            {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e)
        {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static void writeText(String path, String content)
    {
        writeText(path, content, DEFULT_ENCODING, false);
    }

    public static void writeText(String path, String content, String encoding, boolean append)
    {
    	BufferedWriter wr = null;
        try
        {
            File file = new File(path);
            if (!file.exists())
                file.createNewFile();
            wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), encoding));
            wr.write(content);
            wr.flush();
        } catch (Exception e)
        {
            System.out.println("写入文件内容出错");
            e.printStackTrace();
        } finally
        {
            try
            {
                if (wr != null)
                    wr.close();
            } catch (Exception e)
            {
            }
        }
    }
}
