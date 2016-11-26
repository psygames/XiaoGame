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

		private void callback1(string num)
		{
			Debug.Log(num + 1);
		}


		private void callback(string num)
		{
			Debug.Log(num + 0);
		}

		void Update()
		{
			NetworkManager.instance.Update();
		}
	}
}