using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RedStone.Net
{
    public interface ISocketServer<T>
    {
        void Init(int port);
        void Start();
        void Stop();
        void SendTo(string sessionID, byte[] content);
    }
}
