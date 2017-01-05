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
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a CSS function element
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSExpressionMemberFunction implements
                                         ICSSExpressionMember,
                                         ICSSSourceLocationAware,
                                         ICSSExpressionMathMember
{
  private final String m_sFunctionName;
  private final CSSExpression m_aExpression;
  private CSSSourceLocation m_aSourceLocation;

  @Nonnull
  private static String _skipBracketsAtEnd (@Nonnull final String sName)
  {
    final String sRealName = sName.trim ();
    if (sRealName.length () > 2 && sRealName.endsWith ("()"))
      return sRealName.substring (0, sRealName.length () - 2).trim ();
    return sRealName;
  }

  /**
   * Constructor without an expression
   *
   * @param sFunctionName
   *        Function name. May neither be <code>null</code> nor empty.
   */
  public CSSExpressionMemberFunction (@Nonnull @Nonempty final String sFunctionName)
  {
    this (sFunctionName, null);
  }

  /**
   * Constructor
   *
   * @param sFunctionName
   *        Function name. May neither be <code>null</code> nor empty.
   * @param aExpression
   *        Optional parameter expression. May be <code>null</code>.
   */
  public CSSExpressionMemberFunction (@Nonnull @Nonempty final String sFunctionName,
                                      @Nullable final CSSExpression aExpression)
  {
    ValueEnforcer.notEmpty (sFunctionName, "FunctionName");
    // expression may be null

    m_sFunctionName = _skipBracketsAtEnd (sFunctionName);
    m_aExpression = aExpression;
  }

  /**
   * @return The passed function name. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getFunctionName ()
  {
    return m_sFunctionName;
  }

  /**
   * @return <code>true</code> if this is a special IE "expression" function.
   *         This makes a difference, because in case of IE expression
   *         functions, no parameter splitting takes place!
   */
  public boolean isExpressionFunction ()
  {
    return m_sFunctionName.startsWith ("expression(") || m_sFunctionName.equals ("expression");
  }

  /**
   * @return The optional expression parameter. May be <code>null</code>.
   */
  @Nullable
  public CSSExpression getExpression ()
  {
    return m_aExpression;
  }

  /**
   * @return <code>true</code> of an expression parameter is present,
   *         <code>false</code> otherwise.
   * @since 5.0.0
   */
  public boolean hasExpression ()
  {
    return m_aExpression != null;
  }

  @Nonnull
  public CSSExpressionMemberFunction getClone ()
  {
    return new CSSExpressionMemberFunction (m_sFunctionName, m_aExpression);
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    if (m_aExpression == null)
    {
      // No parameter expressions
      if (m_sFunctionName.endsWith (")"))
      {
        // E.g. for special IE expression functions!
        return m_sFunctionName;
      }
      return m_sFunctionName + "()";
    }
    return m_sFunctionName + "(" + m_aExpression.getAsCSSString (aSettings, nIndentLevel) + ")";
  }

  @Nonnull
  public ECSSVersion getMinimumCSSVersion ()
  {
    return ECSSVersion.CSS30;
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
    final CSSExpressionMemberFunction rhs = (CSSExpressionMemberFunction) o;
    return m_sFunctionName.equals (rhs.m_sFunctionName) && EqualsHelper.equals (m_aExpression, rhs.m_aExpression);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sFunctionName).append (m_aExpression).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("funcName", m_sFunctionName)
                                       .appendIfNotNull ("expression", m_aExpression)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .toString ();
  }
}
