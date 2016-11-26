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
		}

		private void Init()
		{
			NetworkManager.instance.Init();
		}

		void Update()
		{
			NetworkManager.instance.Update();
		}
	}
}