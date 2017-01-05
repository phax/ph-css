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
package com.helger.css.utils;

import javax.annotation.Nullable;
import javax.annotation.RegEx;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.commons.regex.RegExHelper;
import com.helger.commons.string.StringHelper;
import com.helger.css.decl.CSSRect;
import com.helger.css.propertyvalue.CCSSValue;

/**
 * Provides rectangle handling sanity methods.
 *
 * @author Philip Helger
 */
@Immutable
public final class CSSRectHelper
{
  // Possible values are: "0", "0px", "0%" or "auto"
  // "?:" - non-capturing group
  @RegEx
  private static final String PATTERN_PART_VALUE = "([0-9]+(?:[a-zA-Z]+|%)?|auto)";
  // Do not use a recurring group (...{3}) so that capturing works!
  @RegEx
  private static final String PATTERN_CURRENT_SYNTAX = "^" +
                                                       CCSSValue.PREFIX_RECT +
                                                       "\\s*\\(\\s*" +
                                                       PATTERN_PART_VALUE +
                                                       "\\s*,\\s*" +
                                                       PATTERN_PART_VALUE +
                                                       "\\s*,\\s*" +
                                                       PATTERN_PART_VALUE +
                                                       "\\s*,\\s*" +
                                                       PATTERN_PART_VALUE +
                                                       "\\s*\\)$";
  @RegEx
  private static final String PATTERN_OLD_SYNTAX = "^" +
                                                   CCSSValue.PREFIX_RECT +
                                                   "\\s*\\(\\s*" +
                                                   PATTERN_PART_VALUE +
                                                   "\\s+" +
                                                   PATTERN_PART_VALUE +
                                                   "\\s+" +
                                                   PATTERN_PART_VALUE +
                                                   "\\s+" +
                                                   PATTERN_PART_VALUE +
                                                   "\\s*\\)$";

  @PresentForCodeCoverage
  private static final CSSRectHelper s_aInstance = new CSSRectHelper ();

  private CSSRectHelper ()
  {}

  /**
   * Check if the passed value is CSS rectangle definition or not. It checks
   * both the current syntax (<code>rect(a,b,c,d)</code>) and the old syntax (
   * <code>rect(a b c d)</code>).
   *
   * @param sCSSValue
   *        The value to check. May be <code>null</code>.
   * @return <code>true</code> if the passed value is a rect value,
   *         <code>false</code> if not
   */
  public static boolean isRectValue (@Nullable final String sCSSValue)
  {
    final String sRealValue = StringHelper.trim (sCSSValue);
    if (StringHelper.hasText (sRealValue))
    {
      // Current syntax: rect(a,b,c,d)
      if (RegExHelper.stringMatchesPattern (PATTERN_CURRENT_SYNTAX, sRealValue))
        return true;

      // Backward compatible syntax: rect(a b c d)
      if (RegExHelper.stringMatchesPattern (PATTERN_OLD_SYNTAX, sRealValue))
        return true;
    }
    return false;
  }

  /**
   * Get all the values from within a CSS rectangle definition.
   *
   * @param sCSSValue
   *        The CSS values to check. May be <code>null</code>.
   * @return <code>null</code> if the passed String is not a CSS rectangle. An
   *         array with 4 Strings if the passed value is a CSS rectangle.
   */
  @Nullable
  public static String [] getRectValues (@Nullable final String sCSSValue)
  {
    String [] ret = null;
    final String sRealValue = StringHelper.trim (sCSSValue);
    if (StringHelper.hasText (sRealValue))
    {
      ret = RegExHelper.getAllMatchingGroupValues (PATTERN_CURRENT_SYNTAX, sRealValue);
      if (ret == null)
        ret = RegExHelper.getAllMatchingGroupValues (PATTERN_OLD_SYNTAX, sRealValue);
    }
    return ret;
  }

  /**
   * Interpret the passed value as a CSS rectangle and convert it to a
   * {@link CSSRect}.
   *
   * @param sCSSValue
   *        The CSS values to check. May be <code>null</code>.
   * @return <code>null</code> if the passed String is not a CSS rectangle.
   */
  @Nullable
  public static CSSRect getAsRect (@Nullable final String sCSSValue)
  {
    final String [] aValues = getRectValues (sCSSValue);
    return aValues == null ? null : new CSSRect (aValues[0], aValues[1], aValues[2], aValues[3]);
  }
}
