/**
 * Copyright (C) 2014-2020 Philip Helger (www.helger.com)
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
package com.helger.css;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;
import com.helger.commons.string.StringHelper;

/**
 * Enumeration containing all W3C CSS specifications.<br>
 * Source: https://www.w3.org/Style/CSS/current-work.en.html<br>
 * Last update: 2019-02-29<br>
 * For regular CSS parsing/writing this enum has no impact!<br>
 * <br>
 * Run MainCreateSupportedCSSPropertiesFile after changing something here!!
 *
 * @author Philip Helger
 */
public enum ECSSSpecification implements IHasID <String>
{
  /*
   * COMPLETED
   */

  /** CSS Color Level 3 */
  CSS3_COLOR ("css3-color", ECSSSpecificationStatus.COMPLETED, "https://www.w3.org/TR/2018/REC-css-color-3-20180619/"),
  /** CSS Namespaces */
  CSS3_NAMESPACE ("css3-namespace", ECSSSpecificationStatus.COMPLETED, "https://www.w3.org/TR/2014/REC-css-namespaces-3-20140320/"),
  /** Selectors Level 3 */
  SELECTORS ("selectors", ECSSSpecificationStatus.COMPLETED, "https://www.w3.org/TR/2018/REC-selectors-3-20181106/"),
  /** CSS Level 2 Revision 1 */
  CSS2 ("CSS2", ECSSSpecificationStatus.COMPLETED, "http://www.w3.org/TR/2011/REC-CSS2-20110607"),
  /** Media Queries */
  CSS3_MEDIAQUERIES ("css3-mediaqueries", ECSSSpecificationStatus.COMPLETED, "http://www.w3.org/TR/2012/REC-css3-mediaqueries-20120619/"),
  /** CSS Style Attributes */
  CSS_STYLE_ATTR ("css-style-attr", ECSSSpecificationStatus.COMPLETED, "http://www.w3.org/TR/2013/REC-css-style-attr-20131107/"),
  /** CSS Fonts Level 3 */
  CSS_FONTS_3 ("css-fonts-3", ECSSSpecificationStatus.COMPLETED, "https://www.w3.org/TR/2018/REC-css-fonts-3-20180920/"),
  /** CSS Writing Modes Level 3 */
  CSS_WRITING_MODES_3 ("css-writing-modes-3",
                       ECSSSpecificationStatus.COMPLETED,
                       "https://www.w3.org/TR/2019/REC-css-writing-modes-3-20191210/"),
  /** CSS Basic User Interface Level 3 */
  CSS_UI_3 ("css-ui-3", ECSSSpecificationStatus.COMPLETED, "https://www.w3.org/TR/2018/REC-css-ui-3-20180621/"),
  /** CSS Containment Module Level 1 */
  CSS_CONTAIN_1 ("css-contain-1", ECSSSpecificationStatus.COMPLETED, "https://www.w3.org/TR/2019/REC-css-contain-1-20191121/"),

  /*
   * STABLE
   */

  /** CSS Backgrounds and Borders Level 3 */
  CSS3_BACKGROUND ("css3-background", ECSSSpecificationStatus.STABLE, "https://www.w3.org/TR/2017/CR-css-backgrounds-3-20171017/"),
  /** CSS Conditional Rules Level 3 */
  CSS3_CONDITIONAL ("css3-conditional", ECSSSpecificationStatus.STABLE, "http://www.w3.org/TR/2013/CR-css3-conditional-20130404/"),
  /** CSS Multi-column Layout */
  CSS3_MULTICOL ("css3-multicol", ECSSSpecificationStatus.STABLE, "http://www.w3.org/TR/2011/CR-css3-multicol-20110412/"),
  /** CSS Values and Units Level 3 */
  CSS_VALUES_3 ("css-values-3", ECSSSpecificationStatus.STABLE, "https://www.w3.org/TR/2016/CR-css-values-3-20160929/"),
  /** CSS Cascading and Inheritance Level 3 */
  CSS_CASCADE_3 ("css-cascade-3", ECSSSpecificationStatus.STABLE, "http://www.w3.org/TR/2016/CR-css-cascade-3-20160519/"),

