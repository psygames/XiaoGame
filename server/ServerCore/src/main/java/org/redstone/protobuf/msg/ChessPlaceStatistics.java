// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: PlaceStatisticsSync.proto

package org.redstone.protobuf.msg;

/**
 * Protobuf type {@code org.redstone.protobuf.msg.ChessPlaceStatistics}
 */
public  final class ChessPlaceStatistics extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:org.redstone.protobuf.msg.ChessPlaceStatistics)
    ChessPlaceStatisticsOrBuilder {
  // Use ChessPlaceStatistics.newBuilder() to construct.
  private ChessPlaceStatistics(com.google.protobuf.GeneratedMessage.Builder builder) {
    super(builder);
  }
  private ChessPlaceStatistics() {
    num_ = 0;
    ratio_ = 0F;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ChessPlaceStatistics(
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
            bitField0_ |= 0x00000001;
            num_ = input.readInt32();
            break;
          }
          case 21: {
            bitField0_ |= 0x00000002;
            ratio_ = input.readFloat();
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
    return org.redstone.protobuf.msg.PlaceStatisticsSyncOuterClass.internal_static_org_redstone_protobuf_msg_ChessPlaceStatistics_descriptor;
  }

  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.redstone.protobuf.msg.PlaceStatisticsSyncOuterClass.internal_static_org_redstone_protobuf_msg_ChessPlaceStatistics_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.redstone.protobuf.msg.ChessPlaceStatistics.class, org.redstone.protobuf.msg.ChessPlaceStatistics.Builder.class);
  }

  public static final com.google.protobuf.Parser<ChessPlaceStatistics> PARSER =
      new com.google.protobuf.AbstractParser<ChessPlaceStatistics>() {
    public ChessPlaceStatistics parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new ChessPlaceStatistics(input, extensionRegistry);
    }
  };

  @java.lang.Override
  public com.google.protobuf.Parser<ChessPlaceStatistics> getParserForType() {
    return PARSER;
  }

  private int bitField0_;
  public static final int NUM_FIELD_NUMBER = 1;
  private int num_;
  /**
   * <code>optional int32 num = 1;</code>
   *
   * <pre>
   * 棋子编号
   * </pre>
   */
  public boolean hasNum() {
    return ((bitField0_ & 0x00000001) == 0x00000001);
  }
  /**
   * <code>optional int32 num = 1;</code>
   *
   * <pre>
   * 棋子编号
   * </pre>
   */
  public int getNum() {
    return num_;
  }

  public static final int RATIO_FIELD_NUMBER = 2;
  private float ratio_;
  /**
   * <code>optional float ratio = 2;</code>
   *
   * <pre>
   * 棋子放置率
   * </pre>
   */
  public boolean hasRatio() {
    return ((bitField0_ & 0x00000002) == 0x00000002);
  }
  /**
   * <code>optional float ratio = 2;</code>
   *
   * <pre>
   * 棋子放置率
   * </pre>
   */
  public float getRatio() {
    return ratio_;
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
      output.writeInt32(1, num_);
    }
    if (((bitField0_ & 0x00000002) == 0x00000002)) {
      output.writeFloat(2, ratio_);
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
        .computeInt32Size(1, num_);
    }
    if (((bitField0_ & 0x00000002) == 0x00000002)) {
      size += com.google.protobuf.CodedOutputStream
        .computeFloatSize(2, ratio_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSerializedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  public static org.redstone.protobuf.msg.ChessPlaceStatistics parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.redstone.protobuf.msg.ChessPlaceStatistics parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.redstone.protobuf.msg.ChessPlaceStatistics parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.redstone.protobuf.msg.ChessPlaceStatistics parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.redstone.protobuf.msg.ChessPlaceStatistics parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return PARSER.parseFrom(input);
  }
  public static org.redstone.protobuf.msg.ChessPlaceStatistics parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseFrom(input, extensionRegistry);
  }
  public static org.redstone.protobuf.msg.ChessPlaceStatistics parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return PARSER.parseDelimitedFrom(input);
  }
  public static org.redstone.protobuf.msg.ChessPlaceStatistics parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseDelimitedFrom(input, extensionRegistry);
  }
  public static org.redstone.protobuf.msg.ChessPlaceStatistics parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return PARSER.parseFrom(input);
  }
  public static org.redstone.protobuf.msg.ChessPlaceStatistics parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseFrom(input, extensionRegistry);
  }

  public static Builder newBuilder() { return new Builder(); }
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder(org.redstone.protobuf.msg.ChessPlaceStatistics prototype) {
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
   * Protobuf type {@code org.redstone.protobuf.msg.ChessPlaceStatistics}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:org.redstone.protobuf.msg.ChessPlaceStatistics)
      org.redstone.protobuf.msg.ChessPlaceStatisticsOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.redstone.protobuf.msg.PlaceStatisticsSyncOuterClass.internal_static_org_redstone_protobuf_msg_ChessPlaceStatistics_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.redstone.protobuf.msg.PlaceStatisticsSyncOuterClass.internal_static_org_redstone_protobuf_msg_ChessPlaceStatistics_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.redstone.protobuf.msg.ChessPlaceStatistics.class, org.redstone.protobuf.msg.ChessPlaceStatistics.Builder.class);
    }

    // Construct using org.redstone.protobuf.msg.ChessPlaceStatistics.newBuilder()
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
      }
    }
    public Builder clear() {
      super.clear();
      num_ = 0;
      bitField0_ = (bitField0_ & ~0x00000001);
      ratio_ = 0F;
      bitField0_ = (bitField0_ & ~0x00000002);
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.redstone.protobuf.msg.PlaceStatisticsSyncOuterClass.internal_static_org_redstone_protobuf_msg_ChessPlaceStatistics_descriptor;
    }

    public org.redstone.protobuf.msg.ChessPlaceStatistics getDefaultInstanceForType() {
      return org.redstone.protobuf.msg.ChessPlaceStatistics.getDefaultInstance();
    }

    public org.redstone.protobuf.msg.ChessPlaceStatistics build() {
      org.redstone.protobuf.msg.ChessPlaceStatistics result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public org.redstone.protobuf.msg.ChessPlaceStatistics buildPartial() {
      org.redstone.protobuf.msg.ChessPlaceStatistics result = new org.redstone.protobuf.msg.ChessPlaceStatistics(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
        to_bitField0_ |= 0x00000001;
      }
      result.num_ = num_;
      if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
        to_bitField0_ |= 0x00000002;
      }
      result.ratio_ = ratio_;
      result.bitField0_ = to_bitField0_;
      onBuilt();
      return result;
    }

    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof org.redstone.protobuf.msg.ChessPlaceStatistics) {
        return mergeFrom((org.redstone.protobuf.msg.ChessPlaceStatistics)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.redstone.protobuf.msg.ChessPlaceStatistics other) {
      if (other == org.redstone.protobuf.msg.ChessPlaceStatistics.getDefaultInstance()) return this;
      if (other.hasNum()) {
        setNum(other.getNum());
      }
      if (other.hasRatio()) {
        setRatio(other.getRatio());
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
      org.redstone.protobuf.msg.ChessPlaceStatistics parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.redstone.protobuf.msg.ChessPlaceStatistics) e.getUnfinishedMessage();
        throw e;
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private int num_ ;
    /**
     * <code>optional int32 num = 1;</code>
     *
     * <pre>
     * 棋子编号
     * </pre>
     */
    public boolean hasNum() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional int32 num = 1;</code>
     *
     * <pre>
     * 棋子编号
     * </pre>
     */
    public int getNum() {
      return num_;
    }
    /**
     * <code>optional int32 num = 1;</code>
     *
     * <pre>
     * 棋子编号
     * </pre>
     */
    public Builder setNum(int value) {
      bitField0_ |= 0x00000001;
      num_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 num = 1;</code>
     *
     * <pre>
     * 棋子编号
     * </pre>
     */
    public Builder clearNum() {
      bitField0_ = (bitField0_ & ~0x00000001);
      num_ = 0;
      onChanged();
      return this;
    }

    private float ratio_ ;
    /**
     * <code>optional float ratio = 2;</code>
     *
     * <pre>
     * 棋子放置率
     * </pre>
     */
    public boolean hasRatio() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional float ratio = 2;</code>
     *
     * <pre>
     * 棋子放置率
     * </pre>
     */
    public float getRatio() {
      return ratio_;
    }
    /**
     * <code>optional float ratio = 2;</code>
     *
     * <pre>
     * 棋子放置率
     * </pre>
     */
    public Builder setRatio(float value) {
      bitField0_ |= 0x00000002;
      ratio_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional float ratio = 2;</code>
     *
     * <pre>
     * 棋子放置率
     * </pre>
     */
    public Builder clearRatio() {
      bitField0_ = (bitField0_ & ~0x00000002);
      ratio_ = 0F;
      onChanged();
      return this;
    }

    // @@protoc_insertion_point(builder_scope:org.redstone.protobuf.msg.ChessPlaceStatistics)
  }

  // @@protoc_insertion_point(class_scope:org.redstone.protobuf.msg.ChessPlaceStatistics)
  private static final org.redstone.protobuf.msg.ChessPlaceStatistics defaultInstance;static {
    defaultInstance = new org.redstone.protobuf.msg.ChessPlaceStatistics();
  }

  public static org.redstone.protobuf.msg.ChessPlaceStatistics getDefaultInstance() {
    return defaultInstance;
  }

  public org.redstone.protobuf.msg.ChessPlaceStatistics getDefaultInstanceForType() {
    return defaultInstance;
  }

}

