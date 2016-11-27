using UnityEngine;
using System.Collections;

namespace RedStone
{
	public class LoginView : ViewBase
	{
		protected T GetProxy<T>() where T : ProxyBase
		{
			return ProxyManager.instance.GetProxy<T>();
		}

		void Start()
		{

		}

		void Update()
		{

		}

		public void OnLogin()
		{
			GetProxy<HallProxy>().Login(SystemInfo.deviceUniqueIdentifier);
		}
	}
}