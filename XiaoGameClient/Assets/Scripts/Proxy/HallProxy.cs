using System;
using UnityEngine;
using message;
namespace RedStone
{
	public class HallProxy : ProxyBase
	{
		private PlayerData m_mainPlayerData = new PlayerData();
		public PlayerData mainPlayerData { get { return m_mainPlayerData; } }

		public HallProxy()
		{
			netType = NetType.Hall;
		}

		public override void OnInit()
		{
			base.OnInit();
		}

		public override void OnDestroy()
		{
			base.OnDestroy();
		}

		public void AssignRoom()
		{
			AssignRoomRequest msg = new AssignRoomRequest();
			network.SendMessage<AssignRoomRequest, AssignRoomReply>(msg,
			(reply) =>
			{

			});
		}

		public void ConnectToGameServer()
		{
			NetworkManager.instance.Get(NetType.Hall).onConnected = (obj) =>
			{
				string deviceUID = UUID.DEVICE;
				ProxyManager.instance.GetProxy<HallProxy>().Login(deviceUID);
			};

			NetworkManager.instance.Connect(NetType.Hall, "ws://192.168.10.106:8180/GameServer/test");
		}

		public void Login(string uuid)
		{
			LoginRequest msg = new LoginRequest();
			msg.deviceUID = uuid;
			network.SendMessage<LoginRequest, LoginReply>
			(msg, (reply) =>
			{
				m_mainPlayerData.SetData(reply);
				UIManager.instance.Show<GomukuView>();
			});
		}
	}
}
