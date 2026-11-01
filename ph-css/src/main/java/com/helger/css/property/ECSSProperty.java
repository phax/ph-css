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

import static com.helger.css.ECSSSpecification.*;

import java.util.EnumSet;
import java.util.Set;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.annotation.misc.ChangeNextMajorRelease;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.lang.EnumHelper;
import com.helger.base.name.IHasName;
import com.helger.base.string.StringHelper;
import com.helger.collection.helper.CollectionHelperExt;
import com.helger.css.ECSSSpecification;
import com.helger.css.ECSSVendorPrefix;

/**
 * Contains a list of most CSS property names.<br>
 * Source of Webkit property names:
 * http://trac.webkit.org/export/0/trunk/Source/WebCore/css/CSSPropertyNames.in <br>
 * <br>
 * MS specific property names:
 * http://msdn.microsoft.com/en-us/library/ie/hh772373%28v=vs.85%29.aspx<br>
 * http://blogs.msdn.com/b/ie/archive/2008/09/08/microsoft-css-vendor-extensions .aspx<br>
 * <br>
 * Mozilla specific property names:
 * https://developer.mozilla.org/en/CSS_Reference/Mozilla_Extensions<br>
 * <br>
 * CSS 3.0: see {@link ECSSSpecification}
 *
 * @author Philip Helger
 */
