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
package com.helger.css;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test class for class {@link CSSFilenameHelper}.
 *
 * @author Philip Helger
 */
public final class CSSFilenameHelperTest
{
  @Test
  public void testAll ()
  {
    final String s1 = "dir/a.css";
    final String s2 = "dir/a.min.css";

    assertTrue (CSSFilenameHelper.isCSSFilename (s1));
    assertTrue (CSSFilenameHelper.isCSSFilename (s2));
    assertFalse (CSSFilenameHelper.isCSSFilename ("anydir/otherfile.txt"));

    assertFalse (CSSFilenameHelper.isMinifiedCSSFilename (s1));
    assertTrue (CSSFilenameHelper.isMinifiedCSSFilename (s2));
    assertFalse (CSSFilenameHelper.isMinifiedCSSFilename ("anydir/otherfile.txt"));

    assertTrue (CSSFilenameHelper.isRegularCSSFilename (s1));
    assertFalse (CSSFilenameHelper.isRegularCSSFilename (s2));
    assertFalse (CSSFilenameHelper.isRegularCSSFilename ("anydir/otherfile.txt"));

    assertEquals (s2, CSSFilenameHelper.getMinifiedCSSFilename (s1));
    assertEquals (s2, CSSFilenameHelper.getMinifiedCSSFilename (s2));
    try
    {
      CSSFilenameHelper.getMinifiedCSSFilename ("anydir/otherfile.txt");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {}
  }
}
