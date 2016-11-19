using System;
using System.IO;
using UnityEngine;

namespace RedStone
{
	public class NetworkManager : Core.Singleton<NetworkManager>
	{
		private Connection m_connection;

		public void Init()
		{
			m_connection = new Connection();
			m_connection.Init("ws://192.168.10.105:8080/XiaoGameServer/test", OnMessage, OnOpen, OnClose, OnError);
			Connect();
		}

		private void Connect()
		{
			m_connection.Connect();
		}

		private void Close()
		{
			m_connection.Close();
		}

		public void SendMessage<T>(T message)
		{
			byte[] data = SerializeProto<T>(message);
			Send(data);
		}

		// 将消息序列化为二进制的方法
		// < param name="model">要序列化的对象< /param>
		private byte[] SerializeProto<T>(T model)
		{
			try
			{
				//涉及格式转换，需要用到流，将二进制序列化到流中
				using (MemoryStream ms = new MemoryStream())
				{
					//使用ProtoBuf工具的序列化方法
					ProtoBuf.Serializer.Serialize<T>(ms, model);
					//定义二级制数组，保存序列化后的结果
					byte[] result = new byte[ms.Length];
					//将流的位置设为0，起始点
					ms.Position = 0;
					//将流中的内容读取到二进制数组中
					ms.Read(result, 0, result.Length);
					return result;
				}
			}
			catch (Exception ex)
			{
				Debug.Log("序列化失败: " + ex.ToString());
				return null;
			}
		}

		// 将收到的消息反序列化成对象
		// < returns>The serialize.< /returns>
		// < param name="msg">收到的消息.</param>
		private T DeSerialize<T>(byte[] msg)
		{
			try
			{
				using (MemoryStream ms = new MemoryStream())
				{
					//将消息写入流中
					ms.Write(msg, 0, msg.Length);
					//将流的位置归0
					ms.Position = 0;
					//使用工具反序列化对象
					T result = ProtoBuf.Serializer.Deserialize<T>(ms);
					return result;
				}
			}
			catch (Exception ex)
			{
				Debug.Log("反序列化失败: " + ex.ToString());
				return default(T);
			}
		}

		private void Send(byte[] data)
		{ 
			m_connection.Send(data);
		}

		private void OnMessage(byte[] data)
		{
			message.Card card = DeSerialize<message.Card>(data);
			Debug.Log("receive: " + card.color);
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
	}
}

