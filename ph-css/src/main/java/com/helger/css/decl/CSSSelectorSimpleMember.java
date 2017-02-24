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
 * Represents a single CSS selector like an element name, a hash value (ID), a
 * class or a pseudo class.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSSelectorSimpleMember implements ICSSSelectorMember, ICSSSourceLocationAware
{
  private final String m_sValue;
  private CSSSourceLocation m_aSourceLocation;

  public CSSSelectorSimpleMember (@Nonnull @Nonempty final String sValue)
  {
    ValueEnforcer.notEmpty (sValue, "Value");
    m_sValue = sValue;
  }

  @Nonnull
  @Nonempty
  public String getValue ()
  {
    return m_sValue;
  }

  /**
   * @return <code>true</code> if it is no hash, no class and no pseudo selector
   */
  public boolean isElementName ()
  {
    return !isHash () && !isClass () && !isPseudo ();
  }

  /**
   * @return <code>true</code> if it is a hash selector
   */
  public boolean isHash ()
  {
    return m_sValue.charAt (0) == '#';
  }

  /**
   * @return <code>true</code> if it is a class selector
   */
  public boolean isClass ()
  {
    return m_sValue.charAt (0) == '.';
  }

  /**
   * @return <code>true</code> if it is a pseudo selector
   */
  public boolean isPseudo ()
  {
    return m_sValue.charAt (0) == ':';
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    return m_sValue;
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
    final CSSSelectorSimpleMember rhs = (CSSSelectorSimpleMember) o;
    return m_sValue.equals (rhs.m_sValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("value", m_sValue)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
