/**
 * Copyright (C) 2014 Philip Helger (www.helger.com)
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

import com.helger.commons.annotations.Nonempty;
import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;
import com.helger.commons.string.StringHelper;

/**
 * Enumeration containing all W3C CSS specifications.<br>
 * Source: http://www.w3.org/Style/CSS/current-work<br>
 * Last update: 27.8.2013<br>
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
  CSS3_CONDITIONAL ("css3-conditional", ECSSSpecificationStatus.TESTING),
  /** CSS Image Values and Replaced Content Level 3 */
  CSS3_IMAGES ("css3-images", ECSSSpecificationStatus.TESTING),
  /** CSS Marquee */
  CSS3_MARQUEE ("css3-marquee", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2008/CR-css3-marquee-20081205/"),
  /** CSS Multi-column Layout */
  CSS3_MULTICOL ("css3-multicol", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2011/CR-css3-multicol-20110412/"),
  /** CSS Speech */
  CSS3_SPEECH ("css3-speech", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2012/CR-css3-speech-20120320/"),
  /** CSS Values and Units Level 3 */
  CSS3_VALUES ("css3-values", ECSSSpecificationStatus.TESTING),
  /** CSS Flexible Box Layout */
  CSS3_FLEXBOX ("css3-flexbox", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2012/CR-css3-flexbox-20120918/"),
  /** CSS Text Decoration Module Level 3 */
  CSS_TEXT_DECOR_3 ("css-text-decor-3", ECSSSpecificationStatus.TESTING),
  /** CSS Cascading and Inheritance Level 3 */
  CSS3_CASCADE ("css3-cascade", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2013/CR-css-cascade-3-20131003/"),
  /** CSS Fonts Level 3 */
  CSS3_FONTS ("css3-fonts", ECSSSpecificationStatus.TESTING, "http://www.w3.org/TR/2013/CR-css-fonts-3-20131003/"),
  /** CSS Mobile Profile 2.0 */
  CSS_MOBILE ("css-mobile", ECSSSpecificationStatus.TESTING),
  /** CSS TV Profile 1.0 */
  CSS_TV ("css-tv", ECSSSpecificationStatus.TESTING),

  /** CSS Animations */
  CSS3_ANIMATIONS ("css3-animations", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2013/WD-css3-animations-20130219/"),
  /** CSS Counter Styles Level 3 */
  CSS_COUNTER_STYLES_3 ("css-counter-styles-3", ECSSSpecificationStatus.REFINING),
  /** CSS Masking */
  CSS_MASKING ("css-masking", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2013/WD-css-masking-1-20131029/"),
  /** CSS Text Level 3 */
  CSS3_TEXT ("css3-text", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2012/WD-css3-text-20121113/"),
  /** CSS Fragmentation Level 3 */
  CSS3_BREAK ("css3-break", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2012/WD-css3-break-20120823/"),
  /** CSS Shapes Module Level 1 */
  CSS_SHAPES ("css-shapes-1", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2013/WD-css-shapes-1-20131203/"),
  /** CSS Transforms */
  CSS3_TRANSFORMS ("css3-transforms", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2012/WD-css3-transforms-20120911/"),
  /** CSS Transitions */
  CSS3_TRANSITIONS ("css3-transitions", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2013/WD-css3-transitions-20130212/"),
  /** Cascading Variables */
  CSS_VARIABLES ("css-variables", ECSSSpecificationStatus.REFINING),
  /** CSS Writing Modes Level 3 */
  CSS3_WRITING_MODES ("css3-writing-modes", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2012/WD-css3-writing-modes-20121115/"),
  /** Compositing and Blending */
  COMPOSITING ("compositing", ECSSSpecificationStatus.REFINING, "http://www.w3.org/TR/2013/WD-compositing-1-20131010/"),

  /** CSS Box Alignment Module Level 3 */
  CSS3_ALIGN ("css3-align", ECSSSpecificationStatus.REVISING, "http://www.w3.org/TR/2013/WD-css3-align-20130514/"),
  /** CSS Grid Layout */
  CSS3_GRID_LAYOUT ("css3-grid-layout", ECSSSpecificationStatus.REVISING, "http://www.w3.org/TR/2013/WD-css3-grid-layout-20130910/"),
  /** CSS Paged Media Level 3 */
  CSS3_PAGE ("css3-page", ECSSSpecificationStatus.REVISING),
  /** CSS Syntax Level 3 */
  CSS3_SYNTAX ("css3-syntax", ECSSSpecificationStatus.REVISING),
  /** CSS Basic User Interface Level 3 */
  CSS3_UI ("css3-ui", ECSSSpecificationStatus.REVISING, "http://www.w3.org/TR/2012/WD-css3-ui-20120117/"),
  /** CSSOM View */
  CSSOM_VIEW ("cssom-view", ECSSSpecificationStatus.REVISING),
  /** Selectors Level 4 */
  SELECTORS4 ("selectors4", ECSSSpecificationStatus.REVISING),

  /** CSS Backgrounds and Borders Level 4 */
  CSS4_BACKGROUND ("css4-background", ECSSSpecificationStatus.EXPLORING),
  /** CSS Device Adaptation */
  CSS_DEVICE_ADAPT ("css-device-adapt", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2011/WD-css-device-adapt-20110915/"),
  /** CSS Exclusions and Shapes */
  CSS3_EXCLUSIONS ("css3-exclusions", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2013/WD-css3-exclusions-20130528/"),
  /** Filter Effects */
  FILTER_EFFECTS ("filter-effects", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2013/WD-filter-effects-20130523/"),
  /** CSS Generated Content for Paged Media */
  CSS3_GCPM ("css3-gpcm", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2011/WD-css3-gcpm-20111129/"),
  /** CSS (Grid) Template Layout */
  CSS3_LAYOUT ("css3-layout", ECSSSpecificationStatus.EXPLORING),
  /** CSS Intrinsic & Extrinsic Sizing Module Level 3 */
  CSS3_SIZING ("css3-sizing", ECSSSpecificationStatus.EXPLORING),
  /** CSS Line Grid */
  CSS_LINE_GRID ("css-line-grid", ECSSSpecificationStatus.EXPLORING),
  /** CSS Lists Level 3 */
  CSS3_LISTS ("css3-lists", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2011/WD-css3-lists-20110524/"),
  /** CSS Positioned Layout Level 3 */
  CSS3_POSITIONING ("css3-positioning", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2012/WD-css3-positioning-20120207/"),
  /** CSS Presentation Levels */
  CSS3_PRESLEV ("css3-preslev", ECSSSpecificationStatus.EXPLORING),
  /** CSS Regions */
  CSS3_REGIONS ("css3-regions", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2013/WD-css3-regions-20130528/"),
  /** CSS Ruby */
  CSS3_RUBY ("css3-ruby", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2013/WD-css3-ruby-20130919/"),
  /** CSS Tables Level 3 */
  CSS3_TABLES ("css3-tables", ECSSSpecificationStatus.EXPLORING),
  /** CSS Object Model */
  CSSOM ("cssom", ECSSSpecificationStatus.EXPLORING),
  /** CSS Overflow */
  CSS_OVERFLOW_3 ("css-overflow-3", ECSSSpecificationStatus.EXPLORING, "http://www.w3.org/TR/2013/WD-css-overflow-3-20130418/"),

  /** CSS Basic Box Model Level 3 */
  CSS3_BOX ("css3-box", ECSSSpecificationStatus.REWRITING),
  /** CSS Generated Content Level 3 */
  CSS3_CONTENT ("css3-content", ECSSSpecificationStatus.REWRITING, "http://www.w3.org/TR/2003/WD-css3-content-20030514/"),
  /** CSS Line Layout Level 3 */
  CSS3_LINEBOX ("css3-linebox", ECSSSpecificationStatus.REWRITING, "http://www.w3.org/TR/2002/WD-css3-linebox-20020515/"),

  /** Behavioral Extensions to CSS - Abandoned */
  @Deprecated
  BECSS ("becss", ECSSSpecificationStatus.ABANDONED),
  /** CSS Hyperlink Presentation - Abandoned */
  @Deprecated
  CSS3_HYPERLINKS ("css3-hyperlinks", ECSSSpecificationStatus.ABANDONED, "http://www.w3.org/TR/2004/WD-css3-hyperlinks-20040224/"),
  /** CSS Grid Positioning - Abandoned */
  @Deprecated
  CSS3_GRID ("css3-grid", ECSSSpecificationStatus.ABANDONED, "http://www.w3.org/TR/2007/WD-css3-grid-20070905/");

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
