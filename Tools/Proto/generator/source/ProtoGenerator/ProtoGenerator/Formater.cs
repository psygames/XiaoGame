using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

public class Formater
{
    public static readonly Dictionary<String, String> replaceMembs = new Dictionary<String, String>()
    {
        { "namespace [\\s\\S]*?\\r\\n", "namespace message\r\n" },
        { "org.redstone.protobuf.msg.", "message." },
    };

    public static string FormatContent(string content)
    {
        foreach (var kv in replaceMembs)
        {
            content = Regex.Replace(content, kv.Key, kv.Value);
        }
        return content;
    }

    public static string Format(string path)
    {
        return FormatContent(FileOpt.ReadTextFromFile(path));
    }

    public static void FormatAndSave(string srcPath, string savePath)
    {
        string content = Format(srcPath);
        FileOpt.WriteText(content, savePath);
    }
}
