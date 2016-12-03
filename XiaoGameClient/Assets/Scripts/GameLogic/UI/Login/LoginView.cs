using UnityEngine;
using System.Collections;

namespace RedStone
{
	public class LoginView : ViewBase
	{
		void Start()
		{

		}

		void Update()
		{

		}

		public void OnLogin()
		{
			NetworkManager.instance.Connect(NetType.Battle, "ws://192.168.10.106:8080/XiaoGameBattleServer/battleServer");
		}
	}
}