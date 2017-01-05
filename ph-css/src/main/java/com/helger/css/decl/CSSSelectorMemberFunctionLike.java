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
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a single CSS complex selector pseudo element. Like
 * <code>:lang(fr)</code>
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSSelectorMemberFunctionLike implements ICSSSelectorMember, ICSSSourceLocationAware
{
  private final String m_sFuncName;
  private final CSSExpression m_aParamExpr;
  private CSSSourceLocation m_aSourceLocation;

  public CSSSelectorMemberFunctionLike (@Nonnull @Nonempty final String sFuncName,
                                        @Nonnull final CSSExpression aParamExpr)
  {
    ValueEnforcer.notEmpty (sFuncName, "FunctionName");
    if (!sFuncName.endsWith ("("))
      throw new IllegalArgumentException ("function name must end with a '('");
    ValueEnforcer.notNull (aParamExpr, "ParameterExpression");

    m_sFuncName = sFuncName;
    m_aParamExpr = aParamExpr;
  }

  /**
   * @return The name of the function with a trailing "("
   */
  @Nonnull
  @Nonempty
  public String getFunctionName ()
  {
    return m_sFuncName;
  }

  @Nonnull
  public CSSExpression getParameterExpression ()
  {
    return m_aParamExpr;
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    return m_sFuncName + m_aParamExpr.getAsCSSString (aSettings, nIndentLevel) + ')';
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
    final CSSSelectorMemberFunctionLike rhs = (CSSSelectorMemberFunctionLike) o;
    return m_sFuncName.equals (rhs.m_sFuncName) && m_aParamExpr.equals (rhs.m_aParamExpr);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sFuncName).append (m_aParamExpr).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("functionName", m_sFuncName)
                                       .append ("paramExpr", m_aParamExpr)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .toString ();
  }
}
