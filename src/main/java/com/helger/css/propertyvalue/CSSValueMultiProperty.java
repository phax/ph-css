/**
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
package com.helger.css.propertyvalue;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotations.Nonempty;
import com.helger.commons.annotations.ReturnsMutableCopy;
import com.helger.commons.collections.ContainerHelper;
import com.helger.commons.hash.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.ICSSWriterSettings;
import com.helger.css.property.ECSSProperty;
import com.helger.css.property.ICSSProperty;

/**
 * Represents a CSS value that has several property names, but only one value.
 * This is e.g. if the property <code>border-radius</code> is used, as in this
 * case also <code>-moz-border-radius</code> should be emitted (with the same
 * value).<br>
 * For consistency issues,
 * 
 * @author Philip Helger
 */
@Immutable
public class CSSValueMultiProperty implements ICSSMultiValue
{
  private final ECSSProperty m_eProperty;
  private final List <CSSValue> m_aValues = new ArrayList <CSSValue> ();

  public CSSValueMultiProperty (@Nonnull final ECSSProperty eProperty,
                                @Nonnull final ICSSProperty [] aProperties,
                                @Nonnull @Nonempty final String sValue,
                                final boolean bIsImportant)
  {
    ValueEnforcer.notNull (eProperty, "Property");
    ValueEnforcer.notEmptyNoNullValue (aProperties, "Properties");
    ValueEnforcer.notNull (sValue, "Value");

    boolean bFound = false;
    for (final ICSSProperty aProperty : aProperties)
      if (aProperty.getProp () == eProperty)
      {
        bFound = true;
        break;
      }
    if (!bFound)
      throw new IllegalArgumentException ("The property " +
                                          eProperty +
                                          " is not contained in an ICSSProperty instance!");

    m_eProperty = eProperty;
    for (final ICSSProperty aProperty : aProperties)
      m_aValues.add (new CSSValue (aProperty, sValue, bIsImportant));
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <CSSValue> getContainedValues ()
  {
    return ContainerHelper.newList (m_aValues);
  }

  @Nonnull
  public ECSSProperty getProp ()
  {
    return m_eProperty;
  }

  @Nonnull
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    final StringBuilder ret = new StringBuilder ();
    for (final CSSValue aValue : m_aValues)
      ret.append (aValue.getAsCSSString (aSettings, nIndentLevel));
    return ret.toString ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final CSSValueMultiProperty rhs = (CSSValueMultiProperty) o;
    return m_aValues.equals (rhs.m_aValues);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aValues).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("values", m_aValues).toString ();
  }
}
