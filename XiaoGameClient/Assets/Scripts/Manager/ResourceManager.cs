using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
namespace RedStone
{
	public class ResourceManager : Core.SingletonBehaviour<ResourceManager>
	{
		public T LoadSync<T>(string url)
		{
			return (T)new object();
		}

		public void Load<T>(string url, Action<T> callback)
		{
			LoadReq req = new LoadReq();
			req.url = url;
			req.callback = (obj) => { callback.Invoke((T)(object)obj.texture); };
			Coroutine co = StartCoroutine("LoadWWWFunc", req);
		}

		IEnumerator LoadWWWFunc(LoadReq req)
		{
			WWW www = new WWW(req.url);
			yield return www;
			req.callback.Invoke(www);
		}

		public class LoadReq
		{
			public string url;
			public Action<WWW> callback;
		}
	}
}
