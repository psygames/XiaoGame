using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RedStone
{
    public class PlayerProxy : MainServerProxyBase
    {
        public Dictionary<long, PlayerData> playerDict = new Dictionary<long, PlayerData>();
        public Dictionary<long, long> sessionPlayerDict = new Dictionary<long, long>();

        public long GetPlayerID(long sessionID)
        {
            return sessionPlayerDict[sessionID];
        }

        public PlayerData GetPlayer(long playerID)
        {
            PlayerData data;
            if (playerDict.TryGetValue(playerID, out data))
            {
                return data;
            }
            return null;
        }

        public long GetSessionID(long playerID)
        {
            return sessionPlayerDict.First(a => { return a.Value == playerID; }).Key;
        }

        public override void OnInit()
        {
            base.OnInit();

            RegisterNet<long, message.LoginRequest>(OnPlayerLogin);
            EventManager.instance.Register<long>(Event.Player.Logout, OnPlayerLogout);
        }

        public void OnPlayerLogin(long sessionID, message.LoginRequest msg)
        {
            long playerUID = msg.deviceUID.GetHashCode();
            sessionPlayerDict[sessionID] = playerUID;

            //TODO: 额外LOG处理
            Debug.Log("receive ---> {0}   {1} :len({2})", playerUID, msg.GetType(), -1);

            message.LoginReply rep = new message.LoginReply();
            rep.level = 1;
            rep.name = "hello";
            SendTo(playerUID, rep);
        }

        public void OnPlayerLogout(long sessionID)
        {
            Debug.Log("force close ---> {0}", sessionPlayerDict[sessionID]);
            sessionPlayerDict.Remove(sessionID);
        }
    }
}
