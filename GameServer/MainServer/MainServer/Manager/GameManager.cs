using System.Collections;
namespace RedStone
{
	public class GameManager : Core.Singleton<GameManager>
	{
        private string serverAddress = "ws://139.196.5.96:8180/GameServer/gameServer";
		public void Awake()
		{
			CreateInstance();
			Init();
		}

		private void CreateInstance()
		{
			ServerManager.CreateInstance();
			ProxyManager.CreateInstance();
			EventManager.CreateInstance();
		}

		private void Init()
		{
			ServerManager.instance.Init();
			ProxyManager.instance.Init();

			ProxyManager.instance.GetProxy<HallProxy>().ConnectToGameServer(serverAddress);
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
			ServerManager.instance.CloseAll();
		}
	}
}