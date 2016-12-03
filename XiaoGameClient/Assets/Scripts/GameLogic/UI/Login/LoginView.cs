using UnityEngine;
using System.Collections;

namespace RedStone
{
	public class LoginView : ViewBase
	{
        public UIEventListener playListener;

        public override void OnInit()
        {
            base.OnInit();
            playListener.onClick = OnClickPlay;
        }

		public void OnClickPlay(UIEventListener listener)
		{
			GetProxy<HallProxy>().AssignRoom();
		}
	}
}