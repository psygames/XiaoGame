using UnityEngine;
using System.Collections;
using WebSocketSharp;
using System;

namespace RedStone
{
	public class Connection
	{
		private WebSocket m_socket;

		public void Init(string addr, Action<byte[]> onMessage, Action onOpen, Action onClose, Action<string> onError)
		{
			m_socket = new WebSocket(addr);
			m_socket.OnMessage += (sender, e) =>
			{
				onMessage.Invoke(e.RawData);
			};
			m_socket.OnOpen += (sender, e) =>
			{
				m_isConnecting = false;
				onOpen.Invoke();
			};
			m_socket.OnError += (sender, e) =>
			{
				m_isConnecting = false;
				m_isSending = false;
				m_isClosing = false;
				onError.Invoke(e.Message);
			};
			m_socket.OnClose += (sender, e) =>
			{
				m_isClosing = false;
				m_isSending = false;
				m_isConnecting = false;
				onClose.Invoke();
			};
		}

		public bool isInit { get { return m_socket != null; } }
		public bool isConnected { get { return m_socket.IsConnected; } }

		public bool isConnecting { get { return m_isConnecting; } }
		public bool isClosing { get { return m_isClosing; } }
		public bool isSending { get { return m_isSending; } }

		private bool isInitAndConnected { get { return isInit && isConnected; } }

		private bool m_isConnecting = false;
		private bool m_isClosing = false;
		private bool m_isSending = false;

		public void Connect()
		{
			if (isInit)
			{
				m_socket.ConnectAsync();
				m_isConnecting = true;
			}
		}

		public void Close()
		{
			if (isInitAndConnected)
			{
				m_socket.CloseAsync();
				m_isConnecting = true;
			}
		}

		public void Send(byte[] content)
		{
			if (isInitAndConnected)
			{
				m_socket.SendAsync(content, (finish) => { m_isSending = finish; });
				m_isSending = true;
			}
		}
	}
}