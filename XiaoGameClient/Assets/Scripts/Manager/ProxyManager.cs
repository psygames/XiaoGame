using System;
using RedStone.Proxy;
namespace RedStone
{
	public class ProxyManager : Core.Singleton<ProxyManager>
	{
		private HallProxy m_hall = new HallProxy();

		public T GetProxy<T>()where T : ProxyBase
		{
			return m_hall as T;
		}
	}
}
