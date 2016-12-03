using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
namespace RedStone
{
	public class GomukuView : ViewBase
	{
		public CellGenerator generator;
		public GomukuChess chessTemplate;
		public Transform chessRoot;
		public PlaceStatistics statisticsTemplate;
		public Transform statisticsRoot;
		public PlaceStatistics selfPlace;
		public GomukuChess selfTake;
		public Text nameText;


		private List<GomukuChess> m_chesses = new List<GomukuChess>();
		private List<PlaceStatistics> m_statisticsChesses = new List<PlaceStatistics>();

		private int m_curPlaceNum = -1;
		private bool m_isPlaced = false;
		private bool canPlace { get { return !m_isPlaced; } }
		private bool isShowSelfPlace { get { return m_curPlaceNum >= 0 && m_isPlaced; } }

		private GomukuProxy proxy { get { return GetProxy<GomukuProxy>(); } }

		public override void OnInit()
		{
			base.OnInit();

			generator.Init();
			generator.onClickCallback = OnCellClick;

			Register(Event.Gomuku.BoardSync, OnBoardSync);
			Register<ECamp>(Event.Gomuku.NewTurn, OnNewTurn);
		}

		public override void OnDestory()
		{
			UnRegister(Event.Gomuku.BoardSync, OnBoardSync);
			UnRegister<ECamp>(Event.Gomuku.NewTurn, OnNewTurn);

			base.OnDestory();
		}

		private void OnCellClick(int num)
		{
			ChessData chess = proxy.GetChess(num);
			if (!canPlace || chess == null || proxy.GetChess(num).type != message.Enums.ChessType.None)
			{
				Debug.Log("can not place!");
				return;
			}

			m_isPlaced = true;
			proxy.PlaceChess(num);
		}

		private void OnNewTurn(ECamp camp)
		{
			m_isPlaced = false;
		}

		private void OnBoardSync()
		{
			GameObjectHelper.SetListContent(chessTemplate, chessRoot, m_chesses, proxy.chesses.Values,
			(index, item, data) =>
			{
				item.SetData(data);
			});

			GameObjectHelper.SetListContent(statisticsTemplate, statisticsRoot, m_statisticsChesses, proxy.placeStatistics,
			(index, item, data) =>
			{
				item.SetData(data);
			});
		}

		private void Update()
		{
			UpdateCurPlace();
			UpdateSelfTake();
		}

		private void UpdateCurPlace()
		{
			selfPlace.gameObject.SetActive(isShowSelfPlace);
			selfPlace.SetNum(m_curPlaceNum);
		}

		private void UpdateSelfTake()
		{
			PlayerData playerData = GetProxy<HallProxy>().mainPlayerData;
			if (playerData.camp == ECamp.Black)
				selfTake.chess.sprite = selfTake.black;
			else if (playerData.camp == ECamp.Black)
				selfTake.chess.sprite = selfTake.white;
			else
				selfTake.gameObject.SetActive(false);

			nameText.text = playerData.name;
		}
	}
}
