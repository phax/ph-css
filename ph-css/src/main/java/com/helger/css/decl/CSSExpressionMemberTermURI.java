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
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents an expression member URI
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSExpressionMemberTermURI implements ICSSExpressionMember, ICSSSourceLocationAware
{
  private CSSURI m_aURI;

  public CSSExpressionMemberTermURI (@NonNull final String sURIString)
  {
    this (new CSSURI (sURIString));
  }

  public CSSExpressionMemberTermURI (@NonNull final CSSURI aURI)
  {
    setURI (aURI);
  }

  /**
   * @return The contained {@link CSSURI} object. Never <code>null</code>.
   */
  @NonNull
  public final CSSURI getURI ()
  {
    return m_aURI;
  }

  /**
   * @return A sanity shortcut for <code>getURI().getURI()</code>
   */
  @NonNull
  @Nonempty
  public final String getURIString ()
  {
    return m_aURI.getURI ();
  }

  /**
   * Set a new URI
   *
   * @param aURI
   *        The new URI to set. May not be <code>null</code>.
   * @return this
   */
  @NonNull
  public final CSSExpressionMemberTermURI setURI (@NonNull final CSSURI aURI)
  {
    m_aURI = ValueEnforcer.notNull (aURI, "URI");
    return this;
  }

  /**
   * Replace the URI string in the existing {@link CSSURI} object.
   *
   * @param sURIString
   *        The new URI string to set. May not be <code>null</code> but may be
   *        empty.
   * @return this
   */
  @NonNull
  public final CSSExpressionMemberTermURI setURIString (@NonNull final String sURIString)
  {
    m_aURI.setURI (sURIString);
    return this;
  }

  @NonNull
  public CSSExpressionMemberTermURI getClone ()
  {
    return new CSSExpressionMemberTermURI (m_aURI);
  }

  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    return m_aURI.getAsCSSString (aSettings, nIndentLevel);
  }

  @Nullable
  public final CSSSourceLocation getSourceLocation ()
  {
    return m_aURI.getSourceLocation ();
  }

  public final void setSourceLocation (@Nullable final CSSSourceLocation aSourceLocation)
  {
    m_aURI.setSourceLocation (aSourceLocation);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final CSSExpressionMemberTermURI rhs = (CSSExpressionMemberTermURI) o;
    return m_aURI.equals (rhs.m_aURI);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aURI).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("URI", m_aURI).getToString ();
  }
}
