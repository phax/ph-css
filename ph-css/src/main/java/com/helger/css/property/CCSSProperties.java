/*
 * Copyright (C) 2014-2025 Philip Helger (www.helger.com)
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
 * Contains the most commonly used CSS properties plus the available value
 * ranges.
 *
 * @author Philip Helger
 */
@Immutable
public final class CCSSProperties
{
  private static final String [] ARRAY_ATTACHMENT = new String [] { SCROLL, FIXED, LOCAL };
  private static final String [] ARRAY_BORDER_STYLE = new String [] { NONE,
                                                                      HIDDEN,
                                                                      DOTTED,
                                                                      DASHED,
                                                                      SOLID,
                                                                      DOUBLE,
                                                                      GROOVE,
                                                                      RIDGE,
                                                                      INSET,
                                                                      OUTSET };
  private static final String [] ARRAY_BOX = new String [] { BORDER_BOX, PADDING_BOX, CONTENT_BOX };
  private static final String [] ARRAY_LINE_WIDTH = new String [] { THIN, MEDIUM, THICK };
  private static final String [] ARRAY_REPEAT_STYLE = new String [] { REPEAT_X, REPEAT_Y, REPEAT, SPACE, ROUND, NO_REPEAT };

  // Text formatting
  public static final ICSSProperty FONT_FAMILY = new CSSPropertyFree (ECSSProperty.FONT_FAMILY);
  public static final ICSSProperty FONT_STYLE = new CSSPropertyEnum (ECSSProperty.FONT_STYLE, ITALIC, OBLIQUE, NORMAL);
  public static final ICSSProperty FONT_VARIANT = new CSSPropertyEnum (ECSSProperty.FONT_VARIANT, SMALL_CAPS, NORMAL);
  public static final ICSSProperty FONT_SIZE = new CSSPropertyEnumOrNumber (ECSSProperty.FONT_SIZE,
                                                                            true,
                                                                            XX_SMALL,
                                                                            X_SMALL,
                                                                            SMALL,
                                                                            MEDIUM,
                                                                            LARGE,
                                                                            X_LARGE,
                                                                            XX_LARGE,
                                                                            SMALLER,
                                                                            LARGER);
  public static final ICSSProperty FONT_WEIGHT = new CSSPropertyEnum (ECSSProperty.FONT_WEIGHT,
                                                                      BOLD,
                                                                      BOLDER,
                                                                      LIGHTER,
                                                                      NORMAL,
                                                                      _100,
                                                                      _200,
                                                                      _300,
                                                                      _400,
                                                                      _500,
                                                                      _600,
                                                                      _700,
                                                                      _800,
                                                                      _900);
  public static final ICSSProperty WORD_SPACING = new CSSPropertyNumber (ECSSProperty.WORD_SPACING, false);
  public static final ICSSProperty LETTER_SPACING = new CSSPropertyNumber (ECSSProperty.LETTER_SPACING, false);
  public static final ICSSProperty TEXT_DECORATION = new CSSPropertyEnum (ECSSProperty.TEXT_DECORATION,
                                                                          UNDERLINE,
                                                                          OVERLINE,
                                                                          LINE_THROUGH,
                                                                          BLINK,
                                                                          NONE);
  public static final ICSSProperty TEXT_TRANSFORM = new CSSPropertyEnum (ECSSProperty.TEXT_TRANSFORM,
                                                                         CAPITALIZE,
                                                                         UPPERCASE,
                                                                         LOWERCASE,
                                                                         NONE);
  public static final ICSSProperty COLOR = new CSSPropertyColor (ECSSProperty.COLOR);

  // Orientation and paragraph control
  public static final ICSSProperty TEXT_INDENT = new CSSPropertyNumber (ECSSProperty.TEXT_INDENT, true);
  public static final ICSSProperty LINE_HEIGHT = new CSSPropertyEnumOrNumber (ECSSProperty.LINE_HEIGHT, true, NORMAL);
  public static final ICSSProperty VERTICAL_ALIGN = new CSSPropertyEnum (ECSSProperty.VERTICAL_ALIGN,
                                                                         CCSSValue.TOP,
                                                                         MIDDLE,
                                                                         CCSSValue.BOTTOM,
                                                                         BASELINE,
                                                                         SUB,
                                                                         SUPER,
                                                                         TEXT_TOP,
                                                                         TEXT_BOTTOM);
  public static final ICSSProperty TEXT_ALIGN = new CSSPropertyEnum (ECSSProperty.TEXT_ALIGN,
                                                                     CCSSValue.LEFT,
                                                                     CENTER,
                                                                     CCSSValue.RIGHT,
                                                                     JUSTIFY);
  public static final ICSSProperty WHITE_SPACE = new CSSPropertyEnum (ECSSProperty.WHITE_SPACE, NORMAL, PRE, NOWRAP, PRE_LINE, PRE_WRAP);

  // margin
  public static final ICSSProperty MARGIN_TOP = new CSSPropertyEnumOrNumber (ECSSProperty.MARGIN_TOP, true, AUTO);
  public static final ICSSProperty MARGIN_RIGHT = new CSSPropertyEnumOrNumber (ECSSProperty.MARGIN_RIGHT, true, AUTO);
  public static final ICSSProperty MARGIN_BOTTOM = new CSSPropertyEnumOrNumber (ECSSProperty.MARGIN_BOTTOM, true, AUTO);
  public static final ICSSProperty MARGIN_LEFT = new CSSPropertyEnumOrNumber (ECSSProperty.MARGIN_LEFT, true, AUTO);
  public static final ICSSProperty MARGIN = new CSSPropertyEnumOrNumbers (ECSSProperty.MARGIN, true, 1, 4, AUTO);
  // padding
  public static final ICSSProperty PADDING_TOP = new CSSPropertyEnumOrNumber (ECSSProperty.PADDING_TOP, true, AUTO);
  public static final ICSSProperty PADDING_RIGHT = new CSSPropertyEnumOrNumber (ECSSProperty.PADDING_RIGHT, true, AUTO);
  public static final ICSSProperty PADDING_BOTTOM = new CSSPropertyEnumOrNumber (ECSSProperty.PADDING_BOTTOM, true, AUTO);
  public static final ICSSProperty PADDING_LEFT = new CSSPropertyEnumOrNumber (ECSSProperty.PADDING_LEFT, true, AUTO);
  public static final ICSSProperty PADDING = new CSSPropertyEnumOrNumbers (ECSSProperty.PADDING, true, 1, 4, AUTO);

  // borders

  // list formatting
  public static final ICSSProperty LIST_STYLE_TYPE = new CSSPropertyEnum (ECSSProperty.LIST_STYLE_TYPE,
                                                                          DECIMAL,
                                                                          LOWER_ROMAN,
                                                                          UPPER_ROMAN,
                                                                          LOWER_ALPHA,
                                                                          UPPER_ALPHA,
                                                                          LOWER_LATIN,
                                                                          UPPER_LATIN,
                                                                          DISC,
                                                                          CIRCLE,
                                                                          SQUARE,
                                                                          NONE);
  public static final ICSSProperty LIST_STYLE_POSITION = new CSSPropertyEnum (ECSSProperty.LIST_STYLE_POSITION, INSIDE, OUTSIDE);
  public static final ICSSProperty LIST_STYLE_IMAGE = new CSSPropertyURL (ECSSProperty.LIST_STYLE_IMAGE);

