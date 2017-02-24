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
package com.helger.css;

import java.io.Serializable;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.CGlobal;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;

/**
 * Defines the source location of an object when reading CSS from a stream. It
 * consists of the position of the start token and the position of the end
 * token.
 *
 * @author Philip Helger
 */
@Immutable
public class CSSSourceLocation implements Serializable
{
  private final CSSSourceArea m_aFirstTokenArea;
  private final CSSSourceArea m_aLastTokenArea;

  /**
   * Constructor.
   *
   * @param aFirstTokenArea
   *        Area of the first token. May be <code>null</code> if the last token
   *        area is not <code>null</code>.
   * @param aLastTokenArea
   *        Area of the last token. May be <code>null</code> if the first token
   *        area is not <code>null</code>.
   * @throws IllegalArgumentException
   *         if both areas are <code>null</code>.
   */
  public CSSSourceLocation (@Nullable final CSSSourceArea aFirstTokenArea, @Nullable final CSSSourceArea aLastTokenArea)
  {
    if (aFirstTokenArea == null && aLastTokenArea == null)
      throw new IllegalArgumentException ("At least one of the areas must be set!");
    m_aFirstTokenArea = aFirstTokenArea;
    m_aLastTokenArea = aLastTokenArea;
  }

  /**
   * @return The area of the first token. May be <code>null</code> if no such
   *         information is available.
   */
  @Nullable
  public CSSSourceArea getFirstTokenArea ()
  {
    return m_aFirstTokenArea;
  }

  /**
   * @return <code>true</code> if the first token area is present
   */
  public boolean hasFirstTokenArea ()
  {
    return m_aFirstTokenArea != null;
  }

  /**
   * @return The line number where the first token begins (incl.). May be -1 if
   *         not such token is available.
   */
  @CheckForSigned
  public int getFirstTokenBeginLineNumber ()
  {
    return m_aFirstTokenArea == null ? CGlobal.ILLEGAL_UINT : m_aFirstTokenArea.getTokenBeginLineNumber ();
  }

  /**
   * @return The column number where the first token begins (incl.). May be -1
   *         if not such token is available.
   */
  @CheckForSigned
  public int getFirstTokenBeginColumnNumber ()
  {
    return m_aFirstTokenArea == null ? CGlobal.ILLEGAL_UINT : m_aFirstTokenArea.getTokenBeginColumnNumber ();
  }

  /**
   * @return The line number where the fist token ends (incl.). May be -1 if not
   *         such token is available.
   */
  @CheckForSigned
  public int getFirstTokenEndLineNumber ()
  {
    return m_aFirstTokenArea == null ? CGlobal.ILLEGAL_UINT : m_aFirstTokenArea.getTokenEndLineNumber ();
  }

  /**
   * @return The column number where the first token ends (incl.). May be -1 if
   *         not such token is available.
   */
  @CheckForSigned
  public int getFirstTokenEndColumnNumber ()
  {
    return m_aFirstTokenArea == null ? CGlobal.ILLEGAL_UINT : m_aFirstTokenArea.getTokenEndColumnNumber ();
  }

  /**
   * @return The area of the last token. May be <code>null</code> if no such
   *         information is available.
   */
  @Nullable
  public CSSSourceArea getLastTokenArea ()
  {
    return m_aLastTokenArea;
  }

  /**
   * @return <code>true</code> if the last token area is present
   */
  public boolean hasLastTokenArea ()
  {
    return m_aLastTokenArea != null;
  }

  /**
   * @return The line number where the last token begins (incl.). May be -1 if
   *         not such token is available.
   */
  @CheckForSigned
  public int getLastTokenBeginLineNumber ()
  {
    return m_aLastTokenArea == null ? CGlobal.ILLEGAL_UINT : m_aLastTokenArea.getTokenBeginLineNumber ();
  }

  /**
   * @return The column number where the last token begins (incl.). May be -1 if
   *         not such token is available.
   */
  @CheckForSigned
  public int getLastTokenBeginColumnNumber ()
  {
    return m_aLastTokenArea == null ? CGlobal.ILLEGAL_UINT : m_aLastTokenArea.getTokenBeginColumnNumber ();
  }

  /**
   * @return The line number where the fist token ends (incl.). May be -1 if not
   *         such token is available.
   */
  @CheckForSigned
  public int getLastTokenEndLineNumber ()
  {
    return m_aLastTokenArea == null ? CGlobal.ILLEGAL_UINT : m_aLastTokenArea.getTokenEndLineNumber ();
  }

  /**
   * @return The column number where the last token ends (incl.). May be -1 if
   *         not such token is available.
   */
  @CheckForSigned
  public int getLastTokenEndColumnNumber ()
  {
    return m_aLastTokenArea == null ? CGlobal.ILLEGAL_UINT : m_aLastTokenArea.getTokenEndColumnNumber ();
  }

  /**
   * @return The location of the first token as a simple string. May be
   *         <code>null</code>.
   */
  @Nullable
  public String getFirstTokenLocationAsString ()
  {
    return m_aFirstTokenArea == null ? null : m_aFirstTokenArea.getTokenLocationAsString ();
  }

  /**
   * @return The location of the last token as a simple string. May be
   *         <code>null</code>.
   */
  @Nullable
  public String getLastTokenLocationAsString ()
  {
    return m_aLastTokenArea == null ? null : m_aLastTokenArea.getTokenLocationAsString ();
  }

  @Nonnull
  @Nonempty
  public String getLocationAsString ()
  {
    final String sFirst = getFirstTokenLocationAsString ();
    final String sLast = getLastTokenLocationAsString ();
    return StringHelper.getNotNull (sFirst, "") + "-" + StringHelper.getNotNull (sLast, "");
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final CSSSourceLocation rhs = (CSSSourceLocation) o;
    return EqualsHelper.equals (m_aFirstTokenArea, rhs.m_aFirstTokenArea) &&
           EqualsHelper.equals (m_aLastTokenArea, rhs.m_aLastTokenArea);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aFirstTokenArea).append (m_aLastTokenArea).getHashCode ();
  }

  @Override
  public String toString ()
  {
    // For debug comparison, return empty
    if (false)
      return "";
    return new ToStringGenerator (null).append ("firstTokenArea", m_aFirstTokenArea)
                                       .append ("lastTokenArea", m_aLastTokenArea)
                                       .getToString ();
  }
}
