/*
 * Copyright (C) 2014-2025 Philip Helger (www.helger.com)
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

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonnegative;
import com.helger.annotation.OverridingMethodsMustInvokeSuper;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.cache.regex.RegExHelper;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.property.customizer.ICSSPropertyCustomizer;
import com.helger.css.utils.CSSNumberHelper;

/**
 * CSS property with a list of numbers (e.g. padding)
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSPropertyNumbers extends AbstractCSSProperty
{
  private final boolean m_bWithPercentage;
  private final int m_nMinArgCount;
  private final int m_nMaxArgCount;

  public CSSPropertyNumbers (@NonNull final ECSSProperty eProp,
                             final boolean bWithPercentage,
                             @Nonnegative final int nMinArgCount,
                             @Nonnegative final int nMaxArgCount)
  {
    this (eProp, (ICSSPropertyCustomizer) null, bWithPercentage, nMinArgCount, nMaxArgCount);
  }

  public CSSPropertyNumbers (@NonNull final ECSSProperty eProp,
                             @Nullable final ICSSPropertyCustomizer aCustomizer,
                             final boolean bWithPercentage,
                             @Nonnegative final int nMinArgCount,
                             @Nonnegative final int nMaxArgCount)
  {
    this (eProp, (ECSSVendorPrefix) null, aCustomizer, bWithPercentage, nMinArgCount, nMaxArgCount);
  }

  public CSSPropertyNumbers (@NonNull final ECSSProperty eProp,
                             @Nullable final ECSSVendorPrefix eVendorPrefix,
                             @Nullable final ICSSPropertyCustomizer aCustomizer,
                             final boolean bWithPercentage,
                             @Nonnegative final int nMinArgCount,
                             @Nonnegative final int nMaxArgCount)
  {
    super (eProp, eVendorPrefix, aCustomizer);
    ValueEnforcer.isGT0 (nMinArgCount, "MinNumbers");
    ValueEnforcer.isGT0 (nMaxArgCount, "MaxNumbers");
    ValueEnforcer.isTrue (nMinArgCount <= nMaxArgCount,
                          () -> "MaxArgCount (" + nMaxArgCount + ") must be >= MinArgCount (" + nMinArgCount + ")");
    m_bWithPercentage = bWithPercentage;
    m_nMinArgCount = nMinArgCount;
    m_nMaxArgCount = nMaxArgCount;
  }

  @Override
  public int getMinimumArgumentCount ()
  {
    return m_nMinArgCount;
  }

  @Override
  public int getMaximumArgumentCount ()
  {
    return m_nMaxArgCount;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public boolean isValidValue (@Nullable final String sValue)
  {
    if (super.isValidValue (sValue))
      return true;

    if (sValue == null)
      return false;

    // Split by whitespaces
    final String [] aParts = RegExHelper.getSplitToArray (sValue.trim (), "\\s+");
    if (aParts.length < m_nMinArgCount || aParts.length > m_nMaxArgCount)
      return false;

    // Check if each part is a valid number
    for (final String sPart : aParts)
      if (!CSSNumberHelper.isValueWithUnit (sPart.trim (), m_bWithPercentage))
        return false;
    return true;
  }

  @NonNull
  public CSSPropertyNumbers getClone (@NonNull final ECSSProperty eProp)
  {
    return new CSSPropertyNumbers (eProp, getVendorPrefix (), getCustomizer (), m_bWithPercentage, m_nMinArgCount, m_nMaxArgCount);
  }

  @NonNull
  public CSSPropertyNumbers getClone (@Nullable final ECSSVendorPrefix eVendorPrefix)
  {
    return new CSSPropertyNumbers (getProp (), eVendorPrefix, getCustomizer (), m_bWithPercentage, m_nMinArgCount, m_nMaxArgCount);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final CSSPropertyNumbers rhs = (CSSPropertyNumbers) o;
    return m_bWithPercentage == rhs.m_bWithPercentage && m_nMinArgCount == rhs.m_nMinArgCount && m_nMaxArgCount == rhs.m_nMaxArgCount;
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (m_bWithPercentage)
                            .append (m_nMinArgCount)
                            .append (m_nMaxArgCount)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("WithPercentage", m_bWithPercentage)
                            .append ("MinArgCount", m_nMinArgCount)
                            .append ("MaxArgCount", m_nMaxArgCount)
                            .getToString ();
  }
}
