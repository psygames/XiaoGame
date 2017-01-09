using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RedStone.Net
{
    public interface ISocketServer
    {
        void Init(int port);

        void Listen();
        void Close();
        void SendTo(string sessionID, byte[] content);
    }
}
