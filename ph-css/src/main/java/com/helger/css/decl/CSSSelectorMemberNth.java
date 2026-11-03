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

import java.util.Locale;

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
 * Represents a pseudo class with an <code>An+B</code> argument, as in <code>:nth-child(odd)</code>,
 * <code>:nth-last-of-type(-n+3)</code> or <code>:nth-child(2n+1 of .important)</code>.<br>
 * The expression is kept as written (without whitespace) for writing, and is available as the two
 * integers A and B for evaluation.
 *
 * @author Philip Helger
 * @since 8.4.0
 */
@NotThreadSafe
public class CSSSelectorMemberNth implements ICSSSelectorMember, ICSSSourceLocationAware
{
  private final ECSSNthPseudoClass m_ePseudoClass;
  private final String m_sExpression;
  private final int m_nA;
  private final int m_nB;
  private final ICommonsList <CSSSelector> m_aOfSelectors = new CommonsArrayList <> ();
  private CSSSourceLocation m_aSourceLocation;

  /**
   * Constructor with an expression as written in the CSS.
   *
   * @param ePseudoClass
   *        The pseudo class to use. May not be <code>null</code>.
   * @param sExpression
   *        The <code>An+B</code> expression without whitespace, e.g. <code>odd</code>,
   *        <code>even</code>, <code>5</code>, <code>-n+3</code> or <code>2n+1</code>. May neither
   *        be <code>null</code> nor empty.
   * @throws IllegalArgumentException
   *         if the expression is not a valid <code>An+B</code> expression
   */
  public CSSSelectorMemberNth (@NonNull final ECSSNthPseudoClass ePseudoClass,
                               @NonNull @Nonempty final String sExpression)
  {
    ValueEnforcer.notNull (ePseudoClass, "PseudoClass");
    ValueEnforcer.notEmpty (sExpression, "Expression");
    m_ePseudoClass = ePseudoClass;
    m_sExpression = sExpression;

    final String sLC = sExpression.toLowerCase (Locale.ROOT);
    try
    {
      if (sLC.equals ("odd"))
      {
        m_nA = 2;
        m_nB = 1;
      }
      else
        if (sLC.equals ("even"))
        {
          m_nA = 2;
          m_nB = 0;
        }
        else
        {
          final int nIndexN = sLC.indexOf ('n');
          if (nIndexN < 0)
          {
            m_nA = 0;
            m_nB = Integer.parseInt (sLC);
          }
          else
          {
            final String sA = sLC.substring (0, nIndexN);
            if (sA.isEmpty () || sA.equals ("+"))
              m_nA = 1;
            else
              if (sA.equals ("-"))
                m_nA = -1;
              else
                m_nA = Integer.parseInt (sA);
            final String sB = sLC.substring (nIndexN + 1);
            m_nB = sB.isEmpty () ? 0 : Integer.parseInt (sB);
          }
        }
    }
    catch (final NumberFormatException ex)
    {
      throw new IllegalArgumentException ("Invalid An+B expression '" + sExpression + "'", ex);
    }
  }

  /**
   * Constructor with the two integers A and B. The expression is created in the form
   * <code>An+B</code>.
   *
   * @param ePseudoClass
   *        The pseudo class to use. May not be <code>null</code>.
   * @param nA
   *        The step size A.
   * @param nB
   *        The offset B.
   */
  public CSSSelectorMemberNth (@NonNull final ECSSNthPseudoClass ePseudoClass, final int nA, final int nB)
  {
    ValueEnforcer.notNull (ePseudoClass, "PseudoClass");
    m_ePseudoClass = ePseudoClass;
    m_nA = nA;
    m_nB = nB;

    if (nA == 0)
      m_sExpression = Integer.toString (nB);
    else
    {
      final StringBuilder aSB = new StringBuilder ();
      if (nA == -1)
        aSB.append ('-');
      else
        if (nA != 1)
          aSB.append (nA);
      aSB.append ('n');
      if (nB > 0)
        aSB.append ('+').append (nB);
      else
        if (nB < 0)
          aSB.append (nB);
      m_sExpression = aSB.toString ();
    }
  }

  /**
   * @return The pseudo class. Never <code>null</code>.
   */
  @NonNull
  public ECSSNthPseudoClass getPseudoClass ()
  {
    return m_ePseudoClass;
  }

  /**
   * @return The <code>An+B</code> expression as written, without whitespace. Neither
   *         <code>null</code> nor empty.
   */
  @NonNull
  @Nonempty
  public String getExpression ()
  {
    return m_sExpression;
  }

  /**
   * @return The step size A. <code>odd</code> and <code>even</code> have an A of 2.
   */
  public int getA ()
  {
    return m_nA;
  }

  /**
   * @return The offset B. <code>odd</code> has a B of 1 and <code>even</code> has a B of 0.
   */
  public int getB ()
  {
    return m_nB;
  }

  public boolean hasOfSelectors ()
  {
    return m_aOfSelectors.isNotEmpty ();
  }

  @Nonnegative
  public int getOfSelectorCount ()
  {
    return m_aOfSelectors.size ();
  }

  /**
   * Add a selector of the <code>of S</code> part. Only valid for <code>:nth-child()</code> and
   * <code>:nth-last-child()</code>.
   *
   * @param aSelector
   *        The selector to add. May not be <code>null</code>.
   * @return this for chaining
   */
  @NonNull
  public CSSSelectorMemberNth addOfSelector (@NonNull final CSSSelector aSelector)
  {
    ValueEnforcer.notNull (aSelector, "Selector");

    m_aOfSelectors.add (aSelector);
    return this;
  }

  @NonNull
  public EChange removeOfSelector (@NonNull final CSSSelector aSelector)
  {
    return m_aOfSelectors.removeObject (aSelector);
  }

  @NonNull
  public EChange removeAllOfSelectors ()
  {
    return m_aOfSelectors.removeAll ();
  }

  @Nullable
  public CSSSelector getOfSelectorAtIndex (@Nonnegative final int nSelectorIndex)
  {
    return m_aOfSelectors.getAtIndex (nSelectorIndex);
  }

  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <CSSSelector> getAllOfSelectors ()
  {
    return m_aOfSelectors.getClone ();
  }

  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();
    final StringBuilder aSB = new StringBuilder (":").append (m_ePseudoClass.getName ())
                                                     .append ('(')
                                                     .append (m_sExpression);
    if (m_aOfSelectors.isNotEmpty ())
    {
      aSB.append (" of ");
      boolean bFirst = true;
      for (final CSSSelector aOfSelector : m_aOfSelectors)
      {
        if (bFirst)
          bFirst = false;
        else
          aSB.append (bOptimizedOutput ? "," : ", ");
        aSB.append (aOfSelector.getAsCSSString (aSettings, 0));
      }
    }
    return aSB.append (')').toString ();
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
    final CSSSelectorMemberNth rhs = (CSSSelectorMemberNth) o;
    return m_ePseudoClass.equals (rhs.m_ePseudoClass) &&
           m_sExpression.equals (rhs.m_sExpression) &&
           m_aOfSelectors.equals (rhs.m_aOfSelectors);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_ePseudoClass)
                                       .append (m_sExpression)
                                       .append (m_aOfSelectors)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("PseudoClass", m_ePseudoClass)
                                       .append ("Expression", m_sExpression)
                                       .append ("A", m_nA)
                                       .append ("B", m_nB)
                                       .append ("OfSelectors", m_aOfSelectors)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
