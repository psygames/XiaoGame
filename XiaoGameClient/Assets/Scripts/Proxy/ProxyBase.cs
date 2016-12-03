﻿using System;
using message;
namespace RedStone
{
	public class ProxyBase
	{
		public Net.Network network
		{
			get { return NetworkManager.instance.Get(netType); }
		}

		public NetType netType = NetType.Hall;

		public ProxyBase()
		{

		}

		public virtual void OnInit()
		{

		}

		public virtual void OnUpdate()
		{

		}

		public virtual void OnDestroy()
		{

		}
	}
}
