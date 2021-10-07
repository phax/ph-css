/*
 * Copyright (C) 2014-2021 Philip Helger (www.helger.com)
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
import java.util.function.Predicate;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.state.EChange;

/**
 * Abstract base class for all classes having top-level rules.
 *
 * @author Philip Helger
 * @since 5.0.5
 */
@NotThreadSafe
public abstract class AbstractHasTopLevelRules implements Serializable
{
  protected final ICommonsList <ICSSTopLevelRule> m_aRules = new CommonsArrayList <> ();

  public AbstractHasTopLevelRules ()
  {}

  /**
   * Check if any top-level rule. This method only considers top-level rules and
   * not <code>@import</code> and <code>@namespace</code> rules!
   *
   * @return <code>true</code> if at least one top-level rule is present,
   *         <code>false</code> if otherwise.
   */
  public boolean hasRules ()
  {
    return m_aRules.isNotEmpty ();
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
   * @deprecated Use {@link #getRuleAtIndex(int)} instead
   */
  @Nullable
  @Deprecated
  public ICSSTopLevelRule getRule (@Nonnegative final int nIndex)
  {
    return getRuleAtIndex (nIndex);
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
    return m_aRules.getAtIndex (nIndex);
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
  public AbstractHasTopLevelRules addRule (@Nonnull final ICSSTopLevelRule aRule)
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
  public AbstractHasTopLevelRules addRule (@Nonnegative final int nIndex, @Nonnull final ICSSTopLevelRule aRule)
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
    return m_aRules.removeObject (aRule);
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
    return m_aRules.removeAtIndex (nRuleIndex);
  }

  /**
   * Remove all rules matching the passed predicate.
   *
   * @param aFilter
   *        The predicate to apply for deletion. May not be <code>null</code>.
   * @return {@link EChange#CHANGED} it at least one rule was removed,
   *         {@link EChange#UNCHANGED} otherwise.
   * @since 5.0.0
   */
  @Nonnull
  public EChange removeRules (@Nonnull final Predicate <? super ICSSTopLevelRule> aFilter)
  {
    return EChange.valueOf (m_aRules.removeIf (aFilter));
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
    return m_aRules.removeAll ();
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
  public ICommonsList <ICSSTopLevelRule> getAllRules ()
  {
    return m_aRules.getClone ();
  }

  /**
   * Get a copy of all contained top-level rules. This method only considers
   * top-level rules and not <code>@import</code> and <code>@namespace</code>
   * rules!
   *
   * @param aFilter
   *        The predicate to be applied
   * @return A copy of all contained top-level rules. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <ICSSTopLevelRule> getAllRules (@Nonnull final Predicate <? super ICSSTopLevelRule> aFilter)
  {
    return m_aRules.getAll (aFilter);
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
    return m_aRules.containsAny (r -> r instanceof CSSStyleRule);
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
    return m_aRules.getCount (r -> r instanceof CSSStyleRule);
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
    return m_aRules.getAtIndexMapped (r -> r instanceof CSSStyleRule, nIndex, r -> (CSSStyleRule) r);
  }

  /**
   * Get a list of all top-level rules that are style rules (implementing
   * {@link CSSStyleRule}).
   *
   * @return A copy of all contained style rules. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <CSSStyleRule> getAllStyleRules ()
  {
    return m_aRules.getAllMapped (r -> r instanceof CSSStyleRule, r -> (CSSStyleRule) r);
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
    return m_aRules.containsAny (r -> r instanceof CSSPageRule);
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
    return m_aRules.getCount (r -> r instanceof CSSPageRule);
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
    return m_aRules.getAtIndexMapped (r -> r instanceof CSSPageRule, nIndex, r -> (CSSPageRule) r);
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
  public ICommonsList <CSSPageRule> getAllPageRules ()
  {
    return m_aRules.getAllMapped (r -> r instanceof CSSPageRule, r -> (CSSPageRule) r);
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
    return m_aRules.containsAny (r -> r instanceof CSSMediaRule);
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
    return m_aRules.getCount (r -> r instanceof CSSMediaRule);
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
    return m_aRules.getAtIndexMapped (r -> r instanceof CSSMediaRule, nIndex, r -> (CSSMediaRule) r);
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
  public ICommonsList <CSSMediaRule> getAllMediaRules ()
  {
    return m_aRules.getAllMapped (r -> r instanceof CSSMediaRule, r -> (CSSMediaRule) r);
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
    return m_aRules.containsAny (r -> r instanceof CSSFontFaceRule);
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
    return m_aRules.getCount (r -> r instanceof CSSFontFaceRule);
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
    return m_aRules.getAtIndexMapped (r -> r instanceof CSSFontFaceRule, nIndex, r -> (CSSFontFaceRule) r);
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
  public ICommonsList <CSSFontFaceRule> getAllFontFaceRules ()
  {
    return m_aRules.getAllMapped (r -> r instanceof CSSFontFaceRule, r -> (CSSFontFaceRule) r);
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
    return m_aRules.containsAny (r -> r instanceof CSSKeyframesRule);
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
    return m_aRules.getCount (r -> r instanceof CSSKeyframesRule);
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
    return m_aRules.getAtIndexMapped (r -> r instanceof CSSKeyframesRule, nIndex, r -> (CSSKeyframesRule) r);
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
  public ICommonsList <CSSKeyframesRule> getAllKeyframesRules ()
  {
    return m_aRules.getAllMapped (r -> r instanceof CSSKeyframesRule, r -> (CSSKeyframesRule) r);
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
    return m_aRules.containsAny (r -> r instanceof CSSViewportRule);
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
    return m_aRules.getCount (r -> r instanceof CSSViewportRule);
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
    return m_aRules.getAtIndexMapped (r -> r instanceof CSSViewportRule, nIndex, r -> (CSSViewportRule) r);
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
  public ICommonsList <CSSViewportRule> getAllViewportRules ()
  {
    return m_aRules.getAllMapped (r -> r instanceof CSSViewportRule, r -> (CSSViewportRule) r);
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
    return m_aRules.containsAny (r -> r instanceof CSSSupportsRule);
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
    return m_aRules.getCount (r -> r instanceof CSSSupportsRule);
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
    return m_aRules.getAtIndexMapped (r -> r instanceof CSSSupportsRule, nIndex, r -> (CSSSupportsRule) r);
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
  public ICommonsList <CSSSupportsRule> getAllSupportsRules ()
  {
    return m_aRules.getAllMapped (r -> r instanceof CSSSupportsRule, r -> (CSSSupportsRule) r);
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
    return m_aRules.containsAny (r -> r instanceof CSSUnknownRule);
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
    return m_aRules.getCount (r -> r instanceof CSSUnknownRule);
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
    return m_aRules.getAtIndexMapped (r -> r instanceof CSSUnknownRule, nIndex, r -> (CSSUnknownRule) r);
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
  public ICommonsList <CSSUnknownRule> getAllUnknownRules ()
  {
    return m_aRules.getAllMapped (r -> r instanceof CSSUnknownRule, r -> (CSSUnknownRule) r);
  }
}
