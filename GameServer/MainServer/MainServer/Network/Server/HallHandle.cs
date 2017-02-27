using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RedStone.Net
{
    public class HallHandle : IServerHandle
    {
        public void OnClose(string reason)
        {
            throw new NotImplementedException();
        }

        public void OnError(string reason)
        {
            throw new NotImplementedException();
        }

        public void OnMessage(byte[] data)
        {
            throw new NotImplementedException();
        }

        public void OnOpen(long sessionId)
        {
            throw new NotImplementedException();
        }
    }
}
