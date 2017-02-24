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
package com.helger.css.decl;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a simple expression member
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSExpressionMemberTermSimple implements ICSSExpressionMember, ICSSSourceLocationAware
{
  private String m_sValue;
  private String m_sOptimizedValue;
  private CSSSourceLocation m_aSourceLocation;

  public CSSExpressionMemberTermSimple (final int nValue)
  {
    this (Integer.toString (nValue));
  }

  public CSSExpressionMemberTermSimple (final long nValue)
  {
    this (Long.toString (nValue));
  }

  public CSSExpressionMemberTermSimple (final float fValue)
  {
    this (Float.toString (fValue));
  }

  public CSSExpressionMemberTermSimple (final double dValue)
  {
    this (Double.toString (dValue));
  }

  public CSSExpressionMemberTermSimple (@Nonnull @Nonempty final String sValue)
  {
    setValue (sValue);
  }

  @Nonnull
  public CSSExpressionMemberTermSimple setValue (@Nonnull @Nonempty final String sValue)
  {
    ValueEnforcer.notEmpty (sValue, "Value");
    m_sValue = sValue;
    m_sOptimizedValue = CSSExpressionTermOptimizer.getOptimizedValue (sValue);
    return this;
  }

  /**
   * @return The original value. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getValue ()
  {
    return m_sValue;
  }

  /**
   * @return An optimized version of the value. In most cases it is identical to
   *         the original version.
   * @see CSSExpressionTermOptimizer#getOptimizedValue(String)
   */
  @Nonnull
  @Nonempty
  public String getOptimizedValue ()
  {
    return m_sOptimizedValue;
  }

  @Nonnull
  public CSSExpressionMemberTermSimple getClone ()
  {
    return new CSSExpressionMemberTermSimple (m_sValue);
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    return aSettings.isOptimizedOutput () ? m_sOptimizedValue : m_sValue;
  }

  public void setSourceLocation (@Nullable final CSSSourceLocation aSourceLocation)
  {
    m_aSourceLocation = aSourceLocation;
  }

  @Nullable
  public CSSSourceLocation getSourceLocation ()
  {
    return m_aSourceLocation;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final CSSExpressionMemberTermSimple rhs = (CSSExpressionMemberTermSimple) o;
    // Compare the optimized value so that "0em" equals "0px"
    return m_sOptimizedValue.equals (rhs.m_sOptimizedValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sOptimizedValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("value", m_sValue)
                                       .append ("optimizedValue", m_sOptimizedValue)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
