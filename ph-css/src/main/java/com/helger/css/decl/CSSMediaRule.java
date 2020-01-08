/**
 * Copyright (C) 2014-2020 Philip Helger (www.helger.com)
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
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.state.EChange;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a single <code>@media</code> rule: a list of style rules only
 * valid for certain media.<br>
 * Example:<br>
 * <code>@media print {
  div#footer {
    display: none;
  }
}</code>
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSMediaRule extends AbstractHasTopLevelRules implements ICSSTopLevelRule, ICSSSourceLocationAware
{
  private final ICommonsList <CSSMediaQuery> m_aMediaQueries = new CommonsArrayList <> ();
  private CSSSourceLocation m_aSourceLocation;

  public CSSMediaRule ()
  {}

  /**
   * @return <code>true</code> if at least one media query is present,
   *         <code>false</code> if no media query is present.
   */
  public boolean hasMediaQueries ()
  {
    return m_aMediaQueries.isNotEmpty ();
  }

  /**
   * @return The number of contained media queries. Always &ge; 0.
   */
  @Nonnegative
  public int getMediaQueryCount ()
  {
    return m_aMediaQueries.size ();
  }

  /**
   * Add a new media query.
   *
   * @param aMediaQuery
   *        The media query to be added. May not be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public CSSMediaRule addMediaQuery (@Nonnull @Nonempty final CSSMediaQuery aMediaQuery)
  {
    ValueEnforcer.notNull (aMediaQuery, "MediaQuery");

    m_aMediaQueries.add (aMediaQuery);
    return this;
  }

  /**
   * Add a media query at the specified index.
   *
   * @param nIndex
   *        The index to use. Must be &ge; 0. If the index is &ge;
   *        {@link #getMediaQueryCount()} than the media query is appended like
   *        in {@link #addMediaQuery(CSSMediaQuery)}.
   * @param aMediaQuery
   *        The media query to be added. May not be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public CSSMediaRule addMediaQuery (@Nonnegative final int nIndex, @Nonnull @Nonempty final CSSMediaQuery aMediaQuery)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aMediaQuery, "MediaQuery");

    if (nIndex >= getMediaQueryCount ())
      m_aMediaQueries.add (aMediaQuery);
    else
      m_aMediaQueries.add (nIndex, aMediaQuery);
    return this;
  }

  /**
   * Remove the provided media query.
   *
   * @param aMediaQuery
   *        The media query to be removed. May be <code>null</code>.
   * @return {@link EChange}.
   */
  @Nonnull
  public EChange removeMediaQuery (@Nullable final CSSMediaQuery aMediaQuery)
  {
    return m_aMediaQueries.removeObject (aMediaQuery);
  }

  /**
   * Remove the media query at the provided index.
   *
   * @param nMediumIndex
   *        The index to be removed. Should be &ge; 0.
   * @return {@link EChange}.
   */
  @Nonnull
  public EChange removeMediaQuery (@Nonnegative final int nMediumIndex)
  {
    return m_aMediaQueries.removeAtIndex (nMediumIndex);
  }

  /**
   * Remove all media queries.
   *
   * @return {@link EChange#CHANGED} if any media query was removed,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   * @since 3.7.3
   */
  @Nonnull
  public EChange removeAllMediaQueries ()
  {
    return m_aMediaQueries.removeAll ();
  }

  /**
   * Get the media query at the specified index or <code>null</code>.
   *
   * @param nMediumIndex
   *        The index to be retrieved. Should be &ge; 0.
   * @return <code>null</code> if an invalid index was provided.
   */
  @Nullable
  public CSSMediaQuery getMediaQueryAtIndex (@Nonnegative final int nMediumIndex)
  {
    return m_aMediaQueries.getAtIndex (nMediumIndex);
  }

  /**
   * @return A copy of all contained media queries. Never <code>null</code>.
   *         Maybe empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <CSSMediaQuery> getAllMediaQueries ()
  {
    return m_aMediaQueries.getClone ();
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    // Always ignore media rules?
    if (!aSettings.isWriteMediaRules ())
      return "";

    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();
    final int nRuleCount = m_aRules.size ();

    if (aSettings.isRemoveUnnecessaryCode () && nRuleCount == 0)
      return "";

    final StringBuilder aSB = new StringBuilder ("@media ");
    boolean bFirst = true;
    for (final CSSMediaQuery sMedium : m_aMediaQueries)
    {
      if (bFirst)
        bFirst = false;
      else
        aSB.append (bOptimizedOutput ? "," : ", ");
      aSB.append (sMedium.getAsCSSString (aSettings, nIndentLevel));
    }

    if (nRuleCount == 0)
    {
      aSB.append (bOptimizedOutput ? "{}" : " {}" + aSettings.getNewLineString ());
    }
    else
    {
      // At least one rule present
      aSB.append (bOptimizedOutput ? "{" : " {" + aSettings.getNewLineString ());
      bFirst = true;
      for (final ICSSTopLevelRule aRule : m_aRules)
      {
        final String sRuleCSS = aRule.getAsCSSString (aSettings, nIndentLevel + 1);
        if (StringHelper.hasText (sRuleCSS))
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
        aSB.append (aSettings.getIndent (nIndentLevel));
      aSB.append ('}');
      if (!bOptimizedOutput)
        aSB.append (aSettings.getNewLineString ());
    }
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
    final CSSMediaRule rhs = (CSSMediaRule) o;
    return m_aMediaQueries.equals (rhs.m_aMediaQueries) && m_aRules.equals (rhs.m_aRules);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aMediaQueries).append (m_aRules).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("mediaQueries", m_aMediaQueries)
                                       .append ("styleRules", m_aRules)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
