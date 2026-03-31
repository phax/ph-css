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

import com.helger.annotation.Nonempty;
import com.helger.annotation.Nonnegative;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.base.clone.ICloneable;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.css.ICSSWriteable;
import com.helger.css.ICSSWriterSettings;
import com.helger.css.propertyvalue.CCSSValue;
import com.helger.css.utils.CSSColorHelper;

/**
 * Represents a single HSLA color value (hue, saturation, lightness, opacity).
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSHSLA implements ICSSWriteable, ICSSColor, ICloneable <CSSHSLA>
{
  private String m_sHue;
  private String m_sSaturation;
  private String m_sLightness;
  private String m_sOpacity;

  /**
   * Copy constructor
   *
   * @param aOther
   *        The object to copy the data from. May not be <code>null</code>.
   */
  public CSSHSLA (@NonNull final CSSHSLA aOther)
  {
    this (aOther.getHue (), aOther.getSaturation (), aOther.getLightness (), aOther.getOpacity ());
  }

  /**
   * Constructor
   *
   * @param aOther
   *        The HSL value to use as the basis. May not be <code>null</code>.
   * @param fOpacity
   *        Opacity part. Is fitted to a value between 0 and 1.
   * @since 3.8.3
   */
  public CSSHSLA (@NonNull final CSSHSL aOther, final float fOpacity)
  {
    this (aOther, Float.toString (CSSColorHelper.getOpacityToUse (fOpacity)));
  }

  /**
   * Constructor
   *
   * @param aOther
   *        The HSL value to use as the basis. May not be <code>null</code>.
   * @param sOpacity
   *        Opacity part. May neither be <code>null</code> nor empty.
   * @since 3.8.3
   */
  public CSSHSLA (@NonNull final CSSHSL aOther, @NonNull @Nonempty final String sOpacity)
  {
    this (aOther.getHue (), aOther.getSaturation (), aOther.getLightness (), sOpacity);
  }

  /**
   * Constructor
   *
   * @param nHue
   *        Hue value. Is scaled to the range 0-360
   * @param nSaturation
   *        Saturation value. Is cut to the range 0-100 (percentage)
   * @param nLightness
   *        Lightness value. Is cut to the range 0-100 (percentage)
   * @param fOpacity
   *        Opacity - is scaled to 0-1
   */
  public CSSHSLA (final int nHue, final int nSaturation, final int nLightness, final float fOpacity)
  {
    this (Integer.toString (CSSColorHelper.getHSLHueValue (nHue)),
          Integer.toString (CSSColorHelper.getHSLPercentageValue (nSaturation)) + "%",
          Integer.toString (CSSColorHelper.getHSLPercentageValue (nLightness)) + "%",
          Float.toString (CSSColorHelper.getOpacityToUse (fOpacity)));
  }

  /**
   * Constructor
   *
   * @param fHue
   *        Hue value. Is scaled to the range 0-360
   * @param fSaturation
   *        Saturation value. Is cut to the range 0-100 (percentage)
   * @param fLightness
   *        Lightness value. Is cut to the range 0-100 (percentage)
   * @param fOpacity
   *        Opacity - is scaled to 0-1
   */
  public CSSHSLA (final float fHue, final float fSaturation, final float fLightness, final float fOpacity)
  {
    this (Float.toString (CSSColorHelper.getHSLHueValue (fHue)),
          Float.toString (CSSColorHelper.getHSLPercentageValue (fSaturation)) + "%",
          Float.toString (CSSColorHelper.getHSLPercentageValue (fLightness)) + "%",
          Float.toString (CSSColorHelper.getOpacityToUse (fOpacity)));
  }

  public CSSHSLA (@NonNull @Nonempty final String sHue,
                  @NonNull @Nonempty final String sSaturation,
                  @NonNull @Nonempty final String sLightness,
                  @NonNull @Nonempty final String sOpacity)
  {
    setHue (sHue);
    setSaturation (sSaturation);
    setLightness (sLightness);
    setOpacity (sOpacity);
  }

  /**
   * @return hue part
   */
  @NonNull
  @Nonempty
  public final String getHue ()
  {
    return m_sHue;
  }

  @NonNull
  public final CSSHSLA setHue (@NonNull @Nonempty final String sHue)
  {
    ValueEnforcer.notEmpty (sHue, "Hue");

    m_sHue = sHue;
    return this;
  }

  /**
   * @return saturation part
   */
  @NonNull
  @Nonempty
  public final String getSaturation ()
  {
    return m_sSaturation;
  }

  @NonNull
  public final CSSHSLA setSaturation (@NonNull @Nonempty final String sSaturation)
  {
    ValueEnforcer.notEmpty (sSaturation, "Saturation");

    m_sSaturation = sSaturation;
    return this;
  }

  /**
   * @return lightness part
   */
  @NonNull
  @Nonempty
  public final String getLightness ()
  {
    return m_sLightness;
  }

  @NonNull
  public final CSSHSLA setLightness (@NonNull @Nonempty final String sLightness)
  {
    ValueEnforcer.notEmpty (sLightness, "Lightness");

    m_sLightness = sLightness;
    return this;
  }

  /**
   * @return opacity part
   */
  @NonNull
  @Nonempty
  public final String getOpacity ()
  {
    return m_sOpacity;
  }

  @NonNull
  public final CSSHSLA setOpacity (@NonNull @Nonempty final String sOpacity)
  {
    ValueEnforcer.notEmpty (sOpacity, "Opacity");

    m_sOpacity = sOpacity;
    return this;
  }

  /**
   * @return This value as HSL value without the opacity. Never <code>null</code>.
   * @since 3.8.3
   */
  @NonNull
  public CSSHSL getAsHSL ()
  {
    return new CSSHSL (m_sHue, m_sSaturation, m_sLightness);
  }

  /**
   * {@inheritDoc}
   *
   * @since 3.8.3
   */
  @NonNull
  @Nonempty
  public String getAsString ()
  {
    return CCSSValue.PREFIX_HSLA_OPEN +
           m_sHue +
           ',' +
           m_sSaturation +
           ',' +
           m_sLightness +
           ',' +
           m_sOpacity +
           CCSSValue.SUFFIX_HSLA_CLOSE;
  }

  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    return getAsString ();
  }

  /**
   * {@inheritDoc}
   *
   * @since 3.8.3
   */
  @NonNull
  public CSSHSLA getClone ()
  {
    return new CSSHSLA (this);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final CSSHSLA rhs = (CSSHSLA) o;
    return m_sHue.equals (rhs.m_sHue) &&
           m_sSaturation.equals (rhs.m_sSaturation) &&
           m_sLightness.equals (rhs.m_sLightness) &&
           m_sOpacity.equals (rhs.m_sOpacity);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sHue)
                                       .append (m_sSaturation)
                                       .append (m_sLightness)
                                       .append (m_sOpacity)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Hue", m_sHue)
                                       .append ("Saturation", m_sSaturation)
                                       .append ("Lightness", m_sLightness)
                                       .append ("Opacity", m_sOpacity)
                                       .getToString ();
  }
}
