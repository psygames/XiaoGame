using System;
using UnityEngine;
using message;
namespace RedStone.Proxy
{
	public class HallProxy : ProxyBase
	{
		public HallProxy()
		{


		}

		protected override void OnInit()
		{
			base.OnInit();
			network.Register<LoginReply>(OnLogin);
		}

		protected override void OnDestroy()
		{
			network.UnRegister<LoginReply>(OnLogin);
			base.OnDestroy();
		}

		public void Login(long uuid)
		{
			LoginRequest msg = new LoginRequest();
			msg.deviceUID = uuid;
			network.SendMessage(msg);

			network.SendMessage<LoginRequest, LoginReply>(msg, (reply) =>
			{
				Debug.Log("Once: " + reply.name);
			});
		}

		public void OnLogin(LoginReply msg)
		{
			Debug.Log("Keep: " + msg.name);
		}
	}
}
