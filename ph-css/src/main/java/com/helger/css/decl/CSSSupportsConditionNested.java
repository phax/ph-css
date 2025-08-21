/*
 * Copyright (C) 2014-2025 Philip Helger (www.helger.com)
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

import com.helger.annotation.Nonempty;
import com.helger.annotation.Nonnegative;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.state.EChange;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.ICommonsList;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Represents a single negation supports condition
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSSupportsConditionNested implements ICSSSupportsConditionMember, ICSSSourceLocationAware
{
  private final ICommonsList <ICSSSupportsConditionMember> m_aMembers = new CommonsArrayList <> ();
  private CSSSourceLocation m_aSourceLocation;

  public CSSSupportsConditionNested ()
  {}

  public boolean hasMembers ()
  {
    return m_aMembers.isNotEmpty ();
  }

  @Nonnegative
  public int getMemberCount ()
  {
    return m_aMembers.size ();
  }

  @Nonnull
  public CSSSupportsConditionNested addMember (@Nonnull final ICSSSupportsConditionMember aMember)
  {
    ValueEnforcer.notNull (aMember, "SupportsConditionMember");

    m_aMembers.add (aMember);
    return this;
  }

  @Nonnull
  public CSSSupportsConditionNested addMember (@Nonnegative final int nIndex, @Nonnull final ICSSSupportsConditionMember aMember)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aMember, "SupportsConditionMember");

    if (nIndex >= getMemberCount ())
      m_aMembers.add (aMember);
    else
      m_aMembers.add (nIndex, aMember);
    return this;
  }

  @Nonnull
  public EChange removeMember (@Nonnull final ICSSSupportsConditionMember aMember)
  {
    return m_aMembers.removeObject (aMember);
  }

  @Nonnull
  public EChange removeMember (@Nonnegative final int nIndex)
  {
    return m_aMembers.removeAtIndex (nIndex);
  }

  /**
   * Remove all members.
   *
   * @return {@link EChange#CHANGED} if any member was removed,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   * @since 3.7.3
   */
  @Nonnull
  public EChange removeAllMembers ()
  {
    return m_aMembers.removeAll ();
  }

  @Nullable
  public ICSSSupportsConditionMember getMemberAtIndex (@Nonnegative final int nIndex)
  {
    return m_aMembers.getAtIndex (nIndex);
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <ICSSSupportsConditionMember> getAllMembers ()
  {
    return m_aMembers.getClone ();
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    aSettings.checkVersionRequirements (this);
    final StringBuilder aSB = new StringBuilder ("(");
    boolean bFirst = true;
    for (final ICSSSupportsConditionMember aMember : m_aMembers)
    {
      if (bFirst)
        bFirst = false;
      else
        aSB.append (' ');
      aSB.append (aMember.getAsCSSString (aSettings, nIndentLevel));
    }
    return aSB.append (')').toString ();
  }

  @Nonnull
  public ECSSVersion getMinimumCSSVersion ()
  {
    return ECSSVersion.CSS30;
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
    final CSSSupportsConditionNested rhs = (CSSSupportsConditionNested) o;
    return m_aMembers.equals (rhs.m_aMembers);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aMembers).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("members", m_aMembers).appendIfNotNull ("SourceLocation", m_aSourceLocation).getToString ();
  }
}
