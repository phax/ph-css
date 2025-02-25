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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.state.EChange;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSVersionAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a CSS line-names element as used in css-grid.
 *
 * @author Philip Helger
 * @since 5.0.4
 */
@NotThreadSafe
public class CSSExpressionMemberLineNames implements ICSSExpressionMember, ICSSVersionAware, ICSSSourceLocationAware
{
  private final ICommonsList <String> m_aMembers = new CommonsArrayList <> ();
  private CSSSourceLocation m_aSourceLocation;

  public CSSExpressionMemberLineNames ()
  {}

  public CSSExpressionMemberLineNames (@Nonnull final Iterable <? extends String> aMembers)
  {
    m_aMembers.addAll (aMembers);
  }

  @Nonnull
  public CSSExpressionMemberLineNames addMember (@Nonnull @Nonempty final String sMember)
  {
    ValueEnforcer.notEmpty (sMember, "Member");
    m_aMembers.add (sMember);
    return this;
  }

  @Nonnull
  public CSSExpressionMemberLineNames addMember (@Nonnegative final int nIndex, @Nonnull @Nonempty final String aMember)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aMember, "Member");

    if (nIndex >= getMemberCount ())
      m_aMembers.add (aMember);
    else
      m_aMembers.add (nIndex, aMember);
    return this;
  }

  @Nonnull
  public EChange removeMember (@Nonnull final String aMember)
  {
    return m_aMembers.removeObject (aMember);
  }

  @Nonnull
  public EChange removeMember (@Nonnegative final int nMemberIndex)
  {
    return m_aMembers.removeAtIndex (nMemberIndex);
  }

  /**
   * Remove all members.
   *
   * @return {@link EChange#CHANGED} if any member was removed,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   */
  @Nonnull
  public EChange removeAllMembers ()
  {
    return m_aMembers.removeAll ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <String> getAllMembers ()
  {
    return m_aMembers.getClone ();
  }

  @Nonnegative
  public int getMemberCount ()
  {
    return m_aMembers.size ();
  }

  @Nonnull
  public CSSExpressionMemberLineNames getClone ()
  {
    return new CSSExpressionMemberLineNames (m_aMembers);
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    aSettings.checkVersionRequirements (this);
    final StringBuilder aSB = new StringBuilder ().append ('[');
    boolean bFirst = true;
    for (final String sMember : m_aMembers)
    {
      if (bFirst)
        bFirst = false;
      else
        aSB.append (' ');
      aSB.append (sMember);
    }
    return aSB.append (']').toString ();
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
    final CSSExpressionMemberLineNames rhs = (CSSExpressionMemberLineNames) o;
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
    return new ToStringGenerator (null).append ("members", m_aMembers).appendIfNotNull ("SourceLocation", m_aSourceLocation).getToString ();
  }
}
