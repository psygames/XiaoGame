using System;
using message;
using RedStone.Net;
namespace RedStone
{
    public class MainServerProxyBase : ProxyBase
    {
        public void SendTo<T>(long playerID, T proto)
        {
            long sessionID = ProxyManager.instance.GetProxy<PlayerProxy>().GetSessionID(playerID);
            byte[] data = ProtoTool.ToData(proto);
            SendTo(sessionID, data);
            Debug.Log("sendto ---> {0}   {1} :len({2})", playerID, proto.GetType(), data.Length);
        }

        public void SendTo(long sessionID, byte[] data)
        {
            ServerManager.instance.Get(NetType.Main).SendTo(sessionID, data);
        }
    }
}
