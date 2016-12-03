using UnityEngine;
using System.Collections;

namespace RedStone
{
	public class LoginView : ViewBase
	{
		void Start()
		{

		}

		void Update()
		{

		}

		public void OnLogin()
		{
			GetProxy<HallProxy>().AssignRoom();
		}
	}
}