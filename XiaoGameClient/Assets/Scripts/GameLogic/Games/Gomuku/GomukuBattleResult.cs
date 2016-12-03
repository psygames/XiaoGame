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

        public void SetData(bool isWin)
        {
            if (isWin)
                resultText.text = "WIN";
            else
                resultText.text = "LOSE";
        }
    }
}