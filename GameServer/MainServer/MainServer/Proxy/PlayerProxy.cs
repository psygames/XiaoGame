using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace RedStone
{
    public class PlayerProxy : ProxyBase
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



            Debug.Log("login  {0}", sessionID);
        }

        public void OnPlayerLogout(long sessionID)
        {
            sessionPlayerDict.Remove(sessionID);
            Debug.Log("logout  {0}", sessionID);
        }
    }
}
