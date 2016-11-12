package org.redstone.protobuf.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yinlong on 16/11/12.
 */
public class CSharpFix
{
    public final String fromDir = "xxx";
    public final String destDir = "xxx";

    public final Map<String, String> replaceMembs = new HashMap<String, String>()
    {{
        put("namespace [\\s\\S]*?\\n", "namespace message\\r\\n");
    }};

    public static void start()
    {
        new CSharpFix().execute();
    }

    private void execute()
    {
        // Move To Dest Directory
        FileTool.moveFiles(fromDir, destDir, ".cs");


        // Format CS File
        String content = FileTool.readText(destDir);
        content = format(content);
        FileTool.writeText(destDir, content);
    }

    private String format(String content)
    {
        for (Map.Entry<String, String> kv : replaceMembs.entrySet())
        {
            content = content.replaceAll(kv.getKey(), kv.getValue());
        }

        return content;
    }

}
