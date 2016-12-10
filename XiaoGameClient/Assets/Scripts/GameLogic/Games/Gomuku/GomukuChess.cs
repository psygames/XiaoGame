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
			SetType(data.type);
		}

		public void SetType(message.Enums.ChessType type)
		{
			if (type == message.Enums.ChessType.Black)
			{
				chess.sprite = black;
				chess.color = Color.white;
			}
			else if (type == message.Enums.ChessType.White)
			{
				chess.sprite = white;
				chess.color = Color.white;
			}
			else
			{
				chess.sprite = white;
				chess.color = new Color(1, 1, 1, 0.5f);
			}
		}
	}
}