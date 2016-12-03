using UnityEngine;
using System.Collections;

namespace RedStone
{
	public enum Camp
	{
		None = 0,
		White = 1, // 对应于 ChessType 可强转
		Black = 2,
	}

	public class PlayerData : DataBase
	{
		private int m_uuid;
		private string m_name;
		private Camp m_camp;

		public int uuid { get { return m_uuid; } }
		public string name { get { return m_name; } }
		public Camp camp { get { return m_camp; } }

		public void SetData(message.LoginReply info)
		{
			m_uuid = 1;
			m_name = info.name;
			m_camp = Camp.None;
		}

		public void UpdateCamp(message.Enums.Camp camp)
		{
			m_camp = (Camp)camp;
		}
	}
}