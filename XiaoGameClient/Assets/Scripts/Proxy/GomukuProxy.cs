using System;
using System.Collections.Generic;
namespace RedStone
{
	public class GomukuProxy : ProxyBase
	{
		public Dictionary<int, ChessData> chesses { get { return m_chesses; } }
		public List<PlaceStatistics> placeStatistics { get { return m_placeStatistics; } }

		private Dictionary<int, ChessData> m_chesses = new Dictionary<int, ChessData>();
		private List<PlaceStatistics> m_placeStatistics = new List<PlaceStatistics>();

		public GomukuProxy()
		{
			netType = NetType.Battle;
		}

		public override void OnInit()
		{
			base.OnInit();

			network.Register<message.BoardSync>(OnBoardSync);
		}

		public override void OnDestroy()
		{
			network.UnRegister<message.BoardSync>(OnBoardSync);

			base.OnDestroy();
		}

		public void OnBoardSync(message.BoardSync board)
		{
			for (int i = 0; i < board.rows.Count; i++)
			{
				for (int j = 0; j < board.rows[i].types.Count; j++)
				{
					int num = i * 12 + j;
					ChessData chess;
					if (!m_chesses.TryGetValue(num, out chess))
					{
						chess = new ChessData();
						m_chesses.Add(chess.num, chess);
					}
					chess.SetData(num, i, j, board.rows[i].types[i]);
				}
			}

			m_placeStatistics.Clear();
			for (int i = 0; i < board.statistics.Count; i++)
			{
				PlaceStatistics sta = new PlaceStatistics();
				sta.SetData(board.statistics[i].num, board.statistics[i].ratio);
				m_placeStatistics.Add(sta);
			}

			EventManager.instance.Send(Event.Gomuku.BoardSync);
		}

		public override void OnUpdate()
		{
			base.OnUpdate();
		}
	}
}
