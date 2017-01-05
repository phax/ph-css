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
package com.helger.css.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for class {@link CSSParseHelper}.
 *
 * @author Philip Helger
 */
public final class CSSParseHelperTest
{
  private static String _split (final String s)
  {
    return CSSParseHelper.splitNumber (new StringBuilder (s));
  }

  @Test
  public void testSplitNumber ()
  {
    assertEquals ("0", _split ("0px"));
    assertEquals ("17", _split ("17in"));
    assertEquals ("17", _split ("17 in"));
    assertEquals ("17.1234", _split ("17.1234 in"));
    assertEquals ("0.5", _split ("0.5dpi"));
    assertEquals (".5", _split (".5dpi"));

    assertEquals ("0", _split ("0"));
    assertEquals ("17", _split ("17"));
    assertEquals ("17", _split ("17 "));
    assertEquals ("17.1234", _split ("17.1234 "));
    assertEquals ("0.5", _split ("0.5"));
    assertEquals (".5", _split (".5"));
    assertEquals ("", _split ("."));
    assertEquals ("", _split ("any"));
    assertEquals ("", _split (""));
  }

  @Test
  public void testUnescapeCSSURL ()
  {
    assertEquals ("bla.gif", CSSParseHelper.unescapeURL ("bla.gif"));
    assertEquals ("/foo/bla.gif", CSSParseHelper.unescapeURL ("/foo/bla.gif"));
    assertEquals ("/foo/bla().gif", CSSParseHelper.unescapeURL ("/foo/bla\\(\\).gif"));
    assertEquals ("\\\\server\\foo\\bla.gif", CSSParseHelper.unescapeURL ("\\\\\\\\server\\\\foo\\\\bla.gif"));
  }
}
