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

import com.helger.annotation.Nonnegative;
import com.helger.annotation.OverridingMethodsMustInvokeSuper;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.property.customizer.ICSSPropertyCustomizer;
import com.helger.css.utils.CSSValueHelper;

/**
 * CSS property that is a comma separated list, where each element must be valid according to another
 * property (e.g. animation-direction)
 *
 * @author Philip Helger
 * @since 8.2.4
 */
@NotThreadSafe
public class CSSPropertyList extends AbstractCSSProperty
{
  private final ICSSProperty m_aElementProperty;

  public CSSPropertyList (@NonNull final ECSSProperty eProp, @NonNull final ICSSProperty aElementProperty)
  {
    this (eProp, (ECSSVendorPrefix) null, (ICSSPropertyCustomizer) null, aElementProperty);
  }

  public CSSPropertyList (@NonNull final ECSSProperty eProp,
                          @Nullable final ECSSVendorPrefix eVendorPrefix,
                          @Nullable final ICSSPropertyCustomizer aCustomizer,
                          @NonNull final ICSSProperty aElementProperty)
  {
    super (eProp, eVendorPrefix, aCustomizer);
    ValueEnforcer.notNull (aElementProperty, "ElementProperty");
    m_aElementProperty = aElementProperty;
  }

  /**
   * @return The property each list element is checked against. Never <code>null</code>.
   */
  @NonNull
  public final ICSSProperty getElementProperty ()
  {
    return m_aElementProperty;
  }

  @Override
  @Nonnegative
  public int getMinimumArgumentCount ()
  {
    return m_aElementProperty.getMinimumArgumentCount ();
  }

  @Override
  @Nonnegative
  public int getMaximumArgumentCount ()
  {
    return m_aElementProperty.getMaximumArgumentCount ();
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public boolean isValidValue (@Nullable final String sValue)
  {
    if (super.isValidValue (sValue))
      return true;

    if (sValue == null)
      return false;

    for (final String sElement : CSSValueHelper.getSplitByComma (sValue))
      if (!m_aElementProperty.isValidValue (sElement))
        return false;
    return true;
  }

  @NonNull
  public CSSPropertyList getClone (@NonNull final ECSSProperty eProp)
  {
    return new CSSPropertyList (eProp, getVendorPrefix (), getCustomizer (), m_aElementProperty.getClone (eProp));
  }

  @NonNull
  public CSSPropertyList getClone (@Nullable final ECSSVendorPrefix eVendorPrefix)
  {
    return new CSSPropertyList (getProp (),
                                eVendorPrefix,
                                getCustomizer (),
                                m_aElementProperty.getClone (eVendorPrefix));
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final CSSPropertyList rhs = (CSSPropertyList) o;
    return m_aElementProperty.equals (rhs.m_aElementProperty);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aElementProperty).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("ElementProperty", m_aElementProperty).getToString ();
  }
}
