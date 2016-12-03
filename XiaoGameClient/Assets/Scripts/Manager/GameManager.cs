using UnityEngine;
using System.Collections;
namespace RedStone
{
	public class GameManager : Core.SingletonBehaviour<GameManager>
	{
		protected override void Awake()
		{
			base.Awake();

			CreateInstance();

			Init();
		}

		private void CreateInstance()
		{
			NetworkManager.CreateInstance();
			ProxyManager.CreateInstance();
			EventManager.CreateInstance();
			UIManager.CreateInstance();
		}

		private void Init()
		{
			NetworkManager.instance.Init();
			ProxyManager.instance.Init();
			UIManager.instance.Init();

			UIManager.instance.Show<LoginView>();
			ProxyManager.instance.GetProxy<HallProxy>().ConnectToGameServer();
		}

		private void Update()
		{
			NetworkManager.instance.Update();
			ProxyManager.instance.Update();
		}

		private void OnDestroy()
		{
			ProxyManager.instance.Destroy();
			EventManager.instance.ClearAll();
		}

		private void OnApplicationQuit()
		{
			NetworkManager.instance.CloseAll();
		}
	}
}