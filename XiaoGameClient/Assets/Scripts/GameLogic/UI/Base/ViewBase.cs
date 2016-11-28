using System;
using UnityEngine;
namespace RedStone
{
	public class ViewBase : MonoBehaviour
	{
		public T GetProxy<T>() where T : ProxyBase
		{
			return ProxyManager.instance.GetProxy<T>();
		}

		public void Register<T>(string eventName, Action<T> callback)
		{
			EventManager.instance.Register<T>(eventName, callback);
		}

		public void Register(string eventName, Action callback)
		{
			EventManager.instance.Register(eventName, callback);
		}

		protected virtual void OnInit()
		{

		}
	}
}
