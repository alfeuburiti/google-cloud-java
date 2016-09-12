/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.datastore;

import static com.google.cloud.datastore.Validator.validateNamespace;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.primitives.Booleans;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Longs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * A Google Cloud Datastore GQL query.
 *
 * <h3>A usage example:</h3>
 *
 * <p>When the type of the results is known the preferred usage would be:
 * <pre> {@code
 * Query<Entity> query =
 *     Query.gqlQueryBuilder(Query.ResultType.ENTITY, "select * from kind").build();
 * QueryResults<Entity> results = datastore.run(query);
 * while (results.hasNext()) {
 *   Entity entity = results.next();
 *   ...
 * }
 * } </pre>
 *
 * <p>When the type of the results is unknown you can use this approach:
 * <pre> {@code
 * Query<?> query = Query.gqlQueryBuilder("select __key__ from kind").build();
 * QueryResults<?> results = datastore.run(query);
 * if (Key.class.isAssignableFrom(results.resultClass())) {
 *   QueryResults<Key> keys = (QueryResults<Key>) results;
 *   while (keys.hasNext()) {
 *     Key key = keys.next();
 *     ...
 *   }
 * }
 * } </pre>
 *
 * @param <V> the type of the result values this query will produce
 * @see <a href="https://cloud.google.com/datastore/docs/apis/gql/gql_reference">GQL Reference</a>
 */
public final class GqlQuery<V> extends Query<V> {

  private static final long serialVersionUID = -5514894742849230793L;

  private final String queryString;
  private final boolean allowLiteral;
  private final ImmutableMap<String, Binding> namedBindings;
  private final ImmutableList<Binding> positionalBindings;

  static final class Binding implements Serializable {

    private static final long serialVersionUID = 2344746877591371548L;

    private final Cursor cursor;
    private final Value<?> value;

    Binding(Cursor cursor) {
      this.cursor = checkNotNull(cursor);
      value = null;
    }

    Binding(Value<?> value) {
      this.value = checkNotNull(value);
      cursor = null;
    }

    Object cursorOrValue() {
      return MoreObjects.firstNonNull(cursor, value);
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this).add("cursor", cursor).add("value", value).toString();
    }

