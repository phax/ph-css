/*
 * Copyright (C) 2014-2025 Philip Helger (www.helger.com)
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

import com.helger.annotation.Nonnegative;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.state.EChange;
import com.helger.base.string.StringHelper;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.ICommonsList;

/**
 * Represents a list of {@link CSSDeclaration} objects. This class emits all declarations in a row,
 * without any surrounding block elements.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSDeclarationList extends CSSWritableList <CSSDeclaration> implements
                                IHasCSSDeclarations <CSSDeclarationList>
{
  public CSSDeclarationList ()
  {}

  @NonNull
  public final CSSDeclarationList addDeclaration (@NonNull final CSSDeclaration aNewDeclaration)
  {
    add (aNewDeclaration);
    return this;
  }

  @NonNull
  public CSSDeclarationList addDeclaration (@Nonnegative final int nIndex,
                                            @NonNull final CSSDeclaration aNewDeclaration)
  {
    add (nIndex, aNewDeclaration);
    return this;
  }

  @NonNull
  public final EChange removeDeclaration (@NonNull final CSSDeclaration aDeclaration)
  {
    return removeObject (aDeclaration);
  }

  @NonNull
  public final EChange removeDeclaration (@Nonnegative final int nDeclarationIndex)
  {
    return removeAtIndex (nDeclarationIndex);
  }

  /**
   * Remove all declarations.
   *
   * @return {@link EChange#CHANGED} if any declaration was removed, {@link EChange#UNCHANGED}
   *         otherwise. Never <code>null</code>.
   * @since 3.7.3
   */
  @NonNull
  public EChange removeAllDeclarations ()
  {
    return removeAll ();
  }

  @NonNull
  @ReturnsMutableCopy
  public final ICommonsList <CSSDeclaration> getAllDeclarations ()
  {
    return getClone ();
  }

  @Nullable
  public final CSSDeclaration getDeclarationAtIndex (@Nonnegative final int nIndex)
  {
    return getAtIndex (nIndex);
  }

  @NonNull
  public CSSDeclarationList setDeclarationAtIndex (@Nonnegative final int nIndex,
                                                   @NonNull final CSSDeclaration aNewDeclaration)
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
    return size ();
  }

  @Nullable
  public CSSDeclaration getDeclarationOfPropertyName (@Nullable final String sPropertyName)
  {
    if (StringHelper.isEmpty (sPropertyName))
      return null;

    return findFirst (aDecl -> aDecl.hasProperty (sPropertyName));
  }

  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <CSSDeclaration> getAllDeclarationsOfPropertyName (@Nullable final String sPropertyName)
  {
    final ICommonsList <CSSDeclaration> ret = new CommonsArrayList <> ();
    if (StringHelper.isNotEmpty (sPropertyName))
      findAll (aDecl -> aDecl.hasProperty (sPropertyName), ret::add);
    return ret;
  }
}
