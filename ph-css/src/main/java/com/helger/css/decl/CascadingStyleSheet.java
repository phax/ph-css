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
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.hashcode.HashCodeGenerator;
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
public class CascadingStyleSheet extends AbstractHasTopLevelRules implements ICSSSourceLocationAware
{
  private final ICommonsList <CSSImportRule> m_aImportRules = new CommonsArrayList <> ();
  private final ICommonsList <CSSNamespaceRule> m_aNamespaceRules = new CommonsArrayList <> ();
  private CSSSourceLocation m_aSourceLocation;

  public CascadingStyleSheet ()
  {}

  /**
   * @return <code>true</code> if at least one <code>@import</code> rule is
   *         present, <code>false</code> otherwise.
   */
  public boolean hasImportRules ()
  {
    return m_aImportRules.isNotEmpty ();
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
    return m_aImportRules.getAtIndex (nIndex);
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
    return m_aImportRules.removeObject (aImportRule);
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
    return m_aImportRules.removeAtIndex (nImportRuleIndex);
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
    return m_aImportRules.removeAll ();
  }

  /**
   * @return A copy of all contained <code>@import</code> rules. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <CSSImportRule> getAllImportRules ()
  {
    return m_aImportRules.getClone ();
  }

  /**
   * @return <code>true</code> if at least one <code>@namespace</code> rule is
   *         present, <code>false</code> otherwise.
   */
  public boolean hasNamespaceRules ()
  {
    return m_aNamespaceRules.isNotEmpty ();
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
    return m_aNamespaceRules.getAtIndex (nIndex);
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
  public CascadingStyleSheet addNamespaceRule (@Nonnegative final int nIndex, @Nonnull final CSSNamespaceRule aNamespaceRule)
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
    return m_aNamespaceRules.removeObject (aNamespaceRule);
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
    return m_aNamespaceRules.removeAtIndex (nNamespaceRuleIndex);
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
    return m_aNamespaceRules.removeAll ();
  }

  /**
   * @return A copy of all <code>@namespace</code> rules. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <CSSNamespaceRule> getAllNamespaceRules ()
  {
    return m_aNamespaceRules.getClone ();
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
    final CascadingStyleSheet rhs = (CascadingStyleSheet) o;
    return m_aImportRules.equals (rhs.m_aImportRules) && m_aNamespaceRules.equals (rhs.m_aNamespaceRules) && m_aRules.equals (rhs.m_aRules);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aImportRules).append (m_aNamespaceRules).append (m_aRules).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("importRules", m_aImportRules)
                                       .append ("namespaceRules", m_aNamespaceRules)
                                       .append ("rules", m_aRules)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
