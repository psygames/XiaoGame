﻿using System;
using System.Collections.Generic;

namespace RedStone
{
	public class EventManager : Core.Singleton<EventManager>
	{
		private Dictionary<string, Dictionary<int, Action<object>>> m_handlers = new Dictionary<string, Dictionary<int, Action<object>>>();

		public void Register<T>(string eventName, Action<T> handler)
		{
			if (!m_handlers.ContainsKey(eventName))
				m_handlers.Add(eventName, new Dictionary<int, Action<object>>());

			m_handlers[eventName].Add(handler.GetHashCode(), (obj) => { handler.Invoke((T)obj); });
		}

		public void UnRegister<T>(string eventName, Action<T> handler)
		{
			if (!m_handlers.ContainsKey(eventName))
				return;

			m_handlers[eventName].Remove(handler.GetHashCode());
			if (m_handlers[eventName].Count == 0)
				m_handlers.Remove(eventName);
		}

		public void Send(string eventName, object param)
		{
			if (!m_handlers.ContainsKey(eventName))
				return;
			foreach (var action in m_handlers[eventName])
				action.Value.Invoke(param);
		}

		public void ClearAll()
		{
			m_handlers.Clear();
		}
	}
}