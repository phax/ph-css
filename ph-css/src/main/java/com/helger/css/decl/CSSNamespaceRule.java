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

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.annotation.Nonnegative;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.equals.EqualsHelper;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.string.StringHelper;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriteable;
import com.helger.css.ICSSWriterSettings;
import com.helger.css.utils.CSSURLHelper;

/**
 * Represents a single namespace rule on top level.<br>
 * Example:<br>
 * <code>@namespace Q "http://example.com/q-markup";</code>
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSNamespaceRule implements ICSSWriteable, ICSSSourceLocationAware
{
  private String m_sPrefix;
  private String m_sURL;
  private CSSSourceLocation m_aSourceLocation;

  /**
   * Constructor for the default namespace
   *
   * @param sURL
   *        The namespace URL to use. May not be <code>null</code>.
   */
  public CSSNamespaceRule (@NonNull final String sURL)
  {
    this (null, sURL);
  }

  /**
   * Constructor
   *
   * @param sNamespacePrefix
   *        The namespace prefix to use. May be <code>null</code> or empty for the default
   *        namespace.
   * @param sURL
   *        The namespace URL to use. May not be <code>null</code>.
   */
  public CSSNamespaceRule (@Nullable final String sNamespacePrefix, @NonNull final String sURL)
  {
    setNamespacePrefix (sNamespacePrefix);
    setNamespaceURL (sURL);
  }

  @Nullable
  public final String getNamespacePrefix ()
  {
    return m_sPrefix;
  }

  @NonNull
  public final CSSNamespaceRule setNamespacePrefix (@Nullable final String sNamespacePrefix)
  {
    m_sPrefix = sNamespacePrefix;
    return this;
  }

  /**
   * @return The namespace URL. May not be <code>null</code> but maybe empty!
   */
  @NonNull
  public final String getNamespaceURL ()
  {
    return m_sURL;
  }

  @NonNull
  public final CSSNamespaceRule setNamespaceURL (@NonNull final String sURL)
  {
    ValueEnforcer.notNull (sURL, "URL");

    m_sURL = sURL;
    return this;
  }

  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    // Always ignore namespace rules?
    if (!aSettings.isWriteNamespaceRules ())
      return "";

    final StringBuilder aSB = new StringBuilder ();
    aSB.append ("@namespace ");
    if (StringHelper.isNotEmpty (m_sPrefix))
      aSB.append (m_sPrefix).append (' ');
    if (StringHelper.isNotEmpty (m_sURL))
      aSB.append (CSSURLHelper.getAsCSSURL (m_sURL, false));
    else
      aSB.append ("\"\"");
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
    final CSSNamespaceRule rhs = (CSSNamespaceRule) o;
    return EqualsHelper.equals (m_sPrefix, rhs.m_sPrefix) && m_sURL.equals (rhs.m_sURL);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sPrefix).append (m_sURL).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("prefix", m_sPrefix)
                                       .append ("URL", m_sURL)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
