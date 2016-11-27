using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System;

namespace RedStone
{
	public class CellGenerator : MonoBehaviour
	{
		public GameObject template;
		private int row = 12;
		private int column = 12;
		private Vector2 space = new Vector2(80, 80);
		[HideInInspector]
		public Action<int, int> onClickCallback;

		void Awake()
		{
			Init();
		}

		void Init()
		{
			for (int i = 0; i < row; i++)
			{
				for (int j = 0; j < column; j++)
				{
					GameObject go = Instantiate(template);
					go.transform.SetParent(transform, false);

					GomukuCell cell = go.GetComponent<GomukuCell>();
					cell.SetData(i, j, row, column, space.x, space.y);
					cell.onClickCallback = onClickCallback;
				}
			}
		}
	}
}
