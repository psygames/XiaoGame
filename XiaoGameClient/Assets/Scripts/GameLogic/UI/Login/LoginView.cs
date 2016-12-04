using UnityEngine;
using UnityEngine.UI;
using System.Collections;

namespace RedStone
{
	public class LoginView : ViewBase
	{
		public UIEventListener playListener;
		public Text matchingText;
		private bool isMatching = false;

		public override void OnInit()
		{
			base.OnInit();
			playListener.onClick = OnClickPlay;
			Register(Event.Gomuku.AssignRoomReply, OnAssignRoomReply);
		}

		public override void OnDestory()
		{
			UnRegister(Event.Gomuku.AssignRoomReply, OnAssignRoomReply);

			base.OnDestory();
		}

		public void OnAssignRoomReply()
		{
			isMatching = false;
		}

		public void OnClickPlay(UIEventListener listener)
		{
			GetProxy<HallProxy>().AssignRoom();
			isMatching = true;
		}

		void Update()
		{
			UpdateMatching();
		}

		private void UpdateMatching()
		{
			if (isMatching)
			{
				playListener.gameObject.SetActive(false);
				matchingText.gameObject.SetActive(true);
			}
			else 
			{
				playListener.gameObject.SetActive(true);
				matchingText.gameObject.SetActive(false);
			}
		}
	}
}