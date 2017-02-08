/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.template.soy.jssrc.dsl;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

/** Represents a symbol that is imported via a {@code goog.require} statement. */
@AutoValue
public abstract class GoogRequire implements Comparable<GoogRequire> {
  private static final CodeChunk.WithValue GOOG_REQUIRE =
      CodeChunk.dottedIdNoRequire("goog.require");

  /**
   * Creates a new {@code GoogRequire} that requires the given symbol: {@code
   * goog.require('symbol'); }
   */
  public static GoogRequire create(String symbol) {
    return new AutoValue_GoogRequire(symbol, GOOG_REQUIRE.call(CodeChunk.stringLiteral(symbol)));
  }

  /**
   * Creates a new {@code GoogRequire} that requires the given symbol and aliases it to the given
   * name: {@code var alias = goog.require('symbol'); }
   */
  public static GoogRequire createWithAlias(String symbol, String alias) {
    CodeChunkUtils.checkId(alias);
    return new AutoValue_GoogRequire(
        symbol,
        CodeChunk.declare(alias)
            .setInitialValue(GOOG_REQUIRE.call(CodeChunk.stringLiteral(symbol)))
            .build());
  }

  /** The symbol to require. */
  public abstract String symbol();

  /** a code chunk that will generate the {@code goog.require()}. */
  abstract CodeChunk chunk();

  /** Returns a code chunk that can act as a reference to the required symbol. */
  public CodeChunk.WithValue reference() {
    CodeChunk.WithValue value;
    if (chunk() instanceof Declaration) {
      value = CodeChunk.id(((Declaration) chunk()).varName());
    } else {
      value = CodeChunk.dottedIdNoRequire(symbol());
    }
    return GoogRequireDecorator.create(value, ImmutableList.of(this));
  }

  /** Access a member of this required symbol. */
  public CodeChunk.WithValue dotAccess(String ident) {
    return reference().dotAccess(ident);
  }

  public void writeTo(StringBuilder sb) {
    sb.append(chunk().getStatementsForInsertingIntoForeignCodeAtIndent(0));
  }

  @Override
  public final int compareTo(GoogRequire o) {
    return symbol().compareTo(o.symbol());
  }
}
