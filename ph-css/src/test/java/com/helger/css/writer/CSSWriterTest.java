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
package com.helger.css.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.helger.commons.system.ENewLineMode;
import com.helger.css.AbstractCSS30TestCase;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;

/**
 * Test class for class {@link CSSWriter}.
 *
 * @author Philip Helger
 */
public final class CSSWriterTest extends AbstractCSS30TestCase
{
  @Test
  public void testIndentation ()
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (CSS3, ECSSVersion.CSS30);
    assertNotNull (aCSS);
    final CSSWriterSettings aSettings = new CSSWriterSettings (ECSSVersion.CSS30, false);
    final CSSWriter aWriter = new CSSWriter (aSettings).setWriteHeaderText (false);
    assertEquals ("h1 {\n" +
                  "  color:red;\n" +
                  "  margin:1px;\n" +
                  "}\n" +
                  "\n" +
                  "h2 { color:rgb(1,2,3); }\n" +
                  "\n" +
                  "h3 {}\n" +
                  "\n" +
                  "@keyframes x {\n" +
                  "  from {\n" +
                  "    align:left;\n" +
                  "    color:#123;\n" +
                  "  }\n" +
                  "  to { x:y; }\n" +
                  "  50% {}\n" +
                  "}\n" +
                  "\n" +
                  "@page {\n" +
                  "  margin:1in;\n" +
                  "  marks:none;\n" +
                  "}\n" +
                  "\n" +
                  "@page :first { margin:2in; }\n" +
                  "\n" +
                  "@page :last {}\n" +
                  "\n" +
                  "@media print {\n" +
                  "  div {\n" +
                  "    width:100%;\n" +
                  "    min-height:0;\n" +
                  "  }\n" +
                  "}\n" +
                  "\n" +
                  "@media all {\n" +
                  "  div { width:90%; }\n" +
                  "}\n" +
                  "\n" +
                  "@media tv {}\n" +
                  "\n" +
                  "@font-face {\n" +
                  "  font-family:'Soho';\n" +
                  "  src:url(Soho.eot);\n" +
                  "}\n" +
                  "\n" +
                  "@font-face { src:local('Soho Gothic Pro'); }\n" +
                  "\n" +
                  "@font-face {}\n",
                  aWriter.getCSSAsString (aCSS));

