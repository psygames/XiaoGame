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
		public GomukuBattleResult result;


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

			result.onClickCallback = OnCloseClick;
			result.gameObject.SetActive(false);

			Register(Event.Gomuku.BoardSync, OnBoardSync);
			Register<ECamp>(Event.Gomuku.NewTurn, OnNewTurn);
			Register<bool>(Event.Gomuku.BattleResult, OnBattleResult);
		}

		public override void OnDestory()
		{
			UnRegister(Event.Gomuku.BoardSync, OnBoardSync);
			UnRegister<ECamp>(Event.Gomuku.NewTurn, OnNewTurn);
			UnRegister<bool>(Event.Gomuku.BattleResult, OnBattleResult);

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

		private void OnCloseClick()
		{
			proxy.network.Close();
			result.gameObject.SetActive(false);
			UIManager.instance.Show<LoginView>();
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

		private void OnBattleResult(bool isWin)
		{
			result.gameObject.SetActive(true);
			result.SetData(isWin);
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
			selfTake.SetType((message.Enums.ChessType)playerData.camp);
			nameText.text = playerData.name;
		}
	}
}
