using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RedStone.Net
{
    public class WebSocketServer : ISocketServer
    {
        WebSocketSharp.Server.WebSocketServer serv = null;
        WebSocketSharp.Server.WebSocketServiceHost host = null;
        public void Close()
        {
            throw new NotImplementedException();
        }

        public void Init(int port)
        {
            serv = new WebSocketSharp.Server.WebSocketServer(port);
            serv.Log.Level = WebSocketSharp.LogLevel.Error;
            serv.AddWebSocketService<DefaultHandle>("/default", () =>
             {
                 return new DefaultHandle();
             });
            host = serv.WebSocketServices["/default"];
        }


        public void CreateDefaultHandle()
        {
            var handle = new DefaultHandle();
            handle.onOpen = () =>
            {
                OnOpen(handle.ID);
            };

            handle.onClose = (msg) =>
            {
                OnClose(handle.ID, msg);
            };
        }

        public void OnOpen(string sessionID)
        {

        }

        public void OnClose(string sessionID, string msg)
        {

        }
        public void OnError(string sessionID, string msg)
        {

        }
        public void OnClose(string sessionID, string msg)
        {

        }

        public void Listen()
        {
            serv.Start();
            host.
        }

        public void SendTo(string sessionID, byte[] content)
        {
            host.Sessions.SendToAsync(content, sessionID, (completed) => { });
        }

        public void SendToAll(byte[] content)
        {
            host.Sessions.BroadcastAsync(content, () => { });
        }

        public class DefaultHandle : WebSocketSharp.Server.WebSocketBehavior
        {
            public Action onOpen;
            public Action<byte[]> onMessage;
            public Action<string> onClose;
            public Action<string> onError;

            protected override void OnOpen()
            {
                this.
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
