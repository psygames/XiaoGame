using UnityEngine;
using System.Collections;
using WebSocketSharp;
using System;

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
			onOpen.Invoke();
		};
		m_socket.OnError += (sender, e) =>
		{
			onError.Invoke(e.Message);
		};
		m_socket.OnClose += (sender, e) =>
		{
			onClose.Invoke();
		};
	}

	private bool isInit { get { return m_socket != null; } }
	private bool isInitAndConnected { get { return isInit && m_socket.IsConnected; } }

	public void Connect()
	{
		if (isInit)
			m_socket.Connect();
	}

	public void Close()
	{
		if (isInitAndConnected)
			m_socket.Close();
	}

	public void Send(byte[] content)
	{
		if (isInitAndConnected)
			m_socket.Send(content);
	}
}
