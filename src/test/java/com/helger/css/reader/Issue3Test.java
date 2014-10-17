/**
 * Copyright (C) 2014 Philip Helger (www.helger.com)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.helger.commons.charset.CCharset;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.errorhandler.LoggingCSSParseErrorHandler;
import com.helger.css.writer.CSSWriter;

/**
 * Test for issue 3: https://github.com/phax/ph-css/issues/3
 *
 * @author Philip Helger
 */
public final class Issue3Test
{
  @Test
  public void testErrorInStyleDeclarationBlock ()
  {
    // Parse error in " unexpected::;"
    final String sTest1 = "div { color:red; unexpected::; align:top; } span {color:blue;}";
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sTest1,
                                                               CCharset.CHARSET_UTF_8_OBJ,
                                                               ECSSVersion.CSS30,
                                                               new LoggingCSSParseErrorHandler ());
    assertNotNull (aCSS);
    if (true)
      System.out.println (new CSSWriter (ECSSVersion.CSS30).getCSSAsString (aCSS));
    assertEquals (2, aCSS.getStyleRuleCount ());
    assertEquals (1, aCSS.getStyleRuleAtIndex (0).getDeclarationCount ());
  }

  @Test
  public void testErrorInMediaRule ()
  {
    // Parse error in " unexpected::;"
    final String sTest1 = "@media print { div { color:red; unexpected::; align:top; } } span {color:blue;}";
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sTest1,
                                                               CCharset.CHARSET_UTF_8_OBJ,
                                                               ECSSVersion.CSS30,
                                                               new LoggingCSSParseErrorHandler ());
    assertNotNull (aCSS);
    if (true)
      System.out.println (new CSSWriter (ECSSVersion.CSS30).getCSSAsString (aCSS));
    assertEquals (1, aCSS.getMediaRuleCount ());
    assertEquals (1, ((CSSStyleRule) aCSS.getMediaRuleAtIndex (0).getRule (0)).getDeclarationCount ());
    assertEquals (1, aCSS.getStyleRuleCount ());
  }

  @Test
  public void testErrorInKeyframeRule ()
  {
    // Parse error in " unexpected::;"
    final String sTest1 = "@keyframes identifier { bla::; } span {color:blue;}";
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sTest1,
                                                               CCharset.CHARSET_UTF_8_OBJ,
                                                               ECSSVersion.CSS30,
                                                               new LoggingCSSParseErrorHandler ());
    assertNotNull (aCSS);
    if (true)
      System.out.println (new CSSWriter (ECSSVersion.CSS30).getCSSAsString (aCSS));
    assertEquals (1, aCSS.getKeyframesRuleCount ());
    assertEquals (0, aCSS.getKeyframesRuleAtIndex (0).getBlockCount ());
    assertEquals (1, aCSS.getStyleRuleCount ());
  }
}
