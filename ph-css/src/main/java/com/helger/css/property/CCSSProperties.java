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

import static com.helger.css.propertyvalue.CCSSValue.*;

import com.helger.annotation.concurrent.Immutable;
import com.helger.annotation.misc.ChangeNextMajorRelease;
import com.helger.annotation.style.PresentForCodeCoverage;
import com.helger.base.array.ArrayHelper;
import com.helger.css.ECSSUnit;
import com.helger.css.property.customizer.CSSPropertyCustomizerBorderBottomLeftRadius;
import com.helger.css.property.customizer.CSSPropertyCustomizerBorderBottomRightRadius;
import com.helger.css.property.customizer.CSSPropertyCustomizerBorderRadius;
import com.helger.css.property.customizer.CSSPropertyCustomizerBorderTopLeftRadius;
import com.helger.css.property.customizer.CSSPropertyCustomizerBorderTopRightRadius;
import com.helger.css.property.customizer.CSSPropertyCustomizerOpacity;
import com.helger.css.propertyvalue.CCSSValue;
import com.helger.css.propertyvalue.ICSSValue;

/**
 * Contains the most commonly used CSS properties plus the available value ranges.
 *
 * @author Philip Helger
 */
@Immutable
public final class CCSSProperties
{
  private static final String [] ARRAY_ATTACHMENT = { SCROLL, FIXED, LOCAL };
  private static final String [] ARRAY_BORDER_STYLE = { NONE,
                                                        HIDDEN,
                                                        DOTTED,
                                                        DASHED,
                                                        SOLID,
                                                        DOUBLE,
                                                        GROOVE,
                                                        RIDGE,
                                                        CCSSValue.INSET,
                                                        OUTSET };
  private static final String [] ARRAY_BOX = { BORDER_BOX, PADDING_BOX, CONTENT_BOX };
  private static final String [] ARRAY_LINE_WIDTH = { THIN, MEDIUM, THICK };
  private static final String [] ARRAY_OUTLINE_LINE_STYLE = { NONE,
                                                              DOTTED,
                                                              DASHED,
                                                              SOLID,
                                                              DOUBLE,
                                                              GROOVE,
                                                              RIDGE,
                                                              CCSSValue.INSET,
                                                              OUTSET };
  private static final String [] ARRAY_REPEAT_STYLE = { REPEAT_X, REPEAT_Y, REPEAT, SPACE, ROUND, NO_REPEAT };
  private static final String [] ARRAY_SIZE = { MIN_CONTENT, MAX_CONTENT, FIT_CONTENT, STRETCH };

