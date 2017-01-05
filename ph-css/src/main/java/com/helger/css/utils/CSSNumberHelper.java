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

import java.math.BigDecimal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.collection.ext.CommonsHashMap;
import com.helger.commons.collection.ext.ICommonsMap;
import com.helger.commons.compare.IComparator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.StringParser;
import com.helger.css.ECSSUnit;
import com.helger.css.propertyvalue.CSSSimpleValueWithUnit;

/**
 * Provides number handling sanity methods.
 *
 * @author Philip Helger
 */
@Immutable
public final class CSSNumberHelper
{
  // Map from unit name to enum
  private static final ICommonsMap <String, ECSSUnit> s_aNameToUnitMap;

  static
  {
    final ICommonsMap <String, ECSSUnit> aNameToUnitMap = new CommonsHashMap<> ();
    for (final ECSSUnit eUnit : ECSSUnit.values ())
      aNameToUnitMap.put (eUnit.getName (), eUnit);
    // Now sort, so that the longest matches are upfront so that they are
    // determined first
    s_aNameToUnitMap = aNameToUnitMap.getSortedByKey (IComparator.getComparatorStringLongestFirst ());
  }

  @SuppressWarnings ("unused")
  private static final CSSNumberHelper s_aInstance = new CSSNumberHelper ();

  private CSSNumberHelper ()
  {}

  /**
   * Try to find the unit that is used in the specified values. This check is
   * done using "endsWith" so you have to make sure, that no trailing spaces are
   * contained in the passed value. This check includes a check for percentage
   * values (e.g. <code>10%</code>)
   *
   * @param sCSSValue
   *        The value to check. May not be <code>null</code>.
   * @return <code>null</code> if no matching unit from {@link ECSSUnit} was
   *         found.
   * @see #getMatchingUnitExclPercentage(String)
   */
  @Nullable
  public static ECSSUnit getMatchingUnitInclPercentage (@Nonnull final String sCSSValue)
  {
    ValueEnforcer.notNull (sCSSValue, "CSSValue");
    // Search units, the ones with the longest names come first
    return s_aNameToUnitMap.findFirstValue (aEntry -> sCSSValue.endsWith (aEntry.getKey ()));
  }

  /**
   * Try to find the unit that is used in the specified values. This check is
   * done using "endsWith" so you have to make sure, that no trailing spaces are
   * contained in the passed value. This check excludes a check for percentage
   * values (e.g. <code>10%</code>)
   *
   * @param sCSSValue
   *        The value to check. May not be <code>null</code>.
   * @return <code>null</code> if no matching unit from {@link ECSSUnit} was
   *         found.
   * @see #getMatchingUnitInclPercentage(String)
   */
  @Nullable
  public static ECSSUnit getMatchingUnitExclPercentage (@Nonnull final String sCSSValue)
  {
    final ECSSUnit eUnit = getMatchingUnitInclPercentage (sCSSValue);
    return eUnit == null || eUnit == ECSSUnit.PERCENTAGE ? null : eUnit;
  }

  /**
   * Check if the passed value is a pure numeric value without a unit.
   *
   * @param sCSSValue
   *        The value to be checked. May be <code>null</code> and is
   *        automatically trimmed inside.
   * @return <code>true</code> if the passed value is a pure decimal numeric
   *         value after trimming, <code>false</code> otherwise.
   */
  public static boolean isNumberValue (@Nullable final String sCSSValue)
  {
    final String sRealValue = StringHelper.trim (sCSSValue);
    return StringHelper.hasText (sRealValue) && StringParser.isDouble (sRealValue);
  }

  /**
   * Check if the passed string value consists of a numeric value and a unit as
   * in <code>5px</code>. This method includes the percentage unit.
   *
   * @param sCSSValue
   *        The value to be parsed. May be <code>null</code> and is trimmed
   *        inside this method.
   * @return <code>true</code> if the passed value consist of a number and a
   *         unit, <code>false</code> otherwise.
   * @see #getValueWithUnit(String)
   * @see #isValueWithUnit(String, boolean)
   */
  public static boolean isValueWithUnit (@Nullable final String sCSSValue)
  {
    return isValueWithUnit (sCSSValue, true);
  }

  /**
   * Check if the passed string value consists of a numeric value and a unit as
   * in <code>5px</code>.
   *
   * @param sCSSValue
   *        The value to be parsed. May be <code>null</code> and is trimmed
   *        inside this method.
   * @param bWithPerc
   *        <code>true</code> to include the percentage unit, <code>false</code>
   *        to exclude the percentage unit.
   * @return <code>true</code> if the passed value consist of a number and a
   *         unit, <code>false</code> otherwise.
   * @see #getValueWithUnit(String, boolean)
   */
  public static boolean isValueWithUnit (@Nullable final String sCSSValue, final boolean bWithPerc)
  {
    return getValueWithUnit (sCSSValue, bWithPerc) != null;
  }

  /**
   * Convert the passed string value with unit into a structured
   * {@link CSSSimpleValueWithUnit}. Example: parsing <code>5px</code> will
   * result in the numeric value <code>5</code> and the unit
   * <code>ECSSUnit.PX</code>. The special value "0" is returned with the unit
   * "px". This method includes the percentage unit.
   *
   * @param sCSSValue
   *        The value to be parsed. May be <code>null</code> and is trimmed
   *        inside this method.
   * @return <code>null</code> if the passed value could not be converted to
   *         value and unit.
   */
  @Nullable
  public static CSSSimpleValueWithUnit getValueWithUnit (@Nullable final String sCSSValue)
  {
    return getValueWithUnit (sCSSValue, true);
  }

  /**
   * Convert the passed string value with unit into a structured
   * {@link CSSSimpleValueWithUnit}. Example: parsing <code>5px</code> will
   * result in the numeric value <code>5</code> and the unit
   * <code>ECSSUnit.PX</code>. The special value "0" is returned with the unit
   * "px".
   *
   * @param sCSSValue
   *        The value to be parsed. May be <code>null</code> and is trimmed
   *        inside this method.
   * @param bWithPerc
   *        <code>true</code> to include the percentage unit, <code>false</code>
   *        to exclude the percentage unit.
   * @return <code>null</code> if the passed value could not be converted to
   *         value and unit.
   */
  @Nullable
  public static CSSSimpleValueWithUnit getValueWithUnit (@Nullable final String sCSSValue, final boolean bWithPerc)
  {
    String sRealValue = StringHelper.trim (sCSSValue);
    if (StringHelper.hasText (sRealValue))
    {
      // Special case for 0!
      if (sRealValue.equals ("0"))
        return new CSSSimpleValueWithUnit (BigDecimal.ZERO, ECSSUnit.PX);

      final ECSSUnit eUnit = bWithPerc ? getMatchingUnitInclPercentage (sRealValue)
                                       : getMatchingUnitExclPercentage (sRealValue);
      if (eUnit != null)
      {
        // Cut the unit off
        sRealValue = sRealValue.substring (0, sRealValue.length () - eUnit.getName ().length ()).trim ();
        final BigDecimal aValue = StringParser.parseBigDecimal (sRealValue);
        if (aValue != null)
          return new CSSSimpleValueWithUnit (aValue, eUnit);
      }
    }
    return null;
  }
}
