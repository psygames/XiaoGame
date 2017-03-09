using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RedStone.Net
{
	public class BattleHandle : HandleBase
	{
		public override void OnClose(string reason)
		{
			base.OnClose(reason);

		}

		public override void OnError(string reason)
		{
			base.OnError(reason);

		}

		public override void OnMessage(byte[] data)
		{
			base.OnMessage(data);

		}

		public override void OnOpen(long sessionId)
		{
			base.OnOpen(sessionId);
		}

		public void Send<T>(T proto)
		{
			byte[] data = ProtoTool.ToData(proto);
			Send(NetType.Battle, data);
		}
	}
}
