/*
 * Copyright (C) 2014-2022 Philip Helger (www.helger.com)
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
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSVersionAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a single, simple CSS selector as used for the ":host()" CSS pseudo class function.<br>
 * Note: this class was completely redesigned for version 3.7.4
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSSelectorMemberHost implements ICSSSelectorMember, ICSSVersionAware, ICSSSourceLocationAware
{
  private final ICSSSelectorMember m_aSimpleSelector;
  private CSSSourceLocation m_aSourceLocation;

  public CSSSelectorMemberHost (@Nonnull final ICSSSelectorMember aSimpleSelector)
  {
    ValueEnforcer.notNull (aSimpleSelector, "SimpleSelector");
    m_aSimpleSelector = aSimpleSelector;
  }

  @Nonnull
  public ICSSSelectorMember getSimpleSelector ()
  {
    return m_aSimpleSelector;
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    aSettings.checkVersionRequirements (this);

    final StringBuilder aSB = new StringBuilder (":host(");
    aSB.append (m_aSimpleSelector.getAsCSSString (aSettings, 0));
    return aSB.append (')').toString ();
  }

  @Nonnull
  public ECSSVersion getMinimumCSSVersion ()
  {
    return ECSSVersion.CSS30;
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
    final CSSSelectorMemberHost rhs = (CSSSelectorMemberHost) o;
    return m_aSimpleSelector.equals (rhs.m_aSimpleSelector);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aSimpleSelector).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("simpleSelector", m_aSimpleSelector)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
