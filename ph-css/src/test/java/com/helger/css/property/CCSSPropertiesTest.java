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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.jspecify.annotations.NonNull;
import org.junit.Test;

import com.helger.css.ECSSSpecification;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.propertyvalue.CCSSValue;

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
    _assertValid (CCSSProperties.BACKGROUND_POSITION,
                  "center",
                  "left top",
                  "0 0",
                  "left 10px top",
                  "left 10px top 20px");
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
  public void testEachPropertyHasConstant () throws Exception
  {
    for (final ECSSProperty e : ECSSProperty.values ())
      if (!e.isVendorSpecific ())
      {
        final Field aField = CCSSProperties.class.getField (e.name ());
        final ICSSProperty aProperty = (ICSSProperty) aField.get (null);
        assertSame (e.name (), e, aProperty.getProp ());

        // Both must be deprecated or none
        final Deprecated aEnumDeprecated = ECSSProperty.class.getField (e.name ()).getAnnotation (Deprecated.class);
        final Deprecated aFieldDeprecated = aField.getAnnotation (Deprecated.class);
        assertEquals (e.name (), Boolean.valueOf (aEnumDeprecated != null), Boolean.valueOf (aFieldDeprecated != null));
        if (aEnumDeprecated != null)
        {
          assertFalse (e.name (), aEnumDeprecated.forRemoval ());
          assertFalse (e.name (), aFieldDeprecated.forRemoval ());
        }
      }
  }

  @Test
  public void testNoVendorSpecificValues () throws Exception
  {
    for (final Field aField : CCSSValue.class.getFields ())
      if (aField.getType () == String.class)
      {
        final String sValue = (String) aField.get (null);
        assertFalse (aField.getName (), sValue.startsWith ("-"));
      }
  }

  @Test
  @SuppressWarnings ("removal")
  public void testMisspelledJustifyConstants ()
  {
    assertSame (ECSSProperty.JUSTIFY_CONTENT, ECSSProperty.JUSITFY_CONTENT);
    assertSame (ECSSProperty.JUSTIFY_ITEMS, ECSSProperty.JUSITFY_ITEMS);
    assertSame (ECSSProperty.JUSTIFY_SELF, ECSSProperty.JUSITFY_SELF);
    assertSame (CCSSProperties.JUSTIFY_CONTENT, CCSSProperties.JUSITFY_CONTENT);
    assertSame (CCSSProperties.JUSTIFY_ITEMS, CCSSProperties.JUSITFY_ITEMS);
    assertSame (CCSSProperties.JUSTIFY_SELF, CCSSProperties.JUSITFY_SELF);
    assertEquals ("justify-content", CCSSProperties.JUSTIFY_CONTENT.getPropertyName ());
  }

  @Test
  public void testDeprecationIsOnlyForEntriesNotOnMDN () throws Exception
  {
    // At-rule descriptors are documented on MDN
    for (final String sName : new String [] { "SRC", "UNICODE_RANGE", "SIZE" })
      assertNull (sName, ECSSProperty.class.getField (sName).getAnnotation (Deprecated.class));
    assertNotNull (ECSSProperty.class.getField ("AZIMUTH").getAnnotation (Deprecated.class));
  }

  @Test
  public void testSubstitutionFunctions ()
  {
    for (final ICSSProperty aProperty : new ICSSProperty [] { CCSSProperties.DISPLAY,
                                                              CCSSProperties.COLOR,
                                                              CCSSProperties.MARGIN,
                                                              CCSSProperties.BORDER_COLOR,
                                                              CCSSProperties.ANIMATION_DIRECTION,
                                                              CCSSProperties.FLEX_DIRECTION })
      _assertValid (aProperty, "var(--x)", "var(--x, 1px 2px)", "env(safe-area-inset-top)");
    _assertValid (CCSSProperties.MARGIN, "var(--x) 1px", "1px var(--y, 2px 3px) 4px");
  }

  @Test
  public void testNumericFunctions ()
  {
    _assertValid (CCSSProperties.WIDTH,
                  "calc(100% - 10px)",
                  "min(1px, 2px)",
                  "clamp(1px, 2vw, 3px)",
                  "fit-content(20em)");
    _assertValid (CCSSProperties.TOP, "anchor(bottom)");
    _assertValid (CCSSProperties.MARGIN, "calc(1px + 2px) auto", "calc(1px + 2px) calc(3px * 2) 4px");
    _assertValid (CCSSProperties.OPACITY, "calc(1 / 2)");
    _assertValid (CCSSProperties.Z_INDEX, "calc(1 + 2)");
    _assertValid (CCSSProperties.ORDER, "calc(1 + 2)");
    _assertInvalid (CCSSProperties.WIDTH, "foo(1px)");
  }

  @Test
  public void testColors ()
  {
    _assertValid (CCSSProperties.COLOR,
                  "rebeccapurple",
                  "Canvas",
                  "ButtonBorder",
                  "currentcolor",
                  "CurrentColor",
                  "TRANSPARENT",
                  "hwb(0 0% 0%)",
                  "lab(50% 40 59)",
                  "lch(50% 40 59)",
                  "oklab(50% 0.1 0.1)",
                  "oklch(50% 0.1 180)",
                  "color(display-p3 1 0 0)",
                  "color-mix(in srgb, red, blue)",
                  "light-dark(white, black)",
                  "rgb(0 0 0)",
                  "rgb(0 0 0 / 50%)",
                  "rgb(0, 0, 0, 0.5)",
                  "rgba(0, 0, 0)",
                  "hsl(120 100% 50%)",
                  "hsl(120, 100%, 50%, 0.5)");
    _assertInvalid (CCSSProperties.COLOR, "rgb(1, 2)", "foo(1 2 3)", "any");
    _assertValid (CCSSProperties.BORDER_COLOR, "rgb(0, 0, 0) red", "rgb(0 0 0) red blue oklch(50% 0.1 180)");
  }

  @Test
  public void testImageFunctions ()
  {
    _assertValid (CCSSProperties.BACKGROUND_IMAGE,
                  "none",
                  "url(a.png)",
                  "linear-gradient(black, white)",
                  "url(a.png), radial-gradient(circle, transparent 45%, black 48%)");
    _assertInvalid (CCSSProperties.BACKGROUND_IMAGE, "-webkit-linear-gradient(black, white)", "foo(a)");
    _assertValid (CCSSProperties.LIST_STYLE_IMAGE, "linear-gradient(to left bottom, red, blue)");
  }

  @Test
  public void testCaseInsensitive ()
  {
    _assertValid (CCSSProperties.DISPLAY, "BLOCK", "Inline-Flex");
    _assertValid (CCSSProperties.WIDTH, "10PX", "AUTO");
    _assertValid (CCSSProperties.LETTER_SPACING, "1Q");
  }

  @Test
  public void testCommaLists ()
  {
    _assertValid (CCSSProperties.ANIMATION_DIRECTION, "normal", "normal, reverse", "normal,reverse, alternate");
    _assertInvalid (CCSSProperties.ANIMATION_DIRECTION, "any", "normal, any", "normal,", ",normal");
    _assertValid (CCSSProperties.TRANSITION_DURATION, "1s", "1s, 200ms");
    _assertValid (CCSSProperties.BACKGROUND_ATTACHMENT, "fixed", "fixed, scroll");
    _assertInvalid (CCSSProperties.BACKGROUND_ATTACHMENT, "fixed scroll");
    _assertValid (CCSSProperties.BACKGROUND_POSITION, "center, left top, 0 0");
    _assertValid (CCSSProperties.BACKGROUND_SIZE, "cover, auto auto");
    _assertValid (CCSSProperties.BACKGROUND_REPEAT, "repeat-x, no-repeat repeat");
    assertEquals (CCSSProperties.ANIMATION_DIRECTION,
                  CCSSProperties.ANIMATION_DIRECTION.getClone ((ECSSVendorPrefix) null));
  }

  @Test
  public void testFormerFreeProperties ()
  {
    _assertValid (CCSSProperties.FLEX_DIRECTION, "row", "row-reverse", "column", "column-reverse");
    _assertInvalid (CCSSProperties.FLEX_DIRECTION, "any", "row column");
    _assertValid (CCSSProperties.OBJECT_FIT, "fill", "contain", "cover", "none", "scale-down");
    _assertInvalid (CCSSProperties.OBJECT_FIT, "any");
    _assertValid (CCSSProperties.OVERFLOW_X, "visible", "hidden", "clip", "scroll", "auto");
    _assertValid (CCSSProperties.ALIGN_ITEMS, "center", "first baseline", "safe center", "anchor-center");
    _assertInvalid (CCSSProperties.ALIGN_ITEMS, "any");
    _assertValid (CCSSProperties.JUSTIFY_CONTENT, "space-between", "safe center");
    _assertValid (CCSSProperties.ORDER, "-1", "0", "3");
    _assertInvalid (CCSSProperties.ORDER, "1.5", "auto");
    _assertValid (CCSSProperties.COLUMN_COUNT, "auto", "3");
    _assertValid (CCSSProperties.COLUMN_GAP, "normal", "1em", "5%", "thin");
    _assertValid (CCSSProperties.TEXT_DECORATION_COLOR, "red", "oklch(50% 0.1 180)");
    _assertInvalid (CCSSProperties.TEXT_DECORATION_COLOR, "1px");
    _assertValid (CCSSProperties.OBJECT_POSITION, "center", "left 10px top 20px");
    // Not expressible, so they stay free
    _assertValid (CCSSProperties.COLUMN_RULE_WIDTH, "1px, repeat(5, 1px, 3px), 5px");
    _assertValid (CCSSProperties.LINE_CLAMP, "3 no-ellipsis");
  }

  @Test
  public void testNewProperties ()
  {
    _assertValid (CCSSProperties.ASPECT_RATIO, "16 / 9", "auto");
    _assertValid (CCSSProperties.ACCENT_COLOR, "auto", "red");
    _assertInvalid (CCSSProperties.ACCENT_COLOR, "1px");
    _assertValid (CCSSProperties.GAP, "1px", "1px 2px", "normal", "thin thick");
    _assertInvalid (CCSSProperties.GAP, "1px 2px 3px", "any");
    _assertValid (CCSSProperties.PADDING_INLINE, "1px", "1px 2px");
    _assertInvalid (CCSSProperties.PADDING_INLINE, "auto");
    _assertValid (CCSSProperties.MARGIN_BLOCK, "auto", "1px auto");
    _assertValid (CCSSProperties.INSET, "0", "auto 1px", "anchor(top) 0");
    _assertValid (CCSSProperties.STROKE_WIDTH, "1", "1.5", "1px", "10%");
    _assertInvalid (CCSSProperties.STROKE_WIDTH, "any");
    _assertValid (CCSSProperties.COLOR_SCHEME, "light dark");
    _assertValid (CCSSProperties.CONTAINER_TYPE, "normal", "size", "inline-size");
    _assertInvalid (CCSSProperties.CONTAINER_TYPE, "any");
    assertTrue (ECSSProperty.ACCENT_COLOR.getAllSpecifications ().contains (ECSSSpecification.CSS_UI_4));
  }

  @Test
  public void testVendorValuesAreRejected ()
  {
    _assertInvalid (CCSSProperties.TEXT_ALIGN, "-moz-center", "-webkit-center");
    _assertInvalid (CCSSProperties.DISPLAY, "-webkit-flex", "-ms-grid");
    _assertInvalid (CCSSProperties.LIST_STYLE_TYPE, "-moz-hangul", "hangul");
    _assertInvalid (CCSSProperties.MASK_CLIP, "padding", "border");
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
    assertTrue (CCSSProperties.COLOR.isValidValue ("#fffe"));
    assertTrue (CCSSProperties.COLOR.isValidValue (" #fffe   "));
    assertTrue (CCSSProperties.COLOR.isValidValue ("#ffffff80"));
    assertFalse (CCSSProperties.COLOR.isValidValue ("#ff"));
    assertFalse (CCSSProperties.COLOR.isValidValue ("#fffff"));
    assertFalse (CCSSProperties.COLOR.isValidValue ("#fffffff"));
    assertFalse (CCSSProperties.COLOR.isValidValue ("#fffffffff"));
    assertFalse (CCSSProperties.COLOR.isValidValue ("#aag"));
    assertFalse (CCSSProperties.COLOR.isValidValue ("#ppp"));
    assertTrue (CCSSProperties.CLIP.isValidValue ("auto"));
    assertTrue (CCSSProperties.CLIP.isValidValue ("rect(5px,10in,33px,456em)"));
    assertTrue (CCSSProperties.Z_INDEX.isValidValue ("-500"));
    assertTrue (CCSSProperties.Z_INDEX.isValidValue ("0"));
    assertTrue (CCSSProperties.Z_INDEX.isValidValue ("1000"));
  }
}
