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
package com.helger.css.decl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jspecify.annotations.NonNull;
import org.junit.Test;

import com.helger.css.media.ECSSMediaExpressionFeature;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.reader.errorhandler.DoNothingCSSParseErrorHandler;
import com.helger.css.writer.CSSWriterSettings;
import com.helger.unittest.support.TestHelper;

/**
 * Test class for class {@link CSSMediaExpression}, especially the Media Queries Level 4 range
 * context.
 */
public final class CSSMediaExpressionTest
{
  private static final CSSWriterSettings WS = new CSSWriterSettings ().setOptimizedOutput (true);

  @NonNull
  private static CSSMediaQuery _parseSingleQuery (@NonNull final String sMediaQuery)
  {
    // Strict mode: the media rule must be parsed, not skipped
    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader ("@media " + sMediaQuery + " { div { color: red } }",
                                                               new CSSReaderSettings ().setBrowserCompliantMode (false)
                                                                                       .setCustomErrorHandler (new DoNothingCSSParseErrorHandler ()));
    assertNotNull (sMediaQuery, aCSS);
    assertEquals (sMediaQuery, 1, aCSS.getMediaRuleCount ());
    final CSSMediaRule aMediaRule = aCSS.getMediaRuleAtIndex (0);
    assertEquals (sMediaQuery, 1, aMediaRule.getRuleCount ());
    assertEquals (sMediaQuery, 1, aMediaRule.getMediaQueryCount ());
    return aMediaRule.getMediaQueryAtIndex (0);
  }

  @NonNull
  private static CSSMediaExpression _parseSingleExpression (@NonNull final String sMediaQuery)
  {
    final CSSMediaQuery aQuery = _parseSingleQuery (sMediaQuery);
    assertEquals (sMediaQuery, 1, aQuery.getMediaExpressionCount ());
    return aQuery.getMediaExpression (0);
  }

  @Test
  public void testClassicForms ()
  {
    CSSMediaExpression aExpr = _parseSingleExpression ("(color)");
    assertEquals ("color", aExpr.getFeature ());
    assertNull (aExpr.getValue ());
    assertFalse (aExpr.isRangeContext ());
    assertEquals ("(color)", aExpr.getAsCSSString (WS, 0));

    aExpr = _parseSingleExpression ("( min-width : 600px )");
    assertEquals ("min-width", aExpr.getFeature ());
    assertEquals ("600px", aExpr.getValue ().getAsCSSString (WS, 0));
    assertFalse (aExpr.isRangeContext ());
    assertNull (aExpr.getRangeLeftValue ());
    assertNull (aExpr.getRangeLeftOperator ());
    assertNull (aExpr.getRangeRightOperator ());
    assertEquals ("(min-width:600px)", aExpr.getAsCSSString (WS, 0));
  }

  @Test
  public void testRangeFeatureFirst ()
  {
    CSSMediaExpression aExpr = _parseSingleExpression ("(width >= 600px)");
    assertTrue (aExpr.isRangeContext ());
    assertNull (aExpr.getRangeLeftValue ());
    assertNull (aExpr.getRangeLeftOperator ());
    assertEquals ("width", aExpr.getFeature ());
    assertSame (ECSSMediaRangeOperator.GREATER_EQUALS, aExpr.getRangeRightOperator ());
    assertEquals ("600px", aExpr.getValue ().getAsCSSString (WS, 0));
    assertEquals ("(width >= 600px)", aExpr.getAsCSSString (WS, 0));

    // no whitespace at all (minified CSS)
    aExpr = _parseSingleExpression ("(width>=600px)");
    assertSame (ECSSMediaRangeOperator.GREATER_EQUALS, aExpr.getRangeRightOperator ());
    assertEquals ("(width >= 600px)", aExpr.getAsCSSString (WS, 0));

    aExpr = _parseSingleExpression ("(width<=600px)");
    assertSame (ECSSMediaRangeOperator.LESS_EQUALS, aExpr.getRangeRightOperator ());
    assertEquals ("(width <= 600px)", aExpr.getAsCSSString (WS, 0));

    aExpr = _parseSingleExpression ("(height < 400px)");
    assertSame (ECSSMediaRangeOperator.LESS, aExpr.getRangeRightOperator ());
    assertEquals ("(height < 400px)", aExpr.getAsCSSString (WS, 0));

    aExpr = _parseSingleExpression ("(height > 400px)");
    assertSame (ECSSMediaRangeOperator.GREATER, aExpr.getRangeRightOperator ());
    assertEquals ("(height > 400px)", aExpr.getAsCSSString (WS, 0));

    aExpr = _parseSingleExpression ("(width = 600px)");
    assertSame (ECSSMediaRangeOperator.EQUALS, aExpr.getRangeRightOperator ());
    assertEquals ("(width = 600px)", aExpr.getAsCSSString (WS, 0));
  }

