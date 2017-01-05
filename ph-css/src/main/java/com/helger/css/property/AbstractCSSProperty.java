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
import com.helger.commons.annotation.OverrideOnDemand;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.ECSSVersion;
import com.helger.css.property.customizer.ICSSPropertyCustomizer;
import com.helger.css.propertyvalue.CCSSValue;
import com.helger.css.propertyvalue.CSSValue;
import com.helger.css.propertyvalue.ICSSValue;

/**
 * Abstract base class for implementing {@link ICSSProperty}
 *
 * @author Philip Helger
 */
@NotThreadSafe
public abstract class AbstractCSSProperty implements ICSSProperty
{
  private final ECSSProperty m_eProp;
  private final ECSSVendorPrefix m_eVendorPrefix;
  private final ICSSPropertyCustomizer m_aCustomizer;

  /**
   * Constructor
   *
   * @param eProp
   *        The base property to use. May not be <code>null</code>.
   * @param eVendorPrefix
   *        The vendor prefix to be used. May be <code>null</code>.
   * @param aCustomizer
   *        The customizer to be used. May be <code>null</code>.
   */
  protected AbstractCSSProperty (@Nonnull final ECSSProperty eProp,
                                 @Nullable final ECSSVendorPrefix eVendorPrefix,
                                 @Nullable final ICSSPropertyCustomizer aCustomizer)
  {
    m_eProp = ValueEnforcer.notNull (eProp, "Property");
    m_eVendorPrefix = eVendorPrefix;
    m_aCustomizer = aCustomizer;
    if (eProp.isVendorSpecific () && eVendorPrefix != null)
      throw new IllegalStateException ("You cannot use the vendor prefix " +
                                       eVendorPrefix +
                                       " on the already vendor specific property " +
                                       eProp);
  }

  @Nonnull
  public final ECSSVersion getMinimumCSSVersion ()
  {
    return m_eProp.getMinimumCSSVersion ();
  }

  @Nonnull
  public final ECSSProperty getProp ()
  {
    return m_eProp;
  }

  @Nullable
  public final ECSSVendorPrefix getVendorPrefix ()
  {
    return m_eVendorPrefix;
  }

  @Nonnull
  @Nonempty
  public final String getPropertyName ()
  {
    if (m_eVendorPrefix != null)
    {
      // Use vendor prefix
      return m_eVendorPrefix.getPrefix () + m_eProp.getName ();
    }

    return m_eProp.getName ();
  }

  @Nullable
  public final ICSSPropertyCustomizer getCustomizer ()
  {
    return m_aCustomizer;
  }

  @Nonnegative
  @OverrideOnDemand
  public int getMinimumArgumentCount ()
  {
    return 1;
  }

  @Nonnegative
  @OverrideOnDemand
  public int getMaximumArgumentCount ()
  {
    return 1;
  }

  public static boolean isValidPropertyValue (@Nullable final String sValue)
  {
    // "inherit" and "initial" is valid for all values in CSS 3.0
    return CCSSValue.INHERIT.equals (sValue) || CCSSValue.INITIAL.equals (sValue);
  }

  @OverridingMethodsMustInvokeSuper
  public boolean isValidValue (@Nullable final String sValue)
  {
    return isValidPropertyValue (sValue);
  }

  @Nonnull
  public ICSSValue newValue (@Nonnull @Nonempty final String sValue, final boolean bIsImportant)
  {
    ValueEnforcer.notEmpty (sValue, "Value");

    // Special handling for browser specific value creation
    if (m_aCustomizer != null)
    {
      final ICSSValue aCustomizedValue = m_aCustomizer.createSpecialValue (this, sValue, bIsImportant);
      if (aCustomizedValue != null)
        return aCustomizedValue;
    }

    return new CSSValue (this, sValue, bIsImportant);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final AbstractCSSProperty rhs = (AbstractCSSProperty) o;
    return m_eProp.equals (rhs.m_eProp);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eProp).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("prop", m_eProp).toString ();
  }
}
