// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: PlaceStatisticsSync.proto

package org.redstone.protobuf.msg;

/**
 * Protobuf type {@code org.redstone.protobuf.msg.PlaceStatisticsSync}
 */
public  final class PlaceStatisticsSync extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:org.redstone.protobuf.msg.PlaceStatisticsSync)
    PlaceStatisticsSyncOrBuilder {
  // Use PlaceStatisticsSync.newBuilder() to construct.
  private PlaceStatisticsSync(com.google.protobuf.GeneratedMessage.Builder builder) {
    super(builder);
  }
  private PlaceStatisticsSync() {
    statistics_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private PlaceStatisticsSync(
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
          case 10: {
            if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
              statistics_ = new java.util.ArrayList<org.redstone.protobuf.msg.ChessPlaceStatistics>();
              mutable_bitField0_ |= 0x00000001;
            }
            statistics_.add(input.readMessage(org.redstone.protobuf.msg.ChessPlaceStatistics.PARSER, extensionRegistry));
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
      if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
        statistics_ = java.util.Collections.unmodifiableList(statistics_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.redstone.protobuf.msg.PlaceStatisticsSyncOuterClass.internal_static_org_redstone_protobuf_msg_PlaceStatisticsSync_descriptor;
  }

  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.redstone.protobuf.msg.PlaceStatisticsSyncOuterClass.internal_static_org_redstone_protobuf_msg_PlaceStatisticsSync_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.redstone.protobuf.msg.PlaceStatisticsSync.class, org.redstone.protobuf.msg.PlaceStatisticsSync.Builder.class);
  }

  public static final com.google.protobuf.Parser<PlaceStatisticsSync> PARSER =
      new com.google.protobuf.AbstractParser<PlaceStatisticsSync>() {
    public PlaceStatisticsSync parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new PlaceStatisticsSync(input, extensionRegistry);
    }
  };

  @java.lang.Override
  public com.google.protobuf.Parser<PlaceStatisticsSync> getParserForType() {
    return PARSER;
  }

  public static final int STATISTICS_FIELD_NUMBER = 1;
  private java.util.List<org.redstone.protobuf.msg.ChessPlaceStatistics> statistics_;
  /**
   * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
   */
  public java.util.List<org.redstone.protobuf.msg.ChessPlaceStatistics> getStatisticsList() {
    return statistics_;
  }
  /**
   * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
   */
  public java.util.List<? extends org.redstone.protobuf.msg.ChessPlaceStatisticsOrBuilder> 
      getStatisticsOrBuilderList() {
    return statistics_;
  }
  /**
   * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
   */
  public int getStatisticsCount() {
    return statistics_.size();
  }
  /**
   * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
   */
  public org.redstone.protobuf.msg.ChessPlaceStatistics getStatistics(int index) {
    return statistics_.get(index);
  }
  /**
   * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
   */
  public org.redstone.protobuf.msg.ChessPlaceStatisticsOrBuilder getStatisticsOrBuilder(
      int index) {
    return statistics_.get(index);
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
    for (int i = 0; i < statistics_.size(); i++) {
      output.writeMessage(1, statistics_.get(i));
    }
    unknownFields.writeTo(output);
  }

  private int memoizedSerializedSize = -1;
  public int getSerializedSize() {
    int size = memoizedSerializedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < statistics_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, statistics_.get(i));
    }
    size += unknownFields.getSerializedSize();
    memoizedSerializedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  public static org.redstone.protobuf.msg.PlaceStatisticsSync parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.redstone.protobuf.msg.PlaceStatisticsSync parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.redstone.protobuf.msg.PlaceStatisticsSync parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.redstone.protobuf.msg.PlaceStatisticsSync parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.redstone.protobuf.msg.PlaceStatisticsSync parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return PARSER.parseFrom(input);
  }
  public static org.redstone.protobuf.msg.PlaceStatisticsSync parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseFrom(input, extensionRegistry);
  }
  public static org.redstone.protobuf.msg.PlaceStatisticsSync parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return PARSER.parseDelimitedFrom(input);
  }
  public static org.redstone.protobuf.msg.PlaceStatisticsSync parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseDelimitedFrom(input, extensionRegistry);
  }
  public static org.redstone.protobuf.msg.PlaceStatisticsSync parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return PARSER.parseFrom(input);
  }
  public static org.redstone.protobuf.msg.PlaceStatisticsSync parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseFrom(input, extensionRegistry);
  }

  public static Builder newBuilder() { return new Builder(); }
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder(org.redstone.protobuf.msg.PlaceStatisticsSync prototype) {
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
   * Protobuf type {@code org.redstone.protobuf.msg.PlaceStatisticsSync}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:org.redstone.protobuf.msg.PlaceStatisticsSync)
      org.redstone.protobuf.msg.PlaceStatisticsSyncOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.redstone.protobuf.msg.PlaceStatisticsSyncOuterClass.internal_static_org_redstone_protobuf_msg_PlaceStatisticsSync_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.redstone.protobuf.msg.PlaceStatisticsSyncOuterClass.internal_static_org_redstone_protobuf_msg_PlaceStatisticsSync_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.redstone.protobuf.msg.PlaceStatisticsSync.class, org.redstone.protobuf.msg.PlaceStatisticsSync.Builder.class);
    }

    // Construct using org.redstone.protobuf.msg.PlaceStatisticsSync.newBuilder()
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
        getStatisticsFieldBuilder();
      }
    }
    public Builder clear() {
      super.clear();
      if (statisticsBuilder_ == null) {
        statistics_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        statisticsBuilder_.clear();
      }
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.redstone.protobuf.msg.PlaceStatisticsSyncOuterClass.internal_static_org_redstone_protobuf_msg_PlaceStatisticsSync_descriptor;
    }

    public org.redstone.protobuf.msg.PlaceStatisticsSync getDefaultInstanceForType() {
      return org.redstone.protobuf.msg.PlaceStatisticsSync.getDefaultInstance();
    }

    public org.redstone.protobuf.msg.PlaceStatisticsSync build() {
      org.redstone.protobuf.msg.PlaceStatisticsSync result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public org.redstone.protobuf.msg.PlaceStatisticsSync buildPartial() {
      org.redstone.protobuf.msg.PlaceStatisticsSync result = new org.redstone.protobuf.msg.PlaceStatisticsSync(this);
      int from_bitField0_ = bitField0_;
      if (statisticsBuilder_ == null) {
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          statistics_ = java.util.Collections.unmodifiableList(statistics_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.statistics_ = statistics_;
      } else {
        result.statistics_ = statisticsBuilder_.build();
      }
      onBuilt();
      return result;
    }

    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof org.redstone.protobuf.msg.PlaceStatisticsSync) {
        return mergeFrom((org.redstone.protobuf.msg.PlaceStatisticsSync)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.redstone.protobuf.msg.PlaceStatisticsSync other) {
      if (other == org.redstone.protobuf.msg.PlaceStatisticsSync.getDefaultInstance()) return this;
      if (statisticsBuilder_ == null) {
        if (!other.statistics_.isEmpty()) {
          if (statistics_.isEmpty()) {
            statistics_ = other.statistics_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureStatisticsIsMutable();
            statistics_.addAll(other.statistics_);
          }
          onChanged();
        }
      } else {
        if (!other.statistics_.isEmpty()) {
          if (statisticsBuilder_.isEmpty()) {
            statisticsBuilder_.dispose();
            statisticsBuilder_ = null;
            statistics_ = other.statistics_;
            bitField0_ = (bitField0_ & ~0x00000001);
            statisticsBuilder_ = 
              com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                 getStatisticsFieldBuilder() : null;
          } else {
            statisticsBuilder_.addAllMessages(other.statistics_);
          }
        }
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
      org.redstone.protobuf.msg.PlaceStatisticsSync parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.redstone.protobuf.msg.PlaceStatisticsSync) e.getUnfinishedMessage();
        throw e;
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<org.redstone.protobuf.msg.ChessPlaceStatistics> statistics_ =
      java.util.Collections.emptyList();
    private void ensureStatisticsIsMutable() {
      if (!((bitField0_ & 0x00000001) == 0x00000001)) {
        statistics_ = new java.util.ArrayList<org.redstone.protobuf.msg.ChessPlaceStatistics>(statistics_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilder<
        org.redstone.protobuf.msg.ChessPlaceStatistics, org.redstone.protobuf.msg.ChessPlaceStatistics.Builder, org.redstone.protobuf.msg.ChessPlaceStatisticsOrBuilder> statisticsBuilder_;

    /**
     * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
     */
    public java.util.List<org.redstone.protobuf.msg.ChessPlaceStatistics> getStatisticsList() {
      if (statisticsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(statistics_);
      } else {
        return statisticsBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
     */
    public int getStatisticsCount() {
      if (statisticsBuilder_ == null) {
        return statistics_.size();
      } else {
        return statisticsBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
     */
    public org.redstone.protobuf.msg.ChessPlaceStatistics getStatistics(int index) {
      if (statisticsBuilder_ == null) {
        return statistics_.get(index);
      } else {
        return statisticsBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
     */
    public Builder setStatistics(
        int index, org.redstone.protobuf.msg.ChessPlaceStatistics value) {
      if (statisticsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureStatisticsIsMutable();
        statistics_.set(index, value);
        onChanged();
      } else {
        statisticsBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
     */
    public Builder setStatistics(
        int index, org.redstone.protobuf.msg.ChessPlaceStatistics.Builder builderForValue) {
      if (statisticsBuilder_ == null) {
        ensureStatisticsIsMutable();
        statistics_.set(index, builderForValue.build());
        onChanged();
      } else {
        statisticsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
     */
    public Builder addStatistics(org.redstone.protobuf.msg.ChessPlaceStatistics value) {
      if (statisticsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureStatisticsIsMutable();
        statistics_.add(value);
        onChanged();
      } else {
        statisticsBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
     */
    public Builder addStatistics(
        int index, org.redstone.protobuf.msg.ChessPlaceStatistics value) {
      if (statisticsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureStatisticsIsMutable();
        statistics_.add(index, value);
        onChanged();
      } else {
        statisticsBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
     */
    public Builder addStatistics(
        org.redstone.protobuf.msg.ChessPlaceStatistics.Builder builderForValue) {
      if (statisticsBuilder_ == null) {
        ensureStatisticsIsMutable();
        statistics_.add(builderForValue.build());
        onChanged();
      } else {
        statisticsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
     */
    public Builder addStatistics(
        int index, org.redstone.protobuf.msg.ChessPlaceStatistics.Builder builderForValue) {
      if (statisticsBuilder_ == null) {
        ensureStatisticsIsMutable();
        statistics_.add(index, builderForValue.build());
        onChanged();
      } else {
        statisticsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
     */
    public Builder addAllStatistics(
        java.lang.Iterable<? extends org.redstone.protobuf.msg.ChessPlaceStatistics> values) {
      if (statisticsBuilder_ == null) {
        ensureStatisticsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, statistics_);
        onChanged();
      } else {
        statisticsBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
     */
    public Builder clearStatistics() {
      if (statisticsBuilder_ == null) {
        statistics_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        statisticsBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
     */
    public Builder removeStatistics(int index) {
      if (statisticsBuilder_ == null) {
        ensureStatisticsIsMutable();
        statistics_.remove(index);
        onChanged();
      } else {
        statisticsBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
     */
    public org.redstone.protobuf.msg.ChessPlaceStatistics.Builder getStatisticsBuilder(
        int index) {
      return getStatisticsFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
     */
    public org.redstone.protobuf.msg.ChessPlaceStatisticsOrBuilder getStatisticsOrBuilder(
        int index) {
      if (statisticsBuilder_ == null) {
        return statistics_.get(index);  } else {
        return statisticsBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
     */
    public java.util.List<? extends org.redstone.protobuf.msg.ChessPlaceStatisticsOrBuilder> 
         getStatisticsOrBuilderList() {
      if (statisticsBuilder_ != null) {
        return statisticsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(statistics_);
      }
    }
    /**
     * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
     */
    public org.redstone.protobuf.msg.ChessPlaceStatistics.Builder addStatisticsBuilder() {
      return getStatisticsFieldBuilder().addBuilder(
          org.redstone.protobuf.msg.ChessPlaceStatistics.getDefaultInstance());
    }
    /**
     * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
     */
    public org.redstone.protobuf.msg.ChessPlaceStatistics.Builder addStatisticsBuilder(
        int index) {
      return getStatisticsFieldBuilder().addBuilder(
          index, org.redstone.protobuf.msg.ChessPlaceStatistics.getDefaultInstance());
    }
    /**
     * <code>repeated .org.redstone.protobuf.msg.ChessPlaceStatistics statistics = 1;</code>
     */
    public java.util.List<org.redstone.protobuf.msg.ChessPlaceStatistics.Builder> 
         getStatisticsBuilderList() {
      return getStatisticsFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilder<
        org.redstone.protobuf.msg.ChessPlaceStatistics, org.redstone.protobuf.msg.ChessPlaceStatistics.Builder, org.redstone.protobuf.msg.ChessPlaceStatisticsOrBuilder> 
        getStatisticsFieldBuilder() {
      if (statisticsBuilder_ == null) {
        statisticsBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
            org.redstone.protobuf.msg.ChessPlaceStatistics, org.redstone.protobuf.msg.ChessPlaceStatistics.Builder, org.redstone.protobuf.msg.ChessPlaceStatisticsOrBuilder>(
                statistics_,
                ((bitField0_ & 0x00000001) == 0x00000001),
                getParentForChildren(),
                isClean());
        statistics_ = null;
      }
      return statisticsBuilder_;
    }

    // @@protoc_insertion_point(builder_scope:org.redstone.protobuf.msg.PlaceStatisticsSync)
  }

  // @@protoc_insertion_point(class_scope:org.redstone.protobuf.msg.PlaceStatisticsSync)
  private static final org.redstone.protobuf.msg.PlaceStatisticsSync defaultInstance;static {
    defaultInstance = new org.redstone.protobuf.msg.PlaceStatisticsSync();
  }

  public static org.redstone.protobuf.msg.PlaceStatisticsSync getDefaultInstance() {
    return defaultInstance;
  }

  public org.redstone.protobuf.msg.PlaceStatisticsSync getDefaultInstanceForType() {
    return defaultInstance;
  }

}

