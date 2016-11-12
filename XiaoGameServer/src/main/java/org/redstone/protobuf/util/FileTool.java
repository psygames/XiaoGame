package org.redstone.protobuf.util;

import java.io.*;

/**
 * Created by yinlong on 16/11/12.
 */
public class FileTool
{
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String DEFULT_ENCODING = "utf8";



    public static void moveFiles(String fromDir,String ext,String destDir)
    {
        
    }

    public static void moveDir(String from, String dest)
    {
        File fromFile = new File(from);

        File destFile = new File(dest);
        if (destFile.exists())
            destFile.delete();
        destFile.mkdirs();

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
        FileOutputStream out = null;
        try
        {
            File file = new File(path);
            if (!file.exists())
                file.createNewFile();
            out = new FileOutputStream(file, append);
            out.write(content.getBytes(encoding));
        } catch (Exception e)
        {
            System.out.println("写入文件内容出错");
            e.printStackTrace();
        } finally
        {
            try
            {
                if (out != null)
                    out.close();
            } catch (Exception e)
            {
            }
        }
    }
}
