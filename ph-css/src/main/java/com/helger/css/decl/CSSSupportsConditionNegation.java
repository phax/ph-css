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
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a single negation supports condition. E.g. "not (color: blue)"
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSSupportsConditionNegation implements ICSSSupportsConditionMember, ICSSSourceLocationAware
{
  private final ICSSSupportsConditionMember m_aSupportsMember;
  private CSSSourceLocation m_aSourceLocation;

  public CSSSupportsConditionNegation (@Nonnull final ICSSSupportsConditionMember aSupportsMember)
  {
    m_aSupportsMember = ValueEnforcer.notNull (aSupportsMember, "SupportsMember");
  }

  /**
   * @return The contained supports condition member. Never <code>null</code>.
   */
  @Nonnull
  public ICSSSupportsConditionMember getSupportsMember ()
  {
    return m_aSupportsMember;
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    aSettings.checkVersionRequirements (this);
    return "not " + m_aSupportsMember.getAsCSSString (aSettings, nIndentLevel);
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
    final CSSSupportsConditionNegation rhs = (CSSSupportsConditionNegation) o;
    return m_aSupportsMember.equals (rhs.m_aSupportsMember);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aSupportsMember).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("supportsMember", m_aSupportsMember)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
