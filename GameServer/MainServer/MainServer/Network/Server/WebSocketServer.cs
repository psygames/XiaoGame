using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using WebSocketSharp;

namespace RedStone.Net
{
    public class WebSocketServer : ISocketServer
    {
        private WebSocketSharp.Server.WebSocketServer m_serv = null;
        private WebSocketSharp.Server.WebSocketServiceHost m_host = null;

        public void Init(int port,Func<IServerHandle> handleInitializer)
        {
            m_serv = new WebSocketSharp.Server.WebSocketServer(port);
            m_serv.Log.Level = WebSocketSharp.LogLevel.Error;
            m_serv.AddWebSocketService<WebSocketServerHandle>("/default", () =>
            {
                return new WebSocketServerHandle(handleInitializer.Invoke());
            });

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

        public void SendTo(long sessionID, byte[] content)
        {
            m_host.Sessions.SendToAsync(content, sessionID, (completed) => { });
        }

        public void SendToAll(byte[] content)
        {
            m_host.Sessions.BroadcastAsync(content, () => { });
        }

        public class WebSocketServerHandle : WebSocketSharp.Server.WebSocketBehavior
        {
            private IServerHandle m_handle;
            public WebSocketServerHandle(IServerHandle handle)
            {
                m_handle = handle;
            }

            protected override void OnOpen()
            {
                base.OnOpen();
                long guid = GUID.Long;
                m_handle.OnOpen(guid);
            }

            protected override void OnMessage(MessageEventArgs e)
            {
                base.OnMessage(e);
                m_handle.OnMessage(e.RawData);
            }

            protected override void OnClose(CloseEventArgs e)
            {
                base.OnClose(e);
                m_handle.OnClose(e.Reason);
            }

            protected override void OnError(ErrorEventArgs e)
            {
                base.OnError(e);
                m_handle.OnError(e.Message);
            }
        }
    }
}
