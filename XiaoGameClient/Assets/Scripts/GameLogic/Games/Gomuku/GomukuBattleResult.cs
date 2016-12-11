using UnityEngine;
using UnityEngine.UI;
using System;
using System.Collections;

namespace RedStone
{
    public class GomukuBattleResult : MonoBehaviour
    {
        public UIEventListener listener;
        public Text resultText;
        public Action onClickCallback;

        private void Awake()
        {
            listener.onClick = (obj) =>
            {
                if (onClickCallback == null)
                    return;
                onClickCallback.Invoke();
            };
        }

		public void SetData(ECamp camp)
        {
			if (camp == ProxyManager.instance.GetProxy<HallProxy>().mainPlayerData.camp)
			{
				resultText.text = "WIN";
				resultText.color = Color.red;
			}
			else
			{
				resultText.text = "LOSE";
				resultText.color = Color.gray;
			}
        }
    }
}