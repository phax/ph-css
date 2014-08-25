/**
 * Copyright (C) 2006-2014 phloc systems (www.phloc.com)
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
package com.helger.css.decl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotations.Nonempty;
import com.helger.commons.annotations.ReturnsMutableCopy;
import com.helger.commons.collections.ContainerHelper;
import com.helger.commons.hash.HashCodeGenerator;
import com.helger.commons.state.EChange;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a single negation supports condition
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSSupportsConditionNested implements ICSSSupportsConditionMember, ICSSSourceLocationAware
{
  private final List <ICSSSupportsConditionMember> m_aMembers = new ArrayList <ICSSSupportsConditionMember> ();
  private CSSSourceLocation m_aSourceLocation;

  public CSSSupportsConditionNested ()
  {}

  public boolean hasMembers ()
  {
    return !m_aMembers.isEmpty ();
  }

  /**
   * @deprecated Use {@link #getMemberCount()} instead
   */
  @Deprecated
  @Nonnegative
  public int getSupportsMemberCount ()
  {
    return getMemberCount ();
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
  public CSSSupportsConditionNested addMember (@Nonnegative final int nIndex,
                                               @Nonnull final ICSSSupportsConditionMember aMember)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aMember, "SupportsConditionMember");

    if (nIndex >= getMemberCount ())
      m_aMembers.add (aMember);
    else
      m_aMembers.add (nIndex, aMember);
    return this;
  }

  /**
   * @deprecated Use {@link #removeMember(ICSSSupportsConditionMember)} instead
   */
  @Deprecated
  @Nonnull
  public EChange removeSupportsMember (@Nonnull final ICSSSupportsConditionMember aMember)
  {
    return removeMember (aMember);
  }

  @Nonnull
  public EChange removeMember (@Nonnull final ICSSSupportsConditionMember aMember)
  {
    return EChange.valueOf (m_aMembers.remove (aMember));
  }

  /**
   * @deprecated Use {@link #removeMember(int)} instead
   */
  @Deprecated
  @Nonnull
  public EChange removeSupportsMember (@Nonnegative final int nIndex)
  {
    return removeMember (nIndex);
  }

  @Nonnull
  public EChange removeMember (@Nonnegative final int nIndex)
  {
    if (nIndex < 0 || nIndex >= m_aMembers.size ())
      return EChange.UNCHANGED;
    m_aMembers.remove (nIndex);
    return EChange.CHANGED;
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
    if (m_aMembers.isEmpty ())
      return EChange.UNCHANGED;
    m_aMembers.clear ();
    return EChange.CHANGED;
  }

  /**
   * @deprecated Use {@link #getMemberAtIndex(int)} instead
   */
  @Deprecated
  @Nullable
  public ICSSSupportsConditionMember getSupportsMemberAtIndex (@Nonnegative final int nIndex)
  {
    return getMemberAtIndex (nIndex);
  }

  @Nullable
  public ICSSSupportsConditionMember getMemberAtIndex (@Nonnegative final int nIndex)
  {
    if (nIndex < 0 || nIndex >= m_aMembers.size ())
      return null;
    return m_aMembers.get (nIndex);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <ICSSSupportsConditionMember> getAllMembers ()
  {
    return ContainerHelper.newList (m_aMembers);
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
    return new ToStringGenerator (this).append ("members", m_aMembers)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .toString ();
  }
}
