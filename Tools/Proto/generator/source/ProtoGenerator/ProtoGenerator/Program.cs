using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using IniTool;
using System.Linq;
using System.Text;

namespace ProtoGenerator
{
    class Program
    {
        const string TOOLS_PATH = @"../../../../../";

        static string GetExePath()
        {
            string exePath = AppDomain.CurrentDomain.SetupInformation.ApplicationBase;
#if DEBUG
            exePath += TOOLS_PATH;
#endif
            return exePath;
        }

        static void Main(string[] args)
        {
            Console.ForegroundColor = ConsoleColor.Green;

            string exePath = GetExePath();
            IniFile ini = new IniFile(exePath + "config.ini");
            string pluginDir = exePath + ini.ReadValue("Path", "PluginDir");
            string pluginExe = exePath + ini.ReadValue("Path", "PluginExe");
            string protoDir = exePath + ini.ReadValue("Path", "ProtoDir");
            string md5Path = exePath + ini.ReadValue("Path", "MD5Path");
            List<string> sharpOutDirs = ini.ReadValues("Path", "SharpOutDir", exePath);
            string protoNum = exePath + ini.ReadValue("Path", "ProtoNum");
            List<string> protoNumOutDirs = ini.ReadValues("Path", "ProtoNumOutDir", exePath);

            // md5 check
            Console.WriteLine("检查MD5");
            List<string> changedProtos = GetChangedProtoPaths(md5Path, protoDir);

            // generator
            Console.WriteLine("生成CS");
            CopyProtosToPluginDir(changedProtos, pluginDir);
            PluginRun(pluginExe, pluginDir);

            // format gen cs files
            Console.WriteLine("格式化CS");
            FormatGenFiles(pluginDir);

            // copy to out dir
            Console.WriteLine("复制CS");
            CopyCsToOutDir(pluginDir, sharpOutDirs);

            // clear plugin dir
            Console.WriteLine("清理CS");
            ClearPluginDir(pluginDir);

            // copy proto num
            Console.WriteLine("复制ProtoNum");
            CopyProtoNum(protoNum, protoNumOutDirs);

            // save md5
            Console.WriteLine("保存MD5");
            SaveMd5s(md5Path, protoDir);

            // end
            Console.WriteLine("生成完成!");
            Console.ForegroundColor = ConsoleColor.White;
            Console.WriteLine("\n按任意键退出...");
            Console.ReadKey();
        }

        static void CopyProtoNum(string scrPath, List<string> destDirs)
        {
            foreach (string destDir in destDirs)
            {
                CopyFile.Copy(scrPath, Path.Combine(destDir, Path.GetFileName(scrPath)));
            }
        }

        static void FormatGenFiles(string pluginDir)
        {
            DirectoryInfo di = new DirectoryInfo(pluginDir);
            FileInfo[] fis = di.GetFiles("*.cs");
            foreach (FileInfo fi in fis)
            {
                Formater.FormatAndSave(fi.FullName, fi.FullName);
            }
        }

        static List<string> GetChangedProtoPaths(string md5Path, string protoDir)
        {
            MD5Check check = MD5Check.GenFromFile(md5Path);
            MD5Check forSave = new MD5Check();

            List<string> paths = new List<string>();

            DirectoryInfo di = new DirectoryInfo(protoDir);
            FileInfo[] fis = di.GetFiles("*.proto");
            foreach (FileInfo fi in fis)
            {
                string md5 = MD5Check.FileMD5(fi.FullName);
                if (!check.md5s.Contains(md5))
                {
                    paths.Add(fi.FullName);
                }
                forSave.md5s.Add(md5);
            }

            return paths;
        }

        static void SaveMd5s(string md5Path, string protoDir)
        {
            MD5Check forSave = new MD5Check();
            DirectoryInfo di = new DirectoryInfo(protoDir);
            FileInfo[] fis = di.GetFiles("*.proto");
            foreach (FileInfo fi in fis)
            {
                string md5 = MD5Check.FileMD5(fi.FullName);
                forSave.md5s.Add(md5);
            }
            forSave.Save(md5Path);
        }

        static void ClearPluginDir(string dir)
        {
            CopyFile.ClearFolder(dir, "*.cs");
            CopyFile.ClearFolder(dir, "*.proto");
        }

        static void CopyProtoToPluginDir(string protoDir, string pluginDir)
        {
            CopyFile.CopyDir(protoDir, pluginDir, "*.proto");
        }

        static void CopyProtosToPluginDir(List<string> protoPaths, string pluginDir)
        {
            foreach (string src in protoPaths)
            {
                CopyFile.Copy(src, Path.Combine(pluginDir, Path.GetFileName(src)));
            }
        }

        static void CopyCsToOutDir(string fromDir, List<string> destDirs)
        {
            for (int i = 0; i < destDirs.Count; i++)
            {
                CopyFile.CopyDir(fromDir, destDirs[i], "*.cs");
            }
        }

        static void PluginRun(string pluginExe, string pluginDir)
        {
            DirectoryInfo di = new DirectoryInfo(pluginDir);
            FileInfo[] fis = di.GetFiles("*.proto");
            foreach (FileInfo fi in fis)
            {
                Console.WriteLine("生成 " + fi.Name);

                ProcessStartInfo info = new ProcessStartInfo();
                info.WorkingDirectory = pluginDir;
                info.FileName = pluginExe;
                info.Arguments = string.Format("-i:{0} -o:{1}.cs", fi.Name, Path.GetFileNameWithoutExtension(fi.Name));
                info.WindowStyle = ProcessWindowStyle.Hidden;
                Process.Start(info).WaitForExit();
            }
        }
    }
}
