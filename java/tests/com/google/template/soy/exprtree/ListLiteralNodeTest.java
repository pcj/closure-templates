/*
 * Copyright 2011 Google Inc.
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

package com.google.template.soy.exprtree;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableList;
import com.google.template.soy.base.SourceLocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests for {@code ListLiteralNode}.
 *
 */
@RunWith(JUnit4.class)
public final class ListLiteralNodeTest {

  private static final SourceLocation X = SourceLocation.UNKNOWN;

  @Test
  public void testToSourceString() {

    VarRefNode dataRef = new VarRefNode("foo", X, false, null);

    ListLiteralNode listLit =
        new ListLiteralNode(
            ImmutableList.<ExprNode>of(new StringNode("blah", X), new IntegerNode(123, X), dataRef),
            X);
    assertEquals("['blah', 123, $foo]", listLit.toSourceString());

    ListLiteralNode emptyListLit = new ListLiteralNode(ImmutableList.<ExprNode>of(), X);
    assertEquals("[]", emptyListLit.toSourceString());
  }
}
