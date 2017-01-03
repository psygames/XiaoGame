using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;
using WebSocketSharp.Server;

namespace RedStone
{
    class SessionManager
    {
        private static SessionManager m_instance;
        public static SessionManager Instance
        {
            get
            {
                if (m_instance == null)
                    m_instance = new SessionManager();
                return m_instance;
            }
        }

        BinaryFormatter binFormat = new BinaryFormatter();//创建二进制序列化器
        WebSocketServer serv = null;
        public WebSocketServiceHost shitHost { get { return serv.WebSocketServices["/ShitMan"]; } }
        public void Start()
        {
            serv = new WebSocketServer(8730);
            serv.Log.Level = WebSocketSharp.LogLevel.Error;
            serv.WaitTime = TimeSpan.FromSeconds(2);
            serv.AddWebSocketService<ShitManHandle>("/ShitMan");
            serv.Start();
            if (serv.IsListening)
            {
                HallManager.Instance.Print("Listening on port : " + serv.Port);
                foreach (var path in serv.WebSocketServices.Paths)
                    HallManager.Instance.Print("Server Path : " + path);
            }
        }

        public void Stop()
        {
            if (serv != null)
                serv.Stop();
        }

        public void SendTo(long guid, MessageBase msg)
        {
            foreach (string ids in shitHost.Sessions.IDs)
            {
                ShitManHandle shitHandle = shitHost.Sessions[ids] as ShitManHandle;
                if (shitHandle.guid == guid)
                {
                    byte[] data = SerializeMsg(msg);
                    SendTo(data, ids);
                }
            }
        }

        public void SendToOthers(long guid, MessageBase msg)
        {
            foreach (string ids in shitHost.Sessions.IDs)
            {
                ShitManHandle shitHandle = shitHost.Sessions[ids] as ShitManHandle;
                if (shitHandle.guid != guid)
                {
                    byte[] data = SerializeMsg(msg);
                    SendTo(data, ids);
                }
            }
        }

        public void SendToAll(MessageBase msg)
        {
            byte[] data = SerializeMsg(msg);
            shitHost.Sessions.Broadcast(data);
            //foreach (string ids in shitHost.Sessions.IDs)
            //{
            //    SendTo(data, ids);
            //}
        }

        public void SendTo(byte[] data,string ids)
        {
            shitHost.Sessions.SendToAsync(data, ids, null);
        }

        private byte[] SerializeMsg(MessageBase msg)
        {
            MemoryStream stream = new MemoryStream();
            binFormat.Serialize(stream, msg);
            return stream.GetBuffer();
        }
    }
}
