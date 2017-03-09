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
        private WebSocketSharp.Server.WebSocketServiceHost host
        {
            get { return m_serv.WebSocketServices["/default"]; }
        }
        private static Dictionary<long, string> m_sessionDict = new Dictionary<long, string>();

        public void Init(int port, Func<IServerHandle> handleInitializer)
        {
            m_serv = new WebSocketSharp.Server.WebSocketServer(port);
            m_serv.Log.Level = WebSocketSharp.LogLevel.Error;
            m_serv.WaitTime = TimeSpan.FromSeconds(1);
            m_serv.AddWebSocketService<WebSocketServerHandle>("/default", () =>
            {
                WebSocketServerHandle handle = new WebSocketServerHandle(handleInitializer.Invoke());
                return handle;
            });
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
            string sessionStrID = m_sessionDict[sessionID];
            host.Sessions.SendToAsync(content, sessionStrID, (completed) => { });
        }

        public void SendToAll(byte[] content)
        {
            host.Sessions.BroadcastAsync(content, () => { });
        }

        public class WebSocketServerHandle : WebSocketSharp.Server.WebSocketBehavior
        {
            private IServerHandle m_handle;
            public long sessionID;
            public WebSocketServerHandle(IServerHandle handle)
            {
                m_handle = handle;
            }

            protected override void OnOpen()
            {
                base.OnOpen();
                sessionID = GUID.Long;
                m_sessionDict[sessionID] = ID;
                m_handle.OnOpen(sessionID);
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
