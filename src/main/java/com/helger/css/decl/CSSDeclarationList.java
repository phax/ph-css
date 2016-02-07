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
import com.helger.commons.state.EChange;
import com.helger.commons.string.StringHelper;

/**
 * Represents a list of {@link CSSDeclaration} objects. This class emits all
 * declarations in a row, without any surrounding block elements.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSDeclarationList extends AbstractCSSWritableList <CSSDeclaration> implements IHasCSSDeclarations
{
  public CSSDeclarationList ()
  {}

  @Nonnull
  public final CSSDeclarationList addDeclaration (@Nonnull final CSSDeclaration aNewDeclaration)
  {
    ValueEnforcer.notNull (aNewDeclaration, "NewDeclaration");

    m_aElements.add (aNewDeclaration);
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
      m_aElements.add (aNewDeclaration);
    else
      m_aElements.add (nIndex, aNewDeclaration);
    return this;
  }

  @Nonnull
  public final EChange removeDeclaration (@Nonnull final CSSDeclaration aDeclaration)
  {
    return EChange.valueOf (m_aElements.remove (aDeclaration));
  }

  @Nonnull
  public final EChange removeDeclaration (@Nonnegative final int nDeclarationIndex)
  {
    if (nDeclarationIndex < 0 || nDeclarationIndex >= m_aElements.size ())
      return EChange.UNCHANGED;
    return EChange.valueOf (m_aElements.remove (nDeclarationIndex) != null);
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
    if (m_aElements.isEmpty ())
      return EChange.UNCHANGED;
    m_aElements.clear ();
    return EChange.CHANGED;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <CSSDeclaration> getAllDeclarations ()
  {
    return CollectionHelper.newList (m_aElements);
  }

  @Nullable
  public final CSSDeclaration getDeclarationAtIndex (@Nonnegative final int nIndex)
  {
    return CollectionHelper.getSafe (m_aElements, nIndex);
  }

  @Nonnull
  public CSSDeclarationList setDeclarationAtIndex (@Nonnegative final int nIndex,
                                                   @Nonnull final CSSDeclaration aNewDeclaration)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aNewDeclaration, "NewDeclaration");

    if (nIndex >= getDeclarationCount ())
      m_aElements.add (aNewDeclaration);
    else
      m_aElements.set (nIndex, aNewDeclaration);
    return this;
  }

  public boolean hasDeclarations ()
  {
    return !m_aElements.isEmpty ();
  }

  @Nonnegative
  public int getDeclarationCount ()
  {
    return m_aElements.size ();
  }

  @Nullable
  public CSSDeclaration getDeclarationOfPropertyName (@Nullable final String sPropertyName)
  {
    if (StringHelper.hasText (sPropertyName))
      for (final CSSDeclaration aDecl : m_aElements)
        if (aDecl.getProperty ().equals (sPropertyName))
          return aDecl;
    return null;
  }

  @Nullable
  public CSSDeclaration getDeclarationOfPropertyNameCaseInsensitive (@Nullable final String sPropertyName)
  {
    if (StringHelper.hasText (sPropertyName))
      for (final CSSDeclaration aDecl : m_aElements)
        if (aDecl.getProperty ().equalsIgnoreCase (sPropertyName))
          return aDecl;
    return null;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <CSSDeclaration> getAllDeclarationsOfPropertyName (@Nullable final String sPropertyName)
  {
    final List <CSSDeclaration> ret = new ArrayList <> ();
    if (StringHelper.hasText (sPropertyName))
      for (final CSSDeclaration aDecl : m_aElements)
        if (aDecl.getProperty ().equals (sPropertyName))
          ret.add (aDecl);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <CSSDeclaration> getAllDeclarationsOfPropertyNameCaseInsensitive (@Nullable final String sPropertyName)
  {
    final List <CSSDeclaration> ret = new ArrayList <> ();
    if (StringHelper.hasText (sPropertyName))
      for (final CSSDeclaration aDecl : m_aElements)
        if (aDecl.getProperty ().equalsIgnoreCase (sPropertyName))
          ret.add (aDecl);
    return ret;
  }
}