  // table formatting
  public static final ICSSProperty CAPTION_SIDE = new CSSPropertyEnum (ECSSProperty.CAPTION_SIDE, CCSSValue.TOP, CCSSValue.BOTTOM);
  public static final ICSSProperty TABLE_LAYOUT = new CSSPropertyEnum (ECSSProperty.TABLE_LAYOUT, AUTO, FIXED);
  public static final ICSSProperty BORDER_COLLAPSE = new CSSPropertyEnum (ECSSProperty.BORDER_COLLAPSE, SEPARATE, COLLAPSE);
  public static final ICSSProperty BORDER_SPACING = new CSSPropertyNumber (ECSSProperty.BORDER_SPACING, true);
  public static final ICSSProperty EMPTY_CELLS = new CSSPropertyEnum (ECSSProperty.EMPTY_CELLS, SHOW, HIDE);
  public static final ICSSProperty SPEAK_HEADER = new CSSPropertyEnum (ECSSProperty.SPEAK_HEADER, ALWAYS, ONCE);

  // positioning
  public static final ICSSProperty POSITION = new CSSPropertyEnum (ECSSProperty.POSITION, STATIC, RELATIVE, ABSOLUTE, FIXED);
  public static final ICSSProperty TOP = new CSSPropertyEnumOrNumber (ECSSProperty.TOP, true, AUTO);
  public static final ICSSProperty LEFT = new CSSPropertyEnumOrNumber (ECSSProperty.LEFT, true, AUTO);
  public static final ICSSProperty BOTTOM = new CSSPropertyEnumOrNumber (ECSSProperty.BOTTOM, true, AUTO);
  public static final ICSSProperty RIGHT = new CSSPropertyEnumOrNumber (ECSSProperty.RIGHT, true, AUTO);
  public static final ICSSProperty WIDTH = new CSSPropertyEnumOrNumber (ECSSProperty.WIDTH, true, AUTO);
  public static final ICSSProperty MIN_WIDTH = new CSSPropertyNumber (ECSSProperty.MIN_WIDTH, true);
  public static final ICSSProperty MAX_WIDTH = new CSSPropertyNumber (ECSSProperty.MAX_WIDTH, true);
  public static final ICSSProperty HEIGHT = new CSSPropertyEnumOrNumber (ECSSProperty.HEIGHT, true, AUTO);
  public static final ICSSProperty MIN_HEIGHT = new CSSPropertyNumber (ECSSProperty.MIN_HEIGHT, true);
  public static final ICSSProperty MAX_HEIGHT = new CSSPropertyNumber (ECSSProperty.MAX_HEIGHT, true);
  public static final ICSSProperty OVERFLOW = new CSSPropertyEnum (ECSSProperty.OVERFLOW, VISIBLE, HIDDEN, SCROLL, AUTO);
  public static final ICSSProperty FLOAT = new CSSPropertyEnum (ECSSProperty.FLOAT, CCSSValue.LEFT, CCSSValue.RIGHT, NONE);
  public static final ICSSProperty CLEAR = new CSSPropertyEnum (ECSSProperty.CLEAR, CCSSValue.LEFT, CCSSValue.RIGHT, BOTH, NONE);
  public static final ICSSProperty Z_INDEX = new CSSPropertyEnumOrInt (ECSSProperty.Z_INDEX, AUTO);
  public static final ICSSProperty DISPLAY = new CSSPropertyEnum (ECSSProperty.DISPLAY,
                                                                  BLOCK,
                                                                  INLINE,
                                                                  INLINE_BLOCK,
                                                                  LIST_ITEM,
                                                                  RUN_IN,
                                                                  NONE,
                                                                  TABLE,
                                                                  INLINE_TABLE,
                                                                  TABLE_ROW,
                                                                  TABLE_CELL,
                                                                  TABLE_ROW_GROUP,
                                                                  TABLE_HEADER_GROUP,
                                                                  TABLE_FOOTER_GROUP,
                                                                  TABLE_COLUMN,
                                                                  TABLE_COLUMN_GROUP,
                                                                  TABLE_CAPTION,
                                                                  CCSSValue.FLEX);
  public static final ICSSProperty VISIBILITY = new CSSPropertyEnum (ECSSProperty.VISIBILITY, VISIBLE, HIDDEN, COLLAPSE);
  public static final ICSSProperty CLIP = new CSSPropertyEnumOrRect (ECSSProperty.CLIP, AUTO);

  // display window
  public static final ICSSProperty OPACITY = new CSSPropertyDouble (ECSSProperty.OPACITY, new CSSPropertyCustomizerOpacity ());

  /*
   * CSS Writing Modes Level 3
   */
  public static final ICSSProperty DIRECTION = new CSSPropertyEnum (ECSSProperty.DIRECTION, LTR, RTL);
  public static final ICSSProperty GLYPH_ORIENTATION_VERTICAL = new CSSPropertyEnumOrNumber (ECSSProperty.GLYPH_ORIENTATION_VERTICAL,
                                                                                             false,
                                                                                             AUTO,
                                                                                             ECSSUnit.deg (0),
                                                                                             ECSSUnit.deg (90));
  @Deprecated
  public static final ICSSProperty TEXT_COMBINE_HORIZONTAL = new CSSPropertyFree (ECSSProperty.TEXT_COMBINE_HORIZONTAL);
  public static final ICSSProperty TEXT_COMBINE_UPRIGHT = new CSSPropertyEnum (ECSSProperty.TEXT_COMBINE_UPRIGHT, NONE, CCSSValue.ALL);
  public static final ICSSProperty TEXT_ORIENTATION = new CSSPropertyEnum (ECSSProperty.TEXT_ORIENTATION, MIXED, UPRIGHT, SIDEWAYS);
  public static final ICSSProperty UNICODE_BIDI = new CSSPropertyEnum (ECSSProperty.UNICODE_BIDI,
                                                                       NORMAL,
                                                                       EMBED,
                                                                       ISOLATE,
                                                                       BIDI_OVERRIDE,
                                                                       ISOLATE_OVERRIDE,
                                                                       PLAINTEXT);
  public static final ICSSProperty WRITING_MODE = new CSSPropertyEnum (ECSSProperty.WRITING_MODE, HORIZONTAL_TB, VERTICAL_RL, VERTICAL_LR);

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
  public static final ICSSProperty OUTLINE_COLOR = new CSSPropertyEnumOrColors (ECSSProperty.OUTLINE_COLOR, 1, 4, INVERT);
  public static final ICSSProperty OUTLINE_OFFSET = new CSSPropertyNumber (ECSSProperty.OUTLINE_OFFSET, false);
  public static final ICSSProperty OUTLINE_STYLE = new CSSPropertyEnums (ECSSProperty.OUTLINE_STYLE,
                                                                         1,
                                                                         4,
                                                                         ArrayHelper.getConcatenated (AUTO, ARRAY_BORDER_STYLE));
  public static final ICSSProperty OUTLINE_WIDTH = new CSSPropertyEnumOrNumbers (ECSSProperty.OUTLINE_WIDTH, false, 1, 4, ARRAY_LINE_WIDTH);
  public static final ICSSProperty RESIZE = new CSSPropertyEnum (ECSSProperty.RESIZE, NONE, BOTH, HORIZONTAL, VERTICAL);
  public static final ICSSProperty TEXT_OVERFLOW = new CSSPropertyEnum (ECSSProperty.TEXT_OVERFLOW, CCSSValue.CLIP, ELLIPSIS);