  @Test
  public void testRangeValueFirst ()
  {
    CSSMediaExpression aExpr = _parseSingleExpression ("(600px <= width)");
    assertTrue (aExpr.isRangeContext ());
    assertEquals ("600px", aExpr.getRangeLeftValue ().getAsCSSString (WS, 0));
    assertSame (ECSSMediaRangeOperator.LESS_EQUALS, aExpr.getRangeLeftOperator ());
    assertEquals ("width", aExpr.getFeature ());
    assertNull (aExpr.getRangeRightOperator ());
    assertNull (aExpr.getValue ());
    assertEquals ("(600px <= width)", aExpr.getAsCSSString (WS, 0));

    aExpr = _parseSingleExpression ("(-1px>height)");
    assertEquals ("-1px", aExpr.getRangeLeftValue ().getAsCSSString (WS, 0));
    assertSame (ECSSMediaRangeOperator.GREATER, aExpr.getRangeLeftOperator ());
    assertEquals ("(-1px > height)", aExpr.getAsCSSString (WS, 0));
  }

  @Test
  public void testRangeBothSides ()
  {
    CSSMediaExpression aExpr = _parseSingleExpression ("(400px <= width <= 600px)");
    assertTrue (aExpr.isRangeContext ());
    assertEquals ("400px", aExpr.getRangeLeftValue ().getAsCSSString (WS, 0));
    assertSame (ECSSMediaRangeOperator.LESS_EQUALS, aExpr.getRangeLeftOperator ());
    assertEquals ("width", aExpr.getFeature ());
    assertSame (ECSSMediaRangeOperator.LESS_EQUALS, aExpr.getRangeRightOperator ());
    assertEquals ("600px", aExpr.getValue ().getAsCSSString (WS, 0));
    assertEquals ("(400px <= width <= 600px)", aExpr.getAsCSSString (WS, 0));

    aExpr = _parseSingleExpression ("(600px>width>400px)");
    assertSame (ECSSMediaRangeOperator.GREATER, aExpr.getRangeLeftOperator ());
    assertSame (ECSSMediaRangeOperator.GREATER, aExpr.getRangeRightOperator ());
    assertEquals ("(600px > width > 400px)", aExpr.getAsCSSString (WS, 0));
  }

  @Test
  public void testRangeValueTypes ()
  {
    CSSMediaExpression aExpr = _parseSingleExpression ("(aspect-ratio >= 16/9)");
    assertEquals ("16/9", aExpr.getValue ().getAsCSSString (WS, 0));
    assertEquals ("(aspect-ratio >= 16/9)", aExpr.getAsCSSString (WS, 0));

    aExpr = _parseSingleExpression ("(16 / 9 <= aspect-ratio)");
    assertEquals ("16/9", aExpr.getRangeLeftValue ().getAsCSSString (WS, 0));
    assertEquals ("(16/9 <= aspect-ratio)", aExpr.getAsCSSString (WS, 0));

    aExpr = _parseSingleExpression ("(resolution >= 2dppx)");
    assertEquals ("(resolution >= 2dppx)", aExpr.getAsCSSString (WS, 0));

    aExpr = _parseSingleExpression ("(color >= 8)");
    assertEquals ("(color >= 8)", aExpr.getAsCSSString (WS, 0));

    aExpr = _parseSingleExpression ("(width >= calc(100px + 2em))");
    assertEquals ("(width >= calc(100px + 2em))", aExpr.getAsCSSString (WS, 0));

    aExpr = _parseSingleExpression ("(calc(100px + 2em) <= width)");
    assertEquals ("(calc(100px + 2em) <= width)", aExpr.getAsCSSString (WS, 0));
  }

  @Test
  public void testRangeInMediaQueryList ()
  {
    final CSSMediaQuery aQuery = _parseSingleQuery ("only screen and (width>=768px) and (hover: hover)");
    assertSame (CSSMediaQuery.EModifier.ONLY, aQuery.getModifier ());
    assertEquals ("screen", aQuery.getMedium ());
    assertEquals (2, aQuery.getMediaExpressionCount ());
    assertEquals ("only screen and (width >= 768px) and (hover:hover)", aQuery.getAsCSSString (WS, 0));

    final CascadingStyleSheet aCSS = CSSReader.readFromString ("@media (width >= 768px), print and (height < 400px) { div { color: red } }");
    assertNotNull (aCSS);
    assertEquals (1, aCSS.getMediaRuleCount ());
    assertEquals (2, aCSS.getMediaRuleAtIndex (0).getMediaQueryCount ());
    assertEquals ("(width >= 768px)", aCSS.getMediaRuleAtIndex (0).getMediaQueryAtIndex (0).getAsCSSString (WS, 0));
    assertEquals ("print and (height < 400px)", aCSS.getMediaRuleAtIndex (0).getMediaQueryAtIndex (1).getAsCSSString (WS, 0));
  }

