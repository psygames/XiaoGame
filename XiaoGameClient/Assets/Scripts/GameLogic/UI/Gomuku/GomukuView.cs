using System;
using System.Collections.Generic;
using UnityEngine;
namespace RedStone
{
	public class GomukuView : ViewBase
	{
		public CellGenerator generator;

		private GomukuProxy proxy { get { return GetProxy<GomukuProxy>(); } }

		protected override void OnInit()
		{
			base.OnInit();

			generator.onClickCallback = OnCellClick;

			Register(Event.Gomuku.BoardSync, OnBoardSync);
		}

		private void OnCellClick(int x, int y)
		{
			Debug.Log(x + "   " + y);
		}

		private void OnBoardSync()
		{
			var itr = proxy.chesses.GetEnumerator();
			while (itr.MoveNext())
			{

			}

			List<PlaceStatistics> statistics = proxy.placeStatistics;
			for (int i = 0; i < statistics.Count; i++)
			{
				
			}
		}

		private void Update()
		{

		}

		private void UpdateChess()
		{

		}
	}
}