  /*
   * CSS Containment Module Level 1
   */
  // none | strict | content | [ size || layout || paint ]
  public static final ICSSProperty CONTAIN = new CSSPropertyFree (ECSSProperty.CONTAIN);

  /*
   * CSS Backgrounds and Borders Level 3
   */
  public static final ICSSProperty BACKGROUND = new CSSPropertyFree (ECSSProperty.BACKGROUND);
  public static final ICSSProperty BACKGROUND_ATTACHMENT = new CSSPropertyEnum (ECSSProperty.BACKGROUND_ATTACHMENT, ARRAY_ATTACHMENT);
  public static final ICSSProperty BACKGROUND_CLIP = new CSSPropertyEnum (ECSSProperty.BACKGROUND_CLIP, ARRAY_BOX);
  public static final ICSSProperty BACKGROUND_COLOR = new CSSPropertyColor (ECSSProperty.BACKGROUND_COLOR);
  public static final ICSSProperty BACKGROUND_IMAGE = new CSSPropertyEnumOrURL (ECSSProperty.BACKGROUND_IMAGE, NONE);
  public static final ICSSProperty BACKGROUND_ORIGIN = new CSSPropertyEnum (ECSSProperty.BACKGROUND_ORIGIN, ARRAY_BOX);
  public static final ICSSProperty BACKGROUND_POSITION = new CSSPropertyEnumOrNumbers (ECSSProperty.BACKGROUND_POSITION,
                                                                                       true,
                                                                                       2,
                                                                                       2,
                                                                                       CCSSValue.TOP,
                                                                                       CCSSValue.BOTTOM,
                                                                                       CENTER,
                                                                                       CCSSValue.LEFT,
                                                                                       CCSSValue.RIGHT);
  // repeat-x | repeat-y | [repeat | space | round | no-repeat]{1,2}
  public static final ICSSProperty BACKGROUND_REPEAT = new CSSPropertyEnums (ECSSProperty.BACKGROUND_REPEAT, 1, 2, ARRAY_REPEAT_STYLE);
  public static final ICSSProperty BACKGROUND_SIZE = new CSSPropertyEnumOrNumbers (ECSSProperty.BACKGROUND_SIZE,
                                                                                   true,
                                                                                   2,
                                                                                   2,
                                                                                   AUTO,
                                                                                   COVER,
                                                                                   CCSSValue.CONTAIN);
  public static final ICSSProperty BORDER = new CSSPropertyFree (ECSSProperty.BORDER);
  public static final ICSSProperty BORDER_BOTTOM = new CSSPropertyFree (ECSSProperty.BORDER_BOTTOM);
  public static final ICSSProperty BORDER_LEFT = new CSSPropertyFree (ECSSProperty.BORDER_LEFT);
  public static final ICSSProperty BORDER_RIGHT = new CSSPropertyFree (ECSSProperty.BORDER_RIGHT);
  public static final ICSSProperty BORDER_TOP = new CSSPropertyFree (ECSSProperty.BORDER_TOP);

  public static final ICSSProperty BORDER_BOTTOM_WIDTH = new CSSPropertyEnumOrNumber (ECSSProperty.BORDER_BOTTOM_WIDTH,
                                                                                      false,
                                                                                      ARRAY_LINE_WIDTH);
  public static final ICSSProperty BORDER_LEFT_WIDTH = new CSSPropertyEnumOrNumber (ECSSProperty.BORDER_LEFT_WIDTH,
                                                                                    false,
                                                                                    ARRAY_LINE_WIDTH);
  public static final ICSSProperty BORDER_RIGHT_WIDTH = new CSSPropertyEnumOrNumber (ECSSProperty.BORDER_RIGHT_WIDTH,
                                                                                     false,
                                                                                     ARRAY_LINE_WIDTH);
  public static final ICSSProperty BORDER_TOP_WIDTH = new CSSPropertyEnumOrNumber (ECSSProperty.BORDER_TOP_WIDTH, false, ARRAY_LINE_WIDTH);
  public static final ICSSProperty BORDER_WIDTH = new CSSPropertyEnumOrNumbers (ECSSProperty.BORDER_WIDTH, false, 1, 4, ARRAY_LINE_WIDTH);
  public static final ICSSProperty BORDER_BOTTOM_COLOR = new CSSPropertyColor (ECSSProperty.BORDER_BOTTOM_COLOR);
  public static final ICSSProperty BORDER_LEFT_COLOR = new CSSPropertyColor (ECSSProperty.BORDER_LEFT_COLOR);
  public static final ICSSProperty BORDER_RIGHT_COLOR = new CSSPropertyColor (ECSSProperty.BORDER_RIGHT_COLOR);
  public static final ICSSProperty BORDER_TOP_COLOR = new CSSPropertyColor (ECSSProperty.BORDER_TOP_COLOR);
  public static final ICSSProperty BORDER_COLOR = new CSSPropertyColors (ECSSProperty.BORDER_COLOR, 1, 4);

  public static final ICSSProperty BORDER_BOTTOM_STYLE = new CSSPropertyEnum (ECSSProperty.BORDER_BOTTOM_STYLE, ARRAY_BORDER_STYLE);
  public static final ICSSProperty BORDER_LEFT_STYLE = new CSSPropertyEnum (ECSSProperty.BORDER_LEFT_STYLE, ARRAY_BORDER_STYLE);
  public static final ICSSProperty BORDER_RIGHT_STYLE = new CSSPropertyEnum (ECSSProperty.BORDER_RIGHT_STYLE, ARRAY_BORDER_STYLE);
  public static final ICSSProperty BORDER_TOP_STYLE = new CSSPropertyEnum (ECSSProperty.BORDER_TOP_STYLE, ARRAY_BORDER_STYLE);
  public static final ICSSProperty BORDER_STYLE = new CSSPropertyEnums (ECSSProperty.BORDER_STYLE, 1, 4, ARRAY_BORDER_STYLE);

  public static final ICSSProperty BORDER_TOP_LEFT_RADIUS = new CSSPropertyNumbers (ECSSProperty.BORDER_TOP_LEFT_RADIUS,
                                                                                    new CSSPropertyCustomizerBorderTopLeftRadius (),
                                                                                    true,
                                                                                    1,
                                                                                    2);
  public static final ICSSProperty BORDER_TOP_RIGHT_RADIUS = new CSSPropertyNumbers (ECSSProperty.BORDER_TOP_RIGHT_RADIUS,
                                                                                     new CSSPropertyCustomizerBorderTopRightRadius (),
                                                                                     true,
                                                                                     1,
                                                                                     2);
  public static final ICSSProperty BORDER_BOTTOM_LEFT_RADIUS = new CSSPropertyNumbers (ECSSProperty.BORDER_BOTTOM_LEFT_RADIUS,
                                                                                       new CSSPropertyCustomizerBorderBottomLeftRadius (),
                                                                                       true,
                                                                                       1,
                                                                                       2);
  public static final ICSSProperty BORDER_BOTTOM_RIGHT_RADIUS = new CSSPropertyNumbers (ECSSProperty.BORDER_BOTTOM_RIGHT_RADIUS,
                                                                                        new CSSPropertyCustomizerBorderBottomRightRadius (),
                                                                                        true,
                                                                                        1,
                                                                                        2);
  public static final ICSSProperty BORDER_RADIUS = new CSSPropertyFree (ECSSProperty.BORDER_RADIUS,
                                                                        new CSSPropertyCustomizerBorderRadius ());

