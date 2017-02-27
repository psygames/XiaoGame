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
            m_servers.Add(NetType.Hall, new WebSocketServer());
            m_servers.Add(NetType.Battle, new WebSocketServer());

            m_servers[NetType.Hall].Init(8004, () => { return new HallHandle(); });
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

        public void SendMessage<T>(NetType type,long sessionId, T message)
        {
            m_servers[type].SendTo(sessionId,message);
        }

        public void SendMessage<T1, T2>(NetType type, T1 message, Action<T2> callback)
        {
            m_networks[type].SendMessage(message, callback);
        }

        public void Register<T>(NetType type, Action<T> callback)
        {
            m_networks[type].Register(callback);
        }

        public void UnRegister<T>(NetType type, Action<T> callback)
        {
            m_networks[type].UnRegister(callback);
        }
    }
}

