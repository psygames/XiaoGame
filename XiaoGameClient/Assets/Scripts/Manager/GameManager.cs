using UnityEngine;
using System.Collections;

public class GameManager : MonoBehaviour
{
	private Connection m_connection;

	void Awake()
	{
		Init();
	}

	void Start()
	{

	}

	void Update()
	{

	}

	public void Init()
	{
		m_connection = new Connection();
		m_connection.Init("ws://192.168.10.105:8080/test.websocket/test", OnMessage, OnOpen, OnClose, OnError);
	}

	public void Connect()
	{
		m_connection.Connect();
	}

	public void Close()
	{
		m_connection.Close();
	}

	public void Send()
	{
		byte[] data = new byte[] { 0xAA, 0xBB, 0xCC, 0xDD };
		m_connection.Send(data);
		Debug.Log("send: " + ByteArrayToHex(data));
	}

	public void OnMessage(byte[] data)
	{
		Debug.Log("receive: " + ByteArrayToHex(data));
	}

	private void OnClose()
	{
		Debug.Log("socket closed");
	}

	private void OnOpen()
	{
		Debug.Log("socket opened");
	}

	private void OnError(string msg)
	{
		Debug.Log("socket error: " + msg);
	}

	private string ByteArrayToHex(byte[] bytes)
	{
		string hexString = string.Empty;
		if (bytes != null)
		{
			System.Text.StringBuilder strB = new System.Text.StringBuilder();
			for (int i = 0; i < bytes.Length; i++)
			{
				strB.Append(bytes[i].ToString("X2"));
			}
			hexString = strB.ToString();
		}
		return hexString;
	}

}
