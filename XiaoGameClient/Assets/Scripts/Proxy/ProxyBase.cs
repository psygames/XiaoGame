using System;
using message;
namespace RedStone.Proxy
{
	public class ProxyBase
	{
		public NetworkManager network
		{
			get { return NetworkManager.instance; }
		}

		public ProxyBase()
		{

		}

		protected virtual void OnInit()
		{

		}

		protected virtual void OnUpdate()
		{

		}
	}
}
