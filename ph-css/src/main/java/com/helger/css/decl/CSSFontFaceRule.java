/*
 * Copyright (C) 2014-2024 Philip Helger (www.helger.com)
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
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.ICommonsList;
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
 * Represents a single <code>@font-face</code> rule.<br>
 * Example:<br>
 * <code>@font-face {
    font-family: 'icons';
    src: url(path/to/font.woff) format('woff');
    unicode-range: U+E000-E005;
}</code>
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSFontFaceRule implements ICSSTopLevelRule, IHasCSSDeclarations <CSSFontFaceRule>, ICSSVersionAware, ICSSSourceLocationAware
{
  private final String m_sDeclaration;
  private final CSSDeclarationContainer m_aDeclarations = new CSSDeclarationContainer ();
  private CSSSourceLocation m_aSourceLocation;

  public static boolean isValidDeclaration (@Nonnull @Nonempty final String sDeclaration)
  {
    return StringHelper.startsWith (sDeclaration, '@') && StringHelper.endsWithIgnoreCase (sDeclaration, "font-face");
  }

  public CSSFontFaceRule ()
  {
    this ("@font-face");
  }

  public CSSFontFaceRule (@Nonnull @Nonempty final String sDeclaration)
  {
    ValueEnforcer.isTrue (isValidDeclaration (sDeclaration), () -> "Declaration '" + sDeclaration + "' is invalid");
    m_sDeclaration = sDeclaration;
  }

  /**
   * @return The rule declaration string used in the CSS. Neither
   *         <code>null</code> nor empty. Always starting with <code>@</code>
   *         and ending with <code>font-face</code>.
   */
  @Nonnull
  @Nonempty
  public final String getDeclaration ()
  {
    return m_sDeclaration;
  }

  @Nonnull
  public CSSFontFaceRule addDeclaration (@Nonnull final CSSDeclaration aDeclaration)
  {
    m_aDeclarations.addDeclaration (aDeclaration);
    return this;
  }

  @Nonnull
  public CSSFontFaceRule addDeclaration (@Nonnegative final int nIndex, @Nonnull final CSSDeclaration aNewDeclaration)
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
  public ICommonsList <CSSDeclaration> getAllDeclarations ()
  {
    return m_aDeclarations.getAllDeclarations ();
  }

  @Nullable
  public CSSDeclaration getDeclarationAtIndex (@Nonnegative final int nIndex)
  {
    return m_aDeclarations.getDeclarationAtIndex (nIndex);
  }

  @Nonnull
  public CSSFontFaceRule setDeclarationAtIndex (@Nonnegative final int nIndex, @Nonnull final CSSDeclaration aNewDeclaration)
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

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <CSSDeclaration> getAllDeclarationsOfPropertyName (@Nullable final String sPropertyName)
  {
    return m_aDeclarations.getAllDeclarationsOfPropertyName (sPropertyName);
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    aSettings.checkVersionRequirements (this);

    // Always ignore font-face rules?
    if (!aSettings.isWriteFontFaceRules ())
      return "";

    if (aSettings.isRemoveUnnecessaryCode () && !hasDeclarations ())
      return "";

    final StringBuilder aSB = new StringBuilder (m_sDeclaration);
    aSB.append (m_aDeclarations.getAsCSSString (aSettings, nIndentLevel));
    if (!aSettings.isOptimizedOutput ())
      aSB.append (aSettings.getNewLineString ());
    return aSB.toString ();
  }

  @Nonnull
  public ECSSVersion getMinimumCSSVersion ()
  {
    return ECSSVersion.CSS30;
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
    final CSSFontFaceRule rhs = (CSSFontFaceRule) o;
    return m_sDeclaration.equals (rhs.m_sDeclaration) && m_aDeclarations.equals (rhs.m_aDeclarations);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sDeclaration).append (m_aDeclarations).append (m_aDeclarations).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Declaration", m_sDeclaration)
                                       .append ("Declarations", m_aDeclarations)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