  /** CSS Image Values and Replaced Content Level 3 */
  CSS3_IMAGES ("css3-images", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2012/CR-css3-images-20120417/"),
  /** CSS Speech */
  CSS3_SPEECH ("css3-speech", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2012/CR-css3-speech-20120320/"),
  /** CSS Flexible Box Layout */
  CSS_FLEXBOX_1 ("css-flexbox-1", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2016/CR-css-flexbox-1-20160526/"),
  /** CSS Text Decoration Module Level 3 */
  CSS_TEXT_DECOR_3 ("css-text-decor-3", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2013/CR-css-text-decor-3-20130801/"),
  /** CSS Shapes Module Level 1 */
  CSS_SHAPES_1 ("css-shapes-1", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2014/CR-css-shapes-1-20140320/"),
  /** CSS Masking Module Level 1 */
  CSS_MASKING_1 ("css-masking-1", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2014/CR-css-masking-1-20140826/"),
  /** CSS Counter Styles Level 3 */
  CSS_COUNTER_STYLES_3 ("css-counter-styles-3",
                        ECSSSpecificationStatus.TESTING,
                        "http://www.w3.org/TR/2015/CR-css-counter-styles-3-20150611/"),
  /** CSS Fragmentation Level 3 */
  CSS_BREAK_3 ("css-break-3", ECSSSpecificationStatus.TESTING, "https://www.w3.org/TR/2017/CR-css-break-3-20170209/"),
  /** Compositing and Blending */
  COMPOSITING_1 ("compositing-1", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2015/CR-compositing-1-20150113/"),
  /** CSS Syntax Level 3 */
  CSS_SYNTAX_3 ("css-syntax-3", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2014/CR-css-syntax-3-20140220/"),
  /** CSS Grid Layout */
  CSS_GRID_1 ("css-grid-1", ECSSSpecificationStatus.TESTING, "https://www.w3.org/TR/2017/CR-css-grid-1-20170209/"),
  /** Geometry Interfaces Module Level 1 */
  GEOMETRY_1 ("geometry-1", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2014/CR-geometry-1-20141125/"),
  /** CSS Cascading and Inheritance Level 4 */
  CSS_CASCADE_4 ("css-cascade-4", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2016/CR-css-cascade-4-20160114/"),
  /** CSS Scroll Snap Module Level 1 */
  CSS_SCROLL_SNAP_1 ("css-scroll-snap-1", ECSSSpecificationStatus.TESTING, "https://www.w3.org/TR/2017/CR-css-scroll-snap-1-20170209/"),

  /** CSS Animations */
  CSS3_ANIMATIONS ("css3-animations", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2013/WD-css3-animations-20130219/"),
  /** Web Animations */
  WEB_ANIMATIONS ("web-animations", ECSSSpecificationStatus.REFINING, "https://www.w3.org/TR/2016/WD-web-animations-1-20160913//"),
  /** CSS Text Level 3 */
  CSS_TEXT_3 ("css-text-3", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2013/WD-css-text-3-20131010/"),
  /** CSS Transforms Module Level 1 */
  CSS_TRANSFORMS_1 ("css-transforms-1", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2013/WD-css-transforms-1-20131126/"),
  /** CSS Transitions */
  CSS3_TRANSITIONS ("css3-transitions", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2013/WD-css3-transitions-20131119/"),
  /** Cascading Variables */
  CSS_VARIABLES_1 ("css-variables-1", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2015/CR-css-variables-1-20151203/"),
  /** CSS Box Alignment Module Level 3 */
  CSS_ALIGN_3 ("css-align-3", ECSSSpecificationStatus.REFINING, "https://www.w3.org/TR/2017/WD-css-align-3-20170215/"),
  /** CSS Will Change */
  CSS_WILL_CHANGE_1 ("css-will-change-1", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2015/CR-css-will-change-1-20151203/"),
  /** Cascading Style Sheets Level 2 Revision 2 (CSS 2.2) Specification */
  CSS22 ("css22", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2016/WD-CSS22-20160412/"),

  /** CSS Paged Media Level 3 */
  CSS3_PAGE ("css3-page", ECSSSpecificationStatus.REVISING, "http://www.w3.org/TR/2013/WD-css3-page-20130314/"),
  /** CSSOM View */
  CSSOM_VIEW_1 ("cssom-view-1", ECSSSpecificationStatus.REVISING, "http://www.w3.org/TR/2016/WD-cssom-view-1-20160317/"),
  /** Selectors Level 4 */
  SELECTORS4 ("selectors4", ECSSSpecificationStatus.REVISING, "http://www.w3.org/TR/2013/WD-selectors4-20130502/"),
  /** CSS Intrinsic &amp; Extrinsic Sizing Module Level 3 */
  CSS_SIZING_3 ("css-sizing-3", ECSSSpecificationStatus.REVISING, "://www.w3.org/TR/2017/WD-css-sizing-3-20170207/"),
  /** CSS Overflow Module Level 3 */
  CSS_OVERFLOW_3 ("css-overflow-3", ECSSSpecificationStatus.REVISING, "http://www.w3.org/TR/2016/WD-css-overflow-3-20160531/"),
  /** CSS Display Module Level 3 */
  CSS_DISPLAY_3 ("css-display-3", ECSSSpecificationStatus.REVISING, "https://www.w3.org/TR/2017/WD-css-display-3-20170126/"),
  /** CSS Pseudo-Elements Module Level 4 */
  CSS_PSEUDO_4 ("css-pseudo-4", ECSSSpecificationStatus.REVISING, "http://www.w3.org/TR/2016/WD-css-pseudo-4-20160607/"),

  /** CSS Backgrounds and Borders Level 4 */
  CSS4_BACKGROUND ("css4-background", ECSSSpecificationStatus.EXPLORING),
  /** CSS Device Adaptation Module Level 1 */
  CSS_DEVICE_ADAPT_1 ("css-device-adapt-1", ECSSSpecificationStatus.EXPLORING, "ttp://www.w3.org/TR/2016/WD-css-device-adapt-1-20160329/"),
  /** CSS Exclusions Module Level 1 */
  CSS3_EXCLUSIONS ("css3-exclusions", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2015/WD-css3-exclusions-20150115/"),
  /** Filter Effects Module Level 1 */
  FILTER_EFFECTS_1 ("filter-effects-1", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/WD-filter-effects-1-20141125/"),
  /** CSS Generated Content for Paged Media */
  CSS_GCPM_3 ("css-gpcm-3", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/WD-css-gcpm-3-20140513/"),
  /** CSS Page Floats */
  PAGE_FLOATS ("page-floats", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2015/WD-css-page-floats-3-20150915/"),
  /** CSS Template Layout Module */
  CSS_TEMPLATE_3 ("css-template-3", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2015/NOTE-css-template-3-20150326/"),
  /** CSS Line Grid Module Level 1 */
  CSS_LINE_GRID_1 ("css-line-grid-1", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/WD-css-line-grid-1-20140916/"),
  /** CSS Lists and Counters Module Level 3 */
  CSS_LISTS_3 ("css-lists-3", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/WD-css-lists-3-20140320/"),
  /** CSS Positioned Layout Level 3 */
  CSS_POSITION_3 ("css-position-3", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2016/WD-css-position-3-20160517/"),
  /** CSS Regions Module Level 1 */
  CSS_REGIONS_1 ("css-regions-1", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/WD-css-regions-1-20141009/"),
  /** CSS Ruby Layout Module Level 1 */
  CSS_RUBY_1 ("css-ruby-1", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/WD-css-ruby-1-20140805/"),
  /** CSS Table Module Level 3 */
  CSS_TABLES_3 ("css-tables-3", ECSSSpecificationStatus.EXPLORING, "https://www.w3.org/TR/2017/WD-css-tables-3-20170307/"),
  /** CSS Object Model (CSSOM) */
  CSSOM_1 ("cssom-1", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2016/WD-cssom-1-20160317/"),
  /** CSS Font Loading Module Level 3 */
  CSS_FONT_LOADING_3 ("css-font-loading-3", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/WD-css-font-loading-3-20140522/"),
  /** CSS Scoping Module Level 1 */
  CSS_SCOPING_1 ("css-scoping-1", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/WD-css-scoping-1-20140403/"),
  /** Media Queries Level 4 */
  MEDIAQUERIES_4 ("mediaqueries-4", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2016/WD-mediaqueries-4-20160706/"),
  /** CSS Inline Layout Module Level 3 */
  CSS_INLINE_3 ("css-inline-3", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2016/WD-css-inline-3-20160524/"),
  /** Motion Path Module Level 1 */
  MOTION_1 ("motion-1", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2015/WD-motion-1-20150409/"),
  /** CSS Round Display Level 1 */
  CSS_ROUND_DISPLAY_1 ("css-round-display-1",
                       ECSSSpecificationStatus.EXPLORING,
                       "https://www.w3.org/TR/2016/WD-css-round-display-1-20161222/"),
  /** CSS Basic User Interface Module Level 4 */
  CSS_UI_4 ("css-ui-4", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2015/WD-css-ui-4-20150922/"),
  /** CSS Text Module Level 4 */
  CSS_TEXT_4 ("css-text-4", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2015/WD-css-text-4-20150922/"),
  /** CSS Painting API Level 1 */
  CSS_PAINT_API_1 ("css-paint-api-1", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2016/WD-css-paint-api-1-20160607/"),
  /** CSS Properties and Values API Level 1 */
  CSS_PROPERTIES_VALUES_API_1 ("css-properties-values-api-1",
                               ECSSSpecificationStatus.EXPLORING,
                               "http://www.w3.org/TR/2016/WD-css-properties-values-api-1-20160607/"),
  /** CSS Typed OM Level 1 */
  CSS_TYPED_OM_1 ("css-typed-om-1", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2016/WD-css-typed-om-1-20160607/"),
  /** Worklets Level 1 */
  WORKLETS_1 ("worklets-1", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2016/WD-worklets-1-20160607/"),
  /** CSS Color Module Level 4 */
  CSS_COLOR_4 ("css-color-4", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2016/WD-css-color-4-20160705/"),
  /** CSS Fonts Level 4 */
  CSS_FONTS_4 ("css-fonts-4", ECSSSpecificationStatus.EXPLORING),
  /** CSS Rhythmic Sizing */
  CSS_RHYTHM_1 ("css-rhythm-1", ECSSSpecificationStatus.EXPLORING, "https://www.w3.org/TR/2017/WD-css-rhythm-1-20170302/"),
  /** CSS Image Values and Replaced Content Module Level 4 */
  CSS4_IMAGES ("css4-images", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2012/WD-css4-images-20120911/"),

  /*
   * REWRITING
   */

  /** CSS Generated Content Level 3 */
  CSS3_CONTENT ("css3-content", ECSSSpecificationStatus.REWRITING, "http://www.w3.org/TR/2003/WD-css3-content-20030514/"),

  /*
   * ABANDONED
   */

  /** CSS Level 1 - Abandoned */
  @Deprecated
  CSS1 ("CSS1", ECSSSpecificationStatus.ABANDONED, "http://www.w3.org/TR/2008/REC-CSS1-20080411"),
  /** CSS Print Profile - Abandoned */
  @Deprecated
  CSS_PRINT ("css-print", ECSSSpecificationStatus.ABANDONED, "http://www.w3.org/TR/2013/NOTE-css-print-20130314/"),
  /** CSS Mobile Profile 2.0 - Abandoned */
  @Deprecated
  CSS_MOBILE ("css-mobile", ECSSSpecificationStatus.ABANDONED, "http://www.w3.org/TR/2014/NOTE-css-mobile-20141014/"),
  /** Non-element Selectors Module Level 1 - Abandoned */
  @Deprecated
  SELECTORS_NONELEMENT_1 ("selectors-nonelement-1",
                          ECSSSpecificationStatus.ABANDONED,
                          "https://www.w3.org/TR/2019/NOTE-selectors-nonelement-1-20190402/"),
  /** The CSS 'Reader' Media Type - Abandoned */
  @Deprecated
  CSS3_READER ("css3-reader", ECSSSpecificationStatus.ABANDONED),
  /** CSS Presentation Levels - Abandoned */
  @Deprecated
  CSS3_PRESLEV ("css3-preslev", ECSSSpecificationStatus.ABANDONED),
  /** CSS TV Profile 1.0 - Abandoned */
  @Deprecated
  CSS_TV ("css-tv", ECSSSpecificationStatus.ABANDONED),
  /** CSS Marquee - Abandoned */
  @Deprecated
  CSS3_MARQUEE ("css3-marquee", ECSSSpecificationStatus.ABANDONED, "http://www.w3.org/TR/2014/NOTE-css3-marquee-20141014/"),
  /** Behavioral Extensions to CSS - Abandoned */
  @Deprecated
  BECSS ("becss", ECSSSpecificationStatus.ABANDONED),
  /** CSS Hyperlink Presentation - Abandoned */
  @Deprecated
  CSS3_HYPERLINKS ("css3-hyperlinks", ECSSSpecificationStatus.ABANDONED, "http://www.w3.org/TR/2004/WD-css3-hyperlinks-20040224/"),
  /** CSS Grid Positioning - Abandoned */
  @Deprecated
  CSS3_GRID ("css3-grid", ECSSSpecificationStatus.ABANDONED, "http://www.w3.org/TR/2007/WD-css3-grid-20070905/"),
  /** Fullscreen - Abandoned */
  @Deprecated
  FULLSCREEN ("fullscreen", ECSSSpecificationStatus.ABANDONED, "http://www.w3.org/TR/2014/NOTE-fullscreen-20141118/"),

  /** Dummy specification that means outside of CSS specified */
  OUTSIDE_CSS ("$outside$", ECSSSpecificationStatus.OUTSIDE_CSS);

  private final String m_sID;
  private final ECSSSpecificationStatus m_eStatus;
  private final String m_sHandledURL;

  private ECSSSpecification (@Nonnull @Nonempty final String sID, @Nonnull final ECSSSpecificationStatus eStatus)
  {
    this (sID, eStatus, null);
  }

  private ECSSSpecification (@Nonnull @Nonempty final String sID,
                             @Nonnull final ECSSSpecificationStatus eStatus,
                             @Nullable final String sHandledURL)
  {
    m_sID = sID;
    m_eStatus = eStatus;
    m_sHandledURL = sHandledURL;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  public ECSSSpecificationStatus getStatus ()
  {
    return m_eStatus;
  }

  public boolean hasHandledURL ()
  {
    return StringHelper.hasText (m_sHandledURL);
  }

  @Nullable
  public String getHandledURL ()
  {
    return m_sHandledURL;
  }

  @Nullable
  public static ECSSSpecification getFromIDOrNull (@Nullable final String sName)
  {
    return EnumHelper.getFromIDOrNull (ECSSSpecification.class, sName);
  }
}
