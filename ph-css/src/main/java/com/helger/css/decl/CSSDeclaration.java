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

import java.util.Locale;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.annotation.Nonnegative;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.annotation.style.ReturnsMutableObject;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.css.CCSS;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;
import com.helger.css.property.ECSSProperty;

/**
 * Represents a single element in a CSS style rule. (eg. <code>color:red;</code>
 * or <code>background:uri(a.gif) !important;</code>)<br>
 * Instances of this class are mutable since 3.7.4.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSDeclaration implements ICSSSourceLocationAware, ICSSPageRuleMember
{
  public static final boolean DEFAULT_IMPORTANT = false;

  private String m_sProperty;
  private CSSExpression m_aExpression;
  private boolean m_bIsImportant;
  private CSSSourceLocation m_aSourceLocation;

  /**
   * Constructor for non-important values.
   *
   * @param sProperty
   *        The name of the property. E.g. "color". May neither be
   *        <code>null</code> nor empty. The property value is automatically
   *        lowercased!
   * @param aExpression
   *        The value of the property. May not be <code>null</code>.
   */
  public CSSDeclaration (@NonNull @Nonempty final String sProperty, @NonNull final CSSExpression aExpression)
  {
    this (sProperty, aExpression, DEFAULT_IMPORTANT);
  }

  /**
   * Constructor.
   *
   * @param sProperty
   *        The name of the property. E.g. "color". May neither be
   *        <code>null</code> nor empty. The property value is automatically
   *        lowercased!
   * @param aExpression
   *        The value of the property. May not be <code>null</code>.
   * @param bIsImportant
   *        <code>true</code> if it is important, <code>false</code> if not.
   */
  public CSSDeclaration (@NonNull @Nonempty final String sProperty, @NonNull final CSSExpression aExpression, final boolean bIsImportant)
  {
    setProperty (sProperty);
    setExpression (aExpression);
    setImportant (bIsImportant);
  }

  /**
   * @return The property of this declaration (e.g. "color" or "margin-top").
   *         The string is always lowercase. Never <code>null</code>.
   */
  @NonNull
  @Nonempty
  public final String getProperty ()
  {
    return m_sProperty;
  }

  @NonNull
  private static String _unifyProperty (@NonNull final String sProperty)
  {
    // CSS variables are case sensitive (see issue 63)
    if (sProperty.startsWith ("--"))
      return sProperty;
    return sProperty.toLowerCase (Locale.ROOT);
  }

  /**
   * Check if this declaration has the specified property. The comparison is
   * case insensitive!
   *
   * @param sProperty
   *        The property to check. May not be <code>null</code>.
   * @return <code>true</code> if this declaration has the specified property.
   * @see #hasProperty(ECSSProperty)
   * @since 6.0.0
   */
  public final boolean hasProperty (@NonNull final String sProperty)
  {
    ValueEnforcer.notNull (sProperty, "Property");
    return m_sProperty.equals (_unifyProperty (sProperty));
  }

  /**
   * Check if this declaration has the specified property. The comparison is
   * case insensitive!
   *
   * @param eProperty
   *        The property to check. May not be <code>null</code>.
   * @return <code>true</code> if this declaration has the specified property.
   * @see #hasProperty(String)
   * @since 6.0.0
   */
  public final boolean hasProperty (@NonNull final ECSSProperty eProperty)
  {
    ValueEnforcer.notNull (eProperty, "Property");
    return hasProperty (eProperty.getName ());
  }

  /**
   * Set the property of this CSS value (e.g. <code>background-color</code>).
   *
   * @param sProperty
   *        The CSS property name to set. May neither be <code>null</code> nor
   *        empty. The property value is automatically lowercased!
   * @return this
   * @since 3.7.4
   */
  @NonNull
  public final CSSDeclaration setProperty (@NonNull @Nonempty final String sProperty)
  {
    ValueEnforcer.notEmpty (sProperty, "Property");
    m_sProperty = _unifyProperty (sProperty);
    return this;
  }

  /**
   * Set the property of this CSS value (e.g. <code>background-color</code>).
   *
   * @param eProperty
   *        The CSS property to set. May not be <code>null</code>.
   * @return this
   * @since 3.7.4
   */
  @NonNull
  public final CSSDeclaration setProperty (@NonNull final ECSSProperty eProperty)
  {
    ValueEnforcer.notNull (eProperty, "Property");
    return setProperty (eProperty.getName ());
  }

  /**
   * @return The expression of this declaration (e.g. "red" or "25px" or "25px
   *         10px 25px 9px") as a structured value. Never <code>null</code>.
   */
  @NonNull
  @ReturnsMutableObject
  public final CSSExpression getExpression ()
  {
    return m_aExpression;
  }

  /**
   * Get the CSS expression, but without an eventual `!important` indicator!
   *
   * @return The CSS expression as a parsable String. Never <code>null</code>.
   */
  @NonNull
  public final String getExpressionAsCSSString ()
  {
    return m_aExpression.getAsCSSString ();
  }

  /**
   * Set the expression (= value) of this declaration.
   *
   * @param aExpression
   *        The value of the property. May not be <code>null</code>.
   * @return this
   * @since 3.7.4
   */
  @NonNull
  public final CSSDeclaration setExpression (@NonNull final CSSExpression aExpression)
  {
    m_aExpression = ValueEnforcer.notNull (aExpression, "Expression");
    return this;
  }

  /**
   * @return <code>true</code> if this declaration is important (
   *         <code>!important</code>) or <code>false</code> if not.
   */
  public final boolean isImportant ()
  {
    return m_bIsImportant;
  }

  /**
   * Set the important flag of this value.
   *
   * @param bIsImportant
   *        <code>true</code> to mark it important, <code>false</code> to remove
   *        it.
   * @return this
   * @since 3.7.4
   */
  @NonNull
  public final CSSDeclaration setImportant (final boolean bIsImportant)
  {
    m_bIsImportant = bIsImportant;
    return this;
  }

  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    return m_sProperty +
           CCSS.SEPARATOR_PROPERTY_VALUE +
           m_aExpression.getAsCSSString (aSettings, nIndentLevel) +
           (m_bIsImportant ? CCSS.IMPORTANT_SUFFIX : "");
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
    final CSSDeclaration rhs = (CSSDeclaration) o;
    return m_sProperty.equals (rhs.m_sProperty) && m_aExpression.equals (rhs.m_aExpression) && m_bIsImportant == rhs.m_bIsImportant;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sProperty).append (m_aExpression).append (m_bIsImportant).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("property", m_sProperty)
                                       .append ("expression", m_aExpression)
                                       .append ("important", m_bIsImportant)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
