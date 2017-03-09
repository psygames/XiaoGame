using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RedStone.Net
{
	public class PlayerHandle : HandleBase
	{
		public override void OnClose(string reason)
		{
			base.OnClose(reason);
            long playerID = ProxyManager.instance.GetProxy<PlayerProxy>().GetPlayerID(sessionId);
            EventManager.instance.Send(Event.Player.ForceQuit, playerID);
		}

		public override void OnError(string reason)
		{
			base.OnError(reason);
			EventManager.instance.Send(Event.Player.Error, sessionId);
		}

		public override void OnOpen(long sessionId)
		{
			base.OnOpen(sessionId);
			EventManager.instance.Send(Event.Player.Connected, sessionId);
		}

		public override void OnMessage(byte[] data)
		{
			base.OnMessage(data);
			object proto = ProtoTool.ToProtoObj(data);
			if (proto.GetType() == typeof(message.LoginRequest))
			{
                // 发送SessionID作为key
                EventManager.instance.Send(proto.GetType().ToString(), sessionId, proto);
            }
            else
			{
				// 发送PlayerID作为key
				long playerID = ProxyManager.instance.GetProxy<PlayerProxy>().GetPlayerID(sessionId);
                Debug.Log("receive ---> {0}   {1} :len({2})", playerID, proto.GetType(), data.Length);
                EventManager.instance.Send(proto.GetType().ToString(), playerID, proto);
            }
        }

		public void Send<T>(T proto)
		{
			byte[] data = ProtoTool.ToData(proto);
			Send(NetType.Main, data);
		}
	}
}
