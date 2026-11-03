/*
 * Copyright (C) 2014-2026 Philip Helger (www.helger.com)
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

import com.helger.annotation.Nonempty;
import com.helger.annotation.OverridingMethodsMustInvokeSuper;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.string.StringParser;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.property.customizer.ICSSPropertyCustomizer;
import com.helger.css.utils.CSSNumberHelper;

/**
 * CSS property that is either an enumeration or a decimal value without a unit, that lies inside an
 * inclusive range (e.g. font-weight)
 *
 * @author Philip Helger
 * @since 8.3.0
 */
@NotThreadSafe
public class CSSPropertyEnumOrDouble extends CSSPropertyEnum
{
  private final double m_dMin;
  private final double m_dMax;

  public CSSPropertyEnumOrDouble (@NonNull final ECSSProperty eProp, @NonNull @Nonempty final String... aEnumValues)
  {
    this (eProp, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, aEnumValues);
  }

  public CSSPropertyEnumOrDouble (@NonNull final ECSSProperty eProp,
                                  final double dMin,
                                  final double dMax,
                                  @NonNull @Nonempty final String... aEnumValues)
  {
    super (eProp, aEnumValues);
    m_dMin = dMin;
    m_dMax = dMax;
  }

  public CSSPropertyEnumOrDouble (@NonNull final ECSSProperty eProp,
                                  @Nullable final ECSSVendorPrefix eVendorPrefix,
                                  @Nullable final ICSSPropertyCustomizer aCustomizer,
                                  final double dMin,
                                  final double dMax,
                                  @NonNull @Nonempty final Iterable <String> aEnumValues)
  {
    super (eProp, eVendorPrefix, aCustomizer, aEnumValues);
    m_dMin = dMin;
    m_dMax = dMax;
  }

  /**
   * @return The minimum numeric value (inclusive).
   */
  public final double getMin ()
  {
    return m_dMin;
  }

  /**
   * @return The maximum numeric value (inclusive).
   */
  public final double getMax ()
  {
    return m_dMax;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public boolean isValidValue (@Nullable final String sValue)
  {
    if (super.isValidValue (sValue) || CSSNumberHelper.isNumericFunction (sValue))
      return true;

    final Double aValue = StringParser.parseDoubleObj (sValue);
    return aValue != null && aValue.doubleValue () >= m_dMin && aValue.doubleValue () <= m_dMax;
  }

  @Override
  @NonNull
  public CSSPropertyEnumOrDouble getClone (@NonNull final ECSSProperty eProp)
  {
    return new CSSPropertyEnumOrDouble (eProp,
                                        getVendorPrefix (),
                                        getCustomizer (),
                                        m_dMin,
                                        m_dMax,
                                        directGetEnumValues ());
  }

  @Override
  @NonNull
  public CSSPropertyEnumOrDouble getClone (@Nullable final ECSSVendorPrefix eVendorPrefix)
  {
    return new CSSPropertyEnumOrDouble (getProp (),
                                        eVendorPrefix,
                                        getCustomizer (),
                                        m_dMin,
                                        m_dMax,
                                        directGetEnumValues ());
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final CSSPropertyEnumOrDouble rhs = (CSSPropertyEnumOrDouble) o;
    return Double.compare (m_dMin, rhs.m_dMin) == 0 && Double.compare (m_dMax, rhs.m_dMax) == 0;
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_dMin).append (m_dMax).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("Min", m_dMin)
                            .append ("Max", m_dMax)
                            .getToString ();
  }
}
