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

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a single <code>@</code> rule that is non-standard and/or unknown.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSUnknownRule implements ICSSTopLevelRule, ICSSSourceLocationAware
{
  private final String m_sDeclaration;
  private String m_sParameterList;
  private String m_sBody;
  private CSSSourceLocation m_aSourceLocation;

  public static boolean isValidDeclaration (@Nonnull @Nonempty final String sDeclaration)
  {
    return StringHelper.startsWith (sDeclaration, '@');
  }

  public CSSUnknownRule (@Nonnull @Nonempty final String sDeclaration)
  {
    if (!isValidDeclaration (sDeclaration))
      throw new IllegalArgumentException ("declaration");
    m_sDeclaration = sDeclaration;
  }

  /**
   * @return The rule declaration string used in the CSS. Neither
   *         <code>null</code> nor empty. Always starting with <code>@</code>.
   */
  @Nonnull
  @Nonempty
  public String getDeclaration ()
  {
    return m_sDeclaration;
  }

  @Nonnull
  public CSSUnknownRule setParameterList (@Nullable final String sParameterList)
  {
    m_sParameterList = StringHelper.trim (sParameterList);
    return this;
  }

  /**
   * @return The parameter-list of the unknown rule. This is the part between
   *         the declaration and the first opening bracket ('{')
   */
  @Nullable
  public String getParameterList ()
  {
    return m_sParameterList;
  }

  @Nonnull
  public CSSUnknownRule setBody (@Nullable final String sBody)
  {
    m_sBody = StringHelper.trim (sBody);
    return this;
  }

  /**
   * @return The body of the unknown rule. This is the part between the first
   *         opening bracket ('{') and the matching closing bracket ('}').
   */
  @Nullable
  public String getBody ()
  {
    return m_sBody;
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    // Always ignore unknown rules?
    if (!aSettings.isWriteUnknownRules ())
      return "";

    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();

    final StringBuilder aSB = new StringBuilder (m_sDeclaration);

    if (StringHelper.hasText (m_sParameterList))
      aSB.append (' ').append (m_sParameterList);

    if (StringHelper.hasNoText (m_sBody))
    {
      aSB.append (bOptimizedOutput ? "{}" : " {}" + aSettings.getNewLineString ());
    }
    else
    {
      // At least one rule present
      aSB.append (bOptimizedOutput ? "{" : " {" + aSettings.getNewLineString ());
      if (!bOptimizedOutput)
        aSB.append (aSettings.getIndent (nIndentLevel));
      aSB.append (m_sBody);
      if (!bOptimizedOutput)
        aSB.append (aSettings.getIndent (nIndentLevel));
      aSB.append ('}');
      if (!bOptimizedOutput)
        aSB.append (aSettings.getNewLineString ());
    }
    return aSB.toString ();
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
    final CSSUnknownRule rhs = (CSSUnknownRule) o;
    return m_sDeclaration.equals (rhs.m_sDeclaration) &&
           EqualsHelper.equals (m_sParameterList, rhs.m_sParameterList) &&
           EqualsHelper.equals (m_sBody, rhs.m_sBody);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sDeclaration)
                                       .append (m_sParameterList)
                                       .append (m_sBody)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("declaration", m_sDeclaration)
                                       .appendIfNotNull ("parameterList", m_sParameterList)
                                       .appendIfNotNull ("body", m_sBody)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
