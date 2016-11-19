using System;
using message;
namespace RedStone.Proxy
{
	public class HallProxy : ProxyBase
	{
		public HallProxy()
		{


		}

		public void Login(string uuid, string pwd = "")
		{
			Card card = new Card();
			card.color = "black";
			card.num = 7;
			card.memo = "黑桃7";

			network.SendMessage(card);
		}

	}
}
