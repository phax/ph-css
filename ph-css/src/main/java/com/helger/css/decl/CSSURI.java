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
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriteable;
import com.helger.css.ICSSWriterSettings;
import com.helger.css.utils.CSSDataURL;
import com.helger.css.utils.CSSDataURLHelper;
import com.helger.css.utils.CSSURLHelper;

/**
 * Represents a single CSS URI. The contained URI might be modified using
 * {@link #setURI(String)}.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSURI implements ICSSWriteable, ICSSSourceLocationAware
{
  private String m_sURI;
  private CSSSourceLocation m_aSourceLocation;

  public CSSURI (@Nonnull final String sURI)
  {
    setURI (sURI);
  }

  /**
   * @return The URI string (without the leading "url(" and the closing ")")
   */
  @Nonnull
  public String getURI ()
  {
    return m_sURI;
  }

  /**
   * Set the URI string of this object. This may either be a regular URI or a
   * data URL string (starting with "data:"). The passed string may not start
   * with the prefix "url(" and end with ")".
   *
   * @param sURI
   *        The URI to be set. May not be <code>null</code> but may be empty
   *        (even though an empty URL usually does not make sense).
   * @return this
   */
  @Nonnull
  public CSSURI setURI (@Nonnull final String sURI)
  {
    ValueEnforcer.notNull (sURI, "URI");
    if (CSSURLHelper.isURLValue (sURI))
      throw new IllegalArgumentException ("Only the URI and not the CSS-URI value must be passed!");

    m_sURI = sURI;
    return this;
  }

  /**
   * Check if this URI is a data URL (starting with "data:")
   *
   * @return <code>true</code> if the URI is a data URL, <code>false</code>
   *         otherwise.
   */
  public boolean isDataURL ()
  {
    return CSSDataURLHelper.isDataURL (m_sURI);
  }

  /**
   * Try to convert the contained URI to a Data URL object.
   *
   * @return <code>null</code> if conversion to a data URL failed, the
   *         {@link CSSDataURL} object otherwise.
   */
  @Nullable
  public CSSDataURL getAsDataURL ()
  {
    return CSSDataURLHelper.parseDataURL (m_sURI);
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    return CSSURLHelper.getAsCSSURL (m_sURI, aSettings.isQuoteURLs ());
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
    final CSSURI rhs = (CSSURI) o;
    return m_sURI.equals (rhs.m_sURI);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sURI).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("URI", m_sURI)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
