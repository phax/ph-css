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
import com.helger.base.equals.EqualsHelper;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.css.CCSS;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriteable;
import com.helger.css.ICSSWriterSettings;
import com.helger.css.media.ECSSMediaExpressionFeature;

/**
 * Represents a single media expression
 */
@NotThreadSafe
public class CSSMediaExpression implements ICSSWriteable, ICSSSourceLocationAware
{
  private final String m_sFeature;
  private final CSSExpression m_aValue;
  private CSSSourceLocation m_aSourceLocation;

  public CSSMediaExpression (@NonNull final ECSSMediaExpressionFeature eFeature)
  {
    this (eFeature.getName ());
  }

  public CSSMediaExpression (@NonNull @Nonempty final String sFeature)
  {
    this (sFeature, null);
  }

  public CSSMediaExpression (@NonNull final ECSSMediaExpressionFeature eFeature, @Nullable final CSSExpression aValue)
  {
    this (eFeature.getName (), aValue);
  }

  public CSSMediaExpression (@NonNull @Nonempty final String sFeature, @Nullable final CSSExpression aValue)
  {
    ValueEnforcer.notEmpty (sFeature, "Feature");
    m_sFeature = sFeature;
    m_aValue = aValue;
  }

  @NonNull
  @Nonempty
  public final String getFeature ()
  {
    return m_sFeature;
  }

  @Nullable
  public final CSSExpression getValue ()
  {
    return m_aValue;
  }

  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    final StringBuilder aSB = new StringBuilder ();
    aSB.append ('(').append (m_sFeature);
    if (m_aValue != null)
      aSB.append (CCSS.SEPARATOR_PROPERTY_VALUE).append (m_aValue.getAsCSSString (aSettings, nIndentLevel));
    return aSB.append (')').toString ();
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
    final CSSMediaExpression rhs = (CSSMediaExpression) o;
    return m_sFeature.equals (rhs.m_sFeature) && EqualsHelper.equals (m_aValue, rhs.m_aValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sFeature).append (m_aValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("feature", m_sFeature)
                                       .appendIfNotNull ("value", m_aValue)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
