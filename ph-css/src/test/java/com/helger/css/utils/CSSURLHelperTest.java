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
package com.helger.css.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.helger.commons.url.SMap;
import com.helger.commons.url.SimpleURL;
import com.helger.css.parser.CSSParseHelper;

/**
 * Test class for class {@link CSSURLHelper}.
 *
 * @author Philip Helger
 */
public final class CSSURLHelperTest
{
  @Test
  public void testGetURLValue ()
  {
    assertEquals ("a.gif", CSSURLHelper.getURLValue ("url(a.gif)"));
    assertEquals ("a.gif", CSSURLHelper.getURLValue ("url('a.gif')"));
    assertEquals ("a.gif", CSSURLHelper.getURLValue ("url(\"a.gif\")"));
    assertEquals ("a.gif?x=y", CSSURLHelper.getURLValue ("url(\"a.gif?x=y\")"));
    // Test quote 'a' character
    assertEquals ("a.gif", CSSURLHelper.getURLValue ("url(\"\\a.gif\")"));
    // different quote types
    assertEquals ("\"a.gif?x=y'", CSSURLHelper.getURLValue ("url(\"a.gif?x=y')"));
    // missing trailing ")"
    assertNull (CSSURLHelper.getURLValue ("url(a.gif"));
    // blank between "url" and "("
    assertNull (CSSURLHelper.getURLValue ("url (a.gif)"));
  }

  @Test
  public void testGetAsCSSURL ()
  {
    for (final String sURL : new String [] { "a.gif",
                                             "\"a.gif\"",
                                             "b\\a.gif",
                                             "\\b\\a\\c.gif",
                                             "'a.gif'",
                                             "'a\".gif'",
                                             "\"a'.gif\"",
                                             "a().gif",
                                             "a\\(\\).gif" })
    {
      final String sEscaped = CSSURLHelper.getAsCSSURL (sURL, false);
      assertEquals (sURL, CSSParseHelper.trimUrl (sEscaped));
    }

    assertEquals ("url(a.gif)", CSSURLHelper.getAsCSSURL ("a.gif", false));
    // By default: single quotes
    assertEquals ("url('a.gif')", CSSURLHelper.getAsCSSURL ("a.gif", true));
    // Force single quotes
    assertEquals ("url('\"a.gif\"')", CSSURLHelper.getAsCSSURL ("\"a.gif\"", true));
    // auto-quote
    assertEquals ("url('\"a.gif\"')", CSSURLHelper.getAsCSSURL ("\"a.gif\"", false));
    // auto-quote
    assertEquals ("url('b\\\\a.gif')", CSSURLHelper.getAsCSSURL ("b\\a.gif", false));
    // Force double quotes
    assertEquals ("url(\"'a.gif'\")", CSSURLHelper.getAsCSSURL ("'a.gif'", true));
    // auto-quote
    assertEquals ("url(\"'a.gif'\")", CSSURLHelper.getAsCSSURL ("'a.gif'", false));
    // Containing char to be escaped
    assertEquals ("url('\\'a\".gif\\'')", CSSURLHelper.getAsCSSURL ("'a\".gif'", true));
    // auto-quote
    assertEquals ("url('\\'a\".gif\\'')", CSSURLHelper.getAsCSSURL ("'a\".gif'", false));
    // Containing char to be escaped
    assertEquals ("url('\"a\\'.gif\"')", CSSURLHelper.getAsCSSURL ("\"a'.gif\"", true));
    // auto-quote
    assertEquals ("url('\"a\\'.gif\"')", CSSURLHelper.getAsCSSURL ("\"a'.gif\"", false));
    // Escaped brackets
    assertEquals ("url('a().gif')", CSSURLHelper.getAsCSSURL ("a().gif", false));

    final SimpleURL aURL = new SimpleURL ("a.gif", new SMap ("x", "y"));
    assertEquals ("url(a.gif?x=y)", CSSURLHelper.getAsCSSURL (aURL, false));
    assertEquals ("url('a.gif?x=y')", CSSURLHelper.getAsCSSURL (aURL, true));
    assertEquals ("url()", CSSURLHelper.getAsCSSURL (new SimpleURL (), false));
    assertEquals ("url('')", CSSURLHelper.getAsCSSURL (new SimpleURL (), true));

    // SimpleURL -> CSS URL -> String -> SimpleURL
    assertEquals (aURL, new SimpleURL (CSSURLHelper.getURLValue (CSSURLHelper.getAsCSSURL (aURL, true))));

    // empty URL!
    assertEquals ("url()", CSSURLHelper.getAsCSSURL ("", false));
    assertEquals ("url('')", CSSURLHelper.getAsCSSURL ("", true));
  }
}
