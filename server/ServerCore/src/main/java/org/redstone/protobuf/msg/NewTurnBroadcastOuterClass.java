// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: NewTurnBroadcast.proto

package org.redstone.protobuf.msg;

public final class NewTurnBroadcastOuterClass {
  private NewTurnBroadcastOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_org_redstone_protobuf_msg_ChessRow_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_org_redstone_protobuf_msg_ChessRow_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_org_redstone_protobuf_msg_BoardSync_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_org_redstone_protobuf_msg_BoardSync_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_org_redstone_protobuf_msg_NewTurnBroadcast_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_org_redstone_protobuf_msg_NewTurnBroadcast_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\026NewTurnBroadcast.proto\022\031org.redstone.p" +
      "rotobuf.msg\032\013Enums.proto\"E\n\010ChessRow\0229\n\005" +
      "types\030\001 \003(\0162*.org.redstone.protobuf.msg." +
      "Enums.ChessType\">\n\tBoardSync\0221\n\004rows\030\001 \003" +
      "(\0132#.org.redstone.protobuf.msg.ChessRow\"" +
      "\212\001\n\020NewTurnBroadcast\022=\n\004camp\030\001 \001(\0162%.org" +
      ".redstone.protobuf.msg.Enums.Camp:\010NoneC" +
      "amp\0227\n\tboardSync\030\002 \001(\0132$.org.redstone.pr" +
      "otobuf.msg.BoardSyncB\002P\001"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          org.redstone.protobuf.msg.EnumsOuterClass.getDescriptor(),
        }, assigner);
    internal_static_org_redstone_protobuf_msg_ChessRow_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_org_redstone_protobuf_msg_ChessRow_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_org_redstone_protobuf_msg_ChessRow_descriptor,
        new java.lang.String[] { "Types", });
    internal_static_org_redstone_protobuf_msg_BoardSync_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_org_redstone_protobuf_msg_BoardSync_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_org_redstone_protobuf_msg_BoardSync_descriptor,
        new java.lang.String[] { "Rows", });
    internal_static_org_redstone_protobuf_msg_NewTurnBroadcast_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_org_redstone_protobuf_msg_NewTurnBroadcast_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_org_redstone_protobuf_msg_NewTurnBroadcast_descriptor,
        new java.lang.String[] { "Camp", "BoardSync", });
    org.redstone.protobuf.msg.EnumsOuterClass.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
