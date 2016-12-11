// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: JoinRoomReply.proto

package org.redstone.protobuf.msg;

/**
 * Protobuf type {@code org.redstone.protobuf.msg.JoinRoomReply}
 */
public  final class JoinRoomReply extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:org.redstone.protobuf.msg.JoinRoomReply)
    JoinRoomReplyOrBuilder {
  // Use JoinRoomReply.newBuilder() to construct.
  private JoinRoomReply(com.google.protobuf.GeneratedMessage.Builder builder) {
    super(builder);
  }
  private JoinRoomReply() {
    camp_ = 1;
    roomId_ = 0;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private JoinRoomReply(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!parseUnknownField(input, unknownFields,
                                   extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
          case 8: {
            int rawValue = input.readEnum();
            org.redstone.protobuf.msg.Enums.Camp value = org.redstone.protobuf.msg.Enums.Camp.valueOf(rawValue);
            if (value == null) {
              unknownFields.mergeVarintField(1, rawValue);
            } else {
              bitField0_ |= 0x00000001;
              camp_ = rawValue;
            }
            break;
          }
          case 16: {
            bitField0_ |= 0x00000002;
            roomId_ = input.readInt32();
            break;
          }
          case 34: {
            org.redstone.protobuf.msg.NewTurnBroadcast.Builder subBuilder = null;
            if (((bitField0_ & 0x00000004) == 0x00000004)) {
              subBuilder = newTurn_.toBuilder();
            }
            newTurn_ = input.readMessage(org.redstone.protobuf.msg.NewTurnBroadcast.PARSER, extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(newTurn_);
              newTurn_ = subBuilder.buildPartial();
            }
            bitField0_ |= 0x00000004;
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e.getMessage()).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.redstone.protobuf.msg.JoinRoomReplyOuterClass.internal_static_org_redstone_protobuf_msg_JoinRoomReply_descriptor;
  }

  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.redstone.protobuf.msg.JoinRoomReplyOuterClass.internal_static_org_redstone_protobuf_msg_JoinRoomReply_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.redstone.protobuf.msg.JoinRoomReply.class, org.redstone.protobuf.msg.JoinRoomReply.Builder.class);
  }

  public static final com.google.protobuf.Parser<JoinRoomReply> PARSER =
      new com.google.protobuf.AbstractParser<JoinRoomReply>() {
    public JoinRoomReply parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new JoinRoomReply(input, extensionRegistry);
    }
  };

  @java.lang.Override
  public com.google.protobuf.Parser<JoinRoomReply> getParserForType() {
    return PARSER;
  }

  private int bitField0_;
  public static final int CAMP_FIELD_NUMBER = 1;
  private int camp_;
  /**
   * <code>optional .org.redstone.protobuf.msg.Enums.Camp camp = 1 [default = NoneCamp];</code>
   */
  public boolean hasCamp() {
    return ((bitField0_ & 0x00000001) == 0x00000001);
  }
  /**
   * <code>optional .org.redstone.protobuf.msg.Enums.Camp camp = 1 [default = NoneCamp];</code>
   */
  public org.redstone.protobuf.msg.Enums.Camp getCamp() {
    org.redstone.protobuf.msg.Enums.Camp result = org.redstone.protobuf.msg.Enums.Camp.valueOf(camp_);
    return result == null ? org.redstone.protobuf.msg.Enums.Camp.NoneCamp : result;
  }

  public static final int ROOMID_FIELD_NUMBER = 2;
  private int roomId_;
  /**
   * <code>optional int32 roomId = 2;</code>
   */
  public boolean hasRoomId() {
    return ((bitField0_ & 0x00000002) == 0x00000002);
  }
  /**
   * <code>optional int32 roomId = 2;</code>
   */
  public int getRoomId() {
    return roomId_;
  }

  public static final int NEWTURN_FIELD_NUMBER = 4;
  private org.redstone.protobuf.msg.NewTurnBroadcast newTurn_;
  /**
   * <code>optional .org.redstone.protobuf.msg.NewTurnBroadcast newTurn = 4;</code>
   */
  public boolean hasNewTurn() {
    return ((bitField0_ & 0x00000004) == 0x00000004);
  }
  /**
   * <code>optional .org.redstone.protobuf.msg.NewTurnBroadcast newTurn = 4;</code>
   */
  public org.redstone.protobuf.msg.NewTurnBroadcast getNewTurn() {
    return newTurn_ == null ? org.redstone.protobuf.msg.NewTurnBroadcast.getDefaultInstance() : newTurn_;
  }
  /**
   * <code>optional .org.redstone.protobuf.msg.NewTurnBroadcast newTurn = 4;</code>
   */
  public org.redstone.protobuf.msg.NewTurnBroadcastOrBuilder getNewTurnOrBuilder() {
    return newTurn_ == null ? org.redstone.protobuf.msg.NewTurnBroadcast.getDefaultInstance() : newTurn_;
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    getSerializedSize();
    if (((bitField0_ & 0x00000001) == 0x00000001)) {
      output.writeEnum(1, camp_);
    }
    if (((bitField0_ & 0x00000002) == 0x00000002)) {
      output.writeInt32(2, roomId_);
    }
    if (((bitField0_ & 0x00000004) == 0x00000004)) {
      output.writeMessage(4, getNewTurn());
    }
    unknownFields.writeTo(output);
  }

  private int memoizedSerializedSize = -1;
  public int getSerializedSize() {
    int size = memoizedSerializedSize;
    if (size != -1) return size;

    size = 0;
    if (((bitField0_ & 0x00000001) == 0x00000001)) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(1, camp_);
    }
    if (((bitField0_ & 0x00000002) == 0x00000002)) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, roomId_);
    }
    if (((bitField0_ & 0x00000004) == 0x00000004)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(4, getNewTurn());
    }
    size += unknownFields.getSerializedSize();
    memoizedSerializedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  public static org.redstone.protobuf.msg.JoinRoomReply parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.redstone.protobuf.msg.JoinRoomReply parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.redstone.protobuf.msg.JoinRoomReply parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.redstone.protobuf.msg.JoinRoomReply parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.redstone.protobuf.msg.JoinRoomReply parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return PARSER.parseFrom(input);
  }
  public static org.redstone.protobuf.msg.JoinRoomReply parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseFrom(input, extensionRegistry);
  }
  public static org.redstone.protobuf.msg.JoinRoomReply parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return PARSER.parseDelimitedFrom(input);
  }
  public static org.redstone.protobuf.msg.JoinRoomReply parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseDelimitedFrom(input, extensionRegistry);
  }
  public static org.redstone.protobuf.msg.JoinRoomReply parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return PARSER.parseFrom(input);
  }
  public static org.redstone.protobuf.msg.JoinRoomReply parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseFrom(input, extensionRegistry);
  }

  public static Builder newBuilder() { return new Builder(); }
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder(org.redstone.protobuf.msg.JoinRoomReply prototype) {
    return newBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() { return newBuilder(this); }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessage.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code org.redstone.protobuf.msg.JoinRoomReply}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:org.redstone.protobuf.msg.JoinRoomReply)
      org.redstone.protobuf.msg.JoinRoomReplyOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.redstone.protobuf.msg.JoinRoomReplyOuterClass.internal_static_org_redstone_protobuf_msg_JoinRoomReply_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.redstone.protobuf.msg.JoinRoomReplyOuterClass.internal_static_org_redstone_protobuf_msg_JoinRoomReply_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.redstone.protobuf.msg.JoinRoomReply.class, org.redstone.protobuf.msg.JoinRoomReply.Builder.class);
    }

    // Construct using org.redstone.protobuf.msg.JoinRoomReply.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        getNewTurnFieldBuilder();
      }
    }
    public Builder clear() {
      super.clear();
      camp_ = 1;
      bitField0_ = (bitField0_ & ~0x00000001);
      roomId_ = 0;
      bitField0_ = (bitField0_ & ~0x00000002);
      if (newTurnBuilder_ == null) {
        newTurn_ = null;
      } else {
        newTurnBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000004);
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.redstone.protobuf.msg.JoinRoomReplyOuterClass.internal_static_org_redstone_protobuf_msg_JoinRoomReply_descriptor;
    }

    public org.redstone.protobuf.msg.JoinRoomReply getDefaultInstanceForType() {
      return org.redstone.protobuf.msg.JoinRoomReply.getDefaultInstance();
    }

    public org.redstone.protobuf.msg.JoinRoomReply build() {
      org.redstone.protobuf.msg.JoinRoomReply result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public org.redstone.protobuf.msg.JoinRoomReply buildPartial() {
      org.redstone.protobuf.msg.JoinRoomReply result = new org.redstone.protobuf.msg.JoinRoomReply(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
        to_bitField0_ |= 0x00000001;
      }
      result.camp_ = camp_;
      if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
        to_bitField0_ |= 0x00000002;
      }
      result.roomId_ = roomId_;
      if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
        to_bitField0_ |= 0x00000004;
      }
      if (newTurnBuilder_ == null) {
        result.newTurn_ = newTurn_;
      } else {
        result.newTurn_ = newTurnBuilder_.build();
      }
      result.bitField0_ = to_bitField0_;
      onBuilt();
      return result;
    }

    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof org.redstone.protobuf.msg.JoinRoomReply) {
        return mergeFrom((org.redstone.protobuf.msg.JoinRoomReply)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.redstone.protobuf.msg.JoinRoomReply other) {
      if (other == org.redstone.protobuf.msg.JoinRoomReply.getDefaultInstance()) return this;
      if (other.hasCamp()) {
        setCamp(other.getCamp());
      }
      if (other.hasRoomId()) {
        setRoomId(other.getRoomId());
      }
      if (other.hasNewTurn()) {
        mergeNewTurn(other.getNewTurn());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      org.redstone.protobuf.msg.JoinRoomReply parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.redstone.protobuf.msg.JoinRoomReply) e.getUnfinishedMessage();
        throw e;
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private int camp_ = 1;
    /**
     * <code>optional .org.redstone.protobuf.msg.Enums.Camp camp = 1 [default = NoneCamp];</code>
     */
    public boolean hasCamp() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional .org.redstone.protobuf.msg.Enums.Camp camp = 1 [default = NoneCamp];</code>
     */
    public org.redstone.protobuf.msg.Enums.Camp getCamp() {
      org.redstone.protobuf.msg.Enums.Camp result = org.redstone.protobuf.msg.Enums.Camp.valueOf(camp_);
      return result == null ? org.redstone.protobuf.msg.Enums.Camp.NoneCamp : result;
    }
    /**
     * <code>optional .org.redstone.protobuf.msg.Enums.Camp camp = 1 [default = NoneCamp];</code>
     */
    public Builder setCamp(org.redstone.protobuf.msg.Enums.Camp value) {
      if (value == null) {
        throw new NullPointerException();
      }
      bitField0_ |= 0x00000001;
      camp_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>optional .org.redstone.protobuf.msg.Enums.Camp camp = 1 [default = NoneCamp];</code>
     */
    public Builder clearCamp() {
      bitField0_ = (bitField0_ & ~0x00000001);
      camp_ = 1;
      onChanged();
      return this;
    }

    private int roomId_ ;
    /**
     * <code>optional int32 roomId = 2;</code>
     */
    public boolean hasRoomId() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional int32 roomId = 2;</code>
     */
    public int getRoomId() {
      return roomId_;
    }
    /**
     * <code>optional int32 roomId = 2;</code>
     */
    public Builder setRoomId(int value) {
      bitField0_ |= 0x00000002;
      roomId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 roomId = 2;</code>
     */
    public Builder clearRoomId() {
      bitField0_ = (bitField0_ & ~0x00000002);
      roomId_ = 0;
      onChanged();
      return this;
    }

    private org.redstone.protobuf.msg.NewTurnBroadcast newTurn_ = null;
    private com.google.protobuf.SingleFieldBuilder<
        org.redstone.protobuf.msg.NewTurnBroadcast, org.redstone.protobuf.msg.NewTurnBroadcast.Builder, org.redstone.protobuf.msg.NewTurnBroadcastOrBuilder> newTurnBuilder_;
    /**
     * <code>optional .org.redstone.protobuf.msg.NewTurnBroadcast newTurn = 4;</code>
     */
    public boolean hasNewTurn() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional .org.redstone.protobuf.msg.NewTurnBroadcast newTurn = 4;</code>
     */
    public org.redstone.protobuf.msg.NewTurnBroadcast getNewTurn() {
      if (newTurnBuilder_ == null) {
        return newTurn_ == null ? org.redstone.protobuf.msg.NewTurnBroadcast.getDefaultInstance() : newTurn_;
      } else {
        return newTurnBuilder_.getMessage();
      }
    }
    /**
     * <code>optional .org.redstone.protobuf.msg.NewTurnBroadcast newTurn = 4;</code>
     */
    public Builder setNewTurn(org.redstone.protobuf.msg.NewTurnBroadcast value) {
      if (newTurnBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        newTurn_ = value;
        onChanged();
      } else {
        newTurnBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000004;
      return this;
    }
    /**
     * <code>optional .org.redstone.protobuf.msg.NewTurnBroadcast newTurn = 4;</code>
     */
    public Builder setNewTurn(
        org.redstone.protobuf.msg.NewTurnBroadcast.Builder builderForValue) {
      if (newTurnBuilder_ == null) {
        newTurn_ = builderForValue.build();
        onChanged();
      } else {
        newTurnBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000004;
      return this;
    }
    /**
     * <code>optional .org.redstone.protobuf.msg.NewTurnBroadcast newTurn = 4;</code>
     */
    public Builder mergeNewTurn(org.redstone.protobuf.msg.NewTurnBroadcast value) {
      if (newTurnBuilder_ == null) {
        if (((bitField0_ & 0x00000004) == 0x00000004) &&
            newTurn_ != null &&
            newTurn_ != org.redstone.protobuf.msg.NewTurnBroadcast.getDefaultInstance()) {
          newTurn_ =
            org.redstone.protobuf.msg.NewTurnBroadcast.newBuilder(newTurn_).mergeFrom(value).buildPartial();
        } else {
          newTurn_ = value;
        }
        onChanged();
      } else {
        newTurnBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000004;
      return this;
    }
    /**
     * <code>optional .org.redstone.protobuf.msg.NewTurnBroadcast newTurn = 4;</code>
     */
    public Builder clearNewTurn() {
      if (newTurnBuilder_ == null) {
        newTurn_ = null;
        onChanged();
      } else {
        newTurnBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000004);
      return this;
    }
    /**
     * <code>optional .org.redstone.protobuf.msg.NewTurnBroadcast newTurn = 4;</code>
     */
    public org.redstone.protobuf.msg.NewTurnBroadcast.Builder getNewTurnBuilder() {
      bitField0_ |= 0x00000004;
      onChanged();
      return getNewTurnFieldBuilder().getBuilder();
    }
    /**
     * <code>optional .org.redstone.protobuf.msg.NewTurnBroadcast newTurn = 4;</code>
     */
    public org.redstone.protobuf.msg.NewTurnBroadcastOrBuilder getNewTurnOrBuilder() {
      if (newTurnBuilder_ != null) {
        return newTurnBuilder_.getMessageOrBuilder();
      } else {
        return newTurn_ == null ?
            org.redstone.protobuf.msg.NewTurnBroadcast.getDefaultInstance() : newTurn_;
      }
    }
    /**
     * <code>optional .org.redstone.protobuf.msg.NewTurnBroadcast newTurn = 4;</code>
     */
    private com.google.protobuf.SingleFieldBuilder<
        org.redstone.protobuf.msg.NewTurnBroadcast, org.redstone.protobuf.msg.NewTurnBroadcast.Builder, org.redstone.protobuf.msg.NewTurnBroadcastOrBuilder> 
        getNewTurnFieldBuilder() {
      if (newTurnBuilder_ == null) {
        newTurnBuilder_ = new com.google.protobuf.SingleFieldBuilder<
            org.redstone.protobuf.msg.NewTurnBroadcast, org.redstone.protobuf.msg.NewTurnBroadcast.Builder, org.redstone.protobuf.msg.NewTurnBroadcastOrBuilder>(
                getNewTurn(),
                getParentForChildren(),
                isClean());
        newTurn_ = null;
      }
      return newTurnBuilder_;
    }

    // @@protoc_insertion_point(builder_scope:org.redstone.protobuf.msg.JoinRoomReply)
  }

  // @@protoc_insertion_point(class_scope:org.redstone.protobuf.msg.JoinRoomReply)
  private static final org.redstone.protobuf.msg.JoinRoomReply defaultInstance;static {
    defaultInstance = new org.redstone.protobuf.msg.JoinRoomReply();
  }

  public static org.redstone.protobuf.msg.JoinRoomReply getDefaultInstance() {
    return defaultInstance;
  }

  public org.redstone.protobuf.msg.JoinRoomReply getDefaultInstanceForType() {
    return defaultInstance;
  }

}

