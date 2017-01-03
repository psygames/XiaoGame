using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Security.Cryptography;
public class MD5Check
{
    public static MD5Check GenFromFile(string path)
    {
        MD5Check check = new MD5Check();
        string content = FileOpt.ReadTextFromFile(path);
        if (!string.IsNullOrEmpty(content))
        {
            string[] items = content.Split('\n');
            check.md5s.AddRange(items);
        }
        return check;
    }

    public static string MD5Encrypt(string strText)
    {
        var md5 = new MD5CryptoServiceProvider();
        string t2 = BitConverter.ToString(md5.ComputeHash(Encoding.Default.GetBytes(strText)), 4, 8);
        t2 = t2.Replace("-", "");
        return t2;
    }

    public static string FileMD5(string filePath)
    {
        return MD5Encrypt(FileOpt.ReadTextFromFile(filePath));
    }

    public List<string> md5s = new List<string>();

    public void Save(string path)
    {
        string content = "";
        foreach (string md5 in md5s)
        {
            content += md5 + "\n";
        }
        content.Trim('\n');
        FileOpt.WriteText(content, path);
    }
}
