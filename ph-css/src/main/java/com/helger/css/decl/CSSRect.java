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
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.ICSSWriteable;
import com.helger.css.ICSSWriterSettings;
import com.helger.css.propertyvalue.CCSSValue;

/**
 * Represents a single CSS rectangle
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSRect implements ICSSWriteable
{
  private String m_sTop;
  private String m_sRight;
  private String m_sBottom;
  private String m_sLeft;

  /**
   * Copy constructor
   *
   * @param aOther
   *        The object to copy the data from. May not be <code>null</code>.
   */
  public CSSRect (@Nonnull final CSSRect aOther)
  {
    this (aOther.getTop (), aOther.getRight (), aOther.getBottom (), aOther.getLeft ());
  }

  /**
   * Constructor
   *
   * @param sTop
   *        Top coordinate. May neither be <code>null</code> nor empty.
   * @param sRight
   *        Tight coordinate. May neither be <code>null</code> nor empty.
   * @param sBottom
   *        Bottom coordinate. May neither be <code>null</code> nor empty.
   * @param sLeft
   *        Left coordinate. May neither be <code>null</code> nor empty.
   */
  public CSSRect (@Nonnull @Nonempty final String sTop,
                  @Nonnull @Nonempty final String sRight,
                  @Nonnull @Nonempty final String sBottom,
                  @Nonnull @Nonempty final String sLeft)
  {
    setTop (sTop);
    setRight (sRight);
    setBottom (sBottom);
    setLeft (sLeft);
  }

  /**
   * @return top part. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getTop ()
  {
    return m_sTop;
  }

  /**
   * Set the top coordinate.
   *
   * @param sTop
   *        May neither be <code>null</code> nor empty.
   * @return this
   */
  @Nonnull
  public CSSRect setTop (@Nonnull @Nonempty final String sTop)
  {
    ValueEnforcer.notEmpty (sTop, "Top");

    m_sTop = sTop;
    return this;
  }

  /**
   * @return right part. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getRight ()
  {
    return m_sRight;
  }

  /**
   * Set the right coordinate.
   *
   * @param sRight
   *        May neither be <code>null</code> nor empty.
   * @return this
   */
  @Nonnull
  public CSSRect setRight (@Nonnull @Nonempty final String sRight)
  {
    ValueEnforcer.notEmpty (sRight, "Right");

    m_sRight = sRight;
    return this;
  }

  /**
   * @return bottom part. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getBottom ()
  {
    return m_sBottom;
  }

  /**
   * Set the bottom coordinate.
   *
   * @param sBottom
   *        May neither be <code>null</code> nor empty.
   * @return this
   */
  @Nonnull
  public CSSRect setBottom (@Nonnull @Nonempty final String sBottom)
  {
    ValueEnforcer.notEmpty (sBottom, "Bottom");

    m_sBottom = sBottom;
    return this;
  }

  /**
   * @return left part. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getLeft ()
  {
    return m_sLeft;
  }

  /**
   * Set the left coordinate.
   *
   * @param sLeft
   *        May neither be <code>null</code> nor empty.
   * @return this
   */
  @Nonnull
  public CSSRect setLeft (@Nonnull @Nonempty final String sLeft)
  {
    ValueEnforcer.notEmpty (sLeft, "Left");

    m_sLeft = sLeft;
    return this;
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    return CCSSValue.PREFIX_RECT_OPEN +
           m_sTop +
           ',' +
           m_sRight +
           ',' +
           m_sBottom +
           ',' +
           m_sLeft +
           CCSSValue.SUFFIX_RECT_CLOSE;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final CSSRect rhs = (CSSRect) o;
    return m_sTop.equals (rhs.m_sTop) &&
           m_sRight.equals (rhs.m_sRight) &&
           m_sBottom.equals (rhs.m_sBottom) &&
           m_sLeft.equals (rhs.m_sLeft);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sTop)
                                       .append (m_sRight)
                                       .append (m_sBottom)
                                       .append (m_sLeft)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("top", m_sTop)
                                       .append ("right", m_sRight)
                                       .append ("bottom", m_sBottom)
                                       .append ("left", m_sLeft)
                                       .getToString ();
  }
}