@SuppressWarnings ("deprecation")
public enum ECSSProperty implements IHasName
{
  ACCENT_COLOR ("accent-color", CSS_UI_4),
  ALIGN_CONTENT ("align-content", CSS_FLEXBOX_1, CSS_ALIGN_3),
  ALIGN_ITEMS ("align-items", CSS_FLEXBOX_1, CSS_ALIGN_3),
  ALIGN_SELF ("align-self", CSS_FLEXBOX_1, CSS_ALIGN_3),
  ALIGNMENT_BASELINE ("alignment-baseline", CSS_INLINE_3),
  ALL ("all", CSS_CASCADE_3),
  ANCHOR_NAME ("anchor-name", CSS_ANCHOR_POSITION_1),
  ANCHOR_SCOPE ("anchor-scope", CSS_ANCHOR_POSITION_1),
  ANIMATION ("animation", CSS3_ANIMATIONS),
  ANIMATION_COMPOSITION ("animation-composition", CSS_ANIMATIONS_2),
  ANIMATION_DELAY ("animation-delay", CSS3_ANIMATIONS),
  ANIMATION_DIRECTION ("animation-direction", CSS3_ANIMATIONS),
  ANIMATION_DURATION ("animation-duration", CSS3_ANIMATIONS),
  ANIMATION_FILL_MODE ("animation-fill-mode", CSS3_ANIMATIONS),
  ANIMATION_ITERATION_COUNT ("animation-iteration-count", CSS3_ANIMATIONS),
  ANIMATION_NAME ("animation-name", CSS3_ANIMATIONS),
  ANIMATION_PLAY_STATE ("animation-play-state", CSS3_ANIMATIONS),
  ANIMATION_RANGE ("animation-range", SCROLL_ANIMATIONS_1),
  ANIMATION_RANGE_END ("animation-range-end", SCROLL_ANIMATIONS_1),
  ANIMATION_RANGE_START ("animation-range-start", SCROLL_ANIMATIONS_1),
  ANIMATION_TIMELINE ("animation-timeline", CSS_ANIMATIONS_2),
  ANIMATION_TIMING_FUNCTION ("animation-timing-function", CSS3_ANIMATIONS),
  ANIMATION_TRIGGER ("animation-trigger", ANIMATION_TRIGGERS_1),
  APPEARANCE ("appearance", CSS_UI_4),
  ASPECT_RATIO ("aspect-ratio", CSS_SIZING_4),
  @Deprecated (forRemoval = false, since = "8.2.4")
  AZIMUTH ("azimuth", CSS2),
  BACKDROP_FILTER ("backdrop-filter", FILTER_EFFECTS_2),
  BACKFACE_VISIBILITY ("backface-visibility", CSS_TRANSFORMS_1),
  BACKGROUND ("background", CSS1, CSS2, CSS3_BACKGROUND),
  BACKGROUND_ATTACHMENT ("background-attachment", CSS1, CSS2, CSS3_BACKGROUND),
  BACKGROUND_BLEND_MODE ("background-blend-mode", COMPOSITING_1),
  BACKGROUND_CLIP ("background-clip", CSS3_BACKGROUND),
  BACKGROUND_COLOR ("background-color", CSS1, CSS2, CSS3_BACKGROUND),
  BACKGROUND_IMAGE ("background-image", CSS1, CSS2, CSS3_BACKGROUND),
  BACKGROUND_ORIGIN ("background-origin", CSS3_BACKGROUND),
  BACKGROUND_POSITION ("background-position", CSS1, CSS2, CSS3_BACKGROUND),
  BACKGROUND_POSITION_X ("background-position-x", CSS4_BACKGROUND),
  BACKGROUND_POSITION_Y ("background-position-y", CSS4_BACKGROUND),
  BACKGROUND_REPEAT ("background-repeat", CSS1, CSS2, CSS3_BACKGROUND),
  BACKGROUND_SIZE ("background-size", CSS3_BACKGROUND),
  BASELINE_SHIFT ("baseline-shift", CSS_INLINE_3),
  BASELINE_SOURCE ("baseline-source", CSS_INLINE_3),
  BLOCK_SIZE ("block-size", CSS_LOGICAL_1, CSS_SIZING_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  BOOKMARK_LABEL ("bookmark-label", CSS_GCPM_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  BOOKMARK_LEVEL ("bookmark-level", CSS_GCPM_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  BOOKMARK_STATE ("bookmark-state", CSS_GCPM_3),
  BORDER ("border", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_BLOCK ("border-block", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_BLOCK_COLOR ("border-block-color", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_BLOCK_END ("border-block-end", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_BLOCK_END_COLOR ("border-block-end-color", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_BLOCK_END_STYLE ("border-block-end-style", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_BLOCK_END_WIDTH ("border-block-end-width", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_BLOCK_START ("border-block-start", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_BLOCK_START_COLOR ("border-block-start-color", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_BLOCK_START_STYLE ("border-block-start-style", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_BLOCK_START_WIDTH ("border-block-start-width", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_BLOCK_STYLE ("border-block-style", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_BLOCK_WIDTH ("border-block-width", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_BOTTOM ("border-bottom", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_BOTTOM_COLOR ("border-bottom-color", CSS2, CSS3_BACKGROUND),
  BORDER_BOTTOM_LEFT_RADIUS ("border-bottom-left-radius", CSS3_BACKGROUND),
  BORDER_BOTTOM_RIGHT_RADIUS ("border-bottom-right-radius", CSS3_BACKGROUND),
  BORDER_BOTTOM_STYLE ("border-bottom-style", CSS2, CSS3_BACKGROUND),
  BORDER_BOTTOM_WIDTH ("border-bottom-width", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_COLLAPSE ("border-collapse", CSS2),
  BORDER_COLOR ("border-color", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_END_END_RADIUS ("border-end-end-radius", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_END_START_RADIUS ("border-end-start-radius", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_IMAGE ("border-image", CSS3_BACKGROUND),
  BORDER_IMAGE_OUTSET ("border-image-outset", CSS3_BACKGROUND),
  BORDER_IMAGE_REPEAT ("border-image-repeat", CSS3_BACKGROUND),
  BORDER_IMAGE_SLICE ("border-image-slice", CSS3_BACKGROUND),
  BORDER_IMAGE_SOURCE ("border-image-source", CSS3_BACKGROUND),
  BORDER_IMAGE_WIDTH ("border-image-width", CSS3_BACKGROUND),
  BORDER_INLINE ("border-inline", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_INLINE_COLOR ("border-inline-color", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_INLINE_END ("border-inline-end", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_INLINE_END_COLOR ("border-inline-end-color", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_INLINE_END_STYLE ("border-inline-end-style", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_INLINE_END_WIDTH ("border-inline-end-width", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_INLINE_START ("border-inline-start", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_INLINE_START_COLOR ("border-inline-start-color", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_INLINE_START_STYLE ("border-inline-start-style", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_INLINE_START_WIDTH ("border-inline-start-width", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_INLINE_STYLE ("border-inline-style", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_INLINE_WIDTH ("border-inline-width", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_LEFT ("border-left", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_LEFT_COLOR ("border-left-color", CSS2, CSS3_BACKGROUND),
  BORDER_LEFT_STYLE ("border-left-style", CSS2, CSS3_BACKGROUND),
  BORDER_LEFT_WIDTH ("border-left-width", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_RADIUS ("border-radius", CSS3_BACKGROUND),
  BORDER_RIGHT ("border-right", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_RIGHT_COLOR ("border-right-color", CSS2, CSS3_BACKGROUND),
  BORDER_RIGHT_STYLE ("border-right-style", CSS2, CSS3_BACKGROUND),
  BORDER_RIGHT_WIDTH ("border-right-width", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_SHAPE ("border-shape", CSS_BORDERS_4),
  BORDER_SPACING ("border-spacing", CSS2),
  BORDER_START_END_RADIUS ("border-start-end-radius", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_START_START_RADIUS ("border-start-start-radius", CSS_BORDERS_4, CSS_LOGICAL_1),
  BORDER_STYLE ("border-style", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_TOP ("border-top", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_TOP_COLOR ("border-top-color", CSS2, CSS3_BACKGROUND),
  BORDER_TOP_LEFT_RADIUS ("border-top-left-radius", CSS3_BACKGROUND),
  BORDER_TOP_RIGHT_RADIUS ("border-top-right-radius", CSS3_BACKGROUND),
  BORDER_TOP_STYLE ("border-top-style", CSS2, CSS3_BACKGROUND),
  BORDER_TOP_WIDTH ("border-top-width", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_WIDTH ("border-width", CSS1, CSS2, CSS3_BACKGROUND),
  BOTTOM ("bottom", CSS2, CSS_POSITION_3),
  BOX_DECORATION_BREAK ("box-decoration-break", CSS_BREAK_3),
  BOX_SHADOW ("box-shadow", CSS3_BACKGROUND),
  BOX_SIZING ("box-sizing", CSS_UI_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  BOX_SNAP ("box-snap", CSS_LINE_GRID_1),
  @Deprecated (forRemoval = false, since = "8.2.4")
  BOX_SUPPRESS ("box-suppress", CSS_DISPLAY_3),
  BREAK_AFTER ("break-after", CSS3_MULTICOL, CSS_BREAK_3, CSS_REGIONS_1),
  BREAK_BEFORE ("break-before", CSS3_MULTICOL, CSS_BREAK_3, CSS_REGIONS_1),
  BREAK_INSIDE ("break-inside", CSS3_MULTICOL, CSS_BREAK_3, CSS_REGIONS_1),
  CAPTION_SIDE ("caption-side", CSS2),
  CARET ("caret", CSS_UI_4),
  CARET_ANIMATION ("caret-animation", CSS_UI_4),
  CARET_COLOR ("caret-color", CSS_UI_3),
  CARET_SHAPE ("caret-shape", CSS_UI_4),
  @Deprecated (forRemoval = false, since = "8.2.4")
  CHAINS ("chains", CSS_TEMPLATE_3),
  CLEAR ("clear", CSS1, CSS2),
  CLIP ("clip", CSS2, CSS_POSITION_3, CSS_MASKING_1),
  CLIP_PATH ("clip-path", CSS_MASKING_1),
  CLIP_RULE ("clip-rule", CSS_MASKING_1),
  COLOR ("color", CSS1, CSS2, CSS3_COLOR),
  COLOR_INTERPOLATION_FILTERS ("color-interpolation-filters", FILTER_EFFECTS_1),
  COLOR_SCHEME ("color-scheme", CSS_COLOR_ADJUST_1),
  COLUMNS ("columns", CSS3_MULTICOL),
  @Deprecated (forRemoval = false, since = "8.2.4")
  COLUMNS_SPAN ("columns-span", CSS3_MULTICOL),
  @Deprecated (forRemoval = false, since = "8.2.4")
  COLUMNS_WIDTH ("columns-width", CSS3_MULTICOL),
  COLUMN_COUNT ("column-count", CSS3_MULTICOL),
  COLUMN_FILL ("column-fill", CSS3_MULTICOL),
  COLUMN_GAP ("column-gap", CSS3_MULTICOL),
  COLUMN_HEIGHT ("column-height", CSS3_MULTICOL),
  COLUMN_RULE ("column-rule", CSS3_MULTICOL),
  COLUMN_RULE_COLOR ("column-rule-color", CSS3_MULTICOL),
  COLUMN_RULE_STYLE ("column-rule-style", CSS3_MULTICOL),
  COLUMN_RULE_WIDTH ("column-rule-width", CSS3_MULTICOL),
  COLUMN_SPAN ("column-span", CSS3_MULTICOL),
  COLUMN_WIDTH ("column-width", CSS3_MULTICOL),
  COLUMN_WRAP ("column-wrap", CSS3_MULTICOL),
  CONTAIN ("contain", CSS_CONTAIN_1),
  CONTAINER ("container", CSS_CONDITIONAL_5),
  CONTAINER_NAME ("container-name", CSS_CONDITIONAL_5),
  CONTAINER_TYPE ("container-type", CSS_CONDITIONAL_5),
  CONTAIN_INTRINSIC_BLOCK_SIZE ("contain-intrinsic-block-size", CSS_SIZING_4),
  CONTAIN_INTRINSIC_HEIGHT ("contain-intrinsic-height", CSS_SIZING_4),
  CONTAIN_INTRINSIC_INLINE_SIZE ("contain-intrinsic-inline-size", CSS_SIZING_4),
  CONTAIN_INTRINSIC_SIZE ("contain-intrinsic-size", CSS_SIZING_4),
  CONTAIN_INTRINSIC_WIDTH ("contain-intrinsic-width", CSS_SIZING_4),
  CONTENT ("content", CSS2, CSS_UI_3),
  CONTENT_VISIBILITY ("content-visibility", CSS_CONTAIN_2),
  CORNER_BLOCK_END_SHAPE ("corner-block-end-shape", CSS_BORDERS_4),
  CORNER_BLOCK_START_SHAPE ("corner-block-start-shape", CSS_BORDERS_4),
  CORNER_BOTTOM_LEFT_SHAPE ("corner-bottom-left-shape", CSS_BORDERS_4),
  CORNER_BOTTOM_RIGHT_SHAPE ("corner-bottom-right-shape", CSS_BORDERS_4),
  CORNER_BOTTOM_SHAPE ("corner-bottom-shape", CSS_BORDERS_4),
  CORNER_END_END_SHAPE ("corner-end-end-shape", CSS_BORDERS_4),
  CORNER_END_START_SHAPE ("corner-end-start-shape", CSS_BORDERS_4),
  CORNER_INLINE_END_SHAPE ("corner-inline-end-shape", CSS_BORDERS_4),
  CORNER_INLINE_START_SHAPE ("corner-inline-start-shape", CSS_BORDERS_4),
  CORNER_LEFT_SHAPE ("corner-left-shape", CSS_BORDERS_4),
  CORNER_RIGHT_SHAPE ("corner-right-shape", CSS_BORDERS_4),
  CORNER_SHAPE ("corner-shape", CSS_BORDERS_4),
  CORNER_START_END_SHAPE ("corner-start-end-shape", CSS_BORDERS_4),
  CORNER_START_START_SHAPE ("corner-start-start-shape", CSS_BORDERS_4),
  CORNER_TOP_LEFT_SHAPE ("corner-top-left-shape", CSS_BORDERS_4),
  CORNER_TOP_RIGHT_SHAPE ("corner-top-right-shape", CSS_BORDERS_4),
  CORNER_TOP_SHAPE ("corner-top-shape", CSS_BORDERS_4),
  COUNTER_INCREMENT ("counter-increment", CSS2, CSS_LISTS_3),
  COUNTER_RESET ("counter-reset", CSS2, CSS_LISTS_3),
  COUNTER_SET ("counter-set", CSS_LISTS_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  CUE ("cue", CSS2, CSS3_SPEECH),
  @Deprecated (forRemoval = false, since = "8.2.4")
  CUE_AFTER ("cue-after", CSS2, CSS3_SPEECH),
  @Deprecated (forRemoval = false, since = "8.2.4")
  CUE_BEFORE ("cue-before", CSS2, CSS3_SPEECH),
  CURSOR ("cursor", CSS2, CSS_UI_3),
  CX ("cx", SVG2),
  CY ("cy", SVG2),
  D ("d", SVG2),
  DIRECTION ("direction", CSS2, CSS_WRITING_MODES_3),
  DISPLAY ("display", CSS1, CSS2, CSS_TEMPLATE_3, CSS_RUBY_1, CSS_DISPLAY_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  DISPLAY_INSIDE ("display-inside", CSS_DISPLAY_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  DISPLAY_LIST ("display-list", CSS_DISPLAY_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  DISPLAY_OUTSIDE ("display-outside", CSS_DISPLAY_3),
  DOMINANT_BASELINE ("dominant-baseline", CSS_INLINE_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  DROP_INITIAL_AFTER_ADJUST ("drop-initial-after-adjust", CSS_INLINE_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  DROP_INITIAL_AFTER_ALIGN ("drop-initial-after-align", CSS_INLINE_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  DROP_INITIAL_BEFORE_ADJUST ("drop-initial-before-adjust", CSS_INLINE_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  DROP_INITIAL_BEFORE_ALIGN ("drop-initial-before-align", CSS_INLINE_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  DROP_INITIAL_SIZE ("drop-initial-size", CSS_INLINE_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  DROP_INITIAL_VALUE ("drop-initial-value", CSS_INLINE_3),
  DYNAMIC_RANGE_LIMIT ("dynamic-range-limit", CSS_COLOR_HDR_1),
  @Deprecated (forRemoval = false, since = "8.2.4")
  ELEVATION ("elevation", CSS2),
  EMPTY_CELLS ("empty-cells", CSS2),
  FIELD_SIZING ("field-sizing", CSS_FORMS_1),
  FILL ("fill", SVG2, FILL_STROKE_3),
  FILL_OPACITY ("fill-opacity", SVG2, FILL_STROKE_3),
  FILL_RULE ("fill-rule", SVG2, FILL_STROKE_3),
  FILTER ("filter", FILTER_EFFECTS_1),
  FLEX ("flex", CSS_FLEXBOX_1),
  FLEX_BASIS ("flex-basis", CSS_FLEXBOX_1),
  FLEX_DIRECTION ("flex-direction", CSS_FLEXBOX_1),
  FLEX_FLOW ("flex-flow", CSS_FLEXBOX_1),
  FLEX_GROW ("flex-grow", CSS_FLEXBOX_1),
  FLEX_LINE_COUNT ("flex-line-count", CSS_FLEXBOX_1),
  FLEX_SHRINK ("flex-shrink", CSS_FLEXBOX_1),
  FLEX_WRAP ("flex-wrap", CSS_FLEXBOX_1),
  FLOAT ("float", CSS1, CSS2),
  FLOOD_COLOR ("flood-color", FILTER_EFFECTS_1),
  FLOOD_OPACITY ("flood-opacity", FILTER_EFFECTS_1),
  @Deprecated (forRemoval = false, since = "8.2.4")
  FLOW_FROM ("flow-from", CSS_REGIONS_1),
  @Deprecated (forRemoval = false, since = "8.2.4")
  FLOW_INTO ("flow-into", CSS_REGIONS_1),
  FONT ("font", CSS1, CSS2, CSS_FONTS_3),
  FONT_FAMILY ("font-family", CSS1, CSS2, CSS_FONTS_3),
  FONT_FEATURE_SETTINGS ("font-feature-settings", CSS_FONTS_3),
  FONT_KERNING ("font-kerning", CSS_FONTS_3),
  FONT_LANGUAGE_OVERRIDE ("font-language-override", CSS_FONTS_3),
  FONT_OPTICAL_SIZING ("font-optical-sizing", CSS_FONTS_4),
  FONT_PALETTE ("font-palette", CSS_FONTS_4),
  FONT_SIZE ("font-size", CSS1, CSS2, CSS_FONTS_3),
  FONT_SIZE_ADJUST ("font-size-adjust", CSS_FONTS_3),
  FONT_STRETCH ("font-stretch", CSS_FONTS_3),
  FONT_STYLE ("font-style", CSS1, CSS2, CSS_FONTS_3),
  FONT_SYNTHESIS ("font-synthesis", CSS_FONTS_3),
  FONT_SYNTHESIS_POSITION ("font-synthesis-position", CSS_FONTS_4),
  FONT_SYNTHESIS_SMALL_CAPS ("font-synthesis-small-caps", CSS_FONTS_4),
  FONT_SYNTHESIS_STYLE ("font-synthesis-style", CSS_FONTS_4),
  FONT_SYNTHESIS_WEIGHT ("font-synthesis-weight", CSS_FONTS_4),
  FONT_VARIANT ("font-variant", CSS1, CSS2, CSS_FONTS_3),
  FONT_VARIANT_ALTERNATES ("font-variant-alternates", CSS_FONTS_3),
  FONT_VARIANT_CAPS ("font-variant-caps", CSS_FONTS_3),
  FONT_VARIANT_EAST_ASIAN ("font-variant-east-asian", CSS_FONTS_3),
  FONT_VARIANT_EMOJI ("font-variant-emoji", CSS_FONTS_4),
  FONT_VARIANT_LIGATURES ("font-variant-ligatures", CSS_FONTS_3),
  FONT_VARIANT_NUMERIC ("font-variant-numeric", CSS_FONTS_3),
  FONT_VARIANT_POSITION ("font-variant-position", CSS_FONTS_3),
  FONT_VARIATION_SETTINGS ("font-variation-settings", CSS_FONTS_4),
  FONT_WEIGHT ("font-weight", CSS1, CSS2, CSS_FONTS_3),
  FONT_WIDTH ("font-width", CSS_FONTS_4),
  @Deprecated (forRemoval = false, since = "8.2.4")
  FOOTNOTE_DISPLAY ("footnote-display", CSS_GCPM_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  FOOTNOTE_POLICY ("footnote-policy", CSS_GCPM_3),
  FORCED_COLOR_ADJUST ("forced-color-adjust", CSS_COLOR_ADJUST_1),
  FRAME_SIZING ("frame-sizing", CSS_SIZING_4),
  GAP ("gap", CSS_GAPS_1),
  @Deprecated (forRemoval = false, since = "8.2.4")
  GLYPH_ORIENTATION_VERTICAL ("glyph-orientation-vertical", CSS_WRITING_MODES_3),
  GRID ("grid", CSS_GRID_1),
  GRID_AREA ("grid-area", CSS_GRID_1),
  GRID_AUTO_COLUMNS ("grid-auto-columns", CSS_GRID_1),
  GRID_AUTO_FLOW ("grid-auto-flow", CSS_GRID_1),
  GRID_AUTO_ROWS ("grid-auto-rows", CSS_GRID_1),
  GRID_COLUMN ("grid-column", CSS_GRID_1),
  GRID_COLUMN_END ("grid-column-end", CSS_GRID_1),
  GRID_COLUMN_START ("grid-column-start", CSS_GRID_1),
  GRID_ROW ("grid-row", CSS_GRID_1),
  GRID_ROW_END ("grid-row-end", CSS_GRID_1),
  GRID_ROW_START ("grid-row-start", CSS_GRID_1),
  GRID_TEMPLATE ("grid-template", CSS_GRID_1),
  GRID_TEMPLATE_AREAS ("grid-template-areas", CSS_GRID_1),
  GRID_TEMPLATE_COLUMNS ("grid-template-columns", CSS_GRID_1),
  GRID_TEMPLATE_ROWS ("grid-template-rows", CSS_GRID_1),
  HANGING_PUNCTUATION ("hanging-punctuation", CSS_TEXT_3),
  HEIGHT ("height", CSS1, CSS2, CSS_DEVICE_ADAPT_1),
  HYPHENATE_CHARACTER ("hyphenate-character", CSS_TEXT_4),
  HYPHENATE_LIMIT_CHARS ("hyphenate-limit-chars", CSS_TEXT_4),
  HYPHENS ("hyphens", CSS_TEXT_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  ICON ("icon", CSS_UI_3),
  IMAGE_ORIENTATION ("image-orientation", CSS3_IMAGES),
  IMAGE_RENDERING ("image-rendering", SVG2, CSS3_IMAGES),
  IMAGE_RESOLUTION ("image-resolution", CSS3_IMAGES),
  IME_MODE ("ime-mode", CSS_UI_3),
  INITIAL_LETTER ("initial-letter", CSS_INLINE_3),
  INITIAL_LETTER_ALIGN ("initial-letter-align", CSS_INLINE_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  INLINE_BOX_ALIGN ("inline-box-align", CSS_INLINE_3),
  INLINE_SIZE ("inline-size", SVG2, CSS_LOGICAL_1, CSS_SIZING_3),
  INSET ("inset", CSS_LOGICAL_1, CSS_POSITION_3),
  INSET_BLOCK ("inset-block", CSS_LOGICAL_1, CSS_POSITION_3),
  INSET_BLOCK_END ("inset-block-end", CSS_LOGICAL_1, CSS_POSITION_3),
  INSET_BLOCK_START ("inset-block-start", CSS_LOGICAL_1, CSS_POSITION_3),
  INSET_INLINE ("inset-inline", CSS_LOGICAL_1, CSS_POSITION_3),
  INSET_INLINE_END ("inset-inline-end", CSS_LOGICAL_1, CSS_POSITION_3),
  INSET_INLINE_START ("inset-inline-start", CSS_LOGICAL_1, CSS_POSITION_3),
  INTERACTIVITY ("interactivity", CSS_UI_4),
  INTEREST_DELAY ("interest-delay", CSS_UI_4),
  INTEREST_DELAY_END ("interest-delay-end", CSS_UI_4),
  INTEREST_DELAY_START ("interest-delay-start", CSS_UI_4),
  INTERPOLATE_SIZE ("interpolate-size", CSS_SIZING_4),
  ISOLATION ("isolation", COMPOSITING_1),
  JUSTIFY_CONTENT ("justify-content", CSS_FLEXBOX_1, CSS_ALIGN_3),
  JUSTIFY_ITEMS ("justify-items", CSS_ALIGN_3),
  JUSTIFY_SELF ("justify-self", CSS_ALIGN_3),
  LEFT ("left", CSS2, CSS_POSITION_3),
  LETTER_SPACING ("letter-spacing", CSS1, CSS2, CSS_TEXT_3),
  LIGHTING_COLOR ("lighting-color", FILTER_EFFECTS_1),
  LINE_BREAK ("line-break", CSS_TEXT_3),
  LINE_CLAMP ("line-clamp", CSS_OVERFLOW_4),
  @Deprecated (forRemoval = false, since = "8.2.4")
  LINE_GRID ("line-grid", CSS_LINE_GRID_1),
  LINE_HEIGHT ("line-height", CSS1, CSS2, CSS_INLINE_3),
  LINE_HEIGHT_STEP ("line-height-step", CSS_RHYTHM_1),
  @Deprecated (forRemoval = false, since = "8.2.4")
  LINE_SNAP ("line-snap", CSS_LINE_GRID_1),
  @Deprecated (forRemoval = false, since = "8.2.4")
  LINE_STACKING ("line-stacking", CSS_INLINE_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  LINE_STACKING_RUBY ("line-stacking-ruby", CSS_INLINE_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  LINE_STACKING_SHIFT ("line-stacking-shift", CSS_INLINE_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  LINE_STACKING_STRATEGY ("line-stacking-strategy", CSS_INLINE_3),
  LINK_PARAMETERS ("link-parameters", CSS_LINK_PARAMS_1),
  LIST_STYLE ("list-style", CSS1, CSS2, CSS_LISTS_3),
  LIST_STYLE_IMAGE ("list-style-image", CSS1, CSS2, CSS_LISTS_3),
  LIST_STYLE_POSITION ("list-style-position", CSS1, CSS2, CSS_LISTS_3),
  LIST_STYLE_TYPE ("list-style-type", CSS1, CSS2, CSS_LISTS_3),
  MARGIN ("margin", CSS1, CSS2),
  MARGIN_BLOCK ("margin-block", CSS_LOGICAL_1),
  MARGIN_BLOCK_END ("margin-block-end", CSS_LOGICAL_1),
  MARGIN_BLOCK_START ("margin-block-start", CSS_LOGICAL_1),
  MARGIN_BOTTOM ("margin-bottom", CSS1, CSS2),
  MARGIN_INLINE ("margin-inline", CSS_LOGICAL_1),
  MARGIN_INLINE_END ("margin-inline-end", CSS_LOGICAL_1),
  MARGIN_INLINE_START ("margin-inline-start", CSS_LOGICAL_1),
  MARGIN_LEFT ("margin-left", CSS1, CSS2),
  MARGIN_RIGHT ("margin-right", CSS1, CSS2),
  MARGIN_TOP ("margin-top", CSS1, CSS2),
  MARGIN_TRIM ("margin-trim", CSS_BOX_3),
  MARKER ("marker", SVG2),
  MARKER_END ("marker-end", SVG2),
  MARKER_MID ("marker-mid", SVG2),
  @Deprecated (forRemoval = false, since = "8.2.4")
  MARKER_SIDE ("marker-side", CSS_LISTS_3),
  MARKER_START ("marker-start", SVG2),
  MASK ("mask", CSS_MASKING_1),
  MASK_BORDER ("mask-border", CSS_MASKING_1),
  MASK_BORDER_MODE ("mask-border-mode", CSS_MASKING_1),
  MASK_BORDER_OUTSET ("mask-border-outset", CSS_MASKING_1),
  MASK_BORDER_REPEAT ("mask-border-repeat", CSS_MASKING_1),
  MASK_BORDER_SLICE ("mask-border-slice", CSS_MASKING_1),
  MASK_BORDER_SOURCE ("mask-border-source", CSS_MASKING_1),
  MASK_BORDER_WIDTH ("mask-border-width", CSS_MASKING_1),
  MASK_CLIP ("mask-clip", CSS_MASKING_1),
  MASK_COMPOSITE ("mask-composite", CSS_MASKING_1),
  MASK_IMAGE ("mask-image", CSS_MASKING_1),
  MASK_MODE ("mask-mode", CSS_MASKING_1),
  MASK_ORIGIN ("mask-origin", CSS_MASKING_1),
  MASK_POSITION ("mask-position", CSS_MASKING_1),
  MASK_REPEAT ("mask-repeat", CSS_MASKING_1),
  MASK_SIZE ("mask-size", CSS_MASKING_1),
  MASK_TYPE ("mask-type", CSS_MASKING_1),
  MATH_DEPTH ("math-depth", MATHML_CORE),
  MATH_SHIFT ("math-shift", MATHML_CORE),
  MATH_STYLE ("math-style", MATHML_CORE),
  MAX_BLOCK_SIZE ("max-block-size", CSS_LOGICAL_1, CSS_SIZING_3),
  MAX_HEIGHT ("max-height", CSS2, CSS_DEVICE_ADAPT_1),
  MAX_INLINE_SIZE ("max-inline-size", CSS_LOGICAL_1, CSS_SIZING_3),
  MAX_LINES ("max-lines", CSS_OVERFLOW_3),
  MAX_WIDTH ("max-width", CSS2, CSS_DEVICE_ADAPT_1),
  @Deprecated (forRemoval = false, since = "8.2.4")
  MAX_ZOOM ("max-zoom", CSS_DEVICE_ADAPT_1),
  MIN_BLOCK_SIZE ("min-block-size", CSS_LOGICAL_1, CSS_SIZING_3),
  MIN_HEIGHT ("min-height", CSS2, CSS_DEVICE_ADAPT_1),
  MIN_INLINE_SIZE ("min-inline-size", CSS_LOGICAL_1, CSS_SIZING_3),
  MIN_WIDTH ("min-width", CSS2, CSS_DEVICE_ADAPT_1),
  @Deprecated (forRemoval = false, since = "8.2.4")
  MIN_ZOOM ("min-zoom", CSS_DEVICE_ADAPT_1),
  MIX_BLEND_MODE ("mix-blend-mode", COMPOSITING_1),
  @Deprecated (forRemoval = false, since = "8.2.4")
  NAV_DOWN ("nav-down", CSS_UI_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  NAV_INDEX ("nav-index", CSS_UI_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  NAV_LEFT ("nav-left", CSS_UI_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  NAV_RIGHT ("nav-right", CSS_UI_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  NAV_UP ("nav-up", CSS_UI_3),
  OBJECT_FIT ("object-fit", CSS3_IMAGES),
  OBJECT_POSITION ("object-position", CSS3_IMAGES),
  OBJECT_VIEW_BOX ("object-view-box", CSS_IMAGES_5),
  OFFSET ("offset", MOTION_1),
  OFFSET_ANCHOR ("offset-anchor", MOTION_1),
  OFFSET_DISTANCE ("offset-distance", MOTION_1),
  OFFSET_PATH ("offset-path", MOTION_1),
  OFFSET_POSITION ("offset-position", MOTION_1),
  OFFSET_ROTATE ("offset-rotate", MOTION_1),
  OPACITY ("opacity", CSS3_COLOR),
  ORDER ("order", CSS_FLEXBOX_1),
  @Deprecated (forRemoval = false, since = "8.2.4")
  ORIENTATION ("orientation", CSS_DEVICE_ADAPT_1),
  ORPHANS ("orphans", CSS2, CSS_BREAK_3),
  OUTLINE ("outline", CSS2, CSS_UI_3),
  OUTLINE_COLOR ("outline-color", CSS2, CSS_UI_3),
  OUTLINE_OFFSET ("outline-offset", CSS_UI_3),
  OUTLINE_STYLE ("outline-style", CSS2, CSS_UI_3),
  OUTLINE_WIDTH ("outline-width", CSS2, CSS_UI_3),
  OVERFLOW ("overflow", CSS2, CSS_OVERFLOW_3),
  OVERFLOW_ANCHOR ("overflow-anchor", CSS_SCROLL_ANCHORING_1),
  OVERFLOW_BLOCK ("overflow-block", CSS_OVERFLOW_3),
  OVERFLOW_CLIP_MARGIN ("overflow-clip-margin", CSS_OVERFLOW_4, CSS_OVERFLOW_3),
  OVERFLOW_INLINE ("overflow-inline", CSS_OVERFLOW_3),
  OVERFLOW_WRAP ("overflow-wrap", CSS_TEXT_3),
  OVERFLOW_X ("overflow-x", CSS_OVERFLOW_3),
  OVERFLOW_Y ("overflow-y", CSS_OVERFLOW_3),
  OVERLAY ("overlay", CSS_POSITION_4),
  OVERSCROLL_BEHAVIOR ("overscroll-behavior", CSS_OVERSCROLL_1),
  OVERSCROLL_BEHAVIOR_BLOCK ("overscroll-behavior-block", CSS_OVERSCROLL_1),
  OVERSCROLL_BEHAVIOR_INLINE ("overscroll-behavior-inline", CSS_OVERSCROLL_1),
  OVERSCROLL_BEHAVIOR_X ("overscroll-behavior-x", CSS_OVERSCROLL_1),
  OVERSCROLL_BEHAVIOR_Y ("overscroll-behavior-y", CSS_OVERSCROLL_1),
  PADDING ("padding", CSS1, CSS2),
  PADDING_BLOCK ("padding-block", CSS_LOGICAL_1),
  PADDING_BLOCK_END ("padding-block-end", CSS_LOGICAL_1),
  PADDING_BLOCK_START ("padding-block-start", CSS_LOGICAL_1),
  PADDING_BOTTOM ("padding-bottom", CSS1, CSS2),
  PADDING_INLINE ("padding-inline", CSS_LOGICAL_1),
  PADDING_INLINE_END ("padding-inline-end", CSS_LOGICAL_1),
  PADDING_INLINE_START ("padding-inline-start", CSS_LOGICAL_1),
  PADDING_LEFT ("padding-left", CSS1, CSS2),
  PADDING_RIGHT ("padding-right", CSS1, CSS2),
  PADDING_TOP ("padding-top", CSS1, CSS2),
  PAGE ("page", CSS3_PAGE),
  PAGE_BREAK_AFTER ("page-break-after", CSS2),
  PAGE_BREAK_BEFORE ("page-break-before", CSS2),
  PAGE_BREAK_INSIDE ("page-break-inside", CSS2),
  PAINT_ORDER ("paint-order", SVG2),
  PATH_LENGTH ("path-length", SVG2),
  @Deprecated (forRemoval = false, since = "8.2.4")
  PAUSE ("pause", CSS2, CSS3_SPEECH),
  @Deprecated (forRemoval = false, since = "8.2.4")
  PAUSE_AFTER ("pause-after", CSS2, CSS3_SPEECH),
  @Deprecated (forRemoval = false, since = "8.2.4")
  PAUSE_BEFORE ("pause-before", CSS2, CSS3_SPEECH),
  PERSPECTIVE ("perspective", CSS_TRANSFORMS_1),
  PERSPECTIVE_ORIGIN ("perspective-origin", CSS_TRANSFORMS_1),
  @Deprecated (forRemoval = false, since = "8.2.4")
  PITCH ("pitch", CSS2),
  @Deprecated (forRemoval = false, since = "8.2.4")
  PITCH_RANGE ("pitch-range", CSS2),
  PLACE_CONTENT ("place-content", CSS_ALIGN_3),
  PLACE_ITEMS ("place-items", CSS_ALIGN_3),
  PLACE_SELF ("place-self", CSS_ALIGN_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  PLAY_DURING ("play-during", CSS2),
  POINTER_EVENTS ("pointer-events", SVG2, CSS_UI_4),
  POSITION ("position", CSS2, CSS_POSITION_3, CSS_TEMPLATE_3),
  POSITION_ANCHOR ("position-anchor", CSS_ANCHOR_POSITION_1),
  POSITION_AREA ("position-area", CSS_ANCHOR_POSITION_1),
  POSITION_TRY ("position-try", CSS_ANCHOR_POSITION_1),
  POSITION_TRY_FALLBACKS ("position-try-fallbacks", CSS_ANCHOR_POSITION_1),
  POSITION_TRY_ORDER ("position-try-order", CSS_ANCHOR_POSITION_1),
  POSITION_VISIBILITY ("position-visibility", CSS_ANCHOR_POSITION_1),
  PRINT_COLOR_ADJUST ("print-color-adjust", CSS_COLOR_ADJUST_1),
  QUOTES ("quotes", CSS2),
  R ("r", SVG2),
  READING_FLOW ("reading-flow", CSS_DISPLAY_3),
  READING_ORDER ("reading-order", CSS_DISPLAY_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  REGION_FRAGMENT ("region-fragment", CSS_REGIONS_1),
  RESIZE ("resize", CSS_UI_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  RESOLUTION ("resolution", CSS_DEVICE_ADAPT_1),
  @Deprecated (forRemoval = false, since = "8.2.4")
  REST ("rest", CSS3_SPEECH),
  @Deprecated (forRemoval = false, since = "8.2.4")
  REST_AFTER ("rest-after", CSS3_SPEECH),
  @Deprecated (forRemoval = false, since = "8.2.4")
  REST_BEFORE ("rest-before", CSS3_SPEECH),
  @Deprecated (forRemoval = false, since = "8.2.4")
  RICHNESS ("richness", CSS2),
  RIGHT ("right", CSS2, CSS_POSITION_3),
  ROTATE ("rotate", CSS_TRANSFORMS_2),
  ROW_GAP ("row-gap", CSS_GAPS_1),
  RUBY_ALIGN ("ruby-align", CSS_RUBY_1),
  RUBY_MERGE ("ruby-merge", CSS_RUBY_1),
  RUBY_OVERHANG ("ruby-overhang", CSS_RUBY_1),
  RUBY_POSITION ("ruby-position", CSS_RUBY_1),
  @Deprecated (forRemoval = false, since = "8.2.4")
  RUNNING ("running", CSS_GCPM_3),
  RX ("rx", SVG2),
  RY ("ry", SVG2),
  SCALE ("scale", CSS_TRANSFORMS_2),
  SCROLLBAR_COLOR ("scrollbar-color", CSS_SCROLLBARS_1),
  SCROLLBAR_GUTTER ("scrollbar-gutter", CSS_OVERFLOW_3),
  SCROLLBAR_WIDTH ("scrollbar-width", CSS_SCROLLBARS_1),
  SCROLL_AXIS_LOCK ("scroll-axis-lock", CSS_OVERFLOW_5),
  SCROLL_BEHAVIOR ("scroll-behavior", CSSOM_VIEW_1),
  SCROLL_INITIAL_TARGET ("scroll-initial-target", CSS_SCROLL_SNAP_2),
  SCROLL_MARGIN ("scroll-margin", CSS_SCROLL_SNAP_1),
  SCROLL_MARGIN_BLOCK ("scroll-margin-block", CSS_SCROLL_SNAP_1),
  SCROLL_MARGIN_BLOCK_END ("scroll-margin-block-end", CSS_SCROLL_SNAP_1),
  SCROLL_MARGIN_BLOCK_START ("scroll-margin-block-start", CSS_SCROLL_SNAP_1),
  SCROLL_MARGIN_BOTTOM ("scroll-margin-bottom", CSS_SCROLL_SNAP_1),
  SCROLL_MARGIN_INLINE ("scroll-margin-inline", CSS_SCROLL_SNAP_1),
  SCROLL_MARGIN_INLINE_END ("scroll-margin-inline-end", CSS_SCROLL_SNAP_1),
  SCROLL_MARGIN_INLINE_START ("scroll-margin-inline-start", CSS_SCROLL_SNAP_1),
  SCROLL_MARGIN_LEFT ("scroll-margin-left", CSS_SCROLL_SNAP_1),
  SCROLL_MARGIN_RIGHT ("scroll-margin-right", CSS_SCROLL_SNAP_1),
  SCROLL_MARGIN_TOP ("scroll-margin-top", CSS_SCROLL_SNAP_1),
  SCROLL_MARKER_GROUP ("scroll-marker-group", CSS_OVERFLOW_5),
  SCROLL_PADDING ("scroll-padding", CSS_SCROLL_SNAP_1),
  SCROLL_PADDING_BLOCK ("scroll-padding-block", CSS_SCROLL_SNAP_1),
  SCROLL_PADDING_BLOCK_END ("scroll-padding-block-end", CSS_SCROLL_SNAP_1),
  SCROLL_PADDING_BLOCK_START ("scroll-padding-block-start", CSS_SCROLL_SNAP_1),
  SCROLL_PADDING_BOTTOM ("scroll-padding-bottom", CSS_SCROLL_SNAP_1),
  SCROLL_PADDING_INLINE ("scroll-padding-inline", CSS_SCROLL_SNAP_1),
  SCROLL_PADDING_INLINE_END ("scroll-padding-inline-end", CSS_SCROLL_SNAP_1),
  SCROLL_PADDING_INLINE_START ("scroll-padding-inline-start", CSS_SCROLL_SNAP_1),
  SCROLL_PADDING_LEFT ("scroll-padding-left", CSS_SCROLL_SNAP_1),
  SCROLL_PADDING_RIGHT ("scroll-padding-right", CSS_SCROLL_SNAP_1),
  SCROLL_PADDING_TOP ("scroll-padding-top", CSS_SCROLL_SNAP_1),
  SCROLL_SNAP_ALIGN ("scroll-snap-align", CSS_SCROLL_SNAP_1),
  SCROLL_SNAP_STOP ("scroll-snap-stop", CSS_SCROLL_SNAP_1),
  SCROLL_SNAP_TYPE ("scroll-snap-type", CSS_SCROLL_SNAP_1),
  SCROLL_TARGET_GROUP ("scroll-target-group", CSS_OVERFLOW_5),
  SCROLL_TIMELINE ("scroll-timeline", SCROLL_ANIMATIONS_1),
  SCROLL_TIMELINE_AXIS ("scroll-timeline-axis", SCROLL_ANIMATIONS_1),
  SCROLL_TIMELINE_NAME ("scroll-timeline-name", SCROLL_ANIMATIONS_1),
  SHAPE_OUTSIDE ("shape-outside", CSS_SHAPES_1),
  SHAPE_IMAGE_THRESHOLD ("shape-image-threshold", CSS_SHAPES_1),
  SHAPE_MARGIN ("shape-margin", CSS_SHAPES_1),
  SHAPE_RENDERING ("shape-rendering", SVG2),
  SIZE ("size", CSS3_PAGE),
  @Deprecated (forRemoval = false, since = "8.2.4")
  SPEAK ("speak", CSS2, CSS3_SPEECH),
  SPEAK_AS ("speak-as", CSS3_SPEECH),
  @Deprecated (forRemoval = false, since = "8.2.4")
  SPEAK_HEADER ("speak-header", CSS2),
  @Deprecated (forRemoval = false, since = "8.2.4")
  SPEAK_NUMERAL ("speak-numeral", CSS2),
  @Deprecated (forRemoval = false, since = "8.2.4")
  SPEAK_PUNCTUATION ("speak-punctuation", CSS2),
  @Deprecated (forRemoval = false, since = "8.2.4")
  SPEECH_RATE ("speech-rate", CSS2),
  SRC ("src", CSS_FONTS_3),
  STOP_COLOR ("stop-color", SVG2),
  STOP_OPACITY ("stop-opacity", SVG2),
  @Deprecated (forRemoval = false, since = "8.2.4")
  STRESS ("stress", CSS2),
  @Deprecated (forRemoval = false, since = "8.2.4")
  STRING_SET ("string-set", CSS_GCPM_3),
  STROKE ("stroke", SVG2, FILL_STROKE_3, SVG_STROKES),
  STROKE_COLOR ("stroke-color", FILL_STROKE_3),
  STROKE_DASHARRAY ("stroke-dasharray", SVG2, FILL_STROKE_3, SVG_STROKES),
  STROKE_DASHOFFSET ("stroke-dashoffset", SVG2, FILL_STROKE_3, SVG_STROKES),
  STROKE_LINECAP ("stroke-linecap", SVG2, FILL_STROKE_3, SVG_STROKES),
  STROKE_LINEJOIN ("stroke-linejoin", SVG2, FILL_STROKE_3, SVG_STROKES),
  STROKE_MITERLIMIT ("stroke-miterlimit", SVG2, FILL_STROKE_3, SVG_STROKES),
  STROKE_OPACITY ("stroke-opacity", SVG2, FILL_STROKE_3, SVG_STROKES),
  STROKE_WIDTH ("stroke-width", SVG2, FILL_STROKE_3, SVG_STROKES),
  TABLE_LAYOUT ("table-layout", CSS2),
  TAB_SIZE ("tab-size", CSS_TEXT_3),
  TEXT_ALIGN ("text-align", CSS1, CSS2, CSS_TEXT_3),
  TEXT_ALIGN_LAST ("text-align-last", CSS_TEXT_3),
  TEXT_ANCHOR ("text-anchor", SVG2),
  TEXT_AUTOSPACE ("text-autospace", CSS_TEXT_4),
  TEXT_BOX ("text-box", CSS_INLINE_3),
  TEXT_BOX_EDGE ("text-box-edge", CSS_INLINE_3),
  TEXT_BOX_TRIM ("text-box-trim", CSS_INLINE_3),
  TEXT_COMBINE_UPRIGHT ("text-combine-upright", CSS_WRITING_MODES_3),
  TEXT_DECORATION ("text-decoration", CSS1, CSS2, CSS_TEXT_DECOR_3),
  TEXT_DECORATION_COLOR ("text-decoration-color", CSS_TEXT_DECOR_3),
  TEXT_DECORATION_INSET ("text-decoration-inset", CSS_TEXT_DECOR_4),
  TEXT_DECORATION_LINE ("text-decoration-line", CSS_TEXT_DECOR_3),
  TEXT_DECORATION_SKIP ("text-decoration-skip", CSS_TEXT_DECOR_3),
  TEXT_DECORATION_SKIP_INK ("text-decoration-skip-ink", CSS_TEXT_DECOR_4),
  TEXT_DECORATION_STYLE ("text-decoration-style", CSS_TEXT_DECOR_3),
  TEXT_DECORATION_THICKNESS ("text-decoration-thickness", CSS_TEXT_DECOR_4),
  TEXT_EMPHASIS ("text-emphasis", CSS_TEXT_DECOR_3),
  TEXT_EMPHASIS_COLOR ("text-emphasis-color", CSS_TEXT_DECOR_3),
  TEXT_EMPHASIS_POSITION ("text-emphasis-position", CSS_TEXT_DECOR_3),
  TEXT_EMPHASIS_STYLE ("text-emphasis-style", CSS_TEXT_DECOR_3),
  TEXT_FIT ("text-fit", CSS_TEXT_5),
  @Deprecated (forRemoval = false, since = "8.2.4")
  TEXT_HEIGHT ("text-height", CSS_INLINE_3),
  TEXT_INDENT ("text-indent", CSS1, CSS2, CSS_TEXT_3),
  TEXT_JUSTIFY ("text-justify", CSS_TEXT_3),
  TEXT_ORIENTATION ("text-orientation", CSS_WRITING_MODES_3),
  TEXT_OVERFLOW ("text-overflow", CSS_UI_3),
  TEXT_RENDERING ("text-rendering", SVG2),
  TEXT_SHADOW ("text-shadow", CSS_TEXT_DECOR_3),
  TEXT_SIZE_ADJUST ("text-size-adjust", CSS_SIZE_ADJUST_1),
  TEXT_SPACING_TRIM ("text-spacing-trim", CSS_TEXT_4),
  TEXT_TRANSFORM ("text-transform", CSS1, CSS2, CSS_TEXT_3),
  TEXT_UNDERLINE_OFFSET ("text-underline-offset", CSS_TEXT_DECOR_4),
  TEXT_UNDERLINE_POSITION ("text-underline-position", CSS_TEXT_DECOR_3),
  TEXT_WRAP ("text-wrap", CSS_TEXT_4),
  TEXT_WRAP_MODE ("text-wrap-mode", CSS_TEXT_4),
  TEXT_WRAP_STYLE ("text-wrap-style", CSS_TEXT_4),
  TIMELINE_SCOPE ("timeline-scope", SCROLL_ANIMATIONS_1),
  TIMELINE_TRIGGER ("timeline-trigger", ANIMATION_TRIGGERS_1),
  TIMELINE_TRIGGER_ACTIVATION_RANGE ("timeline-trigger-activation-range", ANIMATION_TRIGGERS_1),
  TIMELINE_TRIGGER_ACTIVATION_RANGE_END ("timeline-trigger-activation-range-end", ANIMATION_TRIGGERS_1),
  TIMELINE_TRIGGER_ACTIVATION_RANGE_START ("timeline-trigger-activation-range-start", ANIMATION_TRIGGERS_1),
  TIMELINE_TRIGGER_ACTIVE_RANGE ("timeline-trigger-active-range", ANIMATION_TRIGGERS_1),
  TIMELINE_TRIGGER_ACTIVE_RANGE_END ("timeline-trigger-active-range-end", ANIMATION_TRIGGERS_1),
  TIMELINE_TRIGGER_ACTIVE_RANGE_START ("timeline-trigger-active-range-start", ANIMATION_TRIGGERS_1),
  TIMELINE_TRIGGER_NAME ("timeline-trigger-name", ANIMATION_TRIGGERS_1),
  TIMELINE_TRIGGER_SOURCE ("timeline-trigger-source", ANIMATION_TRIGGERS_1),
  TOP ("top", CSS2, CSS_POSITION_3),
  TOUCH_ACTION ("touch-action", OUTSIDE_CSS),
  TRANSFORM ("transform", CSS_TRANSFORMS_1),
  TRANSFORM_BOX ("transform-box", CSS_TRANSFORMS_1),
  TRANSFORM_ORIGIN ("transform-origin", CSS_TRANSFORMS_1),
  TRANSFORM_STYLE ("transform-style", CSS_TRANSFORMS_1),
  TRANSITION ("transition", CSS3_TRANSITIONS),
  TRANSITION_BEHAVIOR ("transition-behavior", CSS_TRANSITIONS_2),
  TRANSITION_DELAY ("transition-delay", CSS3_TRANSITIONS),
  TRANSITION_DURATION ("transition-duration", CSS3_TRANSITIONS),
  TRANSITION_PROPERTY ("transition-property", CSS3_TRANSITIONS),
  TRANSITION_TIMING_FUNCTION ("transition-timing-function", CSS3_TRANSITIONS),
  TRANSLATE ("translate", CSS_TRANSFORMS_2),
  TRIGGER_SCOPE ("trigger-scope", ANIMATION_TRIGGERS_1),
  UNICODE_BIDI ("unicode-bidi", CSS2, CSS_WRITING_MODES_3),
  UNICODE_RANGE ("unicode-range", CSS_FONTS_3),
  USER_SELECT ("user-select", CSS_UI_4),
  @Deprecated (forRemoval = false, since = "8.2.4")
  USER_ZOOM ("user-zoom", CSS_DEVICE_ADAPT_1),
  VECTOR_EFFECT ("vector-effect", SVG2),
  VERTICAL_ALIGN ("vertical-align", CSS1, CSS2, CSS_INLINE_3),
  VIEW_TIMELINE ("view-timeline", SCROLL_ANIMATIONS_1),
  VIEW_TIMELINE_AXIS ("view-timeline-axis", SCROLL_ANIMATIONS_1),
  VIEW_TIMELINE_INSET ("view-timeline-inset", SCROLL_ANIMATIONS_1),
  VIEW_TIMELINE_NAME ("view-timeline-name", SCROLL_ANIMATIONS_1),
  VIEW_TRANSITION_CLASS ("view-transition-class", CSS_VIEW_TRANSITIONS_1),
  VIEW_TRANSITION_NAME ("view-transition-name", CSS_VIEW_TRANSITIONS_1),
  VIEW_TRANSITION_SCOPE ("view-transition-scope", CSS_VIEW_TRANSITIONS_1),
  VISIBILITY ("visibility", CSS2),
  @Deprecated (forRemoval = false, since = "8.2.4")
  VOICE_BALANCE ("voice-balance", CSS3_SPEECH),
  @Deprecated (forRemoval = false, since = "8.2.4")
  VOICE_DURATION ("voice-duration", CSS3_SPEECH),
  @Deprecated (forRemoval = false, since = "8.2.4")
  VOICE_FAMILY ("voice-family", CSS2, CSS3_SPEECH),
  @Deprecated (forRemoval = false, since = "8.2.4")
  VOICE_PITCH ("voice-pitch", CSS3_SPEECH),
  @Deprecated (forRemoval = false, since = "8.2.4")
  VOICE_RANGE ("voice-range", CSS3_SPEECH),
  @Deprecated (forRemoval = false, since = "8.2.4")
  VOICE_RATE ("voice-rate", CSS3_SPEECH),
  @Deprecated (forRemoval = false, since = "8.2.4")
  VOICE_STRESS ("voice-stress", CSS3_SPEECH),
  @Deprecated (forRemoval = false, since = "8.2.4")
  VOICE_VOLUME ("voice-volume", CSS3_SPEECH),
  @Deprecated (forRemoval = false, since = "8.2.4")
  VOLUME ("volume", CSS2),
  WHITE_SPACE ("white-space", CSS1, CSS2, CSS_TEXT_3),
  WHITE_SPACE_COLLAPSE ("white-space-collapse", CSS_TEXT_4),
  WIDOWS ("widows", CSS2, CSS_BREAK_3),
  WIDTH ("width", CSS1, CSS2, CSS_DEVICE_ADAPT_1),
  WILL_CHANGE ("will-change", CSS_WILL_CHANGE_1),
  WINDOW_DRAG ("window-drag", CSS_UI_4),
  WORD_BREAK ("word-break", CSS_TEXT_3),
  WORD_SPACING ("word-spacing", CSS1, CSS2, CSS_TEXT_3),
  // For legacy reasons, UAs must treat 'word-wrap' as an alternate name for the
  // 'overflow-wrap' property, as if it were a shorthand of 'overflow-wrap'.
  WORD_WRAP ("word-wrap", CSS_TEXT_3),
  @Deprecated (forRemoval = false, since = "8.2.4")
  WRAP_FLOW ("wrap-flow", CSS3_EXCLUSIONS),
  @Deprecated (forRemoval = false, since = "8.2.4")
  WRAP_THROUGH ("wrap-through", CSS3_EXCLUSIONS),
  WRITING_MODE ("writing-mode", CSS_WRITING_MODES_3),
  X ("x", SVG2),
  Y ("y", SVG2),
  ZOOM ("zoom", CSS_DEVICE_ADAPT_1),
  Z_INDEX ("z-index", CSS2, CSS_POSITION_3),

  // Do not use the following manually:
  _MOZ_BACKGROUND_INLINE_POLICY ("-moz-background-inline-policy"),
  _MOZ_BINDING ("-moz-binding"),
  _MOZ_BORDER_BOTTOM_COLORS ("-moz-border-bottom-colors"),
  _MOZ_BORDER_END ("-moz-border-end"),
  _MOZ_BORDER_END_COLOR ("-moz-border-end-color"),
  _MOZ_BORDER_END_STYLE ("-moz-border-end-style"),
  _MOZ_BORDER_END_WIDTH ("-moz-border-end-width"),
  _MOZ_BORDER_LEFT_COLORS ("-moz-border-left-colors"),
  _MOZ_BORDER_RADIUS_BOTTOMLEFT ("-moz-border-radius-bottomleft"),
  _MOZ_BORDER_RADIUS_BOTTOMRIGHT ("-moz-border-radius-bottomright"),
  _MOZ_BORDER_RADIUS_TOPLEFT ("-moz-border-radius-topleft"),
  _MOZ_BORDER_RADIUS_TOPRIGHT ("-moz-border-radius-topright"),
  _MOZ_BORDER_RIGHT_COLORS ("-moz-border-right-colors"),
  _MOZ_BORDER_START ("-moz-border-start"),
  _MOZ_BORDER_START_COLOR ("-moz-border-start-color"),
  _MOZ_BORDER_START_STYLE ("-moz-border-start-style"),
  _MOZ_BORDER_START_WIDTH ("-moz-border-start-width"),
  _MOZ_BORDER_TOP_COLORS ("-moz-border-top-colors"),
  _MOZ_BOX_ALIGN ("-moz-box-align"),
  _MOZ_BOX_DIRECTION ("-moz-box-direction"),
  _MOZ_BOX_FLEX ("-moz-box-flex"),
  _MOZ_BOX_FLEXGROUP ("-moz-box-flexgroup"),
  _MOZ_BOX_ORDINAL_GROUP ("-moz-box-ordinal-group"),
  _MOZ_BOX_ORIENT ("-moz-box-orient"),
  _MOZ_BOX_PACK ("-moz-box-pack"),
  _MOZ_FLOAT_EDGE ("-moz-float-edge"),
  _MOZ_FORCE_BROKEN_IMAGE_ICON ("-moz-force-broken-image-icon"),
  _MOZ_IMAGE_REGION ("-moz-image-region"),
  _MOZ_MARGIN_END ("-moz-margin-end"),
  _MOZ_MARGIN_START ("-moz-margin-start"),
  _MOZ_OUTLINE_RADIUS ("-moz-outline-radius"),
  _MOZ_OUTLINE_RADIUS_BOTTOMLEFT ("-moz-outline-radius-bottomleft"),
  _MOZ_OUTLINE_RADIUS_BOTTOMRIGHT ("-moz-outline-radius-bottomright"),
  _MOZ_OUTLINE_RADIUS_TOPLEFT ("-moz-outline-radius-topleft"),
  _MOZ_OUTLINE_RADIUS_TOPRIGHT ("-moz-outline-radius-topright"),
  _MOZ_PADDING_END ("-moz-padding-end"),
  _MOZ_PADDING_START ("-moz-padding-start"),
  _MOZ_STACK_SIZING ("-moz-stack-sizing"),
  _MOZ_TEXT_BLINK ("-moz-text-blink"),
  _MOZ_USER_FOCUS ("-moz-user-focus"),
  _MOZ_USER_INPUT ("-moz-user-input"),
  _MOZ_USER_MODIFY ("-moz-user-modify"),
  _MOZ_USER_SELECT ("-moz-user-select"),
  _MOZ_WINDOW_SHADOW ("-moz-window-shadow"),

  _MS_ACCELERATOR ("-ms-accelerator"),
  _MS_BACKGROUND_POSITION_X ("-ms-background-position-x"),
  _MS_BACKGROUND_POSITION_Y ("-ms-background-position-y"),
  _MS_BEHAVIOR ("-ms-behavior"),
  _MS_BLOCK_PROGRESSION ("-ms-block-progression"),
  _MS_FULLSCREEN ("-ms-fullscreen"),
  _MS_INTERPOLATION_MODE ("-ms-interpolation-mode"),
  _MS_LAYOUT_FLOW ("-ms-layout-flow"),
  _MS_LAYOUT_GRID ("-ms-layout-grid"),
  _MS_LAYOUT_GRID_CHAR ("-ms-layout-grid-char"),
  _MS_LAYOUT_GRID_LINE ("-ms-layout-grid-line"),
  _MS_LAYOUT_GRID_MODE ("-ms-layout-grid-mode"),
  _MS_LAYOUT_GRID_TYPE ("-ms-layout-grid-type"),
  _MS_LINE_GRID_MODE ("-ms-line-grid-mode"),
  _MS_TEXT_AUTOSPACE ("-ms-text-autospace"),
  _MS_TEXT_KASHIDA_SPACE ("-ms-text-kashida-space"),
  _MS_TEXT_SIZE_ADJUST ("-ms-text-size-adjust"),

  _EPUB_TEXT_COMBINE ("-epub-text-combine"),

  _WEBKIT_APP_REGION ("-webkit-app-region"),
  _WEBKIT_ASPECT_RATIO ("-webkit-aspect-ratio"),
  _WEBKIT_BACKGROUND_COMPOSITE ("-webkit-background-composite"),
  _WEBKIT_BLEND_MODE ("-webkit-blend-mode"),
  _WEBKIT_BORDER_AFTER ("-webkit-border-after"),
  _WEBKIT_BORDER_AFTER_COLOR ("-webkit-border-after-color"),
  _WEBKIT_BORDER_AFTER_STYLE ("-webkit-border-after-style"),
  _WEBKIT_BORDER_AFTER_WIDTH ("-webkit-border-after-width"),
  _WEBKIT_BORDER_BEFORE ("-webkit-border-before"),
  _WEBKIT_BORDER_BEFORE_COLOR ("-webkit-border-before-color"),
  _WEBKIT_BORDER_BEFORE_STYLE ("-webkit-border-before-style"),
  _WEBKIT_BORDER_BEFORE_WIDTH ("-webkit-border-before-width"),
  _WEBKIT_BORDER_FIT ("-webkit-border-fit"),
  _WEBKIT_BORDER_HORIZONTAL_SPACING ("-webkit-border-horizontal-spacing"),
  _WEBKIT_BORDER_VERTICAL_SPACING ("-webkit-border-vertical-spacing"),
  _WEBKIT_BOX_FLEX_GROUP ("-webkit-box-flex-group"),
  _WEBKIT_BOX_LINES ("-webkit-box-lines"),
  _WEBKIT_BOX_REFLECT ("-webkit-box-reflect"),
  _WEBKIT_COLOR_CORRECTION ("-webkit-color-correction"),
  _WEBKIT_COLUMN_AXIS ("-webkit-column-axis"),
  _WEBKIT_COLUMN_BREAK_AFTER ("-webkit-column-break-after"),
  _WEBKIT_COLUMN_BREAK_BEFORE ("-webkit-column-break-before"),
  _WEBKIT_COLUMN_BREAK_INSIDE ("-webkit-column-break-inside"),
  _WEBKIT_COLUMN_PROGRESSION ("-webkit-column-progression"),
  _WEBKIT_COLUMN_SPAN ("-webkit-column-span"),
  _WEBKIT_CURSOR_VISIBILITY ("-webkit-cursor-visibility"),
  _WEBKIT_DASHBOARD_REGION ("-webkit-dashboard-region"),
  _WEBKIT_FONT_SIZE_DELTA ("-webkit-font-size-delta"),
  _WEBKIT_FONT_SMOOTHING ("-webkit-font-smoothing"),
  _WEBKIT_GRID_DEFINITION_COLUMNS ("-webkit-grid-definition-columns"),
  _WEBKIT_GRID_DEFINITION_ROWS ("-webkit-grid-definition-rows"),
  _WEBKIT_HIGHLIGHT ("-webkit-highlight"),
  _WEBKIT_HYPHENATE_CHARACTER ("-webkit-hyphenate-character"),
  _WEBKIT_HYPHENATE_LIMIT_AFTER ("-webkit-hyphenate-limit-after"),
  _WEBKIT_HYPHENATE_LIMIT_BEFORE ("-webkit-hyphenate-limit-before"),
  _WEBKIT_HYPHENATE_LIMIT_LINES ("-webkit-hyphenate-limit-lines"),
  _WEBKIT_LINE_ALIGN ("-webkit-line-align"),
  _WEBKIT_LINE_BOX_CONTAIN ("-webkit-line-box-contain"),
  _WEBKIT_LINE_CLAMP ("-webkit-line-clamp"),
  _WEBKIT_LOCALE ("-webkit-locale"),
  _WEBKIT_LOGICAL_HEIGHT ("-webkit-logical-height"),
  _WEBKIT_LOGICAL_WIDTH ("-webkit-logical-width"),
  _WEBKIT_MARGIN_AFTER ("-webkit-margin-after"),
  _WEBKIT_MARGIN_AFTER_COLLAPSE ("-webkit-margin-after-collapse"),
  _WEBKIT_MARGIN_BEFORE ("-webkit-margin-before"),
  _WEBKIT_MARGIN_BEFORE_COLLAPSE ("-webkit-margin-before-collapse"),
  _WEBKIT_MARGIN_BOTTOM_COLLAPSE ("-webkit-margin-bottom-collapse"),
  _WEBKIT_MARGIN_COLLAPSE ("-webkit-margin-collapse"),
  _WEBKIT_MARGIN_TOP_COLLAPSE ("-webkit-margin-top-collapse"),
  _WEBKIT_MARQUEE ("-webkit-marquee"),
  _WEBKIT_MARQUEE_INCREMENT ("-webkit-marquee-increment"),
  _WEBKIT_MARQUEE_REPETITION ("-webkit-marquee-repetition"),
  _WEBKIT_MASK_POSITION_X ("-webkit-mask-position-x"),
  _WEBKIT_MASK_POSITION_Y ("-webkit-mask-position-y"),
  _WEBKIT_MASK_REPEAT_X ("-webkit-mask-repeat-x"),
  _WEBKIT_MASK_REPEAT_Y ("-webkit-mask-repeat-y"),
  _WEBKIT_MAX_LOGICAL_HEIGHT ("-webkit-max-logical-height"),
  _WEBKIT_MAX_LOGICAL_WIDTH ("-webkit-max-logical-width"),
  _WEBKIT_MIN_LOGICAL_HEIGHT ("-webkit-min-logical-height"),
  _WEBKIT_MIN_LOGICAL_WIDTH ("-webkit-min-logical-width"),
  _WEBKIT_NBSP_MODE ("-webkit-nbsp-mode"),
  _WEBKIT_OVERFLOW_SCROLLING ("-webkit-overflow-scrolling"),
  _WEBKIT_PADDING_AFTER ("-webkit-padding-after"),
  _WEBKIT_PADDING_BEFORE ("-webkit-padding-before"),
  _WEBKIT_PERSPECTIVE_ORIGIN_X ("-webkit-perspective-origin-x"),
  _WEBKIT_PERSPECTIVE_ORIGIN_Y ("-webkit-perspective-origin-y"),
  _WEBKIT_PRINT_COLOR_ADJUST ("-webkit-print-color-adjust"),
  _WEBKIT_REGION_BREAK_AFTER ("-webkit-region-break-after"),
  _WEBKIT_REGION_BREAK_BEFORE ("-webkit-region-break-before"),
  _WEBKIT_REGION_BREAK_INSIDE ("-webkit-region-break-inside"),
  _WEBKIT_RTL_ORDERING ("-webkit-rtl-ordering"),
  _WEBKIT_SHAPE_INSIDE ("-webkit-shape-inside"),
  _WEBKIT_SHAPE_PADDING ("-webkit-shape-padding"),
  _WEBKIT_TAP_HIGHLIGHT_COLOR ("-webkit-tap-highlight-color"),
  _WEBKIT_TEXT_DECORATIONS_IN_EFFECT ("-webkit-text-decorations-in-effect"),
  _WEBKIT_TEXT_FILL_COLOR ("-webkit-text-fill-color"),
  _WEBKIT_TEXT_SECURITY ("-webkit-text-security"),
  _WEBKIT_TEXT_STROKE ("-webkit-text-stroke"),
  _WEBKIT_TEXT_STROKE_COLOR ("-webkit-text-stroke-color"),
  _WEBKIT_TEXT_STROKE_WIDTH ("-webkit-text-stroke-width"),
  _WEBKIT_TRANSFORM_ORIGIN_X ("-webkit-transform-origin-x"),
  _WEBKIT_TRANSFORM_ORIGIN_Y ("-webkit-transform-origin-y"),
  _WEBKIT_TRANSFORM_ORIGIN_Z ("-webkit-transform-origin-z"),
  _WEBKIT_USER_DRAG ("-webkit-user-drag");

  /** Use {@link #JUSTIFY_CONTENT} instead. */
  @Deprecated (forRemoval = true, since = "8.2.4")
  @ChangeNextMajorRelease ("Remove this misspelled constant")
  public static final ECSSProperty JUSITFY_CONTENT = JUSTIFY_CONTENT;

  /** Use {@link #JUSTIFY_ITEMS} instead. */
  @Deprecated (forRemoval = true, since = "8.2.4")
  @ChangeNextMajorRelease ("Remove this misspelled constant")
  public static final ECSSProperty JUSITFY_ITEMS = JUSTIFY_ITEMS;

  /** Use {@link #JUSTIFY_SELF} instead. */
  @Deprecated (forRemoval = true, since = "8.2.4")
  @ChangeNextMajorRelease ("Remove this misspelled constant")
  public static final ECSSProperty JUSITFY_SELF = JUSTIFY_SELF;

  private final String m_sName;
  private final ECSSVendorPrefix m_eVendorPrefix;
  private final EnumSet <ECSSSpecification> m_aSpecifications;

  ECSSProperty (@NonNull @Nonempty final String sName)
  {
    // Custom properties are always there
    this (sName, (ECSSSpecification []) null);
  }

  ECSSProperty (@NonNull @Nonempty final String sName, @Nullable final ECSSSpecification... aSpecifications)
  {
    m_sName = sName;
    ECSSVendorPrefix eUsedVendorPrefix = null;
    for (final ECSSVendorPrefix eVendorPrefix : ECSSVendorPrefix.values ())
      if (sName.startsWith (eVendorPrefix.getPrefix ()))
      {
        eUsedVendorPrefix = eVendorPrefix;
        break;
      }
    m_eVendorPrefix = eUsedVendorPrefix;
    m_aSpecifications = CollectionHelperExt.createEnumSet (ECSSSpecification.class, aSpecifications);
  }

  /**
   * @return The name of this property. E.g. <code>color</code> or <code>-webkit-writing-mode</code>
   */
  @NonNull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  /**
   * @return The name of the property without an eventually present vendor prefix.
   * @since 3.9.0
   */
  @NonNull
  @Nonempty
  public String getVendorIndependentName ()
  {
    if (m_eVendorPrefix != null)
      return m_sName.substring (m_eVendorPrefix.getPrefix ().length ());

    return m_sName;
  }

  /**
   * @return A copy with all specifications, where the property is defined. Never <code>null</code>
   *         but maybe empty.
   */
  @NonNull
  @ReturnsMutableCopy
  public Set <ECSSSpecification> getAllSpecifications ()
  {
    return EnumSet.copyOf (m_aSpecifications);
  }

  /**
   * Check if this property is specific to the passed vendor prefix.
   *
   * @param eVendorPrefix
   *        The vendor prefix to check. May not be <code>null</code>.
   * @return <code>true</code> if this property is specific to this vendor, <code>false</code>
   *         otherwise.
   * @since 3.9.0
   */
  public boolean isVendorSpecific (@NonNull final ECSSVendorPrefix eVendorPrefix)
  {
    ValueEnforcer.notNull (eVendorPrefix, "VendorPrefix");
    return eVendorPrefix.equals (m_eVendorPrefix);
  }

  /**
   * @return <code>true</code> if this property is vendor specific.
   * @since 3.9.0
   */
  public boolean isVendorSpecific ()
  {
    return m_eVendorPrefix != null;
  }

  /**
   * @return The vendor prefix used by this property or <code>null</code> if this property is vendor
   *         independent.
   * @since 3.9.0
   */
  @Nullable
  public ECSSVendorPrefix getUsedVendorPrefix ()
  {
    return m_eVendorPrefix;
  }

  @Nullable
  public static ECSSProperty getFromNameOrNull (@Nullable final String sName)
  {
    return EnumHelper.getFromNameOrNull (ECSSProperty.class, sName);
  }

  /**
   * Get the real name of the property without hacking prefixes. This method strips the first
   * leading '*', '_' or '$' from the name.
   *
   * @param sName
   *        The source name. May be <code>null</code>.
   * @return <code>null</code> if the source was <code>null</code> or the string without the leading
   *         hack indicator.
   */
  @Nullable
  public static String getPropertyNameHandlingHacks (@Nullable final String sName)
  {
    String sRealName = sName;
    if (StringHelper.isNotEmpty (sRealName))
    {
      // IE hacks
      final char cFirst = sRealName.charAt (0);
      if (cFirst == '*' || cFirst == '_' || cFirst == '$')
        sRealName = sRealName.substring (1);
    }
    return sRealName;
  }

  /**
   * Get the CSS property with the specified name, but without hacking prefixes. This method strips
   * the first leading '*', '_' or '$' from the name before it searches.
   *
   * @param sName
   *        The source name. May be <code>null</code>.
   * @return <code>null</code> if the source was <code>null</code> or if no such property is known.
   * @see #getPropertyNameHandlingHacks(String)
   */
  @Nullable
  public static ECSSProperty getFromNameOrNullHandlingHacks (@Nullable final String sName)
  {
    final String sRealName = getPropertyNameHandlingHacks (sName);
    return getFromNameOrNull (sRealName);
  }
}
