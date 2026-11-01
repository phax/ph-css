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
package com.helger.css.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jspecify.annotations.NonNull;
import org.junit.Test;

/**
 * Test class for class {@link CCSSProperties}.
 *
 * @author Philip Helger
 */
public final class CCSSPropertiesTest
{
  private static void _assertValid (@NonNull final ICSSProperty aProperty, @NonNull final String... aValues)
  {
    for (final String sValue : aValues)
      assertTrue (aProperty.getPropertyName () + ": " + sValue, aProperty.isValidValue (sValue));
  }

  private static void _assertInvalid (@NonNull final ICSSProperty aProperty, @NonNull final String... aValues)
  {
    for (final String sValue : aValues)
      assertFalse (aProperty.getPropertyName () + ": " + sValue, aProperty.isValidValue (sValue));
  }

  @Test
  public void testCSSWideKeywords ()
  {
    for (final ICSSProperty aProperty : new ICSSProperty [] { CCSSProperties.DISPLAY,
                                                              CCSSProperties.COLOR,
                                                              CCSSProperties.MARGIN,
                                                              CCSSProperties.OPACITY })
      _assertValid (aProperty, "inherit", "initial", "unset", "revert", "revert-layer");
  }

  @Test
  public void testSizes ()
  {
    for (final ICSSProperty aProperty : new ICSSProperty [] { CCSSProperties.WIDTH,
                                                              CCSSProperties.HEIGHT,
                                                              CCSSProperties.MIN_WIDTH,
                                                              CCSSProperties.MIN_HEIGHT })
    {
      _assertValid (aProperty, "auto", "min-content", "max-content", "fit-content", "10px", "50%");
      _assertInvalid (aProperty, "none", "any", "-moz-max-content", "-webkit-fill-available");
    }
    for (final ICSSProperty aProperty : new ICSSProperty [] { CCSSProperties.MAX_WIDTH, CCSSProperties.MAX_HEIGHT })
    {
      _assertValid (aProperty, "none", "min-content", "max-content", "fit-content");
      _assertInvalid (aProperty, "auto");
    }
    _assertValid (CCSSProperties.FLEX_BASIS, "auto", "content", "min-content", "max-content", "fit-content", "10px");
  }

  @Test
  public void testNumbersWithoutUnit ()
  {
    _assertValid (CCSSProperties.LINE_HEIGHT, "normal", "1.5", "0", "12px", "150%");
    _assertInvalid (CCSSProperties.LINE_HEIGHT, "any");
    _assertValid (CCSSProperties.FONT_WEIGHT, "normal", "bold", "bolder", "lighter", "400", "450");
    _assertInvalid (CCSSProperties.FONT_WEIGHT, "any", "10px");
    _assertValid (CCSSProperties.FLEX_GROW, "0", "1", "0.5");
    _assertValid (CCSSProperties.FLEX_SHRINK, "0", "1", "0.5");
    _assertValid (CCSSProperties.OPACITY, "0", "0.5", "1", "50%");
    _assertInvalid (CCSSProperties.OPACITY, "10px", "any");
  }

  @Test
  public void testNumbersAndKeywords ()
  {
    _assertValid (CCSSProperties.WORD_SPACING, "normal", "1px");
    _assertValid (CCSSProperties.LETTER_SPACING, "normal", "1px");
    _assertValid (CCSSProperties.VERTICAL_ALIGN, "middle", "text-top", "2px", "10%");
    _assertValid (CCSSProperties.TEXT_INDENT, "1em", "5%", "1em hanging", "1em each-line", "1em hanging each-line");
    _assertValid (CCSSProperties.FONT_SIZE, "xxx-large", "math", "12px");
  }

  @Test
  public void testPaddingHasNoAuto ()
  {
    for (final ICSSProperty aProperty : new ICSSProperty [] { CCSSProperties.PADDING,
                                                              CCSSProperties.PADDING_TOP,
                                                              CCSSProperties.PADDING_RIGHT,
                                                              CCSSProperties.PADDING_BOTTOM,
                                                              CCSSProperties.PADDING_LEFT })
    {
      _assertValid (aProperty, "0", "1px", "5%");
      _assertInvalid (aProperty, "auto");
    }
    _assertValid (CCSSProperties.PADDING, "1px 2px 3px 4px");
    _assertInvalid (CCSSProperties.PADDING, "1px 2px 3px 4px 5px");
    _assertValid (CCSSProperties.MARGIN, "auto", "0 auto");
  }

