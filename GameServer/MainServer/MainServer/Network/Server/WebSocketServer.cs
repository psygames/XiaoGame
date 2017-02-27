using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RedStone.Net
{
    public class WebSocketServer<T> : ISocketServer<T>
    {
        private WebSocketSharp.Server.WebSocketServer m_serv = null;
        private WebSocketSharp.Server.WebSocketServiceHost m_host = null;
        private IServerHandle m_handle = null;

        public void Init(int port,IServerHandle handle)
        {
            m_serv = new WebSocketSharp.Server.WebSocketServer(port);
            m_serv.Log.Level = WebSocketSharp.LogLevel.Error;
            m_handle = handle;
            m_serv.AddWebSocketService<WebSocketServerHandle>("/default");
            m_host = m_serv.WebSocketServices["/default"];
        }

        public void Stop()
        {
            m_serv.Stop();
        }

        public void Start()
        {
            m_serv.Start();
        }

        public void SendTo(string sessionID, byte[] content)
        {
            m_host.Sessions.SendToAsync(content, sessionID, (completed) => { });
        }

        public void SendToAll(byte[] content)
        {
            m_host.Sessions.BroadcastAsync(content, () => { });
        }

        public class WebSocketServerHandle : WebSocketSharp.Server.WebSocketBehavior
        {
            public IServerHandle 

            protected override void OnOpen()
            {
                base.OnOpen();
                
            }
        }
    }
}