    @Override
    public int hashCode() {
      return Objects.hash(cursor, value);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj instanceof Binding)) {
        return false;
      }
      Binding other = (Binding) obj;
      return Objects.equals(cursor, other.cursor) && Objects.equals(value, other.value);
    }

    com.google.datastore.v1.GqlQueryParameter toPb() {
      com.google.datastore.v1.GqlQueryParameter.Builder argPb =
          com.google.datastore.v1.GqlQueryParameter.newBuilder();
      if (cursor != null) {
        argPb.setCursor(cursor.byteString());
      }
      if (value != null) {
        argPb.setValue(value.toPb());
      }
      return argPb.build();
    }

    static Binding fromPb(com.google.datastore.v1.GqlQueryParameter argPb) {
      switch (argPb.getParameterTypeCase()) {
        case CURSOR:
          return new Binding(new Cursor(argPb.getCursor()));
        case VALUE:
          return new Binding(Value.fromPb(argPb.getValue()));
        default:
          throw new AssertionError("Unexpected enum value " + argPb.getParameterTypeCase());
      }
    }
  }

  /**
   * A GQL query builder.
   */
  public static final class Builder<V> {

    private final ResultType<V> resultType;
    private String namespace;
    private String queryString;
    private boolean allowLiteral;
    private final Map<String, Binding> namedBindings = new TreeMap<>();
    private final List<Binding> positionalBindings = new LinkedList<>();

    Builder(ResultType<V> resultType, String query) {
      this.resultType = checkNotNull(resultType);
      queryString = checkNotNull(query);
    }

    public Builder<V> query(String query) {
      queryString = checkNotNull(query);
      return this;
    }

    public Builder<V> namespace(String namespace) {
      this.namespace = validateNamespace(namespace);
      return this;
    }

    public Builder<V> allowLiteral(boolean allowLiteral) {
      this.allowLiteral = allowLiteral;
      return this;
    }

    public Builder<V> clearBindings() {
      namedBindings.clear();
      positionalBindings.clear();
      return this;
    }

    public Builder<V> setBinding(String name, Cursor cursor) {
      namedBindings.put(name, new Binding(cursor));
      return this;
    }

    public Builder<V> setBinding(String name, String... value) {
      namedBindings.put(name, toBinding(StringValue.MARSHALLER, Arrays.asList(value)));
      return this;
    }

    public Builder<V> setBinding(String name, long... value) {
      namedBindings.put(name, toBinding(LongValue.MARSHALLER, Longs.asList(value)));
      return this;
    }

    public Builder<V> setBinding(String name, double... value) {
      namedBindings.put(name, toBinding(DoubleValue.MARSHALLER, Doubles.asList(value)));
      return this;
    }

    public Builder<V> setBinding(String name, boolean... value) {
      namedBindings.put(name, toBinding(BooleanValue.MARSHALLER, Booleans.asList(value)));
      return this;
    }

    public Builder<V> setBinding(String name, DateTime... value) {
      namedBindings.put(name, toBinding(DateTimeValue.MARSHALLER, Arrays.asList(value)));
      return this;
    }

    public Builder<V> setBinding(String name, Key... value) {
      namedBindings.put(name, toBinding(KeyValue.MARSHALLER, Arrays.asList(value)));
      return this;
    }

    public Builder<V> setBinding(String name, FullEntity<?>... value) {
      namedBindings.put(name, toBinding(EntityValue.MARSHALLER, Arrays.asList(value)));
      return this;
    }

    public Builder<V> setBinding(String name, Blob... value) {
      namedBindings.put(name, toBinding(BlobValue.MARSHALLER, Arrays.asList(value)));
      return this;
    }

    public Builder<V> addBinding(Cursor cursor) {
      positionalBindings.add(new Binding(cursor));
      return this;
    }

    public Builder<V> addBinding(String... value) {
      positionalBindings.add(toBinding(StringValue.MARSHALLER, Arrays.asList(value)));
      return this;
    }

    public Builder<V> addBinding(long... value) {
      positionalBindings.add(toBinding(LongValue.MARSHALLER, Longs.asList(value)));
      return this;
    }

    public Builder<V> addBinding(double... value) {
      positionalBindings.add(toBinding(DoubleValue.MARSHALLER, Doubles.asList(value)));
      return this;
    }

    public Builder<V> addBinding(boolean... value) {
      positionalBindings.add(toBinding(BooleanValue.MARSHALLER, Booleans.asList(value)));
      return this;
    }

    public Builder<V> addBinding(DateTime... value) {
      positionalBindings.add(toBinding(DateTimeValue.MARSHALLER, Arrays.asList(value)));
      return this;
    }

    public Builder<V> addBinding(Key... value) {
      positionalBindings.add(toBinding(KeyValue.MARSHALLER, Arrays.asList(value)));
      return this;
    }

    public Builder<V> addBinding(FullEntity<?>... value) {
      positionalBindings.add(toBinding(EntityValue.MARSHALLER, Arrays.asList(value)));
      return this;
    }

    public Builder<V> addBinding(Blob... value) {
      positionalBindings.add(toBinding(BlobValue.MARSHALLER, Arrays.asList(value)));
      return this;
    }

    public GqlQuery<V> build() {
      return new GqlQuery<>(this);
    }

    private static <V> Binding toBinding(Value.BuilderFactory<V, ?, ?> builderFactory,
        List<?> values) {
      List<Value<V>> list = new ArrayList<>(values.size());
      for (Object object : values) {
        @SuppressWarnings("unchecked")
        V v = (V) object;
        list.add(builderFactory.newBuilder(v).build());
      }
      Value<?> value;
      if (list.isEmpty()) {
        value = new NullValue();
      } else if (list.size() == 1) {
        value = list.get(0);
      } else {
        value = new ListValue(list);
      }
      return new Binding(value);
    }
  }

  private GqlQuery(Builder<V> builder) {
    super(builder.resultType, builder.namespace);
    queryString = builder.queryString;
    allowLiteral = builder.allowLiteral;
    namedBindings = ImmutableMap.copyOf(builder.namedBindings);
    positionalBindings = ImmutableList.copyOf(builder.positionalBindings);
  }

  public String queryString() {
    return queryString;
  }

  public boolean allowLiteral() {
    return allowLiteral;
  }

  /**
   * Returns an immutable map of named bindings.
   */
  public Map<String, Object> namedBindings() {
    ImmutableMap.Builder<String, Object> builder = ImmutableSortedMap.naturalOrder();
    for (Map.Entry<String, Binding> binding : namedBindings.entrySet()) {
      builder.put(binding.getKey(), binding.getValue().cursorOrValue());
    }
    return builder.build();
  }

  /**
   * Returns an immutable list of positional bindings (using original order).
   */
  public List<Object> numberArgs() {
    ImmutableList.Builder<Object> builder = ImmutableList.builder();
    for (Binding binding : positionalBindings) {
      builder.add(binding.cursorOrValue());
    }
    return builder.build();
  }

  @Override
  public String toString() {
    return super.toStringHelper()
        .add("queryString", queryString)
        .add("allowLiteral", allowLiteral)
        .add("namedBindings",  namedBindings)
        .add("positionalBindings", positionalBindings)
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(namespace(), queryString, allowLiteral, namedBindings, positionalBindings);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof GqlQuery)) {
      return false;
    }
    GqlQuery<?> other = (GqlQuery<?>) obj;
    return Objects.equals(namespace(), other.namespace())
        && Objects.equals(queryString, other.queryString)
        && allowLiteral == other.allowLiteral
        && Objects.equals(namedBindings,  other.namedBindings)
        && Objects.equals(positionalBindings,  other.positionalBindings);
  }

  com.google.datastore.v1.GqlQuery toPb() {
    com.google.datastore.v1.GqlQuery.Builder queryPb =
        com.google.datastore.v1.GqlQuery.newBuilder();
    queryPb.setQueryString(queryString);
    queryPb.setAllowLiterals(allowLiteral);
    Map<String, com.google.datastore.v1.GqlQueryParameter> namedBindingsPb =
        queryPb.getMutableNamedBindings();
    for (Map.Entry<String, Binding> entry : namedBindings.entrySet()) {
      namedBindingsPb.put(entry.getKey(), entry.getValue().toPb());
    }
    for (Binding argument : positionalBindings) {
      queryPb.addPositionalBindings(argument.toPb());
    }
    return queryPb.build();
  }

  @Override
  void populatePb(com.google.datastore.v1.RunQueryRequest.Builder requestPb) {
    requestPb.setGqlQuery(toPb());
  }

  @Override
  Query<V> nextQuery(com.google.datastore.v1.RunQueryResponse responsePb) {
    return StructuredQuery.<V>fromPb(type(), namespace(), responsePb.getQuery())
        .nextQuery(responsePb);
  }
}
