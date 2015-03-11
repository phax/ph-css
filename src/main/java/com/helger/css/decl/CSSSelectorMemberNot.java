/**
 * Copyright (C) 2014-2015 Philip Helger (www.helger.com)
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

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotations.Nonempty;
import com.helger.commons.annotations.ReturnsMutableCopy;
import com.helger.commons.collections.CollectionHelper;
import com.helger.commons.hash.HashCodeGenerator;
import com.helger.commons.state.EChange;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSVersionAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents an inverted CSS selector, used for the ":not()" CSS function.<br>
 * Note: this class was completely redesigned for version 3.7.4
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSSelectorMemberNot implements ICSSSelectorMember, ICSSVersionAware, ICSSSourceLocationAware
{
  private final List <CSSSelector> m_aNestedSelectors;
  private CSSSourceLocation m_aSourceLocation;

  public CSSSelectorMemberNot (@Nonnull final CSSSelector aNestedSelector)
  {
    ValueEnforcer.notNull (aNestedSelector, "NestedSelector");
    m_aNestedSelectors = CollectionHelper.newList (aNestedSelector);
  }

  public CSSSelectorMemberNot (@Nonnull final CSSSelector... aNestedSelectors)
  {
    ValueEnforcer.notNull (aNestedSelectors, "NestedSelectors");
    m_aNestedSelectors = CollectionHelper.newList (aNestedSelectors);
  }

  public CSSSelectorMemberNot (@Nonnull final List <CSSSelector> aNestedSelectors)
  {
    ValueEnforcer.notNull (aNestedSelectors, "NestedSelectors");
    m_aNestedSelectors = CollectionHelper.newList (aNestedSelectors);
  }

  public boolean hasSelectors ()
  {
    return !m_aNestedSelectors.isEmpty ();
  }

  @Nonnegative
  public int getSelectorCount ()
  {
    return m_aNestedSelectors.size ();
  }

  @Nonnull
  public CSSSelectorMemberNot addSelector (@Nonnull final ICSSSelectorMember aSingleSelectorMember)
  {
    ValueEnforcer.notNull (aSingleSelectorMember, "SingleSelectorMember");

    return addSelector (new CSSSelector ().addMember (aSingleSelectorMember));
  }

  @Nonnull
  public CSSSelectorMemberNot addSelector (@Nonnull final CSSSelector aSelector)
  {
    ValueEnforcer.notNull (aSelector, "Selector");

    m_aNestedSelectors.add (aSelector);
    return this;
  }

  @Nonnull
  public CSSSelectorMemberNot addSelector (@Nonnegative final int nIndex,
                                           @Nonnull final ICSSSelectorMember aSingleSelectorMember)
  {
    ValueEnforcer.notNull (aSingleSelectorMember, "SingleSelectorMember");

    return addSelector (nIndex, new CSSSelector ().addMember (aSingleSelectorMember));
  }

  @Nonnull
  public CSSSelectorMemberNot addSelector (@Nonnegative final int nIndex, @Nonnull final CSSSelector aSelector)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aSelector, "Selector");

    if (nIndex >= getSelectorCount ())
      m_aNestedSelectors.add (aSelector);
    else
      m_aNestedSelectors.add (nIndex, aSelector);
    return this;
  }

  @Nonnull
  public EChange removeSelector (@Nonnull final CSSSelector aSelector)
  {
    return EChange.valueOf (m_aNestedSelectors.remove (aSelector));
  }

  @Nonnull
  public EChange removeSelector (@Nonnegative final int nSelectorIndex)
  {
    if (nSelectorIndex < 0 || nSelectorIndex >= m_aNestedSelectors.size ())
      return EChange.UNCHANGED;
    m_aNestedSelectors.remove (nSelectorIndex);
    return EChange.CHANGED;
  }

  /**
   * Remove all selectors.
   *
   * @return {@link EChange#CHANGED} if any selector was removed,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   */
  @Nonnull
  public EChange removeAllSelectors ()
  {
    if (m_aNestedSelectors.isEmpty ())
      return EChange.UNCHANGED;
    m_aNestedSelectors.clear ();
    return EChange.CHANGED;
  }

  @Nullable
  public CSSSelector getSelectorAtIndex (@Nonnegative final int nSelectorIndex)
  {
    return CollectionHelper.getSafe (m_aNestedSelectors, nSelectorIndex);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <CSSSelector> getAllSelectors ()
  {
    return CollectionHelper.newList (m_aNestedSelectors);
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    aSettings.checkVersionRequirements (this);

    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();
    final StringBuilder aSB = new StringBuilder (":not(");
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
    final CSSSelectorMemberNot rhs = (CSSSelectorMemberNot) o;
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
    return new ToStringGenerator (null).append ("nestedSelectors", m_aNestedSelectors)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .toString ();
  }
}
