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
package com.helger.css.decl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.helger.commons.mock.CommonsTestHelper;
import com.helger.css.ECSSVersion;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Test class for class {@link CSSRGBA}.
 *
 * @author Philip Helger
 */
public final class CSSRGBATest
{
  @Test
  public void testBasic ()
  {
    final CSSWriterSettings aSettings = new CSSWriterSettings (ECSSVersion.CSS30, false);
    final CSSRGBA aColor = new CSSRGBA (1, 2, 3, 0.5f);
    assertEquals ("rgba(1,2,3,0.5)", aColor.getAsCSSString (aSettings, 0));

    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aColor, new CSSRGBA (aColor));
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aColor, new CSSRGBA (1, 2, 3, 0.5f));
    CommonsTestHelper.testDefaultImplementationWithDifferentContentObject (aColor, new CSSRGBA (0, 2, 3, 0.5f));
    CommonsTestHelper.testDefaultImplementationWithDifferentContentObject (aColor, new CSSRGBA (1, 0, 3, 0.5f));
    CommonsTestHelper.testDefaultImplementationWithDifferentContentObject (aColor, new CSSRGBA (1, 2, 0, 0.5f));
    CommonsTestHelper.testDefaultImplementationWithDifferentContentObject (aColor, new CSSRGBA (1, 2, 3, 0f));
  }
}
