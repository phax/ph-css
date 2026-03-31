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
  ALIGN_CONTENT ("align-content", CSS_FLEXBOX_1, CSS_ALIGN_3),
  ALIGN_ITEMS ("align-items", CSS_FLEXBOX_1, CSS_ALIGN_3),
  ALIGN_SELF ("align-self", CSS_FLEXBOX_1, CSS_ALIGN_3),
  ALIGNMENT_BASELINE ("alignment-baseline", CSS_INLINE_3),
  ALL ("all", CSS_CASCADE_3),
  ANIMATION ("animation", CSS3_ANIMATIONS),
  ANIMATION_DELAY ("animation-delay", CSS3_ANIMATIONS),
  ANIMATION_DIRECTION ("animation-direction", CSS3_ANIMATIONS),
  ANIMATION_DURATION ("animation-duration", CSS3_ANIMATIONS),
  ANIMATION_FILL_MODE ("animation-fill-mode", CSS3_ANIMATIONS),
  ANIMATION_ITERATION_COUNT ("animation-iteration-count", CSS3_ANIMATIONS),
  ANIMATION_NAME ("animation-name", CSS3_ANIMATIONS),
  ANIMATION_PLAY_STATE ("animation-play-state", CSS3_ANIMATIONS),
  ANIMATION_TIMING_FUNCTION ("animation-timing-function", CSS3_ANIMATIONS),
  AZIMUTH ("azimuth", CSS2),
  BACKFACE_VISIBILITY ("backface-visibility", CSS_TRANSFORMS_1),
  BACKGROUND ("background", CSS1, CSS2, CSS3_BACKGROUND),
  BACKGROUND_ATTACHMENT ("background-attachment", CSS1, CSS2, CSS3_BACKGROUND),
  BACKGROUND_BLEND_MODE ("background-blend-mode", COMPOSITING_1),
  BACKGROUND_CLIP ("background-clip", CSS3_BACKGROUND),
  BACKGROUND_COLOR ("background-color", CSS1, CSS2, CSS3_BACKGROUND),
  BACKGROUND_IMAGE ("background-image", CSS1, CSS2, CSS3_BACKGROUND),
  BACKGROUND_ORIGIN ("background-origin", CSS3_BACKGROUND),
  BACKGROUND_POSITION ("background-position", CSS1, CSS2, CSS3_BACKGROUND),
  BACKGROUND_REPEAT ("background-repeat", CSS1, CSS2, CSS3_BACKGROUND),
  BACKGROUND_SIZE ("background-size", CSS3_BACKGROUND),
  BASELINE_SHIFT ("baseline-shift", CSS_INLINE_3),
  BOOKMARK_LABEL ("bookmark-label", CSS_GCPM_3),
  BOOKMARK_LEVEL ("bookmark-level", CSS_GCPM_3),
  BOOKMARK_STATE ("bookmark-state", CSS_GCPM_3),
  BORDER ("border", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_BOTTOM ("border-bottom", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_BOTTOM_COLOR ("border-bottom-color", CSS2, CSS3_BACKGROUND),
  BORDER_BOTTOM_LEFT_RADIUS ("border-bottom-left-radius", CSS3_BACKGROUND),
  BORDER_BOTTOM_RIGHT_RADIUS ("border-bottom-right-radius", CSS3_BACKGROUND),
  BORDER_BOTTOM_STYLE ("border-bottom-style", CSS2, CSS3_BACKGROUND),
  BORDER_BOTTOM_WIDTH ("border-bottom-width", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_COLLAPSE ("border-collapse", CSS2),
  BORDER_COLOR ("border-color", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_IMAGE ("border-image", CSS3_BACKGROUND),
  BORDER_IMAGE_OUTSET ("border-image-outset", CSS3_BACKGROUND),
  BORDER_IMAGE_REPEAT ("border-image-repeat", CSS3_BACKGROUND),
  BORDER_IMAGE_SLICE ("border-image-slice", CSS3_BACKGROUND),
  BORDER_IMAGE_SOURCE ("border-image-source", CSS3_BACKGROUND),
  BORDER_IMAGE_WIDTH ("border-image-width", CSS3_BACKGROUND),
  BORDER_LEFT ("border-left", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_LEFT_COLOR ("border-left-color", CSS2, CSS3_BACKGROUND),
  BORDER_LEFT_STYLE ("border-left-style", CSS2, CSS3_BACKGROUND),
  BORDER_LEFT_WIDTH ("border-left-width", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_RADIUS ("border-radius", CSS3_BACKGROUND),
  BORDER_RIGHT ("border-right", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_RIGHT_COLOR ("border-right-color", CSS2, CSS3_BACKGROUND),
  BORDER_RIGHT_STYLE ("border-right-style", CSS2, CSS3_BACKGROUND),
  BORDER_RIGHT_WIDTH ("border-right-width", CSS1, CSS2, CSS3_BACKGROUND),
  BORDER_SPACING ("border-spacing", CSS2),
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
  BOX_SNAP ("box-snap", CSS_LINE_GRID_1),
  BOX_SUPPRESS ("box-suppress", CSS_DISPLAY_3),
  BREAK_AFTER ("break-after", CSS3_MULTICOL, CSS_BREAK_3, CSS_REGIONS_1),
  BREAK_BEFORE ("break-before", CSS3_MULTICOL, CSS_BREAK_3, CSS_REGIONS_1),
  BREAK_INSIDE ("break-inside", CSS3_MULTICOL, CSS_BREAK_3, CSS_REGIONS_1),
  CAPTION_SIDE ("caption-side", CSS2),
  CARET_COLOR ("caret-color", CSS_UI_3),
  CHAINS ("chains", CSS_TEMPLATE_3),
  CLEAR ("clear", CSS1, CSS2),
  CLIP ("clip", CSS2, CSS_POSITION_3, CSS_MASKING_1),
  CLIP_PATH ("clip-path", CSS_MASKING_1),
  CLIP_RULE ("clip-rule", CSS_MASKING_1),
  COLOR ("color", CSS1, CSS2, CSS3_COLOR),
  COLOR_INTERPOLATION_FILTERS ("color-interpolation-filters", FILTER_EFFECTS_1),
  COLUMNS ("columns", CSS3_MULTICOL),
  COLUMNS_SPAN ("columns-span", CSS3_MULTICOL),
  COLUMNS_WIDTH ("columns-width", CSS3_MULTICOL),
  COLUMN_COUNT ("column-count", CSS3_MULTICOL),
  COLUMN_FILL ("column-fill", CSS3_MULTICOL),
  COLUMN_GAP ("column-gap", CSS3_MULTICOL),
  COLUMN_RULE ("column-rule", CSS3_MULTICOL),
  COLUMN_RULE_COLOR ("column-rule-color", CSS3_MULTICOL),
  COLUMN_RULE_STYLE ("column-rule-style", CSS3_MULTICOL),
  COLUMN_RULE_WIDTH ("column-rule-width", CSS3_MULTICOL),
  COLUMN_WIDTH ("column-width", CSS3_MULTICOL),
  CONTAIN ("contain", CSS_CONTAIN_1),
  CONTENT ("content", CSS2, CSS_UI_3),
  COUNTER_INCREMENT ("counter-increment", CSS2, CSS_LISTS_3),
  COUNTER_RESET ("counter-reset", CSS2, CSS_LISTS_3),
  COUNTER_SET ("counter-set", CSS_LISTS_3),
  CUE ("cue", CSS2, CSS3_SPEECH),
  CUE_AFTER ("cue-after", CSS2, CSS3_SPEECH),
  CUE_BEFORE ("cue-before", CSS2, CSS3_SPEECH),
  CURSOR ("cursor", CSS2, CSS_UI_3),
  DIRECTION ("direction", CSS2, CSS_WRITING_MODES_3),
  DISPLAY ("display", CSS1, CSS2, CSS_TEMPLATE_3, CSS_RUBY_1, CSS_DISPLAY_3),
  DISPLAY_INSIDE ("display-inside", CSS_DISPLAY_3),
  DISPLAY_LIST ("display-list", CSS_DISPLAY_3),
  DISPLAY_OUTSIDE ("display-outside", CSS_DISPLAY_3),
  DOMINANT_BASELINE ("dominant-baseline", CSS_INLINE_3),
  DROP_INITIAL_AFTER_ADJUST ("drop-initial-after-adjust", CSS_INLINE_3),
  DROP_INITIAL_AFTER_ALIGN ("drop-initial-after-align", CSS_INLINE_3),
  DROP_INITIAL_BEFORE_ADJUST ("drop-initial-before-adjust", CSS_INLINE_3),
  DROP_INITIAL_BEFORE_ALIGN ("drop-initial-before-align", CSS_INLINE_3),
  DROP_INITIAL_SIZE ("drop-initial-size", CSS_INLINE_3),
  DROP_INITIAL_VALUE ("drop-initial-value", CSS_INLINE_3),
  ELEVATION ("elevation", CSS2),
  EMPTY_CELLS ("empty-cells", CSS2),
  FILTER ("filter", FILTER_EFFECTS_1),
  FLEX ("flex", CSS_FLEXBOX_1),
  FLEX_BASIS ("flex-basis", CSS_FLEXBOX_1),
  FLEX_DIRECTION ("flex-direction", CSS_FLEXBOX_1),
  FLEX_FLOW ("flex-flow", CSS_FLEXBOX_1),
  FLEX_GROW ("flex-grow", CSS_FLEXBOX_1),
  FLEX_SHRINK ("flex-shrink", CSS_FLEXBOX_1),
  FLEX_WRAP ("flex-wrap", CSS_FLEXBOX_1),
  FLOAT ("float", CSS1, CSS2),
  FLOOD_COLOR ("flood-color", FILTER_EFFECTS_1),
  FLOOD_OPACITY ("flood-opactiy", FILTER_EFFECTS_1),
  FLOW_FROM ("flow-from", CSS_REGIONS_1),
  FLOW_INTO ("flow-into", CSS_REGIONS_1),
  FONT ("font", CSS1, CSS2, CSS_FONTS_3),
  FONT_FAMILY ("font-family", CSS1, CSS2, CSS_FONTS_3),
  FONT_FEATURE_SETTINGS ("font-feature-settings", CSS_FONTS_3),
  FONT_KERNING ("font-kerning", CSS_FONTS_3),
  FONT_LANGUAGE_OVERRIDE ("font-language-override", CSS_FONTS_3),
  FONT_SIZE ("font-size", CSS1, CSS2, CSS_FONTS_3),
  FONT_SIZE_ADJUST ("font-size-adjust", CSS_FONTS_3),
  FONT_STRETCH ("font-stretch", CSS_FONTS_3),
  FONT_STYLE ("font-style", CSS1, CSS2, CSS_FONTS_3),
  FONT_SYNTHESIS ("font-synthesis", CSS_FONTS_3),
  FONT_VARIANT ("font-variant", CSS1, CSS2, CSS_FONTS_3),
  FONT_VARIANT_ALTERNATES ("font-variant-alternates", CSS_FONTS_3),
  FONT_VARIANT_CAPS ("font-variant-caps", CSS_FONTS_3),
  FONT_VARIANT_EAST_ASIAN ("font-variant-east-asian", CSS_FONTS_3),
  FONT_VARIANT_LIGATURES ("font-variant-ligatures", CSS_FONTS_3),
  FONT_VARIANT_NUMERIC ("font-variant-numeric", CSS_FONTS_3),
  FONT_VARIANT_POSITION ("font-variant-position", CSS_FONTS_3),
  FONT_WEIGHT ("font-weight", CSS1, CSS2, CSS_FONTS_3),
  FOOTNOTE_DISPLAY ("footnote-display", CSS_GCPM_3),
  FOOTNOTE_POLICY ("footnote-policy", CSS_GCPM_3),
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
  HYPHENS ("hyphens", CSS_TEXT_3),
  ICON ("icon", CSS_UI_3),
  IMAGE_ORIENTATION ("image-orientation", CSS3_IMAGES),
  IMAGE_RESOLUTION ("image-resolution", CSS3_IMAGES),
  IME_MODE ("ime-mode", CSS_UI_3),
  INLINE_BOX_ALIGN ("inline-box-align", CSS_INLINE_3),
  ISOLATION ("isolation", COMPOSITING_1),
  JUSITFY_CONTENT ("justify-content", CSS_FLEXBOX_1, CSS_ALIGN_3),
  JUSITFY_ITEMS ("justify-items", CSS_ALIGN_3),
  JUSITFY_SELF ("justify-self", CSS_ALIGN_3),
  LEFT ("left", CSS2, CSS_POSITION_3),
  LETTER_SPACING ("letter-spacing", CSS1, CSS2, CSS_TEXT_3),
  LIGHTING_COLOR ("lighting-color", FILTER_EFFECTS_1),
  LINE_BREAK ("line-break", CSS_TEXT_3),
  LINE_GRID ("line-grid", CSS_LINE_GRID_1),
  LINE_HEIGHT ("line-height", CSS1, CSS2, CSS_INLINE_3),
  LINE_SNAP ("line-snap", CSS_LINE_GRID_1),
  LINE_STACKING ("line-stacking", CSS_INLINE_3),
  LINE_STACKING_RUBY ("line-stacking-ruby", CSS_INLINE_3),
  LINE_STACKING_SHIFT ("line-stacking-shift", CSS_INLINE_3),
  LINE_STACKING_STRATEGY ("line-stacking-strategy", CSS_INLINE_3),
  LIST_STYLE ("list-style", CSS1, CSS2, CSS_LISTS_3),
  LIST_STYLE_IMAGE ("list-style-image", CSS1, CSS2, CSS_LISTS_3),
  LIST_STYLE_POSITION ("list-style-position", CSS1, CSS2, CSS_LISTS_3),
  LIST_STYLE_TYPE ("list-style-type", CSS1, CSS2, CSS_LISTS_3),
  MARGIN ("margin", CSS1, CSS2),
  MARGIN_BOTTOM ("margin-bottom", CSS1, CSS2),
  MARGIN_LEFT ("margin-left", CSS1, CSS2),
  MARGIN_RIGHT ("margin-right", CSS1, CSS2),
  MARGIN_TOP ("margin-top", CSS1, CSS2),
  MARKER_SIDE ("marker-side", CSS_LISTS_3),
  MASK ("mask", CSS_MASKING_1),
  MASK_BORDER ("mask-border", CSS_MASKING_1),
  MASK_BORDER_MODE ("mask-border-mode", CSS_MASKING_1),
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
  MAX_HEIGHT ("max-height", CSS2, CSS_DEVICE_ADAPT_1),
  MAX_LINES ("max-lines", CSS_OVERFLOW_3),
  MAX_WIDTH ("max-width", CSS2, CSS_DEVICE_ADAPT_1),
  MAX_ZOOM ("max-zoom", CSS_DEVICE_ADAPT_1),
  MIN_HEIGHT ("min-height", CSS2, CSS_DEVICE_ADAPT_1),
  MIN_WIDTH ("min-width", CSS2, CSS_DEVICE_ADAPT_1),
  MIN_ZOOM ("min-zoom", CSS_DEVICE_ADAPT_1),
  MIX_BLEND_MODE ("mix-blend-mode", COMPOSITING_1),
  NAV_DOWN ("nav-down", CSS_UI_3),
  NAV_INDEX ("nav-index", CSS_UI_3),
  NAV_LEFT ("nav-left", CSS_UI_3),
  NAV_RIGHT ("nav-right", CSS_UI_3),
  NAV_UP ("nav-up", CSS_UI_3),
  OBJECT_FIT ("object-fit", CSS3_IMAGES),
  OBJECT_POSITION ("object-position", CSS3_IMAGES),
  OPACITY ("opacity", CSS3_COLOR),
  ORDER ("order", CSS_FLEXBOX_1),
  ORIENTATION ("orientation", CSS_DEVICE_ADAPT_1),
  ORPHANS ("orphans", CSS2, CSS_BREAK_3),
  OUTLINE ("outline", CSS2, CSS_UI_3),
  OUTLINE_COLOR ("outline-color", CSS2, CSS_UI_3),
  OUTLINE_OFFSET ("outline-offset", CSS_UI_3),
  OUTLINE_STYLE ("outline-style", CSS2, CSS_UI_3),
  OUTLINE_WIDTH ("outline-width", CSS2, CSS_UI_3),
  OVERFLOW ("overflow", CSS2, CSS_OVERFLOW_3),
  OVERFLOW_WRAP ("overflow-wrap", CSS_TEXT_3),
  OVERFLOW_X ("overflow-x", CSS_OVERFLOW_3),
  OVERFLOW_Y ("overflow-y", CSS_OVERFLOW_3),
  PADDING ("padding", CSS1, CSS2),
  PADDING_BOTTOM ("padding-bottom", CSS1, CSS2),
  PADDING_LEFT ("padding-left", CSS1, CSS2),
  PADDING_RIGHT ("padding-right", CSS1, CSS2),
  PADDING_TOP ("padding-top", CSS1, CSS2),
  PAGE_BREAK_AFTER ("page-break-after", CSS2),
  PAGE_BREAK_BEFORE ("page-break-before", CSS2),
  PAGE_BREAK_INSIDE ("page-break-inside", CSS2),
  PAUSE ("pause", CSS2, CSS3_SPEECH),
  PAUSE_AFTER ("pause-after", CSS2, CSS3_SPEECH),
  PAUSE_BEFORE ("pause-before", CSS2, CSS3_SPEECH),
  PERSPECTIVE ("perspective", CSS_TRANSFORMS_1),
  PERSPECTIVE_ORIGIN ("perspective-origin", CSS_TRANSFORMS_1),
  PITCH ("pitch", CSS2),
  PITCH_RANGE ("pitch-range", CSS2),
  PLAY_DURING ("play-during", CSS2),
  POSITION ("position", CSS2, CSS_POSITION_3, CSS_TEMPLATE_3),
  QUOTES ("quotes", CSS2),
  REGION_FRAGMENT ("region-fragment", CSS_REGIONS_1),
  RESIZE ("resize", CSS_UI_3),
  RESOLUTION ("resolution", CSS_DEVICE_ADAPT_1),
  REST ("rest", CSS3_SPEECH),
  REST_AFTER ("rest-after", CSS3_SPEECH),
  REST_BEFORE ("rest-before", CSS3_SPEECH),
  RICHNESS ("richness", CSS2),
  RIGHT ("right", CSS2, CSS_POSITION_3),
  RUBY_ALIGN ("ruby-align", CSS_RUBY_1),
  RUBY_MERGE ("ruby-merge", CSS_RUBY_1),
  RUBY_POSITION ("ruby-position", CSS_RUBY_1),
  RUNNING ("running", CSS_GCPM_3),
  SCROLL_BEHAVIOR ("scroll-behavior", CSSOM_VIEW_1),
  SHAPE_OUTSIDE ("shape-outside", CSS_SHAPES_1),
  SHAPE_IMAGE_THRESHOLD ("shape-image-threshold", CSS_SHAPES_1),
  SHAPE_MARGIN ("shape-margin", CSS_SHAPES_1),
  SIZE ("size", CSS3_PAGE),
  SPEAK ("speak", CSS2, CSS3_SPEECH),
  SPEAK_AS ("speak-as", CSS3_SPEECH),
  SPEAK_HEADER ("speak-header", CSS2),
  SPEAK_NUMERAL ("speak-numeral", CSS2),
  SPEAK_PUNCTUATION ("speak-punctuation", CSS2),
  SPEECH_RATE ("speech-rate", CSS2),
  SRC ("src", CSS_FONTS_3),
  STRESS ("stress", CSS2),
  STRING_SET ("string-set", CSS_GCPM_3),
  TABLE_LAYOUT ("table-layout", CSS2),
  TAB_SIZE ("tab-size", CSS_TEXT_3),
  TEXT_ALIGN ("text-align", CSS1, CSS2, CSS_TEXT_3),
  TEXT_ALIGN_LAST ("text-align-last", CSS_TEXT_3),
  TEXT_COMBINE_UPRIGHT ("text-combine-upright", CSS_WRITING_MODES_3),
  TEXT_DECORATION ("text-decoration", CSS1, CSS2, CSS_TEXT_DECOR_3),
  TEXT_DECORATION_COLOR ("text-decoration-color", CSS_TEXT_DECOR_3),
  TEXT_DECORATION_LINE ("text-decoration-line", CSS_TEXT_DECOR_3),
  TEXT_DECORATION_SKIP ("text-decoration-skip", CSS_TEXT_DECOR_3),
  TEXT_DECORATION_STYLE ("text-decoration-style", CSS_TEXT_DECOR_3),
  TEXT_EMPHASIS ("text-emphasis", CSS_TEXT_DECOR_3),
  TEXT_EMPHASIS_COLOR ("text-emphasis-color", CSS_TEXT_DECOR_3),
  TEXT_EMPHASIS_POSITION ("text-emphasis-position", CSS_TEXT_DECOR_3),
  TEXT_EMPHASIS_STYLE ("text-emphasis-style", CSS_TEXT_DECOR_3),
  TEXT_HEIGHT ("text-height", CSS_INLINE_3),
  TEXT_INDENT ("text-indent", CSS1, CSS2, CSS_TEXT_3),
  TEXT_JUSTIFY ("text-justify", CSS_TEXT_3),
  TEXT_ORIENTATION ("text-orientation", CSS_WRITING_MODES_3),
  TEXT_OVERFLOW ("text-overflow", CSS_UI_3),
  TEXT_SHADOW ("text-shadow", CSS_TEXT_DECOR_3),
  TEXT_TRANSFORM ("text-transform", CSS1, CSS2, CSS_TEXT_3),
  TEXT_UNDERLINE_POSITION ("text-underline-position", CSS_TEXT_DECOR_3),
  TOP ("top", CSS2, CSS_POSITION_3),
  TOUCH_ACTION ("touch-action", OUTSIDE_CSS),
  TRANSFORM ("transform", CSS_TRANSFORMS_1),
  TRANSFORM_ORIGIN ("transform-origin", CSS_TRANSFORMS_1),
  TRANSFORM_STYLE ("transform-style", CSS_TRANSFORMS_1),
  TRANSITION ("transition", CSS3_TRANSITIONS),
  TRANSITION_DELAY ("transition-delay", CSS3_TRANSITIONS),
  TRANSITION_DURATION ("transition-duration", CSS3_TRANSITIONS),
  TRANSITION_PROPERTY ("transition-property", CSS3_TRANSITIONS),
  TRANSITION_TIMING_FUNCTION ("transition-timing-function", CSS3_TRANSITIONS),
  UNICODE_BIDI ("unicode-bidi", CSS2, CSS_WRITING_MODES_3),
  UNICODE_RANGE ("unicode-range", CSS_FONTS_3),
  USER_ZOOM ("user-zoom", CSS_DEVICE_ADAPT_1),
  VERTICAL_ALIGN ("vertical-align", CSS1, CSS2, CSS_INLINE_3),
  VISIBILITY ("visibility", CSS2),
  VOICE_BALANCE ("voice-balance", CSS3_SPEECH),
  VOICE_DURATION ("voice-duration", CSS3_SPEECH),
  VOICE_FAMILY ("voice-family", CSS2, CSS3_SPEECH),
  VOICE_PITCH ("voice-pitch", CSS3_SPEECH),
  VOICE_RANGE ("voice-range", CSS3_SPEECH),
  VOICE_RATE ("voice-rate", CSS3_SPEECH),
  VOICE_STRESS ("voice-stress", CSS3_SPEECH),
  VOICE_VOLUME ("voice-volume", CSS3_SPEECH),
  VOLUME ("volume", CSS2),
  WHITE_SPACE ("white-space", CSS1, CSS2, CSS_TEXT_3),
  WIDOWS ("widows", CSS2, CSS_BREAK_3),
  WIDTH ("width", CSS1, CSS2, CSS_DEVICE_ADAPT_1),
  WILL_CHANGE ("will-change", CSS_WILL_CHANGE_1),
  WORD_BREAK ("word-break", CSS_TEXT_3),
  WORD_SPACING ("word-spacing", CSS1, CSS2, CSS_TEXT_3),
  // For legacy reasons, UAs must treat 'word-wrap' as an alternate name for the
  // 'overflow-wrap' property, as if it were a shorthand of 'overflow-wrap'.
  WORD_WRAP ("word-wrap", CSS_TEXT_3),
  WRAP_FLOW ("wrap-flow", CSS3_EXCLUSIONS),
  WRAP_THROUGH ("wrap-through", CSS3_EXCLUSIONS),
  WRITING_MODE ("writing-mode", CSS_WRITING_MODES_3),
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
