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
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.state.EChange;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.ICommonsList;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;

/**
 * keyframes block
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSKeyframesBlock implements IHasCSSDeclarations <CSSKeyframesBlock>, ICSSSourceLocationAware
{
  private final ICommonsList <String> m_aKeyframesSelectors;
  private final CSSDeclarationContainer m_aDeclarations = new CSSDeclarationContainer ();
  private CSSSourceLocation m_aSourceLocation;

  public CSSKeyframesBlock (@NonNull @Nonempty final String... aKeyframesSelectors)
  {
    ValueEnforcer.notEmptyNoNullValue (aKeyframesSelectors, "KeyframesSelectors");
    m_aKeyframesSelectors = new CommonsArrayList <> (aKeyframesSelectors);
  }

  public CSSKeyframesBlock (@NonNull @Nonempty final Iterable <String> aKeyframesSelectors)
  {
    ValueEnforcer.notEmptyNoNullValue (aKeyframesSelectors, "KeyframesSelectors");
    m_aKeyframesSelectors = new CommonsArrayList <> (aKeyframesSelectors);
  }

  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <String> getAllKeyframesSelectors ()
  {
    return m_aKeyframesSelectors.getClone ();
  }

  @NonNull
  public CSSKeyframesBlock addDeclaration (@NonNull final CSSDeclaration aDeclaration)
  {
    m_aDeclarations.addDeclaration (aDeclaration);
    return this;
  }

  @NonNull
  public CSSKeyframesBlock addDeclaration (@Nonnegative final int nIndex, @NonNull final CSSDeclaration aNewDeclaration)
  {
    m_aDeclarations.addDeclaration (nIndex, aNewDeclaration);
    return this;
  }

  @NonNull
  public EChange removeDeclaration (@NonNull final CSSDeclaration aDeclaration)
  {
    return m_aDeclarations.removeDeclaration (aDeclaration);
  }

  @NonNull
  public EChange removeDeclaration (@Nonnegative final int nDeclarationIndex)
  {
    return m_aDeclarations.removeDeclaration (nDeclarationIndex);
  }

  @NonNull
  public EChange removeAllDeclarations ()
  {
    return m_aDeclarations.removeAllDeclarations ();
  }

  @NonNull
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

  @NonNull
  public CSSKeyframesBlock setDeclarationAtIndex (@Nonnegative final int nIndex,
                                                  @NonNull final CSSDeclaration aNewDeclaration)
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

  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <CSSDeclaration> getAllDeclarationsOfPropertyName (@Nullable final String sPropertyName)
  {
    return m_aDeclarations.getAllDeclarationsOfPropertyName (sPropertyName);
  }

  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    if (aSettings.isRemoveUnnecessaryCode () && !hasDeclarations ())
      return "";

    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();

    final StringBuilder aSB = new StringBuilder ();

    // Emit all selectors
    for (final String sSelector : m_aKeyframesSelectors)
    {
      if (aSB.length () > 0)
        aSB.append (bOptimizedOutput ? "," : ", ");
      aSB.append (sSelector);
    }

    aSB.append (m_aDeclarations.getAsCSSString (aSettings, nIndentLevel));
    return aSB.toString ();
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
    final CSSKeyframesBlock rhs = (CSSKeyframesBlock) o;
    return m_aKeyframesSelectors.equals (rhs.m_aKeyframesSelectors) && m_aDeclarations.equals (rhs.m_aDeclarations);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aKeyframesSelectors).append (m_aDeclarations).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("KeyframesSelectors", m_aKeyframesSelectors)
                                       .append ("Declarations", m_aDeclarations)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
