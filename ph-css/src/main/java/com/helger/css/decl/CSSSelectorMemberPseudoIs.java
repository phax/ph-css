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
 * Represents a single, simple CSS selector as used for the ":is()" CSS pseudo element.<br>
 *
 * @author Philip Helger
 * @since 7.0.3
 */
@NotThreadSafe
public class CSSSelectorMemberPseudoIs implements ICSSSelectorMember, ICSSSourceLocationAware
{
  private final ICommonsList <CSSSelector> m_aNestedSelectors;
  private CSSSourceLocation m_aSourceLocation;

  public CSSSelectorMemberPseudoIs (@NonNull final CSSSelector aNestedSelector)
  {
    ValueEnforcer.notNull (aNestedSelector, "NestedSelector");
    m_aNestedSelectors = new CommonsArrayList <> (aNestedSelector);
  }

  public CSSSelectorMemberPseudoIs (@NonNull final CSSSelector... aNestedSelectors)
  {
    ValueEnforcer.notNull (aNestedSelectors, "NestedSelectors");
    m_aNestedSelectors = new CommonsArrayList <> (aNestedSelectors);
  }

  public CSSSelectorMemberPseudoIs (@NonNull final Iterable <CSSSelector> aNestedSelectors)
  {
    ValueEnforcer.notNull (aNestedSelectors, "NestedSelectors");
    m_aNestedSelectors = new CommonsArrayList <> (aNestedSelectors);
  }

  public boolean hasSelectors ()
  {
    return m_aNestedSelectors.isNotEmpty ();
  }

  @Nonnegative
  public int getSelectorCount ()
  {
    return m_aNestedSelectors.size ();
  }

  @NonNull
  public CSSSelectorMemberPseudoIs addSelector (@NonNull final ICSSSelectorMember aSingleSelectorMember)
  {
    ValueEnforcer.notNull (aSingleSelectorMember, "SingleSelectorMember");

    return addSelector (new CSSSelector ().addMember (aSingleSelectorMember));
  }

  @NonNull
  public CSSSelectorMemberPseudoIs addSelector (@NonNull final CSSSelector aSelector)
  {
    ValueEnforcer.notNull (aSelector, "Selector");

    m_aNestedSelectors.add (aSelector);
    return this;
  }

  @NonNull
  public CSSSelectorMemberPseudoIs addSelector (@Nonnegative final int nIndex,
                                                @NonNull final ICSSSelectorMember aSingleSelectorMember)
  {
    ValueEnforcer.notNull (aSingleSelectorMember, "SingleSelectorMember");

    return addSelector (nIndex, new CSSSelector ().addMember (aSingleSelectorMember));
  }

  @NonNull
  public CSSSelectorMemberPseudoIs addSelector (@Nonnegative final int nIndex, @NonNull final CSSSelector aSelector)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aSelector, "Selector");

    if (nIndex >= getSelectorCount ())
      m_aNestedSelectors.add (aSelector);
    else
      m_aNestedSelectors.add (nIndex, aSelector);
    return this;
  }

  @NonNull
  public EChange removeSelector (@NonNull final CSSSelector aSelector)
  {
    return m_aNestedSelectors.removeObject (aSelector);
  }

  @NonNull
  public EChange removeSelector (@Nonnegative final int nSelectorIndex)
  {
    return m_aNestedSelectors.removeAtIndex (nSelectorIndex);
  }

  /**
   * Remove all selectors.
   *
   * @return {@link EChange#CHANGED} if any selector was removed, {@link EChange#UNCHANGED}
   *         otherwise. Never <code>null</code>.
   */
  @NonNull
  public EChange removeAllSelectors ()
  {
    return m_aNestedSelectors.removeAll ();
  }

  @Nullable
  public CSSSelector getSelectorAtIndex (@Nonnegative final int nSelectorIndex)
  {
    return m_aNestedSelectors.getAtIndex (nSelectorIndex);
  }

  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <CSSSelector> getAllSelectors ()
  {
    return m_aNestedSelectors.getClone ();
  }

  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();
    final StringBuilder aSB = new StringBuilder (":is(");

    boolean bFirst = true;
    for (final CSSSelector aNestedSelector : m_aNestedSelectors)
    {
      if (bFirst)
        bFirst = false;
      else
        aSB.append (bOptimizedOutput ? "," : ", ");
      aSB.append (aNestedSelector.getAsCSSString (aSettings, 0));
    }
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
    final CSSSelectorMemberPseudoIs rhs = (CSSSelectorMemberPseudoIs) o;
    return m_aNestedSelectors.equals (rhs.m_aNestedSelectors);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aNestedSelectors).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("NestedSelectors", m_aNestedSelectors)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
