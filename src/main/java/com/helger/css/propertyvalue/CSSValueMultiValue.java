/**
 * Copyright (C) 2014-2015 Philip Helger (www.helger.com)
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
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.ICSSWriterSettings;
import com.helger.css.property.ECSSProperty;
import com.helger.css.property.ICSSProperty;

/**
 * Represents a CSS value that has one property name, but multiple different
 * values. This is e.g. if the property <code>display</code> is used with the
 * value <code>inline-block</code> than the result coding should first emit
 * <code>display:-moz-inline-block;</code> and then
 * <code>display:inline-block;</code> for FireFox 2.x specific support.
 *
 * @author Philip Helger
 */
@Immutable
public class CSSValueMultiValue implements ICSSMultiValue
{
  private final List <CSSValue> m_aValues = new ArrayList <> ();

  public CSSValueMultiValue (@Nonnull final ICSSProperty aProperty,
                             @Nonnull @Nonempty final String [] aValues,
                             final boolean bIsImportant)
  {
    ValueEnforcer.notNull (aProperty, "Property");
    ValueEnforcer.notEmptyNoNullValue (aValues, "Values");

    for (final String sValue : aValues)
      m_aValues.add (new CSSValue (aProperty, sValue, bIsImportant));
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <CSSValue> getAllContainedValues ()
  {
    return CollectionHelper.newList (m_aValues);
  }

  @Nonnull
  public ECSSProperty getProp ()
  {
    return CollectionHelper.getFirstElement (m_aValues).getProp ();
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
    final CSSValueMultiValue rhs = (CSSValueMultiValue) o;
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
