using UnityEngine;
using RedStone.Proxy;
using System.Collections;

namespace RedStone
{
	public class LoginView : MonoBehaviour
	{
		protected T GetProxy<T>() where T : Proxy.ProxyBase
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
			GetProxy<HallProxy>().Login(SystemInfo.deviceUniqueIdentifier.GetHashCode());
		}
	}
}