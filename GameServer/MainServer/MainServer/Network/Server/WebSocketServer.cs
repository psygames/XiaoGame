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

        public void Init(int port)
        {
            m_serv = new WebSocketSharp.Server.WebSocketServer(port);
            m_serv.Log.Level = WebSocketSharp.LogLevel.Error;
            m_serv.AddWebSocketService<DefaultHandle>("/default", () =>
            {
                return CreateDefaultHandle();
            });
            m_host = m_serv.WebSocketServices["/default"];
        }


        public DefaultHandle CreateDefaultHandle()
        {
            var handle = new DefaultHandle();

            return handle;
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

        public class DefaultHandle : WebSocketSharp.Server.WebSocketBehavior
        {
            public Action onOpen;
            public Action<byte[]> onMessage;
            public Action<string> onClose;
            public Action<string> onError;

            protected override void OnOpen()
            {
                base.OnOpen();
                if (onOpen != null)
                    onOpen.Invoke();
            }

            protected override void OnMessage(WebSocketSharp.MessageEventArgs e)
            {
                base.OnMessage(e);
                if (onMessage != null)
                    onMessage.Invoke(e.RawData);
            }

            protected override void OnClose(WebSocketSharp.CloseEventArgs e)
            {
                if (onClose != null)
                    onClose.Invoke(e.Reason);
                base.OnClose(e);
            }

            protected override void OnError(WebSocketSharp.ErrorEventArgs e)
            {
                if (onError != null)
                    onError.Invoke(e.Message);
                base.OnError(e);
            }
        }
    }
}
