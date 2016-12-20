using System;
using System.Collections.Generic;
using System.Diagnostics;
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
            string exePath = GetExePath();
            IniFiles ini = new IniFiles(exePath + "config.ini");
            string pluginDir = exePath + ini.ReadValue("Path", "PluginDir");
            string pluginExe = exePath + ini.ReadValue("Path", "PluginExe");
            string protoDir = exePath + ini.ReadValue("Path", "ProtoDir");
            List<string> sharpOutDirs = ini.ReadValues("Path", "SharpOutDir", exePath);

            CopyProtoToPluginDir(protoDir, pluginDir);
            PluginRun(pluginExe, pluginDir);
            CopyCsToOutDir(pluginDir, sharpOutDirs);
            ClearPluginDir(pluginDir);
            Console.WriteLine("按任意键退出...");
            Console.ReadKey();
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

        static void CopyCsToOutDir(string fromDir, List<string> destDirs)
        {
            for (int i = 0; i < destDirs.Count; i++)
            {
                CopyFile.CopyDir(fromDir, destDirs[i], "*.cs");
            }
        }

        static void PluginRun(string pluginExe, string pluginDir)
        {
            ProcessStartInfo info = new ProcessStartInfo();
            info.WorkingDirectory = pluginDir;
            info.FileName = pluginExe;
            Process.Start(info).WaitForExit();
        }
    }
}
