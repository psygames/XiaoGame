using System;
using WebSocketSharp;
using WebSocketSharp.Server;

namespace ShitMan
{
    class ShitManHandle : WebSocketBehavior
    {
        public long guid;
        protected override void OnOpen()
        {
            guid = MathHelper.LongGUID;
            HallManager.Instance.Print(guid + " 连接成功.");
        }

        protected override void OnMessage(MessageEventArgs e)
        {
            MessageHandler.Instance.Handle(guid, e.RawData);
        }

        protected override void OnClose(CloseEventArgs e)
        {
            Sessions.CloseSession(ID);
            EntryManager.Instance.PlayerExit(guid);
            HallManager.Instance.Print(guid + " 退出游戏.");
        }
    }

}
