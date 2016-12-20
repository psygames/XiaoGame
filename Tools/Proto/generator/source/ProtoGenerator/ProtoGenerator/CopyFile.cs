using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

class CopyFile
{
    public static void ClearFolder(string folderPath, string filter)
    {
        Console.WriteLine("清空目录 ==> " + folderPath);
        DirectoryInfo di = new DirectoryInfo(folderPath);
        if (di.Exists)
        {
            FileInfo[] fi = di.GetFiles(filter);
            for (int i = 0; i < fi.Length; i++)
            {
                fi[i].Delete();
            }
        }

        Console.WriteLine("clear folder succeed :" + folderPath);
    }

    public static void CopyDir(string fromDir, string destDir, string filter)
    {
        DirectoryInfo di = new DirectoryInfo(fromDir);
        if (di.Exists)
        {
            FileInfo[] fi = di.GetFiles(filter);
            for (int i = 0; i < fi.Length; i++)
            {
                Copy(fi[i].FullName, Path.Combine(destDir, fi[i].Name));
            }
        }
    }

    public static bool Copy(string pathSrc, string pathDest)
    {
        try
        {
            CheckAndCreate(pathDest);
            File.Copy(pathSrc, pathDest, true);
            return true;
        }
        catch (Exception e)
        {
            Console.WriteLine(e.Message);
        }
        return false;
    }

    public static void CheckAndCreate(string path)
    {
        if (!File.Exists(path))
        {
            if (!Directory.Exists(Path.GetDirectoryName(path)))
                Directory.CreateDirectory(Path.GetDirectoryName(path));
            File.Create(path).Close();
        }
    }

}
