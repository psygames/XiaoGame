using System;
using UnityEngine;
using message;
namespace RedStone
{
	public class HallProxy : ProxyBase
	{
		public HallProxy()
		{


		}

		public override void OnInit()
		{
			base.OnInit();
		}

		public override void OnDestroy()
		{
			base.OnDestroy();
		}

		public void Login(string uuid)
		{
			LoginRequest msg = new LoginRequest();
			msg.deviceUID = uuid;
			network.SendMessage<LoginRequest, LoginReply>
			(msg, (reply) =>
			{
				UIManager.instance.Show<GomukuView>();
			});
		}
	}
}
