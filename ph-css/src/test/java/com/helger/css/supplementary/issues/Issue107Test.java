/*
 * Copyright (C) 2014-2026 Philip Helger (www.helger.com)
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
package com.helger.css.supplementary.issues;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Test for issue: https://github.com/phax/ph-css/issues/107
 *
 * @author Philip Helger
 */
public final class Issue107Test
{
  @Test
  public void testIssue ()
  {
    final String sCSS = "a:focus {\r\n" +
                        "  background-color: red;\r\n" +
                        "}\r\n" +
                        ".box {\r\n" +
                        "  *zoom: 1; /* Applied only in IE7 and below */\r\n" +
                        "  background-image: url(\"https://www.example.com/image3.png\");\r\n" +
                        "}";
    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader (sCSS,
                                                                     new CSSReaderSettings ().setBrowserCompliantMode (true)
                                                                                             .setUseSourceLocation (true)
                                                                                             .setKeepDeprecatedProperties (true));
    assertNotNull (aCSS);

    assertEquals (2, aCSS.getAllStyleRules ().size ());

    final CSSStyleRule aRule2 = aCSS.getAllStyleRules ().get (1);
    assertNotNull (aRule2);

    // Check selector
    assertEquals (".box", aRule2.getSelectorsAsCSSString (new CSSWriterSettings (), 0));

    // Check declarations
    // *zoom is a deprecated property
    assertEquals (2, aRule2.getDeclarationCount ());
    assertEquals ("*zoom", aRule2.getDeclarationAtIndex (0).getProperty ());
    assertEquals ("background-image", aRule2.getDeclarationAtIndex (1).getProperty ());
  }

  @Test
  public void testIssue2 ()
  {
    final String sCSS = "a:focus {\r\n" +
                        "  background-color: red;\r\n" +
                        "}\r\n" +
                        ".box {\r\n" +
                        "  *zoom: 1; /* Applied only in IE7 and below */\r\n" +
                        "  background-image: url(\"https://www.example.com/image3.png\");\r\n" +
                        "}";
    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader (sCSS,
                                                                     new CSSReaderSettings ().setBrowserCompliantMode (true)
                                                                                             .setUseSourceLocation (true)
                                                                                             .setKeepDeprecatedProperties (false));
    assertNotNull (aCSS);

    assertEquals (2, aCSS.getAllStyleRules ().size ());

    final CSSStyleRule aRule2 = aCSS.getAllStyleRules ().get (1);
    assertNotNull (aRule2);

    // Check selector
    assertEquals (".box", aRule2.getSelectorsAsCSSString (new CSSWriterSettings (), 0));

    // Check declarations
    // *zoom is a deprecated property
    assertEquals (1, aRule2.getDeclarationCount ());
    assertEquals ("background-image", aRule2.getDeclarationAtIndex (0).getProperty ());
  }
}
