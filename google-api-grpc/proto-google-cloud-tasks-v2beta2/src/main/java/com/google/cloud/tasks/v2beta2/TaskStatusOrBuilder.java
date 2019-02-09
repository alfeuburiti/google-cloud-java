// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/cloud/tasks/v2beta2/task.proto

package com.google.cloud.tasks.v2beta2;

public interface TaskStatusOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.cloud.tasks.v2beta2.TaskStatus)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Output only. The number of attempts dispatched.
   * This count includes tasks which have been dispatched but haven't
   * received a response.
   * </pre>
   *
   * <code>int32 attempt_dispatch_count = 1;</code>
   */
  int getAttemptDispatchCount();

  /**
   *
   *
   * <pre>
   * Output only. The number of attempts which have received a response.
   * This field is not calculated for [pull tasks][google.cloud.tasks.v2beta2.PullMessage].
   * </pre>
   *
   * <code>int32 attempt_response_count = 2;</code>
   */
  int getAttemptResponseCount();

  /**
   *
   *
   * <pre>
   * Output only. The status of the task's first attempt.
   * Only [dispatch_time][google.cloud.tasks.v2beta2.AttemptStatus.dispatch_time] will be set.
   * The other [AttemptStatus][google.cloud.tasks.v2beta2.AttemptStatus] information is not retained by Cloud Tasks.
   * This field is not calculated for [pull tasks][google.cloud.tasks.v2beta2.PullMessage].
   * </pre>
   *
   * <code>.google.cloud.tasks.v2beta2.AttemptStatus first_attempt_status = 3;</code>
   */
  boolean hasFirstAttemptStatus();
  /**
   *
   *
   * <pre>
   * Output only. The status of the task's first attempt.
   * Only [dispatch_time][google.cloud.tasks.v2beta2.AttemptStatus.dispatch_time] will be set.
   * The other [AttemptStatus][google.cloud.tasks.v2beta2.AttemptStatus] information is not retained by Cloud Tasks.
   * This field is not calculated for [pull tasks][google.cloud.tasks.v2beta2.PullMessage].
   * </pre>
   *
   * <code>.google.cloud.tasks.v2beta2.AttemptStatus first_attempt_status = 3;</code>
   */
  com.google.cloud.tasks.v2beta2.AttemptStatus getFirstAttemptStatus();
  /**
   *
   *
   * <pre>
   * Output only. The status of the task's first attempt.
   * Only [dispatch_time][google.cloud.tasks.v2beta2.AttemptStatus.dispatch_time] will be set.
   * The other [AttemptStatus][google.cloud.tasks.v2beta2.AttemptStatus] information is not retained by Cloud Tasks.
   * This field is not calculated for [pull tasks][google.cloud.tasks.v2beta2.PullMessage].
   * </pre>
   *
   * <code>.google.cloud.tasks.v2beta2.AttemptStatus first_attempt_status = 3;</code>
   */
  com.google.cloud.tasks.v2beta2.AttemptStatusOrBuilder getFirstAttemptStatusOrBuilder();

  /**
   *
   *
   * <pre>
   * Output only. The status of the task's last attempt.
   * This field is not calculated for [pull tasks][google.cloud.tasks.v2beta2.PullMessage].
   * </pre>
   *
   * <code>.google.cloud.tasks.v2beta2.AttemptStatus last_attempt_status = 4;</code>
   */
  boolean hasLastAttemptStatus();
  /**
   *
   *
   * <pre>
   * Output only. The status of the task's last attempt.
   * This field is not calculated for [pull tasks][google.cloud.tasks.v2beta2.PullMessage].
   * </pre>
   *
   * <code>.google.cloud.tasks.v2beta2.AttemptStatus last_attempt_status = 4;</code>
   */
  com.google.cloud.tasks.v2beta2.AttemptStatus getLastAttemptStatus();
  /**
   *
   *
   * <pre>
   * Output only. The status of the task's last attempt.
   * This field is not calculated for [pull tasks][google.cloud.tasks.v2beta2.PullMessage].
   * </pre>
   *
   * <code>.google.cloud.tasks.v2beta2.AttemptStatus last_attempt_status = 4;</code>
   */
  com.google.cloud.tasks.v2beta2.AttemptStatusOrBuilder getLastAttemptStatusOrBuilder();
}