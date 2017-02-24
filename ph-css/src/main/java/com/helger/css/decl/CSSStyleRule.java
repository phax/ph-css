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
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a single CSS style rule. A style rule consists of a number of
 * selectors (determine the element to which the style rule applies) and a
 * number of declarations (the rules to be applied to the selected elements).
 * <br>
 * Example:<br>
 * <code>div { color: red; }</code>
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSStyleRule implements ICSSTopLevelRule, IHasCSSDeclarations <CSSStyleRule>, ICSSSourceLocationAware
{
  private final ICommonsList <CSSSelector> m_aSelectors = new CommonsArrayList<> ();
  private final CSSDeclarationContainer m_aDeclarations = new CSSDeclarationContainer ();
  private CSSSourceLocation m_aSourceLocation;

  public CSSStyleRule ()
  {}

  public boolean hasSelectors ()
  {
    return m_aSelectors.isNotEmpty ();
  }

  @Nonnegative
  public int getSelectorCount ()
  {
    return m_aSelectors.size ();
  }

  @Nonnull
  public CSSStyleRule addSelector (@Nonnull final ICSSSelectorMember aSingleSelectorMember)
  {
    ValueEnforcer.notNull (aSingleSelectorMember, "SingleSelectorMember");

    return addSelector (new CSSSelector ().addMember (aSingleSelectorMember));
  }

  @Nonnull
  public CSSStyleRule addSelector (@Nonnull final CSSSelector aSelector)
  {
    ValueEnforcer.notNull (aSelector, "Selector");

    m_aSelectors.add (aSelector);
    return this;
  }

  @Nonnull
  public CSSStyleRule addSelector (@Nonnegative final int nIndex,
                                   @Nonnull final ICSSSelectorMember aSingleSelectorMember)
  {
    ValueEnforcer.notNull (aSingleSelectorMember, "SingleSelectorMember");

    return addSelector (nIndex, new CSSSelector ().addMember (aSingleSelectorMember));
  }

  @Nonnull
  public CSSStyleRule addSelector (@Nonnegative final int nIndex, @Nonnull final CSSSelector aSelector)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aSelector, "Selector");

    if (nIndex >= getSelectorCount ())
      m_aSelectors.add (aSelector);
    else
      m_aSelectors.add (nIndex, aSelector);
    return this;
  }

  @Nonnull
  public EChange removeSelector (@Nonnull final CSSSelector aSelector)
  {
    return EChange.valueOf (m_aSelectors.remove (aSelector));
  }

  @Nonnull
  public EChange removeSelector (@Nonnegative final int nSelectorIndex)
  {
    return m_aSelectors.removeAtIndex (nSelectorIndex);
  }

  /**
   * Remove all selectors.
   *
   * @return {@link EChange#CHANGED} if any selector was removed,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   * @since 3.7.3
   */
  @Nonnull
  public EChange removeAllSelectors ()
  {
    return m_aSelectors.removeAll ();
  }

  @Nullable
  public CSSSelector getSelectorAtIndex (@Nonnegative final int nSelectorIndex)
  {
    return m_aSelectors.getAtIndex (nSelectorIndex);
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <CSSSelector> getAllSelectors ()
  {
    return m_aSelectors.getClone ();
  }

  @Nonnull
  public CSSStyleRule addDeclaration (@Nonnull final CSSDeclaration aDeclaration)
  {
    m_aDeclarations.addDeclaration (aDeclaration);
    return this;
  }

  @Nonnull
  public CSSStyleRule addDeclaration (@Nonnegative final int nIndex, @Nonnull final CSSDeclaration aNewDeclaration)
  {
    m_aDeclarations.addDeclaration (nIndex, aNewDeclaration);
    return this;
  }

  @Nonnull
  public EChange removeDeclaration (@Nonnull final CSSDeclaration aDeclaration)
  {
    return m_aDeclarations.removeDeclaration (aDeclaration);
  }

  @Nonnull
  public EChange removeDeclaration (@Nonnegative final int nDeclarationIndex)
  {
    return m_aDeclarations.removeDeclaration (nDeclarationIndex);
  }

  @Nonnull
  public EChange removeAllDeclarations ()
  {
    return m_aDeclarations.removeAllDeclarations ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <CSSDeclaration> getAllDeclarations ()
  {
    return m_aDeclarations.getAllDeclarations ();
  }

  @Nullable
  public CSSDeclaration getDeclarationAtIndex (@Nonnegative final int nIndex)
  {
    return m_aDeclarations.getDeclarationAtIndex (nIndex);
  }

  @Nonnull
  public CSSStyleRule setDeclarationAtIndex (@Nonnegative final int nIndex,
                                             @Nonnull final CSSDeclaration aNewDeclaration)
  {
    m_aDeclarations.setDeclarationAtIndex (nIndex, aNewDeclaration);
    return this;
  }

  public boolean hasDeclarations ()
  {
    return m_aDeclarations.hasDeclarations ();
  }

  @Nonnegative
  public int getDeclarationCount ()
  {
    return m_aDeclarations.getDeclarationCount ();
  }

  @Nullable
  public CSSDeclaration getDeclarationOfPropertyName (@Nullable final String sPropertyName)
  {
    return m_aDeclarations.getDeclarationOfPropertyName (sPropertyName);
  }

  @Nullable
  public CSSDeclaration getDeclarationOfPropertyNameCaseInsensitive (@Nullable final String sPropertyName)
  {
    return m_aDeclarations.getDeclarationOfPropertyNameCaseInsensitive (sPropertyName);
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <CSSDeclaration> getAllDeclarationsOfPropertyName (@Nullable final String sPropertyName)
  {
    return m_aDeclarations.getAllDeclarationsOfPropertyName (sPropertyName);
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <CSSDeclaration> getAllDeclarationsOfPropertyNameCaseInsensitive (@Nullable final String sPropertyName)
  {
    return m_aDeclarations.getAllDeclarationsOfPropertyNameCaseInsensitive (sPropertyName);
  }

  @Nonnull
  public String getSelectorsAsCSSString (@Nonnull final ICSSWriterSettings aSettings,
                                         @Nonnegative final int nIndentLevel)
  {
    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();
    final StringBuilder aSB = new StringBuilder ();
    boolean bFirst = true;
    for (final CSSSelector aSelector : m_aSelectors)
    {
      if (bFirst)
        bFirst = false;
      else
      {
        aSB.append (',');
        if (!bOptimizedOutput)
          aSB.append (aSettings.getNewLineString ()).append (aSettings.getIndent (nIndentLevel));
      }
      aSB.append (aSelector.getAsCSSString (aSettings, nIndentLevel));
    }
    return aSB.toString ();
  }

  @Nonnull
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    if (aSettings.isRemoveUnnecessaryCode () && !hasDeclarations ())
      return "";

    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();

    final StringBuilder aSB = new StringBuilder ();

    // Append the selectors
    aSB.append (getSelectorsAsCSSString (aSettings, nIndentLevel));

    // Append the declarations
    aSB.append (m_aDeclarations.getAsCSSString (aSettings, nIndentLevel));
    if (!bOptimizedOutput)
      aSB.append (aSettings.getNewLineString ());
    return aSB.toString ();
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
    final CSSStyleRule rhs = (CSSStyleRule) o;
    return m_aSelectors.equals (rhs.m_aSelectors) && m_aDeclarations.equals (rhs.m_aDeclarations);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aSelectors).append (m_aDeclarations).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("selectors", m_aSelectors)
                                       .append ("declarations", m_aDeclarations)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
