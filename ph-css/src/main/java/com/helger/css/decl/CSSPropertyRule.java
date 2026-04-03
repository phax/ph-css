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

import com.helger.annotation.Nonempty;
import com.helger.annotation.Nonnegative;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.state.EChange;
import com.helger.base.string.StringHelper;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.collection.commons.ICommonsList;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Represents a single <code>@viewport</code> rule.
 *
 * <p>Example:
 *
 * <pre>@property --rotation {
  syntax: '&lt;angle&gt;';
  inherits: false;
  initial-value: 45deg;
}</pre>
 *
 * @author Philip Helger
 * @since 8.2.0
 */
@NotThreadSafe
public class CSSPropertyRule implements ICSSTopLevelRule, IHasCSSDeclarations <CSSPropertyRule>, ICSSSourceLocationAware
{
  private final String m_sPropertyName;
  private final CSSDeclarationContainer m_aDeclarations = new CSSDeclarationContainer ();
  private CSSSourceLocation m_aSourceLocation;

  /**
   * Checks if the passed property name is a valid custom property name. A valid custom property name starts with
   * <code>--</code>.
   * @param sPropertyName The property name to check. May not be <code>null</code> or empty.
   * @return <code>true</code> if the passed property name is valid, <code>false</code> otherwise.
   */
  public static boolean isValidPropertyName (@NonNull @Nonempty final String sPropertyName)
  {
    return StringHelper.startsWith (sPropertyName, "--");
  }

  public CSSPropertyRule(@NonNull @Nonempty final String sPropertyName)
  {
    ValueEnforcer.isTrue (isValidPropertyName (sPropertyName), "Property name is invalid");
    m_sPropertyName = sPropertyName;
  }

  /**
   * @return The property name. Neither <code>null</code> nor empty. Always starts with <code>--</code>.
   */
  @NonNull
  @Nonempty
  public String getPropertyName ()
  {
    return m_sPropertyName;
  }

  @Override
  @NonNull
  public CSSPropertyRule addDeclaration (@NonNull final CSSDeclaration aDeclaration)
  {
    m_aDeclarations.addDeclaration (aDeclaration);
    return this;
  }

  @Override
  @NonNull
  public CSSPropertyRule addDeclaration (@Nonnegative final int nIndex, @NonNull final CSSDeclaration aNewDeclaration)
  {
    m_aDeclarations.addDeclaration (nIndex, aNewDeclaration);
    return this;
  }

  @Override
  @NonNull
  public EChange removeDeclaration (@NonNull final CSSDeclaration aDeclaration)
  {
    return m_aDeclarations.removeDeclaration (aDeclaration);
  }

  @Override
  @NonNull
  public EChange removeDeclaration (@Nonnegative final int nDeclarationIndex)
  {
    return m_aDeclarations.removeDeclaration (nDeclarationIndex);
  }

  @Override
  @NonNull
  public EChange removeAllDeclarations ()
  {
    return m_aDeclarations.removeAllDeclarations ();
  }

  @Override
  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <CSSDeclaration> getAllDeclarations ()
  {
    return m_aDeclarations.getAllDeclarations ();
  }

  @Override
  @Nullable
  public CSSDeclaration getDeclarationAtIndex (@Nonnegative final int nIndex)
  {
    return m_aDeclarations.getDeclarationAtIndex (nIndex);
  }

  @Override
  @NonNull
  public CSSPropertyRule setDeclarationAtIndex (@Nonnegative final int nIndex,
                                                @NonNull final CSSDeclaration aNewDeclaration)
  {
    m_aDeclarations.setDeclarationAtIndex (nIndex, aNewDeclaration);
    return this;
  }

  @Override
  public boolean hasDeclarations ()
  {
    return m_aDeclarations.hasDeclarations ();
  }

  @Override
  @Nonnegative
  public int getDeclarationCount ()
  {
    return m_aDeclarations.getDeclarationCount ();
  }

  @Override
  @Nullable
  public CSSDeclaration getDeclarationOfPropertyName (@Nullable final String sPropertyName)
  {
    return m_aDeclarations.getDeclarationOfPropertyName (sPropertyName);
  }

  @Override
  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <CSSDeclaration> getAllDeclarationsOfPropertyName (@Nullable final String sPropertyName)
  {
    return m_aDeclarations.getAllDeclarationsOfPropertyName (sPropertyName);
  }

  @Override
  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    // Always ignore property rules?
    if (!aSettings.isWritePropertyRules ())
      return "";

    final StringBuilder aSB = new StringBuilder ("@property ");
    aSB.append (m_sPropertyName);
    aSB.append (m_aDeclarations.getAsCSSString(aSettings, nIndentLevel));
    return aSB.toString ();
  }

  @Override
  @Nullable
  public final CSSSourceLocation getSourceLocation ()
  {
    return m_aSourceLocation;
  }

  @Override
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
    final CSSPropertyRule rhs = (CSSPropertyRule) o;
    return m_sPropertyName.equals (rhs.m_sPropertyName) && m_aDeclarations.equals (rhs.m_aDeclarations);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sPropertyName).append (m_aDeclarations).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("propertyName", m_sPropertyName)
                                       .append ("declarations", m_aDeclarations)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
