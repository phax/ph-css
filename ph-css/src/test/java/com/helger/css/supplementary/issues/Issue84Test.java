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
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Test for https://github.com/phax/ph-css/issues/84
 *
 * @author Philip Helger
 */
public final class Issue84Test
{
  @Test
  public void testBasicStarBrowserCompliant ()
  {
    final String sCSS = "div {\n" + "a: 100px;\n" + "*b: 1;\n" + "c:d;\n" + "}";
    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader (sCSS,
                                                                     new CSSReaderSettings ().setBrowserCompliantMode (true));
    assertNotNull (aCSS);
    assertEquals (1, aCSS.getStyleRuleCount ());

    final CSSStyleRule aSR = aCSS.getStyleRuleAtIndex (0);
    assertEquals (2, aSR.getDeclarationCount ());

    assertEquals ("div{a:100px;c:d}",
                  new CSSWriter (new CSSWriterSettings ().setOptimizedOutput (true)).setWriteHeaderText (false)
                                                                                    .getCSSAsString (aCSS));
  }

  @Test
  public void testBasicStarNotBrowserCompliant ()
  {
    final String sCSS = "div {\n" + "a: 100px;\n" + "*b: 1;\n" + "c:d;\n" + "}";
    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader (sCSS,
                                                                     new CSSReaderSettings ().setBrowserCompliantMode (false));
    assertNotNull (aCSS);
    assertEquals (1, aCSS.getStyleRuleCount ());

    final CSSStyleRule aSR = aCSS.getStyleRuleAtIndex (0);
    assertEquals (1, aSR.getDeclarationCount ());

    assertEquals ("div{a:100px}",
                  new CSSWriter (new CSSWriterSettings ().setOptimizedOutput (true)).setWriteHeaderText (false)
                                                                                    .getCSSAsString (aCSS));
  }
}