  /*
   * Unspecified stuff (created by MainAddMissingProperties)
   */
  public static final ICSSProperty ALIGN_CONTENT = new CSSPropertyFree (ECSSProperty.ALIGN_CONTENT);
  public static final ICSSProperty ALIGN_ITEMS = new CSSPropertyFree (ECSSProperty.ALIGN_ITEMS);
  public static final ICSSProperty ALIGN_SELF = new CSSPropertyFree (ECSSProperty.ALIGN_SELF);
  @Deprecated
  public static final ICSSProperty ALIGNMENT_ADJUST = new CSSPropertyFree (ECSSProperty.ALIGNMENT_ADJUST);
  public static final ICSSProperty ALIGNMENT_BASELINE = new CSSPropertyFree (ECSSProperty.ALIGNMENT_BASELINE);
  public static final ICSSProperty ALL = new CSSPropertyFree (ECSSProperty.ALL);
  public static final ICSSProperty ANIMATION = new CSSPropertyFree (ECSSProperty.ANIMATION);
  public static final ICSSProperty ANIMATION_DELAY = new CSSPropertyFree (ECSSProperty.ANIMATION_DELAY);
  public static final ICSSProperty ANIMATION_DIRECTION = new CSSPropertyFree (ECSSProperty.ANIMATION_DIRECTION);
  public static final ICSSProperty ANIMATION_DURATION = new CSSPropertyFree (ECSSProperty.ANIMATION_DURATION);
  public static final ICSSProperty ANIMATION_FILL_MODE = new CSSPropertyFree (ECSSProperty.ANIMATION_FILL_MODE);
  public static final ICSSProperty ANIMATION_ITERATION_COUNT = new CSSPropertyFree (ECSSProperty.ANIMATION_ITERATION_COUNT);
  public static final ICSSProperty ANIMATION_NAME = new CSSPropertyFree (ECSSProperty.ANIMATION_NAME);
  public static final ICSSProperty ANIMATION_PLAY_STATE = new CSSPropertyFree (ECSSProperty.ANIMATION_PLAY_STATE);
  public static final ICSSProperty ANIMATION_TIMING_FUNCTION = new CSSPropertyFree (ECSSProperty.ANIMATION_TIMING_FUNCTION);
  public static final ICSSProperty AZIMUTH = new CSSPropertyFree (ECSSProperty.AZIMUTH);
  public static final ICSSProperty BACKFACE_VISIBILITY = new CSSPropertyFree (ECSSProperty.BACKFACE_VISIBILITY);
  public static final ICSSProperty BACKGROUND_BLEND_MODE = new CSSPropertyFree (ECSSProperty.BACKGROUND_BLEND_MODE);
  public static final ICSSProperty BASELINE_SHIFT = new CSSPropertyFree (ECSSProperty.BASELINE_SHIFT);
  @Deprecated
  public static final ICSSProperty BLEED = new CSSPropertyFree (ECSSProperty.BLEED);
  public static final ICSSProperty BOOKMARK_LABEL = new CSSPropertyFree (ECSSProperty.BOOKMARK_LABEL);
  public static final ICSSProperty BOOKMARK_LEVEL = new CSSPropertyFree (ECSSProperty.BOOKMARK_LEVEL);
  public static final ICSSProperty BOOKMARK_STATE = new CSSPropertyFree (ECSSProperty.BOOKMARK_STATE);
  @Deprecated
  public static final ICSSProperty BOOKMARK_TARGET = new CSSPropertyFree (ECSSProperty.BOOKMARK_TARGET);
  public static final ICSSProperty BORDER_IMAGE = new CSSPropertyFree (ECSSProperty.BORDER_IMAGE);
  public static final ICSSProperty BORDER_IMAGE_OUTSET = new CSSPropertyFree (ECSSProperty.BORDER_IMAGE_OUTSET);
  public static final ICSSProperty BORDER_IMAGE_REPEAT = new CSSPropertyFree (ECSSProperty.BORDER_IMAGE_REPEAT);
  public static final ICSSProperty BORDER_IMAGE_SLICE = new CSSPropertyFree (ECSSProperty.BORDER_IMAGE_SLICE);
  public static final ICSSProperty BORDER_IMAGE_SOURCE = new CSSPropertyFree (ECSSProperty.BORDER_IMAGE_SOURCE);
  public static final ICSSProperty BORDER_IMAGE_WIDTH = new CSSPropertyFree (ECSSProperty.BORDER_IMAGE_WIDTH);
  public static final ICSSProperty BOX_DECORATION_BREAK = new CSSPropertyFree (ECSSProperty.BOX_DECORATION_BREAK);
  public static final ICSSProperty BOX_SHADOW = new CSSPropertyFree (ECSSProperty.BOX_SHADOW);
  public static final ICSSProperty BOX_SNAP = new CSSPropertyFree (ECSSProperty.BOX_SNAP);
  public static final ICSSProperty BOX_SUPPRESS = new CSSPropertyFree (ECSSProperty.BOX_SUPPRESS);
  public static final ICSSProperty BREAK_AFTER = new CSSPropertyFree (ECSSProperty.BREAK_AFTER);
  public static final ICSSProperty BREAK_BEFORE = new CSSPropertyFree (ECSSProperty.BREAK_BEFORE);
  public static final ICSSProperty BREAK_INSIDE = new CSSPropertyFree (ECSSProperty.BREAK_INSIDE);
  public static final ICSSProperty CHAINS = new CSSPropertyFree (ECSSProperty.CHAINS);
  public static final ICSSProperty CLIP_PATH = new CSSPropertyFree (ECSSProperty.CLIP_PATH);
  public static final ICSSProperty CLIP_RULE = new CSSPropertyFree (ECSSProperty.CLIP_RULE);
  public static final ICSSProperty COLOR_INTERPOLATION_FILTERS = new CSSPropertyFree (ECSSProperty.COLOR_INTERPOLATION_FILTERS);
  public static final ICSSProperty COLUMNS = new CSSPropertyFree (ECSSProperty.COLUMNS);
  public static final ICSSProperty COLUMNS_SPAN = new CSSPropertyFree (ECSSProperty.COLUMNS_SPAN);
  public static final ICSSProperty COLUMNS_WIDTH = new CSSPropertyFree (ECSSProperty.COLUMNS_WIDTH);
  public static final ICSSProperty COLUMN_COUNT = new CSSPropertyFree (ECSSProperty.COLUMN_COUNT);
  public static final ICSSProperty COLUMN_FILL = new CSSPropertyFree (ECSSProperty.COLUMN_FILL);
  public static final ICSSProperty COLUMN_GAP = new CSSPropertyFree (ECSSProperty.COLUMN_GAP);
  public static final ICSSProperty COLUMN_RULE = new CSSPropertyFree (ECSSProperty.COLUMN_RULE);
  public static final ICSSProperty COLUMN_RULE_COLOR = new CSSPropertyFree (ECSSProperty.COLUMN_RULE_COLOR);
  public static final ICSSProperty COLUMN_RULE_STYLE = new CSSPropertyFree (ECSSProperty.COLUMN_RULE_STYLE);
  public static final ICSSProperty COLUMN_RULE_WIDTH = new CSSPropertyFree (ECSSProperty.COLUMN_RULE_WIDTH);
  public static final ICSSProperty COLUMN_WIDTH = new CSSPropertyFree (ECSSProperty.COLUMN_WIDTH);
  public static final ICSSProperty CONTENT = new CSSPropertyFree (ECSSProperty.CONTENT);
  public static final ICSSProperty COUNTER_INCREMENT = new CSSPropertyFree (ECSSProperty.COUNTER_INCREMENT);
  public static final ICSSProperty COUNTER_RESET = new CSSPropertyFree (ECSSProperty.COUNTER_RESET);
  public static final ICSSProperty COUNTER_SET = new CSSPropertyFree (ECSSProperty.COUNTER_SET);
  public static final ICSSProperty CUE = new CSSPropertyFree (ECSSProperty.CUE);
  public static final ICSSProperty CUE_AFTER = new CSSPropertyFree (ECSSProperty.CUE_AFTER);
  public static final ICSSProperty CUE_BEFORE = new CSSPropertyFree (ECSSProperty.CUE_BEFORE);
  public static final ICSSProperty DISPLAY_INSIDE = new CSSPropertyFree (ECSSProperty.DISPLAY_INSIDE);
  public static final ICSSProperty DISPLAY_LIST = new CSSPropertyFree (ECSSProperty.DISPLAY_LIST);
  public static final ICSSProperty DISPLAY_OUTSIDE = new CSSPropertyFree (ECSSProperty.DISPLAY_OUTSIDE);
  public static final ICSSProperty DOMINANT_BASELINE = new CSSPropertyFree (ECSSProperty.DOMINANT_BASELINE);
  public static final ICSSProperty DROP_INITIAL_AFTER_ADJUST = new CSSPropertyFree (ECSSProperty.DROP_INITIAL_AFTER_ADJUST);
  public static final ICSSProperty DROP_INITIAL_AFTER_ALIGN = new CSSPropertyFree (ECSSProperty.DROP_INITIAL_AFTER_ALIGN);
  public static final ICSSProperty DROP_INITIAL_BEFORE_ADJUST = new CSSPropertyFree (ECSSProperty.DROP_INITIAL_BEFORE_ADJUST);
  public static final ICSSProperty DROP_INITIAL_BEFORE_ALIGN = new CSSPropertyFree (ECSSProperty.DROP_INITIAL_BEFORE_ALIGN);
  public static final ICSSProperty DROP_INITIAL_SIZE = new CSSPropertyFree (ECSSProperty.DROP_INITIAL_SIZE);
  public static final ICSSProperty DROP_INITIAL_VALUE = new CSSPropertyFree (ECSSProperty.DROP_INITIAL_VALUE);
  public static final ICSSProperty ELEVATION = new CSSPropertyFree (ECSSProperty.ELEVATION);
  @Deprecated
  public static final ICSSProperty ENABLE_BACKGROUND = new CSSPropertyFree (ECSSProperty.ENABLE_BACKGROUND);
  public static final ICSSProperty FILTER = new CSSPropertyFree (ECSSProperty.FILTER);
  public static final ICSSProperty FLEX = new CSSPropertyFree (ECSSProperty.FLEX);
  public static final ICSSProperty FLEX_BASIS = new CSSPropertyEnumOrNumber (ECSSProperty.FLEX_BASIS, true, AUTO);
  public static final ICSSProperty FLEX_DIRECTION = new CSSPropertyFree (ECSSProperty.FLEX_DIRECTION);
  public static final ICSSProperty FLEX_FLOW = new CSSPropertyFree (ECSSProperty.FLEX_FLOW);
  public static final ICSSProperty FLEX_GROW = new CSSPropertyLong (ECSSProperty.FLEX_GROW);
  public static final ICSSProperty FLEX_SHRINK = new CSSPropertyLong (ECSSProperty.FLEX_SHRINK);
  public static final ICSSProperty FLEX_WRAP = new CSSPropertyFree (ECSSProperty.FLEX_WRAP);
  @Deprecated
  public static final ICSSProperty FLOAT_OFFSET = new CSSPropertyFree (ECSSProperty.FLOAT_OFFSET);
  public static final ICSSProperty FLOOD_COLOR = new CSSPropertyFree (ECSSProperty.FLOOD_COLOR);
  public static final ICSSProperty FLOOD_OPACITY = new CSSPropertyFree (ECSSProperty.FLOOD_OPACITY);
  public static final ICSSProperty FLOW_FROM = new CSSPropertyFree (ECSSProperty.FLOW_FROM);
  public static final ICSSProperty FLOW_INTO = new CSSPropertyFree (ECSSProperty.FLOW_INTO);
  public static final ICSSProperty FONT = new CSSPropertyFree (ECSSProperty.FONT);
  public static final ICSSProperty FONT_FEATURE_SETTINGS = new CSSPropertyFree (ECSSProperty.FONT_FEATURE_SETTINGS);
  public static final ICSSProperty FONT_KERNING = new CSSPropertyFree (ECSSProperty.FONT_KERNING);
  public static final ICSSProperty FONT_LANGUAGE_OVERRIDE = new CSSPropertyFree (ECSSProperty.FONT_LANGUAGE_OVERRIDE);
  public static final ICSSProperty FONT_SIZE_ADJUST = new CSSPropertyFree (ECSSProperty.FONT_SIZE_ADJUST);
  public static final ICSSProperty FONT_STRETCH = new CSSPropertyFree (ECSSProperty.FONT_STRETCH);
  public static final ICSSProperty FONT_SYNTHESIS = new CSSPropertyFree (ECSSProperty.FONT_SYNTHESIS);
  public static final ICSSProperty FONT_VARIANT_ALTERNATES = new CSSPropertyFree (ECSSProperty.FONT_VARIANT_ALTERNATES);
  public static final ICSSProperty FONT_VARIANT_CAPS = new CSSPropertyFree (ECSSProperty.FONT_VARIANT_CAPS);
  public static final ICSSProperty FONT_VARIANT_EAST_ASIAN = new CSSPropertyFree (ECSSProperty.FONT_VARIANT_EAST_ASIAN);
  public static final ICSSProperty FONT_VARIANT_LIGATURES = new CSSPropertyFree (ECSSProperty.FONT_VARIANT_LIGATURES);
  public static final ICSSProperty FONT_VARIANT_NUMERIC = new CSSPropertyFree (ECSSProperty.FONT_VARIANT_NUMERIC);
  public static final ICSSProperty FONT_VARIANT_POSITION = new CSSPropertyFree (ECSSProperty.FONT_VARIANT_POSITION);
  public static final ICSSProperty FOOTNOTE_DISPLAY = new CSSPropertyFree (ECSSProperty.FOOTNOTE_DISPLAY);
  public static final ICSSProperty FOOTNOTE_POLICY = new CSSPropertyFree (ECSSProperty.FOOTNOTE_POLICY);
  public static final ICSSProperty GRID = new CSSPropertyFree (ECSSProperty.GRID);
  public static final ICSSProperty GRID_AREA = new CSSPropertyFree (ECSSProperty.GRID_AREA);
  public static final ICSSProperty GRID_AUTO_COLUMNS = new CSSPropertyFree (ECSSProperty.GRID_AUTO_COLUMNS);
  public static final ICSSProperty GRID_AUTO_FLOW = new CSSPropertyFree (ECSSProperty.GRID_AUTO_FLOW);
  @Deprecated
  public static final ICSSProperty GRID_AUTO_POSITION = new CSSPropertyFree (ECSSProperty.GRID_AUTO_POSITION);
  public static final ICSSProperty GRID_AUTO_ROWS = new CSSPropertyFree (ECSSProperty.GRID_AUTO_ROWS);
  public static final ICSSProperty GRID_COLUMN = new CSSPropertyFree (ECSSProperty.GRID_COLUMN);
  public static final ICSSProperty GRID_COLUMN_END = new CSSPropertyFree (ECSSProperty.GRID_COLUMN_END);
  public static final ICSSProperty GRID_COLUMN_START = new CSSPropertyFree (ECSSProperty.GRID_COLUMN_START);
  @Deprecated
  public static final ICSSProperty GRID_COLUMNS = new CSSPropertyFree (ECSSProperty.GRID_COLUMNS);
  public static final ICSSProperty GRID_ROW = new CSSPropertyFree (ECSSProperty.GRID_ROW);
  public static final ICSSProperty GRID_ROW_END = new CSSPropertyFree (ECSSProperty.GRID_ROW_END);
  public static final ICSSProperty GRID_ROW_START = new CSSPropertyFree (ECSSProperty.GRID_ROW_START);
  @Deprecated
  public static final ICSSProperty GRID_ROWS = new CSSPropertyFree (ECSSProperty.GRID_ROWS);
  public static final ICSSProperty GRID_TEMPLATE = new CSSPropertyFree (ECSSProperty.GRID_TEMPLATE);
  public static final ICSSProperty GRID_TEMPLATE_AREAS = new CSSPropertyFree (ECSSProperty.GRID_TEMPLATE_AREAS);
  public static final ICSSProperty GRID_TEMPLATE_COLUMNS = new CSSPropertyFree (ECSSProperty.GRID_TEMPLATE_COLUMNS);
  public static final ICSSProperty GRID_TEMPLATE_ROWS = new CSSPropertyFree (ECSSProperty.GRID_TEMPLATE_ROWS);
  public static final ICSSProperty HANGING_PUNCTUATION = new CSSPropertyFree (ECSSProperty.HANGING_PUNCTUATION);
  public static final ICSSProperty HYPHENS = new CSSPropertyFree (ECSSProperty.HYPHENS);
  public static final ICSSProperty ICON = new CSSPropertyFree (ECSSProperty.ICON);
  public static final ICSSProperty IMAGE_ORIENTATION = new CSSPropertyFree (ECSSProperty.IMAGE_ORIENTATION);
  public static final ICSSProperty IMAGE_RESOLUTION = new CSSPropertyFree (ECSSProperty.IMAGE_RESOLUTION);
  public static final ICSSProperty IME_MODE = new CSSPropertyFree (ECSSProperty.IME_MODE);
  public static final ICSSProperty INLINE_BOX_ALIGN = new CSSPropertyFree (ECSSProperty.INLINE_BOX_ALIGN);
  public static final ICSSProperty ISOLATION = new CSSPropertyFree (ECSSProperty.ISOLATION);
  public static final ICSSProperty JUSITFY_CONTENT = new CSSPropertyFree (ECSSProperty.JUSITFY_CONTENT);
  public static final ICSSProperty JUSITFY_ITEMS = new CSSPropertyFree (ECSSProperty.JUSITFY_ITEMS);
  public static final ICSSProperty JUSITFY_SELF = new CSSPropertyFree (ECSSProperty.JUSITFY_SELF);
  public static final ICSSProperty LIGHTING_COLOR = new CSSPropertyFree (ECSSProperty.LIGHTING_COLOR);
  public static final ICSSProperty LINE_BREAK = new CSSPropertyFree (ECSSProperty.LINE_BREAK);
  public static final ICSSProperty LINE_GRID = new CSSPropertyFree (ECSSProperty.LINE_GRID);
  public static final ICSSProperty LINE_SNAP = new CSSPropertyFree (ECSSProperty.LINE_SNAP);
  public static final ICSSProperty LINE_STACKING = new CSSPropertyFree (ECSSProperty.LINE_STACKING);
  public static final ICSSProperty LINE_STACKING_RUBY = new CSSPropertyFree (ECSSProperty.LINE_STACKING_RUBY);
  public static final ICSSProperty LINE_STACKING_SHIFT = new CSSPropertyFree (ECSSProperty.LINE_STACKING_SHIFT);
  public static final ICSSProperty LINE_STACKING_STRATEGY = new CSSPropertyFree (ECSSProperty.LINE_STACKING_STRATEGY);
  public static final ICSSProperty LIST_STYLE = new CSSPropertyFree (ECSSProperty.LIST_STYLE);
  @Deprecated
  public static final ICSSProperty MARKS = new CSSPropertyFree (ECSSProperty.MARKS);
  @Deprecated
  public static final ICSSProperty MARQUEE_DIRECTION = new CSSPropertyFree (ECSSProperty.MARQUEE_DIRECTION);
  @Deprecated
  public static final ICSSProperty MARQUEE_PLAY_COUNT = new CSSPropertyFree (ECSSProperty.MARQUEE_PLAY_COUNT);
  @Deprecated
  public static final ICSSProperty MARQUEE_SPEED = new CSSPropertyFree (ECSSProperty.MARQUEE_SPEED);
  @Deprecated
  public static final ICSSProperty MARQUEE_STYLE = new CSSPropertyFree (ECSSProperty.MARQUEE_STYLE);
  public static final ICSSProperty MARKER_SIDE = new CSSPropertyFree (ECSSProperty.MARKER_SIDE);
  public static final ICSSProperty MASK = new CSSPropertyFree (ECSSProperty.MASK);
  @Deprecated
  public static final ICSSProperty MASK_BOX_IMAGE = new CSSPropertyFree (ECSSProperty.MASK_BOX_IMAGE);
  @Deprecated
  public static final ICSSProperty MASK_BOX_IMAGE_OUTSET = new CSSPropertyFree (ECSSProperty.MASK_BOX_IMAGE_OUTSET);
  @Deprecated
  public static final ICSSProperty MASK_BOX_IMAGE_REPEAT = new CSSPropertyFree (ECSSProperty.MASK_BOX_IMAGE_REPEAT);
  @Deprecated
  public static final ICSSProperty MASK_BOX_IMAGE_SLICE = new CSSPropertyFree (ECSSProperty.MASK_BOX_IMAGE_SLICE);
  @Deprecated
  public static final ICSSProperty MASK_BOX_IMAGE_SOURCE = new CSSPropertyFree (ECSSProperty.MASK_BOX_IMAGE_SOURCE);
  @Deprecated
  public static final ICSSProperty MASK_BOX_IMAGE_WIDTH = new CSSPropertyFree (ECSSProperty.MASK_BOX_IMAGE_WIDTH);
  public static final ICSSProperty MASK_BORDER = new CSSPropertyFree (ECSSProperty.MASK_BORDER);
  public static final ICSSProperty MASK_BORDER_MODE = new CSSPropertyFree (ECSSProperty.MASK_BORDER_MODE);
  public static final ICSSProperty MASK_BORDER_REPEAT = new CSSPropertyFree (ECSSProperty.MASK_BORDER_REPEAT);
  public static final ICSSProperty MASK_BORDER_SLICE = new CSSPropertyFree (ECSSProperty.MASK_BORDER_SLICE);
  public static final ICSSProperty MASK_BORDER_SOURCE = new CSSPropertyFree (ECSSProperty.MASK_BORDER_SOURCE);
  public static final ICSSProperty MASK_BORDER_WIDTH = new CSSPropertyFree (ECSSProperty.MASK_BORDER_WIDTH);
  public static final ICSSProperty MASK_CLIP = new CSSPropertyFree (ECSSProperty.MASK_CLIP);
  public static final ICSSProperty MASK_COMPOSITE = new CSSPropertyFree (ECSSProperty.MASK_COMPOSITE);
  public static final ICSSProperty MASK_IMAGE = new CSSPropertyFree (ECSSProperty.MASK_IMAGE);
  public static final ICSSProperty MASK_MODE = new CSSPropertyFree (ECSSProperty.MASK_MODE);
  public static final ICSSProperty MASK_ORIGIN = new CSSPropertyFree (ECSSProperty.MASK_ORIGIN);
  public static final ICSSProperty MASK_POSITION = new CSSPropertyFree (ECSSProperty.MASK_POSITION);
  public static final ICSSProperty MASK_REPEAT = new CSSPropertyFree (ECSSProperty.MASK_REPEAT);
  public static final ICSSProperty MASK_SIZE = new CSSPropertyFree (ECSSProperty.MASK_SIZE);
  @Deprecated
  public static final ICSSProperty MASK_SOURCE_TYPE = new CSSPropertyFree (ECSSProperty.MASK_SOURCE_TYPE);
  public static final ICSSProperty MASK_TYPE = new CSSPropertyFree (ECSSProperty.MASK_TYPE);
  public static final ICSSProperty MAX_LINES = new CSSPropertyFree (ECSSProperty.MAX_LINES);
  public static final ICSSProperty MAX_ZOOM = new CSSPropertyFree (ECSSProperty.MAX_ZOOM);
  public static final ICSSProperty MIN_ZOOM = new CSSPropertyFree (ECSSProperty.MIN_ZOOM);
  public static final ICSSProperty MIX_BLEND_MODE = new CSSPropertyFree (ECSSProperty.MIX_BLEND_MODE);
  public static final ICSSProperty NAV_DOWN = new CSSPropertyFree (ECSSProperty.NAV_DOWN);
  public static final ICSSProperty NAV_INDEX = new CSSPropertyFree (ECSSProperty.NAV_INDEX);
  public static final ICSSProperty NAV_LEFT = new CSSPropertyFree (ECSSProperty.NAV_LEFT);
  public static final ICSSProperty NAV_RIGHT = new CSSPropertyFree (ECSSProperty.NAV_RIGHT);
  public static final ICSSProperty NAV_UP = new CSSPropertyFree (ECSSProperty.NAV_UP);
  public static final ICSSProperty OBJECT_FIT = new CSSPropertyFree (ECSSProperty.OBJECT_FIT);
  public static final ICSSProperty OBJECT_POSITION = new CSSPropertyFree (ECSSProperty.OBJECT_POSITION);
  public static final ICSSProperty ORDER = new CSSPropertyFree (ECSSProperty.ORDER);
  public static final ICSSProperty ORIENTATION = new CSSPropertyFree (ECSSProperty.ORIENTATION);
  public static final ICSSProperty ORPHANS = new CSSPropertyFree (ECSSProperty.ORPHANS);
  @Deprecated
  public static final ICSSProperty OVERFLOW_STYLE = new CSSPropertyFree (ECSSProperty.OVERFLOW_STYLE);
  public static final ICSSProperty OVERFLOW_WRAP = new CSSPropertyFree (ECSSProperty.OVERFLOW_WRAP);
  public static final ICSSProperty OVERFLOW_X = new CSSPropertyFree (ECSSProperty.OVERFLOW_X);
  public static final ICSSProperty OVERFLOW_Y = new CSSPropertyFree (ECSSProperty.OVERFLOW_Y);
  public static final ICSSProperty PAGE_BREAK_AFTER = new CSSPropertyFree (ECSSProperty.PAGE_BREAK_AFTER);
  public static final ICSSProperty PAGE_BREAK_BEFORE = new CSSPropertyFree (ECSSProperty.PAGE_BREAK_BEFORE);
  public static final ICSSProperty PAGE_BREAK_INSIDE = new CSSPropertyFree (ECSSProperty.PAGE_BREAK_INSIDE);
  public static final ICSSProperty PAUSE = new CSSPropertyFree (ECSSProperty.PAUSE);
  public static final ICSSProperty PAUSE_AFTER = new CSSPropertyFree (ECSSProperty.PAUSE_AFTER);
  public static final ICSSProperty PAUSE_BEFORE = new CSSPropertyFree (ECSSProperty.PAUSE_BEFORE);
  public static final ICSSProperty PERSPECTIVE = new CSSPropertyFree (ECSSProperty.PERSPECTIVE);
  public static final ICSSProperty PERSPECTIVE_ORIGIN = new CSSPropertyFree (ECSSProperty.PERSPECTIVE_ORIGIN);
  public static final ICSSProperty PITCH = new CSSPropertyFree (ECSSProperty.PITCH);
  public static final ICSSProperty PITCH_RANGE = new CSSPropertyFree (ECSSProperty.PITCH_RANGE);
  public static final ICSSProperty PLAY_DURING = new CSSPropertyFree (ECSSProperty.PLAY_DURING);
  public static final ICSSProperty QUOTES = new CSSPropertyFree (ECSSProperty.QUOTES);
  public static final ICSSProperty REGION_FRAGMENT = new CSSPropertyFree (ECSSProperty.REGION_FRAGMENT);
  public static final ICSSProperty RESOLUTION = new CSSPropertyFree (ECSSProperty.RESOLUTION);
  public static final ICSSProperty REST = new CSSPropertyFree (ECSSProperty.REST);
  public static final ICSSProperty REST_AFTER = new CSSPropertyFree (ECSSProperty.REST_AFTER);
  public static final ICSSProperty REST_BEFORE = new CSSPropertyFree (ECSSProperty.REST_BEFORE);
  public static final ICSSProperty RICHNESS = new CSSPropertyFree (ECSSProperty.RICHNESS);
  public static final ICSSProperty RUBY_ALIGN = new CSSPropertyFree (ECSSProperty.RUBY_ALIGN);
  public static final ICSSProperty RUBY_MERGE = new CSSPropertyFree (ECSSProperty.RUBY_MERGE);
  @Deprecated
  public static final ICSSProperty RUBY_OVERHANG = new CSSPropertyFree (ECSSProperty.RUBY_OVERHANG);
  public static final ICSSProperty RUBY_POSITION = new CSSPropertyFree (ECSSProperty.RUBY_POSITION);
  @Deprecated
  public static final ICSSProperty RUBY_SPAN = new CSSPropertyFree (ECSSProperty.RUBY_SPAN);
  public static final ICSSProperty RUNNING = new CSSPropertyFree (ECSSProperty.RUNNING);
  public static final ICSSProperty SCROLL_BEHAVIOR = new CSSPropertyFree (ECSSProperty.SCROLL_BEHAVIOR);
  public static final ICSSProperty SHAPE_OUTSIDE = new CSSPropertyFree (ECSSProperty.SHAPE_OUTSIDE);
  public static final ICSSProperty SHAPE_IMAGE_THRESHOLD = new CSSPropertyFree (ECSSProperty.SHAPE_IMAGE_THRESHOLD);
  public static final ICSSProperty SHAPE_MARGIN = new CSSPropertyFree (ECSSProperty.SHAPE_MARGIN);
  public static final ICSSProperty SIZE = new CSSPropertyFree (ECSSProperty.SIZE);
  public static final ICSSProperty SPEAK = new CSSPropertyFree (ECSSProperty.SPEAK);
  public static final ICSSProperty SPEAK_AS = new CSSPropertyFree (ECSSProperty.SPEAK_AS);
  public static final ICSSProperty SPEAK_NUMERAL = new CSSPropertyFree (ECSSProperty.SPEAK_NUMERAL);
  public static final ICSSProperty SPEAK_PUNCTUATION = new CSSPropertyFree (ECSSProperty.SPEAK_PUNCTUATION);
  public static final ICSSProperty SPEECH_RATE = new CSSPropertyFree (ECSSProperty.SPEECH_RATE);
  public static final ICSSProperty SRC = new CSSPropertyFree (ECSSProperty.SRC);
  public static final ICSSProperty STRESS = new CSSPropertyFree (ECSSProperty.STRESS);
  public static final ICSSProperty STRING_SET = new CSSPropertyFree (ECSSProperty.STRING_SET);
  public static final ICSSProperty TAB_SIZE = new CSSPropertyFree (ECSSProperty.TAB_SIZE);
  @Deprecated
  public static final ICSSProperty TARGET = new CSSPropertyFree (ECSSProperty.TARGET);
  @Deprecated
  public static final ICSSProperty TARGET_NAME = new CSSPropertyFree (ECSSProperty.TARGET_NAME);
  @Deprecated
  public static final ICSSProperty TARGET_NEW = new CSSPropertyFree (ECSSProperty.TARGET_NEW);
  @Deprecated
  public static final ICSSProperty TARGET_POSITION = new CSSPropertyFree (ECSSProperty.TARGET_POSITION);
  public static final ICSSProperty TEXT_ALIGN_LAST = new CSSPropertyFree (ECSSProperty.TEXT_ALIGN_LAST);
  public static final ICSSProperty TEXT_DECORATION_COLOR = new CSSPropertyFree (ECSSProperty.TEXT_DECORATION_COLOR);
  public static final ICSSProperty TEXT_DECORATION_LINE = new CSSPropertyFree (ECSSProperty.TEXT_DECORATION_LINE);
  public static final ICSSProperty TEXT_DECORATION_SKIP = new CSSPropertyFree (ECSSProperty.TEXT_DECORATION_SKIP);
  public static final ICSSProperty TEXT_DECORATION_STYLE = new CSSPropertyFree (ECSSProperty.TEXT_DECORATION_STYLE);
  public static final ICSSProperty TEXT_EMPHASIS = new CSSPropertyFree (ECSSProperty.TEXT_EMPHASIS);
  public static final ICSSProperty TEXT_EMPHASIS_COLOR = new CSSPropertyFree (ECSSProperty.TEXT_EMPHASIS_COLOR);
  public static final ICSSProperty TEXT_EMPHASIS_POSITION = new CSSPropertyFree (ECSSProperty.TEXT_EMPHASIS_POSITION);
  public static final ICSSProperty TEXT_EMPHASIS_STYLE = new CSSPropertyFree (ECSSProperty.TEXT_EMPHASIS_STYLE);
  public static final ICSSProperty TEXT_HEIGHT = new CSSPropertyFree (ECSSProperty.TEXT_HEIGHT);
  public static final ICSSProperty TEXT_JUSTIFY = new CSSPropertyFree (ECSSProperty.TEXT_JUSTIFY);
  public static final ICSSProperty TEXT_SHADOW = new CSSPropertyFree (ECSSProperty.TEXT_SHADOW);
  public static final ICSSProperty TEXT_UNDERLINE_POSITION = new CSSPropertyFree (ECSSProperty.TEXT_UNDERLINE_POSITION);
  public static final ICSSProperty TOUCH_ACTION = new CSSPropertyFree (ECSSProperty.TOUCH_ACTION);
  public static final ICSSProperty TRANSFORM = new CSSPropertyFree (ECSSProperty.TRANSFORM);
  public static final ICSSProperty TRANSFORM_ORIGIN = new CSSPropertyFree (ECSSProperty.TRANSFORM_ORIGIN);
  public static final ICSSProperty TRANSFORM_STYLE = new CSSPropertyFree (ECSSProperty.TRANSFORM_STYLE);
  public static final ICSSProperty TRANSITION = new CSSPropertyFree (ECSSProperty.TRANSITION);
  public static final ICSSProperty TRANSITION_DELAY = new CSSPropertyFree (ECSSProperty.TRANSITION_DELAY);
  public static final ICSSProperty TRANSITION_DURATION = new CSSPropertyFree (ECSSProperty.TRANSITION_DURATION);
  public static final ICSSProperty TRANSITION_PROPERTY = new CSSPropertyFree (ECSSProperty.TRANSITION_PROPERTY);
  public static final ICSSProperty TRANSITION_TIMING_FUNCTION = new CSSPropertyFree (ECSSProperty.TRANSITION_TIMING_FUNCTION);
  public static final ICSSProperty UNICODE_RANGE = new CSSPropertyFree (ECSSProperty.UNICODE_RANGE);
  public static final ICSSProperty USER_ZOOM = new CSSPropertyFree (ECSSProperty.USER_ZOOM);
  public static final ICSSProperty VOICE_BALANCE = new CSSPropertyFree (ECSSProperty.VOICE_BALANCE);
  public static final ICSSProperty VOICE_DURATION = new CSSPropertyFree (ECSSProperty.VOICE_DURATION);
  public static final ICSSProperty VOICE_FAMILY = new CSSPropertyFree (ECSSProperty.VOICE_FAMILY);
  public static final ICSSProperty VOICE_PITCH = new CSSPropertyFree (ECSSProperty.VOICE_PITCH);
  public static final ICSSProperty VOICE_RANGE = new CSSPropertyFree (ECSSProperty.VOICE_RANGE);
  public static final ICSSProperty VOICE_RATE = new CSSPropertyFree (ECSSProperty.VOICE_RATE);
  public static final ICSSProperty VOICE_STRESS = new CSSPropertyFree (ECSSProperty.VOICE_STRESS);
  public static final ICSSProperty VOICE_VOLUME = new CSSPropertyFree (ECSSProperty.VOICE_VOLUME);
  public static final ICSSProperty VOLUME = new CSSPropertyFree (ECSSProperty.VOLUME);
  public static final ICSSProperty WIDOWS = new CSSPropertyFree (ECSSProperty.WIDOWS);
  public static final ICSSProperty WILL_CHANGE = new CSSPropertyFree (ECSSProperty.WILL_CHANGE);
  public static final ICSSProperty WORD_BREAK = new CSSPropertyFree (ECSSProperty.WORD_BREAK);
  public static final ICSSProperty WORD_WRAP = new CSSPropertyFree (ECSSProperty.WORD_WRAP);
  public static final ICSSProperty WRAP_FLOW = new CSSPropertyFree (ECSSProperty.WRAP_FLOW);
  public static final ICSSProperty WRAP_THROUGH = new CSSPropertyFree (ECSSProperty.WRAP_THROUGH);
  public static final ICSSProperty ZOOM = new CSSPropertyFree (ECSSProperty.ZOOM);

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
