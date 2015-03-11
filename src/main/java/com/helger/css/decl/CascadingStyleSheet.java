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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotations.ReturnsMutableCopy;
import com.helger.commons.collections.CollectionHelper;
import com.helger.commons.hash.HashCodeGenerator;
import com.helger.commons.state.EChange;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;

/**
 * This is the main object for a parsed CSS declaration. It has special handling
 * for import and namespace rules, as these rules must always be on the
 * beginning of a file. All other rules (all implementing
 * {@link ICSSTopLevelRule}) are maintained in a combined list.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CascadingStyleSheet implements ICSSSourceLocationAware, Serializable
{
  private final List <CSSImportRule> m_aImportRules = new ArrayList <CSSImportRule> ();
  private final List <CSSNamespaceRule> m_aNamespaceRules = new ArrayList <CSSNamespaceRule> ();
  private final List <ICSSTopLevelRule> m_aRules = new ArrayList <ICSSTopLevelRule> ();
  private CSSSourceLocation m_aSourceLocation;

  public CascadingStyleSheet ()
  {}

  /**
   * @return <code>true</code> if at least one <code>@import</code> rule is
   *         present, <code>false</code> otherwise.
   */
  public boolean hasImportRules ()
  {
    return !m_aImportRules.isEmpty ();
  }

  /**
   * @return The number of contained <code>@import</code> rules. Always &ge; 0.
   */
  @Nonnegative
  public int getImportRuleCount ()
  {
    return m_aImportRules.size ();
  }

  /**
   * Get the <code>@import</code> rule at the specified index.
   *
   * @param nIndex
   *        The index to be resolved. Should be &ge; 0 and &lt;
   *        {@link #getImportRuleCount()}.
   * @return <code>null</code> if an invalid index was specified.
   * @since 3.7.4
   */
  @Nullable
  public CSSImportRule getImportRuleAtIndex (@Nonnegative final int nIndex)
  {
    return CollectionHelper.getSafe (m_aImportRules, nIndex);
  }

  /**
   * Add a new <code>@import</code> rule at the end of the <code>@import</code>
   * rule list.
   *
   * @param aImportRule
   *        The import rule to add. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public CascadingStyleSheet addImportRule (@Nonnull final CSSImportRule aImportRule)
  {
    ValueEnforcer.notNull (aImportRule, "ImportRule");

    m_aImportRules.add (aImportRule);
    return this;
  }

  /**
   * Add a new <code>@import</code> rule at a specified index of the
   * <code>@import</code> rule list.
   *
   * @param nIndex
   *        The index where the rule should be added. Must be &ge; 0.
   * @param aImportRule
   *        The import rule to add. May not be <code>null</code>.
   * @return this
   * @throws ArrayIndexOutOfBoundsException
   *         if the index is invalid
   */
  @Nonnull
  public CascadingStyleSheet addImportRule (@Nonnegative final int nIndex, @Nonnull final CSSImportRule aImportRule)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aImportRule, "ImportRule");

    if (nIndex >= getImportRuleCount ())
      m_aImportRules.add (aImportRule);
    else
      m_aImportRules.add (nIndex, aImportRule);
    return this;
  }

  /**
   * Remove the specified <code>@import</code> rule.
   *
   * @param aImportRule
   *        The import rule to be removed. May be <code>null</code>.
   * @return {@link EChange#CHANGED} if removal was successful,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   */
  @Nonnull
  public EChange removeImportRule (@Nullable final CSSImportRule aImportRule)
  {
    return EChange.valueOf (m_aImportRules.remove (aImportRule));
  }

  /**
   * Remove the <code>@import</code> rule at the specified index.
   *
   * @param nImportRuleIndex
   *        The index to be removed. Should be &ge; 0.
   * @return {@link EChange#CHANGED} if removal was successful,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   */
  @Nonnull
  public EChange removeImportRule (@Nonnegative final int nImportRuleIndex)
  {
    if (nImportRuleIndex < 0 || nImportRuleIndex >= m_aImportRules.size ())
      return EChange.UNCHANGED;
    m_aImportRules.remove (nImportRuleIndex);
    return EChange.CHANGED;
  }

  /**
   * Remove all <code>@import</code> rules.
   *
   * @return {@link EChange#CHANGED} if any rule was removed,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   * @since 3.7.3
   */
  @Nonnull
  public EChange removeAllImportRules ()
  {
    if (m_aImportRules.isEmpty ())
      return EChange.UNCHANGED;
    m_aImportRules.clear ();
    return EChange.CHANGED;
  }

  /**
   * @return A copy of all contained <code>@import</code> rules. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <CSSImportRule> getAllImportRules ()
  {
    return CollectionHelper.newList (m_aImportRules);
  }

  /**
   * @return <code>true</code> if at least one <code>@namespace</code> rule is
   *         present, <code>false</code> otherwise.
   */
  public boolean hasNamespaceRules ()
  {
    return !m_aNamespaceRules.isEmpty ();
  }

  /**
   * @return The number of contained <code>@namespace</code> rules. Always &ge;
   *         0.
   */
  @Nonnegative
  public int getNamespaceRuleCount ()
  {
    return m_aNamespaceRules.size ();
  }

  /**
   * Get the <code>@namespace</code> rule at the specified index.
   *
   * @param nIndex
   *        The index to be resolved. Should be &ge; 0 and &lt;
   *        {@link #getNamespaceRuleCount()}.
   * @return <code>null</code> if an invalid index was specified.
   * @since 3.7.4
   */
  @Nullable
  public CSSNamespaceRule getNamespaceRuleAtIndex (@Nonnegative final int nIndex)
  {
    return CollectionHelper.getSafe (m_aNamespaceRules, nIndex);
  }

  /**
   * Add a new <code>@namespace</code> rule at the end of the
   * <code>@namespace</code> rule list.
   *
   * @param aNamespaceRule
   *        The namespace rule to be added. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public CascadingStyleSheet addNamespaceRule (@Nonnull final CSSNamespaceRule aNamespaceRule)
  {
    ValueEnforcer.notNull (aNamespaceRule, "NamespaceRule");

    m_aNamespaceRules.add (aNamespaceRule);
    return this;
  }

  /**
   * Add a new <code>@namespace</code> rule at the specified index of the
   * <code>@namespace</code> rule list.
   *
   * @param nIndex
   *        The index where the rule should be added. Must be &ge; 0.
   * @param aNamespaceRule
   *        The namespace rule to be added. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public CascadingStyleSheet addNamespaceRule (@Nonnegative final int nIndex,
                                               @Nonnull final CSSNamespaceRule aNamespaceRule)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aNamespaceRule, "NamespaceRule");

    if (nIndex >= getNamespaceRuleCount ())
      m_aNamespaceRules.add (aNamespaceRule);
    else
      m_aNamespaceRules.add (nIndex, aNamespaceRule);
    return this;
  }

  /**
   * Remove the specified <code>@namespace</code> rule.
   *
   * @param aNamespaceRule
   *        The namespace rule to be removed. May be <code>null</code>.
   * @return {@link EChange#CHANGED} if the namespace rule was successfully
   *         removed, {@link EChange#UNCHANGED} otherwise. Never
   *         <code>null</code>.
   */
  @Nonnull
  public EChange removeNamespaceRule (@Nullable final CSSNamespaceRule aNamespaceRule)
  {
    return EChange.valueOf (m_aNamespaceRules.remove (aNamespaceRule));
  }

  /**
   * Remove the <code>@namespace</code> rule at the specified index.
   *
   * @param nNamespaceRuleIndex
   *        The index to be removed. Should be &ge; 0.
   * @return {@link EChange#CHANGED} if the namespace rule was successfully
   *         removed, {@link EChange#UNCHANGED} otherwise. Never
   *         <code>null</code>.
   */
  @Nonnull
  public EChange removeNamespaceRule (@Nonnegative final int nNamespaceRuleIndex)
  {
    if (nNamespaceRuleIndex < 0 || nNamespaceRuleIndex >= m_aNamespaceRules.size ())
      return EChange.UNCHANGED;
    m_aNamespaceRules.remove (nNamespaceRuleIndex);
    return EChange.CHANGED;
  }

  /**
   * Remove all <code>@namespace</code> rules.
   *
   * @return {@link EChange#CHANGED} if any rule was removed,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   * @since 3.7.3
   */
  @Nonnull
  public EChange removeAllNamespaceRules ()
  {
    if (m_aNamespaceRules.isEmpty ())
      return EChange.UNCHANGED;
    m_aNamespaceRules.clear ();
    return EChange.CHANGED;
  }

  /**
   * @return A copy of all <code>@namespace</code> rules. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <CSSNamespaceRule> getAllNamespaceRules ()
  {
    return CollectionHelper.newList (m_aNamespaceRules);
  }

  /**
   * Check if any top-level rule. This method only considers top-level rules and
   * not <code>@import</code> and <code>@namespace</code> rules!
   *
   * @return <code>true</code> if at least one top-level rule is present,
   *         <code>false</code> if otherwise.
   */
  public boolean hasRules ()
  {
    return !m_aRules.isEmpty ();
  }

  /**
   * Get the number of total contained top-level rules. This method only
   * considers top-level rules and not <code>@import</code> and
   * <code>@namespace</code> rules!
   *
   * @return The number of total contained top-level rules. Always &ge; 0.
   */
  @Nonnegative
  public int getRuleCount ()
  {
    return m_aRules.size ();
  }

  /**
   * Get the top-level rule at the specified index.
   *
   * @param nIndex
   *        The index to be resolved. Should be &ge; 0 and &lt;
   *        {@link #getRuleCount()}.
   * @return <code>null</code> if an invalid index was specified.
   * @since 3.7.4
   */
  @Nullable
  public ICSSTopLevelRule getRuleAtIndex (@Nonnegative final int nIndex)
  {
    return CollectionHelper.getSafe (m_aRules, nIndex);
  }

  /**
   * Add a new top-level rule at the end. This method only considers top-level
   * rules and not <code>@import</code> and <code>@namespace</code> rules!
   *
   * @param aRule
   *        The rule to be added. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public CascadingStyleSheet addRule (@Nonnull final ICSSTopLevelRule aRule)
  {
    ValueEnforcer.notNull (aRule, "Rule");

    m_aRules.add (aRule);
    return this;
  }

  /**
   * Add a new top-level rule at the specified index. This method only considers
   * top-level rules and not <code>@import</code> and <code>@namespace</code>
   * rules!
   *
   * @param nIndex
   *        The index where the top-level rule should be added. Must be &ge; 0.
   * @param aRule
   *        The rule to be added. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public CascadingStyleSheet addRule (@Nonnegative final int nIndex, @Nonnull final ICSSTopLevelRule aRule)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aRule, "Rule");

    if (nIndex >= getRuleCount ())
      m_aRules.add (aRule);
    else
      m_aRules.add (nIndex, aRule);
    return this;
  }

  /**
   * Remove the specified top-level rule. This method only considers top-level
   * rules and not <code>@import</code> and <code>@namespace</code> rules!
   *
   * @param aRule
   *        The rule to be removed. May be <code>null</code>.
   * @return {@link EChange#CHANGED} if the rule was successfully removed,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   */
  @Nonnull
  public EChange removeRule (@Nullable final ICSSTopLevelRule aRule)
  {
    return EChange.valueOf (m_aRules.remove (aRule));
  }

  /**
   * Remove the rule at the specified index. This method only considers
   * top-level rules and not <code>@import</code> and <code>@namespace</code>
   * rules!
   *
   * @param nRuleIndex
   *        The index of the rule to be removed. Should be &ge; 0.
   * @return {@link EChange#CHANGED} if the rule at the specified index was
   *         successfully removed, {@link EChange#UNCHANGED} otherwise. Never
   *         <code>null</code>.
   */
  @Nonnull
  public EChange removeRule (@Nonnegative final int nRuleIndex)
  {
    if (nRuleIndex < 0 || nRuleIndex >= m_aRules.size ())
      return EChange.UNCHANGED;
    m_aRules.remove (nRuleIndex);
    return EChange.CHANGED;
  }

  /**
   * Remove all top-level rules.
   *
   * @return {@link EChange#CHANGED} if any rule was removed,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   * @since 3.7.3
   */
  @Nonnull
  public EChange removeAllRules ()
  {
    if (m_aRules.isEmpty ())
      return EChange.UNCHANGED;
    m_aRules.clear ();
    return EChange.CHANGED;
  }

  /**
   * Get a copy of all contained top-level rules. This method only considers
   * top-level rules and not <code>@import</code> and <code>@namespace</code>
   * rules!
   *
   * @return A copy of all contained top-level rules. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <ICSSTopLevelRule> getAllRules ()
  {
    return CollectionHelper.newList (m_aRules);
  }

  /**
   * Check if at least one of the top-level rules is a style rule (implementing
   * {@link CSSStyleRule}).
   *
   * @return <code>true</code> if at least one style rule is contained,
   *         <code>false</code> otherwise.
   */
  public boolean hasStyleRules ()
  {
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSStyleRule)
        return true;
    return false;
  }

  /**
   * Get the number of top-level rules that are style rules (implementing
   * {@link CSSStyleRule}).
   *
   * @return The number of contained style rules. Always &ge; 0.
   */
  @Nonnegative
  public int getStyleRuleCount ()
  {
    int ret = 0;
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSStyleRule)
        ret++;
    return ret;
  }

  /**
   * Get the style rule at the specified index.
   *
   * @param nIndex
   *        The index to be resolved. Should be &ge; 0 and &lt;
   *        {@link #getStyleRuleCount()}.
   * @return <code>null</code> if an invalid index was specified.
   * @since 3.7.4
   */
  @Nullable
  public CSSStyleRule getStyleRuleAtIndex (@Nonnegative final int nIndex)
  {
    if (nIndex >= 0)
    {
      int nCurIndex = 0;
      for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
        if (aTopLevelRule instanceof CSSStyleRule)
        {
          if (nCurIndex == nIndex)
            return (CSSStyleRule) aTopLevelRule;
          ++nCurIndex;
        }
    }
    return null;
  }

  /**
   * Get a list of all top-level rules that are style rules (implementing
   * {@link CSSStyleRule}).
   *
   * @return A copy of all contained style rules. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <CSSStyleRule> getAllStyleRules ()
  {
    final List <CSSStyleRule> ret = new ArrayList <CSSStyleRule> ();
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSStyleRule)
        ret.add ((CSSStyleRule) aTopLevelRule);
    return ret;
  }

  /**
   * Check if at least one of the top-level rules is a page rule (implementing
   * {@link CSSPageRule}).
   *
   * @return <code>true</code> if at least one <code>@page</code> rule is
   *         contained, <code>false</code> otherwise.
   */
  public boolean hasPageRules ()
  {
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSPageRule)
        return true;
    return false;
  }

  /**
   * Get the number of top-level rules that are page rules (implementing
   * {@link CSSPageRule}).
   *
   * @return The number of contained <code>@page</code> rules. Always &ge; 0.
   */
  @Nonnegative
  public int getPageRuleCount ()
  {
    int ret = 0;
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSPageRule)
        ret++;
    return ret;
  }

  /**
   * Get the <code>@page</code> rule at the specified index.
   *
   * @param nIndex
   *        The index to be resolved. Should be &ge; 0 and &lt;
   *        {@link #getPageRuleCount()}.
   * @return <code>null</code> if an invalid index was specified.
   * @since 3.7.4
   */
  @Nullable
  public CSSPageRule getPageRuleAtIndex (@Nonnegative final int nIndex)
  {
    if (nIndex >= 0)
    {
      int nCurIndex = 0;
      for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
        if (aTopLevelRule instanceof CSSPageRule)
        {
          if (nCurIndex == nIndex)
            return (CSSPageRule) aTopLevelRule;
          ++nCurIndex;
        }
    }
    return null;
  }

  /**
   * Get a list of all top-level rules that are page rules (implementing
   * {@link CSSPageRule}).
   *
   * @return A copy of all contained <code>@page</code> rules. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <CSSPageRule> getAllPageRules ()
  {
    final List <CSSPageRule> ret = new ArrayList <CSSPageRule> ();
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSPageRule)
        ret.add ((CSSPageRule) aTopLevelRule);
    return ret;
  }

  /**
   * Check if at least one of the top-level rules is a media rule (implementing
   * {@link CSSMediaRule}).
   *
   * @return <code>true</code> if at least one <code>@media</code> rule is
   *         contained, <code>false</code> otherwise.
   */
  public boolean hasMediaRules ()
  {
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSMediaRule)
        return true;
    return false;
  }

  /**
   * Get the number of top-level rules that are media rules (implementing
   * {@link CSSMediaRule}).
   *
   * @return The number of contained <code>@media</code> rules. Always &ge; 0.
   */
  @Nonnegative
  public int getMediaRuleCount ()
  {
    int ret = 0;
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSMediaRule)
        ret++;
    return ret;
  }

  /**
   * Get the <code>@media</code> rule at the specified index.
   *
   * @param nIndex
   *        The index to be resolved. Should be &ge; 0 and &lt;
   *        {@link #getMediaRuleCount()}.
   * @return <code>null</code> if an invalid index was specified.
   * @since 3.7.4
   */
  @Nullable
  public CSSMediaRule getMediaRuleAtIndex (@Nonnegative final int nIndex)
  {
    if (nIndex >= 0)
    {
      int nCurIndex = 0;
      for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
        if (aTopLevelRule instanceof CSSMediaRule)
        {
          if (nCurIndex == nIndex)
            return (CSSMediaRule) aTopLevelRule;
          ++nCurIndex;
        }
    }
    return null;
  }

  /**
   * Get a list of all top-level rules that are media rules (implementing
   * {@link CSSMediaRule}).
   *
   * @return A copy of all contained <code>@media</code> rules. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <CSSMediaRule> getAllMediaRules ()
  {
    final List <CSSMediaRule> ret = new ArrayList <CSSMediaRule> ();
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSMediaRule)
        ret.add ((CSSMediaRule) aTopLevelRule);
    return ret;
  }

  /**
   * Check if at least one of the top-level rules is a font-face rule
   * (implementing {@link CSSFontFaceRule}).
   *
   * @return <code>true</code> if at least one <code>@font-face</code> rule is
   *         contained, <code>false</code> otherwise.
   */
  public boolean hasFontFaceRules ()
  {
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSFontFaceRule)
        return true;
    return false;
  }

  /**
   * Get the number of top-level rules that are font-face rules (implementing
   * {@link CSSFontFaceRule}).
   *
   * @return The number of contained <code>@font-face</code> rules. Always &ge;
   *         0.
   */
  @Nonnegative
  public int getFontFaceRuleCount ()
  {
    int ret = 0;
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSFontFaceRule)
        ret++;
    return ret;
  }

  /**
   * Get the <code>@font-face</code> rule at the specified index.
   *
   * @param nIndex
   *        The index to be resolved. Should be &ge; 0 and &lt;
   *        {@link #getFontFaceRuleCount()}.
   * @return <code>null</code> if an invalid index was specified.
   * @since 3.7.4
   */
  @Nullable
  public CSSFontFaceRule getFontFaceRuleAtIndex (@Nonnegative final int nIndex)
  {
    if (nIndex >= 0)
    {
      int nCurIndex = 0;
      for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
        if (aTopLevelRule instanceof CSSFontFaceRule)
        {
          if (nCurIndex == nIndex)
            return (CSSFontFaceRule) aTopLevelRule;
          ++nCurIndex;
        }
    }
    return null;
  }

  /**
   * Get a list of all top-level rules that are font-face rules (implementing
   * {@link CSSFontFaceRule}).
   *
   * @return A copy of all contained <code>@font-face</code> rules. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <CSSFontFaceRule> getAllFontFaceRules ()
  {
    final List <CSSFontFaceRule> ret = new ArrayList <CSSFontFaceRule> ();
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSFontFaceRule)
        ret.add ((CSSFontFaceRule) aTopLevelRule);
    return ret;
  }

  /**
   * Check if at least one of the top-level rules is a keyframes rule
   * (implementing {@link CSSKeyframesRule}).
   *
   * @return <code>true</code> if at least one <code>@keyframes</code> rule is
   *         contained, <code>false</code> otherwise.
   */
  public boolean hasKeyframesRules ()
  {
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSKeyframesRule)
        return true;
    return false;
  }

  /**
   * Get the number of top-level rules that are keyframes rules (implementing
   * {@link CSSKeyframesRule}).
   *
   * @return The number of contained <code>@keyframes</code> rules. Always &ge;
   *         0.
   */
  @Nonnegative
  public int getKeyframesRuleCount ()
  {
    int ret = 0;
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSKeyframesRule)
        ret++;
    return ret;
  }

  /**
   * Get the <code>@keyframes</code> rule at the specified index.
   *
   * @param nIndex
   *        The index to be resolved. Should be &ge; 0 and &lt;
   *        {@link #getKeyframesRuleCount()}.
   * @return <code>null</code> if an invalid index was specified.
   * @since 3.7.4
   */
  @Nullable
  public CSSKeyframesRule getKeyframesRuleAtIndex (@Nonnegative final int nIndex)
  {
    if (nIndex >= 0)
    {
      int nCurIndex = 0;
      for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
        if (aTopLevelRule instanceof CSSKeyframesRule)
        {
          if (nCurIndex == nIndex)
            return (CSSKeyframesRule) aTopLevelRule;
          ++nCurIndex;
        }
    }
    return null;
  }

  /**
   * Get a list of all top-level rules that are keyframes rules (implementing
   * {@link CSSKeyframesRule}).
   *
   * @return A copy of all contained <code>@keyframes</code> rules. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <CSSKeyframesRule> getAllKeyframesRules ()
  {
    final List <CSSKeyframesRule> ret = new ArrayList <CSSKeyframesRule> ();
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSKeyframesRule)
        ret.add ((CSSKeyframesRule) aTopLevelRule);
    return ret;
  }

  /**
   * Check if at least one of the top-level rules is a viewport rule
   * (implementing {@link CSSViewportRule}).
   *
   * @return <code>true</code> if at least one <code>@viewport</code> rule is
   *         contained, <code>false</code> otherwise.
   */
  public boolean hasViewportRules ()
  {
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSViewportRule)
        return true;
    return false;
  }

  /**
   * Get the number of top-level rules that are viewport rules (implementing
   * {@link CSSViewportRule}).
   *
   * @return The number of contained <code>@viewport</code> rules. Always &ge;
   *         0.
   */
  @Nonnegative
  public int getViewportRuleCount ()
  {
    int ret = 0;
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSViewportRule)
        ret++;
    return ret;
  }

  /**
   * Get the <code>@viewport</code> rule at the specified index.
   *
   * @param nIndex
   *        The index to be resolved. Should be &ge; 0 and &lt;
   *        {@link #getViewportRuleCount()}.
   * @return <code>null</code> if an invalid index was specified.
   * @since 3.7.4
   */
  @Nullable
  public CSSViewportRule getViewportRuleAtIndex (@Nonnegative final int nIndex)
  {
    if (nIndex >= 0)
    {
      int nCurIndex = 0;
      for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
        if (aTopLevelRule instanceof CSSViewportRule)
        {
          if (nCurIndex == nIndex)
            return (CSSViewportRule) aTopLevelRule;
          ++nCurIndex;
        }
    }
    return null;
  }

  /**
   * Get a list of all top-level rules that are viewport rules (implementing
   * {@link CSSViewportRule}).
   *
   * @return A copy of all contained <code>@viewport</code> rules. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <CSSViewportRule> getAllViewportRules ()
  {
    final List <CSSViewportRule> ret = new ArrayList <CSSViewportRule> ();
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSViewportRule)
        ret.add ((CSSViewportRule) aTopLevelRule);
    return ret;
  }

  /**
   * Check if at least one of the top-level rules is a supports rule
   * (implementing {@link CSSSupportsRule}).
   *
   * @return <code>true</code> if at least one <code>@supports</code> rule is
   *         contained, <code>false</code> otherwise.
   */
  public boolean hasSupportsRules ()
  {
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSSupportsRule)
        return true;
    return false;
  }

  /**
   * Get the number of top-level rules that are support rules (implementing
   * {@link CSSSupportsRule}).
   *
   * @return The number of contained <code>@supports</code> rules. Always &ge;
   *         0.
   */
  @Nonnegative
  public int getSupportsRuleCount ()
  {
    int ret = 0;
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSSupportsRule)
        ret++;
    return ret;
  }

  /**
   * Get the <code>@supports</code> rule at the specified index.
   *
   * @param nIndex
   *        The index to be resolved. Should be &ge; 0 and &lt;
   *        {@link #getSupportsRuleCount()}.
   * @return <code>null</code> if an invalid index was specified.
   * @since 3.7.4
   */
  @Nullable
  public CSSSupportsRule getSupportsRuleAtIndex (@Nonnegative final int nIndex)
  {
    if (nIndex >= 0)
    {
      int nCurIndex = 0;
      for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
        if (aTopLevelRule instanceof CSSSupportsRule)
        {
          if (nCurIndex == nIndex)
            return (CSSSupportsRule) aTopLevelRule;
          ++nCurIndex;
        }
    }
    return null;
  }

  /**
   * Get a list of all top-level rules that are support rules (implementing
   * {@link CSSSupportsRule}).
   *
   * @return A copy of all contained <code>@supports</code> rules. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <CSSSupportsRule> getAllSupportsRules ()
  {
    final List <CSSSupportsRule> ret = new ArrayList <CSSSupportsRule> ();
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSSupportsRule)
        ret.add ((CSSSupportsRule) aTopLevelRule);
    return ret;
  }

  /**
   * Check if at least one of the top-level rules is an unknown rule
   * (implementing {@link CSSUnknownRule}).
   *
   * @return <code>true</code> if at least one unknown <code>@</code> rule is
   *         contained, <code>false</code> otherwise.
   */
  public boolean hasUnknownRules ()
  {
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSUnknownRule)
        return true;
    return false;
  }

  /**
   * Get the number of top-level rules that are unknown rules (implementing
   * {@link CSSUnknownRule}).
   *
   * @return The number of contained unknown <code>@</code> rules. Always &ge;
   *         0.
   */
  @Nonnegative
  public int getUnknownRuleCount ()
  {
    int ret = 0;
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSUnknownRule)
        ret++;
    return ret;
  }

  /**
   * Get the unknown rule at the specified index.
   *
   * @param nIndex
   *        The index to be resolved. Should be &ge; 0 and &lt;
   *        {@link #getUnknownRuleCount()}.
   * @return <code>null</code> if an invalid index was specified.
   * @since 3.7.4
   */
  @Nullable
  public CSSUnknownRule getUnknownRuleAtIndex (@Nonnegative final int nIndex)
  {
    if (nIndex >= 0)
    {
      int nCurIndex = 0;
      for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
        if (aTopLevelRule instanceof CSSUnknownRule)
        {
          if (nCurIndex == nIndex)
            return (CSSUnknownRule) aTopLevelRule;
          ++nCurIndex;
        }
    }
    return null;
  }

  /**
   * Get a list of all top-level rules that are unknown rules (implementing
   * {@link CSSUnknownRule}).
   *
   * @return A copy of all contained unknown <code>@</code> rules. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <CSSUnknownRule> getAllUnknownRules ()
  {
    final List <CSSUnknownRule> ret = new ArrayList <CSSUnknownRule> ();
    for (final ICSSTopLevelRule aTopLevelRule : m_aRules)
      if (aTopLevelRule instanceof CSSUnknownRule)
        ret.add ((CSSUnknownRule) aTopLevelRule);
    return ret;
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
    final CascadingStyleSheet rhs = (CascadingStyleSheet) o;
    return m_aImportRules.equals (rhs.m_aImportRules) &&
           m_aNamespaceRules.equals (rhs.m_aNamespaceRules) &&
           m_aRules.equals (rhs.m_aRules);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aImportRules)
                                       .append (m_aNamespaceRules)
                                       .append (m_aRules)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("importRules", m_aImportRules)
                                       .append ("namespaceRules", m_aNamespaceRules)
                                       .append ("rules", m_aRules)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .toString ();
  }
}
