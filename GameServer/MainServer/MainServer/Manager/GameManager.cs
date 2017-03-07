using System.Collections;
namespace RedStone
{
	public class GameManager : Core.Singleton<GameManager>
	{
		public void Init()
		{
            DBManager.CreateInstance();
            ProtoTool.CreateInstance();
            ServerManager.CreateInstance();
            ProxyManager.CreateInstance();
            EventManager.CreateInstance();

            DBManager.instance.Init();
            ProtoTool.instance.Init();
            ServerManager.instance.Init();
            ProxyManager.instance.Init();
		}

		private void Update()
		{
			ServerManager.instance.Update();
			ProxyManager.instance.Update();
		}

		private void OnDestroy()
		{
			ProxyManager.instance.Destroy();
			EventManager.instance.ClearAll();
		}

		private void OnApplicationQuit()
		{
			ServerManager.instance.StopAll();
		}
	}
}