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

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.annotation.Nonnegative;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.equals.EqualsHelper;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.css.CCSS;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriteable;
import com.helger.css.ICSSWriterSettings;
import com.helger.css.media.ECSSMediaExpressionFeature;

/**
 * Represents a single media expression. Besides the classic <code>(feature)</code> and
 * <code>(feature: value)</code> forms, the Media Queries Level 4 range context is supported:
 * <code>(feature &gt;= value)</code>, <code>(value &lt;= feature)</code> and
 * <code>(value &lt;= feature &lt;= value)</code>.
 *
 * @see <a href="https://www.w3.org/TR/mediaqueries-4/#mq-range-context">Media Queries Level 4 -
 *      Range context</a>
 */
@NotThreadSafe
public class CSSMediaExpression implements ICSSWriteable, ICSSSourceLocationAware
{
  private final CSSExpression m_aRangeLeftValue;
  private final ECSSMediaRangeOperator m_eRangeLeftOperator;
  private final String m_sFeature;
  private final ECSSMediaRangeOperator m_eRangeRightOperator;
  private final CSSExpression m_aValue;
  private CSSSourceLocation m_aSourceLocation;

  /**
   * Constructor for a feature only expression like <code>(color)</code>.
   *
   * @param eFeature
   *        The media feature. May not be <code>null</code>.
   */
  public CSSMediaExpression (@NonNull final ECSSMediaExpressionFeature eFeature)
  {
    this (eFeature.getName ());
  }

  /**
   * Constructor for a feature only expression like <code>(color)</code>.
   *
   * @param sFeature
   *        The media feature name. May neither be <code>null</code> nor empty.
   */
  public CSSMediaExpression (@NonNull @Nonempty final String sFeature)
  {
    this (sFeature, null);
  }

  /**
   * Constructor for a feature with value like <code>(min-width: 600px)</code>.
   *
   * @param eFeature
   *        The media feature. May not be <code>null</code>.
   * @param aValue
   *        The value of the feature. May be <code>null</code>.
   */
  public CSSMediaExpression (@NonNull final ECSSMediaExpressionFeature eFeature, @Nullable final CSSExpression aValue)
  {
    this (eFeature.getName (), aValue);
  }

  /**
   * Constructor for a feature with value like <code>(min-width: 600px)</code>.
   *
   * @param sFeature
   *        The media feature name. May neither be <code>null</code> nor empty.
   * @param aValue
   *        The value of the feature. May be <code>null</code>.
   */
  public CSSMediaExpression (@NonNull @Nonempty final String sFeature, @Nullable final CSSExpression aValue)
  {
    this (null, null, sFeature, null, aValue);
  }

  /**
   * Constructor for a range expression with the feature in front like
   * <code>(width &gt;= 600px)</code>.
   *
   * @param eFeature
   *        The media feature. May not be <code>null</code>.
   * @param eOperator
   *        The comparison operator. May not be <code>null</code>.
   * @param aValue
   *        The value to compare the feature with. May not be <code>null</code>.
   */
  public CSSMediaExpression (@NonNull final ECSSMediaExpressionFeature eFeature,
                             @NonNull final ECSSMediaRangeOperator eOperator,
                             @NonNull final CSSExpression aValue)
  {
    this (eFeature.getName (), eOperator, aValue);
  }

  /**
   * Constructor for a range expression with the feature in front like
   * <code>(width &gt;= 600px)</code>.
   *
   * @param sFeature
   *        The media feature name. May neither be <code>null</code> nor empty.
   * @param eOperator
   *        The comparison operator. May not be <code>null</code>.
   * @param aValue
   *        The value to compare the feature with. May not be <code>null</code>.
   */
  public CSSMediaExpression (@NonNull @Nonempty final String sFeature,
                             @NonNull final ECSSMediaRangeOperator eOperator,
                             @NonNull final CSSExpression aValue)
  {
    this (null, null, sFeature, ValueEnforcer.notNull (eOperator, "Operator"), ValueEnforcer.notNull (aValue, "Value"));
  }

  /**
   * Constructor for a range expression with the value in front like
   * <code>(600px &lt;= width)</code>.
   *
   * @param aLeftValue
   *        The value to compare the feature with. May not be <code>null</code>.
   * @param eLeftOperator
   *        The comparison operator. May not be <code>null</code>.
   * @param sFeature
   *        The media feature name. May neither be <code>null</code> nor empty.
   */
  public CSSMediaExpression (@NonNull final CSSExpression aLeftValue,
                             @NonNull final ECSSMediaRangeOperator eLeftOperator,
                             @NonNull @Nonempty final String sFeature)
  {
    this (ValueEnforcer.notNull (aLeftValue, "LeftValue"),
          ValueEnforcer.notNull (eLeftOperator, "LeftOperator"),
          sFeature,
          null,
          null);
  }

