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
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.state.EChange;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.ICommonsList;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a CSS calc() element
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSExpressionMemberMath implements ICSSExpressionMember, ICSSExpressionMathMember, ICSSSourceLocationAware
{
  private final ICommonsList <ICSSExpressionMathMember> m_aMembers = new CommonsArrayList <> ();
  private CSSSourceLocation m_aSourceLocation;

  public CSSExpressionMemberMath ()
  {}

  public CSSExpressionMemberMath (@NonNull final Iterable <? extends ICSSExpressionMathMember> aMembers)
  {
    m_aMembers.addAll (aMembers);
  }

  @NonNull
  public CSSExpressionMemberMath addMember (@NonNull final ICSSExpressionMathMember aMember)
  {
    ValueEnforcer.notNull (aMember, "Member");
    m_aMembers.add (aMember);
    return this;
  }

  @NonNull
  public CSSExpressionMemberMath addMember (@Nonnegative final int nIndex,
                                            @NonNull final ICSSExpressionMathMember aMember)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aMember, "Member");

    if (nIndex >= getMemberCount ())
      m_aMembers.add (aMember);
    else
      m_aMembers.add (nIndex, aMember);
    return this;
  }

  @NonNull
  public EChange removeMember (@NonNull final ICSSExpressionMathMember aMember)
  {
    return m_aMembers.removeObject (aMember);
  }

  @NonNull
  public EChange removeMember (@Nonnegative final int nMemberIndex)
  {
    return m_aMembers.removeAtIndex (nMemberIndex);
  }

  /**
   * Remove all members.
   *
   * @return {@link EChange#CHANGED} if any member was removed, {@link EChange#UNCHANGED} otherwise.
   *         Never <code>null</code>.
   * @since 3.7.3
   */
  @NonNull
  public EChange removeAllMembers ()
  {
    return m_aMembers.removeAll ();
  }

  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <ICSSExpressionMathMember> getAllMembers ()
  {
    return m_aMembers.getClone ();
  }

  @Nonnegative
  public int getMemberCount ()
  {
    return m_aMembers.size ();
  }

  @NonNull
  public CSSExpressionMemberMath getClone ()
  {
    return new CSSExpressionMemberMath (m_aMembers);
  }

  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    final StringBuilder aSB = new StringBuilder ("calc(");
    for (final ICSSExpressionMathMember aMember : m_aMembers)
      aSB.append (aMember.getAsCSSString (aSettings, nIndentLevel));
    return aSB.append (")").toString ();
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
    final CSSExpressionMemberMath rhs = (CSSExpressionMemberMath) o;
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
    return new ToStringGenerator (null).append ("members", m_aMembers)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
