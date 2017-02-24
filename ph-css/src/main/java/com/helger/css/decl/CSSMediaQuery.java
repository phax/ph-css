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

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.state.EChange;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriteable;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a single media query
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSMediaQuery implements ICSSWriteable, ICSSSourceLocationAware
{
  /**
   * A global modifier that can be used in front of a single CSS media query.
   *
   * @author Philip Helger
   */
  public static enum EModifier
  {
    NONE (""),
    NOT ("not "),
    ONLY ("only ");

    private final String m_sText;

    private EModifier (@Nonnull final String sText)
    {
      m_sText = sText;
    }

    @Nonnull
    public String getCSSText ()
    {
      return m_sText;
    }
  }

  private final EModifier m_eModifier;
  private final String m_sMedium;
  private final ICommonsList <CSSMediaExpression> m_aMediaExpressions = new CommonsArrayList<> ();
  private CSSSourceLocation m_aSourceLocation;

  /**
   * Constructor without a modifier. This implicitly uses the modifier
   * {@link EModifier#NONE}.
   *
   * @param sMedium
   *        The medium to use. May be <code>null</code>.
   */
  public CSSMediaQuery (@Nullable final String sMedium)
  {
    this (EModifier.NONE, sMedium);
  }

  /**
   * Constructor
   *
   * @param eModifier
   *        The modifier to use. May not be <code>null</code>.
   * @param sMedium
   *        The medium to use. May be <code>null</code>.
   */
  public CSSMediaQuery (@Nonnull final EModifier eModifier, @Nullable final String sMedium)
  {
    ValueEnforcer.notNull (eModifier, "Modifier");
    m_eModifier = eModifier;
    m_sMedium = sMedium;
  }

  /**
   * @return The media query modifier that was used. Never <code>null</code>.
   */
  @Nonnull
  public EModifier getModifier ()
  {
    return m_eModifier;
  }

  /**
   * @return <code>true</code> if the modifier is {@link EModifier#NOT}.
   * @see #getModifier()
   */
  public boolean isNot ()
  {
    return m_eModifier == EModifier.NOT;
  }

  /**
   * @return <code>true</code> if the modifier is {@link EModifier#ONLY}.
   * @see #getModifier()
   */
  public boolean isOnly ()
  {
    return m_eModifier == EModifier.ONLY;
  }

  /**
   * @return The medium passed in the constructor.
   */
  @Nullable
  public String getMedium ()
  {
    return m_sMedium;
  }

  /**
   * @return <code>true</code> if at least a single media expression is present.
   */
  public boolean hasMediaExpressions ()
  {
    return m_aMediaExpressions.isNotEmpty ();
  }

  /**
   * @return The number of contained media expressions. Always &ge; 0.
   */
  @Nonnegative
  public int getMediaExpressionCount ()
  {
    return m_aMediaExpressions.size ();
  }

  /**
   * Append a media expression to the list.
   *
   * @param aMediaExpression
   *        The media expression to be added. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public CSSMediaQuery addMediaExpression (@Nonnull final CSSMediaExpression aMediaExpression)
  {
    ValueEnforcer.notNull (aMediaExpression, "MediaExpression");

    m_aMediaExpressions.add (aMediaExpression);
    return this;
  }

  /**
   * Add a media expression to the list at the specified index.
   *
   * @param nIndex
   *        The index where the media expression should be added. Must be &ge;
   *        0.
   * @param aMediaExpression
   *        The media expression to be added. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public CSSMediaQuery addMediaExpression (@Nonnegative final int nIndex,
                                           @Nonnull final CSSMediaExpression aMediaExpression)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aMediaExpression, "MediaExpression");

    if (nIndex >= getMediaExpressionCount ())
      m_aMediaExpressions.add (aMediaExpression);
    else
      m_aMediaExpressions.add (nIndex, aMediaExpression);
    return this;
  }

  /**
   * Remove the specified media expression.
   *
   * @param aMediaExpression
   *        The media expression to be removed. May be <code>null</code>.
   * @return {@link EChange#CHANGED} if removal succeeded,
   *         {@link EChange#UNCHANGED} otherwise.
   */
  @Nonnull
  public EChange removeMediaExpression (@Nullable final CSSMediaExpression aMediaExpression)
  {
    return EChange.valueOf (m_aMediaExpressions.remove (aMediaExpression));
  }

  /**
   * Remove the media expression at the specified index.
   *
   * @param nExpressionIndex
   *        The index of the media expression to be removed.
   * @return {@link EChange#CHANGED} if removal succeeded,
   *         {@link EChange#UNCHANGED} otherwise.
   */
  @Nonnull
  public EChange removeMediaExpression (final int nExpressionIndex)
  {
    return m_aMediaExpressions.removeAtIndex (nExpressionIndex);
  }

  /**
   * Remove all media expressions.
   *
   * @return {@link EChange#CHANGED} if any media expression was removed,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   * @since 3.7.3
   */
  @Nonnull
  public EChange removeAllMediaExpressions ()
  {
    return m_aMediaExpressions.removeAll ();
  }

  /**
   * Get the media expression at the specified index.
   *
   * @param nExpressionIndex
   *        The index to be retrieved.
   * @return <code>null</code> if the index is &lt; 0 or too large.
   */
  @Nullable
  public CSSMediaExpression getMediaExpression (@Nonnegative final int nExpressionIndex)
  {
    return m_aMediaExpressions.getAtIndex (nExpressionIndex);
  }

  /**
   * @return A copy of all media expression. Never <code>null</code> but maybe
   *         empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <CSSMediaExpression> getAllMediaExpressions ()
  {
    return m_aMediaExpressions.getClone ();
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    final StringBuilder aSB = new StringBuilder ();

    // The modifier already contains a trailing space if necessary!
    aSB.append (m_eModifier.getCSSText ());

    boolean bIsFirstExpression = true;
    if (m_sMedium != null)
    {
      // Medium is optional
      aSB.append (m_sMedium);
      bIsFirstExpression = false;
    }

    if (m_aMediaExpressions.isNotEmpty ())
    {
      for (final CSSMediaExpression aMediaExpression : m_aMediaExpressions)
      {
        if (bIsFirstExpression)
          bIsFirstExpression = false;
        else
        {
          // The leading blank is required in case this is the first expression
          // after a medium declaration ("projectorand" instead of
          // "projector and")!
          // The trailing blank is required, because otherwise it is considered
          // a function ("and(")!
          aSB.append (" and ");
        }
        aSB.append (aMediaExpression.getAsCSSString (aSettings, nIndentLevel));
      }
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
    final CSSMediaQuery rhs = (CSSMediaQuery) o;
    return m_eModifier.equals (rhs.m_eModifier) &&
           EqualsHelper.equals (m_sMedium, rhs.m_sMedium) &&
           m_aMediaExpressions.equals (rhs.m_aMediaExpressions);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eModifier)
                                       .append (m_sMedium)
                                       .append (m_aMediaExpressions)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("modifier", m_eModifier)
                                       .append ("medium", m_sMedium)
                                       .append ("expressions", m_aMediaExpressions)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
