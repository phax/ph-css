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

import java.util.Collection;
import java.util.function.Predicate;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.state.EChange;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CCSS;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriteable;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a list of {@link ICSSWriteable} objects. This class emits all
 * contained elements with a semicolon as separator but without any surrounding
 * block elements.
 *
 * @author Philip Helger
 * @param <DATATYPE>
 *        The type of object to be handled.
 * @since 5.0.0
 */
@NotThreadSafe
public class CSSWritableList <DATATYPE extends ICSSWriteable> implements ICSSSourceLocationAware, ICSSWriteable
{
  private final ICommonsList <DATATYPE> m_aElements = new CommonsArrayList<> ();
  private CSSSourceLocation m_aSourceLocation;

  public CSSWritableList ()
  {}

  protected final void add (@Nonnull final DATATYPE aElement)
  {
    ValueEnforcer.notNull (aElement, "Element");
    m_aElements.add (aElement);
  }

  protected final void add (@Nonnegative final int nIndex, @Nonnull final DATATYPE aElement)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aElement, "Element");

    if (nIndex >= m_aElements.size ())
      m_aElements.add (aElement);
    else
      m_aElements.add (nIndex, aElement);
  }

  protected final void set (@Nonnegative final int nIndex, @Nonnull final DATATYPE aElement)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aElement, "Element");

    if (nIndex >= m_aElements.size ())
      m_aElements.add (aElement);
    else
      m_aElements.set (nIndex, aElement);
  }

  @Nonnull
  protected final EChange remove (@Nonnull final DATATYPE aElement)
  {
    return EChange.valueOf (m_aElements.remove (aElement));
  }

  @Nonnull
  protected final EChange remove (@Nonnegative final int nIndex)
  {
    if (nIndex < 0 || nIndex >= m_aElements.size ())
      return EChange.UNCHANGED;
    return EChange.valueOf (m_aElements.remove (nIndex) != null);
  }

  @Nonnull
  protected final EChange removeAll ()
  {
    return m_aElements.removeAll ();
  }

  @Nonnull
  @ReturnsMutableCopy
  protected final ICommonsList <DATATYPE> getAll ()
  {
    return m_aElements.getClone ();
  }

  @Nullable
  protected final DATATYPE getAtIndex (@Nonnegative final int nIndex)
  {
    return m_aElements.getAtIndex (nIndex);
  }

  protected final boolean isEmpty ()
  {
    return m_aElements.isEmpty ();
  }

  protected final boolean isNotEmpty ()
  {
    return m_aElements.isNotEmpty ();
  }

  @Nonnegative
  protected final int getCount ()
  {
    return m_aElements.size ();
  }

  protected final boolean containsAny (@Nonnull final Predicate <? super DATATYPE> aFilter)
  {
    return m_aElements.containsAny (aFilter);
  }

  @Nullable
  protected final DATATYPE findFirst (@Nonnull final Predicate <? super DATATYPE> aFilter)
  {
    return m_aElements.findFirst (aFilter);
  }

  protected final void findAll (@Nonnull final Predicate <? super DATATYPE> aFilter,
                                @Nonnull final Collection <? super DATATYPE> ret)
  {
    m_aElements.findAll (aFilter, ret::add);
  }

  @Nonnull
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();

    final int nDeclCount = m_aElements.size ();
    if (nDeclCount == 0)
      return "";
    if (nDeclCount == 1)
    {
      // A single element
      final StringBuilder aSB = new StringBuilder ();
      aSB.append (m_aElements.get (0).getAsCSSString (aSettings, nIndentLevel));
      // No ';' at the last entry
      if (!bOptimizedOutput)
        aSB.append (CCSS.DEFINITION_END);
      return aSB.toString ();
    }

    // More than one element
    final StringBuilder aSB = new StringBuilder ();
    int nIndex = 0;
    for (final DATATYPE aElement : m_aElements)
    {
      // Indentation
      if (!bOptimizedOutput)
        aSB.append (aSettings.getIndent (nIndentLevel + 1));
      // Emit the main element plus the semicolon
      aSB.append (aElement.getAsCSSString (aSettings, nIndentLevel + 1));
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
    final CSSWritableList <?> rhs = (CSSWritableList <?>) o;
    return m_aElements.equals (rhs.m_aElements);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aElements).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("elements", m_aElements)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .toString ();
  }
}
