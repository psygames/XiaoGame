using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace RedStone
{
    public class ProtoTool : Core.Singleton<ProtoTool>
    {
        private Dictionary<string, int> m_protocolNum = new Dictionary<string, int>();
        private Dictionary<int, string> m_numProtocal = new Dictionary<int, string>();
        public static object ToProtoObj(byte[] data)
        {
            if (data.Length < 2)
            {
                Debug.LogError("receive data length: " + data.Length);
                return null;
            }

            byte[] header = new byte[2];
            Array.Copy(data, header, 2);

            byte[] body = new byte[data.Length - 2];
            Array.Copy(data, 2, body, 0, data.Length - 2);
            Type type = instance.HeaderToType(header);

            if (type == null)
            {
                Debug.LogError("Wrong Type header : [" + header[0] + "," + header[1] + "]");
                return null;
            }
            try
            {
                object msg = instance.DeSerialize(body, type);
                return msg;
            }
            catch (Exception e)
            {
                Debug.LogError(type + " : DeSerialize Failed \n" + e);
            }
            return null;
        }

        public static byte[] ToData<T>(T proto)
        {
            byte[] data = instance.SerializeProto<T>(proto);
            instance.AddHeader(ref data, typeof(T));
            return data;
        }

        public void Init()
        {
            string content = FileOpt.ReadTextFromFile(MyPath.RES_PROTO_NUM);
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

        private Type HeaderToType(byte[] header)
        {
            int num = BitConvert.ToUshort(header);
            if (!m_numProtocal.ContainsKey(num))
            {
                Debug.LogError("not found protonum: " + num);
                return null;
            }

            Type type = Type.GetType(m_numProtocal[num]);
            if (type == null)
                Debug.LogError("null message num: " + num);
            return type;
        }

        private byte[] TypeToHeader(Type t)
        {
            int num = m_protocolNum[t.ToString()];
            return BitConvert.ToBytes((ushort)num);
        }

        private void AddHeader(ref byte[] data, Type type)
        {
            byte[] _data = new byte[data.Length + 2];
            byte[] header = TypeToHeader(type);
            Array.Copy(header, 0, _data, 0, 2);
            Array.Copy(data, 0, _data, 2, data.Length);
            data = _data;
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
                Debug.LogError("序列化失败: " + ex.ToString());
                return null;
            }

        }

        // 将收到的消息反序列化成对象
        // < returns>The serialize.< /returns>
        // < param name="msg">收到的消息.</param>
        private object DeSerialize(byte[] msg, Type type)
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
    }
}