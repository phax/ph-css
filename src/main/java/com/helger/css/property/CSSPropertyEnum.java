/**
 * Copyright (C) 2006-2014 phloc systems (www.phloc.com)
 * Copyright (C) 2014 Philip Helger (www.helger.com)
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

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotations.Nonempty;
import com.helger.commons.annotations.ReturnsMutableObject;
import com.helger.commons.collections.ContainerHelper;
import com.helger.commons.hash.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.property.customizer.ICSSPropertyCustomizer;

/**
 * CSS property with a predefined list of possible values (e.g. "cursor")
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSPropertyEnum extends AbstractCSSProperty
{
  private final Set <String> m_aEnumValues;

  public CSSPropertyEnum (@Nonnull final ECSSProperty eProp, @Nonnull @Nonempty final String... aEnumValues)
  {
    this (eProp, null, aEnumValues);
  }

  public CSSPropertyEnum (@Nonnull final ECSSProperty eProp,
                          @Nullable final ICSSPropertyCustomizer aCustomizer,
                          @Nonnull @Nonempty final String... aEnumValues)
  {
    super (eProp, aCustomizer);
    ValueEnforcer.notEmptyNoNullValue (aEnumValues, "EnumValues");
    m_aEnumValues = new HashSet <String> (aEnumValues.length);
    for (final String sPotentialValue : aEnumValues)
    {
      if (StringHelper.hasNoText (sPotentialValue))
        throw new IllegalArgumentException ("At least one enumeration value is empty");
      m_aEnumValues.add (sPotentialValue);
    }
  }

  public CSSPropertyEnum (@Nonnull final ECSSProperty eProp, @Nonnull @Nonempty final Iterable <String> aEnumValues)
  {
    this (eProp, null, aEnumValues);
  }

  public CSSPropertyEnum (@Nonnull final ECSSProperty eProp,
                          @Nullable final ICSSPropertyCustomizer aCustomizer,
                          @Nonnull @Nonempty final Iterable <String> aEnumValues)
  {
    super (eProp, aCustomizer);
    ValueEnforcer.notEmptyNoNullValue (aEnumValues, "EnumValues");
    m_aEnumValues = new HashSet <String> ();
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
   * @param aCustomizer
   *        Customizer to use.
   * @param aEnumValues
   *        Enum values to use. May neither be <code>null</code> nor empty.
   */
  private CSSPropertyEnum (@Nonnull final ECSSProperty eProp,
                           @Nullable final ICSSPropertyCustomizer aCustomizer,
                           @Nonnull @Nonempty final Set <String> aEnumValues)
  {
    super (eProp, aCustomizer);
    m_aEnumValues = ContainerHelper.newSet (aEnumValues);
  }

  /**
   * @return The Set with the enum values - only used for derived classes. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableObject (reason = "Design")
  protected final Set <String> directGetEnumValues ()
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
    return new CSSPropertyEnum (eProp, getCustomizer (), m_aEnumValues);
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
    return ToStringGenerator.getDerived (super.toString ()).append ("enumValues", m_aEnumValues).toString ();
  }
}
