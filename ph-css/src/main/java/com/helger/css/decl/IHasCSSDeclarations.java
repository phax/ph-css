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

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.state.EChange;
import com.helger.commons.traits.IGenericImplTrait;
import com.helger.css.ICSSWriteable;

/**
 * Sanity interface for all objects having CSS declarations.
 *
 * @author Philip Helger
 */
public interface IHasCSSDeclarations <IMPLTYPE extends IHasCSSDeclarations <IMPLTYPE>>
                                     extends ICSSWriteable, IGenericImplTrait <IMPLTYPE>
{
  /**
   * Add a new declaration.
   *
   * @param aDeclaration
   *        The declaration to be added. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  IMPLTYPE addDeclaration (@Nonnull CSSDeclaration aDeclaration);

  /**
   * Add a new declaration.
   *
   * @param sProperty
   *        The name of the property. E.g. "color". May neither be
   *        <code>null</code> nor empty.
   * @param aExpression
   *        The value of the property. May not be <code>null</code>.
   * @param bImportant
   *        <code>true</code> if it is important, <code>false</code> if not.
   * @return this
   */
  @Nonnull
  default IMPLTYPE addDeclaration (@Nonnull @Nonempty final String sProperty,
                                   @Nonnull final CSSExpression aExpression,
                                   final boolean bImportant)
  {
    return addDeclaration (new CSSDeclaration (sProperty, aExpression, bImportant));
  }

  /**
   * Add a new declaration at the specified index.
   *
   * @param nIndex
   *        The index to retrieve. Must be &ge; 0. If the index is &ge; than
   *        <code>getDeclarationCount()</code>, it behaves like
   *        {@link #addDeclaration(CSSDeclaration)}.
   * @param aDeclaration
   *        The declaration to be added. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  IMPLTYPE addDeclaration (@Nonnegative int nIndex, @Nonnull CSSDeclaration aDeclaration);

  /**
   * Remove the given declaration
   *
   * @param aDeclaration
   *        The declaration to be removed. May not be <code>null</code>.
   * @return {@link EChange#CHANGED} if the declaration was successfully removed
   */
  @Nonnull
  EChange removeDeclaration (@Nonnull CSSDeclaration aDeclaration);

  /**
   * Remove the declaration at the specified index
   *
   * @param nDeclarationIndex
   *        The index of the declaration to be removed. Must be &ge; 0.
   * @return {@link EChange#CHANGED} if the declaration was successfully
   *         removed, {@link EChange#UNCHANGED} if the index was invalid.
   */
  @Nonnull
  EChange removeDeclaration (@Nonnegative int nDeclarationIndex);

  /**
   * Remove all declarations.
   *
   * @return {@link EChange#CHANGED} if any declaration was removed,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   * @since 3.7.3
   */
  @Nonnull
  EChange removeAllDeclarations ();

  /**
   * @return A mutable, non-<code>null</code> copy of all contained
   *         declarations.
   */
  @Nonnull
  @ReturnsMutableCopy
  ICommonsList <CSSDeclaration> getAllDeclarations ();

  /**
   * @param nIndex
   *        The index to retrieve
   * @return The declaration at the specified index or <code>null</code> if the
   *         index is invalid
   */
  @Nullable
  CSSDeclaration getDeclarationAtIndex (@Nonnegative int nIndex);

  /**
   * Set the declaration at the specified index with a new one. If an existing
   * declaration is present at that index, it is overwritten.
   *
   * @param nIndex
   *        The index to retrieve. Must be &ge; 0. If the index is &ge; than
   *        <code>getDeclarationCount()</code>, it behaves like
   *        {@link #addDeclaration(CSSDeclaration)}.
   * @param aNewDeclaration
   *        The new declaration to be set.
   * @return this
   */
  @Nonnull
  IMPLTYPE setDeclarationAtIndex (@Nonnegative int nIndex, @Nonnull CSSDeclaration aNewDeclaration);

  /**
   * @return <code>true</code> if at least one declaration is present,
   *         <code>false</code> if no declaration is present.
   */
  boolean hasDeclarations ();

  /**
   * @return The number of contained declarations. Always &ge; 0.
   */
  @Nonnegative
  int getDeclarationCount ();

  /**
   * Get the first declaration with the specified property name. If no such
   * property name is present, <code>null</code> is returned. If more than one
   * declaration ith the specified property name is present, always the first in
   * the list will be returned. The comparison happens <b>case sensitive</b>.
   *
   * @param sPropertyName
   *        The property name of the declaration to search (e.g.
   *        <code>color</code>). May be <code>null</code>.
   * @return <code>null</code> if no such property name was found.
   * @since 3.7.4
   */
  @Nullable
  CSSDeclaration getDeclarationOfPropertyName (@Nullable String sPropertyName);

  /**
   * Get the first declaration with the specified property name. If no such
   * property name is present, <code>null</code> is returned. If more than one
   * declaration ith the specified property name is present, always the first in
   * the list will be returned. The comparison happens <b>case insensitive</b>.
   *
   * @param sPropertyName
   *        The property name of the declaration to search (e.g.
   *        <code>color</code>). May be <code>null</code>.
   * @return <code>null</code> if no such property name was found.
   * @since 3.7.4
   */
  @Nullable
  CSSDeclaration getDeclarationOfPropertyNameCaseInsensitive (@Nullable String sPropertyName);

  /**
   * Get all declarations within this list that have the specified property
   * name. The comparison happens <b>case sensitive</b>.
   *
   * @param sPropertyName
   *        The property name of the declaration to search (e.g.
   *        <code>color</code>). May be <code>null</code>.
   * @return Never <code>null</code> but maybe an empty list.
   * @since 3.7.4
   */
  @Nonnull
  @ReturnsMutableCopy
  ICommonsList <CSSDeclaration> getAllDeclarationsOfPropertyName (@Nullable String sPropertyName);

  /**
   * Get all declarations within this list that have the specified property
   * name. The comparison happens <b>case insensitive</b>.
   *
   * @param sPropertyName
   *        The property name of the declaration to search (e.g.
   *        <code>color</code>). May be <code>null</code>.
   * @return Never <code>null</code> but maybe an empty list.
   * @since 3.7.4
   */
  @Nonnull
  @ReturnsMutableCopy
  ICommonsList <CSSDeclaration> getAllDeclarationsOfPropertyNameCaseInsensitive (@Nullable String sPropertyName);
}
