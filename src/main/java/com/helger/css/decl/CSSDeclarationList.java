/**
 * Copyright (C) 2014-2016 Philip Helger (www.helger.com)
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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.state.EChange;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CCSS;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a list of {@link CSSDeclaration} objects. This class emits all
 * declarations in a row, without any surrounding block elements.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSDeclarationList implements IHasCSSDeclarations, ICSSSourceLocationAware
{
  private final List <CSSDeclaration> m_aDeclarations = new ArrayList <CSSDeclaration> ();
  private CSSSourceLocation m_aSourceLocation;

  public CSSDeclarationList ()
  {}

  @Nonnull
  public final CSSDeclarationList addDeclaration (@Nonnull final CSSDeclaration aNewDeclaration)
  {
    ValueEnforcer.notNull (aNewDeclaration, "NewDeclaration");

    m_aDeclarations.add (aNewDeclaration);
    return this;
  }

  public final IHasCSSDeclarations addDeclaration (@Nonnull @Nonempty final String sProperty,
                                                   @Nonnull final CSSExpression aExpression,
                                                   final boolean bImportant)
  {
    return addDeclaration (new CSSDeclaration (sProperty, aExpression, bImportant));
  }

  @Nonnull
  public CSSDeclarationList addDeclaration (@Nonnegative final int nIndex,
                                            @Nonnull final CSSDeclaration aNewDeclaration)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aNewDeclaration, "NewDeclaration");

    if (nIndex >= getDeclarationCount ())
      m_aDeclarations.add (aNewDeclaration);
    else
      m_aDeclarations.add (nIndex, aNewDeclaration);
    return this;
  }

  @Nonnull
  public final EChange removeDeclaration (@Nonnull final CSSDeclaration aDeclaration)
  {
    return EChange.valueOf (m_aDeclarations.remove (aDeclaration));
  }

  @Nonnull
  public final EChange removeDeclaration (@Nonnegative final int nDeclarationIndex)
  {
    if (nDeclarationIndex < 0 || nDeclarationIndex >= m_aDeclarations.size ())
      return EChange.UNCHANGED;
    return EChange.valueOf (m_aDeclarations.remove (nDeclarationIndex) != null);
  }

  /**
   * Remove all declarations.
   *
   * @return {@link EChange#CHANGED} if any declaration was removed,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   * @since 3.7.3
   */
  @Nonnull
  public EChange removeAllDeclarations ()
  {
    if (m_aDeclarations.isEmpty ())
      return EChange.UNCHANGED;
    m_aDeclarations.clear ();
    return EChange.CHANGED;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <CSSDeclaration> getAllDeclarations ()
  {
    return CollectionHelper.newList (m_aDeclarations);
  }

  @Nullable
  public final CSSDeclaration getDeclarationAtIndex (@Nonnegative final int nIndex)
  {
    return CollectionHelper.getSafe (m_aDeclarations, nIndex);
  }

  @Nonnull
  public CSSDeclarationList setDeclarationAtIndex (@Nonnegative final int nIndex,
                                                   @Nonnull final CSSDeclaration aNewDeclaration)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aNewDeclaration, "NewDeclaration");

    if (nIndex >= getDeclarationCount ())
      m_aDeclarations.add (aNewDeclaration);
    else
      m_aDeclarations.set (nIndex, aNewDeclaration);
    return this;
  }

  public boolean hasDeclarations ()
  {
    return !m_aDeclarations.isEmpty ();
  }

  @Nonnegative
  public int getDeclarationCount ()
  {
    return m_aDeclarations.size ();
  }

  @Nullable
  public CSSDeclaration getDeclarationOfPropertyName (@Nullable final String sPropertyName)
  {
    if (StringHelper.hasText (sPropertyName))
      for (final CSSDeclaration aDecl : m_aDeclarations)
        if (aDecl.getProperty ().equals (sPropertyName))
          return aDecl;
    return null;
  }

  @Nullable
  public CSSDeclaration getDeclarationOfPropertyNameCaseInsensitive (@Nullable final String sPropertyName)
  {
    if (StringHelper.hasText (sPropertyName))
      for (final CSSDeclaration aDecl : m_aDeclarations)
        if (aDecl.getProperty ().equalsIgnoreCase (sPropertyName))
          return aDecl;
    return null;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <CSSDeclaration> getAllDeclarationsOfPropertyName (@Nullable final String sPropertyName)
  {
    final List <CSSDeclaration> ret = new ArrayList <CSSDeclaration> ();
    if (StringHelper.hasText (sPropertyName))
      for (final CSSDeclaration aDecl : m_aDeclarations)
        if (aDecl.getProperty ().equals (sPropertyName))
          ret.add (aDecl);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <CSSDeclaration> getAllDeclarationsOfPropertyNameCaseInsensitive (@Nullable final String sPropertyName)
  {
    final List <CSSDeclaration> ret = new ArrayList <CSSDeclaration> ();
    if (StringHelper.hasText (sPropertyName))
      for (final CSSDeclaration aDecl : m_aDeclarations)
        if (aDecl.getProperty ().equalsIgnoreCase (sPropertyName))
          ret.add (aDecl);
    return ret;
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();

    final int nDeclCount = m_aDeclarations.size ();
    if (nDeclCount == 0)
      return "";
    if (nDeclCount == 1)
    {
      // A single declaration
      final StringBuilder aSB = new StringBuilder ();
      aSB.append (CollectionHelper.getFirstElement (m_aDeclarations).getAsCSSString (aSettings, nIndentLevel));
      // No ';' at the last entry
      if (!bOptimizedOutput)
        aSB.append (CCSS.DEFINITION_END);
      return aSB.toString ();
    }

    // More than one declaration
    final StringBuilder aSB = new StringBuilder ();
    int nIndex = 0;
    for (final CSSDeclaration aDeclaration : m_aDeclarations)
    {
      // Indentation
      if (!bOptimizedOutput)
        aSB.append (aSettings.getIndent (nIndentLevel + 1));
      // Emit the main declaration plus the semicolon
      aSB.append (aDeclaration.getAsCSSString (aSettings, nIndentLevel + 1));
      // No ';' at the last decl
      if (!bOptimizedOutput || nIndex < nDeclCount - 1)
        aSB.append (CCSS.DEFINITION_END);
      if (!bOptimizedOutput)
        aSB.append (aSettings.getNewLineString ());
      ++nIndex;
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
    final CSSDeclarationList rhs = (CSSDeclarationList) o;
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
