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

import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotation.PresentForCodeCoverage;

/**
 * Contains CSS style constants and utility stuff. Only constants that are part
 * of the CSS specification are contained in this class.<br>
 * Units of measurement are based on:
 * http://de.selfhtml.org/css/formate/wertzuweisung.htm<br>
 *
 * @author Philip Helger
 */
@Immutable
public final class CCSS
{
  /** The separator between a property and a value. (e.g. display:none) */
  public static final char SEPARATOR_PROPERTY_VALUE = ':';
  /** The separator between a property and a value. (e.g. display:none) */
  public static final String SEPARATOR_PROPERTY_VALUE_STR = Character.toString (SEPARATOR_PROPERTY_VALUE);

  /** The character to end a definition. (e.g. display:none;) */
  public static final char DEFINITION_END = ';';
  /** The character to end a definition. (e.g. display:none;) */
  public static final String DEFINITION_END_STR = Character.toString (DEFINITION_END);

  /** Regular CSS file extension */
  public static final String FILE_EXTENSION_CSS = ".css";

  /** Minified CSS file extension */
  public static final String FILE_EXTENSION_MIN_CSS = ".min.css";

  /** The "!important" suffix for property values */
  public static final String IMPORTANT_SUFFIX = " !important";

  /**
   * The maximum fraction digits to be considered when printing CSS decimal
   * values.
   *
   * @since 3.7.3
   */
  public static final int CSS_MAXIMUM_FRACTION_DIGITS = 16;

  @PresentForCodeCoverage
  private static final CCSS s_aInstance = new CCSS ();

  private CCSS ()
  {}
}