  @Test
  public void testOutline ()
  {
    _assertValid (CCSSProperties.OUTLINE_COLOR, "auto", "red", "#fff");
    _assertInvalid (CCSSProperties.OUTLINE_COLOR, "invert", "red blue");
    _assertValid (CCSSProperties.OUTLINE_STYLE, "auto", "none", "solid", "dotted");
    _assertInvalid (CCSSProperties.OUTLINE_STYLE, "hidden", "solid dotted");
    _assertValid (CCSSProperties.OUTLINE_WIDTH, "thin", "1px");
    _assertInvalid (CCSSProperties.OUTLINE_WIDTH, "1px 2px", "5%");
  }

  @Test
  public void testBackground ()
  {
    _assertValid (CCSSProperties.BACKGROUND_POSITION, "center", "left top", "0 0", "left 10px top", "left 10px top 20px");
    _assertInvalid (CCSSProperties.BACKGROUND_POSITION, "left 1px top 2px 3px");
    _assertValid (CCSSProperties.BACKGROUND_SIZE, "cover", "contain", "auto", "10px", "auto auto", "50% 10px");
    _assertInvalid (CCSSProperties.BACKGROUND_SIZE, "1px 2px 3px");
    _assertValid (CCSSProperties.BACKGROUND_CLIP, "border-box", "padding-box", "content-box", "border-area", "text");
    _assertInvalid (CCSSProperties.BACKGROUND_ORIGIN, "border-area", "text");
  }

  @Test
  public void testKeywordRanges ()
  {
    _assertValid (CCSSProperties.POSITION, "static", "relative", "absolute", "fixed", "sticky");
    _assertInvalid (CCSSProperties.POSITION, "-webkit-sticky");
    _assertValid (CCSSProperties.DISPLAY,
                  "contents",
                  "flow-root",
                  "grid",
                  "inline-flex",
                  "inline-grid",
                  "ruby",
                  "inline flex",
                  "block flow list-item");
    _assertInvalid (CCSSProperties.DISPLAY, "-webkit-box", "-ms-flexbox", "-moz-inline-box", "any");
    _assertValid (CCSSProperties.OVERFLOW, "clip", "hidden visible");
    _assertInvalid (CCSSProperties.OVERFLOW, "hidden visible clip", "-moz-hidden-unscrollable");
    _assertValid (CCSSProperties.TEXT_ALIGN, "start", "end", "match-parent");
    _assertInvalid (CCSSProperties.TEXT_ALIGN, "-moz-center", "-webkit-center");
    _assertValid (CCSSProperties.WHITE_SPACE, "break-spaces", "preserve-spaces", "preserve nowrap");
    _assertValid (CCSSProperties.FLOAT, "inline-start", "inline-end");
    _assertValid (CCSSProperties.CLEAR, "inline-start", "inline-end");
    _assertValid (CCSSProperties.RESIZE, "block", "inline");
    _assertValid (CCSSProperties.WRITING_MODE, "sideways-rl", "sideways-lr");
    _assertValid (CCSSProperties.TEXT_COMBINE_UPRIGHT, "digits");
    _assertValid (CCSSProperties.TEXT_TRANSFORM, "math-auto", "full-size-kana", "uppercase full-width");
    _assertValid (CCSSProperties.TEXT_OVERFLOW, "clip", "ellipsis", "clip ellipsis");
    _assertValid (CCSSProperties.FONT_VARIANT, "none", "small-caps", "all-small-caps", "small-caps slashed-zero");
    _assertInvalid (CCSSProperties.FONT_VARIANT, "any");
    _assertValid (CCSSProperties.LIST_STYLE_TYPE, "georgian", "decimal-leading-zero", "disclosure-open", "khmer");
    _assertInvalid (CCSSProperties.LIST_STYLE_TYPE, "-moz-arabic-indic", "inside");
    _assertValid (CCSSProperties.LIST_STYLE_IMAGE, "none", "url(a.png)");
    _assertValid (CCSSProperties.TEXT_DECORATION, "underline", "underline dotted red");
  }