  /**
   * Generic constructor covering all forms. Use it directly for the two-sided range like
   * <code>(400px &lt;= width &lt;= 600px)</code>.
   *
   * @param aLeftValue
   *        The value in front of the feature. May be <code>null</code>. Must be present if and only if
   *        <code>eLeftOperator</code> is present.
   * @param eLeftOperator
   *        The comparison operator between the left value and the feature. May be <code>null</code>.
   * @param sFeature
   *        The media feature name. May neither be <code>null</code> nor empty.
   * @param eRightOperator
   *        The comparison operator between the feature and the value. May be <code>null</code>, in
   *        which case a non-<code>null</code> value is written in the classic
   *        <code>feature: value</code> form.
   * @param aValue
   *        The value after the feature. May be <code>null</code> unless <code>eRightOperator</code>
   *        is present.
   */
  public CSSMediaExpression (@Nullable final CSSExpression aLeftValue,
                             @Nullable final ECSSMediaRangeOperator eLeftOperator,
                             @NonNull @Nonempty final String sFeature,
                             @Nullable final ECSSMediaRangeOperator eRightOperator,
                             @Nullable final CSSExpression aValue)
  {
    ValueEnforcer.notEmpty (sFeature, "Feature");
    ValueEnforcer.isTrue ((aLeftValue == null) == (eLeftOperator == null),
                          "Left value and left operator must be present or absent together");
    ValueEnforcer.isTrue (eRightOperator == null || aValue != null, "A right operator requires a value");
    m_aRangeLeftValue = aLeftValue;
    m_eRangeLeftOperator = eLeftOperator;
    m_sFeature = sFeature;
    m_eRangeRightOperator = eRightOperator;
    m_aValue = aValue;
  }

  /**
   * @return The value in front of the feature in the range context (<code>600px</code> in
   *         <code>(600px &lt;= width)</code>). May be <code>null</code>.
   */
  @Nullable
  public final CSSExpression getRangeLeftValue ()
  {
    return m_aRangeLeftValue;
  }

  /**
   * @return The comparison operator between the left value and the feature. May be <code>null</code>.
   */
  @Nullable
  public final ECSSMediaRangeOperator getRangeLeftOperator ()
  {
    return m_eRangeLeftOperator;
  }

  /**
   * @return The media feature name. Neither <code>null</code> nor empty.
   */
  @NonNull
  @Nonempty
  public final String getFeature ()
  {
    return m_sFeature;
  }

  /**
   * @return The comparison operator between the feature and the value in the range context
   *         (<code>&gt;=</code> in <code>(width &gt;= 600px)</code>). <code>null</code> if the value
   *         is absent or uses the classic <code>feature: value</code> form.
   */
  @Nullable
  public final ECSSMediaRangeOperator getRangeRightOperator ()
  {
    return m_eRangeRightOperator;
  }

  /**
   * @return The value after the feature - either after the colon of the classic form or after the
   *         right range operator. May be <code>null</code>.
   */
  @Nullable
  public final CSSExpression getValue ()
  {
    return m_aValue;
  }

  /**
   * @return <code>true</code> if this expression uses the Media Queries Level 4 range context,
   *         <code>false</code> for the classic <code>(feature)</code> and
   *         <code>(feature: value)</code> forms.
   */
  public final boolean isRangeContext ()
  {
    return m_eRangeLeftOperator != null || m_eRangeRightOperator != null;
  }

  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    final StringBuilder aSB = new StringBuilder ();
    aSB.append ('(');
    if (m_eRangeLeftOperator != null)
    {
      aSB.append (m_aRangeLeftValue.getAsCSSString (aSettings, nIndentLevel))
         .append (' ')
         .append (m_eRangeLeftOperator.getAsCSSString (aSettings, nIndentLevel))
         .append (' ');
    }
    aSB.append (m_sFeature);
    if (m_eRangeRightOperator != null)
    {
      aSB.append (' ')
         .append (m_eRangeRightOperator.getAsCSSString (aSettings, nIndentLevel))
         .append (' ')
         .append (m_aValue.getAsCSSString (aSettings, nIndentLevel));
    }
    else
      if (m_aValue != null)
        aSB.append (CCSS.SEPARATOR_PROPERTY_VALUE).append (m_aValue.getAsCSSString (aSettings, nIndentLevel));
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
    final CSSMediaExpression rhs = (CSSMediaExpression) o;
    return EqualsHelper.equals (m_aRangeLeftValue, rhs.m_aRangeLeftValue) &&
           EqualsHelper.equals (m_eRangeLeftOperator, rhs.m_eRangeLeftOperator) &&
           m_sFeature.equals (rhs.m_sFeature) &&
           EqualsHelper.equals (m_eRangeRightOperator, rhs.m_eRangeRightOperator) &&
           EqualsHelper.equals (m_aValue, rhs.m_aValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aRangeLeftValue)
                                       .append (m_eRangeLeftOperator)
                                       .append (m_sFeature)
                                       .append (m_eRangeRightOperator)
                                       .append (m_aValue)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("rangeLeftValue", m_aRangeLeftValue)
                                       .appendIfNotNull ("rangeLeftOperator", m_eRangeLeftOperator)
                                       .append ("feature", m_sFeature)
                                       .appendIfNotNull ("rangeRightOperator", m_eRangeRightOperator)
                                       .appendIfNotNull ("value", m_aValue)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
