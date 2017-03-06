using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Windows.Forms;

namespace MainServer
{
	static class Program
	{
		[DllImport("kernel32.dll")]
		public static extern Boolean AllocConsole();
		[DllImport("kernel32.dll")]
		public static extern Boolean FreeConsole();
		/// <summary>
		/// The main entry point for the application.
		/// </summary>
		[STAThread]
		static void Main()
		{
			switch (System.Environment.OSVersion.Platform)
			{
				case PlatformID.MacOSX:
				case PlatformID.Unix:
					UnixMain();
					break;
				case PlatformID.Win32Windows:
				case PlatformID.Win32S:
				case PlatformID.Win32NT:
					WinMain();
					break;
				default:
					Debug.LogError("not support for platform {0}", System.Environment.OSVersion.Platform);
					break;
			}
		}

		static void WinMain()
		{
			AllocConsole();
			Application.EnableVisualStyles();
			Application.SetCompatibleTextRenderingDefault(false);
			Application.Run(new MainForm());
			FreeConsole();
		}

		static void UnixMain()
		{
			RedStone.GameManager.CreateInstance().Init();
			RedStone.ServerManager.instance.Start(RedStone.Net.NetType.Main);
			RedStone.ServerManager.instance.Start(RedStone.Net.NetType.Battle);
			while (true)
			{
				System.Threading.Thread.Sleep(1000);
			}
		}
	}
}