    // Change indentation
    aSettings.setIndent ("\t");
    assertEquals ("h1 {\n" +
                  "\tcolor:red;\n" +
                  "\tmargin:1px;\n" +
                  "}\n" +
                  "\n" +
                  "h2 { color:rgb(1,2,3); }\n" +
                  "\n" +
                  "h3 {}\n" +
                  "\n" +
                  "@keyframes x {\n" +
                  "\tfrom {\n" +
                  "\t\talign:left;\n" +
                  "\t\tcolor:#123;\n" +
                  "\t}\n" +
                  "\tto { x:y; }\n" +
                  "\t50% {}\n" +
                  "}\n" +
                  "\n" +
                  "@page {\n" +
                  "\tmargin:1in;\n" +
                  "\tmarks:none;\n" +
                  "}\n" +
                  "\n" +
                  "@page :first { margin:2in; }\n" +
                  "\n" +
                  "@page :last {}\n" +
                  "\n" +
                  "@media print {\n" +
                  "\tdiv {\n" +
                  "\t\twidth:100%;\n" +
                  "\t\tmin-height:0;\n" +
                  "\t}\n" +
                  "}\n" +
                  "\n" +
                  "@media all {\n" +
                  "\tdiv { width:90%; }\n" +
                  "}\n" +
                  "\n" +
                  "@media tv {}\n" +
                  "\n" +
                  "@font-face {\n" +
                  "\tfont-family:'Soho';\n" +
                  "\tsrc:url(Soho.eot);\n" +
                  "}\n" +
                  "\n" +
                  "@font-face { src:local('Soho Gothic Pro'); }\n" +
                  "\n" +
                  "@font-face {}\n",
                  aWriter.getCSSAsString (aCSS));
  }

  @Test
  public void testIndentationNested ()
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (CSS4, ECSSVersion.CSS30);
    assertNotNull (aCSS);
    final CSSWriterSettings aSettings = new CSSWriterSettings (ECSSVersion.CSS30, false);
    final CSSWriter aWriter = new CSSWriter (aSettings).setWriteHeaderText (false);
    assertEquals ("@media print {\n" +
                  "  h1 {\n" +
                  "    color:red;\n" +
                  "    margin:1px;\n" +
                  "  }\n" +
                  "\n" +
                  "  h2 { color:rgb(1,2,3); }\n" +
                  "\n" +
                  "  h3 {}\n" +
                  "\n" +
                  "  @keyframes x {\n" +
                  "    from {\n" +
                  "      align:left;\n" +
                  "      color:#123;\n" +
                  "    }\n" +
                  "    to { x:y; }\n" +
                  "    50% {}\n" +
                  "  }\n" +
                  "\n" +
                  "  @page {\n" +
                  "    margin:1in;\n" +
                  "    marks:none;\n" +
                  "  }\n" +
                  "\n" +
                  "  @page :first { margin:2in; }\n" +
                  "\n" +
                  "  @font-face {\n" +
                  "    font-family:'Soho';\n" +
                  "    src:url(Soho.eot);\n" +
                  "  }\n" +
                  "\n" +
                  "  @font-face { src:local('Soho Gothic Pro'); }\n" +
                  "\n" +
                  "  @font-face {}\n" +
                  "}\n",
                  aWriter.getCSSAsString (aCSS));
  }

  @Test
  public void testRemoveUnnecessaryCode1 ()
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (CSS3, ECSSVersion.CSS30);
    assertNotNull (aCSS);
    final CSSWriterSettings aSettings = new CSSWriterSettings (ECSSVersion.CSS30,
                                                               false).setRemoveUnnecessaryCode (true);
    final CSSWriter aWriter = new CSSWriter (aSettings).setWriteHeaderText (false);
    assertEquals ("h1 {\n" +
                  "  color:red;\n" +
                  "  margin:1px;\n" +
                  "}\n" +
                  "\n" +
                  "h2 { color:rgb(1,2,3); }\n" +
                  "\n" +
                  "@keyframes x {\n" +
                  "  from {\n" +
                  "    align:left;\n" +
                  "    color:#123;\n" +
                  "  }\n" +
                  "  to { x:y; }\n" +
                  "}\n" +
                  "\n" +
                  "@page {\n" +
                  "  margin:1in;\n" +
                  "  marks:none;\n" +
                  "}\n" +
                  "\n" +
                  "@page :first { margin:2in; }\n" +
                  "\n" +
                  "@media print {\n" +
                  "  div {\n" +
                  "    width:100%;\n" +
                  "    min-height:0;\n" +
                  "  }\n" +
                  "}\n" +
                  "\n" +
                  "@media all {\n" +
                  "  div { width:90%; }\n" +
                  "}\n" +
                  "\n" +
                  "@font-face {\n" +
                  "  font-family:'Soho';\n" +
                  "  src:url(Soho.eot);\n" +
                  "}\n" +
                  "\n" +
                  "@font-face { src:local('Soho Gothic Pro'); }\n",
                  aWriter.getCSSAsString (aCSS));
  }

  @Test
  public void testRemoveUnnecessaryCode2 ()
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (CSS4, ECSSVersion.CSS30);
    assertNotNull (aCSS);
    final CSSWriterSettings aSettings = new CSSWriterSettings (ECSSVersion.CSS30,
                                                               false).setRemoveUnnecessaryCode (true);
    final CSSWriter aWriter = new CSSWriter (aSettings).setWriteHeaderText (false);
    assertEquals ("@media print {\n" +
                  "  h1 {\n" +
                  "    color:red;\n" +
                  "    margin:1px;\n" +
                  "  }\n" +
                  "\n" +
                  "  h2 { color:rgb(1,2,3); }\n" +
                  "\n" +
                  "  @keyframes x {\n" +
                  "    from {\n" +
                  "      align:left;\n" +
                  "      color:#123;\n" +
                  "    }\n" +
                  "    to { x:y; }\n" +
                  "  }\n" +
                  "\n" +
                  "  @page {\n" +
                  "    margin:1in;\n" +
                  "    marks:none;\n" +
                  "  }\n" +
                  "\n" +
                  "  @page :first { margin:2in; }\n" +
                  "\n" +
                  "  @font-face {\n" +
                  "    font-family:'Soho';\n" +
                  "    src:url(Soho.eot);\n" +
                  "  }\n" +
                  "\n" +
                  "  @font-face { src:local('Soho Gothic Pro'); }\n" +
                  "}\n",
                  aWriter.getCSSAsString (aCSS));
  }

  @Test
  public void testHeaderText ()
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (CSS5, ECSSVersion.CSS30);
    assertNotNull (aCSS);

    // Non-optimized version
    CSSWriter aWriter = new CSSWriter (ECSSVersion.CSS30, false).setWriteHeaderText (true).setHeaderText ("Unit test");
    assertEquals ("/*\n" +
                  " * Unit test\n" +
                  " */\n" +
                  "h1 {\n" +
                  "  color:red;\n" +
                  "  margin:1px;\n" +
                  "}\n" +
                  "\n" +
                  "h2 {\n" +
                  "  color:red;\n" +
                  "  margin:1px;\n" +
                  "}\n",
                  aWriter.getCSSAsString (aCSS));

    // Optimized version
    aWriter = new CSSWriter (ECSSVersion.CSS30, true).setWriteHeaderText (true).setHeaderText ("Unit test2");
    assertEquals ("/*\n" +
                  " * Unit test2\n" +
                  " */\n" +
                  "h1{color:red;margin:1px}h2{color:red;margin:1px}",
                  aWriter.getCSSAsString (aCSS));
  }

  @Test
  public void testNewLineModeWindows ()
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (CSS5, ECSSVersion.CSS30);
    assertNotNull (aCSS);

    // Non-optimized version
    CSSWriter aWriter = new CSSWriter (new CSSWriterSettings (ECSSVersion.CSS30).setOptimizedOutput (false)
                                                                                .setNewLineMode (ENewLineMode.WINDOWS)).setWriteHeaderText (true)
                                                                                                                       .setHeaderText ("Unit test");
    assertEquals ("/*\r\n" +
                  " * Unit test\r\n" +
                  " */\r\n" +
                  "h1 {\r\n" +
                  "  color:red;\r\n" +
                  "  margin:1px;\r\n" +
                  "}\r\n" +
                  "\r\n" +
                  "h2 {\r\n" +
                  "  color:red;\r\n" +
                  "  margin:1px;\r\n" +
                  "}\r\n",
                  aWriter.getCSSAsString (aCSS));

    // Optimized version
    aWriter = new CSSWriter (new CSSWriterSettings (ECSSVersion.CSS30).setOptimizedOutput (true)
                                                                      .setNewLineMode (ENewLineMode.WINDOWS)).setWriteHeaderText (true)
                                                                                                             .setHeaderText ("Unit test2");
    assertEquals ("/*\r\n" +
                  " * Unit test2\r\n" +
                  " */\r\n" +
                  "h1{color:red;margin:1px}h2{color:red;margin:1px}",
                  aWriter.getCSSAsString (aCSS));
  }

  @Test
  public void testNewLineModeMac ()
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (CSS5, ECSSVersion.CSS30);
    assertNotNull (aCSS);

    // Non-optimized version
    CSSWriter aWriter = new CSSWriter (new CSSWriterSettings (ECSSVersion.CSS30).setOptimizedOutput (false)
                                                                                .setNewLineMode (ENewLineMode.MAC)).setWriteHeaderText (true)
                                                                                                                   .setHeaderText ("Unit test");
    assertEquals ("/*\r" +
                  " * Unit test\r" +
                  " */\r" +
                  "h1 {\r" +
                  "  color:red;\r" +
                  "  margin:1px;\r" +
                  "}\r" +
                  "\r" +
                  "h2 {\r" +
                  "  color:red;\r" +
                  "  margin:1px;\r" +
                  "}\r",
                  aWriter.getCSSAsString (aCSS));

    // Optimized version
    aWriter = new CSSWriter (new CSSWriterSettings (ECSSVersion.CSS30).setOptimizedOutput (true)
                                                                      .setNewLineMode (ENewLineMode.MAC)).setWriteHeaderText (true)
                                                                                                         .setHeaderText ("Unit test2");
    assertEquals ("/*\r" +
                  " * Unit test2\r" +
                  " */\r" +
                  "h1{color:red;margin:1px}h2{color:red;margin:1px}",
                  aWriter.getCSSAsString (aCSS));
  }

  @Test
  public void testWriteCertainRules ()
  {
    final CSSWriterSettings aSettings = new CSSWriterSettings (ECSSVersion.CSS30, true);
    aSettings.setWriteFontFaceRules (false);
    aSettings.setWriteKeyframesRules (false);
    aSettings.setWriteMediaRules (false);
    aSettings.setWritePageRules (false);
    final CSSWriter aWriter = new CSSWriter (aSettings).setWriteHeaderText (false);

    // Some non-special rules
    CascadingStyleSheet aCSS = CSSReader.readFromString (CSS3, ECSSVersion.CSS30);
    assertNotNull (aCSS);
    assertEquals ("h1{color:red;margin:1px}h2{color:rgb(1,2,3)}h3{}", aWriter.getCSSAsString (aCSS));

    // Only @media rule
    aCSS = CSSReader.readFromString (CSS4, ECSSVersion.CSS30);
    assertNotNull (aCSS);
    assertEquals ("", aWriter.getCSSAsString (aCSS));

    // Nothing special
    aCSS = CSSReader.readFromString (CSS5, ECSSVersion.CSS30);
    assertNotNull (aCSS);
    assertEquals ("h1{color:red;margin:1px}h2{color:red;margin:1px}", aWriter.getCSSAsString (aCSS));
  }
}
