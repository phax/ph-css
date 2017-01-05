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

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.state.EChange;
import com.helger.commons.string.StringHelper;

/**
 * Represents a list of {@link CSSDeclaration} objects. This class emits all
 * declarations in a row, without any surrounding block elements.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSDeclarationList extends CSSWritableList <CSSDeclaration>
                                implements IHasCSSDeclarations <CSSDeclarationList>
{
  public CSSDeclarationList ()
  {}

  @Nonnull
  public final CSSDeclarationList addDeclaration (@Nonnull final CSSDeclaration aNewDeclaration)
  {
    add (aNewDeclaration);
    return this;
  }

  @Nonnull
  public CSSDeclarationList addDeclaration (@Nonnegative final int nIndex,
                                            @Nonnull final CSSDeclaration aNewDeclaration)
  {
    add (nIndex, aNewDeclaration);
    return this;
  }

  @Nonnull
  public final EChange removeDeclaration (@Nonnull final CSSDeclaration aDeclaration)
  {
    return remove (aDeclaration);
  }

  @Nonnull
  public final EChange removeDeclaration (@Nonnegative final int nDeclarationIndex)
  {
    return remove (nDeclarationIndex);
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
    return removeAll ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public final ICommonsList <CSSDeclaration> getAllDeclarations ()
  {
    return getAll ();
  }

  @Nullable
  public final CSSDeclaration getDeclarationAtIndex (@Nonnegative final int nIndex)
  {
    return getAtIndex (nIndex);
  }

  @Nonnull
  public CSSDeclarationList setDeclarationAtIndex (@Nonnegative final int nIndex,
                                                   @Nonnull final CSSDeclaration aNewDeclaration)
  {
    set (nIndex, aNewDeclaration);
    return this;
  }

  public boolean hasDeclarations ()
  {
    return isNotEmpty ();
  }

  @Nonnegative
  public int getDeclarationCount ()
  {
    return getCount ();
  }

  @Nullable
  public CSSDeclaration getDeclarationOfPropertyName (@Nullable final String sPropertyName)
  {
    if (StringHelper.hasNoText (sPropertyName))
      return null;

    return findFirst (aDecl -> aDecl.getProperty ().equals (sPropertyName));
  }

  @Nullable
  public CSSDeclaration getDeclarationOfPropertyNameCaseInsensitive (@Nullable final String sPropertyName)
  {
    if (StringHelper.hasNoText (sPropertyName))
      return null;

    return findFirst (aDecl -> aDecl.getProperty ().equalsIgnoreCase (sPropertyName));
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <CSSDeclaration> getAllDeclarationsOfPropertyName (@Nullable final String sPropertyName)
  {
    final ICommonsList <CSSDeclaration> ret = new CommonsArrayList <> ();
    if (StringHelper.hasText (sPropertyName))
      findAll (aDecl -> aDecl.getProperty ().equals (sPropertyName), ret);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <CSSDeclaration> getAllDeclarationsOfPropertyNameCaseInsensitive (@Nullable final String sPropertyName)
  {
    final ICommonsList <CSSDeclaration> ret = new CommonsArrayList <> ();
    if (StringHelper.hasText (sPropertyName))
      findAll (aDecl -> aDecl.getProperty ().equalsIgnoreCase (sPropertyName), ret);
    return ret;
  }
}
