// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/cloud/language/v1beta2/language_service.proto

package com.google.cloud.language.v1beta2;

public interface AnalyzeEntitySentimentResponseOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.cloud.language.v1beta2.AnalyzeEntitySentimentResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The recognized entities in the input document with associated sentiments.
   * </pre>
   *
   * <code>repeated .google.cloud.language.v1beta2.Entity entities = 1;</code>
   */
  java.util.List<com.google.cloud.language.v1beta2.Entity> getEntitiesList();
  /**
   *
   *
   * <pre>
   * The recognized entities in the input document with associated sentiments.
   * </pre>
   *
   * <code>repeated .google.cloud.language.v1beta2.Entity entities = 1;</code>
   */
  com.google.cloud.language.v1beta2.Entity getEntities(int index);
  /**
   *
   *
   * <pre>
   * The recognized entities in the input document with associated sentiments.
   * </pre>
   *
   * <code>repeated .google.cloud.language.v1beta2.Entity entities = 1;</code>
   */
  int getEntitiesCount();
  /**
   *
   *
   * <pre>
   * The recognized entities in the input document with associated sentiments.
   * </pre>
   *
   * <code>repeated .google.cloud.language.v1beta2.Entity entities = 1;</code>
   */
  java.util.List<? extends com.google.cloud.language.v1beta2.EntityOrBuilder>
      getEntitiesOrBuilderList();
  /**
   *
   *
   * <pre>
   * The recognized entities in the input document with associated sentiments.
   * </pre>
   *
   * <code>repeated .google.cloud.language.v1beta2.Entity entities = 1;</code>
   */
  com.google.cloud.language.v1beta2.EntityOrBuilder getEntitiesOrBuilder(int index);

  /**
   *
   *
   * <pre>
   * The language of the text, which will be the same as the language specified
   * in the request or, if not specified, the automatically-detected language.
   * See [Document.language][google.cloud.language.v1beta2.Document.language] field for more details.
   * </pre>
   *
   * <code>string language = 2;</code>
   */
  java.lang.String getLanguage();
  /**
   *
   *
   * <pre>
   * The language of the text, which will be the same as the language specified
   * in the request or, if not specified, the automatically-detected language.
   * See [Document.language][google.cloud.language.v1beta2.Document.language] field for more details.
   * </pre>
   *
   * <code>string language = 2;</code>
   */
  com.google.protobuf.ByteString getLanguageBytes();
}