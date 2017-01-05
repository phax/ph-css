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
package com.helger.css.propertyvalue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CCSS;
import com.helger.css.ECSSUnit;

/**
 * This class encapsulates a single numeric value and a unit ({@link ECSSUnit}).
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSSimpleValueWithUnit implements Serializable
{
  private BigDecimal m_aValue;
  private ECSSUnit m_eUnit;

  /**
   * Constructor
   *
   * @param aValue
   *        Numeric value. May not be <code>null</code>.
   * @param eUnit
   *        CSS unit to use. May not be <code>null</code>.
   */
  public CSSSimpleValueWithUnit (@Nonnull final BigDecimal aValue, @Nonnull final ECSSUnit eUnit)
  {
    setValue (aValue);
    setUnit (eUnit);
  }

  /**
   * Constructor
   *
   * @param dValue
   *        Numeric value
   * @param eUnit
   *        CSS unit to use. May not be <code>null</code>.
   */
  public CSSSimpleValueWithUnit (final double dValue, @Nonnull final ECSSUnit eUnit)
  {
    this (BigDecimal.valueOf (dValue), eUnit);
  }

  /**
   * Set the numerical value.
   *
   * @param aValue
   *        The new value to set. May not be <code>null</code>.
   * @return this
   * @since 3.7.3
   */
  @Nonnull
  public CSSSimpleValueWithUnit setValue (@Nonnull final BigDecimal aValue)
  {
    m_aValue = ValueEnforcer.notNull (aValue, "Value");
    return this;
  }

  /**
   * Set the numerical value.
   *
   * @param dValue
   *        The new value to set.
   * @return this
   * @since 3.7.3
   */
  @Nonnull
  public CSSSimpleValueWithUnit setValue (final double dValue)
  {
    return setValue (BigDecimal.valueOf (dValue));
  }

  /**
   * @return The numeric value as a decimal value (as passed in the constructor)
   * @since 3.7.3
   */
  @Nonnull
  public BigDecimal getAsBigDecimalValue ()
  {
    return m_aValue;
  }

  /**
   * @return The numeric value as a decimal value (as passed in the constructor)
   */
  public double getValue ()
  {
    return m_aValue.doubleValue ();
  }

  /**
   * @return The numeric value as an int value - no check for validity is
   *         performed
   */
  public int getAsIntValue ()
  {
    return m_aValue.intValue ();
  }

  /**
   * @return The numeric value as a long value - no check for validity is
   *         performed
   */
  public long getAsLongValue ()
  {
    return m_aValue.longValue ();
  }

  /**
   * Set the unit type.
   *
   * @param eUnit
   *        The new unit to set. May not be <code>null</code>.
   * @return this
   * @since 3.7.3
   */
  @Nonnull
  public CSSSimpleValueWithUnit setUnit (@Nonnull final ECSSUnit eUnit)
  {
    m_eUnit = ValueEnforcer.notNull (eUnit, "Unit");
    return this;
  }

  /**
   * @return The CSS unit of this value. Never <code>null</code>.
   */
  @Nonnull
  public ECSSUnit getUnit ()
  {
    return m_eUnit;
  }

  /**
   * @return The formatted string value of this item. Neither <code>null</code>
   *         nor empty.
   * @since 3.7.3
   */
  @Nonnull
  @Nonempty
  public String getFormatted ()
  {
    return m_eUnit.format (m_aValue);
  }

  /**
   * Get a new object with the same unit but an added value.
   *
   * @param aDelta
   *        The delta to be added. May not be <code>null</code>.
   * @return A new object. Never <code>null</code>.
   * @since 3.7.3
   */
  @Nonnull
  @CheckReturnValue
  public CSSSimpleValueWithUnit add (@Nonnull final BigDecimal aDelta)
  {
    return new CSSSimpleValueWithUnit (m_aValue.add (aDelta), m_eUnit);
  }

  /**
   * Get a new object with the same unit but an added value.
   *
   * @param dDelta
   *        The delta to be added.
   * @return A new object. Never <code>null</code>.
   */
  @Nonnull
  @CheckReturnValue
  public CSSSimpleValueWithUnit add (final double dDelta)
  {
    return add (BigDecimal.valueOf (dDelta));
  }

  /**
   * Get a new object with the same unit but a subtracted value.
   *
   * @param aDelta
   *        The delta to be subtracted. May not be <code>null</code>.
   * @return A new object. Never <code>null</code>.
   * @since 3.7.3
   */
  @Nonnull
  @CheckReturnValue
  public CSSSimpleValueWithUnit substract (@Nonnull final BigDecimal aDelta)
  {
    return new CSSSimpleValueWithUnit (m_aValue.subtract (aDelta), m_eUnit);
  }

  /**
   * Get a new object with the same unit but a subtracted value.
   *
   * @param dDelta
   *        The delta to be subtracted.
   * @return A new object. Never <code>null</code>.
   */
  @Nonnull
  @CheckReturnValue
  public CSSSimpleValueWithUnit substract (final double dDelta)
  {
    return substract (BigDecimal.valueOf (dDelta));
  }

  /**
   * Get a new object with the same unit but a multiplied value.
   *
   * @param aValue
   *        The value to be multiply with this value. May not be
   *        <code>null</code>.
   * @return A new object. Never <code>null</code>.
   * @since 3.7.3
   */
  @Nonnull
  @CheckReturnValue
  public CSSSimpleValueWithUnit multiply (@Nonnull final BigDecimal aValue)
  {
    return new CSSSimpleValueWithUnit (m_aValue.multiply (aValue), m_eUnit);
  }

  /**
   * Get a new object with the same unit but a multiplied value.
   *
   * @param dValue
   *        The value to be multiply with this value.
   * @return A new object. Never <code>null</code>.
   */
  @Nonnull
  @CheckReturnValue
  public CSSSimpleValueWithUnit multiply (final double dValue)
  {
    return multiply (BigDecimal.valueOf (dValue));
  }

  /**
   * Get a new object with the same unit but an divided value.
   *
   * @param aDivisor
   *        The divisor to use. May not be <code>null</code>.
   * @param nScale
   *        The maximum fraction digits to use.
   * @param eRoundingMode
   *        The rounding mode to use. May not be <code>null</code>.
   * @return A new object. Never <code>null</code>.
   * @since 3.7.3
   */
  @Nonnull
  @CheckReturnValue
  public CSSSimpleValueWithUnit divide (@Nonnull final BigDecimal aDivisor,
                                        @Nonnegative final int nScale,
                                        @Nonnull final RoundingMode eRoundingMode)
  {
    return new CSSSimpleValueWithUnit (m_aValue.divide (aDivisor, nScale, eRoundingMode), m_eUnit);
  }

  /**
   * Get a new object with the same unit but an divided value. By default
   * {@link CCSS#CSS_MAXIMUM_FRACTION_DIGITS} is used as scale and
   * {@link RoundingMode#HALF_UP} is used as rounding mode.
   *
   * @param aDivisor
   *        The divisor to use. May not be <code>null</code>.
   * @return A new object. Never <code>null</code>.
   * @since 3.7.3
   */
  @Nonnull
  @CheckReturnValue
  public CSSSimpleValueWithUnit divide (@Nonnull final BigDecimal aDivisor)
  {
    return divide (aDivisor, CCSS.CSS_MAXIMUM_FRACTION_DIGITS, RoundingMode.HALF_UP);
  }

  /**
   * Get a new object with the same unit but an divided value.
   *
   * @param dDivisor
   *        The divisor to use.
   * @return A new object. Never <code>null</code>.
   */
  @Nonnull
  @CheckReturnValue
  public CSSSimpleValueWithUnit divide (final double dDivisor)
  {
    return divide (BigDecimal.valueOf (dDivisor));
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final CSSSimpleValueWithUnit rhs = (CSSSimpleValueWithUnit) o;
    return EqualsHelper.equals (m_aValue, rhs.m_aValue) && m_eUnit.equals (rhs.m_eUnit);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aValue).append (m_eUnit).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("value", m_aValue).append ("unit", m_eUnit).toString ();
  }
}
