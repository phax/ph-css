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
package com.helger.css.supplementary.issues;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.reader.errorhandler.LoggingCSSParseErrorHandler;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Test for issue 3: https://github.com/phax/ph-css/issues/3
 *
 * @author Philip Helger
 */
public final class Issue3Test
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (Issue3Test.class);

  @Nonnull
  private static CascadingStyleSheet _parse (@Nonnull final String sCSS, final boolean bBrowserCompliantMode)
  {
    if (true)
      s_aLogger.info ("[Parsing] " + sCSS);
    return CSSReader.readFromStringReader (sCSS,
                                           new CSSReaderSettings ().setCSSVersion (ECSSVersion.CSS30)
                                                                   .setCustomErrorHandler (new LoggingCSSParseErrorHandler ())
                                                                   .setBrowserCompliantMode (bBrowserCompliantMode));
  }

  private static void _print (@Nonnull final CascadingStyleSheet aCSS)
  {
    s_aLogger.info (new CSSWriter (new CSSWriterSettings (ECSSVersion.CSS30).setOptimizedOutput (true)).setWriteHeaderText (false)
                                                                                                       .getCSSAsString (aCSS));
  }

  @Test
  public void testErrorInStyleDeclarationBlock1 ()
  {
    // Parse error in "unexpected:;"
    final String sTest = "body { background:red; unexpected:; background:blue; } span {color:blue;}";
    // Expected output:
    // body { background:red; background:blue; } span {color:blue;}
    final CascadingStyleSheet aCSS = _parse (sTest, true);
    assertNotNull (aCSS);
    if (true)
      _print (aCSS);
    assertEquals (2, aCSS.getStyleRuleCount ());
    // both backgrounds are present
    assertEquals (2, aCSS.getStyleRuleAtIndex (0).getDeclarationCount ());
  }

  @Test
  public void testErrorInStyleDeclarationBlock1a ()
  {
    // Parse error in "unexpected background"
    final String sTest = "body { background:red; unexpected background:blue; } span {color:blue;}";
    // Expected output:
    // body { background:red; background:blue; } span {color:blue;}
    final CascadingStyleSheet aCSS = _parse (sTest, true);
    assertNotNull (aCSS);
    if (true)
      _print (aCSS);
    assertEquals (2, aCSS.getStyleRuleCount ());
    // only first background is present
    assertEquals (1, aCSS.getStyleRuleAtIndex (0).getDeclarationCount ());
  }

  @Test
  public void testErrorInStyleDeclarationBlock2 ()
  {
    // Parse error at ".class" - nesting error which is afterwards closed
    final String sTest = "body {background:red;}" +
                         "body {background:blue;.class{color:green}" +
                         "  body {background:green;}" +
                         "}" +
                         "body{background:orange;}";
    final CascadingStyleSheet aCSS = _parse (sTest, true);
    assertNotNull (aCSS);
    if (true)
      _print (aCSS);
    assertEquals (3, aCSS.getStyleRuleCount ());
    assertEquals (1, aCSS.getStyleRuleAtIndex (0).getDeclarationCount ());
    assertEquals (1, aCSS.getStyleRuleAtIndex (1).getDeclarationCount ());
    assertEquals (1, aCSS.getStyleRuleAtIndex (2).getDeclarationCount ());
  }

  @Test
  public void testErrorInStyleDeclarationBlock2a ()
  {
    // Parse error at ".class" - nesting error which is not closed afterwards
    final String sTest = "body1 {background:red;}\n" +
                         "body2 {background:blue;.class{color:green}\n" +
                         "  body3 {background:green;}\n" +
                         "body4{background:orange;}";
    final CascadingStyleSheet aCSS = _parse (sTest, true);
    assertNotNull (aCSS);
    if (true)
      _print (aCSS);
    assertEquals (1, aCSS.getStyleRuleCount ());
    assertEquals (1, aCSS.getStyleRuleAtIndex (0).getDeclarationCount ());
  }

  @Test
  public void testErrorInMediaRule1 ()
  {
    // Parse error in "unexpected:;"
    final String sTest = "@media print { div { color:red; unexpected:; align:top; } } span {color:blue;}";
    final CascadingStyleSheet aCSS = _parse (sTest, true);
    assertNotNull (aCSS);
    if (false)
      _print (aCSS);
    assertEquals (1, aCSS.getMediaRuleCount ());
    assertEquals (2, ((CSSStyleRule) aCSS.getMediaRuleAtIndex (0).getRule (0)).getDeclarationCount ());
    assertEquals (1, aCSS.getStyleRuleCount ());
  }

  @Test
  public void testErrorInKeyframeRule1 ()
  {
    // Parse error in "unexpected::;"
    final String sTest = "@keyframes identifier { unexpected::; } span {color:blue;}";
    final CascadingStyleSheet aCSS = _parse (sTest, true);
    assertNotNull (aCSS);
    if (false)
      _print (aCSS);
    assertEquals (1, aCSS.getKeyframesRuleCount ());
    assertEquals (0, aCSS.getKeyframesRuleAtIndex (0).getBlockCount ());
    assertEquals (1, aCSS.getStyleRuleCount ());
  }

  @Test
  public void testErrorInKeyframeRule2 ()
  {
    // Parse error in "unexpected::;"
    final String sTest = "@keyframes identifier { 0% { unexpected::; } 30% { top: 50px; }   } span {color:blue;}";
    final CascadingStyleSheet aCSS = _parse (sTest, true);
    assertNotNull (aCSS);
    if (true)
      _print (aCSS);
    assertEquals (1, aCSS.getKeyframesRuleCount ());
    assertEquals (2, aCSS.getKeyframesRuleAtIndex (0).getBlockCount ());
    assertEquals (1, aCSS.getStyleRuleCount ());
  }

  @Test
  public void testErrorInKeyframeRule3 ()
  {
    final String sTest = "body {background:red;}\n" +
                         "@keyframes identifier { .class{color:red;.class{color:green} } \n" +
                         "/* No  matching closing bracket: the block is not closed and all the following rules are ignored. */\n" +
                         "/* Add the \"}\" before the following rule to close the block and enable the rule */\n" +
                         "body {background:green;}";
    final CascadingStyleSheet aCSS = _parse (sTest, true);
    assertNotNull (aCSS);
    if (true)
      _print (aCSS);
    assertEquals (1, aCSS.getRuleCount ());
    assertEquals (0, aCSS.getKeyframesRuleCount ());
  }

  @Test
  public void testErrorInSupportsRule1 ()
  {
    // Parse error in style declaration "unexpected::;"
    final String sTest = "@supports(column-count: 1) { body { unexpected::; } } span {color:blue;}";
    final CascadingStyleSheet aCSS = _parse (sTest, true);
    assertNotNull (aCSS);
    if (true)
      _print (aCSS);
    assertEquals (1, aCSS.getSupportsRuleCount ());
    assertEquals (1, aCSS.getSupportsRuleAtIndex (0).getRuleCount ());
    assertEquals (0, ((CSSStyleRule) aCSS.getSupportsRuleAtIndex (0).getRule (0)).getDeclarationCount ());
    assertEquals (1, aCSS.getStyleRuleCount ());
  }
}
