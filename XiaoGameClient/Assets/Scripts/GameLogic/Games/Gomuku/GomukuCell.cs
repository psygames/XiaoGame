using UnityEngine;
using System;
using System.Collections;

namespace RedStone
{
	public class GomukuCell : MonoBehaviour
	{
		public UIEventListener listener;
		public GameObject point;
		public Action<int, int> onClickCallback;

		public int x;
		public int y;


		private void Awake()
		{
			listener.onClick = (obj) =>
			{
				if (onClickCallback == null)
					return;
				onClickCallback.Invoke(x, y);
			};
		}

		public void SetData(int x, int y, int w, int h, float spacex, float spacey)
		{
			this.x = x;
			this.y = y;

			float posx = (x - w / 2) * spacex + 0.5f * spacex;
			float posy = (y - h / 2) * spacey + 0.5f * spacey;

			point.SetActive(false);
			if (x % 5 == 3 && y % 5 == 3)
				point.SetActive(true);

			transform.localPosition = new Vector2(posx, posy);
		}

	}
}