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
package com.helger.css.decl;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.annotation.Nonnegative;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a single, simple CSS selector as used for the "::slotted()" CSS pseudo element.<br>
 *
 * @author Mike Wiedenauer
 * @author Philip Helger
 * @since 6.4.1
 */
@NotThreadSafe
public class CSSSelectorMemberSlotted implements ICSSSelectorMember, ICSSSourceLocationAware
{
  private final CSSSelector m_aSelector;
  private CSSSourceLocation m_aSourceLocation;

  public CSSSelectorMemberSlotted (@NonNull final CSSSelector aSelector)
  {
    ValueEnforcer.notNull (aSelector, "Selector");
    m_aSelector = aSelector;
  }

  @NonNull
  public final CSSSelector getSelector ()
  {
    return m_aSelector;
  }

  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    final StringBuilder aSB = new StringBuilder ("::slotted(");
    aSB.append (m_aSelector.getAsCSSString (aSettings, 0));
    return aSB.append (')').toString ();
  }

  @Nullable
  public final CSSSourceLocation getSourceLocation ()
  {
    return m_aSourceLocation;
  }

  public final void setSourceLocation (@Nullable final CSSSourceLocation aSourceLocation)
  {
    m_aSourceLocation = aSourceLocation;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final CSSSelectorMemberSlotted rhs = (CSSSelectorMemberSlotted) o;
    return m_aSelector.equals (rhs.m_aSelector);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aSelector).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("Selector", m_aSelector)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
