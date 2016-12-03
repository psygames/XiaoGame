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


		}

		private void Connect()
		{ 
			NetworkManager.instance.Connect(NetType.Hall, "ws://192.168.10.106:8180/XiaoGameServer/test");
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