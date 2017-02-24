/**
 * Copyright (C) 2014-2017 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.css.reader;

import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.helger.commons.collection.ext.CommonsArrayList;

/**
 * Test reading CSS 2.1 files
 *
 * @author Philip Helger
 */
@RunWith (Parameterized.class)
public final class CSSReader21FuncTest extends AbstractFuncTestCSSReader
{
  @Parameters (name = "{index}: browserCompliant={0}")
  public static Iterable <Object []> data ()
  {
    return new CommonsArrayList<> (new Object [] { true }, new Object [] { false });
  }

  public CSSReader21FuncTest (final boolean bBrowserCompliant)
  {
    super (StandardCharsets.UTF_8, false, bBrowserCompliant);
  }

  @Test
  public void testReadAll21Good ()
  {
    testReadGood ("src/test/resources/testfiles/css21/good");
  }

  @Test
  public void testReadAll21BadButRecoverableAndBrowserCompliant ()
  {
    testReadBadButRecoverableAndBrowserCompliant ("src/test/resources/testfiles/css21/bad_but_recoverable_and_browsercompliant");
  }
}