  // Text formatting
  public static final ICSSProperty FONT_FAMILY = new CSSPropertyFree (ECSSProperty.FONT_FAMILY);
  public static final ICSSProperty FONT_STYLE = new CSSPropertyEnum (ECSSProperty.FONT_STYLE, ITALIC, OBLIQUE, NORMAL);
  // The functional values are not covered
  public static final ICSSProperty FONT_VARIANT = new CSSPropertyEnumCombination (ECSSProperty.FONT_VARIANT,
                                                                                  16,
                                                                                  new String [] { NORMAL, NONE },
                                                                                  COMMON_LIGATURES,
                                                                                  NO_COMMON_LIGATURES,
                                                                                  DISCRETIONARY_LIGATURES,
                                                                                  NO_DISCRETIONARY_LIGATURES,
                                                                                  HISTORICAL_LIGATURES,
                                                                                  NO_HISTORICAL_LIGATURES,
                                                                                  CONTEXTUAL,
                                                                                  NO_CONTEXTUAL,
                                                                                  HISTORICAL_FORMS,
                                                                                  SMALL_CAPS,
                                                                                  ALL_SMALL_CAPS,
                                                                                  PETITE_CAPS,
                                                                                  ALL_PETITE_CAPS,
                                                                                  UNICASE,
                                                                                  TITLING_CAPS,
                                                                                  LINING_NUMS,
                                                                                  OLDSTYLE_NUMS,
                                                                                  PROPORTIONAL_NUMS,
                                                                                  TABULAR_NUMS,
                                                                                  DIAGONAL_FRACTIONS,
                                                                                  STACKED_FRACTIONS,
                                                                                  ORDINAL,
                                                                                  SLASHED_ZERO,
                                                                                  JIS78,
                                                                                  JIS83,
                                                                                  JIS90,
                                                                                  JIS04,
                                                                                  SIMPLIFIED,
                                                                                  TRADITIONAL,
                                                                                  FULL_WIDTH,
                                                                                  PROPORTIONAL_WIDTH,
                                                                                  RUBY,
                                                                                  SUB,
                                                                                  SUPER,
                                                                                  TEXT,
                                                                                  EMOJI,
                                                                                  UNICODE);
  public static final ICSSProperty FONT_SIZE = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.FONT_SIZE,
                                                                                    true,
                                                                                    XX_SMALL,
                                                                                    X_SMALL,
                                                                                    SMALL,
                                                                                    MEDIUM,
                                                                                    LARGE,
                                                                                    X_LARGE,
                                                                                    XX_LARGE,
                                                                                    XXX_LARGE,
                                                                                    SMALLER,
                                                                                    LARGER,
                                                                                    MATH);
  public static final ICSSProperty FONT_WEIGHT = new CSSPropertyEnumOrDouble (ECSSProperty.FONT_WEIGHT,
                                                                              1,
                                                                              1000,
                                                                              BOLD,
                                                                              BOLDER,
                                                                              LIGHTER,
                                                                              NORMAL);
  public static final ICSSProperty WORD_SPACING = new CSSPropertyEnumOrNumber (ECSSProperty.WORD_SPACING, true, NORMAL);
  public static final ICSSProperty LETTER_SPACING = new CSSPropertyEnumOrNumber (ECSSProperty.LETTER_SPACING,
                                                                                 true,
                                                                                 NORMAL);
  public static final ICSSProperty TEXT_DECORATION = new CSSPropertyFree (ECSSProperty.TEXT_DECORATION);
  public static final ICSSProperty TEXT_TRANSFORM = new CSSPropertyEnumCombination (ECSSProperty.TEXT_TRANSFORM,
                                                                                    3,
                                                                                    new String [] { NONE, MATH_AUTO },
                                                                                    CAPITALIZE,
                                                                                    UPPERCASE,
                                                                                    LOWERCASE,
                                                                                    FULL_WIDTH,
                                                                                    FULL_SIZE_KANA);
  public static final ICSSProperty COLOR = new CSSPropertyColor (ECSSProperty.COLOR);

  // Orientation and paragraph control
  public static final ICSSProperty TEXT_INDENT = new CSSPropertyEnumOrNumbers (ECSSProperty.TEXT_INDENT,
                                                                               true,
                                                                               1,
                                                                               3,
                                                                               HANGING,
                                                                               EACH_LINE);
  public static final ICSSProperty LINE_HEIGHT = new CSSPropertyEnumOrNumberOrDouble (ECSSProperty.LINE_HEIGHT,
                                                                                      true,
                                                                                      NORMAL);
  public static final ICSSProperty VERTICAL_ALIGN = new CSSPropertyEnumOrNumber (ECSSProperty.VERTICAL_ALIGN,
                                                                                 true,
                                                                                 CCSSValue.TOP,
                                                                                 MIDDLE,
                                                                                 CCSSValue.BOTTOM,
                                                                                 BASELINE,
                                                                                 SUB,
                                                                                 SUPER,
                                                                                 TEXT_TOP,
                                                                                 TEXT_BOTTOM);
  public static final ICSSProperty TEXT_ALIGN = new CSSPropertyEnum (ECSSProperty.TEXT_ALIGN,
                                                                     START,
                                                                     END,
                                                                     CCSSValue.LEFT,
                                                                     CENTER,
                                                                     CCSSValue.RIGHT,
                                                                     JUSTIFY,
                                                                     MATCH_PARENT);
  public static final ICSSProperty WHITE_SPACE = new CSSPropertyEnumCombination (ECSSProperty.WHITE_SPACE,
                                                                                 2,
                                                                                 new String [] { NORMAL, PRE, PRE_LINE, PRE_WRAP },
                                                                                 NOWRAP,
                                                                                 BREAK_SPACES,
                                                                                 COLLAPSE,
                                                                                 PRESERVE,
                                                                                 PRESERVE_BREAKS,
                                                                                 PRESERVE_SPACES,
                                                                                 WRAP);

  // margin
  public static final ICSSProperty MARGIN_TOP = new CSSPropertyEnumOrNumber (ECSSProperty.MARGIN_TOP, true, AUTO);
  public static final ICSSProperty MARGIN_RIGHT = new CSSPropertyEnumOrNumber (ECSSProperty.MARGIN_RIGHT, true, AUTO);
  public static final ICSSProperty MARGIN_BOTTOM = new CSSPropertyEnumOrNumber (ECSSProperty.MARGIN_BOTTOM, true, AUTO);
  public static final ICSSProperty MARGIN_LEFT = new CSSPropertyEnumOrNumber (ECSSProperty.MARGIN_LEFT, true, AUTO);
  public static final ICSSProperty MARGIN = new CSSPropertyEnumOrNumbers (ECSSProperty.MARGIN, true, 1, 4, AUTO);
  // padding
  public static final ICSSProperty PADDING_TOP = new CSSPropertyUnsignedNumber (ECSSProperty.PADDING_TOP, true);
  public static final ICSSProperty PADDING_RIGHT = new CSSPropertyUnsignedNumber (ECSSProperty.PADDING_RIGHT, true);
  public static final ICSSProperty PADDING_BOTTOM = new CSSPropertyUnsignedNumber (ECSSProperty.PADDING_BOTTOM, true);
  public static final ICSSProperty PADDING_LEFT = new CSSPropertyUnsignedNumber (ECSSProperty.PADDING_LEFT, true);
  public static final ICSSProperty PADDING = new CSSPropertyUnsignedNumbers (ECSSProperty.PADDING, true, 1, 4);

  // borders

  // list formatting
  // <custom-ident>, <string> and symbols() are not covered, because the list-style shorthand
  // needs a distinct value range
  public static final ICSSProperty LIST_STYLE_TYPE = new CSSPropertyEnum (ECSSProperty.LIST_STYLE_TYPE,
                                                                          NONE,
                                                                          DISC,
                                                                          CIRCLE,
                                                                          SQUARE,
                                                                          DECIMAL,
                                                                          CJK_DECIMAL,
                                                                          DECIMAL_LEADING_ZERO,
                                                                          LOWER_ROMAN,
                                                                          UPPER_ROMAN,
                                                                          LOWER_GREEK,
                                                                          LOWER_ALPHA,
                                                                          LOWER_LATIN,
                                                                          UPPER_ALPHA,
                                                                          UPPER_LATIN,
                                                                          ARABIC_INDIC,
                                                                          ARMENIAN,
                                                                          BENGALI,
                                                                          CAMBODIAN,
                                                                          KHMER,
                                                                          CJK_EARTHLY_BRANCH,
                                                                          CJK_HEAVENLY_STEM,
                                                                          CJK_IDEOGRAPHIC,
                                                                          DEVANAGARI,
                                                                          ETHIOPIC_NUMERIC,
                                                                          GEORGIAN,
                                                                          GUJARATI,
                                                                          GURMUKHI,
                                                                          HEBREW,
                                                                          HIRAGANA,
                                                                          HIRAGANA_IROHA,
                                                                          JAPANESE_FORMAL,
                                                                          JAPANESE_INFORMAL,
                                                                          KANNADA,
                                                                          KATAKANA,
                                                                          KATAKANA_IROHA,
                                                                          KOREAN_HANGUL_FORMAL,
                                                                          KOREAN_HANJA_FORMAL,
                                                                          KOREAN_HANJA_INFORMAL,
                                                                          LAO,
                                                                          LOWER_ARMENIAN,
                                                                          MALAYALAM,
                                                                          MONGOLIAN,
                                                                          MYANMAR,
                                                                          ORIYA,
                                                                          PERSIAN,
                                                                          SIMP_CHINESE_FORMAL,
                                                                          SIMP_CHINESE_INFORMAL,
                                                                          TAMIL,
                                                                          TELUGU,
                                                                          THAI,
                                                                          TIBETAN,
                                                                          TRAD_CHINESE_FORMAL,
                                                                          TRAD_CHINESE_INFORMAL,
                                                                          UPPER_ARMENIAN,
                                                                          DISCLOSURE_OPEN,
                                                                          DISCLOSURE_CLOSED);
  public static final ICSSProperty LIST_STYLE_POSITION = new CSSPropertyEnum (ECSSProperty.LIST_STYLE_POSITION,
                                                                              INSIDE,
                                                                              OUTSIDE);
  public static final ICSSProperty LIST_STYLE_IMAGE = new CSSPropertyURL (ECSSProperty.LIST_STYLE_IMAGE);

  // table formatting
  public static final ICSSProperty CAPTION_SIDE = new CSSPropertyEnum (ECSSProperty.CAPTION_SIDE,
                                                                       CCSSValue.TOP,
                                                                       CCSSValue.BOTTOM);
  public static final ICSSProperty TABLE_LAYOUT = new CSSPropertyEnum (ECSSProperty.TABLE_LAYOUT, AUTO, FIXED);
  public static final ICSSProperty BORDER_COLLAPSE = new CSSPropertyEnum (ECSSProperty.BORDER_COLLAPSE,
                                                                          SEPARATE,
                                                                          COLLAPSE);
  public static final ICSSProperty BORDER_SPACING = new CSSPropertyNumbers (ECSSProperty.BORDER_SPACING, false, 1, 2);
  public static final ICSSProperty EMPTY_CELLS = new CSSPropertyEnum (ECSSProperty.EMPTY_CELLS, SHOW, HIDE);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty SPEAK_HEADER = new CSSPropertyEnum (ECSSProperty.SPEAK_HEADER, ALWAYS, ONCE);

  // positioning
  public static final ICSSProperty POSITION = new CSSPropertyEnum (ECSSProperty.POSITION,
                                                                   STATIC,
                                                                   RELATIVE,
                                                                   ABSOLUTE,
                                                                   FIXED,
                                                                   STICKY);
  public static final ICSSProperty TOP = new CSSPropertyEnumOrNumber (ECSSProperty.TOP, true, AUTO);
  public static final ICSSProperty LEFT = new CSSPropertyEnumOrNumber (ECSSProperty.LEFT, true, AUTO);
  public static final ICSSProperty BOTTOM = new CSSPropertyEnumOrNumber (ECSSProperty.BOTTOM, true, AUTO);
  public static final ICSSProperty RIGHT = new CSSPropertyEnumOrNumber (ECSSProperty.RIGHT, true, AUTO);
  public static final ICSSProperty WIDTH = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.WIDTH,
                                                                                true,
                                                                                ArrayHelper.getConcatenated (AUTO, ARRAY_SIZE));
  public static final ICSSProperty MIN_WIDTH = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.MIN_WIDTH,
                                                                                    true,
                                                                                    ArrayHelper.getConcatenated (AUTO, ARRAY_SIZE));
  public static final ICSSProperty MAX_WIDTH = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.MAX_WIDTH,
                                                                                    true,
                                                                                    ArrayHelper.getConcatenated (NONE, ARRAY_SIZE));
  public static final ICSSProperty HEIGHT = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.HEIGHT,
                                                                                 true,
                                                                                 ArrayHelper.getConcatenated (AUTO, ARRAY_SIZE));
  public static final ICSSProperty MIN_HEIGHT = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.MIN_HEIGHT,
                                                                                     true,
                                                                                     ArrayHelper.getConcatenated (AUTO, ARRAY_SIZE));
  public static final ICSSProperty MAX_HEIGHT = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.MAX_HEIGHT,
                                                                                     true,
                                                                                     ArrayHelper.getConcatenated (NONE, ARRAY_SIZE));
  public static final ICSSProperty OVERFLOW = new CSSPropertyEnums (ECSSProperty.OVERFLOW,
                                                                    1,
                                                                    2,
                                                                    VISIBLE,
                                                                    HIDDEN,
                                                                    CCSSValue.CLIP,
                                                                    SCROLL,
                                                                    AUTO,
                                                                    CCSSValue.OVERLAY);
  public static final ICSSProperty FLOAT = new CSSPropertyEnum (ECSSProperty.FLOAT,
                                                                CCSSValue.LEFT,
                                                                CCSSValue.RIGHT,
                                                                NONE,
                                                                INLINE_START,
                                                                INLINE_END);
  public static final ICSSProperty CLEAR = new CSSPropertyEnum (ECSSProperty.CLEAR,
                                                                CCSSValue.LEFT,
                                                                CCSSValue.RIGHT,
                                                                BOTH,
                                                                NONE,
                                                                INLINE_START,
                                                                INLINE_END);
  public static final ICSSProperty Z_INDEX = new CSSPropertyEnumOrInt (ECSSProperty.Z_INDEX, AUTO);
  // [ <display-outside> || <display-inside> ] or <display-listitem> have up to 3 values
  public static final ICSSProperty DISPLAY = new CSSPropertyEnumCombination (ECSSProperty.DISPLAY,
                                                                             3,
                                                                             new String [] { INLINE_BLOCK, NONE, CONTENTS, INLINE_TABLE, TABLE_ROW, TABLE_CELL, TABLE_ROW_GROUP, TABLE_HEADER_GROUP, TABLE_FOOTER_GROUP, TABLE_COLUMN, TABLE_COLUMN_GROUP, TABLE_CAPTION, INLINE_FLEX, INLINE_GRID, INLINE_LIST_ITEM, RUBY_BASE, RUBY_TEXT, RUBY_BASE_CONTAINER, RUBY_TEXT_CONTAINER, INLINE_GRID_LANES },
                                                                             BLOCK,
                                                                             INLINE,
                                                                             LIST_ITEM,
                                                                             RUN_IN,
                                                                             FLOW,
                                                                             FLOW_ROOT,
                                                                             TABLE,
                                                                             CCSSValue.FLEX,
                                                                             CCSSValue.GRID,
                                                                             RUBY,
                                                                             GRID_LANES);
  public static final ICSSProperty VISIBILITY = new CSSPropertyEnum (ECSSProperty.VISIBILITY,
                                                                     VISIBLE,
                                                                     HIDDEN,
                                                                     COLLAPSE);
  public static final ICSSProperty CLIP = new CSSPropertyEnumOrRect (ECSSProperty.CLIP, AUTO);

  // display window
  public static final ICSSProperty OPACITY = new CSSPropertyDoubleOrPercentage (ECSSProperty.OPACITY,
                                                                                new CSSPropertyCustomizerOpacity ());

  /*
   * CSS Writing Modes Level 3
   */
  public static final ICSSProperty DIRECTION = new CSSPropertyEnum (ECSSProperty.DIRECTION, LTR, RTL);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty GLYPH_ORIENTATION_VERTICAL = new CSSPropertyEnumOrNumber (ECSSProperty.GLYPH_ORIENTATION_VERTICAL,
                                                                                             false,
                                                                                             AUTO,
                                                                                             ECSSUnit.deg (0),
                                                                                             ECSSUnit.deg (90));
  public static final ICSSProperty TEXT_COMBINE_UPRIGHT = new CSSPropertyEnum (ECSSProperty.TEXT_COMBINE_UPRIGHT,
                                                                               NONE,
                                                                               CCSSValue.ALL,
                                                                               DIGITS);
  public static final ICSSProperty TEXT_ORIENTATION = new CSSPropertyEnum (ECSSProperty.TEXT_ORIENTATION,
                                                                           MIXED,
                                                                           UPRIGHT,
                                                                           SIDEWAYS,
                                                                           SIDEWAYS_RIGHT,
                                                                           USE_GLYPH_ORIENTATION);
  public static final ICSSProperty UNICODE_BIDI = new CSSPropertyEnum (ECSSProperty.UNICODE_BIDI,
                                                                       NORMAL,
                                                                       EMBED,
                                                                       ISOLATE,
                                                                       BIDI_OVERRIDE,
                                                                       ISOLATE_OVERRIDE,
                                                                       PLAINTEXT);
  public static final ICSSProperty WRITING_MODE = new CSSPropertyEnum (ECSSProperty.WRITING_MODE,
                                                                       HORIZONTAL_TB,
                                                                       VERTICAL_RL,
                                                                       VERTICAL_LR,
                                                                       SIDEWAYS_RL,
                                                                       SIDEWAYS_LR,
                                                                       LR,
                                                                       LR_TB,
                                                                       RL,
                                                                       TB,
                                                                       TB_LR,
                                                                       TB_RL);

  /*
   * CSS Basic User Interface Level 3
   */
  public static final ICSSProperty BOX_SIZING = new CSSPropertyEnum (ECSSProperty.BOX_SIZING, CONTENT_BOX, BORDER_BOX);
  public static final ICSSProperty CARET_COLOR = new CSSPropertyEnumOrColor (ECSSProperty.CARET_COLOR, AUTO);
  // URL prefix is missing
  // [ [<url> [<x> <y>]?,]* [ auto | ... ] ]
  public static final ICSSProperty CURSOR = new CSSPropertyEnum (ECSSProperty.CURSOR,
                                                                 AUTO,
                                                                 DEFAULT,
                                                                 NONE,
                                                                 CONTEXT_MENU,
                                                                 HELP,
                                                                 POINTER,
                                                                 PROGRESS,
                                                                 WAIT,
                                                                 CELL,
                                                                 CROSSHAIR,
                                                                 TEXT,
                                                                 VERTICAL_TEXT,
                                                                 ALIAS,
                                                                 COPY,
                                                                 MOVE,
                                                                 NO_DROP,
                                                                 NOT_ALLOWED,
                                                                 GRAB,
                                                                 GRABBING,
                                                                 E_RESIZE,
                                                                 N_RESIZE,
                                                                 NE_RESIZE,
                                                                 NW_RESIZE,
                                                                 S_RESIZE,
                                                                 SE_RESIZE,
                                                                 SW_RESIZE,
                                                                 W_RESIZE,
                                                                 EW_RESIZE,
                                                                 NS_RESIZE,
                                                                 NESW_RESIZE,
                                                                 NWSE_RESIZE,
                                                                 COL_RESIZE,
                                                                 ROW_RESIZE,
                                                                 ALL_SCROLL,
                                                                 ZOOM_IN,
                                                                 ZOOM_OUT);
  public static final ICSSProperty OUTLINE = new CSSPropertyFree (ECSSProperty.OUTLINE);
  public static final ICSSProperty OUTLINE_COLOR = new CSSPropertyEnumOrColor (ECSSProperty.OUTLINE_COLOR, AUTO);
  public static final ICSSProperty OUTLINE_OFFSET = new CSSPropertyNumber (ECSSProperty.OUTLINE_OFFSET, false);
  public static final ICSSProperty OUTLINE_STYLE = new CSSPropertyEnum (ECSSProperty.OUTLINE_STYLE,
                                                                        ArrayHelper.getConcatenated (AUTO, ARRAY_OUTLINE_LINE_STYLE));
  public static final ICSSProperty OUTLINE_WIDTH = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.OUTLINE_WIDTH,
                                                                                        false,
                                                                                        ARRAY_LINE_WIDTH);
  public static final ICSSProperty RESIZE = new CSSPropertyEnum (ECSSProperty.RESIZE,
                                                                 NONE,
                                                                 BOTH,
                                                                 HORIZONTAL,
                                                                 VERTICAL,
                                                                 BLOCK,
                                                                 INLINE);
  public static final ICSSProperty TEXT_OVERFLOW = new CSSPropertyEnums (ECSSProperty.TEXT_OVERFLOW,
                                                                         1,
                                                                         2,
                                                                         CCSSValue.CLIP,
                                                                         ELLIPSIS);

  /*
   * CSS Containment Module Level 1
   */
  // none | strict | content | [ size || layout || paint ]
  public static final ICSSProperty CONTAIN = new CSSPropertyFree (ECSSProperty.CONTAIN);

  /*
   * CSS Backgrounds and Borders Level 3
   */
  public static final ICSSProperty BACKGROUND = new CSSPropertyFree (ECSSProperty.BACKGROUND);
  public static final ICSSProperty BACKGROUND_ATTACHMENT = new CSSPropertyList (ECSSProperty.BACKGROUND_ATTACHMENT,
                                                                                new CSSPropertyEnum (ECSSProperty.BACKGROUND_ATTACHMENT,
                                                                                                     ARRAY_ATTACHMENT));
  public static final ICSSProperty BACKGROUND_CLIP = new CSSPropertyList (ECSSProperty.BACKGROUND_CLIP,
                                                                          new CSSPropertyEnum (ECSSProperty.BACKGROUND_CLIP,
                                                                                               BORDER_BOX,
                                                                                               PADDING_BOX,
                                                                                               CONTENT_BOX,
                                                                                               BORDER_AREA,
                                                                                               TEXT));
  public static final ICSSProperty BACKGROUND_COLOR = new CSSPropertyColor (ECSSProperty.BACKGROUND_COLOR);
  public static final ICSSProperty BACKGROUND_IMAGE = new CSSPropertyList (ECSSProperty.BACKGROUND_IMAGE,
                                                                           new CSSPropertyEnumOrURL (ECSSProperty.BACKGROUND_IMAGE,
                                                                                                     NONE));
  public static final ICSSProperty BACKGROUND_ORIGIN = new CSSPropertyList (ECSSProperty.BACKGROUND_ORIGIN,
                                                                            new CSSPropertyEnum (ECSSProperty.BACKGROUND_ORIGIN,
                                                                                                 ARRAY_BOX));
  public static final ICSSProperty BACKGROUND_POSITION = new CSSPropertyList (ECSSProperty.BACKGROUND_POSITION,
                                                                              new CSSPropertyEnumOrNumbers (ECSSProperty.BACKGROUND_POSITION,
                                                                                                            true,
                                                                                                            1,
                                                                                                            4,
                                                                                                            CCSSValue.TOP,
                                                                                                            CCSSValue.BOTTOM,
                                                                                                            CENTER,
                                                                                                            CCSSValue.LEFT,
                                                                                                            CCSSValue.RIGHT));
  // repeat-x | repeat-y | [repeat | space | round | no-repeat]{1,2}
  public static final ICSSProperty BACKGROUND_REPEAT = new CSSPropertyList (ECSSProperty.BACKGROUND_REPEAT,
                                                                            new CSSPropertyEnums (ECSSProperty.BACKGROUND_REPEAT,
                                                                                                  1,
                                                                                                  2,
                                                                                                  ARRAY_REPEAT_STYLE));
  public static final ICSSProperty BACKGROUND_SIZE = new CSSPropertyList (ECSSProperty.BACKGROUND_SIZE,
                                                                          new CSSPropertyEnumOrUnsignedNumbers (ECSSProperty.BACKGROUND_SIZE,
                                                                                                                true,
                                                                                                                1,
                                                                                                                2,
                                                                                                                AUTO,
                                                                                                                COVER,
                                                                                                                CCSSValue.CONTAIN));
  public static final ICSSProperty BORDER = new CSSPropertyFree (ECSSProperty.BORDER);
  public static final ICSSProperty BORDER_BOTTOM = new CSSPropertyFree (ECSSProperty.BORDER_BOTTOM);
  public static final ICSSProperty BORDER_LEFT = new CSSPropertyFree (ECSSProperty.BORDER_LEFT);
  public static final ICSSProperty BORDER_RIGHT = new CSSPropertyFree (ECSSProperty.BORDER_RIGHT);
  public static final ICSSProperty BORDER_TOP = new CSSPropertyFree (ECSSProperty.BORDER_TOP);

  public static final ICSSProperty BORDER_BOTTOM_WIDTH = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.BORDER_BOTTOM_WIDTH,
                                                                                              false,
                                                                                              ARRAY_LINE_WIDTH);
  public static final ICSSProperty BORDER_LEFT_WIDTH = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.BORDER_LEFT_WIDTH,
                                                                                            false,
                                                                                            ARRAY_LINE_WIDTH);
  public static final ICSSProperty BORDER_RIGHT_WIDTH = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.BORDER_RIGHT_WIDTH,
                                                                                             false,
                                                                                             ARRAY_LINE_WIDTH);
  public static final ICSSProperty BORDER_TOP_WIDTH = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.BORDER_TOP_WIDTH,
                                                                                           false,
                                                                                           ARRAY_LINE_WIDTH);
  public static final ICSSProperty BORDER_WIDTH = new CSSPropertyEnumOrUnsignedNumbers (ECSSProperty.BORDER_WIDTH,
                                                                                        false,
                                                                                        1,
                                                                                        4,
                                                                                        ARRAY_LINE_WIDTH);
  public static final ICSSProperty BORDER_BOTTOM_COLOR = new CSSPropertyColor (ECSSProperty.BORDER_BOTTOM_COLOR);
  public static final ICSSProperty BORDER_LEFT_COLOR = new CSSPropertyColor (ECSSProperty.BORDER_LEFT_COLOR);
  public static final ICSSProperty BORDER_RIGHT_COLOR = new CSSPropertyColor (ECSSProperty.BORDER_RIGHT_COLOR);
  public static final ICSSProperty BORDER_TOP_COLOR = new CSSPropertyColor (ECSSProperty.BORDER_TOP_COLOR);
  public static final ICSSProperty BORDER_COLOR = new CSSPropertyColors (ECSSProperty.BORDER_COLOR, 1, 4);

  public static final ICSSProperty BORDER_BOTTOM_STYLE = new CSSPropertyEnum (ECSSProperty.BORDER_BOTTOM_STYLE,
                                                                              ARRAY_BORDER_STYLE);
  public static final ICSSProperty BORDER_LEFT_STYLE = new CSSPropertyEnum (ECSSProperty.BORDER_LEFT_STYLE,
                                                                            ARRAY_BORDER_STYLE);
  public static final ICSSProperty BORDER_RIGHT_STYLE = new CSSPropertyEnum (ECSSProperty.BORDER_RIGHT_STYLE,
                                                                             ARRAY_BORDER_STYLE);
  public static final ICSSProperty BORDER_TOP_STYLE = new CSSPropertyEnum (ECSSProperty.BORDER_TOP_STYLE,
                                                                           ARRAY_BORDER_STYLE);
  public static final ICSSProperty BORDER_STYLE = new CSSPropertyEnums (ECSSProperty.BORDER_STYLE,
                                                                        1,
                                                                        4,
                                                                        ARRAY_BORDER_STYLE);

  public static final ICSSProperty BORDER_TOP_LEFT_RADIUS = new CSSPropertyUnsignedNumbers (ECSSProperty.BORDER_TOP_LEFT_RADIUS,
                                                                                            new CSSPropertyCustomizerBorderTopLeftRadius (),
                                                                                            true,
                                                                                            1,
                                                                                            2);
  public static final ICSSProperty BORDER_TOP_RIGHT_RADIUS = new CSSPropertyUnsignedNumbers (ECSSProperty.BORDER_TOP_RIGHT_RADIUS,
                                                                                             new CSSPropertyCustomizerBorderTopRightRadius (),
                                                                                             true,
                                                                                             1,
                                                                                             2);
  public static final ICSSProperty BORDER_BOTTOM_LEFT_RADIUS = new CSSPropertyUnsignedNumbers (ECSSProperty.BORDER_BOTTOM_LEFT_RADIUS,
                                                                                               new CSSPropertyCustomizerBorderBottomLeftRadius (),
                                                                                               true,
                                                                                               1,
                                                                                               2);
  public static final ICSSProperty BORDER_BOTTOM_RIGHT_RADIUS = new CSSPropertyUnsignedNumbers (ECSSProperty.BORDER_BOTTOM_RIGHT_RADIUS,
                                                                                                new CSSPropertyCustomizerBorderBottomRightRadius (),
                                                                                                true,
                                                                                                1,
                                                                                                2);
  public static final ICSSProperty BORDER_RADIUS = new CSSPropertyFree (ECSSProperty.BORDER_RADIUS,
                                                                        new CSSPropertyCustomizerBorderRadius ());

  /*
   * All other properties. Values that are not fully covered by a dedicated class are free.
   */
  public static final ICSSProperty ACCENT_COLOR = new CSSPropertyEnumOrColor (ECSSProperty.ACCENT_COLOR, AUTO);
  public static final ICSSProperty ALIGNMENT_BASELINE = new CSSPropertyEnum (ECSSProperty.ALIGNMENT_BASELINE,
                                                                             ALPHABETIC,
                                                                             BASELINE,
                                                                             CENTRAL,
                                                                             IDEOGRAPHIC,
                                                                             MATHEMATICAL,
                                                                             MIDDLE,
                                                                             TEXT_AFTER_EDGE,
                                                                             TEXT_BEFORE_EDGE,
                                                                             AUTO,
                                                                             AFTER_EDGE,
                                                                             BEFORE_EDGE,
                                                                             HANGING,
                                                                             TEXT_BOTTOM,
                                                                             TEXT_TOP);
  public static final ICSSProperty ALIGN_CONTENT = new CSSPropertyEnumCombination (ECSSProperty.ALIGN_CONTENT,
                                                                                   2,
                                                                                   new String [] { NORMAL, SPACE_AROUND, SPACE_BETWEEN, SPACE_EVENLY, STRETCH },
                                                                                   BASELINE,
                                                                                   CENTER,
                                                                                   END,
                                                                                   FIRST,
                                                                                   FLEX_END,
                                                                                   FLEX_START,
                                                                                   LAST,
                                                                                   SAFE,
                                                                                   START,
                                                                                   UNSAFE);
  public static final ICSSProperty ALIGN_ITEMS = new CSSPropertyEnumCombination (ECSSProperty.ALIGN_ITEMS,
                                                                                 2,
                                                                                 new String [] { ANCHOR_CENTER, NORMAL, STRETCH },
                                                                                 BASELINE,
                                                                                 CENTER,
                                                                                 END,
                                                                                 FIRST,
                                                                                 FLEX_END,
                                                                                 FLEX_START,
                                                                                 LAST,
                                                                                 SAFE,
                                                                                 SELF_END,
                                                                                 SELF_START,
                                                                                 START,
                                                                                 UNSAFE);
  public static final ICSSProperty ALIGN_SELF = new CSSPropertyEnumCombination (ECSSProperty.ALIGN_SELF,
                                                                                2,
                                                                                new String [] { ANCHOR_CENTER, AUTO, NORMAL, STRETCH },
                                                                                BASELINE,
                                                                                CENTER,
                                                                                END,
                                                                                FIRST,
                                                                                FLEX_END,
                                                                                FLEX_START,
                                                                                LAST,
                                                                                SAFE,
                                                                                SELF_END,
                                                                                SELF_START,
                                                                                START,
                                                                                UNSAFE);
  public static final ICSSProperty ALL = new CSSPropertyEnum (ECSSProperty.ALL,
                                                              INHERIT,
                                                              INITIAL,
                                                              REVERT,
                                                              REVERT_LAYER,
                                                              UNSET);
  public static final ICSSProperty ANCHOR_NAME = new CSSPropertyFree (ECSSProperty.ANCHOR_NAME);
  public static final ICSSProperty ANCHOR_SCOPE = new CSSPropertyFree (ECSSProperty.ANCHOR_SCOPE);
  public static final ICSSProperty ANIMATION = new CSSPropertyFree (ECSSProperty.ANIMATION);
  public static final ICSSProperty ANIMATION_COMPOSITION = new CSSPropertyList (ECSSProperty.ANIMATION_COMPOSITION,
                                                                                new CSSPropertyEnum (ECSSProperty.ANIMATION_COMPOSITION,
                                                                                                     ACCUMULATE,
                                                                                                     ADD,
                                                                                                     REPLACE));
  public static final ICSSProperty ANIMATION_DELAY = new CSSPropertyList (ECSSProperty.ANIMATION_DELAY,
                                                                          new CSSPropertyNumber (ECSSProperty.ANIMATION_DELAY,
                                                                                                 false));
  public static final ICSSProperty ANIMATION_DIRECTION = new CSSPropertyList (ECSSProperty.ANIMATION_DIRECTION,
                                                                              new CSSPropertyEnum (ECSSProperty.ANIMATION_DIRECTION,
                                                                                                   ALTERNATE,
                                                                                                   ALTERNATE_REVERSE,
                                                                                                   NORMAL,
                                                                                                   REVERSE));
  public static final ICSSProperty ANIMATION_DURATION = new CSSPropertyList (ECSSProperty.ANIMATION_DURATION,
                                                                             new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.ANIMATION_DURATION,
                                                                                                                  false,
                                                                                                                  AUTO));
  public static final ICSSProperty ANIMATION_FILL_MODE = new CSSPropertyList (ECSSProperty.ANIMATION_FILL_MODE,
                                                                              new CSSPropertyEnum (ECSSProperty.ANIMATION_FILL_MODE,
                                                                                                   BACKWARDS,
                                                                                                   BOTH,
                                                                                                   FORWARDS,
                                                                                                   NONE));
  public static final ICSSProperty ANIMATION_ITERATION_COUNT = new CSSPropertyList (ECSSProperty.ANIMATION_ITERATION_COUNT,
                                                                                    new CSSPropertyEnumOrDouble (ECSSProperty.ANIMATION_ITERATION_COUNT,
                                                                                                                 0,
                                                                                                                 Double.POSITIVE_INFINITY,
                                                                                                                 INFINITE));
  public static final ICSSProperty ANIMATION_NAME = new CSSPropertyFree (ECSSProperty.ANIMATION_NAME);
  public static final ICSSProperty ANIMATION_PLAY_STATE = new CSSPropertyList (ECSSProperty.ANIMATION_PLAY_STATE,
                                                                               new CSSPropertyEnum (ECSSProperty.ANIMATION_PLAY_STATE,
                                                                                                    PAUSED,
                                                                                                    CCSSValue.RUNNING));
  public static final ICSSProperty ANIMATION_RANGE = new CSSPropertyFree (ECSSProperty.ANIMATION_RANGE);
  public static final ICSSProperty ANIMATION_RANGE_END = new CSSPropertyList (ECSSProperty.ANIMATION_RANGE_END,
                                                                              new CSSPropertyEnumOrNumbers (ECSSProperty.ANIMATION_RANGE_END,
                                                                                                            true,
                                                                                                            1,
                                                                                                            2,
                                                                                                            CCSSValue.CONTAIN,
                                                                                                            COVER,
                                                                                                            ENTRY,
                                                                                                            ENTRY_CROSSING,
                                                                                                            EXIT,
                                                                                                            EXIT_CROSSING,
                                                                                                            NORMAL));
  public static final ICSSProperty ANIMATION_RANGE_START = new CSSPropertyList (ECSSProperty.ANIMATION_RANGE_START,
                                                                                new CSSPropertyEnumOrNumbers (ECSSProperty.ANIMATION_RANGE_START,
                                                                                                              true,
                                                                                                              1,
                                                                                                              2,
                                                                                                              CCSSValue.CONTAIN,
                                                                                                              COVER,
                                                                                                              ENTRY,
                                                                                                              ENTRY_CROSSING,
                                                                                                              EXIT,
                                                                                                              EXIT_CROSSING,
                                                                                                              NORMAL));
  public static final ICSSProperty ANIMATION_TIMELINE = new CSSPropertyFree (ECSSProperty.ANIMATION_TIMELINE);
  public static final ICSSProperty ANIMATION_TIMING_FUNCTION = new CSSPropertyFree (ECSSProperty.ANIMATION_TIMING_FUNCTION);
  public static final ICSSProperty ANIMATION_TRIGGER = new CSSPropertyFree (ECSSProperty.ANIMATION_TRIGGER);
  public static final ICSSProperty APPEARANCE = new CSSPropertyEnum (ECSSProperty.APPEARANCE,
                                                                     AUTO,
                                                                     BUTTON,
                                                                     CHECKBOX,
                                                                     LISTBOX,
                                                                     MENULIST,
                                                                     MENULIST_BUTTON,
                                                                     METER,
                                                                     NONE,
                                                                     PROGRESS_BAR,
                                                                     RADIO,
                                                                     SEARCHFIELD,
                                                                     TEXTAREA,
                                                                     TEXTFIELD,
                                                                     BASE_SELECT);
  public static final ICSSProperty ASPECT_RATIO = new CSSPropertyFree (ECSSProperty.ASPECT_RATIO);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty AZIMUTH = new CSSPropertyFree (ECSSProperty.AZIMUTH);
  public static final ICSSProperty BACKDROP_FILTER = new CSSPropertyFree (ECSSProperty.BACKDROP_FILTER);
  public static final ICSSProperty BACKFACE_VISIBILITY = new CSSPropertyEnum (ECSSProperty.BACKFACE_VISIBILITY,
                                                                              HIDDEN,
                                                                              VISIBLE);
  public static final ICSSProperty BACKGROUND_BLEND_MODE = new CSSPropertyList (ECSSProperty.BACKGROUND_BLEND_MODE,
                                                                                new CSSPropertyEnum (ECSSProperty.BACKGROUND_BLEND_MODE,
                                                                                                     CCSSValue.COLOR,
                                                                                                     COLOR_BURN,
                                                                                                     COLOR_DODGE,
                                                                                                     DARKEN,
                                                                                                     DIFFERENCE,
                                                                                                     EXCLUSION,
                                                                                                     HARD_LIGHT,
                                                                                                     HUE,
                                                                                                     LIGHTEN,
                                                                                                     LUMINOSITY,
                                                                                                     MULTIPLY,
                                                                                                     NORMAL,
                                                                                                     CCSSValue.OVERLAY,
                                                                                                     SATURATION,
                                                                                                     SCREEN,
                                                                                                     SOFT_LIGHT));
  public static final ICSSProperty BACKGROUND_POSITION_X = new CSSPropertyList (ECSSProperty.BACKGROUND_POSITION_X,
                                                                                new CSSPropertyEnumOrNumbers (ECSSProperty.BACKGROUND_POSITION_X,
                                                                                                              true,
                                                                                                              1,
                                                                                                              2,
                                                                                                              CENTER,
                                                                                                              CCSSValue.LEFT,
                                                                                                              CCSSValue.RIGHT,
                                                                                                              X_END,
                                                                                                              X_START));
  public static final ICSSProperty BACKGROUND_POSITION_Y = new CSSPropertyList (ECSSProperty.BACKGROUND_POSITION_Y,
                                                                                new CSSPropertyEnumOrNumbers (ECSSProperty.BACKGROUND_POSITION_Y,
                                                                                                              true,
                                                                                                              1,
                                                                                                              2,
                                                                                                              CCSSValue.BOTTOM,
                                                                                                              CENTER,
                                                                                                              CCSSValue.TOP,
                                                                                                              Y_END,
                                                                                                              Y_START));
  public static final ICSSProperty BASELINE_SHIFT = new CSSPropertyEnumOrNumber (ECSSProperty.BASELINE_SHIFT,
                                                                                 true,
                                                                                 BASELINE,
                                                                                 SUB,
                                                                                 SUPER);
  public static final ICSSProperty BASELINE_SOURCE = new CSSPropertyEnum (ECSSProperty.BASELINE_SOURCE,
                                                                          AUTO,
                                                                          FIRST,
                                                                          LAST);
  public static final ICSSProperty BLOCK_SIZE = new CSSPropertyFree (ECSSProperty.BLOCK_SIZE);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty BOOKMARK_LABEL = new CSSPropertyFree (ECSSProperty.BOOKMARK_LABEL);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty BOOKMARK_LEVEL = new CSSPropertyFree (ECSSProperty.BOOKMARK_LEVEL);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty BOOKMARK_STATE = new CSSPropertyFree (ECSSProperty.BOOKMARK_STATE);
  public static final ICSSProperty BORDER_BLOCK = new CSSPropertyFree (ECSSProperty.BORDER_BLOCK);
  public static final ICSSProperty BORDER_BLOCK_COLOR = new CSSPropertyColors (ECSSProperty.BORDER_BLOCK_COLOR, 1, 2);
  public static final ICSSProperty BORDER_BLOCK_END = new CSSPropertyFree (ECSSProperty.BORDER_BLOCK_END);
  public static final ICSSProperty BORDER_BLOCK_END_COLOR = new CSSPropertyColor (ECSSProperty.BORDER_BLOCK_END_COLOR);
  public static final ICSSProperty BORDER_BLOCK_END_STYLE = new CSSPropertyEnum (ECSSProperty.BORDER_BLOCK_END_STYLE,
                                                                                 DASHED,
                                                                                 DOTTED,
                                                                                 DOUBLE,
                                                                                 GROOVE,
                                                                                 HIDDEN,
                                                                                 CCSSValue.INSET,
                                                                                 NONE,
                                                                                 OUTSET,
                                                                                 RIDGE,
                                                                                 SOLID);
  public static final ICSSProperty BORDER_BLOCK_END_WIDTH = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.BORDER_BLOCK_END_WIDTH,
                                                                                                 false,
                                                                                                 MEDIUM,
                                                                                                 THICK,
                                                                                                 THIN);
  public static final ICSSProperty BORDER_BLOCK_START = new CSSPropertyFree (ECSSProperty.BORDER_BLOCK_START);
  public static final ICSSProperty BORDER_BLOCK_START_COLOR = new CSSPropertyColor (ECSSProperty.BORDER_BLOCK_START_COLOR);
  public static final ICSSProperty BORDER_BLOCK_START_STYLE = new CSSPropertyEnum (ECSSProperty.BORDER_BLOCK_START_STYLE,
                                                                                   DASHED,
                                                                                   DOTTED,
                                                                                   DOUBLE,
                                                                                   GROOVE,
                                                                                   HIDDEN,
                                                                                   CCSSValue.INSET,
                                                                                   NONE,
                                                                                   OUTSET,
                                                                                   RIDGE,
                                                                                   SOLID);
  public static final ICSSProperty BORDER_BLOCK_START_WIDTH = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.BORDER_BLOCK_START_WIDTH,
                                                                                                   false,
                                                                                                   MEDIUM,
                                                                                                   THICK,
                                                                                                   THIN);
  public static final ICSSProperty BORDER_BLOCK_STYLE = new CSSPropertyEnums (ECSSProperty.BORDER_BLOCK_STYLE,
                                                                              1,
                                                                              2,
                                                                              DASHED,
                                                                              DOTTED,
                                                                              DOUBLE,
                                                                              GROOVE,
                                                                              HIDDEN,
                                                                              CCSSValue.INSET,
                                                                              NONE,
                                                                              OUTSET,
                                                                              RIDGE,
                                                                              SOLID);
  public static final ICSSProperty BORDER_BLOCK_WIDTH = new CSSPropertyEnumOrUnsignedNumbers (ECSSProperty.BORDER_BLOCK_WIDTH,
                                                                                              false,
                                                                                              1,
                                                                                              2,
                                                                                              MEDIUM,
                                                                                              THICK,
                                                                                              THIN);
  public static final ICSSProperty BORDER_END_END_RADIUS = new CSSPropertyUnsignedNumbers (ECSSProperty.BORDER_END_END_RADIUS,
                                                                                           true,
                                                                                           1,
                                                                                           2);
  public static final ICSSProperty BORDER_END_START_RADIUS = new CSSPropertyUnsignedNumbers (ECSSProperty.BORDER_END_START_RADIUS,
                                                                                             true,
                                                                                             1,
                                                                                             2);
  public static final ICSSProperty BORDER_IMAGE = new CSSPropertyFree (ECSSProperty.BORDER_IMAGE);
  public static final ICSSProperty BORDER_IMAGE_OUTSET = new CSSPropertyFree (ECSSProperty.BORDER_IMAGE_OUTSET);
  public static final ICSSProperty BORDER_IMAGE_REPEAT = new CSSPropertyEnums (ECSSProperty.BORDER_IMAGE_REPEAT,
                                                                               1,
                                                                               2,
                                                                               REPEAT,
                                                                               ROUND,
                                                                               SPACE,
                                                                               STRETCH);
  public static final ICSSProperty BORDER_IMAGE_SLICE = new CSSPropertyFree (ECSSProperty.BORDER_IMAGE_SLICE);
  public static final ICSSProperty BORDER_IMAGE_SOURCE = new CSSPropertyFree (ECSSProperty.BORDER_IMAGE_SOURCE);
  public static final ICSSProperty BORDER_IMAGE_WIDTH = new CSSPropertyFree (ECSSProperty.BORDER_IMAGE_WIDTH);
  public static final ICSSProperty BORDER_INLINE = new CSSPropertyFree (ECSSProperty.BORDER_INLINE);
  public static final ICSSProperty BORDER_INLINE_COLOR = new CSSPropertyColors (ECSSProperty.BORDER_INLINE_COLOR, 1, 2);
  public static final ICSSProperty BORDER_INLINE_END = new CSSPropertyFree (ECSSProperty.BORDER_INLINE_END);
  public static final ICSSProperty BORDER_INLINE_END_COLOR = new CSSPropertyColor (ECSSProperty.BORDER_INLINE_END_COLOR);
  public static final ICSSProperty BORDER_INLINE_END_STYLE = new CSSPropertyEnum (ECSSProperty.BORDER_INLINE_END_STYLE,
                                                                                  DASHED,
                                                                                  DOTTED,
                                                                                  DOUBLE,
                                                                                  GROOVE,
                                                                                  HIDDEN,
                                                                                  CCSSValue.INSET,
                                                                                  NONE,
                                                                                  OUTSET,
                                                                                  RIDGE,
                                                                                  SOLID);
  public static final ICSSProperty BORDER_INLINE_END_WIDTH = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.BORDER_INLINE_END_WIDTH,
                                                                                                  false,
                                                                                                  MEDIUM,
                                                                                                  THICK,
                                                                                                  THIN);
  public static final ICSSProperty BORDER_INLINE_START = new CSSPropertyFree (ECSSProperty.BORDER_INLINE_START);
  public static final ICSSProperty BORDER_INLINE_START_COLOR = new CSSPropertyColor (ECSSProperty.BORDER_INLINE_START_COLOR);
  public static final ICSSProperty BORDER_INLINE_START_STYLE = new CSSPropertyEnum (ECSSProperty.BORDER_INLINE_START_STYLE,
                                                                                    DASHED,
                                                                                    DOTTED,
                                                                                    DOUBLE,
                                                                                    GROOVE,
                                                                                    HIDDEN,
                                                                                    CCSSValue.INSET,
                                                                                    NONE,
                                                                                    OUTSET,
                                                                                    RIDGE,
                                                                                    SOLID);
  public static final ICSSProperty BORDER_INLINE_START_WIDTH = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.BORDER_INLINE_START_WIDTH,
                                                                                                    false,
                                                                                                    MEDIUM,
                                                                                                    THICK,
                                                                                                    THIN);
  public static final ICSSProperty BORDER_INLINE_STYLE = new CSSPropertyEnums (ECSSProperty.BORDER_INLINE_STYLE,
                                                                               1,
                                                                               2,
                                                                               DASHED,
                                                                               DOTTED,
                                                                               DOUBLE,
                                                                               GROOVE,
                                                                               HIDDEN,
                                                                               CCSSValue.INSET,
                                                                               NONE,
                                                                               OUTSET,
                                                                               RIDGE,
                                                                               SOLID);
  public static final ICSSProperty BORDER_INLINE_WIDTH = new CSSPropertyEnumOrUnsignedNumbers (ECSSProperty.BORDER_INLINE_WIDTH,
                                                                                               false,
                                                                                               1,
                                                                                               2,
                                                                                               MEDIUM,
                                                                                               THICK,
                                                                                               THIN);
  public static final ICSSProperty BORDER_SHAPE = new CSSPropertyFree (ECSSProperty.BORDER_SHAPE);
  public static final ICSSProperty BORDER_START_END_RADIUS = new CSSPropertyUnsignedNumbers (ECSSProperty.BORDER_START_END_RADIUS,
                                                                                             true,
                                                                                             1,
                                                                                             2);
  public static final ICSSProperty BORDER_START_START_RADIUS = new CSSPropertyUnsignedNumbers (ECSSProperty.BORDER_START_START_RADIUS,
                                                                                               true,
                                                                                               1,
                                                                                               2);
  public static final ICSSProperty BOX_DECORATION_BREAK = new CSSPropertyEnum (ECSSProperty.BOX_DECORATION_BREAK,
                                                                               CLONE,
                                                                               SLICE);
  public static final ICSSProperty BOX_SHADOW = new CSSPropertyFree (ECSSProperty.BOX_SHADOW);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty BOX_SNAP = new CSSPropertyFree (ECSSProperty.BOX_SNAP);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty BOX_SUPPRESS = new CSSPropertyFree (ECSSProperty.BOX_SUPPRESS);
  public static final ICSSProperty BREAK_AFTER = new CSSPropertyEnum (ECSSProperty.BREAK_AFTER,
                                                                      CCSSValue.ALL,
                                                                      ALWAYS,
                                                                      AUTO,
                                                                      AVOID,
                                                                      AVOID_COLUMN,
                                                                      AVOID_PAGE,
                                                                      AVOID_REGION,
                                                                      COLUMN,
                                                                      CCSSValue.LEFT,
                                                                      CCSSValue.PAGE,
                                                                      RECTO,
                                                                      REGION,
                                                                      CCSSValue.RIGHT,
                                                                      VERSO);
  public static final ICSSProperty BREAK_BEFORE = new CSSPropertyEnum (ECSSProperty.BREAK_BEFORE,
                                                                       CCSSValue.ALL,
                                                                       ALWAYS,
                                                                       AUTO,
                                                                       AVOID,
                                                                       AVOID_COLUMN,
                                                                       AVOID_PAGE,
                                                                       AVOID_REGION,
                                                                       COLUMN,
                                                                       CCSSValue.LEFT,
                                                                       CCSSValue.PAGE,
                                                                       RECTO,
                                                                       REGION,
                                                                       CCSSValue.RIGHT,
                                                                       VERSO);
  public static final ICSSProperty BREAK_INSIDE = new CSSPropertyEnum (ECSSProperty.BREAK_INSIDE,
                                                                       AUTO,
                                                                       AVOID,
                                                                       AVOID_COLUMN,
                                                                       AVOID_PAGE,
                                                                       AVOID_REGION);
  public static final ICSSProperty CARET = new CSSPropertyEnumOrColors (ECSSProperty.CARET,
                                                                        1,
                                                                        3,
                                                                        AUTO,
                                                                        BAR,
                                                                        BLOCK,
                                                                        MANUAL,
                                                                        UNDERSCORE);
  public static final ICSSProperty CARET_ANIMATION = new CSSPropertyEnum (ECSSProperty.CARET_ANIMATION, AUTO, MANUAL);
  public static final ICSSProperty CARET_SHAPE = new CSSPropertyEnum (ECSSProperty.CARET_SHAPE,
                                                                      AUTO,
                                                                      BAR,
                                                                      BLOCK,
                                                                      UNDERSCORE);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty CHAINS = new CSSPropertyFree (ECSSProperty.CHAINS);
  public static final ICSSProperty CLIP_PATH = new CSSPropertyFree (ECSSProperty.CLIP_PATH);
  public static final ICSSProperty CLIP_RULE = new CSSPropertyEnum (ECSSProperty.CLIP_RULE, EVENODD, NONZERO);
  public static final ICSSProperty COLOR_INTERPOLATION_FILTERS = new CSSPropertyEnum (ECSSProperty.COLOR_INTERPOLATION_FILTERS,
                                                                                      AUTO,
                                                                                      LINEARRGB,
                                                                                      SRGB);
  public static final ICSSProperty COLOR_SCHEME = new CSSPropertyFree (ECSSProperty.COLOR_SCHEME);
  public static final ICSSProperty COLUMNS = new CSSPropertyFree (ECSSProperty.COLUMNS);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty COLUMNS_SPAN = new CSSPropertyFree (ECSSProperty.COLUMNS_SPAN);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty COLUMNS_WIDTH = new CSSPropertyFree (ECSSProperty.COLUMNS_WIDTH);
  public static final ICSSProperty COLUMN_COUNT = new CSSPropertyEnumOrInt (ECSSProperty.COLUMN_COUNT, AUTO);
  public static final ICSSProperty COLUMN_FILL = new CSSPropertyEnum (ECSSProperty.COLUMN_FILL, AUTO, BALANCE);
  public static final ICSSProperty COLUMN_GAP = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.COLUMN_GAP,
                                                                                     true,
                                                                                     NORMAL,
                                                                                     THIN,
                                                                                     MEDIUM,
                                                                                     THICK);
  public static final ICSSProperty COLUMN_HEIGHT = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.COLUMN_HEIGHT,
                                                                                        false,
                                                                                        AUTO);
  public static final ICSSProperty COLUMN_RULE = new CSSPropertyFree (ECSSProperty.COLUMN_RULE);
  public static final ICSSProperty COLUMN_RULE_COLOR = new CSSPropertyFree (ECSSProperty.COLUMN_RULE_COLOR);
  public static final ICSSProperty COLUMN_RULE_STYLE = new CSSPropertyFree (ECSSProperty.COLUMN_RULE_STYLE);
  public static final ICSSProperty COLUMN_RULE_WIDTH = new CSSPropertyFree (ECSSProperty.COLUMN_RULE_WIDTH);
  public static final ICSSProperty COLUMN_SPAN = new CSSPropertyEnum (ECSSProperty.COLUMN_SPAN, CCSSValue.ALL, NONE);
  public static final ICSSProperty COLUMN_WIDTH = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.COLUMN_WIDTH,
                                                                                       false,
                                                                                       AUTO);
  public static final ICSSProperty COLUMN_WRAP = new CSSPropertyEnum (ECSSProperty.COLUMN_WRAP, AUTO, NOWRAP, WRAP);
  public static final ICSSProperty CONTAINER = new CSSPropertyFree (ECSSProperty.CONTAINER);
  public static final ICSSProperty CONTAINER_NAME = new CSSPropertyFree (ECSSProperty.CONTAINER_NAME);
  public static final ICSSProperty CONTAINER_TYPE = new CSSPropertyEnumCombination (ECSSProperty.CONTAINER_TYPE,
                                                                                    2,
                                                                                    new String [] { NORMAL, ANCHORED },
                                                                                    CCSSValue.INLINE_SIZE,
                                                                                    SCROLL_STATE,
                                                                                    CCSSValue.SIZE);
  public static final ICSSProperty CONTAIN_INTRINSIC_BLOCK_SIZE = new CSSPropertyEnumOrNumbers (ECSSProperty.CONTAIN_INTRINSIC_BLOCK_SIZE,
                                                                                                false,
                                                                                                1,
                                                                                                2,
                                                                                                AUTO,
                                                                                                NONE);
  public static final ICSSProperty CONTAIN_INTRINSIC_HEIGHT = new CSSPropertyEnumOrNumbers (ECSSProperty.CONTAIN_INTRINSIC_HEIGHT,
                                                                                            false,
                                                                                            1,
                                                                                            2,
                                                                                            AUTO,
                                                                                            NONE);
  public static final ICSSProperty CONTAIN_INTRINSIC_INLINE_SIZE = new CSSPropertyEnumOrNumbers (ECSSProperty.CONTAIN_INTRINSIC_INLINE_SIZE,
                                                                                                 false,
                                                                                                 1,
                                                                                                 2,
                                                                                                 AUTO,
                                                                                                 NONE);
  public static final ICSSProperty CONTAIN_INTRINSIC_SIZE = new CSSPropertyEnumOrNumbers (ECSSProperty.CONTAIN_INTRINSIC_SIZE,
                                                                                          false,
                                                                                          1,
                                                                                          4,
                                                                                          AUTO,
                                                                                          NONE);
  public static final ICSSProperty CONTAIN_INTRINSIC_WIDTH = new CSSPropertyEnumOrNumbers (ECSSProperty.CONTAIN_INTRINSIC_WIDTH,
                                                                                           false,
                                                                                           1,
                                                                                           2,
                                                                                           AUTO,
                                                                                           NONE);
  public static final ICSSProperty CONTENT = new CSSPropertyFree (ECSSProperty.CONTENT);
  public static final ICSSProperty CONTENT_VISIBILITY = new CSSPropertyEnum (ECSSProperty.CONTENT_VISIBILITY,
                                                                             AUTO,
                                                                             HIDDEN,
                                                                             VISIBLE);
  public static final ICSSProperty CORNER_BLOCK_END_SHAPE = new CSSPropertyFree (ECSSProperty.CORNER_BLOCK_END_SHAPE);
  public static final ICSSProperty CORNER_BLOCK_START_SHAPE = new CSSPropertyFree (ECSSProperty.CORNER_BLOCK_START_SHAPE);
  public static final ICSSProperty CORNER_BOTTOM_LEFT_SHAPE = new CSSPropertyFree (ECSSProperty.CORNER_BOTTOM_LEFT_SHAPE);
  public static final ICSSProperty CORNER_BOTTOM_RIGHT_SHAPE = new CSSPropertyFree (ECSSProperty.CORNER_BOTTOM_RIGHT_SHAPE);
  public static final ICSSProperty CORNER_BOTTOM_SHAPE = new CSSPropertyFree (ECSSProperty.CORNER_BOTTOM_SHAPE);
  public static final ICSSProperty CORNER_END_END_SHAPE = new CSSPropertyFree (ECSSProperty.CORNER_END_END_SHAPE);
  public static final ICSSProperty CORNER_END_START_SHAPE = new CSSPropertyFree (ECSSProperty.CORNER_END_START_SHAPE);
  public static final ICSSProperty CORNER_INLINE_END_SHAPE = new CSSPropertyFree (ECSSProperty.CORNER_INLINE_END_SHAPE);
  public static final ICSSProperty CORNER_INLINE_START_SHAPE = new CSSPropertyFree (ECSSProperty.CORNER_INLINE_START_SHAPE);
  public static final ICSSProperty CORNER_LEFT_SHAPE = new CSSPropertyFree (ECSSProperty.CORNER_LEFT_SHAPE);
  public static final ICSSProperty CORNER_RIGHT_SHAPE = new CSSPropertyFree (ECSSProperty.CORNER_RIGHT_SHAPE);
  public static final ICSSProperty CORNER_SHAPE = new CSSPropertyFree (ECSSProperty.CORNER_SHAPE);
  public static final ICSSProperty CORNER_START_END_SHAPE = new CSSPropertyFree (ECSSProperty.CORNER_START_END_SHAPE);
  public static final ICSSProperty CORNER_START_START_SHAPE = new CSSPropertyFree (ECSSProperty.CORNER_START_START_SHAPE);
  public static final ICSSProperty CORNER_TOP_LEFT_SHAPE = new CSSPropertyFree (ECSSProperty.CORNER_TOP_LEFT_SHAPE);
  public static final ICSSProperty CORNER_TOP_RIGHT_SHAPE = new CSSPropertyFree (ECSSProperty.CORNER_TOP_RIGHT_SHAPE);
  public static final ICSSProperty CORNER_TOP_SHAPE = new CSSPropertyFree (ECSSProperty.CORNER_TOP_SHAPE);
  public static final ICSSProperty COUNTER_INCREMENT = new CSSPropertyFree (ECSSProperty.COUNTER_INCREMENT);
  public static final ICSSProperty COUNTER_RESET = new CSSPropertyFree (ECSSProperty.COUNTER_RESET);
  public static final ICSSProperty COUNTER_SET = new CSSPropertyFree (ECSSProperty.COUNTER_SET);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty CUE = new CSSPropertyFree (ECSSProperty.CUE);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty CUE_AFTER = new CSSPropertyFree (ECSSProperty.CUE_AFTER);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty CUE_BEFORE = new CSSPropertyFree (ECSSProperty.CUE_BEFORE);
  public static final ICSSProperty CX = new CSSPropertyNumber (ECSSProperty.CX, true);
  public static final ICSSProperty CY = new CSSPropertyNumber (ECSSProperty.CY, true);
  public static final ICSSProperty D = new CSSPropertyFree (ECSSProperty.D);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty DISPLAY_INSIDE = new CSSPropertyFree (ECSSProperty.DISPLAY_INSIDE);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty DISPLAY_LIST = new CSSPropertyFree (ECSSProperty.DISPLAY_LIST);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty DISPLAY_OUTSIDE = new CSSPropertyFree (ECSSProperty.DISPLAY_OUTSIDE);
  public static final ICSSProperty DOMINANT_BASELINE = new CSSPropertyEnum (ECSSProperty.DOMINANT_BASELINE,
                                                                            ALPHABETIC,
                                                                            AUTO,
                                                                            CENTRAL,
                                                                            HANGING,
                                                                            IDEOGRAPHIC,
                                                                            MATHEMATICAL,
                                                                            MIDDLE,
                                                                            TEXT_BOTTOM,
                                                                            TEXT_TOP);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty DROP_INITIAL_AFTER_ADJUST = new CSSPropertyFree (ECSSProperty.DROP_INITIAL_AFTER_ADJUST);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty DROP_INITIAL_AFTER_ALIGN = new CSSPropertyFree (ECSSProperty.DROP_INITIAL_AFTER_ALIGN);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty DROP_INITIAL_BEFORE_ADJUST = new CSSPropertyFree (ECSSProperty.DROP_INITIAL_BEFORE_ADJUST);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty DROP_INITIAL_BEFORE_ALIGN = new CSSPropertyFree (ECSSProperty.DROP_INITIAL_BEFORE_ALIGN);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty DROP_INITIAL_SIZE = new CSSPropertyFree (ECSSProperty.DROP_INITIAL_SIZE);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty DROP_INITIAL_VALUE = new CSSPropertyFree (ECSSProperty.DROP_INITIAL_VALUE);
  public static final ICSSProperty DYNAMIC_RANGE_LIMIT = new CSSPropertyFree (ECSSProperty.DYNAMIC_RANGE_LIMIT);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty ELEVATION = new CSSPropertyFree (ECSSProperty.ELEVATION);
  public static final ICSSProperty FIELD_SIZING = new CSSPropertyEnum (ECSSProperty.FIELD_SIZING,
                                                                       CCSSValue.CONTENT,
                                                                       FIXED);
  public static final ICSSProperty FILL = new CSSPropertyFree (ECSSProperty.FILL);
  public static final ICSSProperty FILL_OPACITY = new CSSPropertyDoubleOrPercentage (ECSSProperty.FILL_OPACITY);
  public static final ICSSProperty FILL_RULE = new CSSPropertyEnum (ECSSProperty.FILL_RULE, EVENODD, NONZERO);
  public static final ICSSProperty FILTER = new CSSPropertyFree (ECSSProperty.FILTER);
  public static final ICSSProperty FLEX = new CSSPropertyFree (ECSSProperty.FLEX);
  public static final ICSSProperty FLEX_BASIS = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.FLEX_BASIS,
                                                                                     true,
                                                                                     ArrayHelper.getConcatenated (CCSSValue.CONTENT, ArrayHelper.getConcatenated (AUTO, ARRAY_SIZE)));
  public static final ICSSProperty FLEX_DIRECTION = new CSSPropertyEnum (ECSSProperty.FLEX_DIRECTION,
                                                                         COLUMN,
                                                                         COLUMN_REVERSE,
                                                                         ROW,
                                                                         ROW_REVERSE);
  public static final ICSSProperty FLEX_FLOW = new CSSPropertyEnumCombination (ECSSProperty.FLEX_FLOW,
                                                                               3,
                                                                               new String [0],
                                                                               BALANCE,
                                                                               COLUMN,
                                                                               COLUMN_REVERSE,
                                                                               NOWRAP,
                                                                               ROW,
                                                                               ROW_REVERSE,
                                                                               WRAP,
                                                                               WRAP_REVERSE);
  public static final ICSSProperty FLEX_GROW = new CSSPropertyUnsignedDouble (ECSSProperty.FLEX_GROW);
  public static final ICSSProperty FLEX_LINE_COUNT = new CSSPropertyLong (ECSSProperty.FLEX_LINE_COUNT);
  public static final ICSSProperty FLEX_SHRINK = new CSSPropertyUnsignedDouble (ECSSProperty.FLEX_SHRINK);
  public static final ICSSProperty FLEX_WRAP = new CSSPropertyEnumCombination (ECSSProperty.FLEX_WRAP,
                                                                               2,
                                                                               new String [] { NOWRAP },
                                                                               BALANCE,
                                                                               WRAP,
                                                                               WRAP_REVERSE);
  public static final ICSSProperty FLOOD_COLOR = new CSSPropertyColor (ECSSProperty.FLOOD_COLOR);
  public static final ICSSProperty FLOOD_OPACITY = new CSSPropertyDoubleOrPercentage (ECSSProperty.FLOOD_OPACITY);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty FLOW_FROM = new CSSPropertyFree (ECSSProperty.FLOW_FROM);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty FLOW_INTO = new CSSPropertyFree (ECSSProperty.FLOW_INTO);
  public static final ICSSProperty FONT = new CSSPropertyFree (ECSSProperty.FONT);
  public static final ICSSProperty FONT_FEATURE_SETTINGS = new CSSPropertyFree (ECSSProperty.FONT_FEATURE_SETTINGS);
  public static final ICSSProperty FONT_KERNING = new CSSPropertyEnum (ECSSProperty.FONT_KERNING, AUTO, NONE, NORMAL);
  public static final ICSSProperty FONT_LANGUAGE_OVERRIDE = new CSSPropertyFree (ECSSProperty.FONT_LANGUAGE_OVERRIDE);
  public static final ICSSProperty FONT_OPTICAL_SIZING = new CSSPropertyEnum (ECSSProperty.FONT_OPTICAL_SIZING,
                                                                              AUTO,
                                                                              NONE);
  public static final ICSSProperty FONT_PALETTE = new CSSPropertyFree (ECSSProperty.FONT_PALETTE);
  public static final ICSSProperty FONT_SIZE_ADJUST = new CSSPropertyFree (ECSSProperty.FONT_SIZE_ADJUST);
  public static final ICSSProperty FONT_STRETCH = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.FONT_STRETCH,
                                                                                       true,
                                                                                       CONDENSED,
                                                                                       EXPANDED,
                                                                                       EXTRA_CONDENSED,
                                                                                       EXTRA_EXPANDED,
                                                                                       NORMAL,
                                                                                       SEMI_CONDENSED,
                                                                                       SEMI_EXPANDED,
                                                                                       ULTRA_CONDENSED,
                                                                                       ULTRA_EXPANDED);
  public static final ICSSProperty FONT_SYNTHESIS = new CSSPropertyEnumCombination (ECSSProperty.FONT_SYNTHESIS,
                                                                                    4,
                                                                                    new String [] { NONE },
                                                                                    CCSSValue.POSITION,
                                                                                    SMALL_CAPS,
                                                                                    STYLE,
                                                                                    WEIGHT);
  public static final ICSSProperty FONT_SYNTHESIS_POSITION = new CSSPropertyEnum (ECSSProperty.FONT_SYNTHESIS_POSITION,
                                                                                  AUTO,
                                                                                  NONE);
  public static final ICSSProperty FONT_SYNTHESIS_SMALL_CAPS = new CSSPropertyEnum (ECSSProperty.FONT_SYNTHESIS_SMALL_CAPS,
                                                                                    AUTO,
                                                                                    NONE);
  public static final ICSSProperty FONT_SYNTHESIS_STYLE = new CSSPropertyEnum (ECSSProperty.FONT_SYNTHESIS_STYLE,
                                                                               AUTO,
                                                                               NONE,
                                                                               OBLIQUE_ONLY);
  public static final ICSSProperty FONT_SYNTHESIS_WEIGHT = new CSSPropertyEnum (ECSSProperty.FONT_SYNTHESIS_WEIGHT,
                                                                                AUTO,
                                                                                NONE);
  public static final ICSSProperty FONT_VARIANT_ALTERNATES = new CSSPropertyFree (ECSSProperty.FONT_VARIANT_ALTERNATES);
  public static final ICSSProperty FONT_VARIANT_CAPS = new CSSPropertyEnum (ECSSProperty.FONT_VARIANT_CAPS,
                                                                            ALL_PETITE_CAPS,
                                                                            ALL_SMALL_CAPS,
                                                                            NORMAL,
                                                                            PETITE_CAPS,
                                                                            SMALL_CAPS,
                                                                            TITLING_CAPS,
                                                                            UNICASE);
  public static final ICSSProperty FONT_VARIANT_EAST_ASIAN = new CSSPropertyEnumCombination (ECSSProperty.FONT_VARIANT_EAST_ASIAN,
                                                                                             3,
                                                                                             new String [] { NORMAL },
                                                                                             FULL_WIDTH,
                                                                                             JIS04,
                                                                                             JIS78,
                                                                                             JIS83,
                                                                                             JIS90,
                                                                                             PROPORTIONAL_WIDTH,
                                                                                             RUBY,
                                                                                             SIMPLIFIED,
                                                                                             TRADITIONAL);
  public static final ICSSProperty FONT_VARIANT_EMOJI = new CSSPropertyEnum (ECSSProperty.FONT_VARIANT_EMOJI,
                                                                             EMOJI,
                                                                             NORMAL,
                                                                             TEXT,
                                                                             UNICODE);
  public static final ICSSProperty FONT_VARIANT_LIGATURES = new CSSPropertyEnumCombination (ECSSProperty.FONT_VARIANT_LIGATURES,
                                                                                            4,
                                                                                            new String [] { NONE, NORMAL },
                                                                                            COMMON_LIGATURES,
                                                                                            CONTEXTUAL,
                                                                                            DISCRETIONARY_LIGATURES,
                                                                                            HISTORICAL_LIGATURES,
                                                                                            NO_COMMON_LIGATURES,
                                                                                            NO_CONTEXTUAL,
                                                                                            NO_DISCRETIONARY_LIGATURES,
                                                                                            NO_HISTORICAL_LIGATURES);
  public static final ICSSProperty FONT_VARIANT_NUMERIC = new CSSPropertyEnumCombination (ECSSProperty.FONT_VARIANT_NUMERIC,
                                                                                          5,
                                                                                          new String [] { NORMAL },
                                                                                          DIAGONAL_FRACTIONS,
                                                                                          LINING_NUMS,
                                                                                          OLDSTYLE_NUMS,
                                                                                          ORDINAL,
                                                                                          PROPORTIONAL_NUMS,
                                                                                          SLASHED_ZERO,
                                                                                          STACKED_FRACTIONS,
                                                                                          TABULAR_NUMS);
  public static final ICSSProperty FONT_VARIANT_POSITION = new CSSPropertyEnum (ECSSProperty.FONT_VARIANT_POSITION,
                                                                                NORMAL,
                                                                                SUB,
                                                                                SUPER);
  public static final ICSSProperty FONT_VARIATION_SETTINGS = new CSSPropertyFree (ECSSProperty.FONT_VARIATION_SETTINGS);
  public static final ICSSProperty FONT_WIDTH = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.FONT_WIDTH,
                                                                                     true,
                                                                                     CONDENSED,
                                                                                     EXPANDED,
                                                                                     EXTRA_CONDENSED,
                                                                                     EXTRA_EXPANDED,
                                                                                     NORMAL,
                                                                                     SEMI_CONDENSED,
                                                                                     SEMI_EXPANDED,
                                                                                     ULTRA_CONDENSED,
                                                                                     ULTRA_EXPANDED);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty FOOTNOTE_DISPLAY = new CSSPropertyFree (ECSSProperty.FOOTNOTE_DISPLAY);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty FOOTNOTE_POLICY = new CSSPropertyFree (ECSSProperty.FOOTNOTE_POLICY);
  public static final ICSSProperty FORCED_COLOR_ADJUST = new CSSPropertyEnum (ECSSProperty.FORCED_COLOR_ADJUST,
                                                                              AUTO,
                                                                              NONE,
                                                                              PRESERVE_PARENT_COLOR);
  public static final ICSSProperty FRAME_SIZING = new CSSPropertyEnum (ECSSProperty.FRAME_SIZING,
                                                                       AUTO,
                                                                       CONTENT_BLOCK_SIZE,
                                                                       CONTENT_HEIGHT,
                                                                       CONTENT_INLINE_SIZE,
                                                                       CONTENT_WIDTH);
  public static final ICSSProperty GAP = new CSSPropertyEnumOrUnsignedNumbers (ECSSProperty.GAP,
                                                                               true,
                                                                               1,
                                                                               2,
                                                                               NORMAL,
                                                                               THIN,
                                                                               MEDIUM,
                                                                               THICK);
  public static final ICSSProperty GRID = new CSSPropertyFree (ECSSProperty.GRID);
  public static final ICSSProperty GRID_AREA = new CSSPropertyFree (ECSSProperty.GRID_AREA);
  public static final ICSSProperty GRID_AUTO_COLUMNS = new CSSPropertyFree (ECSSProperty.GRID_AUTO_COLUMNS);
  public static final ICSSProperty GRID_AUTO_FLOW = new CSSPropertyEnumCombination (ECSSProperty.GRID_AUTO_FLOW,
                                                                                    2,
                                                                                    new String [0],
                                                                                    COLUMN,
                                                                                    DENSE,
                                                                                    ROW);
  public static final ICSSProperty GRID_AUTO_ROWS = new CSSPropertyFree (ECSSProperty.GRID_AUTO_ROWS);
  public static final ICSSProperty GRID_COLUMN = new CSSPropertyFree (ECSSProperty.GRID_COLUMN);
  public static final ICSSProperty GRID_COLUMN_END = new CSSPropertyFree (ECSSProperty.GRID_COLUMN_END);
  public static final ICSSProperty GRID_COLUMN_START = new CSSPropertyFree (ECSSProperty.GRID_COLUMN_START);
  public static final ICSSProperty GRID_ROW = new CSSPropertyFree (ECSSProperty.GRID_ROW);
  public static final ICSSProperty GRID_ROW_END = new CSSPropertyFree (ECSSProperty.GRID_ROW_END);
  public static final ICSSProperty GRID_ROW_START = new CSSPropertyFree (ECSSProperty.GRID_ROW_START);
  public static final ICSSProperty GRID_TEMPLATE = new CSSPropertyFree (ECSSProperty.GRID_TEMPLATE);
  public static final ICSSProperty GRID_TEMPLATE_AREAS = new CSSPropertyFree (ECSSProperty.GRID_TEMPLATE_AREAS);
  public static final ICSSProperty GRID_TEMPLATE_COLUMNS = new CSSPropertyFree (ECSSProperty.GRID_TEMPLATE_COLUMNS);
  public static final ICSSProperty GRID_TEMPLATE_ROWS = new CSSPropertyFree (ECSSProperty.GRID_TEMPLATE_ROWS);
  public static final ICSSProperty HANGING_PUNCTUATION = new CSSPropertyEnumCombination (ECSSProperty.HANGING_PUNCTUATION,
                                                                                         3,
                                                                                         new String [] { NONE },
                                                                                         ALLOW_END,
                                                                                         FIRST,
                                                                                         FORCE_END,
                                                                                         LAST);
  public static final ICSSProperty HYPHENATE_CHARACTER = new CSSPropertyFree (ECSSProperty.HYPHENATE_CHARACTER);
  public static final ICSSProperty HYPHENATE_LIMIT_CHARS = new CSSPropertyFree (ECSSProperty.HYPHENATE_LIMIT_CHARS);
  public static final ICSSProperty HYPHENS = new CSSPropertyEnum (ECSSProperty.HYPHENS, AUTO, MANUAL, NONE);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty ICON = new CSSPropertyFree (ECSSProperty.ICON);
  public static final ICSSProperty IMAGE_ORIENTATION = new CSSPropertyEnumOrNumbers (ECSSProperty.IMAGE_ORIENTATION,
                                                                                     false,
                                                                                     1,
                                                                                     2,
                                                                                     FLIP,
                                                                                     FROM_IMAGE,
                                                                                     NONE);
  public static final ICSSProperty IMAGE_RENDERING = new CSSPropertyEnum (ECSSProperty.IMAGE_RENDERING,
                                                                          AUTO,
                                                                          CRISP_EDGES,
                                                                          PIXELATED,
                                                                          SMOOTH);
  public static final ICSSProperty IMAGE_RESOLUTION = new CSSPropertyFree (ECSSProperty.IMAGE_RESOLUTION);
  public static final ICSSProperty IME_MODE = new CSSPropertyEnum (ECSSProperty.IME_MODE,
                                                                   ACTIVE,
                                                                   AUTO,
                                                                   DISABLED,
                                                                   INACTIVE,
                                                                   NORMAL);
  public static final ICSSProperty INITIAL_LETTER = new CSSPropertyFree (ECSSProperty.INITIAL_LETTER);
  public static final ICSSProperty INITIAL_LETTER_ALIGN = new CSSPropertyEnum (ECSSProperty.INITIAL_LETTER_ALIGN,
                                                                               ALPHABETIC,
                                                                               AUTO,
                                                                               HANGING,
                                                                               IDEOGRAPHIC);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty INLINE_BOX_ALIGN = new CSSPropertyFree (ECSSProperty.INLINE_BOX_ALIGN);
  public static final ICSSProperty INLINE_SIZE = new CSSPropertyFree (ECSSProperty.INLINE_SIZE);
  public static final ICSSProperty INSET = new CSSPropertyFree (ECSSProperty.INSET);
  public static final ICSSProperty INSET_BLOCK = new CSSPropertyFree (ECSSProperty.INSET_BLOCK);
  public static final ICSSProperty INSET_BLOCK_END = new CSSPropertyFree (ECSSProperty.INSET_BLOCK_END);
  public static final ICSSProperty INSET_BLOCK_START = new CSSPropertyFree (ECSSProperty.INSET_BLOCK_START);
  public static final ICSSProperty INSET_INLINE = new CSSPropertyFree (ECSSProperty.INSET_INLINE);
  public static final ICSSProperty INSET_INLINE_END = new CSSPropertyFree (ECSSProperty.INSET_INLINE_END);
  public static final ICSSProperty INSET_INLINE_START = new CSSPropertyFree (ECSSProperty.INSET_INLINE_START);
  public static final ICSSProperty INTERACTIVITY = new CSSPropertyEnum (ECSSProperty.INTERACTIVITY, AUTO, INERT);
  public static final ICSSProperty INTEREST_DELAY = new CSSPropertyEnumOrNumbers (ECSSProperty.INTEREST_DELAY,
                                                                                  false,
                                                                                  1,
                                                                                  2,
                                                                                  NORMAL);
  public static final ICSSProperty INTEREST_DELAY_END = new CSSPropertyEnumOrNumber (ECSSProperty.INTEREST_DELAY_END,
                                                                                     false,
                                                                                     NORMAL);
  public static final ICSSProperty INTEREST_DELAY_START = new CSSPropertyEnumOrNumber (ECSSProperty.INTEREST_DELAY_START,
                                                                                       false,
                                                                                       NORMAL);
  public static final ICSSProperty INTERPOLATE_SIZE = new CSSPropertyEnum (ECSSProperty.INTERPOLATE_SIZE,
                                                                           ALLOW_KEYWORDS,
                                                                           NUMERIC_ONLY);
  public static final ICSSProperty ISOLATION = new CSSPropertyEnum (ECSSProperty.ISOLATION, AUTO, ISOLATE);
  public static final ICSSProperty JUSTIFY_CONTENT = new CSSPropertyEnumCombination (ECSSProperty.JUSTIFY_CONTENT,
                                                                                     2,
                                                                                     new String [] { NORMAL, SPACE_AROUND, SPACE_BETWEEN, SPACE_EVENLY, STRETCH },
                                                                                     CENTER,
                                                                                     END,
                                                                                     FLEX_END,
                                                                                     FLEX_START,
                                                                                     CCSSValue.LEFT,
                                                                                     CCSSValue.RIGHT,
                                                                                     SAFE,
                                                                                     START,
                                                                                     UNSAFE);
  public static final ICSSProperty JUSTIFY_ITEMS = new CSSPropertyEnumCombination (ECSSProperty.JUSTIFY_ITEMS,
                                                                                   2,
                                                                                   new String [] { ANCHOR_CENTER, NORMAL, STRETCH },
                                                                                   BASELINE,
                                                                                   CENTER,
                                                                                   END,
                                                                                   FIRST,
                                                                                   FLEX_END,
                                                                                   FLEX_START,
                                                                                   LAST,
                                                                                   CCSSValue.LEFT,
                                                                                   LEGACY,
                                                                                   CCSSValue.RIGHT,
                                                                                   SAFE,
                                                                                   SELF_END,
                                                                                   SELF_START,
                                                                                   START,
                                                                                   UNSAFE);
  public static final ICSSProperty JUSTIFY_SELF = new CSSPropertyEnumCombination (ECSSProperty.JUSTIFY_SELF,
                                                                                  2,
                                                                                  new String [] { ANCHOR_CENTER, AUTO, NORMAL, STRETCH },
                                                                                  BASELINE,
                                                                                  CENTER,
                                                                                  END,
                                                                                  FIRST,
                                                                                  FLEX_END,
                                                                                  FLEX_START,
                                                                                  LAST,
                                                                                  CCSSValue.LEFT,
                                                                                  CCSSValue.RIGHT,
                                                                                  SAFE,
                                                                                  SELF_END,
                                                                                  SELF_START,
                                                                                  START,
                                                                                  UNSAFE);
  public static final ICSSProperty LIGHTING_COLOR = new CSSPropertyColor (ECSSProperty.LIGHTING_COLOR);
  public static final ICSSProperty LINE_BREAK = new CSSPropertyEnum (ECSSProperty.LINE_BREAK,
                                                                     ANYWHERE,
                                                                     AUTO,
                                                                     LOOSE,
                                                                     NORMAL,
                                                                     STRICT);
  public static final ICSSProperty LINE_CLAMP = new CSSPropertyFree (ECSSProperty.LINE_CLAMP);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty LINE_GRID = new CSSPropertyFree (ECSSProperty.LINE_GRID);
  public static final ICSSProperty LINE_HEIGHT_STEP = new CSSPropertyNumber (ECSSProperty.LINE_HEIGHT_STEP, false);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty LINE_SNAP = new CSSPropertyFree (ECSSProperty.LINE_SNAP);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty LINE_STACKING = new CSSPropertyFree (ECSSProperty.LINE_STACKING);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty LINE_STACKING_RUBY = new CSSPropertyFree (ECSSProperty.LINE_STACKING_RUBY);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty LINE_STACKING_SHIFT = new CSSPropertyFree (ECSSProperty.LINE_STACKING_SHIFT);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty LINE_STACKING_STRATEGY = new CSSPropertyFree (ECSSProperty.LINE_STACKING_STRATEGY);
  public static final ICSSProperty LINK_PARAMETERS = new CSSPropertyFree (ECSSProperty.LINK_PARAMETERS);
  public static final ICSSProperty LIST_STYLE = new CSSPropertyFree (ECSSProperty.LIST_STYLE);
  public static final ICSSProperty MARGIN_BLOCK = new CSSPropertyFree (ECSSProperty.MARGIN_BLOCK);
  public static final ICSSProperty MARGIN_BLOCK_END = new CSSPropertyFree (ECSSProperty.MARGIN_BLOCK_END);
  public static final ICSSProperty MARGIN_BLOCK_START = new CSSPropertyFree (ECSSProperty.MARGIN_BLOCK_START);
  public static final ICSSProperty MARGIN_INLINE = new CSSPropertyFree (ECSSProperty.MARGIN_INLINE);
  public static final ICSSProperty MARGIN_INLINE_END = new CSSPropertyFree (ECSSProperty.MARGIN_INLINE_END);
  public static final ICSSProperty MARGIN_INLINE_START = new CSSPropertyFree (ECSSProperty.MARGIN_INLINE_START);
  public static final ICSSProperty MARGIN_TRIM = new CSSPropertyFree (ECSSProperty.MARGIN_TRIM);
  public static final ICSSProperty MARKER = new CSSPropertyEnumOrURL (ECSSProperty.MARKER, NONE);
  public static final ICSSProperty MARKER_END = new CSSPropertyEnumOrURL (ECSSProperty.MARKER_END, NONE);
  public static final ICSSProperty MARKER_MID = new CSSPropertyEnumOrURL (ECSSProperty.MARKER_MID, NONE);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty MARKER_SIDE = new CSSPropertyFree (ECSSProperty.MARKER_SIDE);
  public static final ICSSProperty MARKER_START = new CSSPropertyEnumOrURL (ECSSProperty.MARKER_START, NONE);
  public static final ICSSProperty MASK = new CSSPropertyFree (ECSSProperty.MASK);
  public static final ICSSProperty MASK_BORDER = new CSSPropertyFree (ECSSProperty.MASK_BORDER);
  public static final ICSSProperty MASK_BORDER_MODE = new CSSPropertyEnum (ECSSProperty.MASK_BORDER_MODE,
                                                                           ALPHA,
                                                                           LUMINANCE);
  public static final ICSSProperty MASK_BORDER_OUTSET = new CSSPropertyFree (ECSSProperty.MASK_BORDER_OUTSET);
  public static final ICSSProperty MASK_BORDER_REPEAT = new CSSPropertyEnums (ECSSProperty.MASK_BORDER_REPEAT,
                                                                              1,
                                                                              2,
                                                                              REPEAT,
                                                                              ROUND,
                                                                              SPACE,
                                                                              STRETCH);
  public static final ICSSProperty MASK_BORDER_SLICE = new CSSPropertyFree (ECSSProperty.MASK_BORDER_SLICE);
  public static final ICSSProperty MASK_BORDER_SOURCE = new CSSPropertyFree (ECSSProperty.MASK_BORDER_SOURCE);
  public static final ICSSProperty MASK_BORDER_WIDTH = new CSSPropertyFree (ECSSProperty.MASK_BORDER_WIDTH);
  public static final ICSSProperty MASK_CLIP = new CSSPropertyList (ECSSProperty.MASK_CLIP,
                                                                    new CSSPropertyEnum (ECSSProperty.MASK_CLIP,
                                                                                         BORDER_BOX,
                                                                                         CONTENT_BOX,
                                                                                         FILL_BOX,
                                                                                         NO_CLIP,
                                                                                         PADDING_BOX,
                                                                                         STROKE_BOX,
                                                                                         VIEW_BOX));
  public static final ICSSProperty MASK_COMPOSITE = new CSSPropertyList (ECSSProperty.MASK_COMPOSITE,
                                                                         new CSSPropertyEnum (ECSSProperty.MASK_COMPOSITE,
                                                                                              ADD,
                                                                                              EXCLUDE,
                                                                                              INTERSECT,
                                                                                              SUBTRACT));
  public static final ICSSProperty MASK_IMAGE = new CSSPropertyFree (ECSSProperty.MASK_IMAGE);
  public static final ICSSProperty MASK_MODE = new CSSPropertyList (ECSSProperty.MASK_MODE,
                                                                    new CSSPropertyEnum (ECSSProperty.MASK_MODE,
                                                                                         ALPHA,
                                                                                         LUMINANCE,
                                                                                         MATCH_SOURCE));
  public static final ICSSProperty MASK_ORIGIN = new CSSPropertyList (ECSSProperty.MASK_ORIGIN,
                                                                      new CSSPropertyEnum (ECSSProperty.MASK_ORIGIN,
                                                                                           BORDER_BOX,
                                                                                           CONTENT_BOX,
                                                                                           FILL_BOX,
                                                                                           PADDING_BOX,
                                                                                           STROKE_BOX,
                                                                                           VIEW_BOX));
  public static final ICSSProperty MASK_POSITION = new CSSPropertyList (ECSSProperty.MASK_POSITION,
                                                                        new CSSPropertyEnumOrNumbers (ECSSProperty.MASK_POSITION,
                                                                                                      true,
                                                                                                      1,
                                                                                                      4,
                                                                                                      CCSSValue.BOTTOM,
                                                                                                      CENTER,
                                                                                                      CCSSValue.LEFT,
                                                                                                      CCSSValue.RIGHT,
                                                                                                      CCSSValue.TOP));
  public static final ICSSProperty MASK_REPEAT = new CSSPropertyList (ECSSProperty.MASK_REPEAT,
                                                                      new CSSPropertyEnums (ECSSProperty.MASK_REPEAT,
                                                                                            1,
                                                                                            2,
                                                                                            NO_REPEAT,
                                                                                            REPEAT,
                                                                                            REPEAT_X,
                                                                                            REPEAT_Y,
                                                                                            ROUND,
                                                                                            SPACE));
  public static final ICSSProperty MASK_SIZE = new CSSPropertyList (ECSSProperty.MASK_SIZE,
                                                                    new CSSPropertyEnumOrUnsignedNumbers (ECSSProperty.MASK_SIZE,
                                                                                                          true,
                                                                                                          1,
                                                                                                          2,
                                                                                                          AUTO,
                                                                                                          CCSSValue.CONTAIN,
                                                                                                          COVER));
  public static final ICSSProperty MASK_TYPE = new CSSPropertyEnum (ECSSProperty.MASK_TYPE, ALPHA, LUMINANCE);
  public static final ICSSProperty MATH_DEPTH = new CSSPropertyFree (ECSSProperty.MATH_DEPTH);
  public static final ICSSProperty MATH_SHIFT = new CSSPropertyEnum (ECSSProperty.MATH_SHIFT, COMPACT, NORMAL);
  public static final ICSSProperty MATH_STYLE = new CSSPropertyEnum (ECSSProperty.MATH_STYLE, COMPACT, NORMAL);
  public static final ICSSProperty MAX_BLOCK_SIZE = new CSSPropertyFree (ECSSProperty.MAX_BLOCK_SIZE);
  public static final ICSSProperty MAX_INLINE_SIZE = new CSSPropertyFree (ECSSProperty.MAX_INLINE_SIZE);
  public static final ICSSProperty MAX_LINES = new CSSPropertyEnumOrInt (ECSSProperty.MAX_LINES, NONE);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty MAX_ZOOM = new CSSPropertyFree (ECSSProperty.MAX_ZOOM);
  public static final ICSSProperty MIN_BLOCK_SIZE = new CSSPropertyFree (ECSSProperty.MIN_BLOCK_SIZE);
  public static final ICSSProperty MIN_INLINE_SIZE = new CSSPropertyFree (ECSSProperty.MIN_INLINE_SIZE);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty MIN_ZOOM = new CSSPropertyFree (ECSSProperty.MIN_ZOOM);
  public static final ICSSProperty MIX_BLEND_MODE = new CSSPropertyEnum (ECSSProperty.MIX_BLEND_MODE,
                                                                         CCSSValue.COLOR,
                                                                         COLOR_BURN,
                                                                         COLOR_DODGE,
                                                                         DARKEN,
                                                                         DIFFERENCE,
                                                                         EXCLUSION,
                                                                         HARD_LIGHT,
                                                                         HUE,
                                                                         LIGHTEN,
                                                                         LUMINOSITY,
                                                                         MULTIPLY,
                                                                         NORMAL,
                                                                         CCSSValue.OVERLAY,
                                                                         PLUS_DARKER,
                                                                         PLUS_LIGHTER,
                                                                         SATURATION,
                                                                         SCREEN,
                                                                         SOFT_LIGHT);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty NAV_DOWN = new CSSPropertyFree (ECSSProperty.NAV_DOWN);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty NAV_INDEX = new CSSPropertyFree (ECSSProperty.NAV_INDEX);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty NAV_LEFT = new CSSPropertyFree (ECSSProperty.NAV_LEFT);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty NAV_RIGHT = new CSSPropertyFree (ECSSProperty.NAV_RIGHT);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty NAV_UP = new CSSPropertyFree (ECSSProperty.NAV_UP);
  public static final ICSSProperty OBJECT_FIT = new CSSPropertyEnum (ECSSProperty.OBJECT_FIT,
                                                                     CCSSValue.CONTAIN,
                                                                     COVER,
                                                                     CCSSValue.FILL,
                                                                     NONE,
                                                                     SCALE_DOWN);
  public static final ICSSProperty OBJECT_POSITION = new CSSPropertyEnumOrNumbers (ECSSProperty.OBJECT_POSITION,
                                                                                   true,
                                                                                   1,
                                                                                   4,
                                                                                   CCSSValue.BOTTOM,
                                                                                   CENTER,
                                                                                   CCSSValue.LEFT,
                                                                                   CCSSValue.RIGHT,
                                                                                   CCSSValue.TOP);
  public static final ICSSProperty OBJECT_VIEW_BOX = new CSSPropertyFree (ECSSProperty.OBJECT_VIEW_BOX);
  public static final ICSSProperty OFFSET = new CSSPropertyFree (ECSSProperty.OFFSET);
  public static final ICSSProperty OFFSET_ANCHOR = new CSSPropertyEnumOrNumbers (ECSSProperty.OFFSET_ANCHOR,
                                                                                 true,
                                                                                 1,
                                                                                 4,
                                                                                 AUTO,
                                                                                 CCSSValue.BOTTOM,
                                                                                 CENTER,
                                                                                 CCSSValue.LEFT,
                                                                                 CCSSValue.RIGHT,
                                                                                 CCSSValue.TOP);
  public static final ICSSProperty OFFSET_DISTANCE = new CSSPropertyNumber (ECSSProperty.OFFSET_DISTANCE, true);
  public static final ICSSProperty OFFSET_PATH = new CSSPropertyFree (ECSSProperty.OFFSET_PATH);
  public static final ICSSProperty OFFSET_POSITION = new CSSPropertyEnumOrNumbers (ECSSProperty.OFFSET_POSITION,
                                                                                   true,
                                                                                   1,
                                                                                   4,
                                                                                   AUTO,
                                                                                   CCSSValue.BOTTOM,
                                                                                   CENTER,
                                                                                   CCSSValue.LEFT,
                                                                                   NORMAL,
                                                                                   CCSSValue.RIGHT,
                                                                                   CCSSValue.TOP);
  public static final ICSSProperty OFFSET_ROTATE = new CSSPropertyEnumOrNumbers (ECSSProperty.OFFSET_ROTATE,
                                                                                 false,
                                                                                 1,
                                                                                 2,
                                                                                 AUTO,
                                                                                 REVERSE);
  public static final ICSSProperty ORDER = new CSSPropertyLong (ECSSProperty.ORDER);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty ORIENTATION = new CSSPropertyFree (ECSSProperty.ORIENTATION);
  public static final ICSSProperty ORPHANS = new CSSPropertyLong (ECSSProperty.ORPHANS);
  public static final ICSSProperty OVERFLOW_ANCHOR = new CSSPropertyEnum (ECSSProperty.OVERFLOW_ANCHOR, AUTO, NONE);
  public static final ICSSProperty OVERFLOW_BLOCK = new CSSPropertyEnum (ECSSProperty.OVERFLOW_BLOCK,
                                                                         AUTO,
                                                                         CCSSValue.CLIP,
                                                                         HIDDEN,
                                                                         SCROLL,
                                                                         VISIBLE);
  public static final ICSSProperty OVERFLOW_CLIP_MARGIN = new CSSPropertyEnumOrUnsignedNumbers (ECSSProperty.OVERFLOW_CLIP_MARGIN,
                                                                                                false,
                                                                                                1,
                                                                                                2,
                                                                                                BORDER_BOX,
                                                                                                CONTENT_BOX,
                                                                                                PADDING_BOX);
  public static final ICSSProperty OVERFLOW_INLINE = new CSSPropertyEnum (ECSSProperty.OVERFLOW_INLINE,
                                                                          AUTO,
                                                                          CCSSValue.CLIP,
                                                                          HIDDEN,
                                                                          SCROLL,
                                                                          VISIBLE);
  public static final ICSSProperty OVERFLOW_WRAP = new CSSPropertyEnum (ECSSProperty.OVERFLOW_WRAP,
                                                                        ANYWHERE,
                                                                        BREAK_WORD,
                                                                        NORMAL);
  public static final ICSSProperty OVERFLOW_X = new CSSPropertyEnum (ECSSProperty.OVERFLOW_X,
                                                                     AUTO,
                                                                     CCSSValue.CLIP,
                                                                     HIDDEN,
                                                                     SCROLL,
                                                                     VISIBLE);
  public static final ICSSProperty OVERFLOW_Y = new CSSPropertyEnum (ECSSProperty.OVERFLOW_Y,
                                                                     AUTO,
                                                                     CCSSValue.CLIP,
                                                                     HIDDEN,
                                                                     SCROLL,
                                                                     VISIBLE);
  public static final ICSSProperty OVERLAY = new CSSPropertyEnum (ECSSProperty.OVERLAY, AUTO, NONE);
  public static final ICSSProperty OVERSCROLL_BEHAVIOR = new CSSPropertyEnums (ECSSProperty.OVERSCROLL_BEHAVIOR,
                                                                               1,
                                                                               2,
                                                                               AUTO,
                                                                               CCSSValue.CONTAIN,
                                                                               NONE,
                                                                               CHAIN);
  public static final ICSSProperty OVERSCROLL_BEHAVIOR_BLOCK = new CSSPropertyEnum (ECSSProperty.OVERSCROLL_BEHAVIOR_BLOCK,
                                                                                    AUTO,
                                                                                    CCSSValue.CONTAIN,
                                                                                    NONE,
                                                                                    CHAIN);
  public static final ICSSProperty OVERSCROLL_BEHAVIOR_INLINE = new CSSPropertyEnum (ECSSProperty.OVERSCROLL_BEHAVIOR_INLINE,
                                                                                     AUTO,
                                                                                     CCSSValue.CONTAIN,
                                                                                     NONE,
                                                                                     CHAIN);
  public static final ICSSProperty OVERSCROLL_BEHAVIOR_X = new CSSPropertyEnum (ECSSProperty.OVERSCROLL_BEHAVIOR_X,
                                                                                AUTO,
                                                                                CCSSValue.CONTAIN,
                                                                                NONE,
                                                                                CHAIN);
  public static final ICSSProperty OVERSCROLL_BEHAVIOR_Y = new CSSPropertyEnum (ECSSProperty.OVERSCROLL_BEHAVIOR_Y,
                                                                                AUTO,
                                                                                CCSSValue.CONTAIN,
                                                                                NONE,
                                                                                CHAIN);
  public static final ICSSProperty PADDING_BLOCK = new CSSPropertyUnsignedNumbers (ECSSProperty.PADDING_BLOCK,
                                                                                   true,
                                                                                   1,
                                                                                   2);
  public static final ICSSProperty PADDING_BLOCK_END = new CSSPropertyUnsignedNumber (ECSSProperty.PADDING_BLOCK_END,
                                                                                      true);
  public static final ICSSProperty PADDING_BLOCK_START = new CSSPropertyUnsignedNumber (ECSSProperty.PADDING_BLOCK_START,
                                                                                        true);
  public static final ICSSProperty PADDING_INLINE = new CSSPropertyUnsignedNumbers (ECSSProperty.PADDING_INLINE,
                                                                                    true,
                                                                                    1,
                                                                                    2);
  public static final ICSSProperty PADDING_INLINE_END = new CSSPropertyUnsignedNumber (ECSSProperty.PADDING_INLINE_END,
                                                                                       true);
  public static final ICSSProperty PADDING_INLINE_START = new CSSPropertyUnsignedNumber (ECSSProperty.PADDING_INLINE_START,
                                                                                         true);
  public static final ICSSProperty PAGE = new CSSPropertyFree (ECSSProperty.PAGE);
  public static final ICSSProperty PAGE_BREAK_AFTER = new CSSPropertyEnum (ECSSProperty.PAGE_BREAK_AFTER,
                                                                           ALWAYS,
                                                                           AUTO,
                                                                           AVOID,
                                                                           CCSSValue.LEFT,
                                                                           RECTO,
                                                                           CCSSValue.RIGHT,
                                                                           VERSO);
  public static final ICSSProperty PAGE_BREAK_BEFORE = new CSSPropertyEnum (ECSSProperty.PAGE_BREAK_BEFORE,
                                                                            ALWAYS,
                                                                            AUTO,
                                                                            AVOID,
                                                                            CCSSValue.LEFT,
                                                                            RECTO,
                                                                            CCSSValue.RIGHT,
                                                                            VERSO);
  public static final ICSSProperty PAGE_BREAK_INSIDE = new CSSPropertyEnum (ECSSProperty.PAGE_BREAK_INSIDE,
                                                                            AUTO,
                                                                            AVOID);
  public static final ICSSProperty PAINT_ORDER = new CSSPropertyEnumCombination (ECSSProperty.PAINT_ORDER,
                                                                                 3,
                                                                                 new String [] { NORMAL },
                                                                                 CCSSValue.FILL,
                                                                                 MARKERS,
                                                                                 CCSSValue.STROKE);
  public static final ICSSProperty PATH_LENGTH = new CSSPropertyFree (ECSSProperty.PATH_LENGTH);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty PAUSE = new CSSPropertyFree (ECSSProperty.PAUSE);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty PAUSE_AFTER = new CSSPropertyFree (ECSSProperty.PAUSE_AFTER);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty PAUSE_BEFORE = new CSSPropertyFree (ECSSProperty.PAUSE_BEFORE);
  public static final ICSSProperty PERSPECTIVE = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.PERSPECTIVE,
                                                                                      false,
                                                                                      NONE);
  public static final ICSSProperty PERSPECTIVE_ORIGIN = new CSSPropertyEnumOrNumbers (ECSSProperty.PERSPECTIVE_ORIGIN,
                                                                                      true,
                                                                                      1,
                                                                                      4,
                                                                                      CCSSValue.BOTTOM,
                                                                                      CENTER,
                                                                                      CCSSValue.LEFT,
                                                                                      CCSSValue.RIGHT,
                                                                                      CCSSValue.TOP);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty PITCH = new CSSPropertyFree (ECSSProperty.PITCH);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty PITCH_RANGE = new CSSPropertyFree (ECSSProperty.PITCH_RANGE);
  public static final ICSSProperty PLACE_CONTENT = new CSSPropertyEnums (ECSSProperty.PLACE_CONTENT,
                                                                         1,
                                                                         4,
                                                                         BASELINE,
                                                                         CENTER,
                                                                         END,
                                                                         FIRST,
                                                                         FLEX_END,
                                                                         FLEX_START,
                                                                         LAST,
                                                                         CCSSValue.LEFT,
                                                                         NORMAL,
                                                                         CCSSValue.RIGHT,
                                                                         SAFE,
                                                                         SPACE_AROUND,
                                                                         SPACE_BETWEEN,
                                                                         SPACE_EVENLY,
                                                                         START,
                                                                         STRETCH,
                                                                         UNSAFE);
  public static final ICSSProperty PLACE_ITEMS = new CSSPropertyEnums (ECSSProperty.PLACE_ITEMS,
                                                                       1,
                                                                       4,
                                                                       ANCHOR_CENTER,
                                                                       BASELINE,
                                                                       CENTER,
                                                                       END,
                                                                       FIRST,
                                                                       FLEX_END,
                                                                       FLEX_START,
                                                                       LAST,
                                                                       CCSSValue.LEFT,
                                                                       LEGACY,
                                                                       NORMAL,
                                                                       CCSSValue.RIGHT,
                                                                       SAFE,
                                                                       SELF_END,
                                                                       SELF_START,
                                                                       START,
                                                                       STRETCH,
                                                                       UNSAFE);
  public static final ICSSProperty PLACE_SELF = new CSSPropertyEnums (ECSSProperty.PLACE_SELF,
                                                                      1,
                                                                      4,
                                                                      ANCHOR_CENTER,
                                                                      AUTO,
                                                                      BASELINE,
                                                                      CENTER,
                                                                      END,
                                                                      FIRST,
                                                                      FLEX_END,
                                                                      FLEX_START,
                                                                      LAST,
                                                                      CCSSValue.LEFT,
                                                                      NORMAL,
                                                                      CCSSValue.RIGHT,
                                                                      SAFE,
                                                                      SELF_END,
                                                                      SELF_START,
                                                                      START,
                                                                      STRETCH,
                                                                      UNSAFE);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty PLAY_DURING = new CSSPropertyFree (ECSSProperty.PLAY_DURING);
  public static final ICSSProperty POINTER_EVENTS = new CSSPropertyEnum (ECSSProperty.POINTER_EVENTS,
                                                                         CCSSValue.ALL,
                                                                         AUTO,
                                                                         CCSSValue.FILL,
                                                                         INHERIT,
                                                                         NONE,
                                                                         PAINTED,
                                                                         CCSSValue.STROKE,
                                                                         VISIBLE,
                                                                         VISIBLEFILL,
                                                                         VISIBLEPAINTED,
                                                                         VISIBLESTROKE,
                                                                         BOUNDING_BOX);
  public static final ICSSProperty POSITION_ANCHOR = new CSSPropertyFree (ECSSProperty.POSITION_ANCHOR);
  public static final ICSSProperty POSITION_AREA = new CSSPropertyFree (ECSSProperty.POSITION_AREA);
  public static final ICSSProperty POSITION_TRY = new CSSPropertyFree (ECSSProperty.POSITION_TRY);
  public static final ICSSProperty POSITION_TRY_FALLBACKS = new CSSPropertyFree (ECSSProperty.POSITION_TRY_FALLBACKS);
  public static final ICSSProperty POSITION_TRY_ORDER = new CSSPropertyEnum (ECSSProperty.POSITION_TRY_ORDER,
                                                                             MOST_BLOCK_SIZE,
                                                                             MOST_HEIGHT,
                                                                             MOST_INLINE_SIZE,
                                                                             MOST_WIDTH,
                                                                             NORMAL);
  public static final ICSSProperty POSITION_VISIBILITY = new CSSPropertyEnumCombination (ECSSProperty.POSITION_VISIBILITY,
                                                                                         3,
                                                                                         new String [] { ALWAYS },
                                                                                         ANCHORS_VALID,
                                                                                         ANCHORS_VISIBLE,
                                                                                         NO_OVERFLOW);
  public static final ICSSProperty PRINT_COLOR_ADJUST = new CSSPropertyEnum (ECSSProperty.PRINT_COLOR_ADJUST,
                                                                             ECONOMY,
                                                                             EXACT);
  public static final ICSSProperty QUOTES = new CSSPropertyFree (ECSSProperty.QUOTES);
  public static final ICSSProperty R = new CSSPropertyUnsignedNumber (ECSSProperty.R, true);
  public static final ICSSProperty READING_FLOW = new CSSPropertyEnum (ECSSProperty.READING_FLOW,
                                                                       CCSSValue.FLEX_FLOW,
                                                                       FLEX_VISUAL,
                                                                       GRID_COLUMNS,
                                                                       GRID_ORDER,
                                                                       GRID_ROWS,
                                                                       NORMAL,
                                                                       SOURCE_ORDER);
  public static final ICSSProperty READING_ORDER = new CSSPropertyLong (ECSSProperty.READING_ORDER);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty REGION_FRAGMENT = new CSSPropertyFree (ECSSProperty.REGION_FRAGMENT);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty RESOLUTION = new CSSPropertyFree (ECSSProperty.RESOLUTION);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty REST = new CSSPropertyFree (ECSSProperty.REST);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty REST_AFTER = new CSSPropertyFree (ECSSProperty.REST_AFTER);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty REST_BEFORE = new CSSPropertyFree (ECSSProperty.REST_BEFORE);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty RICHNESS = new CSSPropertyFree (ECSSProperty.RICHNESS);
  public static final ICSSProperty ROTATE = new CSSPropertyFree (ECSSProperty.ROTATE);
  public static final ICSSProperty ROW_GAP = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.ROW_GAP,
                                                                                  true,
                                                                                  NORMAL,
                                                                                  THIN,
                                                                                  MEDIUM,
                                                                                  THICK);
  public static final ICSSProperty RUBY_ALIGN = new CSSPropertyEnum (ECSSProperty.RUBY_ALIGN,
                                                                     CENTER,
                                                                     SPACE_AROUND,
                                                                     SPACE_BETWEEN,
                                                                     START);
  public static final ICSSProperty RUBY_MERGE = new CSSPropertyEnum (ECSSProperty.RUBY_MERGE, AUTO, COLLAPSE, SEPARATE);
  public static final ICSSProperty RUBY_OVERHANG = new CSSPropertyEnum (ECSSProperty.RUBY_OVERHANG, AUTO, NONE);
  public static final ICSSProperty RUBY_POSITION = new CSSPropertyEnumCombination (ECSSProperty.RUBY_POSITION,
                                                                                   2,
                                                                                   new String [] { INTER_CHARACTER },
                                                                                   ALTERNATE,
                                                                                   OVER,
                                                                                   UNDER);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty RUNNING = new CSSPropertyFree (ECSSProperty.RUNNING);
  public static final ICSSProperty RX = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.RX, true, AUTO);
  public static final ICSSProperty RY = new CSSPropertyEnumOrUnsignedNumber (ECSSProperty.RY, true, AUTO);
  public static final ICSSProperty SCALE = new CSSPropertyFree (ECSSProperty.SCALE);
  public static final ICSSProperty SCROLLBAR_COLOR = new CSSPropertyEnumOrColors (ECSSProperty.SCROLLBAR_COLOR,
                                                                                  1,
                                                                                  2,
                                                                                  AUTO);
  public static final ICSSProperty SCROLLBAR_GUTTER = new CSSPropertyEnumCombination (ECSSProperty.SCROLLBAR_GUTTER,
                                                                                      2,
                                                                                      new String [] { AUTO },
                                                                                      BOTH_EDGES,
                                                                                      STABLE);
  public static final ICSSProperty SCROLLBAR_WIDTH = new CSSPropertyEnum (ECSSProperty.SCROLLBAR_WIDTH,
                                                                          AUTO,
                                                                          NONE,
                                                                          THIN);
  public static final ICSSProperty SCROLL_AXIS_LOCK = new CSSPropertyEnum (ECSSProperty.SCROLL_AXIS_LOCK, AUTO, NONE);
  public static final ICSSProperty SCROLL_BEHAVIOR = new CSSPropertyEnum (ECSSProperty.SCROLL_BEHAVIOR, AUTO, SMOOTH);
  public static final ICSSProperty SCROLL_INITIAL_TARGET = new CSSPropertyEnum (ECSSProperty.SCROLL_INITIAL_TARGET,
                                                                                NEAREST,
                                                                                NONE);
  public static final ICSSProperty SCROLL_MARGIN = new CSSPropertyNumbers (ECSSProperty.SCROLL_MARGIN, false, 1, 4);
  public static final ICSSProperty SCROLL_MARGIN_BLOCK = new CSSPropertyNumbers (ECSSProperty.SCROLL_MARGIN_BLOCK,
                                                                                 false,
                                                                                 1,
                                                                                 2);
  public static final ICSSProperty SCROLL_MARGIN_BLOCK_END = new CSSPropertyNumber (ECSSProperty.SCROLL_MARGIN_BLOCK_END,
                                                                                    false);
  public static final ICSSProperty SCROLL_MARGIN_BLOCK_START = new CSSPropertyNumber (ECSSProperty.SCROLL_MARGIN_BLOCK_START,
                                                                                      false);
  public static final ICSSProperty SCROLL_MARGIN_BOTTOM = new CSSPropertyNumber (ECSSProperty.SCROLL_MARGIN_BOTTOM,
                                                                                 false);
  public static final ICSSProperty SCROLL_MARGIN_INLINE = new CSSPropertyNumbers (ECSSProperty.SCROLL_MARGIN_INLINE,
                                                                                  false,
                                                                                  1,
                                                                                  2);
  public static final ICSSProperty SCROLL_MARGIN_INLINE_END = new CSSPropertyNumber (ECSSProperty.SCROLL_MARGIN_INLINE_END,
                                                                                     false);
  public static final ICSSProperty SCROLL_MARGIN_INLINE_START = new CSSPropertyNumber (ECSSProperty.SCROLL_MARGIN_INLINE_START,
                                                                                       false);
  public static final ICSSProperty SCROLL_MARGIN_LEFT = new CSSPropertyNumber (ECSSProperty.SCROLL_MARGIN_LEFT, false);
  public static final ICSSProperty SCROLL_MARGIN_RIGHT = new CSSPropertyNumber (ECSSProperty.SCROLL_MARGIN_RIGHT,
                                                                                false);
  public static final ICSSProperty SCROLL_MARGIN_TOP = new CSSPropertyNumber (ECSSProperty.SCROLL_MARGIN_TOP, false);
  public static final ICSSProperty SCROLL_MARKER_GROUP = new CSSPropertyEnum (ECSSProperty.SCROLL_MARKER_GROUP,
                                                                              AFTER,
                                                                              BEFORE,
                                                                              NONE);
  public static final ICSSProperty SCROLL_PADDING = new CSSPropertyEnumOrNumbers (ECSSProperty.SCROLL_PADDING,
                                                                                  true,
                                                                                  1,
                                                                                  4,
                                                                                  AUTO);
  public static final ICSSProperty SCROLL_PADDING_BLOCK = new CSSPropertyEnumOrNumbers (ECSSProperty.SCROLL_PADDING_BLOCK,
                                                                                        true,
                                                                                        1,
                                                                                        2,
                                                                                        AUTO);
  public static final ICSSProperty SCROLL_PADDING_BLOCK_END = new CSSPropertyEnumOrNumber (ECSSProperty.SCROLL_PADDING_BLOCK_END,
                                                                                           true,
                                                                                           AUTO);
  public static final ICSSProperty SCROLL_PADDING_BLOCK_START = new CSSPropertyEnumOrNumber (ECSSProperty.SCROLL_PADDING_BLOCK_START,
                                                                                             true,
                                                                                             AUTO);
  public static final ICSSProperty SCROLL_PADDING_BOTTOM = new CSSPropertyEnumOrNumber (ECSSProperty.SCROLL_PADDING_BOTTOM,
                                                                                        true,
                                                                                        AUTO);
  public static final ICSSProperty SCROLL_PADDING_INLINE = new CSSPropertyEnumOrNumbers (ECSSProperty.SCROLL_PADDING_INLINE,
                                                                                         true,
                                                                                         1,
                                                                                         2,
                                                                                         AUTO);
  public static final ICSSProperty SCROLL_PADDING_INLINE_END = new CSSPropertyEnumOrNumber (ECSSProperty.SCROLL_PADDING_INLINE_END,
                                                                                            true,
                                                                                            AUTO);
  public static final ICSSProperty SCROLL_PADDING_INLINE_START = new CSSPropertyEnumOrNumber (ECSSProperty.SCROLL_PADDING_INLINE_START,
                                                                                              true,
                                                                                              AUTO);
  public static final ICSSProperty SCROLL_PADDING_LEFT = new CSSPropertyEnumOrNumber (ECSSProperty.SCROLL_PADDING_LEFT,
                                                                                      true,
                                                                                      AUTO);
  public static final ICSSProperty SCROLL_PADDING_RIGHT = new CSSPropertyEnumOrNumber (ECSSProperty.SCROLL_PADDING_RIGHT,
                                                                                       true,
                                                                                       AUTO);
  public static final ICSSProperty SCROLL_PADDING_TOP = new CSSPropertyEnumOrNumber (ECSSProperty.SCROLL_PADDING_TOP,
                                                                                     true,
                                                                                     AUTO);
  public static final ICSSProperty SCROLL_SNAP_ALIGN = new CSSPropertyEnums (ECSSProperty.SCROLL_SNAP_ALIGN,
                                                                             1,
                                                                             2,
                                                                             CENTER,
                                                                             END,
                                                                             NONE,
                                                                             START);
  public static final ICSSProperty SCROLL_SNAP_STOP = new CSSPropertyEnum (ECSSProperty.SCROLL_SNAP_STOP,
                                                                           ALWAYS,
                                                                           NORMAL);
  public static final ICSSProperty SCROLL_SNAP_TYPE = new CSSPropertyEnumCombination (ECSSProperty.SCROLL_SNAP_TYPE,
                                                                                      2,
                                                                                      new String [] { NONE },
                                                                                      BLOCK,
                                                                                      BOTH,
                                                                                      INLINE,
                                                                                      MANDATORY,
                                                                                      PROXIMITY,
                                                                                      CCSSValue.X,
                                                                                      CCSSValue.Y);
  public static final ICSSProperty SCROLL_TARGET_GROUP = new CSSPropertyEnum (ECSSProperty.SCROLL_TARGET_GROUP,
                                                                              AUTO,
                                                                              NONE);
  public static final ICSSProperty SCROLL_TIMELINE = new CSSPropertyFree (ECSSProperty.SCROLL_TIMELINE);
  public static final ICSSProperty SCROLL_TIMELINE_AXIS = new CSSPropertyList (ECSSProperty.SCROLL_TIMELINE_AXIS,
                                                                               new CSSPropertyEnum (ECSSProperty.SCROLL_TIMELINE_AXIS,
                                                                                                    BLOCK,
                                                                                                    INLINE,
                                                                                                    CCSSValue.X,
                                                                                                    CCSSValue.Y));
  public static final ICSSProperty SCROLL_TIMELINE_NAME = new CSSPropertyFree (ECSSProperty.SCROLL_TIMELINE_NAME);
  public static final ICSSProperty SHAPE_IMAGE_THRESHOLD = new CSSPropertyDoubleOrPercentage (ECSSProperty.SHAPE_IMAGE_THRESHOLD);
  public static final ICSSProperty SHAPE_MARGIN = new CSSPropertyNumber (ECSSProperty.SHAPE_MARGIN, true);
  public static final ICSSProperty SHAPE_OUTSIDE = new CSSPropertyFree (ECSSProperty.SHAPE_OUTSIDE);
  public static final ICSSProperty SHAPE_RENDERING = new CSSPropertyEnum (ECSSProperty.SHAPE_RENDERING,
                                                                          AUTO,
                                                                          CRISPEDGES,
                                                                          GEOMETRICPRECISION,
                                                                          OPTIMIZESPEED);
  public static final ICSSProperty SIZE = new CSSPropertyFree (ECSSProperty.SIZE);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty SPEAK = new CSSPropertyFree (ECSSProperty.SPEAK);
  public static final ICSSProperty SPEAK_AS = new CSSPropertyEnumCombination (ECSSProperty.SPEAK_AS,
                                                                              3,
                                                                              new String [] { NORMAL },
                                                                              DIGITS,
                                                                              LITERAL_PUNCTUATION,
                                                                              NO_PUNCTUATION,
                                                                              SPELL_OUT);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty SPEAK_NUMERAL = new CSSPropertyFree (ECSSProperty.SPEAK_NUMERAL);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty SPEAK_PUNCTUATION = new CSSPropertyFree (ECSSProperty.SPEAK_PUNCTUATION);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty SPEECH_RATE = new CSSPropertyFree (ECSSProperty.SPEECH_RATE);
  public static final ICSSProperty SRC = new CSSPropertyFree (ECSSProperty.SRC);
  public static final ICSSProperty STOP_COLOR = new CSSPropertyColor (ECSSProperty.STOP_COLOR);
  public static final ICSSProperty STOP_OPACITY = new CSSPropertyDoubleOrPercentage (ECSSProperty.STOP_OPACITY);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty STRESS = new CSSPropertyFree (ECSSProperty.STRESS);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty STRING_SET = new CSSPropertyFree (ECSSProperty.STRING_SET);
  public static final ICSSProperty STROKE = new CSSPropertyFree (ECSSProperty.STROKE);
  public static final ICSSProperty STROKE_COLOR = new CSSPropertyColor (ECSSProperty.STROKE_COLOR);
  public static final ICSSProperty STROKE_DASHARRAY = new CSSPropertyFree (ECSSProperty.STROKE_DASHARRAY);
  public static final ICSSProperty STROKE_DASHOFFSET = new CSSPropertyEnumOrNumberOrDouble (ECSSProperty.STROKE_DASHOFFSET,
                                                                                            true,
                                                                                            NONE);
  public static final ICSSProperty STROKE_LINECAP = new CSSPropertyEnum (ECSSProperty.STROKE_LINECAP,
                                                                         BUTT,
                                                                         ROUND,
                                                                         SQUARE);
  public static final ICSSProperty STROKE_LINEJOIN = new CSSPropertyEnum (ECSSProperty.STROKE_LINEJOIN,
                                                                          ARCS,
                                                                          BEVEL,
                                                                          MITER,
                                                                          MITER_CLIP,
                                                                          ROUND,
                                                                          CROP,
                                                                          FALLBACK);
  public static final ICSSProperty STROKE_MITERLIMIT = new CSSPropertyDouble (ECSSProperty.STROKE_MITERLIMIT);
  public static final ICSSProperty STROKE_OPACITY = new CSSPropertyDoubleOrPercentage (ECSSProperty.STROKE_OPACITY);
  public static final ICSSProperty STROKE_WIDTH = new CSSPropertyEnumOrNumberOrDouble (ECSSProperty.STROKE_WIDTH,
                                                                                       true,
                                                                                       INHERIT);
  public static final ICSSProperty TAB_SIZE = new CSSPropertyEnumOrUnsignedNumberOrDouble (ECSSProperty.TAB_SIZE,
                                                                                           false,
                                                                                           INHERIT);
  public static final ICSSProperty TEXT_ALIGN_LAST = new CSSPropertyEnum (ECSSProperty.TEXT_ALIGN_LAST,
                                                                          AUTO,
                                                                          CENTER,
                                                                          END,
                                                                          JUSTIFY,
                                                                          CCSSValue.LEFT,
                                                                          CCSSValue.RIGHT,
                                                                          START);
  public static final ICSSProperty TEXT_ANCHOR = new CSSPropertyEnum (ECSSProperty.TEXT_ANCHOR, END, MIDDLE, START);
  public static final ICSSProperty TEXT_AUTOSPACE = new CSSPropertyFree (ECSSProperty.TEXT_AUTOSPACE);
  public static final ICSSProperty TEXT_BOX = new CSSPropertyEnumCombination (ECSSProperty.TEXT_BOX,
                                                                              3,
                                                                              new String [] { NORMAL },
                                                                              ALPHABETIC,
                                                                              AUTO,
                                                                              CAP,
                                                                              EX,
                                                                              IDEOGRAPHIC,
                                                                              IDEOGRAPHIC_INK,
                                                                              NONE,
                                                                              TEXT,
                                                                              TRIM_BOTH,
                                                                              TRIM_END,
                                                                              TRIM_START);
  public static final ICSSProperty TEXT_BOX_EDGE = new CSSPropertyEnums (ECSSProperty.TEXT_BOX_EDGE,
                                                                         1,
                                                                         2,
                                                                         AUTO,
                                                                         ALPHABETIC,
                                                                         CAP,
                                                                         EX,
                                                                         IDEOGRAPHIC,
                                                                         IDEOGRAPHIC_INK,
                                                                         TEXT);
  public static final ICSSProperty TEXT_BOX_TRIM = new CSSPropertyEnum (ECSSProperty.TEXT_BOX_TRIM,
                                                                        NONE,
                                                                        TRIM_BOTH,
                                                                        TRIM_END,
                                                                        TRIM_START);
  public static final ICSSProperty TEXT_DECORATION_COLOR = new CSSPropertyColor (ECSSProperty.TEXT_DECORATION_COLOR);
  public static final ICSSProperty TEXT_DECORATION_INSET = new CSSPropertyEnumOrNumbers (ECSSProperty.TEXT_DECORATION_INSET,
                                                                                         true,
                                                                                         1,
                                                                                         2,
                                                                                         AUTO);
  public static final ICSSProperty TEXT_DECORATION_LINE = new CSSPropertyEnumCombination (ECSSProperty.TEXT_DECORATION_LINE,
                                                                                          4,
                                                                                          new String [] { GRAMMAR_ERROR, NONE, SPELLING_ERROR },
                                                                                          BLINK,
                                                                                          LINE_THROUGH,
                                                                                          OVERLINE,
                                                                                          UNDERLINE);
  public static final ICSSProperty TEXT_DECORATION_SKIP = new CSSPropertyEnumCombination (ECSSProperty.TEXT_DECORATION_SKIP,
                                                                                          5,
                                                                                          new String [] { NONE },
                                                                                          BOX_DECORATION,
                                                                                          EDGES,
                                                                                          LEADING_SPACES,
                                                                                          OBJECTS,
                                                                                          SPACES,
                                                                                          TRAILING_SPACES);
  public static final ICSSProperty TEXT_DECORATION_SKIP_INK = new CSSPropertyEnum (ECSSProperty.TEXT_DECORATION_SKIP_INK,
                                                                                   CCSSValue.ALL,
                                                                                   AUTO,
                                                                                   NONE);
  public static final ICSSProperty TEXT_DECORATION_STYLE = new CSSPropertyEnum (ECSSProperty.TEXT_DECORATION_STYLE,
                                                                                DASHED,
                                                                                DOTTED,
                                                                                DOUBLE,
                                                                                SOLID,
                                                                                WAVY);
  public static final ICSSProperty TEXT_DECORATION_THICKNESS = new CSSPropertyEnumOrNumber (ECSSProperty.TEXT_DECORATION_THICKNESS,
                                                                                            true,
                                                                                            AUTO,
                                                                                            FROM_FONT);
  public static final ICSSProperty TEXT_EMPHASIS = new CSSPropertyFree (ECSSProperty.TEXT_EMPHASIS);
  public static final ICSSProperty TEXT_EMPHASIS_COLOR = new CSSPropertyColor (ECSSProperty.TEXT_EMPHASIS_COLOR);
  public static final ICSSProperty TEXT_EMPHASIS_POSITION = new CSSPropertyEnumCombination (ECSSProperty.TEXT_EMPHASIS_POSITION,
                                                                                            2,
                                                                                            new String [] { AUTO },
                                                                                            CCSSValue.LEFT,
                                                                                            OVER,
                                                                                            CCSSValue.RIGHT,
                                                                                            UNDER);
  public static final ICSSProperty TEXT_EMPHASIS_STYLE = new CSSPropertyFree (ECSSProperty.TEXT_EMPHASIS_STYLE);
  public static final ICSSProperty TEXT_FIT = new CSSPropertyEnumOrNumbers (ECSSProperty.TEXT_FIT,
                                                                            true,
                                                                            1,
                                                                            3,
                                                                            CONSISTENT,
                                                                            GROW,
                                                                            NONE,
                                                                            PER_LINE,
                                                                            PER_LINE_ALL,
                                                                            SHRINK);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty TEXT_HEIGHT = new CSSPropertyFree (ECSSProperty.TEXT_HEIGHT);
  public static final ICSSProperty TEXT_JUSTIFY = new CSSPropertyEnum (ECSSProperty.TEXT_JUSTIFY,
                                                                       AUTO,
                                                                       INTER_CHARACTER,
                                                                       INTER_WORD,
                                                                       NONE,
                                                                       DISTRIBUTE);
  public static final ICSSProperty TEXT_RENDERING = new CSSPropertyEnum (ECSSProperty.TEXT_RENDERING,
                                                                         AUTO,
                                                                         GEOMETRICPRECISION,
                                                                         OPTIMIZELEGIBILITY,
                                                                         OPTIMIZESPEED);
  public static final ICSSProperty TEXT_SHADOW = new CSSPropertyFree (ECSSProperty.TEXT_SHADOW);
  public static final ICSSProperty TEXT_SIZE_ADJUST = new CSSPropertyEnumOrNumber (ECSSProperty.TEXT_SIZE_ADJUST,
                                                                                   true,
                                                                                   AUTO,
                                                                                   NONE);
  public static final ICSSProperty TEXT_SPACING_TRIM = new CSSPropertyEnum (ECSSProperty.TEXT_SPACING_TRIM,
                                                                            NORMAL,
                                                                            SPACE_ALL,
                                                                            SPACE_FIRST,
                                                                            TRIM_START);
  public static final ICSSProperty TEXT_UNDERLINE_OFFSET = new CSSPropertyEnumOrNumber (ECSSProperty.TEXT_UNDERLINE_OFFSET,
                                                                                        true,
                                                                                        AUTO);
  public static final ICSSProperty TEXT_UNDERLINE_POSITION = new CSSPropertyEnumCombination (ECSSProperty.TEXT_UNDERLINE_POSITION,
                                                                                             2,
                                                                                             new String [] { AUTO, FROM_FONT },
                                                                                             CCSSValue.LEFT,
                                                                                             CCSSValue.RIGHT,
                                                                                             UNDER);
  public static final ICSSProperty TEXT_WRAP = new CSSPropertyEnumCombination (ECSSProperty.TEXT_WRAP,
                                                                               2,
                                                                               new String [0],
                                                                               AUTO,
                                                                               BALANCE,
                                                                               NOWRAP,
                                                                               PRETTY,
                                                                               STABLE,
                                                                               WRAP);
  public static final ICSSProperty TEXT_WRAP_MODE = new CSSPropertyEnum (ECSSProperty.TEXT_WRAP_MODE, NOWRAP, WRAP);
  public static final ICSSProperty TEXT_WRAP_STYLE = new CSSPropertyEnum (ECSSProperty.TEXT_WRAP_STYLE,
                                                                          AUTO,
                                                                          BALANCE,
                                                                          PRETTY,
                                                                          STABLE);
  public static final ICSSProperty TIMELINE_SCOPE = new CSSPropertyFree (ECSSProperty.TIMELINE_SCOPE);
  public static final ICSSProperty TIMELINE_TRIGGER = new CSSPropertyFree (ECSSProperty.TIMELINE_TRIGGER);
  public static final ICSSProperty TIMELINE_TRIGGER_ACTIVATION_RANGE = new CSSPropertyFree (ECSSProperty.TIMELINE_TRIGGER_ACTIVATION_RANGE);
  public static final ICSSProperty TIMELINE_TRIGGER_ACTIVATION_RANGE_END = new CSSPropertyList (ECSSProperty.TIMELINE_TRIGGER_ACTIVATION_RANGE_END,
                                                                                                new CSSPropertyEnumOrNumbers (ECSSProperty.TIMELINE_TRIGGER_ACTIVATION_RANGE_END,
                                                                                                                              true,
                                                                                                                              1,
                                                                                                                              2,
                                                                                                                              CCSSValue.CONTAIN,
                                                                                                                              COVER,
                                                                                                                              ENTRY,
                                                                                                                              ENTRY_CROSSING,
                                                                                                                              EXIT,
                                                                                                                              EXIT_CROSSING,
                                                                                                                              NORMAL));
  public static final ICSSProperty TIMELINE_TRIGGER_ACTIVATION_RANGE_START = new CSSPropertyList (ECSSProperty.TIMELINE_TRIGGER_ACTIVATION_RANGE_START,
                                                                                                  new CSSPropertyEnumOrNumbers (ECSSProperty.TIMELINE_TRIGGER_ACTIVATION_RANGE_START,
                                                                                                                                true,
                                                                                                                                1,
                                                                                                                                2,
                                                                                                                                CCSSValue.CONTAIN,
                                                                                                                                COVER,
                                                                                                                                ENTRY,
                                                                                                                                ENTRY_CROSSING,
                                                                                                                                EXIT,
                                                                                                                                EXIT_CROSSING,
                                                                                                                                NORMAL));
  public static final ICSSProperty TIMELINE_TRIGGER_ACTIVE_RANGE = new CSSPropertyFree (ECSSProperty.TIMELINE_TRIGGER_ACTIVE_RANGE);
  public static final ICSSProperty TIMELINE_TRIGGER_ACTIVE_RANGE_END = new CSSPropertyList (ECSSProperty.TIMELINE_TRIGGER_ACTIVE_RANGE_END,
                                                                                            new CSSPropertyEnumOrNumbers (ECSSProperty.TIMELINE_TRIGGER_ACTIVE_RANGE_END,
                                                                                                                          true,
                                                                                                                          1,
                                                                                                                          2,
                                                                                                                          AUTO,
                                                                                                                          CCSSValue.CONTAIN,
                                                                                                                          COVER,
                                                                                                                          ENTRY,
                                                                                                                          ENTRY_CROSSING,
                                                                                                                          EXIT,
                                                                                                                          EXIT_CROSSING,
                                                                                                                          NORMAL));
  public static final ICSSProperty TIMELINE_TRIGGER_ACTIVE_RANGE_START = new CSSPropertyList (ECSSProperty.TIMELINE_TRIGGER_ACTIVE_RANGE_START,
                                                                                              new CSSPropertyEnumOrNumbers (ECSSProperty.TIMELINE_TRIGGER_ACTIVE_RANGE_START,
                                                                                                                            true,
                                                                                                                            1,
                                                                                                                            2,
                                                                                                                            AUTO,
                                                                                                                            CCSSValue.CONTAIN,
                                                                                                                            COVER,
                                                                                                                            ENTRY,
                                                                                                                            ENTRY_CROSSING,
                                                                                                                            EXIT,
                                                                                                                            EXIT_CROSSING,
                                                                                                                            NORMAL));
  public static final ICSSProperty TIMELINE_TRIGGER_NAME = new CSSPropertyFree (ECSSProperty.TIMELINE_TRIGGER_NAME);
  public static final ICSSProperty TIMELINE_TRIGGER_SOURCE = new CSSPropertyFree (ECSSProperty.TIMELINE_TRIGGER_SOURCE);
  public static final ICSSProperty TOUCH_ACTION = new CSSPropertyEnumCombination (ECSSProperty.TOUCH_ACTION,
                                                                                  3,
                                                                                  new String [] { AUTO, MANIPULATION, NONE },
                                                                                  PAN_DOWN,
                                                                                  PAN_LEFT,
                                                                                  PAN_RIGHT,
                                                                                  PAN_UP,
                                                                                  PAN_X,
                                                                                  PAN_Y,
                                                                                  PINCH_ZOOM);
  public static final ICSSProperty TRANSFORM = new CSSPropertyFree (ECSSProperty.TRANSFORM);
  public static final ICSSProperty TRANSFORM_BOX = new CSSPropertyEnum (ECSSProperty.TRANSFORM_BOX,
                                                                        BORDER_BOX,
                                                                        CONTENT_BOX,
                                                                        FILL_BOX,
                                                                        STROKE_BOX,
                                                                        VIEW_BOX);
  public static final ICSSProperty TRANSFORM_ORIGIN = new CSSPropertyEnumOrNumbers (ECSSProperty.TRANSFORM_ORIGIN,
                                                                                    true,
                                                                                    1,
                                                                                    3,
                                                                                    CCSSValue.BOTTOM,
                                                                                    CENTER,
                                                                                    CCSSValue.LEFT,
                                                                                    CCSSValue.RIGHT,
                                                                                    CCSSValue.TOP);
  public static final ICSSProperty TRANSFORM_STYLE = new CSSPropertyEnum (ECSSProperty.TRANSFORM_STYLE,
                                                                          FLAT,
                                                                          PRESERVE_3D);
  public static final ICSSProperty TRANSITION = new CSSPropertyFree (ECSSProperty.TRANSITION);
  public static final ICSSProperty TRANSITION_BEHAVIOR = new CSSPropertyList (ECSSProperty.TRANSITION_BEHAVIOR,
                                                                              new CSSPropertyEnum (ECSSProperty.TRANSITION_BEHAVIOR,
                                                                                                   ALLOW_DISCRETE,
                                                                                                   NORMAL));
  public static final ICSSProperty TRANSITION_DELAY = new CSSPropertyList (ECSSProperty.TRANSITION_DELAY,
                                                                           new CSSPropertyNumber (ECSSProperty.TRANSITION_DELAY,
                                                                                                  false));
  public static final ICSSProperty TRANSITION_DURATION = new CSSPropertyList (ECSSProperty.TRANSITION_DURATION,
                                                                              new CSSPropertyUnsignedNumber (ECSSProperty.TRANSITION_DURATION,
                                                                                                             false));
  public static final ICSSProperty TRANSITION_PROPERTY = new CSSPropertyFree (ECSSProperty.TRANSITION_PROPERTY);
  public static final ICSSProperty TRANSITION_TIMING_FUNCTION = new CSSPropertyFree (ECSSProperty.TRANSITION_TIMING_FUNCTION);
  public static final ICSSProperty TRANSLATE = new CSSPropertyEnumOrNumbers (ECSSProperty.TRANSLATE, true, 1, 3, NONE);
  public static final ICSSProperty TRIGGER_SCOPE = new CSSPropertyFree (ECSSProperty.TRIGGER_SCOPE);
  public static final ICSSProperty UNICODE_RANGE = new CSSPropertyFree (ECSSProperty.UNICODE_RANGE);
  public static final ICSSProperty USER_SELECT = new CSSPropertyEnum (ECSSProperty.USER_SELECT,
                                                                      CCSSValue.ALL,
                                                                      AUTO,
                                                                      NONE,
                                                                      TEXT);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty USER_ZOOM = new CSSPropertyFree (ECSSProperty.USER_ZOOM);
  public static final ICSSProperty VECTOR_EFFECT = new CSSPropertyEnum (ECSSProperty.VECTOR_EFFECT,
                                                                        FIXED_POSITION,
                                                                        NON_ROTATION,
                                                                        NON_SCALING_SIZE,
                                                                        NON_SCALING_STROKE,
                                                                        NONE);
  public static final ICSSProperty VIEW_TIMELINE = new CSSPropertyFree (ECSSProperty.VIEW_TIMELINE);
  public static final ICSSProperty VIEW_TIMELINE_AXIS = new CSSPropertyList (ECSSProperty.VIEW_TIMELINE_AXIS,
                                                                             new CSSPropertyEnum (ECSSProperty.VIEW_TIMELINE_AXIS,
                                                                                                  BLOCK,
                                                                                                  INLINE,
                                                                                                  CCSSValue.X,
                                                                                                  CCSSValue.Y));
  public static final ICSSProperty VIEW_TIMELINE_INSET = new CSSPropertyList (ECSSProperty.VIEW_TIMELINE_INSET,
                                                                              new CSSPropertyEnumOrNumbers (ECSSProperty.VIEW_TIMELINE_INSET,
                                                                                                            true,
                                                                                                            1,
                                                                                                            2,
                                                                                                            AUTO));
  public static final ICSSProperty VIEW_TIMELINE_NAME = new CSSPropertyFree (ECSSProperty.VIEW_TIMELINE_NAME);
  public static final ICSSProperty VIEW_TRANSITION_CLASS = new CSSPropertyFree (ECSSProperty.VIEW_TRANSITION_CLASS);
  public static final ICSSProperty VIEW_TRANSITION_NAME = new CSSPropertyFree (ECSSProperty.VIEW_TRANSITION_NAME);
  public static final ICSSProperty VIEW_TRANSITION_SCOPE = new CSSPropertyEnum (ECSSProperty.VIEW_TRANSITION_SCOPE,
                                                                                CCSSValue.ALL,
                                                                                NONE);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty VOICE_BALANCE = new CSSPropertyFree (ECSSProperty.VOICE_BALANCE);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty VOICE_DURATION = new CSSPropertyFree (ECSSProperty.VOICE_DURATION);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty VOICE_FAMILY = new CSSPropertyFree (ECSSProperty.VOICE_FAMILY);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty VOICE_PITCH = new CSSPropertyFree (ECSSProperty.VOICE_PITCH);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty VOICE_RANGE = new CSSPropertyFree (ECSSProperty.VOICE_RANGE);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty VOICE_RATE = new CSSPropertyFree (ECSSProperty.VOICE_RATE);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty VOICE_STRESS = new CSSPropertyFree (ECSSProperty.VOICE_STRESS);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty VOICE_VOLUME = new CSSPropertyFree (ECSSProperty.VOICE_VOLUME);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty VOLUME = new CSSPropertyFree (ECSSProperty.VOLUME);
  public static final ICSSProperty WHITE_SPACE_COLLAPSE = new CSSPropertyEnum (ECSSProperty.WHITE_SPACE_COLLAPSE,
                                                                               BREAK_SPACES,
                                                                               COLLAPSE,
                                                                               PRESERVE,
                                                                               PRESERVE_BREAKS,
                                                                               PRESERVE_SPACES);
  public static final ICSSProperty WIDOWS = new CSSPropertyLong (ECSSProperty.WIDOWS);
  public static final ICSSProperty WILL_CHANGE = new CSSPropertyFree (ECSSProperty.WILL_CHANGE);
  public static final ICSSProperty WINDOW_DRAG = new CSSPropertyEnum (ECSSProperty.WINDOW_DRAG, MOVE, NONE);
  public static final ICSSProperty WORD_BREAK = new CSSPropertyEnum (ECSSProperty.WORD_BREAK,
                                                                     AUTO_PHRASE,
                                                                     BREAK_ALL,
                                                                     BREAK_WORD,
                                                                     KEEP_ALL,
                                                                     NORMAL);
  public static final ICSSProperty WORD_WRAP = new CSSPropertyEnum (ECSSProperty.WORD_WRAP, BREAK_WORD, NORMAL);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty WRAP_FLOW = new CSSPropertyFree (ECSSProperty.WRAP_FLOW);
  @Deprecated (forRemoval = false, since = "8.3.0")
  public static final ICSSProperty WRAP_THROUGH = new CSSPropertyFree (ECSSProperty.WRAP_THROUGH);
  public static final ICSSProperty X = new CSSPropertyNumber (ECSSProperty.X, true);
  public static final ICSSProperty Y = new CSSPropertyNumber (ECSSProperty.Y, true);
  public static final ICSSProperty ZOOM = new CSSPropertyFree (ECSSProperty.ZOOM);

  /** Use {@link #JUSTIFY_CONTENT} instead. */
  @Deprecated (forRemoval = true, since = "8.3.0")
  @ChangeNextMajorRelease ("Remove this misspelled constant")
  public static final ICSSProperty JUSITFY_CONTENT = JUSTIFY_CONTENT;
  /** Use {@link #JUSTIFY_ITEMS} instead. */
  @Deprecated (forRemoval = true, since = "8.3.0")
  @ChangeNextMajorRelease ("Remove this misspelled constant")
  public static final ICSSProperty JUSITFY_ITEMS = JUSTIFY_ITEMS;
  /** Use {@link #JUSTIFY_SELF} instead. */
  @Deprecated (forRemoval = true, since = "8.3.0")
  @ChangeNextMajorRelease ("Remove this misspelled constant")
  public static final ICSSProperty JUSITFY_SELF = JUSTIFY_SELF;

  // Special predefined properties that are used quite often
  public static final ICSSValue DISPLAY_BLOCK = DISPLAY.newValue (BLOCK);
  public static final ICSSValue DISPLAY_FLEX = DISPLAY.newValue (CCSSValue.FLEX);
  public static final ICSSValue DISPLAY_INLINE = DISPLAY.newValue (INLINE);
  public static final ICSSValue DISPLAY_INLINE_BLOCK = DISPLAY.newValue (INLINE_BLOCK);
  public static final ICSSValue DISPLAY_NONE = DISPLAY.newValue (NONE);

  public static final ICSSValue VISIBILITY_VISIBLE = VISIBILITY.newValue (VISIBLE);
  public static final ICSSValue VISIBILITY_HIDDEN = VISIBILITY.newValue (HIDDEN);

  public static final ICSSValue WIDTH_0 = WIDTH.newValue (ECSSUnit.zero ());
  public static final ICSSValue WIDTH_100PERC = WIDTH.newValue (ECSSUnit.perc (100));

  public static final ICSSValue HEIGHT_0 = HEIGHT.newValue (ECSSUnit.zero ());
  public static final ICSSValue HEIGHT_100PERC = HEIGHT.newValue (ECSSUnit.perc (100));

  public static final ICSSValue PADDING_0 = PADDING.newValue (ECSSUnit.zero ());
  public static final ICSSValue PADDING_TOP_0 = PADDING_TOP.newValue (ECSSUnit.zero ());
  public static final ICSSValue PADDING_RIGHT_0 = PADDING_RIGHT.newValue (ECSSUnit.zero ());
  public static final ICSSValue PADDING_BOTTOM_0 = PADDING_BOTTOM.newValue (ECSSUnit.zero ());
  public static final ICSSValue PADDING_LEFT_0 = PADDING_LEFT.newValue (ECSSUnit.zero ());

  public static final ICSSValue MARGIN_0 = MARGIN.newValue (ECSSUnit.zero ());
  public static final ICSSValue MARGIN_TOP_0 = MARGIN_TOP.newValue (ECSSUnit.zero ());
  public static final ICSSValue MARGIN_RIGHT_0 = MARGIN_RIGHT.newValue (ECSSUnit.zero ());
  public static final ICSSValue MARGIN_BOTTOM_0 = MARGIN_BOTTOM.newValue (ECSSUnit.zero ());
  public static final ICSSValue MARGIN_LEFT_0 = MARGIN_LEFT.newValue (ECSSUnit.zero ());

  @PresentForCodeCoverage
  private static final CCSSProperties INSTANCE = new CCSSProperties ();

  private CCSSProperties ()
  {}
}
