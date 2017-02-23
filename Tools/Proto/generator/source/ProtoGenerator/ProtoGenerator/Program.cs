using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using IniTool;
using System.Linq;
using System.Text;
using System.Net;
using ICSharpCode.SharpZipLib.Zip;

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
                RemotePlguninRun(protoDir, pluginDir);
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

        static void RemotePlguninRun(string protoDir, string pluginDir)
        {
            DirectoryInfo di = new DirectoryInfo(pluginDir);
            FileInfo[] fis = di.GetFiles("*.proto");
            string url = "http://139.196.5.96/proto/gen?type=zip&out_type=file";
            string zipFile = pluginDir + "protos.zip";
            foreach (FileInfo fi in fis)
            {
                Console.WriteLine("生成 " + fi.Name);
                string shortname = Path.GetFileNameWithoutExtension(fi.FullName);
                string out_file = Path.GetDirectoryName(fi.FullName)
                    + "/" + shortname + ".cs";
                string tmp_url = url + "&filename=" + shortname;
                ZipFileByRule(protoDir, zipFile, shortname);
                HttpUploadFile(tmp_url, zipFile, out_file);
            }
        }

        static void ZipFileByRule(string protoDir, string outFile, string curProtoName)
        {
            CreateZipFile(protoDir, outFile);
        }

        private static void CreateZipFile(string fileDir, string zipFilePath)
        {
            if (!Directory.Exists(fileDir))
            {
                Console.WriteLine("Cannot find directory '{0}'", fileDir);
                return;
            }
            try
            {
                string[] filenames = Directory.GetFiles(fileDir);
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


        /// <summary>
        /// Http上传文件
        /// </summary>
        public static void HttpUploadFile(string url, string srcpath, string savepath)
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
            Console.WriteLine(savepath);
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
