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

import com.helger.base.state.EChange;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonnegative;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.string.StringHelper;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.ICommonsList;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;

@NotThreadSafe
public class CSSLayerRule extends AbstractHasTopLevelRules implements ICSSTopLevelRule, ICSSNestedRule, ICSSSourceLocationAware
{
  private final ICommonsList <String> m_aSelectors;
  private CSSSourceLocation m_aSourceLocation;

  public CSSLayerRule (@Nullable final String sLayerSelector)
  {
    m_aSelectors = StringHelper.isNotEmpty (sLayerSelector) ? new CommonsArrayList <> (sLayerSelector)
                                                            : new CommonsArrayList <> ();
  }

  public CSSLayerRule (@NonNull final Iterable <String> aSelectors)
  {
    ValueEnforcer.notNullNoNullValue (aSelectors, "Selectors");
    m_aSelectors = new CommonsArrayList <> (aSelectors);
  }

  /**
   * Checks if at least one selector is present.
   * @return <code>true</code> if at least one selector is present, <code>false</code> otherwise.
   * @since 8.2.0
   */
  public boolean hasSelectors ()
  {
    return m_aSelectors.isNotEmpty ();
  }

  /**
   * Gets the number of selectors.
   * @return The number of selectors. Always &ge; 0.
   * @since 8.2.0
   */
  @Nonnegative
  public int getSelectorCount ()
  {
    return m_aSelectors.size ();
  }

  /**
   * Adds a selector to the end of the selector list.
   * @param sSelector The selector to be added. Must not be <code>null</code>.
   * @return This rule for chaining. Never <code>null</code>.
   * @since 8.2.0
   */
  @NonNull
  public CSSLayerRule addSelector (@NonNull final String sSelector)
  {
    ValueEnforcer.notNull (sSelector, "Selector");

    m_aSelectors.add (sSelector);
    return this;
  }

  /**
   * Adds a selector at the specified index. If the index is greater than the current number of selectors, the selector
   * is added at the end of the list.
   * @param nIndex The index at which the selector should be added. Must be &ge; 0.
   * @param sSelector The selector to be added. Must not be <code>null</code>.
   * @return This rule for chaining. Never <code>null</code>.
   * @since 8.2.0
   */
  @NonNull
  public CSSLayerRule addSelector (@Nonnegative final int nIndex, @NonNull final String sSelector)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (sSelector, "Selector");

    if (nIndex >= getSelectorCount ())
      m_aSelectors.add (sSelector);
    else
      m_aSelectors.add (nIndex, sSelector);
    return this;
  }

  /**
   * Remove the specified selector, if present.
   *
   * @param sSelector The selector to be removed. Must not be <code>null</code>.
   * @return {@link EChange#CHANGED} if the selector was removed, {@link EChange#UNCHANGED} if the selector was not found.
   *         Never <code>null</code>.
   * @since 8.2.0
   */
  @NonNull
  public EChange removeSelector (@NonNull final String sSelector)
  {
    return m_aSelectors.removeObject (sSelector);
  }

  /**
   * Removes the selector at the specified index.
   *
   * @param nSelectorIndex The index of the selector to be removed. Must be &ge; 0.
   * @return {@link EChange#CHANGED} if the selector was removed, {@link EChange#UNCHANGED} if the index was &ge; the
   * number of selectors. Never <code>null</code>.
   * @since 8.2.0
   */
  @NonNull
  public EChange removeSelector (@Nonnegative final int nSelectorIndex)
  {
    return m_aSelectors.removeAtIndex (nSelectorIndex);
  }

  /**
   * Removes all selectors.
   *
   * @return {@link EChange#CHANGED} if any selector was removed,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   * @since 8.2.0
   */
  @NonNull
  public EChange removeAllSelectors ()
  {
    return m_aSelectors.removeAll ();
  }

  /**
   * Gets the selector at the specified index.
   *
   * @param nSelectorIndex The index of the selector to be retrieved. Must be &ge; 0.
   * @return The selector at the specified index, or <code>null</code> if the index is &ge; the number of selectors.
   * @since 8.2.0
   */
  @Nullable
  public String getSelectorAtIndex (@Nonnegative final int nSelectorIndex)
  {
    return m_aSelectors.getAtIndex (nSelectorIndex);
  }

  /**
   * Gets a copy of all selectors. Modifications to the returned list do not affect this rule, and vice versa.
   * @return A list of all selectors. Never <code>null</code>.
   */
  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <String> getAllSelectors ()
  {
    return m_aSelectors.getClone ();
  }

  @NonNull
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    // Always ignore layer rules?
    if (!aSettings.isWriteLayerRules ())
      return "";

    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();

    final StringBuilder aSB = new StringBuilder ("@layer ");
    boolean bFirst = true;
    if (m_aSelectors.isNotEmpty ())
    {
      for (final String sSelector : m_aSelectors)
      {
        if (bFirst)
          bFirst = false;
        else
          aSB.append (bOptimizedOutput ? "," : ", ");
        aSB.append (sSelector);
      }
    }

    final int nRuleCount = m_aRules.size ();
    if (nRuleCount == 0)
    {
      aSB.append (';');
    }
    else
    {
      // At least one rule present
      aSB.append (bOptimizedOutput ? "{" : " {" + aSettings.getNewLineString ());
      bFirst = true;
      for (final ICSSTopLevelRule aRule : m_aRules)
      {
        final String sRuleCSS = aRule.getAsCSSString (aSettings, nIndentLevel + 1);
        if (StringHelper.isNotEmpty (sRuleCSS))
        {
          if (bFirst)
            bFirst = false;
          else
            if (!bOptimizedOutput)
              aSB.append (aSettings.getNewLineString ());

          if (!bOptimizedOutput)
            aSB.append (aSettings.getIndent (nIndentLevel + 1));
          aSB.append (sRuleCSS);
        }
      }
      if (!bOptimizedOutput)
        aSB.append(aSettings.getNewLineString()).append (aSettings.getIndent (nIndentLevel));
      aSB.append ('}');
    }

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
    return true;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("SourceLocation", m_aSourceLocation).getToString ();
  }
}
