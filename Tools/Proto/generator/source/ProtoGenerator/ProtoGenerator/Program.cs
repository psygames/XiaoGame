using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using IniTool;
using System.Linq;
using System.Text;
using System.Net;
using ICSharpCode.SharpZipLib.Zip;
using System.Threading;

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
            string useRemotePlugin = ini.ReadValue("Path", "UseRemotePlugin");
            string remoteUrl = ini.ReadValue("Path", "RemoteUrl");
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
            if (!bool.Parse(useRemotePlugin))
            {
                PluginRun(pluginExe, pluginDir);
            }
            else
            {
                RemotePlguninRun(remoteUrl, protoDir, pluginDir);
            }

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
            CopyFile.ClearFolder(dir, "*.zip");
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

        static void RemotePlguninRun(string remoteUrl, string protoDir, string pluginDir)
        {
            DirectoryInfo di = new DirectoryInfo(pluginDir);
            FileInfo[] fis = di.GetFiles("*.proto");
            string url = remoteUrl + "?type=zip&out_type=file";
            AsyncUpDownloadFileInit(); // async remote generate need call init
            int count = 1;
            foreach (FileInfo fi in fis)
            {
                string shortname = Path.GetFileNameWithoutExtension(fi.FullName);
                string out_file = Path.GetDirectoryName(fi.FullName);
                out_file += "/" + shortname + ".cs";
                string tmp_url = url + "&filename=" + shortname;
                string zipFile = pluginDir + "protos_" + fi.GetHashCode() + ".zip";
                int improtcount = 0;
                float zipsize = 0;
                ZipFileByRule(protoDir, zipFile, shortname + ".proto", out zipsize, out improtcount);
                AsyncUpDownloadFile(tmp_url, zipFile, out_file);

                Console.WriteLine("网络生成 {0}. zip({2:F2}kb) impt({3})  {1}"
                    , count++, fi.Name, zipsize, improtcount);
            }
            WaitForAsyncUpDownloadDone(); // async remote generate need call wait for done
        }

        const int maxAsyncThreads = 6;
        const int mainThreadWaitForAsyncTime = 10;
        static void AsyncUpDownloadFileInit()
        {
            ThreadPool.SetMaxThreads(maxAsyncThreads, maxAsyncThreads);
            ThreadPool.SetMinThreads(maxAsyncThreads, maxAsyncThreads);
        }

        static void AsyncUpDownloadFile(string url, string srcpath, string savepath)
        {
            AsyncUpDownLoadParams apms = new AsyncUpDownLoadParams(url, srcpath, savepath);
            while (!HasReadyThread())
            {
                Thread.Sleep(mainThreadWaitForAsyncTime);
            }
            ThreadPool.QueueUserWorkItem(AsyncUpDownLoadFunc, apms);
        }

        static int GetActiveThreadCount()
        {
            int works = 0;
            int maxWorks = 0;
            int ports = 0;
            ThreadPool.GetAvailableThreads(out works, out ports);
            ThreadPool.GetMaxThreads(out maxWorks, out ports);
            return maxWorks - works;
        }

        static bool HasReadyThread()
        {
            return GetActiveThreadCount() < maxAsyncThreads;
        }

        static void WaitForAsyncUpDownloadDone()
        {
            int works = 0;
            int readys = 0;
            while (true)
            {
                ThreadPool.GetAvailableThreads(out works, out readys);
                if (GetActiveThreadCount() <= 0)
                    break;
                Thread.Sleep(mainThreadWaitForAsyncTime);
            }
        }

        class AsyncUpDownLoadParams
        {
            public string url;
            public string src;
            public string save;
            public AsyncUpDownLoadParams(string url, string src, string save)
            {
                this.url = url;
                this.src = src;
                this.save = save;
            }
        }

        static void AsyncUpDownLoadFunc(object asyncParams)
        {
            AsyncUpDownLoadParams pms = asyncParams as AsyncUpDownLoadParams;
            HttpUploadAndDownloadFile(pms.url, pms.src, pms.save);
        }

        static void ZipFileByRule(string protoDir, string outFile, string curProtoName, out float zipsize, out int importcount)
        {
            List<string> imports = LoopImportFiles(protoDir, curProtoName);
            for (int i = 0; i < imports.Count; i++)
                imports[i] = Path.Combine(protoDir, imports[i]);
            CreateZipFile(imports.ToArray(), outFile);
            zipsize = new FileInfo(outFile).Length / 1024f;
            importcount = imports.Count - 1;
        }

        static List<string> LoopImportFiles(string dir, string protoName)
        {
            List<string> imports = new List<string>();
            imports.Add(protoName);
            LoopImportSt(imports, dir, protoName);
            return imports;
        }

        static void LoopImportSt(List<string> imports, string dir, string protoName)
        {
            for (int i = 0; i < imports.Count; i++)
            {
                List<string> subImports = GetImportProtoNames(dir, imports[i]);
                foreach (string sub in subImports)
                {
                    if (!imports.Contains(sub))
                    {
                        imports.Add(sub);
                        LoopImportSt(imports, dir, sub);
                    }
                }
            }
        }

        static List<string> GetImportProtoNames(string dir, string protoName)
        {
            List<string> imports = new List<string>();
            string text = FileOpt.ReadTextFromFile(Path.Combine(dir, protoName));
            string[] lines = text.Split('\n');
            foreach (string line in lines)
            {
                string str = line.Trim();
                if (str.StartsWith("import"))
                {
                    int st = str.IndexOf("\"") + 1;
                    int ed = str.IndexOf("\"", st);
                    string importName = str.Substring(st, ed - st);
                    imports.Add(importName);
                }
            }
            return imports;
        }



        private static void CreateZipFile(string[] filenames, string zipFilePath)
        {
            try
            {
                using (ZipOutputStream s = new ZipOutputStream(File.Create(zipFilePath)))
                {
                    s.SetLevel(5); // 压缩级别 0-9
                    byte[] buffer = new byte[4096]; //缓冲区大小
                    foreach (string file in filenames)
                    {
                        ZipEntry entry = new ZipEntry(Path.GetFileName(file));
                        entry.DateTime = DateTime.Now;
                        s.PutNextEntry(entry);
                        using (FileStream fs = File.OpenRead(file))
                        {
                            int sourceBytes;
                            do
                            {
                                sourceBytes = fs.Read(buffer, 0, buffer.Length);
                                s.Write(buffer, 0, sourceBytes);
                            } while (sourceBytes > 0);
                        }
                    }
                    s.Finish();
                    s.Close();
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Exception during processing {0}", ex);
            }
        }

        private static void CreateZipFile(string fileDir, string zipFilePath)
        {
            if (!Directory.Exists(fileDir))
            {
                Console.WriteLine("Cannot find directory '{0}'", fileDir);
                return;
            }

            string[] filenames = Directory.GetFiles(fileDir);
            CreateZipFile(filenames, zipFilePath);
        }


        /// <summary>
        /// Http上传文件
        /// </summary>
        public static void HttpUploadAndDownloadFile(string url, string srcpath, string savepath)
        {
            /*------------------- Upload -----------------*/

            // 设置参数
            HttpWebRequest request = WebRequest.Create(url) as HttpWebRequest;
            CookieContainer cookieContainer = new CookieContainer();
            request.CookieContainer = cookieContainer;
            request.AllowAutoRedirect = true;
            request.Method = "POST";
            string boundary = DateTime.Now.Ticks.ToString("X"); // 随机分隔线
            request.ContentType = "multipart/form-data;charset=utf-8;boundary=" + boundary;
            byte[] itemBoundaryBytes = Encoding.UTF8.GetBytes("\r\n--" + boundary + "\r\n");
            byte[] endBoundaryBytes = Encoding.UTF8.GetBytes("\r\n--" + boundary + "--\r\n");

            int pos = srcpath.LastIndexOf("\\");
            string fileName = srcpath.Substring(pos + 1);

            //请求头部信息
            StringBuilder sbHeader = new StringBuilder(string.Format("Content-Disposition:form-data;name=\"file\";filename=\"{0}\"\r\nContent-Type:application/octet-stream\r\n\r\n", fileName));
            byte[] postHeaderBytes = Encoding.UTF8.GetBytes(sbHeader.ToString());

            FileStream fs = new FileStream(srcpath, FileMode.Open, FileAccess.Read);
            byte[] bArr = new byte[fs.Length];
            fs.Read(bArr, 0, bArr.Length);
            fs.Close();

            Stream postStream = request.GetRequestStream();
            postStream.Write(itemBoundaryBytes, 0, itemBoundaryBytes.Length);
            postStream.Write(postHeaderBytes, 0, postHeaderBytes.Length);
            postStream.Write(bArr, 0, bArr.Length);
            postStream.Write(endBoundaryBytes, 0, endBoundaryBytes.Length);
            postStream.Close();

            /*-------------------- Download ----------------*/

            //发送请求并获取相应回应数据
            HttpWebResponse response = request.GetResponse() as HttpWebResponse;
            //直到request.GetResponse()程序才开始向目标网页发送Post请求
            Stream st = response.GetResponseStream();
            Stream so = new FileStream(savepath, FileMode.Create);
            byte[] by = new byte[1024];
            int osize = st.Read(by, 0, by.Length);
            while (osize > 0)
            {
                so.Write(by, 0, osize);
                osize = st.Read(by, 0, by.Length);
            }
            so.Close();
            st.Close();
        }

        static void PluginRun(string pluginExe, string pluginDir)
        {
            DirectoryInfo di = new DirectoryInfo(pluginDir);
            FileInfo[] fis = di.GetFiles("*.proto");
            foreach (FileInfo fi in fis)
            {
                Console.WriteLine("本地生成 " + fi.Name);

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
