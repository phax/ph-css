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
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.state.EChange;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriteable;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a single expression consisting of several expression members
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSExpression implements ICSSWriteable, ICSSSourceLocationAware
{
  private final ICommonsList <ICSSExpressionMember> m_aMembers = new CommonsArrayList <> ();
  private CSSSourceLocation m_aSourceLocation;

  public CSSExpression ()
  {}

  /**
   * Add an expression member
   *
   * @param aMember
   *        The member to be added. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public CSSExpression addMember (@Nonnull final ICSSExpressionMember aMember)
  {
    ValueEnforcer.notNull (aMember, "ExpressionMember");

    m_aMembers.add (aMember);
    return this;
  }

  /**
   * Add an expression member
   *
   * @param nIndex
   *        The index where the member should be added. Must be &ge; 0.
   * @param aMember
   *        The member to be added. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public CSSExpression addMember (@Nonnegative final int nIndex, @Nonnull final ICSSExpressionMember aMember)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aMember, "ExpressionMember");

    if (nIndex >= getMemberCount ())
      m_aMembers.add (aMember);
    else
      m_aMembers.add (nIndex, aMember);
    return this;
  }

  /**
   * Shortcut method to add a simple text value.
   *
   * @param sValue
   *        The value to be added. May neither be <code>null</code> nor empty.
   * @return this
   */
  @Nonnull
  public CSSExpression addTermSimple (@Nonnull @Nonempty final String sValue)
  {
    return addMember (new CSSExpressionMemberTermSimple (sValue));
  }

  /**
   * Shortcut method to add a simple text value.
   *
   * @param nIndex
   *        The index where the member should be added. Must be &ge; 0.
   * @param sValue
   *        The value to be added. May neither be <code>null</code> nor empty.
   * @return this
   */
  @Nonnull
  public CSSExpression addTermSimple (@Nonnegative final int nIndex, @Nonnull @Nonempty final String sValue)
  {
    return addMember (nIndex, new CSSExpressionMemberTermSimple (sValue));
  }

  /**
   * Shortcut method to add a numeric value
   *
   * @param nValue
   *        The value to be added.
   * @return this
   */
  @Nonnull
  public CSSExpression addNumber (final int nValue)
  {
    return addMember (new CSSExpressionMemberTermSimple (nValue));
  }

  /**
   * Shortcut method to add a numeric value
   *
   * @param nIndex
   *        The index where the member should be added. Must be &ge; 0.
   * @param nValue
   *        The value to be added.
   * @return this
   */
  @Nonnull
  public CSSExpression addNumber (@Nonnegative final int nIndex, final int nValue)
  {
    return addMember (nIndex, new CSSExpressionMemberTermSimple (nValue));
  }

  /**
   * Shortcut method to add a numeric value
   *
   * @param nValue
   *        The value to be added.
   * @return this
   */
  @Nonnull
  public CSSExpression addNumber (final long nValue)
  {
    return addMember (new CSSExpressionMemberTermSimple (nValue));
  }

  /**
   * Shortcut method to add a numeric value
   *
   * @param nIndex
   *        The index where the member should be added. Must be &ge; 0.
   * @param nValue
   *        The value to be added.
   * @return this
   */
  @Nonnull
  public CSSExpression addNumber (@Nonnegative final int nIndex, final long nValue)
  {
    return addMember (nIndex, new CSSExpressionMemberTermSimple (nValue));
  }

  /**
   * Shortcut method to add a numeric value
   *
   * @param fValue
   *        The value to be added.
   * @return this
   */
  @Nonnull
  public CSSExpression addNumber (final float fValue)
  {
    return addMember (new CSSExpressionMemberTermSimple (fValue));
  }

  /**
   * Shortcut method to add a numeric value
   *
   * @param nIndex
   *        The index where the member should be added. Must be &ge; 0.
   * @param fValue
   *        The value to be added.
   * @return this
   */
  @Nonnull
  public CSSExpression addNumber (@Nonnegative final int nIndex, final float fValue)
  {
    return addMember (nIndex, new CSSExpressionMemberTermSimple (fValue));
  }

  /**
   * Shortcut method to add a numeric value
   *
   * @param dValue
   *        The value to be added.
   * @return this
   */
  @Nonnull
  public CSSExpression addNumber (final double dValue)
  {
    return addMember (new CSSExpressionMemberTermSimple (dValue));
  }

  /**
   * Shortcut method to add a numeric value
   *
   * @param nIndex
   *        The index where the member should be added. Must be &ge; 0.
   * @param dValue
   *        The value to be added.
   * @return this
   */
  @Nonnull
  public CSSExpression addNumber (@Nonnegative final int nIndex, final double dValue)
  {
    return addMember (nIndex, new CSSExpressionMemberTermSimple (dValue));
  }

  @Nonnull
  @Nonempty
  private static String _createStringValue (@Nonnull final String sValue)
  {
    return '"' + StringHelper.replaceAll (sValue, "\"", "\\\"") + '"';
  }

  /**
   * Shortcut method to add a string value that is automatically quoted inside
   *
   * @param sValue
   *        The value to be quoted and than added. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public CSSExpression addString (@Nonnull final String sValue)
  {
    return addTermSimple (_createStringValue (sValue));
  }

  /**
   * Shortcut method to add a string value that is automatically quoted inside
   *
   * @param nIndex
   *        The index where the member should be added. Must be &ge; 0.
   * @param sValue
   *        The value to be quoted and than added. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public CSSExpression addString (@Nonnegative final int nIndex, @Nonnull final String sValue)
  {
    return addTermSimple (nIndex, _createStringValue (sValue));
  }

  /**
   * Shortcut method to add a URI value
   *
   * @param sURI
   *        The value to be added. May neither be <code>null</code> nor empty
   * @return this
   */
  @Nonnull
  public CSSExpression addURI (@Nonnull @Nonempty final String sURI)
  {
    return addMember (new CSSExpressionMemberTermURI (sURI));
  }

  /**
   * Shortcut method to add a URI value
   *
   * @param nIndex
   *        The index where the member should be added. Must be &ge; 0.
   * @param sURI
   *        The value to be added. May neither be <code>null</code> nor empty
   * @return this
   */
  @Nonnull
  public CSSExpression addURI (@Nonnegative final int nIndex, @Nonnull @Nonempty final String sURI)
  {
    return addMember (nIndex, new CSSExpressionMemberTermURI (sURI));
  }

  /**
   * Remove the passed expression member
   *
   * @param aMember
   *        The member to be removed. May be <code>null</code>.
   * @return {@link EChange}
   */
  @Nonnull
  public EChange removeMember (@Nullable final ICSSExpressionMember aMember)
  {
    return EChange.valueOf (m_aMembers.remove (aMember));
  }

  /**
   * Remove the expression member at the specified in
   *
   * @param nMemberIndex
   *        the index of the member to be removed. May not be &lt; 0.
   * @return {@link EChange}
   */
  @Nonnull
  public EChange removeMember (@Nonnegative final int nMemberIndex)
  {
    return m_aMembers.removeAtIndex (nMemberIndex);
  }

  /**
   * Remove all members.
   *
   * @return {@link EChange#CHANGED} if any member was removed,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   * @since 3.7.3
   */
  @Nonnull
  public EChange removeAllMembers ()
  {
    return m_aMembers.removeAll ();
  }

  /**
   * @return A copy of all contained expression members. Never <code>null</code>
   *         .
   */
  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <ICSSExpressionMember> getAllMembers ()
  {
    return m_aMembers.getClone ();
  }

  /**
   * Get the expression member at the specified index.
   *
   * @param nIndex
   *        The index to be retrieved
   * @return <code>null</code> if an invalid member index was passed.
   */
  @Nullable
  public ICSSExpressionMember getMemberAtIndex (@Nonnegative final int nIndex)
  {
    return m_aMembers.getAtIndex (nIndex);
  }

  /**
   * @return The number of expression members present. Always &ge; 0.
   */
  @Nonnegative
  public int getMemberCount ()
  {
    return m_aMembers.size ();
  }

  /**
   * @return A list with all expression members that are of type
   *         {@link CSSExpressionMemberTermSimple}
   */
  @Nonnull
  public ICommonsList <CSSExpressionMemberTermSimple> getAllSimpleMembers ()
  {
    return m_aMembers.getAllInstanceOf (CSSExpressionMemberTermSimple.class);
  }

  @Nonnull
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    final StringBuilder aSB = new StringBuilder ();
    boolean bPrevWasOperator = false;
    for (final ICSSExpressionMember aMember : m_aMembers)
    {
      final boolean bIsOp = aMember instanceof ECSSExpressionOperator;
      if (!bIsOp && !bPrevWasOperator && aSB.length () > 0)
      {
        // The space is required for separating values like "solid 1px black"
        aSB.append (' ');
      }
      aSB.append (aMember.getAsCSSString (aSettings, nIndentLevel));
      bPrevWasOperator = bIsOp;
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
    final CSSExpression rhs = (CSSExpression) o;
    return m_aMembers.equals (rhs.m_aMembers);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aMembers).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("members", m_aMembers)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .toString ();
  }

  /**
   * Create a CSS expression only containing a text value
   *
   * @param sValue
   *        The value to be wrapped in an expression
   * @return The CSS expression to be used.
   */
  @Nonnull
  public static CSSExpression createSimple (@Nonnull @Nonempty final String sValue)
  {
    return new CSSExpression ().addTermSimple (sValue);
  }

  /**
   * Create a CSS expression only containing a string
   *
   * @param sValue
   *        The value to be wrapped in a string
   * @return The CSS expression to be used.
   */
  @Nonnull
  public static CSSExpression createString (@Nonnull @Nonempty final String sValue)
  {
    return new CSSExpression ().addString (sValue);
  }

  /**
   * Create a CSS expression only containing a numeric value
   *
   * @param nValue
   *        The value to be wrapped in an expression
   * @return The CSS expression to be used.
   */
  @Nonnull
  public static CSSExpression createNumber (final int nValue)
  {
    return new CSSExpression ().addNumber (nValue);
  }

  /**
   * Create a CSS expression only containing a numeric value
   *
   * @param nValue
   *        The value to be wrapped in an expression
   * @return The CSS expression to be used.
   */
  @Nonnull
  public static CSSExpression createNumber (final long nValue)
  {
    return new CSSExpression ().addNumber (nValue);
  }

  /**
   * Create a CSS expression only containing a numeric value
   *
   * @param fValue
   *        The value to be wrapped in an expression
   * @return The CSS expression to be used.
   */
  @Nonnull
  public static CSSExpression createNumber (final float fValue)
  {
    return new CSSExpression ().addNumber (fValue);
  }

  /**
   * Create a CSS expression only containing a numeric value
   *
   * @param dValue
   *        The value to be wrapped in an expression
   * @return The CSS expression to be used.
   */
  @Nonnull
  public static CSSExpression createNumber (final double dValue)
  {
    return new CSSExpression ().addNumber (dValue);
  }

  /**
   * Create a CSS expression only containing a URI
   *
   * @param sURI
   *        The URI to be wrapped in an expression
   * @return The CSS expression to be used.
   */
  @Nonnull
  public static CSSExpression createURI (@Nonnull @Nonempty final String sURI)
  {
    return new CSSExpression ().addURI (sURI);
  }
}
