using UnityEngine;
using UnityEngine.UI;
using System.Collections;

namespace RedStone
{
	public class GomukuChess : MonoBehaviour
	{
		public Image chess;

		public Sprite white;
		public Sprite black;

		public void SetNum(int num)
		{
			transform.localPosition = LogicHelper.Gomuku.GetChessPos(num);
		}

		public void SetData(ChessData data)
		{
			SetNum(data.num);
			if (data.type == message.Enums.ChessType.Black)
				chess.sprite = black;
			else if (data.type == message.Enums.ChessType.White)
				chess.sprite = white;
			else
				chess.sprite = black;
		}
	}
}