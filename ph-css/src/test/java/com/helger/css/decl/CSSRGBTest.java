/*
 * Copyright (C) 2014-2025 Philip Helger (www.helger.com)
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
package com.helger.css.decl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.helger.css.writer.CSSWriterSettings;
import com.helger.unittest.support.TestHelper;

/**
 * Test class for class {@link CSSRGB}.
 *
 * @author Philip Helger
 */
public final class CSSRGBTest
{
  @Test
  public void testBasic ()
  {
    final CSSWriterSettings aSettings = new CSSWriterSettings ( false);
    final CSSRGB aColor = new CSSRGB (1, 2, 3);
    assertEquals ("rgb(1,2,3)", aColor.getAsCSSString (aSettings));

    TestHelper.testDefaultImplementationWithEqualContentObject (aColor, new CSSRGB (aColor));
    TestHelper.testDefaultImplementationWithEqualContentObject (aColor, new CSSRGB (1, 2, 3));
    TestHelper.testDefaultImplementationWithDifferentContentObject (aColor, new CSSRGB (0, 2, 3));
    TestHelper.testDefaultImplementationWithDifferentContentObject (aColor, new CSSRGB (1, 0, 3));
    TestHelper.testDefaultImplementationWithDifferentContentObject (aColor, new CSSRGB (1, 2, 0));
  }
}
