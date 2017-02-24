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
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Part of a CSS calc element
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSExpressionMemberMathUnitProduct implements ICSSExpressionMathMember, ICSSSourceLocationAware
{
  private final CSSExpressionMemberMathProduct m_aProduct;

  public CSSExpressionMemberMathUnitProduct (@Nonnull @Nonempty final CSSExpressionMemberMathProduct aProduct)
  {
    m_aProduct = ValueEnforcer.notNull (aProduct, "Product");
  }

  @Nonnull
  public CSSExpressionMemberMathProduct getProduct ()
  {
    return m_aProduct;
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    aSettings.checkVersionRequirements (this);
    return "(" + m_aProduct.getAsCSSString (aSettings, nIndentLevel) + ")";
  }

  @Nonnull
  public ECSSVersion getMinimumCSSVersion ()
  {
    return ECSSVersion.CSS30;
  }

  public void setSourceLocation (@Nullable final CSSSourceLocation aSourceLocation)
  {
    m_aProduct.setSourceLocation (aSourceLocation);
  }

  @Nullable
  public CSSSourceLocation getSourceLocation ()
  {
    return m_aProduct.getSourceLocation ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final CSSExpressionMemberMathUnitProduct rhs = (CSSExpressionMemberMathUnitProduct) o;
    return m_aProduct.equals (rhs.m_aProduct);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aProduct).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("product", m_aProduct).getToString ();
  }
}
