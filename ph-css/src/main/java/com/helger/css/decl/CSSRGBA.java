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
import com.helger.commons.lang.ICloneable;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.ICSSWriteable;
import com.helger.css.ICSSWriterSettings;
import com.helger.css.propertyvalue.CCSSValue;
import com.helger.css.utils.CSSColorHelper;

/**
 * Represents a single RGBA color value (red, green, blue, opacity)
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSRGBA implements ICSSWriteable, ICSSColor, ICloneable <CSSRGBA>
{
  private String m_sRed;
  private String m_sGreen;
  private String m_sBlue;
  private String m_sOpacity;

  /**
   * Copy constructor
   *
   * @param aOther
   *        The object to copy the data from. May not be <code>null</code>.
   */
  public CSSRGBA (@Nonnull final CSSRGBA aOther)
  {
    this (aOther.getRed (), aOther.getGreen (), aOther.getBlue (), aOther.getOpacity ());
  }

  /**
   * Constructor
   *
   * @param aOther
   *        The RGB value to use as the basis. May not be <code>null</code>.
   * @param fOpacity
   *        Opacity part. Is fitted to a value between 0 and 1.
   * @since 3.8.3
   */
  public CSSRGBA (@Nonnull final CSSRGB aOther, final float fOpacity)
  {
    this (aOther, Float.toString (CSSColorHelper.getOpacityToUse (fOpacity)));
  }

  /**
   * Constructor
   *
   * @param aOther
   *        The RGB value to use as the basis. May not be <code>null</code>.
   * @param sOpacity
   *        Opacity part. May neither be <code>null</code> nor empty.
   * @since 3.8.3
   */
  public CSSRGBA (@Nonnull final CSSRGB aOther, @Nonnull @Nonempty final String sOpacity)
  {
    this (aOther.getRed (), aOther.getGreen (), aOther.getBlue (), sOpacity);
  }

  /**
   * Constructor
   *
   * @param nRed
   *        Red part. Is fitted to a value between 0 and 255.
   * @param nGreen
   *        Green part. Is fitted to a value between 0 and 255.
   * @param nBlue
   *        Blue part. Is fitted to a value between 0 and 255.
   * @param fOpacity
   *        Opacity part. Is fitted to a value between 0 and 1.
   */
  public CSSRGBA (final int nRed, final int nGreen, final int nBlue, final float fOpacity)
  {
    this (Integer.toString (CSSColorHelper.getRGBValue (nRed)),
          Integer.toString (CSSColorHelper.getRGBValue (nGreen)),
          Integer.toString (CSSColorHelper.getRGBValue (nBlue)),
          Float.toString (CSSColorHelper.getOpacityToUse (fOpacity)));
  }

  /**
   * Constructor
   *
   * @param sRed
   *        Red part. May neither be <code>null</code> nor empty.
   * @param sGreen
   *        Green part. May neither be <code>null</code> nor empty.
   * @param sBlue
   *        Blue part. May neither be <code>null</code> nor empty.
   * @param sOpacity
   *        Opacity part. May neither be <code>null</code> nor empty.
   */
  public CSSRGBA (@Nonnull @Nonempty final String sRed,
                  @Nonnull @Nonempty final String sGreen,
                  @Nonnull @Nonempty final String sBlue,
                  @Nonnull @Nonempty final String sOpacity)
  {
    setRed (sRed);
    setGreen (sGreen);
    setBlue (sBlue);
    setOpacity (sOpacity);
  }

  /**
   * @return red part
   */
  @Nonnull
  @Nonempty
  public String getRed ()
  {
    return m_sRed;
  }

  @Nonnull
  public CSSRGBA setRed (@Nonnull @Nonempty final String sRed)
  {
    ValueEnforcer.notEmpty (sRed, "Red");

    m_sRed = sRed;
    return this;
  }

  /**
   * @return green part
   */
  @Nonnull
  @Nonempty
  public String getGreen ()
  {
    return m_sGreen;
  }

  @Nonnull
  public CSSRGBA setGreen (@Nonnull @Nonempty final String sGreen)
  {
    ValueEnforcer.notEmpty (sGreen, "Green");

    m_sGreen = sGreen;
    return this;
  }

  /**
   * @return blue part
   */
  @Nonnull
  @Nonempty
  public String getBlue ()
  {
    return m_sBlue;
  }

  @Nonnull
  public CSSRGBA setBlue (@Nonnull @Nonempty final String sBlue)
  {
    ValueEnforcer.notEmpty (sBlue, "Blue");

    m_sBlue = sBlue;
    return this;
  }

  /**
   * @return opacity part
   */
  @Nonnull
  @Nonempty
  public String getOpacity ()
  {
    return m_sOpacity;
  }

  @Nonnull
  public CSSRGBA setOpacity (@Nonnull @Nonempty final String sOpacity)
  {
    ValueEnforcer.notEmpty (sOpacity, "Opacity");

    m_sOpacity = sOpacity;
    return this;
  }

  /**
   * @return This value as RGB value without the opacity. Never
   *         <code>null</code>.
   * @since 3.8.3
   */
  @Nonnull
  public CSSRGB getAsRGB ()
  {
    return new CSSRGB (m_sRed, m_sGreen, m_sBlue);
  }

  /**
   * {@inheritDoc}
   *
   * @since 3.8.3
   */
  @Nonnull
  @Nonempty
  public String getAsString ()
  {
    return CCSSValue.PREFIX_RGBA_OPEN +
           m_sRed +
           ',' +
           m_sGreen +
           ',' +
           m_sBlue +
           ',' +
           m_sOpacity +
           CCSSValue.SUFFIX_RGBA_CLOSE;
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    return getAsString ();
  }

  /**
   * {@inheritDoc}
   *
   * @since 3.8.3
   */
  @Nonnull
  public CSSRGBA getClone ()
  {
    return new CSSRGBA (this);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final CSSRGBA rhs = (CSSRGBA) o;
    return m_sRed.equals (rhs.m_sRed) &&
           m_sGreen.equals (rhs.m_sGreen) &&
           m_sBlue.equals (rhs.m_sBlue) &&
           m_sOpacity.equals (rhs.m_sOpacity);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sRed)
                                       .append (m_sGreen)
                                       .append (m_sBlue)
                                       .append (m_sOpacity)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("red", m_sRed)
                                       .append ("green", m_sGreen)
                                       .append ("blue", m_sBlue)
                                       .append ("opacity", m_sOpacity)
                                       .getToString ();
  }
}
