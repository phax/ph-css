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
import com.helger.css.ICSSWriteable;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a single import rule on top level. It consists of a mandatory URI
 * and an optional list of media queries.<br>
 * Example:<br>
 * <code>@import url("style.css") screen;</code>
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSImportRule implements ICSSWriteable, ICSSSourceLocationAware
{
  private CSSURI m_aLocation;
  private final ICommonsList <CSSMediaQuery> m_aMediaQueries = new CommonsArrayList <> ();
  private CSSSourceLocation m_aSourceLocation;

  public CSSImportRule (@NonNull final String sLocation)
  {
    this (new CSSURI (sLocation));
  }

  public CSSImportRule (@NonNull final CSSURI aLocation)
  {
    setLocation (aLocation);
  }

  /**
   * @return <code>true</code> if this import rule has any specific media
   *         queries, to which it belongs, <code>false</code> if not.
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
   * Add a media query at the end of the list.
   *
   * @param aMediaQuery
   *        The media query to be added. May not be <code>null</code>.
   * @return this
   */
  @NonNull
  public CSSImportRule addMediaQuery (@NonNull final CSSMediaQuery aMediaQuery)
  {
    ValueEnforcer.notNull (aMediaQuery, "MediaQuery");

    m_aMediaQueries.add (aMediaQuery);
    return this;
  }

  /**
   * Add a media query at the specified index of the list.
   *
   * @param nIndex
   *        The index where to add the media query. Must be &ge; 0.
   * @param aMediaQuery
   *        The media query to be added. May not be <code>null</code>.
   * @return this
   */
  @NonNull
  public CSSImportRule addMediaQuery (@Nonnegative final int nIndex, @NonNull final CSSMediaQuery aMediaQuery)
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
   * Remove the specified media query.
   *
   * @param aMediaQuery
   *        The media query to be removed. May be <code>null</code>.
   * @return {@link EChange#CHANGED} if removal was successful.
   */
  @NonNull
  public EChange removeMediaQuery (@Nullable final CSSMediaQuery aMediaQuery)
  {
    return m_aMediaQueries.removeObject (aMediaQuery);
  }

  /**
   * Remove the media query at the specified index.
   *
   * @param nMediumIndex
   *        The index of the media query to be removed.
   * @return {@link EChange#CHANGED} if removal was successful.
   */
  @NonNull
  public EChange removeMediaQuery (final int nMediumIndex)
  {
    return m_aMediaQueries.removeAtIndex (nMediumIndex);
  }

  /**
   * Remove all contained media queries.
   *
   * @return {@link EChange#CHANGED} if at least one media query was contained.
   */
  @NonNull
  public EChange removeAllMediaQueries ()
  {
    return m_aMediaQueries.removeAll ();
  }

  /**
   * @return A list with all contained media queries. Never <code>null</code>
   *         and always a copy of the underlying list.
   */
  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <CSSMediaQuery> getAllMediaQueries ()
  {
    return m_aMediaQueries.getClone ();
  }

  /**
   * @return The URL object of the CSS file to import. Never <code>null</code>.
   */
  @NonNull
  public final CSSURI getLocation ()
  {
    return m_aLocation;
  }

  /**
   * @return The URL of the CSS file to import. Never <code>null</code>. This is
   *         a shortcut for <code>getLocation().getURI()</code>
   */
  @NonNull
  @Nonempty
  public final String getLocationString ()
  {
    return m_aLocation.getURI ();
  }

  /**
   * Set the URI of the file to be imported.
   *
   * @param aLocation
   *        The location to use. May not be <code>null</code>.
   * @return this;
   */
  @NonNull
  public final CSSImportRule setLocation (@NonNull final CSSURI aLocation)
  {
    ValueEnforcer.notNull (aLocation, "Location");

    m_aLocation = aLocation;
    return this;
  }

  /**
   * Set the URI of the file to be imported.
   *
   * @param sLocationURI
   *        The location URI to use. May not be <code>null</code>.
   * @return this;
   */
  @NonNull
  public final CSSImportRule setLocationString (@NonNull final String sLocationURI)
  {
    m_aLocation.setURI (sLocationURI);
    return this;
  }

  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();

    final StringBuilder aSB = new StringBuilder ();
    aSB.append ("@import ").append (m_aLocation.getAsCSSString (aSettings, nIndentLevel));
    if (!m_aMediaQueries.isEmpty ())
    {
      aSB.append (' ');
      boolean bFirst = true;
      for (final CSSMediaQuery aMediaQuery : m_aMediaQueries)
      {
        if (bFirst)
          bFirst = false;
        else
          aSB.append (bOptimizedOutput ? "," : ", ");
        aSB.append (aMediaQuery.getAsCSSString (aSettings, nIndentLevel));
      }
    }
    return aSB.append (';').append (aSettings.getNewLineString ()).toString ();
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
    final CSSImportRule rhs = (CSSImportRule) o;
    return m_aLocation.equals (rhs.m_aLocation) && m_aMediaQueries.equals (rhs.m_aMediaQueries);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aLocation).append (m_aMediaQueries).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("location", m_aLocation)
                                       .append ("mediaQueries", m_aMediaQueries)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