  @Test
  public void testBorderSpacing ()
  {
    _assertValid (CCSSProperties.BORDER_SPACING, "1px", "1px 2px");
    _assertInvalid (CCSSProperties.BORDER_SPACING, "5%", "1px 2px 3px");
  }

  @Test
  public void testFloodOpacityName ()
  {
    assertEquals ("flood-opacity", ECSSProperty.FLOOD_OPACITY.getName ());
  }

  @Test
  public void testClone ()
  {
    assertTrue (CCSSProperties.LINE_HEIGHT.getClone (ECSSProperty.LINE_HEIGHT).isValidValue ("1.5"));
    assertTrue (CCSSProperties.OPACITY.getClone (ECSSProperty.OPACITY).isValidValue ("50%"));
  }

  @Test
  public void testMaxWidthAndMaxHeight ()
  {
    for (final ICSSProperty aProperty : new ICSSProperty [] { CCSSProperties.MAX_WIDTH, CCSSProperties.MAX_HEIGHT })
    {
      assertTrue (aProperty.isValidValue ("none"));
      assertTrue (aProperty.isValidValue ("10px"));
      assertTrue (aProperty.isValidValue ("50%"));
      assertFalse (aProperty.isValidValue ("any"));
    }
    // "none" is no valid value for the minimum
    assertFalse (CCSSProperties.MIN_WIDTH.isValidValue ("none"));
    assertFalse (CCSSProperties.MIN_HEIGHT.isValidValue ("none"));
  }

  @Test
  public void testBasic ()
  {
    assertTrue (CCSSProperties.BORDER_LEFT_STYLE.isValidValue ("none"));
    assertFalse (CCSSProperties.BORDER_LEFT_STYLE.isValidValue ("any"));
    assertTrue (CCSSProperties.BORDER_STYLE.isValidValue ("solid none"));
    assertTrue (CCSSProperties.BORDER_STYLE.isValidValue ("      solid           none       "));
    assertFalse (CCSSProperties.BORDER_STYLE.isValidValue ("      solid           any       "));
    assertTrue (CCSSProperties.BORDER_STYLE.isValidValue ("solid none none none"));
    assertFalse (CCSSProperties.BORDER_STYLE.isValidValue ("solid none none none solid"));
    assertFalse (CCSSProperties.BORDER_STYLE.isValidValue (""));
    assertTrue (CCSSProperties.COLOR.isValidValue ("rgb(0,0,0)"));
    assertTrue (CCSSProperties.COLOR.isValidValue ("rgb ( 0% , 0% , 50% )"));
    assertTrue (CCSSProperties.COLOR.isValidValue ("rgb(0%,0%,50%)"));
    assertTrue (CCSSProperties.COLOR.isValidValue ("#ffffff"));
    assertTrue (CCSSProperties.COLOR.isValidValue (" #ffffff "));
    assertTrue (CCSSProperties.COLOR.isValidValue ("#fff"));
    assertTrue (CCSSProperties.COLOR.isValidValue (" #fff   "));
    assertFalse (CCSSProperties.COLOR.isValidValue ("#fffe"));
    assertFalse (CCSSProperties.COLOR.isValidValue (" #fffe   "));
    assertFalse (CCSSProperties.COLOR.isValidValue ("#fffffff"));
    assertFalse (CCSSProperties.COLOR.isValidValue ("#aag"));
    assertFalse (CCSSProperties.COLOR.isValidValue ("#ppp"));
    assertTrue (CCSSProperties.CLIP.isValidValue ("auto"));
    assertTrue (CCSSProperties.CLIP.isValidValue ("rect(5px,10in,33px,456em)"));
    assertTrue (CCSSProperties.Z_INDEX.isValidValue ("-500"));
    assertTrue (CCSSProperties.Z_INDEX.isValidValue ("0"));
    assertTrue (CCSSProperties.Z_INDEX.isValidValue ("1000"));
  }
}
