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
package com.helger.css;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;
import com.helger.commons.string.StringHelper;

/**
 * Enumeration containing all W3C CSS specifications.<br>
 * Source: http://www.w3.org/Style/CSS/current-work<br>
 * Last update: 17.12.2014<br>
 * For regular CSS parsing/writing this enum has no impact!
 *
 * @author Philip Helger
 */
public enum ECSSSpecification implements IHasID <String>
{
  /** CSS Color Level 3 */
  CSS3_COLOR ("css3-color", ECSSSpecificationStatus.COMPLETED, "http://www.w3.org/TR/2011/REC-css3-color-20110607/"),
  /** CSS Namespaces */
  CSS3_NAMESPACE ("css3-namespace", ECSSSpecificationStatus.COMPLETED, "http://www.w3.org/TR/2011/REC-css3-namespace-20110929/"),
  /** Selectors Level 3 */
  SELECTORS ("selectors", ECSSSpecificationStatus.COMPLETED, "http://www.w3.org/TR/2011/REC-css3-selectors-20110929/"),
  /** CSS Level 2 Revision 1 */
  CSS2 ("CSS2", ECSSSpecificationStatus.COMPLETED, "http://www.w3.org/TR/CSS2/"),
  /** CSS Level 1 */
  CSS1 ("CSS1", ECSSSpecificationStatus.COMPLETED, "http://www.w3.org/TR/CSS1/"),
  /** CSS Print Profile */
  CSS_PRINT ("css-print", ECSSSpecificationStatus.COMPLETED, "http://www.w3.org/TR/2013/NOTE-css-print-20130314/"),
  /** Media Queries */
  CSS3_MEDIAQUERIES ("css3-mediaqueries", ECSSSpecificationStatus.COMPLETED, "http://www.w3.org/TR/2012/REC-css3-mediaqueries-20120619/"),
  /** CSS Style Attributes */
  CSS_STYLE_ATTR ("css-style-attr", ECSSSpecificationStatus.COMPLETED, "http://www.w3.org/TR/2013/REC-css-style-attr-20131107/"),

