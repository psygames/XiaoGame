using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


namespace RedStone
{
    public class MatchProxy : MainServerProxyBase
    {
        private List<long> matchPlayers = new List<long>();
        private List<long> battlePlayers = new List<long>();

        public override void OnInit()
        {
            base.OnInit();

            EventManager.instance.Register<long>(Event.Player.ForceQuit, OnPlayerForceQuit);
            RegisterNet<long, message.AssignRoomRequest>(OnPlayerAssignRoom);
        }

        private void OnPlayerAssignRoom(long pid, message.AssignRoomRequest msg)
        {
            if (!matchPlayers.Contains(pid))
                matchPlayers.Add(pid);
        }

        public override void OnUpdate()
        {
            base.OnUpdate();

            MatchLogic();
        }

        public void MatchLogic()
        {
            int minMatchCount = 1;

            if (matchPlayers.Count < minMatchCount)
                return;

            List<long> matched = new List<long>();
            for (int i = 0; i < minMatchCount; i++)
            {
                long pid = matchPlayers[i];
                matched.Add(pid);
                matchPlayers.Remove(pid);
            }

            var md = AssignRoom(matched);
            message.AssignRoomReply rep = new message.AssignRoomReply();
            rep.address = md.address;
            rep.roomId = md.roomID;
            foreach (var pid in matched)
            {
                SendTo(pid, rep);
            }
        }

        public MatchData AssignRoom(List<long> players)
        {
            MatchData data = new MatchData();
            data.address = "ws://127.0.0.1:8004/default";
            data.roomID = 1;
            return data;
        }

        public void OnPlayerForceQuit(long pid)
        {
            matchPlayers.Remove(pid);
            battlePlayers.Remove(pid);
        }
    }
}
