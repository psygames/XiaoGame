using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RedStone.Net
{
    public class DefaultHandle : IServerHandle
    {

        protected override void OnOpen()
        {
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
