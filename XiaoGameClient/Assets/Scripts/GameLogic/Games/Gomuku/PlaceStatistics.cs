using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System;

namespace RedStone
{
	public class PlaceStatistics : MonoBehaviour
	{
		public Image chess;
		public Text text;

		public void SetNum(int num)
		{
			transform.localPosition = LogicHelper.Gomuku.GetChessPos(num);
		}

		public void SetData(PlaceStatisticsData data)
		{
			SetNum(data.num);
			text.text = data.ratio.ToString("P1");
		}
	}
}