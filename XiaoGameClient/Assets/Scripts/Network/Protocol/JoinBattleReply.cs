//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

// Generated from: JoinBattleReply.proto
namespace message
{
  [global::System.Serializable, global::ProtoBuf.ProtoContract(Name=@"JoinBattleReply")]
  public partial class JoinBattleReply : global::ProtoBuf.IExtensible
  {
    public JoinBattleReply() {}
    
    private string _address = "";
    [global::ProtoBuf.ProtoMember(1, IsRequired = false, Name=@"address", DataFormat = global::ProtoBuf.DataFormat.Default)]
    [global::System.ComponentModel.DefaultValue("")]
    public string address
    {
      get { return _address; }
      set { _address = value; }
    }
    private int _roomId = default(int);
    [global::ProtoBuf.ProtoMember(2, IsRequired = false, Name=@"roomId", DataFormat = global::ProtoBuf.DataFormat.TwosComplement)]
    [global::System.ComponentModel.DefaultValue(default(int))]
    public int roomId
    {
      get { return _roomId; }
      set { _roomId = value; }
    }
    private global::ProtoBuf.IExtension extensionObject;
    global::ProtoBuf.IExtension global::ProtoBuf.IExtensible.GetExtensionObject(bool createIfMissing)
      { return global::ProtoBuf.Extensible.GetExtensionObject(ref extensionObject, createIfMissing); }
  }
  
}
