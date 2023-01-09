/*
 * Copyright (C) 2014-2023 Philip Helger (www.helger.com)
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
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.property.customizer.ICSSPropertyCustomizer;

/**
 * CSS properties with a dynamic number of entries.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public abstract class AbstractCSSPropertyEnums extends CSSPropertyEnum
{
  private final int m_nMinArgCount;
  private final int m_nMaxArgCount;

  public AbstractCSSPropertyEnums (@Nonnull final ECSSProperty eProp,
                                   @Nullable final ECSSVendorPrefix eVendorPrefix,
                                   @Nullable final ICSSPropertyCustomizer aCustomizer,
                                   @Nonnegative final int nMinArgCount,
                                   @Nonnegative final int nMaxArgCount,
                                   @Nonnull @Nonempty final String... aEnumValues)
  {
    super (eProp, eVendorPrefix, aCustomizer, aEnumValues);
    ValueEnforcer.isGT0 (nMinArgCount, "MinArgCount");
    ValueEnforcer.isGT0 (nMaxArgCount, "MaxArgCount");
    ValueEnforcer.isTrue (nMinArgCount <= nMaxArgCount,
                          () -> "MaxArgCount (" + nMaxArgCount + ") must be >= MinArgCount (" + nMinArgCount + ")");
    m_nMinArgCount = nMinArgCount;
    m_nMaxArgCount = nMaxArgCount;
  }

  public AbstractCSSPropertyEnums (@Nonnull final ECSSProperty eProp,
                                   @Nullable final ECSSVendorPrefix eVendorPrefix,
                                   @Nullable final ICSSPropertyCustomizer aCustomizer,
                                   @Nonnegative final int nMinArgCount,
                                   @Nonnegative final int nMaxArgCount,
                                   @Nonnull @Nonempty final Iterable <String> aEnumValues)
  {
    super (eProp, eVendorPrefix, aCustomizer, aEnumValues);
    ValueEnforcer.isGT0 (nMinArgCount, "MinArgCount");
    ValueEnforcer.isGT0 (nMaxArgCount, "MaxArgCount");
    ValueEnforcer.isTrue (nMinArgCount <= nMaxArgCount,
                          () -> "MaxArgCount (" + nMaxArgCount + ") must be >= MinArgCount (" + nMinArgCount + ")");
    m_nMinArgCount = nMinArgCount;
    m_nMaxArgCount = nMaxArgCount;
  }

  @Override
  public final int getMinimumArgumentCount ()
  {
    return m_nMinArgCount;
  }

  @Override
  public final int getMaximumArgumentCount ()
  {
    return m_nMaxArgCount;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final AbstractCSSPropertyEnums rhs = (AbstractCSSPropertyEnums) o;
    return m_nMinArgCount == rhs.m_nMinArgCount && m_nMaxArgCount == rhs.m_nMaxArgCount;
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_nMinArgCount).append (m_nMaxArgCount).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("MinArgCount", m_nMinArgCount)
                            .append ("MaxArgCount", m_nMaxArgCount)
                            .getToString ();
  }
}
