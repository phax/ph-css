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
package com.helger.css.property;

import java.util.EnumSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.lang.EnumHelper;
import com.helger.commons.name.IHasName;
import com.helger.commons.string.StringHelper;
import com.helger.css.ECSSSpecification;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSVersionAware;
import com.helger.css.annotation.DeprecatedInCSS30;

/**
 * Contains a list of most CSS property names.<br>
 * Source of Webkit property names:
 * http://trac.webkit.org/export/0/trunk/Source/WebCore/css/CSSPropertyNames.in
 * <br>
 * <br>
 * MS specific property names:
 * http://msdn.microsoft.com/en-us/library/ie/hh772373%28v=vs.85%29.aspx<br>
 * http://blogs.msdn.com/b/ie/archive/2008/09/08/microsoft-css-vendor-extensions
 * .aspx<br>
 * <br>
 * Mozilla specific property names:
 * https://developer.mozilla.org/en/CSS_Reference/Mozilla_Extensions<br>
 * <br>
 * CSS 3.0: see {@link ECSSSpecification}
 *
 * @author Philip Helger
 */
public enum ECSSProperty implements IHasName,ICSSVersionAware
{
  ALIGN_CONTENT ("align-content", ECSSVersion.CSS30, ECSSSpecification.CSS3_FLEXBOX, ECSSSpecification.CSS3_ALIGN),
  ALIGN_ITEMS ("align-items", ECSSVersion.CSS30, ECSSSpecification.CSS3_FLEXBOX, ECSSSpecification.CSS3_ALIGN),
  ALIGN_SELF ("align-self", ECSSVersion.CSS30, ECSSSpecification.CSS3_FLEXBOX, ECSSSpecification.CSS3_ALIGN),
  ALIGNMENT_ADJUST ("alignment-adjust", ECSSVersion.CSS30, ECSSSpecification.CSS3_LINEBOX),
  ALIGNMENT_BASELINE ("alignment-baseline", ECSSVersion.CSS30, ECSSSpecification.CSS3_LINEBOX),
  ALL ("all", ECSSVersion.CSS30, ECSSSpecification.CSS3_CASCADE),
  ANIMATION ("animation", ECSSVersion.CSS30, ECSSSpecification.CSS3_ANIMATIONS),
  ANIMATION_DELAY ("animation-delay", ECSSVersion.CSS30, ECSSSpecification.CSS3_ANIMATIONS),
  ANIMATION_DIRECTION ("animation-direction", ECSSVersion.CSS30, ECSSSpecification.CSS3_ANIMATIONS),
  ANIMATION_DURATION ("animation-duration", ECSSVersion.CSS30, ECSSSpecification.CSS3_ANIMATIONS),
  ANIMATION_FILL_MODE ("animation-fill-mode", ECSSVersion.CSS30, ECSSSpecification.CSS3_ANIMATIONS),
  ANIMATION_ITERATION_COUNT ("animation-iteration-count", ECSSVersion.CSS30, ECSSSpecification.CSS3_ANIMATIONS),
  ANIMATION_NAME ("animation-name", ECSSVersion.CSS30, ECSSSpecification.CSS3_ANIMATIONS),
  ANIMATION_PLAY_STATE ("animation-play-state", ECSSVersion.CSS30, ECSSSpecification.CSS3_ANIMATIONS),
  ANIMATION_TIMING_FUNCTION ("animation-timing-function", ECSSVersion.CSS30, ECSSSpecification.CSS3_ANIMATIONS),
  AZIMUTH ("azimuth", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  BACKFACE_VISIBILITY ("backface-visibility", ECSSVersion.CSS30, ECSSSpecification.CSS3_TRANSFORMS),
  BACKGROUND ("background", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BACKGROUND_ATTACHMENT ("background-attachment", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BACKGROUND_BLEND_MODE ("background-blend-mode", ECSSVersion.CSS30, ECSSSpecification.COMPOSITING),
  BACKGROUND_CLIP ("background-clip", ECSSVersion.CSS30, ECSSSpecification.CSS3_BACKGROUND),
  BACKGROUND_COLOR ("background-color", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BACKGROUND_IMAGE ("background-image", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BACKGROUND_ORIGIN ("background-origin", ECSSVersion.CSS30, ECSSSpecification.CSS3_BACKGROUND),
  BACKGROUND_POSITION ("background-position", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BACKGROUND_REPEAT ("background-repeat", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BACKGROUND_SIZE ("background-size", ECSSVersion.CSS30, ECSSSpecification.CSS3_BACKGROUND),
  BASELINE_SHIFT ("baseline-shift", ECSSVersion.CSS30, ECSSSpecification.CSS3_LINEBOX),
  @Deprecated BLEED ("bleed", ECSSVersion.CSS30, ECSSSpecification.CSS3_GCPM),
  BOOKMARK_LABEL ("bookmark-label", ECSSVersion.CSS30, ECSSSpecification.CSS3_GCPM),
  BOOKMARK_LEVEL ("bookmark-level", ECSSVersion.CSS30, ECSSSpecification.CSS3_GCPM),
  BOOKMARK_STATE ("bookmark-state", ECSSVersion.CSS30, ECSSSpecification.CSS3_GCPM),
  @Deprecated BOOKMARK_TARGET ("bookmark-target", ECSSVersion.CSS30, ECSSSpecification.CSS3_GCPM),
  BORDER ("border", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_BOTTOM ("border-bottom", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_BOTTOM_COLOR ("border-bottom-color", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_BOTTOM_LEFT_RADIUS ("border-bottom-left-radius", ECSSVersion.CSS30, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_BOTTOM_RIGHT_RADIUS ("border-bottom-right-radius", ECSSVersion.CSS30, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_BOTTOM_STYLE ("border-bottom-style", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_BOTTOM_WIDTH ("border-bottom-width", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_COLLAPSE ("border-collapse", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  BORDER_COLOR ("border-color", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_IMAGE ("border-image", ECSSVersion.CSS30, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_IMAGE_OUTSET ("border-image-outset", ECSSVersion.CSS30, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_IMAGE_REPEAT ("border-image-repeat", ECSSVersion.CSS30, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_IMAGE_SLICE ("border-image-slice", ECSSVersion.CSS30, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_IMAGE_SOURCE ("border-image-source", ECSSVersion.CSS30, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_IMAGE_WIDTH ("border-image-width", ECSSVersion.CSS30, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_LEFT ("border-left", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_LEFT_COLOR ("border-left-color", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_LEFT_STYLE ("border-left-style", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_LEFT_WIDTH ("border-left-width", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_RADIUS ("border-radius", ECSSVersion.CSS30, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_RIGHT ("border-right", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_RIGHT_COLOR ("border-right-color", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_RIGHT_STYLE ("border-right-style", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_RIGHT_WIDTH ("border-right-width", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_SPACING ("border-spacing", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  BORDER_STYLE ("border-style", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_TOP ("border-top", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_TOP_COLOR ("border-top-color", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_TOP_LEFT_RADIUS ("border-top-left-radius", ECSSVersion.CSS30, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_TOP_RIGHT_RADIUS ("border-top-right-radius", ECSSVersion.CSS30, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_TOP_STYLE ("border-top-style", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_TOP_WIDTH ("border-top-width", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BORDER_WIDTH ("border-width", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BACKGROUND),
  BOTTOM ("bottom", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_POSITIONING),
  BOX_DECORATION_BREAK ("box-decoration-break", ECSSVersion.CSS30, ECSSSpecification.CSS3_BACKGROUND),
  BOX_SHADOW ("box-shadow", ECSSVersion.CSS30, ECSSSpecification.CSS3_BACKGROUND, ECSSSpecification.CSS3_BREAK),
  BOX_SIZING ("box-sizing", ECSSVersion.CSS30, ECSSSpecification.CSS3_UI),
  BOX_SNAP ("box-snap", ECSSVersion.CSS30, ECSSSpecification.CSS_LINE_GRID),
  BOX_SUPPRESS ("box-suppress", ECSSVersion.CSS30, ECSSSpecification.CSS_DISPLAY_3),
  BREAK_AFTER ("break-after", ECSSVersion.CSS30, ECSSSpecification.CSS3_MULTICOL, ECSSSpecification.CSS3_BREAK, ECSSSpecification.CSS3_REGIONS),
  BREAK_BEFORE ("break-before", ECSSVersion.CSS30, ECSSSpecification.CSS3_MULTICOL, ECSSSpecification.CSS3_BREAK, ECSSSpecification.CSS3_REGIONS),
  BREAK_INSIDE ("break-inside", ECSSVersion.CSS30, ECSSSpecification.CSS3_MULTICOL, ECSSSpecification.CSS3_BREAK, ECSSSpecification.CSS3_REGIONS),
  CAPTION_SIDE ("caption-side", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  CHAINS ("chains", ECSSVersion.CSS30, ECSSSpecification.CSS3_LAYOUT),
  CLEAR ("clear", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2),
  CLIP ("clip", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_POSITIONING, ECSSSpecification.CSS_MASKING),
  CLIP_PATH ("clip-path", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  CLIP_RULE ("clip-rule", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  COLOR ("color", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_COLOR),
  COLOR_INTERPOLATION_FILTERS ("color-interpolation-filters", ECSSVersion.CSS30, ECSSSpecification.FILTER_EFFECTS),
  COLUMNS ("columns", ECSSVersion.CSS30, ECSSSpecification.CSS3_MULTICOL),
  COLUMNS_SPAN ("columns-span", ECSSVersion.CSS30, ECSSSpecification.CSS3_MULTICOL),
  COLUMNS_WIDTH ("columns-width", ECSSVersion.CSS30, ECSSSpecification.CSS3_MULTICOL),
  COLUMN_COUNT ("column-count", ECSSVersion.CSS30, ECSSSpecification.CSS3_MULTICOL),
  COLUMN_FILL ("column-fill", ECSSVersion.CSS30, ECSSSpecification.CSS3_MULTICOL),
  COLUMN_GAP ("column-gap", ECSSVersion.CSS30, ECSSSpecification.CSS3_MULTICOL),
  COLUMN_RULE ("column-rule", ECSSVersion.CSS30, ECSSSpecification.CSS3_MULTICOL),
  COLUMN_RULE_COLOR ("column-rule-color", ECSSVersion.CSS30, ECSSSpecification.CSS3_MULTICOL),
  COLUMN_RULE_STYLE ("column-rule-style", ECSSVersion.CSS30, ECSSSpecification.CSS3_MULTICOL),
  COLUMN_RULE_WIDTH ("column-rule-width", ECSSVersion.CSS30, ECSSSpecification.CSS3_MULTICOL),
  COLUMN_WIDTH ("column-width", ECSSVersion.CSS30, ECSSSpecification.CSS3_MULTICOL),
  CONTENT ("content", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_UI),
  COUNTER_INCREMENT ("counter-increment", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_LISTS),
  COUNTER_RESET ("counter-reset", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_LISTS),
  COUNTER_SET ("counter-set", ECSSVersion.CSS30, ECSSSpecification.CSS3_LISTS),
  CUE ("cue", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_SPEECH),
  CUE_AFTER ("cue-after", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_SPEECH),
  CUE_BEFORE ("cue-before", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_SPEECH),
  CURSOR ("cursor", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_UI),
  DIRECTION ("direction", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_WRITING_MODES),
  DISPLAY ("display", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_LAYOUT, ECSSSpecification.CSS3_RUBY, ECSSSpecification.CSS_DISPLAY_3),
  DISPLAY_INSIDE ("display-inside", ECSSVersion.CSS30, ECSSSpecification.CSS_DISPLAY_3),
  DISPLAY_LIST ("display-list", ECSSVersion.CSS30, ECSSSpecification.CSS_DISPLAY_3),
  DISPLAY_OUTSIDE ("display-outside", ECSSVersion.CSS30, ECSSSpecification.CSS_DISPLAY_3),
  DOMINANT_BASELINE ("dominant-baseline", ECSSVersion.CSS30, ECSSSpecification.CSS3_LINEBOX),
  DROP_INITIAL_AFTER_ADJUST ("drop-initial-after-adjust", ECSSVersion.CSS30, ECSSSpecification.CSS3_LINEBOX),
  DROP_INITIAL_AFTER_ALIGN ("drop-initial-after-align", ECSSVersion.CSS30, ECSSSpecification.CSS3_LINEBOX),
  DROP_INITIAL_BEFORE_ADJUST ("drop-initial-before-adjust", ECSSVersion.CSS30, ECSSSpecification.CSS3_LINEBOX),
  DROP_INITIAL_BEFORE_ALIGN ("drop-initial-before-align", ECSSVersion.CSS30, ECSSSpecification.CSS3_LINEBOX),
  DROP_INITIAL_SIZE ("drop-initial-size", ECSSVersion.CSS30, ECSSSpecification.CSS3_LINEBOX),
  DROP_INITIAL_VALUE ("drop-initial-value", ECSSVersion.CSS30, ECSSSpecification.CSS3_LINEBOX),
  ELEVATION ("elevation", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  EMPTY_CELLS ("empty-cells", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  @Deprecated ENABLE_BACKGROUND ("enable-background", ECSSVersion.CSS30, ECSSSpecification.FILTER_EFFECTS),
  FILTER ("filter", ECSSVersion.CSS30, ECSSSpecification.FILTER_EFFECTS),
  FLEX ("flex", ECSSVersion.CSS30, ECSSSpecification.CSS3_FLEXBOX),
  FLEX_BASIS ("flex-basis", ECSSVersion.CSS30, ECSSSpecification.CSS3_FLEXBOX),
  FLEX_DIRECTION ("flex-direction", ECSSVersion.CSS30, ECSSSpecification.CSS3_FLEXBOX),
  FLEX_FLOW ("flex-flow", ECSSVersion.CSS30, ECSSSpecification.CSS3_FLEXBOX),
  FLEX_GROW ("flex-grow", ECSSVersion.CSS30, ECSSSpecification.CSS3_FLEXBOX),
  FLEX_SHRINK ("flex-shrink", ECSSVersion.CSS30, ECSSSpecification.CSS3_FLEXBOX),
  FLEX_WRAP ("flex-wrap", ECSSVersion.CSS30, ECSSSpecification.CSS3_FLEXBOX),
  FLOAT ("float", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2),
  @Deprecated FLOAT_OFFSET ("float-offset", ECSSVersion.CSS30, ECSSSpecification.CSS3_GCPM),
  FLOOD_COLOR ("flood-color", ECSSVersion.CSS30, ECSSSpecification.FILTER_EFFECTS),
  FLOOD_OPACITY ("flood-opactiy", ECSSVersion.CSS30, ECSSSpecification.FILTER_EFFECTS),
  FLOW_FROM ("flow-from", ECSSVersion.CSS30, ECSSSpecification.CSS3_REGIONS),
  FLOW_INTO ("flow-into", ECSSVersion.CSS30, ECSSSpecification.CSS3_REGIONS),
  FONT ("font", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_FONTS),
  FONT_FAMILY ("font-family", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_FONTS),
  FONT_FEATURE_SETTINGS ("font-feature-settings", ECSSVersion.CSS30, ECSSSpecification.CSS3_FONTS),
  FONT_KERNING ("font-kerning", ECSSVersion.CSS30, ECSSSpecification.CSS3_FONTS),
  FONT_LANGUAGE_OVERRIDE ("font-language-override", ECSSVersion.CSS30, ECSSSpecification.CSS3_FONTS),
  FONT_SIZE ("font-size", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_FONTS),
  FONT_SIZE_ADJUST ("font-size-adjust", ECSSVersion.CSS30, ECSSSpecification.CSS3_FONTS),
  FONT_STRETCH ("font-stretch", ECSSVersion.CSS30, ECSSSpecification.CSS3_FONTS),
  FONT_STYLE ("font-style", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_FONTS),
  FONT_SYNTHESIS ("font-synthesis", ECSSVersion.CSS30, ECSSSpecification.CSS3_FONTS),
  FONT_VARIANT ("font-variant", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_FONTS),
  FONT_VARIANT_ALTERNATES ("font-variant-alternates", ECSSVersion.CSS30, ECSSSpecification.CSS3_FONTS),
  FONT_VARIANT_CAPS ("font-variant-caps", ECSSVersion.CSS30, ECSSSpecification.CSS3_FONTS),
  FONT_VARIANT_EAST_ASIAN ("font-variant-east-asian", ECSSVersion.CSS30, ECSSSpecification.CSS3_FONTS),
  FONT_VARIANT_LIGATURES ("font-variant-ligatures", ECSSVersion.CSS30, ECSSSpecification.CSS3_FONTS),
  FONT_VARIANT_NUMERIC ("font-variant-numeric", ECSSVersion.CSS30, ECSSSpecification.CSS3_FONTS),
  FONT_VARIANT_POSITION ("font-variant-position", ECSSVersion.CSS30, ECSSSpecification.CSS3_FONTS),
  FONT_WEIGHT ("font-weight", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_FONTS),
  FOOTNOTE_DISPLAY ("footnote-display", ECSSVersion.CSS30, ECSSSpecification.CSS3_GCPM),
  FOOTNOTE_POLICY ("footnote-policy", ECSSVersion.CSS30, ECSSSpecification.CSS3_GCPM),
  GRID ("grid", ECSSVersion.CSS30, ECSSSpecification.CSS3_GRID_LAYOUT),
  GRID_AREA ("grid-area", ECSSVersion.CSS30, ECSSSpecification.CSS3_GRID_LAYOUT),
  GRID_AUTO_COLUMNS ("grid-auto-columns", ECSSVersion.CSS30, ECSSSpecification.CSS3_GRID_LAYOUT),
  GRID_AUTO_FLOW ("grid-auto-flow", ECSSVersion.CSS30, ECSSSpecification.CSS3_GRID_LAYOUT),
  @Deprecated GRID_AUTO_POSITION ("grid-auto-position", ECSSVersion.CSS30, ECSSSpecification.CSS3_GRID_LAYOUT),
  GRID_AUTO_ROWS ("grid-auto-rows", ECSSVersion.CSS30, ECSSSpecification.CSS3_GRID_LAYOUT),
  GRID_COLUMN ("grid-column", ECSSVersion.CSS30, ECSSSpecification.CSS3_GRID_LAYOUT),
  GRID_COLUMN_END ("grid-column-end", ECSSVersion.CSS30, ECSSSpecification.CSS3_GRID_LAYOUT),
  GRID_COLUMN_START ("grid-column-start", ECSSVersion.CSS30, ECSSSpecification.CSS3_GRID_LAYOUT),
  @Deprecated GRID_COLUMNS ("grid-columns", ECSSVersion.CSS30, ECSSSpecification.CSS3_GRID),
  GRID_ROW ("grid-row", ECSSVersion.CSS30, ECSSSpecification.CSS3_GRID_LAYOUT),
  GRID_ROW_END ("grid-row-end", ECSSVersion.CSS30, ECSSSpecification.CSS3_GRID_LAYOUT),
  GRID_ROW_START ("grid-row-start", ECSSVersion.CSS30, ECSSSpecification.CSS3_GRID_LAYOUT),
  @Deprecated GRID_ROWS ("grid-rows", ECSSVersion.CSS30, ECSSSpecification.CSS3_GRID),
  GRID_TEMPLATE ("grid-template", ECSSVersion.CSS30, ECSSSpecification.CSS3_GRID_LAYOUT),
  GRID_TEMPLATE_AREAS ("grid-template-areas", ECSSVersion.CSS30, ECSSSpecification.CSS3_GRID_LAYOUT),
  GRID_TEMPLATE_COLUMNS ("grid-template-columns", ECSSVersion.CSS30, ECSSSpecification.CSS3_GRID_LAYOUT),
  GRID_TEMPLATE_ROWS ("grid-template-rows", ECSSVersion.CSS30, ECSSSpecification.CSS3_GRID_LAYOUT),
  HANGING_PUNCTUATION ("hanging-punctuation", ECSSVersion.CSS30, ECSSSpecification.CSS3_TEXT),
  HEIGHT ("height", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS_DEVICE_ADAPT),
  HYPHENS ("hyphens", ECSSVersion.CSS30, ECSSSpecification.CSS3_TEXT),
  ICON ("icon", ECSSVersion.CSS30, ECSSSpecification.CSS3_UI),
  IMAGE_ORIENTATION ("image-orientation", ECSSVersion.CSS30, ECSSSpecification.CSS3_IMAGES),
  IMAGE_RESOLUTION ("image-resolution", ECSSVersion.CSS30, ECSSSpecification.CSS3_IMAGES),
  IME_MODE ("ime-mode", ECSSVersion.CSS30, ECSSSpecification.CSS3_UI),
  INLINE_BOX_ALIGN ("inline-box-align", ECSSVersion.CSS30, ECSSSpecification.CSS3_LINEBOX),
  ISOLATION ("isolation", ECSSVersion.CSS30, ECSSSpecification.COMPOSITING),
  JUSITFY_CONTENT ("justify-content", ECSSVersion.CSS30, ECSSSpecification.CSS3_FLEXBOX, ECSSSpecification.CSS3_ALIGN),
  JUSITFY_ITEMS ("justify-items", ECSSVersion.CSS30, ECSSSpecification.CSS3_ALIGN),
  JUSITFY_SELF ("justify-self", ECSSVersion.CSS30, ECSSSpecification.CSS3_ALIGN),
  LEFT ("left", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_POSITIONING),
  LETTER_SPACING ("letter-spacing", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_TEXT),
  LIGHTING_COLOR ("lighting-color", ECSSVersion.CSS30, ECSSSpecification.FILTER_EFFECTS),
  LINE_BREAK ("line-break", ECSSVersion.CSS30, ECSSSpecification.CSS3_TEXT),
  LINE_GRID ("line-grid", ECSSVersion.CSS30, ECSSSpecification.CSS_LINE_GRID),
  LINE_HEIGHT ("line-height", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_LINEBOX),
  LINE_SNAP ("line-snap", ECSSVersion.CSS30, ECSSSpecification.CSS_LINE_GRID),
  LINE_STACKING ("line-stacking", ECSSVersion.CSS30, ECSSSpecification.CSS3_LINEBOX),
  LINE_STACKING_RUBY ("line-stacking-ruby", ECSSVersion.CSS30, ECSSSpecification.CSS3_LINEBOX),
  LINE_STACKING_SHIFT ("line-stacking-shift", ECSSVersion.CSS30, ECSSSpecification.CSS3_LINEBOX),
  LINE_STACKING_STRATEGY ("line-stacking-strategy", ECSSVersion.CSS30, ECSSSpecification.CSS3_LINEBOX),
  LIST_STYLE ("list-style", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_LISTS),
  LIST_STYLE_IMAGE ("list-style-image", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_LISTS),
  LIST_STYLE_POSITION ("list-style-position", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_LISTS),
  LIST_STYLE_TYPE ("list-style-type", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_LISTS),
  MARGIN ("margin", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2),
  MARGIN_BOTTOM ("margin-bottom", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2),
  MARGIN_LEFT ("margin-left", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2),
  MARGIN_RIGHT ("margin-right", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2),
  MARGIN_TOP ("margin-top", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2),
  MARKER_SIDE ("marker-side", ECSSVersion.CSS30, ECSSSpecification.CSS3_LISTS),
  @Deprecated MARKS ("marks", ECSSVersion.CSS30, ECSSSpecification.CSS3_GCPM),
  @Deprecated MARQUEE_DIRECTION ("marquee-direction", ECSSVersion.CSS30, ECSSSpecification.CSS3_MARQUEE),
  @Deprecated MARQUEE_PLAY_COUNT ("marquee-play-count", ECSSVersion.CSS30, ECSSSpecification.CSS3_MARQUEE),
  @Deprecated MARQUEE_SPEED ("marquee-speed", ECSSVersion.CSS30, ECSSSpecification.CSS3_MARQUEE),
  @Deprecated MARQUEE_STYLE ("marquee-style", ECSSVersion.CSS30, ECSSSpecification.CSS3_MARQUEE),
  MASK ("mask", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  MASK_BORDER ("mask-border", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  MASK_BORDER_MODE ("mask-border-mode", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  MASK_BORDER_REPEAT ("mask-border-repeat", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  MASK_BORDER_SLICE ("mask-border-slice", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  MASK_BORDER_SOURCE ("mask-border-source", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  MASK_BORDER_WIDTH ("mask-border-width", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  @Deprecated MASK_BOX_IMAGE ("mask-box-image", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  @Deprecated MASK_BOX_IMAGE_OUTSET ("mask-box-image-outset", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  @Deprecated MASK_BOX_IMAGE_REPEAT ("mask-box-image-repeat", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  @Deprecated MASK_BOX_IMAGE_SLICE ("mask-box-image-slice", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  @Deprecated MASK_BOX_IMAGE_SOURCE ("mask-box-image-source", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  @Deprecated MASK_BOX_IMAGE_WIDTH ("mask-box-image-width", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  MASK_CLIP ("mask-clip", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  MASK_COMPOSITE ("mask-composite", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  MASK_IMAGE ("mask-image", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  MASK_MODE ("mask-mode", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  MASK_ORIGIN ("mask-origin", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  MASK_POSITION ("mask-position", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  MASK_REPEAT ("mask-repeat", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  MASK_SIZE ("mask-size", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  @Deprecated MASK_SOURCE_TYPE ("mask-source-type", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  MASK_TYPE ("mask-type", ECSSVersion.CSS30, ECSSSpecification.CSS_MASKING),
  MAX_HEIGHT ("max-height", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS_DEVICE_ADAPT),
  MAX_LINES ("max-lines", ECSSVersion.CSS30, ECSSSpecification.CSS_OVERFLOW_3),
  MAX_WIDTH ("max-width", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS_DEVICE_ADAPT),
  MAX_ZOOM ("max-zoom", ECSSVersion.CSS30, ECSSSpecification.CSS_DEVICE_ADAPT),
  MIN_HEIGHT ("min-height", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS_DEVICE_ADAPT),
  MIN_WIDTH ("min-width", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS_DEVICE_ADAPT),
  MIN_ZOOM ("min-zoom", ECSSVersion.CSS30, ECSSSpecification.CSS_DEVICE_ADAPT),
  MIX_BLEND_MODE ("mix-blend-mode", ECSSVersion.CSS30, ECSSSpecification.COMPOSITING),
  NAV_DOWN ("nav-down", ECSSVersion.CSS30, ECSSSpecification.CSS3_UI),
  NAV_INDEX ("nav-index", ECSSVersion.CSS30, ECSSSpecification.CSS3_UI),
  NAV_LEFT ("nav-left", ECSSVersion.CSS30, ECSSSpecification.CSS3_UI),
  NAV_RIGHT ("nav-right", ECSSVersion.CSS30, ECSSSpecification.CSS3_UI),
  NAV_UP ("nav-up", ECSSVersion.CSS30, ECSSSpecification.CSS3_UI),
  OBJECT_FIT ("object-fit", ECSSVersion.CSS30, ECSSSpecification.CSS3_IMAGES),
  OBJECT_POSITION ("object-position", ECSSVersion.CSS30, ECSSSpecification.CSS3_IMAGES),
  OPACITY ("opacity", ECSSVersion.CSS30, ECSSSpecification.CSS3_COLOR),
  ORDER ("order", ECSSVersion.CSS30, ECSSSpecification.CSS3_FLEXBOX),
  ORIENTATION ("orientation", ECSSVersion.CSS30, ECSSSpecification.CSS_DEVICE_ADAPT),
  ORPHANS ("orphans", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BREAK),
  OUTLINE ("outline", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_UI),
  OUTLINE_COLOR ("outline-color", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_UI),
  OUTLINE_OFFSET ("outline-offset", ECSSVersion.CSS30, ECSSSpecification.CSS3_UI),
  OUTLINE_STYLE ("outline-style", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_UI),
  OUTLINE_WIDTH ("outline-width", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_UI),
  OVERFLOW ("overflow", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS_OVERFLOW_3),
  @Deprecated OVERFLOW_STYLE ("overflow-style", ECSSVersion.CSS30, ECSSSpecification.CSS3_MARQUEE),
  OVERFLOW_WRAP ("overflow-wrap", ECSSVersion.CSS30, ECSSSpecification.CSS3_TEXT),
  OVERFLOW_X ("overflow-x", ECSSVersion.CSS30, ECSSSpecification.CSS_OVERFLOW_3),
  OVERFLOW_Y ("overflow-y", ECSSVersion.CSS30, ECSSSpecification.CSS_OVERFLOW_3),
  PADDING ("padding", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2),
  PADDING_BOTTOM ("padding-bottom", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2),
  PADDING_LEFT ("padding-left", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2),
  PADDING_RIGHT ("padding-right", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2),
  PADDING_TOP ("padding-top", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2),
  PAGE_BREAK_AFTER ("page-break-after", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  PAGE_BREAK_BEFORE ("page-break-before", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  PAGE_BREAK_INSIDE ("page-break-inside", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  PAUSE ("pause", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_SPEECH),
  PAUSE_AFTER ("pause-after", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_SPEECH),
  PAUSE_BEFORE ("pause-before", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_SPEECH),
  PERSPECTIVE ("perspective", ECSSVersion.CSS30, ECSSSpecification.CSS3_TRANSFORMS),
  PERSPECTIVE_ORIGIN ("perspective-origin", ECSSVersion.CSS30, ECSSSpecification.CSS3_TRANSFORMS),
  PITCH ("pitch", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  PITCH_RANGE ("pitch-range", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  PLAY_DURING ("play-during", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  POSITION ("position", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_POSITIONING, ECSSSpecification.CSS3_LAYOUT),
  QUOTES ("quotes", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  REGION_FRAGMENT ("region-fragment", ECSSVersion.CSS30, ECSSSpecification.CSS3_REGIONS),
  RESIZE ("resize", ECSSVersion.CSS30, ECSSSpecification.CSS3_UI),
  RESOLUTION ("resolution", ECSSVersion.CSS30, ECSSSpecification.CSS_DEVICE_ADAPT),
  REST ("rest", ECSSVersion.CSS30, ECSSSpecification.CSS3_SPEECH),
  REST_AFTER ("rest-after", ECSSVersion.CSS30, ECSSSpecification.CSS3_SPEECH),
  REST_BEFORE ("rest-before", ECSSVersion.CSS30, ECSSSpecification.CSS3_SPEECH),
  RICHNESS ("richness", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  RIGHT ("right", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_POSITIONING),
  RUBY_ALIGN ("ruby-align", ECSSVersion.CSS30, ECSSSpecification.CSS3_RUBY),
  RUBY_MERGE ("ruby-merge", ECSSVersion.CSS30, ECSSSpecification.CSS3_RUBY),
  @Deprecated RUBY_OVERHANG ("ruby-overhang", ECSSVersion.CSS30, ECSSSpecification.CSS3_RUBY),
  RUBY_POSITION ("ruby-position", ECSSVersion.CSS30, ECSSSpecification.CSS3_RUBY),
  @Deprecated RUBY_SPAN ("ruby-span", ECSSVersion.CSS30, ECSSSpecification.CSS3_RUBY),
  RUNNING ("running", ECSSVersion.CSS30, ECSSSpecification.CSS3_GCPM),
  SCROLL_BEHAVIOR ("scroll-behavior", ECSSVersion.CSS30, ECSSSpecification.CSSOM_VIEW),
  SHAPE_OUTSIDE ("shape-outside", ECSSVersion.CSS30, ECSSSpecification.CSS_SHAPES),
  SHAPE_IMAGE_THRESHOLD ("shape-image-threshold", ECSSVersion.CSS30, ECSSSpecification.CSS_SHAPES),
  SHAPE_MARGIN ("shape-margin", ECSSVersion.CSS30, ECSSSpecification.CSS_SHAPES),
  SIZE ("size", ECSSVersion.CSS30, ECSSSpecification.CSS3_PAGE),
  SPEAK ("speak", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_SPEECH),
  SPEAK_AS ("speak-as", ECSSVersion.CSS30, ECSSSpecification.CSS3_SPEECH),
  SPEAK_HEADER ("speak-header", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  SPEAK_NUMERAL ("speak-numeral", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  SPEAK_PUNCTUATION ("speak-punctuation", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  SPEECH_RATE ("speech-rate", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  SRC ("src", ECSSVersion.CSS30, ECSSSpecification.CSS3_FONTS),
  STRESS ("stress", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  STRING_SET ("string-set", ECSSVersion.CSS30, ECSSSpecification.CSS3_GCPM),
  TABLE_LAYOUT ("table-layout", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  TAB_SIZE ("tab-size", ECSSVersion.CSS30, ECSSSpecification.CSS3_TEXT),
  @Deprecated TARGET ("target", ECSSVersion.CSS30, ECSSSpecification.CSS3_HYPERLINKS),
  @Deprecated TARGET_NAME ("target-name", ECSSVersion.CSS30, ECSSSpecification.CSS3_HYPERLINKS),
  @Deprecated TARGET_NEW ("target-new", ECSSVersion.CSS30, ECSSSpecification.CSS3_HYPERLINKS),
  @Deprecated TARGET_POSITION ("target-position", ECSSVersion.CSS30, ECSSSpecification.CSS3_HYPERLINKS),
  TEXT_ALIGN ("text-align", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_TEXT),
  TEXT_ALIGN_LAST ("text-align-last", ECSSVersion.CSS30, ECSSSpecification.CSS3_TEXT),
  @Deprecated TEXT_COMBINE_HORIZONTAL ("text-combine-horizontal", ECSSVersion.CSS30, ECSSSpecification.CSS3_WRITING_MODES),
  TEXT_COMBINE_UPRIGHT ("text-combine-upright", ECSSVersion.CSS30, ECSSSpecification.CSS3_WRITING_MODES),
  TEXT_DECORATION ("text-decoration", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS_TEXT_DECOR_3),
  TEXT_DECORATION_COLOR ("text-decoration-color", ECSSVersion.CSS30, ECSSSpecification.CSS_TEXT_DECOR_3),
  TEXT_DECORATION_LINE ("text-decoration-line", ECSSVersion.CSS30, ECSSSpecification.CSS_TEXT_DECOR_3),
  TEXT_DECORATION_SKIP ("text-decoration-skip", ECSSVersion.CSS30, ECSSSpecification.CSS_TEXT_DECOR_3),
  TEXT_DECORATION_STYLE ("text-decoration-style", ECSSVersion.CSS30, ECSSSpecification.CSS_TEXT_DECOR_3),
  TEXT_EMPHASIS ("text-emphasis", ECSSVersion.CSS30, ECSSSpecification.CSS_TEXT_DECOR_3),
  TEXT_EMPHASIS_COLOR ("text-emphasis-color", ECSSVersion.CSS30, ECSSSpecification.CSS_TEXT_DECOR_3),
  TEXT_EMPHASIS_POSITION ("text-emphasis-position", ECSSVersion.CSS30, ECSSSpecification.CSS_TEXT_DECOR_3),
  TEXT_EMPHASIS_STYLE ("text-emphasis-style", ECSSVersion.CSS30, ECSSSpecification.CSS_TEXT_DECOR_3),
  TEXT_HEIGHT ("text-height", ECSSVersion.CSS30, ECSSSpecification.CSS3_LINEBOX),
  TEXT_INDENT ("text-indent", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_TEXT),
  TEXT_JUSTIFY ("text-justify", ECSSVersion.CSS30, ECSSSpecification.CSS3_TEXT),
  TEXT_ORIENTATION ("text-orientation", ECSSVersion.CSS30, ECSSSpecification.CSS3_WRITING_MODES),
  TEXT_OVERFLOW ("text-overflow", ECSSVersion.CSS30, ECSSSpecification.CSS3_UI),
  TEXT_SHADOW ("text-shadow", ECSSVersion.CSS30, ECSSSpecification.CSS_TEXT_DECOR_3),
  TEXT_TRANSFORM ("text-transform", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_TEXT),
  TEXT_UNDERLINE_POSITION ("text-underline-position", ECSSVersion.CSS30, ECSSSpecification.CSS_TEXT_DECOR_3),
  TOP ("top", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_POSITIONING),
  TOUCH_ACTION ("touch-action", ECSSVersion.CSS30, ECSSSpecification.OUTSIDE_CSS),
  TRANSFORM ("transform", ECSSVersion.CSS30, ECSSSpecification.CSS3_TRANSFORMS),
  TRANSFORM_ORIGIN ("transform-origin", ECSSVersion.CSS30, ECSSSpecification.CSS3_TRANSFORMS),
  TRANSFORM_STYLE ("transform-style", ECSSVersion.CSS30, ECSSSpecification.CSS3_TRANSFORMS),
  TRANSITION ("transition", ECSSVersion.CSS30, ECSSSpecification.CSS3_TRANSITIONS),
  TRANSITION_DELAY ("transition-delay", ECSSVersion.CSS30, ECSSSpecification.CSS3_TRANSITIONS),
  TRANSITION_DURATION ("transition-duration", ECSSVersion.CSS30, ECSSSpecification.CSS3_TRANSITIONS),
  TRANSITION_PROPERTY ("transition-property", ECSSVersion.CSS30, ECSSSpecification.CSS3_TRANSITIONS),
  TRANSITION_TIMING_FUNCTION ("transition-timing-function", ECSSVersion.CSS30, ECSSSpecification.CSS3_TRANSITIONS),
  UNICODE_BIDI ("unicode-bidi", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_WRITING_MODES),
  UNICODE_RANGE ("unicode-range", ECSSVersion.CSS30, ECSSSpecification.CSS3_FONTS),
  USER_ZOOM ("user-zoom", ECSSVersion.CSS30, ECSSSpecification.CSS_DEVICE_ADAPT),
  VERTICAL_ALIGN ("vertical-align", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_LINEBOX),
  VISIBILITY ("visibility", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  VOICE_BALANCE ("voice-balance", ECSSVersion.CSS30, ECSSSpecification.CSS3_SPEECH),
  VOICE_DURATION ("voice-duration", ECSSVersion.CSS30, ECSSSpecification.CSS3_SPEECH),
  VOICE_FAMILY ("voice-family", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_SPEECH),
  VOICE_PITCH ("voice-pitch", ECSSVersion.CSS30, ECSSSpecification.CSS3_SPEECH),
  VOICE_RANGE ("voice-range", ECSSVersion.CSS30, ECSSSpecification.CSS3_SPEECH),
  VOICE_RATE ("voice-rate", ECSSVersion.CSS30, ECSSSpecification.CSS3_SPEECH),
  VOICE_STRESS ("voice-stress", ECSSVersion.CSS30, ECSSSpecification.CSS3_SPEECH),
  VOICE_VOLUME ("voice-volume", ECSSVersion.CSS30, ECSSSpecification.CSS3_SPEECH),
  VOLUME ("volume", ECSSVersion.CSS21, ECSSSpecification.CSS2),
  WHITE_SPACE ("white-space", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_TEXT),
  WIDOWS ("widows", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_BREAK),
  WIDTH ("width", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS_DEVICE_ADAPT),
  WILL_CHANGE ("will-change", ECSSVersion.CSS30, ECSSSpecification.CSS_WILL_CHANGE_1),
  WORD_BREAK ("word-break", ECSSVersion.CSS30, ECSSSpecification.CSS3_TEXT),
  WORD_SPACING ("word-spacing", ECSSVersion.CSS10, ECSSSpecification.CSS1, ECSSSpecification.CSS2, ECSSSpecification.CSS3_TEXT),
  // For legacy reasons, UAs must treat 'word-wrap' as an alternate name for the
  // 'overflow-wrap' property, as if it were a shorthand of 'overflow-wrap'.
  WORD_WRAP ("word-wrap", ECSSVersion.CSS30, ECSSSpecification.CSS3_TEXT),
  WRAP_FLOW ("wrap-flow", ECSSVersion.CSS30, ECSSSpecification.CSS3_EXCLUSIONS),
  WRAP_THROUGH ("wrap-through", ECSSVersion.CSS30, ECSSSpecification.CSS3_EXCLUSIONS),
  WRITING_MODE ("writing-mode", ECSSVersion.CSS30, ECSSSpecification.CSS3_WRITING_MODES),
  ZOOM ("zoom", ECSSVersion.CSS30, ECSSSpecification.CSS_DEVICE_ADAPT),
  Z_INDEX ("z-index", ECSSVersion.CSS21, ECSSSpecification.CSS2, ECSSSpecification.CSS3_POSITIONING),

  // Old and deprecated elements
  @DeprecatedInCSS30 _SCROLLBAR_3DLIGHT_COLOR ("scrollbar-3dlight-color"),
  @DeprecatedInCSS30 _SCROLLBAR_ARROW_COLOR ("scrollbar-arrow-color"),
  @DeprecatedInCSS30 _SCROLLBAR_BASE_COLOR ("scrollbar-base-color"),
  @DeprecatedInCSS30 _SCROLLBAR_DARKSHADOW_COLOR ("scrollbar-darkshadow-color"),
  @DeprecatedInCSS30 _SCROLLBAR_FACE_COLOR ("scrollbar-face-color"),
  @DeprecatedInCSS30 _SCROLLBAR_HIGHLIGHT_COLOR ("scrollbar-highlight-color"),
  @DeprecatedInCSS30 _SCROLLBAR_SHADOW_COLOR ("scrollbar-shadow-color"),
  @DeprecatedInCSS30 _SCROLLBAR_TRACK_COLOR ("scrollbar-track-color"),

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
  private final ECSSVersion m_eVersion;
  private final EnumSet <ECSSSpecification> m_aSpecifications;

  private ECSSProperty (@Nonnull @Nonempty final String sName)
  {
    // Custom properties are always there
    this (sName, ECSSVersion.CSS10, (ECSSSpecification []) null);
  }

  private ECSSProperty (@Nonnull @Nonempty final String sName, @Nonnull final ECSSVersion eVersion)
  {
    this (sName, eVersion, (ECSSSpecification []) null);
  }

  private ECSSProperty (@Nonnull @Nonempty final String sName,
                        @Nonnull final ECSSVersion eVersion,
                        @Nullable final ECSSSpecification... aSpecifications)
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
    m_eVersion = eVersion;
    m_aSpecifications = CollectionHelper.newEnumSet (ECSSSpecification.class, aSpecifications);
  }

  /**
   * @return The name of this property. E.g. <code>color</code> or
   *         <code>-webkit-writing-mode</code>
   */
  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  /**
   * @return The name of the property without an eventually present vendor
   *         prefix.
   * @since 3.9.0
   */
  @Nonnull
  @Nonempty
  public String getVendorIndependentName ()
  {
    if (m_eVendorPrefix != null)
      return m_sName.substring (m_eVendorPrefix.getPrefix ().length ());

    return m_sName;
  }

  @Nonnull
  public ECSSVersion getMinimumCSSVersion ()
  {
    return m_eVersion;
  }

  /**
   * @return A copy with all specifications, where the property is defined.
   *         Never <code>null</code> but maybe empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  public EnumSet <ECSSSpecification> getAllSpecifications ()
  {
    return CollectionHelper.newEnumSet (ECSSSpecification.class, m_aSpecifications);
  }

  /**
   * Check if this property is specific to the passed vendor prefix.
   *
   * @param eVendorPrefix
   *        The vendor prefix to check. May not be <code>null</code>.
   * @return <code>true</code> if this property is specific to this vendor,
   *         <code>false</code> otherwise.
   * @since 3.9.0
   */
  public boolean isVendorSpecific (@Nonnull final ECSSVendorPrefix eVendorPrefix)
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
   * @return The vendor prefix used by this property or <code>null</code> if
   *         this property is vendor independent.
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
   * Get the real name of the property without hacking prefixes. This method
   * strips the first leading '*', '_' or '$' from the name.
   *
   * @param sName
   *        The source name. May be <code>null</code>.
   * @return <code>null</code> if the source was <code>null</code> or the string
   *         without the leading hack indicator.
   */
  @Nullable
  public static String getPropertyNameHandlingHacks (@Nullable final String sName)
  {
    String sRealName = sName;
    if (StringHelper.hasText (sRealName))
    {
      // IE hacks
      final char cFirst = sRealName.charAt (0);
      if (cFirst == '*' || cFirst == '_' || cFirst == '$')
        sRealName = sRealName.substring (1);
    }
    return sRealName;
  }

  /**
   * Get the CSS property with the specified name, but without hacking prefixes.
   * This method strips the first leading '*', '_' or '$' from the name before
   * it searches.
   *
   * @param sName
   *        The source name. May be <code>null</code>.
   * @return <code>null</code> if the source was <code>null</code> or if no such
   *         property is known.
   * @see #getPropertyNameHandlingHacks(String)
   */
  @Nullable
  public static ECSSProperty getFromNameOrNullHandlingHacks (@Nullable final String sName)
  {
    final String sRealName = getPropertyNameHandlingHacks (sName);
    return getFromNameOrNull (sRealName);
  }
}
