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

import com.helger.css.CCSS;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

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
 * Represents a single CSS style rule. A style rule consists of a number of
 * {@link CSSSelector selectors} (determines the elements to which
 * the style rule applies), a number of {@link CSSDeclarationContainer declarations}
 * (the styles to be applied to the selected elements), and a number of
 * {@link ICSSNestedRule nested rules} (the rules nested within the style rule,
 * e.g. media rules, supports rules, or nested declarations).
 *
 * <p>Example:
 *
 * <pre>div {
  color: red;
  &:hover {
    color: blue;
  }
}</pre>
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSStyleRule implements ICSSTopLevelRule, ICSSNestedRule, IHasCSSDeclarations <CSSStyleRule>, IHasCSSNestedRules<CSSStyleRule>, ICSSSourceLocationAware
{
  private final ICommonsList <CSSSelector> m_aSelectors = new CommonsArrayList <> ();
  private final CSSDeclarationContainer m_aDeclarations = new CSSDeclarationContainer ();
  private final ICommonsList <ICSSNestedRule> m_aRules = new CommonsArrayList <> ();
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

  @NonNull
  public CSSStyleRule addSelector (@NonNull final ICSSSelectorMember aSingleSelectorMember)
  {
    ValueEnforcer.notNull (aSingleSelectorMember, "SingleSelectorMember");

    return addSelector (new CSSSelector ().addMember (aSingleSelectorMember));
  }

  @NonNull
  public CSSStyleRule addSelector (@NonNull final CSSSelector aSelector)
  {
    ValueEnforcer.notNull (aSelector, "Selector");

    m_aSelectors.add (aSelector);
    return this;
  }

  @NonNull
  public CSSStyleRule addSelector (@Nonnegative final int nIndex, @NonNull final ICSSSelectorMember aSingleSelectorMember)
  {
    ValueEnforcer.notNull (aSingleSelectorMember, "SingleSelectorMember");

    return addSelector (nIndex, new CSSSelector ().addMember (aSingleSelectorMember));
  }

  @NonNull
  public CSSStyleRule addSelector (@Nonnegative final int nIndex, @NonNull final CSSSelector aSelector)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aSelector, "Selector");

    if (nIndex >= getSelectorCount ())
      m_aSelectors.add (aSelector);
    else
      m_aSelectors.add (nIndex, aSelector);
    return this;
  }

  @NonNull
  public EChange removeSelector (@NonNull final CSSSelector aSelector)
  {
    return m_aSelectors.removeObject (aSelector);
  }

  @NonNull
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
  @NonNull
  public EChange removeAllSelectors ()
  {
    return m_aSelectors.removeAll ();
  }

  @Nullable
  public CSSSelector getSelectorAtIndex (@Nonnegative final int nSelectorIndex)
  {
    return m_aSelectors.getAtIndex (nSelectorIndex);
  }

  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <CSSSelector> getAllSelectors ()
  {
    return m_aSelectors.getClone ();
  }

  public boolean hasRules()
  {
    return m_aRules.isNotEmpty ();
  }

  @Nonnegative
  public int getRuleCount()
  {
    return m_aRules.size ();
  }

  @NonNull
  public CSSStyleRule addRule(@NonNull final ICSSNestedRule aRule)
  {
    ValueEnforcer.notNull (aRule, "Rule");

    m_aRules.add (aRule);
    return this;
  }

  @NonNull
  public CSSStyleRule addRule(@Nonnegative final int nIndex, @NonNull final ICSSNestedRule aRule)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aRule, "Rule");

    if (nIndex >= getRuleCount ())
      m_aRules.add (aRule);
    else
      m_aRules.add (nIndex, aRule);
    return this;
  }

  @NonNull
  public EChange removeRule(@NonNull final ICSSNestedRule aRule)
  {
    return m_aRules.removeObject (aRule);
  }

  @NonNull
  public EChange removeRule(@Nonnegative final int nRuleIndex)
  {
    return m_aRules.removeAtIndex (nRuleIndex);
  }

  @NonNull
  public EChange removeAllRules()
  {
    return m_aRules.removeAll ();
  }

  @Nullable
  public ICSSNestedRule getRuleAtIndex(@Nonnegative final int nRuleIndex)
  {
    return m_aRules.getAtIndex (nRuleIndex);
  }

  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <ICSSNestedRule> getAllRules()
  {
    return m_aRules.getClone ();
  }

  @NonNull
  public CSSStyleRule addDeclaration (@NonNull final CSSDeclaration aDeclaration)
  {
    m_aDeclarations.addDeclaration (aDeclaration);
    return this;
  }

  @NonNull
  public CSSStyleRule addDeclaration (@Nonnegative final int nIndex, @NonNull final CSSDeclaration aNewDeclaration)
  {
    m_aDeclarations.addDeclaration (nIndex, aNewDeclaration);
    return this;
  }

  @NonNull
  public EChange removeDeclaration (@NonNull final CSSDeclaration aDeclaration)
  {
    return m_aDeclarations.removeDeclaration (aDeclaration);
  }

  @NonNull
  public EChange removeDeclaration (@Nonnegative final int nDeclarationIndex)
  {
    return m_aDeclarations.removeDeclaration (nDeclarationIndex);
  }

  @NonNull
  public EChange removeAllDeclarations ()
  {
    return m_aDeclarations.removeAllDeclarations ();
  }

  @NonNull
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

  @NonNull
  public CSSStyleRule setDeclarationAtIndex (@Nonnegative final int nIndex, @NonNull final CSSDeclaration aNewDeclaration)
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

  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <CSSDeclaration> getAllDeclarationsOfPropertyName (@Nullable final String sPropertyName)
  {
    return m_aDeclarations.getAllDeclarationsOfPropertyName (sPropertyName);
  }

  /**
   * Get the selectors as a serialized CSS string for writing to an output.
   *
   * @param aSettings
   *        The settings to be used to format the output. May not be
   *        <code>null</code>.
   * @param nIndentLevel
   *        The current indentation level
   * @return The content of the selectors as CSS string. Never <code>null</code>.
   */
  @NonNull
  public String getSelectorsAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
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

  @NonNull
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    if (aSettings.isRemoveUnnecessaryCode () && !hasDeclarations () && !hasRules())
      return "";

    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();
    final int nDeclCount = m_aDeclarations.getDeclarationCount ();
    final int nRuleCount = m_aRules.size ();
    final int nElementCount = nDeclCount + nRuleCount;

    final StringBuilder aSB = new StringBuilder ();

    // Append the selectors
    aSB.append (getSelectorsAsCSSString (aSettings, nIndentLevel));

    // Append the opening brace
    if (nElementCount == 0)
      aSB.append (bOptimizedOutput ? "{" : " {");
    else
      if (nElementCount == 1)
        aSB.append (bOptimizedOutput ? "{" : " { ");
      else
        aSB.append (bOptimizedOutput ? "{" : " {" + aSettings.getNewLineString ());

    // Append the declarations
    if (nDeclCount == 1 && nRuleCount == 0)
    {
      aSB.append (m_aDeclarations.get(0).getAsCSSString (aSettings, nIndentLevel));
      // No ';' at the last entry
      if (!bOptimizedOutput)
        aSB.append (CCSS.DEFINITION_END);
    }
    else
      if (nDeclCount >= 1) {
        int nIndex = 0;
        for (final CSSDeclaration aDeclaration : m_aDeclarations)
        {
          // Indentation
          if (!bOptimizedOutput)
            aSB.append (aSettings.getIndent (nIndentLevel + 1));
          // Emit the main element plus the semicolon
          aSB.append (aDeclaration.getAsCSSString (aSettings, nIndentLevel + 1));
          // No ';' at the last decl
          if (!bOptimizedOutput || nIndex < nDeclCount - 1 || nRuleCount > 0)
            aSB.append (CCSS.DEFINITION_END);
          // No line break at the last decl
          if (!bOptimizedOutput && nIndex != nDeclCount - 1)
            aSB.append (aSettings.getNewLineString ());
          ++nIndex;
        }
      }

    // Empty line between declarations and nested rules
    if (!bOptimizedOutput && (nDeclCount > 0 && nRuleCount > 0))
      aSB.append (aSettings.getNewLineString ()).append (aSettings.getNewLineString ());

    // Append the rules
    if (nRuleCount > 0)
    {
      boolean bFirst = true;
      int nRuleIndex = 0;
      for (final ICSSNestedRule aRule : m_aRules)
      {
        if (bFirst)
          bFirst = false;
        else
          if (!bOptimizedOutput)
            aSB.append (aSettings.getNewLineString ()).append (aSettings.getNewLineString ());

        if (!bOptimizedOutput)
          aSB.append (aSettings.getIndent (nIndentLevel + 1));
        aSB.append(aRule.getAsCSSString(aSettings, nIndentLevel + 1));
        // When outputting optimized, no semicolon is added after the last declaration
        // But when there are more rules, we need a semicolon as a separator
        if (bOptimizedOutput && aRule instanceof CSSNestedDeclarations && nRuleIndex != nRuleCount - 1) {
          aSB.append (CCSS.DEFINITION_END);
        }

        ++nRuleIndex;
      }
    }

    if (!bOptimizedOutput && nElementCount > 0)
      // Add space if there is exactly one declaration and no rules. Otherwise, add a line break
      if (nElementCount == 1 && nRuleCount == 0)
        aSB.append(" ");
      else
        aSB.append(aSettings.getNewLineString()).append(aSettings.getIndent(nIndentLevel));

    // Append the closing brace
    aSB.append ("}");

    return aSB.toString ();
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
    final CSSStyleRule rhs = (CSSStyleRule) o;
    return m_aSelectors.equals (rhs.m_aSelectors) && m_aDeclarations.equals (rhs.m_aDeclarations)
            && m_aRules.equals (rhs.m_aRules);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aSelectors).append (m_aDeclarations).append(m_aRules).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("selectors", m_aSelectors)
                                       .append ("declarations", m_aDeclarations)
                                      .append ("rules", m_aRules)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
