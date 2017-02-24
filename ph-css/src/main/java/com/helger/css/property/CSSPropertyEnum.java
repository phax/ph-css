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

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.ext.CommonsHashSet;
import com.helger.commons.collection.ext.ICommonsSet;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.property.customizer.ICSSPropertyCustomizer;

/**
 * CSS property with a predefined list of possible values (e.g. "cursor")
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSPropertyEnum extends AbstractCSSProperty
{
  private final ICommonsSet <String> m_aEnumValues;

  public CSSPropertyEnum (@Nonnull final ECSSProperty eProp, @Nonnull @Nonempty final String... aEnumValues)
  {
    this (eProp, (ICSSPropertyCustomizer) null, aEnumValues);
  }

  public CSSPropertyEnum (@Nonnull final ECSSProperty eProp,
                          @Nullable final ICSSPropertyCustomizer aCustomizer,
                          @Nonnull @Nonempty final String... aEnumValues)
  {
    this (eProp, (ECSSVendorPrefix) null, aCustomizer, aEnumValues);
  }

  public CSSPropertyEnum (@Nonnull final ECSSProperty eProp,
                          @Nullable final ECSSVendorPrefix eVendorPrefix,
                          @Nullable final ICSSPropertyCustomizer aCustomizer,
                          @Nonnull @Nonempty final String... aEnumValues)
  {
    super (eProp, eVendorPrefix, aCustomizer);
    ValueEnforcer.notEmptyNoNullValue (aEnumValues, "EnumValues");
    m_aEnumValues = new CommonsHashSet<> (aEnumValues.length);
    for (final String sPotentialValue : aEnumValues)
    {
      if (StringHelper.hasNoText (sPotentialValue))
        throw new IllegalArgumentException ("At least one enumeration value is empty");
      m_aEnumValues.add (sPotentialValue);
    }
  }

  public CSSPropertyEnum (@Nonnull final ECSSProperty eProp, @Nonnull @Nonempty final Iterable <String> aEnumValues)
  {
    this (eProp, (ICSSPropertyCustomizer) null, aEnumValues);
  }

  public CSSPropertyEnum (@Nonnull final ECSSProperty eProp,
                          @Nullable final ICSSPropertyCustomizer aCustomizer,
                          @Nonnull @Nonempty final Iterable <String> aEnumValues)
  {
    this (eProp, (ECSSVendorPrefix) null, aCustomizer, aEnumValues);
  }

  public CSSPropertyEnum (@Nonnull final ECSSProperty eProp,
                          @Nullable final ECSSVendorPrefix eVendorPrefix,
                          @Nullable final ICSSPropertyCustomizer aCustomizer,
                          @Nonnull @Nonempty final Iterable <String> aEnumValues)
  {
    super (eProp, eVendorPrefix, aCustomizer);
    ValueEnforcer.notEmptyNoNullValue (aEnumValues, "EnumValues");
    m_aEnumValues = new CommonsHashSet<> ();
    for (final String sPotentialValue : aEnumValues)
    {
      if (StringHelper.hasNoText (sPotentialValue))
        throw new IllegalArgumentException ("At least one enumeration value is empty");
      m_aEnumValues.add (sPotentialValue);
    }
  }

  /**
   * Private constructor for {@link #getClone(ECSSProperty)}
   *
   * @param eProp
   *        Property to use
   * @param eVendorPrefix
   *        The vendor prefix to be used. May be <code>null</code>.
   * @param aCustomizer
   *        Customizer to use.
   * @param aEnumValues
   *        Enum values to use. May neither be <code>null</code> nor empty.
   */
  private CSSPropertyEnum (@Nonnull final ECSSProperty eProp,
                           @Nullable final ECSSVendorPrefix eVendorPrefix,
                           @Nullable final ICSSPropertyCustomizer aCustomizer,
                           @Nonnull @Nonempty final Set <String> aEnumValues)
  {
    super (eProp, eVendorPrefix, aCustomizer);
    m_aEnumValues = new CommonsHashSet<> (aEnumValues);
  }

  /**
   * @return The Set with the enum values - only used for derived classes. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableObject ("Design")
  protected final ICommonsSet <String> directGetEnumValues ()
  {
    return m_aEnumValues;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public boolean isValidValue (@Nullable final String sValue)
  {
    return super.isValidValue (sValue) || m_aEnumValues.contains (sValue);
  }

  @Nonnull
  public CSSPropertyEnum getClone (@Nonnull final ECSSProperty eProp)
  {
    return new CSSPropertyEnum (eProp, getVendorPrefix (), getCustomizer (), m_aEnumValues);
  }

  @Nonnull
  public CSSPropertyEnum getClone (@Nullable final ECSSVendorPrefix eVendorPrefix)
  {
    return new CSSPropertyEnum (getProp (), eVendorPrefix, getCustomizer (), m_aEnumValues);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final CSSPropertyEnum rhs = (CSSPropertyEnum) o;
    return m_aEnumValues.equals (rhs.m_aEnumValues);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aEnumValues).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("enumValues", m_aEnumValues).getToString ();
  }
}
