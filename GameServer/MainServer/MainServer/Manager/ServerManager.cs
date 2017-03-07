using System;
using System.Collections.Generic;
using RedStone.Net;

namespace RedStone
{
    public class ServerManager : Core.Singleton<ServerManager>
    {
        private Dictionary<NetType, ISocketServer> m_servers = new Dictionary<NetType, ISocketServer>();

        public ISocketServer Get(NetType type)
        {
            return m_servers[type];
        }

        public void Init()
        {
            m_servers.Add(NetType.Main, new WebSocketServer());
            m_servers.Add(NetType.Battle, new WebSocketServer());

            m_servers[NetType.Main].Init(8004, () => { return new PlayerHandle(); });
            m_servers[NetType.Battle].Init(8006, () => { return new BattleHandle(); });
        }

        public void Update()
        {

        }

        public void Start(NetType type)
        {
            m_servers[type].Start();
        }

        public void StopAll()
        {
            var itr = m_servers.GetEnumerator();
            while (itr.MoveNext())
            {
                itr.Current.Value.Stop();
            }
        }

        public void Stop(NetType type)
        {
            m_servers[type].Stop();
        }

        public void SendMessage(NetType type, long sessionId, byte[] message)
        {
            m_servers[type].SendTo(sessionId, message);
        }
    }
}

