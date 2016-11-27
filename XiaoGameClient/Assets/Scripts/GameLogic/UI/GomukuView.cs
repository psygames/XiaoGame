using System;
using UnityEngine;
namespace RedStone
{
	public class GomukuView : ViewBase
	{
		public CellGenerator generator;

		private void Awake()
		{
			generator.onClickCallback = OnCellClick;
		}

		private void OnCellClick(int x, int y)
		{
			Debug.Log(x + "   " + y);
		}

		private void Update()
		{ 
			
		}

		private void UpdateChess()
		{ 
			
		}
	}
}
