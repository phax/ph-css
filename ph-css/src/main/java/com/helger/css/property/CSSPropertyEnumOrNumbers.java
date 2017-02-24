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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.regex.RegExHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.property.customizer.ICSSPropertyCustomizer;
import com.helger.css.utils.CSSNumberHelper;

/**
 * CSS property that is a list of either an enumeration or a numeric value (e.g.
 * margin)
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSPropertyEnumOrNumbers extends CSSPropertyEnum
{
  private final boolean m_bWithPercentage;
  private final int m_nMinNumbers;
  private final int m_nMaxNumbers;

  public CSSPropertyEnumOrNumbers (@Nonnull final ECSSProperty eProp,
                                   final boolean bWithPercentage,
                                   @Nonnegative final int nMinNumbers,
                                   @Nonnegative final int nMaxNumbers,
                                   @Nonnull @Nonempty final String... aEnumValues)
  {
    this (eProp, (ICSSPropertyCustomizer) null, bWithPercentage, nMinNumbers, nMaxNumbers, aEnumValues);
  }

  public CSSPropertyEnumOrNumbers (@Nonnull final ECSSProperty eProp,
                                   @Nullable final ICSSPropertyCustomizer aCustomizer,
                                   final boolean bWithPercentage,
                                   @Nonnegative final int nMinNumbers,
                                   @Nonnegative final int nMaxNumbers,
                                   @Nonnull @Nonempty final String... aEnumValues)
  {
    this (eProp, (ECSSVendorPrefix) null, aCustomizer, bWithPercentage, nMinNumbers, nMaxNumbers, aEnumValues);
  }

  public CSSPropertyEnumOrNumbers (@Nonnull final ECSSProperty eProp,
                                   @Nullable final ECSSVendorPrefix eVendorPrefix,
                                   @Nullable final ICSSPropertyCustomizer aCustomizer,
                                   final boolean bWithPercentage,
                                   @Nonnegative final int nMinNumbers,
                                   @Nonnegative final int nMaxNumbers,
                                   @Nonnull @Nonempty final String... aEnumValues)
  {
    super (eProp, eVendorPrefix, aCustomizer, aEnumValues);
    ValueEnforcer.isGT0 (nMinNumbers, "MinNumbers");
    ValueEnforcer.isGT0 (nMaxNumbers, "MaxNumbers");
    if (nMaxNumbers < nMinNumbers)
      throw new IllegalArgumentException ("MaxNumbers (" +
                                          nMaxNumbers +
                                          ") must be >= MinNumbers (" +
                                          nMinNumbers +
                                          ")");
    m_bWithPercentage = bWithPercentage;
    m_nMinNumbers = nMinNumbers;
    m_nMaxNumbers = nMaxNumbers;
  }

  public CSSPropertyEnumOrNumbers (@Nonnull final ECSSProperty eProp,
                                   final boolean bWithPercentage,
                                   @Nonnegative final int nMinNumbers,
                                   @Nonnegative final int nMaxNumbers,
                                   @Nonnull @Nonempty final Iterable <String> aEnumValues)
  {
    this (eProp, (ICSSPropertyCustomizer) null, bWithPercentage, nMinNumbers, nMaxNumbers, aEnumValues);
  }

  public CSSPropertyEnumOrNumbers (@Nonnull final ECSSProperty eProp,
                                   @Nullable final ICSSPropertyCustomizer aCustomizer,
                                   final boolean bWithPercentage,
                                   @Nonnegative final int nMinNumbers,
                                   @Nonnegative final int nMaxNumbers,
                                   @Nonnull @Nonempty final Iterable <String> aEnumValues)
  {
    this (eProp, (ECSSVendorPrefix) null, aCustomizer, bWithPercentage, nMinNumbers, nMaxNumbers, aEnumValues);
  }

  public CSSPropertyEnumOrNumbers (@Nonnull final ECSSProperty eProp,
                                   @Nullable final ECSSVendorPrefix eVendorPrefix,
                                   @Nullable final ICSSPropertyCustomizer aCustomizer,
                                   final boolean bWithPercentage,
                                   @Nonnegative final int nMinNumbers,
                                   @Nonnegative final int nMaxNumbers,
                                   @Nonnull @Nonempty final Iterable <String> aEnumValues)
  {
    super (eProp, eVendorPrefix, aCustomizer, aEnumValues);
    ValueEnforcer.isGT0 (nMinNumbers, "MinNumbers");
    ValueEnforcer.isGT0 (nMaxNumbers, "MaxNumbers");
    if (nMaxNumbers < nMinNumbers)
      throw new IllegalArgumentException ("MaxNumbers (" +
                                          nMaxNumbers +
                                          ") must be >= MinNumbers (" +
                                          nMinNumbers +
                                          ")");
    m_bWithPercentage = bWithPercentage;
    m_nMinNumbers = nMinNumbers;
    m_nMaxNumbers = nMaxNumbers;
  }

  @Override
  public int getMinimumArgumentCount ()
  {
    return m_nMinNumbers;
  }

  @Override
  public int getMaximumArgumentCount ()
  {
    return m_nMaxNumbers;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public boolean isValidValue (@Nullable final String sValue)
  {
    if (sValue == null)
      return false;

    // Split by whitespaces " a b " results in { "a", "b" }
    final String [] aParts = RegExHelper.getSplitToArray (sValue.trim (), "\\s+");
    if (aParts.length < m_nMinNumbers || aParts.length > m_nMaxNumbers)
      return false;

    for (final String aPart : aParts)
    {
      final String sTrimmedPart = aPart.trim ();
      if (!super.isValidValue (sTrimmedPart) && !CSSNumberHelper.isValueWithUnit (sTrimmedPart, m_bWithPercentage))
        return false;
    }
    return true;
  }

  @Override
  @Nonnull
  public CSSPropertyEnumOrNumbers getClone (@Nonnull final ECSSProperty eProp)
  {
    return new CSSPropertyEnumOrNumbers (eProp,
                                         getVendorPrefix (),
                                         getCustomizer (),
                                         m_bWithPercentage,
                                         m_nMinNumbers,
                                         m_nMaxNumbers,
                                         directGetEnumValues ());
  }

  @Override
  @Nonnull
  public CSSPropertyEnumOrNumbers getClone (@Nullable final ECSSVendorPrefix eVendorPrefix)
  {
    return new CSSPropertyEnumOrNumbers (getProp (),
                                         eVendorPrefix,
                                         getCustomizer (),
                                         m_bWithPercentage,
                                         m_nMinNumbers,
                                         m_nMaxNumbers,
                                         directGetEnumValues ());
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final CSSPropertyEnumOrNumbers rhs = (CSSPropertyEnumOrNumbers) o;
    return m_bWithPercentage == rhs.m_bWithPercentage &&
           m_nMinNumbers == rhs.m_nMinNumbers &&
           m_nMaxNumbers == rhs.m_nMaxNumbers;
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (m_bWithPercentage)
                            .append (m_nMinNumbers)
                            .append (m_nMaxNumbers)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("withPercentage", m_bWithPercentage)
                            .append ("minNumbers", m_nMinNumbers)
                            .append ("maxNumbers", m_nMaxNumbers)
                            .getToString ();
  }
}
