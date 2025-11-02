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

import com.helger.annotation.OverridingMethodsMustInvokeSuper;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.property.customizer.ICSSPropertyCustomizer;
import com.helger.css.utils.CSSNumberHelper;

/**
 * CSS property that is a number (e.g. line-height)
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSPropertyNumber extends AbstractCSSProperty
{
  private final boolean m_bWithPercentage;

  public CSSPropertyNumber (@NonNull final ECSSProperty eProp, final boolean bWithPercentage)
  {
    this (eProp, (ICSSPropertyCustomizer) null, bWithPercentage);
  }

  public CSSPropertyNumber (@NonNull final ECSSProperty eProp,
                            @Nullable final ICSSPropertyCustomizer aCustomizer,
                            final boolean bWithPercentage)
  {
    this (eProp, (ECSSVendorPrefix) null, aCustomizer, bWithPercentage);
  }

  public CSSPropertyNumber (@NonNull final ECSSProperty eProp,
                            @Nullable final ECSSVendorPrefix eVendorPrefix,
                            @Nullable final ICSSPropertyCustomizer aCustomizer,
                            final boolean bWithPercentage)
  {
    super (eProp, eVendorPrefix, aCustomizer);
    m_bWithPercentage = bWithPercentage;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public boolean isValidValue (@Nullable final String sValue)
  {
    return super.isValidValue (sValue) || CSSNumberHelper.isValueWithUnit (sValue, m_bWithPercentage);
  }

  @NonNull
  public CSSPropertyNumber getClone (@NonNull final ECSSProperty eProp)
  {
    return new CSSPropertyNumber (eProp, getVendorPrefix (), getCustomizer (), m_bWithPercentage);
  }

  @NonNull
  public CSSPropertyNumber getClone (@Nullable final ECSSVendorPrefix eVendorPrefix)
  {
    return new CSSPropertyNumber (getProp (), eVendorPrefix, getCustomizer (), m_bWithPercentage);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final CSSPropertyNumber rhs = (CSSPropertyNumber) o;
    return m_bWithPercentage == rhs.m_bWithPercentage;
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_bWithPercentage).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("withPercentage", m_bWithPercentage).getToString ();
  }
}
