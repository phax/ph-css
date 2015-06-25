/**
 * Copyright (C) 2014-2015 Philip Helger (www.helger.com)
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

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.state.EChange;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSVersionAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a single <code>@page</code> rule.<br>
 * Example:<br>
 * <code>@page {
  size: auto;
  margin: 10%;
}</code>
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSPageRule implements ICSSTopLevelRule, IHasCSSDeclarations, ICSSVersionAware, ICSSSourceLocationAware
{
  private final String m_sPseudoPage;
  private final CSSDeclarationContainer m_aDeclarations = new CSSDeclarationContainer ();
  private CSSSourceLocation m_aSourceLocation;

  public CSSPageRule (@Nullable final String sPseudoPage)
  {
    m_sPseudoPage = sPseudoPage;
  }

  @Nullable
  public String getPseudoPage ()
  {
    return m_sPseudoPage;
  }

  @Nonnull
  public CSSPageRule addDeclaration (@Nonnull final CSSDeclaration aDeclaration)
  {
    m_aDeclarations.addDeclaration (aDeclaration);
    return this;
  }

  @Nonnull
  public CSSPageRule addDeclaration (@Nonnull @Nonempty final String sProperty,
                                     @Nonnull final CSSExpression aExpression,
                                     final boolean bImportant)
  {
    m_aDeclarations.addDeclaration (sProperty, aExpression, bImportant);
    return this;
  }

  @Nonnull
  public CSSPageRule addDeclaration (@Nonnegative final int nIndex, @Nonnull final CSSDeclaration aNewDeclaration)
  {
    m_aDeclarations.addDeclaration (nIndex, aNewDeclaration);
    return this;
  }

  @Nonnull
  public EChange removeDeclaration (@Nonnull final CSSDeclaration aDeclaration)
  {
    return m_aDeclarations.removeDeclaration (aDeclaration);
  }

  @Nonnull
  public EChange removeDeclaration (@Nonnegative final int nDeclarationIndex)
  {
    return m_aDeclarations.removeDeclaration (nDeclarationIndex);
  }

  @Nonnull
  public EChange removeAllDeclarations ()
  {
    return m_aDeclarations.removeAllDeclarations ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <CSSDeclaration> getAllDeclarations ()
  {
    return m_aDeclarations.getAllDeclarations ();
  }

  @Nullable
  public CSSDeclaration getDeclarationAtIndex (@Nonnegative final int nIndex)
  {
    return m_aDeclarations.getDeclarationAtIndex (nIndex);
  }

  @Nonnull
  public CSSPageRule setDeclarationAtIndex (@Nonnegative final int nIndex, @Nonnull final CSSDeclaration aNewDeclaration)
  {
    m_aDeclarations.setDeclarationAtIndex (nIndex, aNewDeclaration);
    return this;
  }

  public boolean hasDeclarations ()
  {
    return m_aDeclarations.hasDeclarations ();
  }

  @Nonnegative
  public int getDeclarationCount ()
  {
    return m_aDeclarations.getDeclarationCount ();
  }

  @Nullable
  public CSSDeclaration getDeclarationOfPropertyName (@Nullable final String sPropertyName)
  {
    return m_aDeclarations.getDeclarationOfPropertyName (sPropertyName);
  }

  @Nullable
  public CSSDeclaration getDeclarationOfPropertyNameCaseInsensitive (@Nullable final String sPropertyName)
  {
    return m_aDeclarations.getDeclarationOfPropertyNameCaseInsensitive (sPropertyName);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <CSSDeclaration> getAllDeclarationsOfPropertyName (@Nullable final String sPropertyName)
  {
    return m_aDeclarations.getAllDeclarationsOfPropertyName (sPropertyName);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <CSSDeclaration> getAllDeclarationsOfPropertyNameCaseInsensitive (@Nullable final String sPropertyName)
  {
    return m_aDeclarations.getAllDeclarationsOfPropertyNameCaseInsensitive (sPropertyName);
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    aSettings.checkVersionRequirements (this);

    // Always ignore page rules?
    if (!aSettings.isWritePageRules ())
      return "";

    if (aSettings.isRemoveUnnecessaryCode () && !hasDeclarations ())
      return "";

    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();

    final StringBuilder aSB = new StringBuilder ("@page");

    if (StringHelper.hasText (m_sPseudoPage))
      aSB.append (' ').append (m_sPseudoPage);

    aSB.append (m_aDeclarations.getAsCSSString (aSettings, nIndentLevel));
    if (!bOptimizedOutput)
      aSB.append ('\n');

    return aSB.toString ();
  }

  @Nonnull
  public ECSSVersion getMinimumCSSVersion ()
  {
    return ECSSVersion.CSS21;
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
    final CSSPageRule rhs = (CSSPageRule) o;
    return m_aDeclarations.equals (rhs.m_aDeclarations);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aDeclarations).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("declarations", m_aDeclarations)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .toString ();
  }
}
