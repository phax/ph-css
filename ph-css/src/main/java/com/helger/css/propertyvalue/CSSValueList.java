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
package com.helger.css.propertyvalue;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.Nonnegative;
import com.helger.annotation.concurrent.Immutable;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.string.StringImplode;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.ICommonsList;
import com.helger.css.ICSSWriterSettings;
import com.helger.css.property.ECSSProperty;
import com.helger.css.property.ICSSProperty;

/**
 * Represents a CSS value that has both different property names and multiple different values. This
 * is e.g. if the property <code>display</code> is used with the value <code>inline-block</code>
 * than the result coding should first emit <code>display:-moz-inline-block;</code> and them
 * <code>display:inline-block;</code> for FireFox 2.x specific support.
 *
 * @author Philip Helger
 */
@Immutable
public class CSSValueList implements ICSSMultiValue
{
  private final ECSSProperty m_eProperty;
  private final ICommonsList <CSSValue> m_aValues;

  public CSSValueList (@NonNull final ECSSProperty eProperty,
                       @NonNull final ICSSProperty [] aProperties,
                       @NonNull final String [] aValues,
                       final boolean bIsImportant)
  {
    ValueEnforcer.notNull (eProperty, "Property");
    ValueEnforcer.notEmptyNoNullValue (aProperties, "Properties");
    ValueEnforcer.notEmptyNoNullValue (aValues, "Values");
    if (aProperties.length != aValues.length)
      throw new IllegalArgumentException ("Different number of properties and values passed");

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
    m_aValues = new CommonsArrayList <> (aProperties.length);
    for (int i = 0; i < aProperties.length; ++i)
      m_aValues.add (new CSSValue (aProperties[i], aValues[i], bIsImportant));
  }

  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <CSSValue> getAllContainedValues ()
  {
    return m_aValues.getClone ();
  }

  @NonNull
  public ECSSProperty getProp ()
  {
    return m_eProperty;
  }

  @NonNull
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    return StringImplode.getImplodedMapped (m_aValues, x -> x.getAsCSSString (aSettings, nIndentLevel));
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final CSSValueList rhs = (CSSValueList) o;
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
    return new ToStringGenerator (this).append ("values", m_aValues).getToString ();
  }
}
