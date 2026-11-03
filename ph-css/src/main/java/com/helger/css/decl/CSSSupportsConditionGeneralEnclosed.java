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
 * Represents a single supports condition that is not understood, e.g. an unknown function like
 * "foo(bar)" or parenthesised content that is not a declaration like "(foo bar)". This is the
 * <code>&lt;general-enclosed&gt;</code> production of the specification, that always evaluates to
 * false. The content is kept as written.
 *
 * @author Philip Helger
 * @since 8.3.1
 */
@NotThreadSafe
public class CSSSupportsConditionGeneralEnclosed implements ICSSSupportsConditionMember, ICSSSourceLocationAware
{
  private final String m_sText;
  private CSSSourceLocation m_aSourceLocation;

  /**
   * Constructor
   *
   * @param sText
   *        The complete text including the function name or the opening parenthesis and the
   *        closing parenthesis. E.g. <code>foo(bar)</code>. May neither be <code>null</code> nor
   *        empty.
   */
  public CSSSupportsConditionGeneralEnclosed (@NonNull @Nonempty final String sText)
  {
    m_sText = ValueEnforcer.notEmpty (sText, "Text");
  }

  /**
   * @return The complete text as written in the CSS. Neither <code>null</code> nor empty.
   */
  @NonNull
  @Nonempty
  public String getText ()
  {
    return m_sText;
  }

  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    return m_sText;
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
    final CSSSupportsConditionGeneralEnclosed rhs = (CSSSupportsConditionGeneralEnclosed) o;
    return m_sText.equals (rhs.m_sText);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sText).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("text", m_sText)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