  /** CSS Backgrounds and Borders Level 3 */
  CSS3_BACKGROUND ("css3-background", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2012/CR-css3-background-20120724/"),
  /** CSS Conditional Rules Level 3 */
  CSS3_CONDITIONAL ("css3-conditional", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2013/CR-css3-conditional-20130404/"),
  /** CSS Image Values and Replaced Content Level 3 */
  CSS3_IMAGES ("css3-images", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2012/CR-css3-images-20120417/"),
  /** CSS Multi-column Layout */
  CSS3_MULTICOL ("css3-multicol", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2011/CR-css3-multicol-20110412/"),
  /** CSS Speech */
  CSS3_SPEECH ("css3-speech", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2012/CR-css3-speech-20120320/"),
  /** CSS Values and Units Level 3 */
  CSS3_VALUES ("css3-values", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2013/CR-css3-values-20130730/"),
  /** CSS Flexible Box Layout */
  CSS3_FLEXBOX ("css3-flexbox", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2014/WD-css-flexbox-1-20140925/"),
  /** CSS Text Decoration Module Level 3 */
  CSS_TEXT_DECOR_3 ("css-text-decor-3", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2013/CR-css-text-decor-3-20130801/"),
  /** CSS Cascading and Inheritance Level 3 */
  CSS3_CASCADE ("css3-cascade", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2013/CR-css-cascade-3-20131003/"),
  /** CSS Fonts Level 3 */
  CSS3_FONTS ("css3-fonts", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2013/CR-css-fonts-3-20131003/"),
  /** CSS Writing Modes Level 3 */
  CSS3_WRITING_MODES ("css3-writing-modes", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2014/CR-css-writing-modes-3-20140320/"),
  /** CSS Shapes Module Level 1 */
  CSS_SHAPES ("css-shapes-1", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2014/CR-css-shapes-1-20140320/"),
  /** CSS Masking */
  CSS_MASKING ("css-masking", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2014/CR-css-masking-1-20140826/"),
  /** CSS Mobile Profile 2.0 */
  CSS_MOBILE ("css-mobile", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2014/NOTE-css-mobile-20141014/"),

  /** CSS Animations */
  CSS3_ANIMATIONS ("css3-animations", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2013/WD-css3-animations-20130219/"),
  /** Web Animations 1.0 */
  WEB_ANIMATIONS ("web-animations", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2014/WD-web-animations-20140605/"),
  /** CSS Counter Styles Level 3 */
  CSS_COUNTER_STYLES_3 ("css-counter-styles-3", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2014/WD-css-counter-styles-3-20140826/"),
  /** CSS Text Level 3 */
  CSS3_TEXT ("css3-text", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2013/WD-css-text-3-20131010/"),
  /** CSS Fragmentation Level 3 */
  CSS3_BREAK ("css3-break", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2014/WD-css3-break-20140116/"),
  /** CSS Transforms */
  CSS3_TRANSFORMS ("css3-transforms", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2013/WD-css-transforms-1-20131126/"),
  /** CSS Transitions */
  CSS3_TRANSITIONS ("css3-transitions", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2013/WD-css3-transitions-20131119/"),
  /** Cascading Variables */
  CSS_VARIABLES ("css-variables", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2014/WD-css-variables-1-20140506/"),
  /** Compositing and Blending */
  COMPOSITING ("compositing", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2014/CR-compositing-1-20140220/"),
  /** CSS Syntax Level 3 */
  CSS3_SYNTAX ("css3-syntax", ECSSSpecificationStatus.REFINING),
  /** CSS Will Change */
  CSS_WILL_CHANGE_1 ("css-will-change-1", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2014/WD-css-will-change-1-20140429/"),

  /** CSS Box Alignment Module Level 3 */
  CSS3_ALIGN ("css3-align", ECSSSpecificationStatus.REVISING, "http://www.w3.org/TR/2013/WD-css3-align-20130514/"),
  /** CSS Grid Layout */
  CSS3_GRID_LAYOUT ("css3-grid-layout", ECSSSpecificationStatus.REVISING, "http://www.w3.org/TR/2014/WD-css-grid-1-20140513/"),
  /** CSS Paged Media Level 3 */
  CSS3_PAGE ("css3-page", ECSSSpecificationStatus.REVISING, "http://www.w3.org/TR/2013/WD-css3-page-20130314/"),
  /** CSS Basic User Interface Level 3 */
  CSS3_UI ("css3-ui", ECSSSpecificationStatus.REVISING, "http://www.w3.org/TR/2012/WD-css3-ui-20120117/"),
  /** CSSOM View */
  CSSOM_VIEW ("cssom-view", ECSSSpecificationStatus.REVISING, "http://www.w3.org/TR/2013/WD-cssom-view-20131217/"),
  /** Selectors Level 4 */
  SELECTORS4 ("selectors4", ECSSSpecificationStatus.REVISING, "http://www.w3.org/TR/2013/WD-selectors4-20130502/"),

  /** CSS Backgrounds and Borders Level 4 */
  CSS4_BACKGROUND ("css4-background", ECSSSpecificationStatus.EXPLORING),
  /** CSS Device Adaptation */
  CSS_DEVICE_ADAPT ("css-device-adapt", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2011/WD-css-device-adapt-20110915/"),
  /** CSS Exclusions */
  CSS3_EXCLUSIONS ("css3-exclusions", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2013/WD-css3-exclusions-20130528/"),
  /** Filter Effects */
  FILTER_EFFECTS ("filter-effects", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/WD-filter-effects-1-20141125/"),
  /** CSS Generated Content for Paged Media */
  CSS3_GCPM ("css3-gpcm", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/WD-css-gcpm-3-20140513/"),
  /** CSS Page Floats */
  PAGE_FLOATS ("page-floats", ECSSSpecificationStatus.EXPLORING),
  /** CSS (Grid) Template Layout */
  CSS3_LAYOUT ("css3-layout", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2011/WD-css3-layout-20111129/"),
  /** CSS Intrinsic &amp; Extrinsic Sizing Module Level 3 */
  CSS3_SIZING ("css3-sizing", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2012/WD-css3-sizing-20120927/"),
  /** CSS Line Grid */
  CSS_LINE_GRID ("css-line-grid", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/WD-css-line-grid-1-20140916/"),
  /** CSS Lists Level 3 */
  CSS3_LISTS ("css3-lists", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/WD-css-lists-3-20140320/"),
  /** CSS Positioned Layout Level 3 */
  CSS3_POSITIONING ("css3-positioning", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2012/WD-css3-positioning-20120207/"),
  /** CSS Regions */
  CSS3_REGIONS ("css3-regions", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/WD-css-regions-1-20141009/"),
  /** CSS Ruby */
  CSS3_RUBY ("css3-ruby", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/WD-css-ruby-1-20140805/"),
  /** CSS Tables Level 3 */
  CSS3_TABLES ("css3-tables", ECSSSpecificationStatus.EXPLORING),
  /** CSS Object Model */
  CSSOM ("cssom", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2013/WD-cssom-20131205/"),
  /** CSS Overflow */
  CSS_OVERFLOW_3 ("css-overflow-3", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2013/WD-css-overflow-3-20130418/"),
  /** CSS Font Loading */
  CSS_FONT_LOADING_3 ("css-font-loading-3", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/WD-css-font-loading-3-20140522/"),
  /** CSS Display */
  CSS_DISPLAY_3 ("css-display-3", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/WD-css-display-3-20140911/"),
  /** CSS Scoping */
  CSS_SCOPING_1 ("css-scoping-1", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/WD-css-scoping-1-20140403/"),
  /** Media Queries level 4 */
  CSS3_MEDIAQUERIES_4 ("css3-mediaqueries-4", ECSSSpecificationStatus.EXPLORING),
  /** Non-element Selectors */
  SELECTORS_NONELEMENT_1 ("selectors-nonelement-1", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/WD-selectors-nonelement-1-20140603/"),
  /** Geometry Interfaces Module Level 1 */
  GEOMETRY_1 ("geometry-1", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2014/CR-geometry-1-20141125/"),

  /** CSS Basic Box Model Level 3 */
  CSS3_BOX ("css3-box", ECSSSpecificationStatus.REWRITING),
  /** CSS Generated Content Level 3 */
  CSS3_CONTENT ("css3-content", ECSSSpecificationStatus.REWRITING, "http://www.w3.org/TR/2003/WD-css3-content-20030514/"),
  /** CSS Line Layout Level 3 */
  CSS3_LINEBOX ("css3-linebox", ECSSSpecificationStatus.REWRITING, "http://www.w3.org/TR/2002/WD-css3-linebox-20020515/"),

  /** The CSS 'Reader' Media Type - Abandoned */
  @Deprecated CSS3_READER ("css3-reader", ECSSSpecificationStatus.ABANDONED),
  /** CSS Presentation Levels - Abandoned */
  @Deprecated CSS3_PRESLEV ("css3-preslev", ECSSSpecificationStatus.ABANDONED),
  /** CSS TV Profile 1.0 - Abandoned */
  @Deprecated CSS_TV ("css-tv", ECSSSpecificationStatus.ABANDONED),
  /** CSS Marquee - Abandoned */
  @Deprecated CSS3_MARQUEE ("css3-marquee", ECSSSpecificationStatus.ABANDONED, "http://www.w3.org/TR/2014/NOTE-css3-marquee-20141014/"),
  /** Behavioral Extensions to CSS - Abandoned */
  @Deprecated BECSS ("becss", ECSSSpecificationStatus.ABANDONED),
  /** CSS Hyperlink Presentation - Abandoned */
  @Deprecated CSS3_HYPERLINKS ("css3-hyperlinks", ECSSSpecificationStatus.ABANDONED, "http://www.w3.org/TR/2004/WD-css3-hyperlinks-20040224/"),
  /** CSS Grid Positioning - Abandoned */
  @Deprecated CSS3_GRID ("css3-grid", ECSSSpecificationStatus.ABANDONED, "http://www.w3.org/TR/2007/WD-css3-grid-20070905/"),
  /** Fullscreen - Abandoned */
  @Deprecated FULLSCREEN ("fullscreen", ECSSSpecificationStatus.ABANDONED, "http://www.w3.org/TR/2014/NOTE-fullscreen-20141118/"),

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
