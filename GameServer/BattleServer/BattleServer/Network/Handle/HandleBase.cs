using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RedStone.Net
{
    public class HandleBase : IServerHandle
    {
        public long sessionId = -1;

        public virtual void OnClose(string reason)
        {
            Debug.LogError("close --> [{0}]   {1} ", sessionId, reason);
        }

        public virtual void OnError(string reason)
        {
            Debug.LogError("error --> {0} ", reason);
        }

        public virtual void OnMessage(byte[] data)
        {

        }

        public virtual void OnOpen(long sessionId)
        {
            Debug.Log("open --> [{0}]", sessionId);
            this.sessionId = sessionId;
        }

        public virtual void Send(NetType type, byte[] data)
        {
            ServerManager.instance.Get(type).SendTo(sessionId, data);
        }
    }
}
