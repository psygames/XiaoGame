using System;
using System.Collections.Generic;
using System.IO;
using UnityEngine;

namespace RedStone
{
	public class NetworkManager : Core.Singleton<NetworkManager>
	{
		private Connection m_connection;
		private List<byte[]> m_receiveQueue = new List<byte[]>();
		private Dictionary<string, int> m_protocolNum = new Dictionary<string, int>();
		private Dictionary<int, string> m_numProtocal = new Dictionary<int, string>();

		public void Init()
		{
			ProtocalNumInit();

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
			AddHeader(data, typeof(T));
			Send(data);
		}

		public void SendMessage<T1, T2>(T1 message, Action<T2> callback)
		{
			Action<T2> delegateAction = null;
			delegateAction = (obj) =>
			{
				callback(obj);
				// UnRegister when callback
				UnRegister(delegateAction);
			};
			Register<T2>(delegateAction);
		}

		private byte[] AddHeader(byte[] data, Type type)
		{
			byte[] _data = new byte[data.Length + 2];
			return _data;
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
		private object DeSerialize(byte[] msg, Type type)
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
					object result = ProtoBuf.Serializer.Deserialize(ms, type);
					return result;
				}
			}
			catch (Exception ex)
			{
				Debug.Log("反序列化失败: " + ex.ToString());
				return default(object);
			}
		}

		private void Send(byte[] data)
		{
			m_connection.Send(data);
		}

		private void OnMessage(byte[] data)
		{
			m_receiveQueue.Add(data);
			Debug.Log("receive: " + data.Length);
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

		public void Update()
		{
			for (int i = 0; i < m_receiveQueue.Count; i++)
			{
				ProcessData(m_receiveQueue[i]);
			}

			m_receiveQueue.Clear();
		}

		private void ProcessData(byte[] data)
		{
			byte[] header = new byte[2];
			Array.Copy(data, header, 2);

			byte[] body = new byte[data.Length - 2];
			Array.Copy(data, 2, body, 0, data.Length - 2);

			Type type = HeaderToType(header);
			object msg = DeSerialize(body, type);

			EventManager.instance.Send(GetEventName(msg.GetType()), msg);
		}

		#region Tools
		private void ProtocalNumInit()
		{
			string filePath = MyPath.PROJECT + "Tools/ProtoBuf/protoNum.md";
			string content = tools.FileOpt.ReadTextFromFile(filePath);
			content.Replace("\r", "");
			string[] lines = content.Split('\n');
			for (int i = 0; i < lines.Length; i++)
			{
				lines[i] = lines[i].Trim();
				if (string.IsNullOrEmpty(lines[i]))
					continue;
				string[] lineSplit = lines[i].Split('=');
				string protoName = "message." + lineSplit[0].Trim();
				int protoNum = int.Parse(lineSplit[1].Trim());
				m_protocolNum.Add(protoName, protoNum);
				m_numProtocal.Add(protoNum, protoName);
			}
		}

		private string GetEventName(Type t)
		{
			return t.ToString();
		}

		private Type HeaderToType(byte[] header)
		{
			int num = BitConvert.ToUshort(header);
			return Type.GetType(m_numProtocal[num]);
		}

		private byte[] TypeToHeader(Type t)
		{
			int num = m_protocolNum[t.ToString()];
			return BitConvert.ToBytes((ushort)num);
		}

		#endregion

		public void Register<T>(Action<T> callback)
		{
			string name = typeof(T).ToString();
			EventManager.instance.Register<T>(name, callback);
		}

		public void UnRegister<T>(Action<T> callback)
		{
			string name = typeof(T).ToString();
			EventManager.instance.UnRegister<T>(name, callback);
		}
	}
}

