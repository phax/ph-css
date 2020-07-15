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
package com.helger.css.property;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.regex.RegExHelper;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.property.customizer.ICSSPropertyCustomizer;
import com.helger.css.utils.CSSColorHelper;

/**
 * CSS property that is a list of color values (e.g. border-color)
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSPropertyColors extends AbstractCSSProperty
{
  private final int m_nMinArgCount;
  private final int m_nMaxArgCount;

  public CSSPropertyColors (@Nonnull final ECSSProperty eProp, @Nonnegative final int nMinArgCount, @Nonnegative final int nMaxArgCount)
  {
    this (eProp, (ICSSPropertyCustomizer) null, nMinArgCount, nMaxArgCount);
  }

  public CSSPropertyColors (@Nonnull final ECSSProperty eProp,
                            @Nullable final ICSSPropertyCustomizer aCustomizer,
                            @Nonnegative final int nMinArgCount,
                            @Nonnegative final int nMaxArgCount)
  {
    this (eProp, (ECSSVendorPrefix) null, aCustomizer, nMinArgCount, nMaxArgCount);
  }

  public CSSPropertyColors (@Nonnull final ECSSProperty eProp,
                            @Nullable final ECSSVendorPrefix eVendorPrefix,
                            @Nullable final ICSSPropertyCustomizer aCustomizer,
                            @Nonnegative final int nMinArgCount,
                            @Nonnegative final int nMaxArgCount)
  {
    super (eProp, eVendorPrefix, aCustomizer);
    ValueEnforcer.isGT0 (nMinArgCount, "MinNumbers");
    ValueEnforcer.isGT0 (nMaxArgCount, "MaxNumbers");
    ValueEnforcer.isTrue (nMinArgCount <= nMaxArgCount,
                          () -> "MaxArgCount (" + nMaxArgCount + ") must be >= MinArgCount (" + nMinArgCount + ")");
    m_nMinArgCount = nMinArgCount;
    m_nMaxArgCount = nMaxArgCount;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public boolean isValidValue (@Nullable final String sValue)
  {
    if (sValue == null)
      return false;

    // Split by whitespaces " a b " results in { "a", "b" }
    final String [] aParts = RegExHelper.getSplitToArray (sValue.trim (), "\\s+");
    if (aParts.length < getMinimumArgumentCount () || aParts.length > getMaximumArgumentCount ())
      return false;

    // Check each value
    for (final String aPart : aParts)
    {
      final String sTrimmedPart = aPart.trim ();
      if (!super.isValidValue (sTrimmedPart) && !CSSColorHelper.isColorValue (sTrimmedPart))
        return false;
    }
    return true;
  }

  @Override
  @Nonnull
  public CSSPropertyColors getClone (@Nonnull final ECSSProperty eProp)
  {
    return new CSSPropertyColors (eProp, getVendorPrefix (), getCustomizer (), getMinimumArgumentCount (), getMaximumArgumentCount ());
  }

  @Override
  @Nonnull
  public CSSPropertyColors getClone (@Nullable final ECSSVendorPrefix eVendorPrefix)
  {
    return new CSSPropertyColors (getProp (), eVendorPrefix, getCustomizer (), getMinimumArgumentCount (), getMaximumArgumentCount ());
  }
}
