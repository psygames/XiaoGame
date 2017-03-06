using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RedStone.Net
{
    public interface ISocketServer
    {
        void Init(int port,Func<IServerHandle> initializer);
        void Start();
        void Stop();
        void SendTo(long sessionId, byte[] content);
        void SendTo<T>(long sessionId, T msg);
    }
}
