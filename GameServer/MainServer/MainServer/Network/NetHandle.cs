using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Hotfire.Net
{
    public class NetHandle
    {
        public string sessionID;
        public void OnOpen()
        {
        }

        public void OnMessage(byte[] data)
        {
        }

        public void OnClose(string msg)
        {

        }

        public void OnError(string msg)
        {

        }
    }
}
