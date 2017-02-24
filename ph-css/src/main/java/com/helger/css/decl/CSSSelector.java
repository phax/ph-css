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
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.state.EChange;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriteable;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a single selector as the aggregation of selector members. An
 * example for a selector is e.g. <code>div</code> or <code>.class</code> or
 * <code>h1.class</code> or <code>#id</code> or
 * <code>table tbody td .class</code> etc.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSSelector implements ICSSWriteable, ICSSSourceLocationAware
{
  private final ICommonsList <ICSSSelectorMember> m_aMembers = new CommonsArrayList<> ();
  private CSSSourceLocation m_aSourceLocation;

  public CSSSelector ()
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
  public CSSSelector addMember (@Nonnull final ICSSSelectorMember aMember)
  {
    ValueEnforcer.notNull (aMember, "Member");

    m_aMembers.add (aMember);
    return this;
  }

  @Nonnull
  public CSSSelector addMember (@Nonnegative final int nIndex, @Nonnull final ICSSSelectorMember aMember)
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
  public EChange removeMember (@Nonnull final ICSSSelectorMember aMember)
  {
    return EChange.valueOf (m_aMembers.remove (aMember));
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

  @Nullable
  public ICSSSelectorMember getMemberAtIndex (@Nonnegative final int nMemberIndex)
  {
    return m_aMembers.getAtIndex (nMemberIndex);
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <ICSSSelectorMember> getAllMembers ()
  {
    return m_aMembers.getClone ();
  }

  @Nonnull
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    return StringHelper.getImplodedMapped (m_aMembers, x -> x.getAsCSSString (aSettings, nIndentLevel));
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
    final CSSSelector rhs = (CSSSelector) o;
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
                                       .getToString ();
  }
}