  @Test
  public void testRangeInImportRule ()
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString ("@import url(\"print.css\") (width >= 600px);");
    assertNotNull (aCSS);
    assertEquals (1, aCSS.getImportRuleCount ());
    final CSSImportRule aImportRule = aCSS.getImportRuleAtIndex (0);
    assertEquals (1, aImportRule.getMediaQueryCount ());
    assertEquals ("(width >= 600px)", aImportRule.getAllMediaQueries ().get (0).getAsCSSString (WS, 0));
  }

  @Test
  public void testInvalidRange ()
  {
    // Strict mode: a broken range makes the whole style sheet fail
    final CSSReaderSettings aStrict = new CSSReaderSettings ().setBrowserCompliantMode (false)
                                                              .setCustomErrorHandler (new DoNothingCSSParseErrorHandler ());
    assertNull (CSSReader.readFromStringReader ("@media (width >=) { div { color: red } }", aStrict));
    assertNull (CSSReader.readFromStringReader ("@media (>= 600px) { div { color: red } }", aStrict));
    assertNull (CSSReader.readFromStringReader ("@media (600px <= <= 900px) { div { color: red } }", aStrict));

    // Browser compliant mode: the broken media rule is skipped, the rest survives
    final CSSReaderSettings aLenient = new CSSReaderSettings ().setBrowserCompliantMode (true)
                                                               .setCustomErrorHandler (new DoNothingCSSParseErrorHandler ());
    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader ("@media (width >=) { div { color: red } } p { color: blue }",
                                                               aLenient);
    assertNotNull (aCSS);
    assertEquals (0, aCSS.getMediaRuleCount ());
    assertEquals (1, aCSS.getStyleRuleCount ());
  }

  @Test
  public void testConstructorsAndEquals ()
  {
    final CSSExpression a600 = CSSExpression.createSimple ("600px");
    final CSSExpression a900 = CSSExpression.createSimple ("900px");

    final CSSMediaExpression aFeatureFirst = new CSSMediaExpression (ECSSMediaExpressionFeature.WIDTH,
                                                                     ECSSMediaRangeOperator.GREATER_EQUALS,
                                                                     a600);
    assertEquals ("(width >= 600px)", aFeatureFirst.getAsCSSString (WS, 0));
    TestHelper.testDefaultImplementationWithEqualContentObject (aFeatureFirst,
                                                                new CSSMediaExpression ("width",
                                                                                        ECSSMediaRangeOperator.GREATER_EQUALS,
                                                                                        a600));
    TestHelper.testDefaultImplementationWithDifferentContentObject (aFeatureFirst,
                                                                    new CSSMediaExpression ("width", a600));
    TestHelper.testDefaultImplementationWithDifferentContentObject (aFeatureFirst,
                                                                    new CSSMediaExpression ("width",
                                                                                            ECSSMediaRangeOperator.GREATER,
                                                                                            a600));

    final CSSMediaExpression aValueFirst = new CSSMediaExpression (a600, ECSSMediaRangeOperator.LESS_EQUALS, "width");
    assertEquals ("(600px <= width)", aValueFirst.getAsCSSString (WS, 0));
    TestHelper.testDefaultImplementationWithDifferentContentObject (aFeatureFirst, aValueFirst);

    final CSSMediaExpression aBoth = new CSSMediaExpression (a600,
                                                             ECSSMediaRangeOperator.LESS_EQUALS,
                                                             "width",
                                                             ECSSMediaRangeOperator.LESS_EQUALS,
                                                             a900);
    assertEquals ("(600px <= width <= 900px)", aBoth.getAsCSSString (WS, 0));
    TestHelper.testDefaultImplementationWithEqualContentObject (aBoth, _parseSingleExpression ("(600px<=width<=900px)"));
    TestHelper.testDefaultImplementationWithDifferentContentObject (aBoth, aValueFirst);

    // Classic form is untouched
    assertEquals ("(width:600px)", new CSSMediaExpression ("width", a600).getAsCSSString (WS, 0));
    assertEquals ("(width)", new CSSMediaExpression ("width").getAsCSSString (WS, 0));

    // Inconsistent combinations are rejected
    try
    {
      new CSSMediaExpression (a600, null, "width", null, null);
      org.junit.Assert.fail ();
    }
    catch (final IllegalArgumentException ex)
    {
      // expected
    }
    try
    {
      new CSSMediaExpression (null, null, "width", ECSSMediaRangeOperator.LESS, null);
      org.junit.Assert.fail ();
    }
    catch (final IllegalArgumentException ex)
    {
      // expected
    }
  }
}
