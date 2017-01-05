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
package com.helger.css.utils;

import java.awt.Color;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.lang.EnumHelper;
import com.helger.css.decl.CSSHSL;
import com.helger.css.decl.CSSHSLA;
import com.helger.css.decl.CSSRGB;
import com.helger.css.decl.CSSRGBA;

/**
 * CSS basic colors. Source: http://www.w3.org/TR/css3-color/ chapter 4.1 and
 * 4.3
 *
 * @author Philip Helger
 */
public enum ECSSColor implements ICSSNamedColor
{
  ALICEBLUE ("aliceblue", 240, 248, 255),
  ANTIQUEWHITE ("antiquewhite", 250, 235, 215),
  AQUA ("aqua", 0, 255, 255),
  AQUAMARINE ("aquamarine", 127, 255, 212),
  AZURE ("azure", 240, 255, 255),
  BEIGE ("beige", 245, 245, 220),
  BISQUE ("bisque", 255, 228, 196),
  BLACK ("black", 0, 0, 0),
  BLANCHEDALMOND ("blanchedalmond", 255, 235, 205),
  BLUE ("blue", 0, 0, 255),
  BLUEVIOLET ("blueviolet", 138, 43, 226),
  BROWN ("brown", 165, 42, 42),
  BURLYWOOD ("burlywood", 222, 184, 135),
  CADETBLUE ("cadetblue", 95, 158, 160),
  CHARTREUSE ("chartreuse", 127, 255, 0),
  CHOCOLATE ("chocolate", 210, 105, 30),
  CORAL ("coral", 255, 127, 80),
  CORNFLOWERBLUE ("cornflowerblue", 100, 149, 237),
  CORNSILK ("cornsilk", 255, 248, 220),
  CRIMSON ("crimson", 220, 20, 60),
  CYAN ("cyan", 0, 255, 255),
  DARKBLUE ("darkblue", 0, 0, 139),
  DARKCYAN ("darkcyan", 0, 139, 139),
  DARKGOLDENROD ("darkgoldenrod", 184, 134, 11),
  DARKGRAY ("darkgray", 169, 169, 169),
  DARKGREEN ("darkgreen", 0, 100, 0),
  DARKGREY ("darkgrey", 169, 169, 169),
  DARKKHAKI ("darkkhaki", 189, 183, 107),
  DARKMAGENTA ("darkmagenta", 139, 0, 139),
  DARKOLIVEGREEN ("darkolivegreen", 85, 107, 47),
  DARKORANGE ("darkorange", 255, 140, 0),
  DARKORCHID ("darkorchid", 153, 50, 204),
  DARKRED ("darkred", 139, 0, 0),
  DARKSALMON ("darksalmon", 233, 150, 122),
  DARKSEAGREEN ("darkseagreen", 143, 188, 143),
  DARKSLATEBLUE ("darkslateblue", 72, 61, 139),
  DARKSLATEGRAY ("darkslategray", 47, 79, 79),
  DARKSLATEGREY ("darkslategrey", 47, 79, 79),
  DARKTURQUOISE ("darkturquoise", 0, 206, 209),
  DARKVIOLET ("darkviolet", 148, 0, 211),
  DEEPPINK ("deeppink", 255, 20, 147),
  DEEPSKYBLUE ("deepskyblue", 0, 191, 255),
  DIMGRAY ("dimgray", 105, 105, 105),
  DIMGREY ("dimgrey", 105, 105, 105),
  DODGERBLUE ("dodgerblue", 30, 144, 255),
  FIREBRICK ("firebrick", 178, 34, 34),
  FLORALWHITE ("floralwhite", 255, 250, 240),
  FORESTGREEN ("forestgreen", 34, 139, 34),
  FUCHSIA ("fuchsia", 255, 0, 255),
  GAINSBORO ("gainsboro", 220, 220, 220),
  GHOSTWHITE ("ghostwhite", 248, 248, 255),
  GOLD ("gold", 255, 215, 0),
  GOLDENROD ("goldenrod", 218, 165, 32),
  GRAY ("gray", 128, 128, 128),
  GREEN ("green", 0, 128, 0),
  GREENYELLOW ("greenyellow", 173, 255, 47),
  GREY ("grey", 128, 128, 128),
  HONEYDEW ("honeydew", 240, 255, 240),
  HOTPINK ("hotpink", 255, 105, 180),
  INDIANRED ("indianred", 205, 92, 92),
  INDIGO ("indigo", 75, 0, 130),
  IVORY ("ivory", 255, 255, 240),
  KHAKI ("khaki", 240, 230, 140),
  LAVENDER ("lavender", 230, 230, 250),
  LAVENDERBLUSH ("lavenderblush", 255, 240, 245),
  LAWNGREEN ("lawngreen", 124, 252, 0),
  LEMONCHIFFON ("lemonchiffon", 255, 250, 205),
  LIGHTBLUE ("lightblue", 173, 216, 230),
  LIGHTCORAL ("lightcoral", 240, 128, 128),
  LIGHTCYAN ("lightcyan", 224, 255, 255),
  LIGHTGOLDENRODYELLOW ("lightgoldenrodyellow", 250, 250, 210),
  LIGHTGRAY ("lightgray", 211, 211, 211),
  LIGHTGREEN ("lightgreen", 144, 238, 144),
  LIGHTGREY ("lightgrey", 211, 211, 211),
  LIGHTPINK ("lightpink", 255, 182, 193),
  LIGHTSALMON ("lightsalmon", 255, 160, 122),
  LIGHTSEAGREEN ("lightseagreen", 32, 178, 170),
  LIGHTSKYBLUE ("lightskyblue", 135, 206, 250),
  LIGHTSLATEGRAY ("lightslategray", 119, 136, 153),
  LIGHTSLATEGREY ("lightslategrey", 119, 136, 153),
  LIGHTSTEELBLUE ("lightsteelblue", 176, 196, 222),
  LIGHTYELLOW ("lightyellow", 255, 255, 224),
  LIME ("lime", 0, 255, 0),
  LIMEGREEN ("limegreen", 50, 205, 50),
  LINEN ("linen", 250, 240, 230),
  MAGENTA ("magenta", 255, 0, 255),
  MAROON ("maroon", 128, 0, 0),
  MEDIUMAQUAMARINE ("mediumaquamarine", 102, 205, 170),
  MEDIUMBLUE ("mediumblue", 0, 0, 205),
  MEDIUMORCHID ("mediumorchid", 186, 85, 211),
  MEDIUMPURPLE ("mediumpurple", 147, 112, 219),
  MEDIUMSEAGREEN ("mediumseagreen", 60, 179, 113),
  MEDIUMSLATEBLUE ("mediumslateblue", 123, 104, 238),
  MEDIUMSPRINGGREEN ("mediumspringgreen", 0, 250, 154),
  MEDIUMTURQUOISE ("mediumturquoise", 72, 209, 204),
  MEDIUMVIOLETRED ("mediumvioletred", 199, 21, 133),
  MIDNIGHTBLUE ("midnightblue", 25, 25, 112),
  MINTCREAM ("mintcream", 245, 255, 250),
  MISTYROSE ("mistyrose", 255, 228, 225),
  MOCCASIN ("moccasin", 255, 228, 181),
  NAVAJOWHITE ("navajowhite", 255, 222, 173),
  NAVY ("navy", 0, 0, 128),
  OLDLACE ("oldlace", 253, 245, 230),
  OLIVE ("olive", 128, 128, 0),
  OLIVEDRAB ("olivedrab", 107, 142, 35),
  ORANGE ("orange", 255, 165, 0),
  ORANGERED ("orangered", 255, 69, 0),
  ORCHID ("orchid", 218, 112, 214),
  PALEGOLDENROD ("palegoldenrod", 238, 232, 170),
  PALEGREEN ("palegreen", 152, 251, 152),
  PALETURQUOISE ("paleturquoise", 175, 238, 238),
  PALEVIOLETRED ("palevioletred", 219, 112, 147),
  PAPAYAWHIP ("papayawhip", 255, 239, 213),
  PEACHPUFF ("peachpuff", 255, 218, 185),
  PERU ("peru", 205, 133, 63),
  PINK ("pink", 255, 192, 203),
  PLUM ("plum", 221, 160, 221),
  POWDERBLUE ("powderblue", 176, 224, 230),
  PURPLE ("purple", 128, 0, 128),
  RED ("red", 255, 0, 0),
  ROSYBROWN ("rosybrown", 188, 143, 143),
  ROYALBLUE ("royalblue", 65, 105, 225),
  SADDLEBROWN ("saddlebrown", 139, 69, 19),
  SALMON ("salmon", 250, 128, 114),
  SANDYBROWN ("sandybrown", 244, 164, 96),
  SEAGREEN ("seagreen", 46, 139, 87),
  SEASHELL ("seashell", 255, 245, 238),
  SIENNA ("sienna", 160, 82, 45),
  SILVER ("silver", 192, 192, 192),
  SKYBLUE ("skyblue", 135, 206, 235),
  SLATEBLUE ("slateblue", 106, 90, 205),
  SLATEGRAY ("slategray", 112, 128, 144),
  SLATEGREY ("slategrey", 112, 128, 144),
  SNOW ("snow", 255, 250, 250),
  SPRINGGREEN ("springgreen", 0, 255, 127),
  STEELBLUE ("steelblue", 70, 130, 180),
  TAN ("tan", 210, 180, 140),
  TEAL ("teal", 0, 128, 128),
  THISTLE ("thistle", 216, 191, 216),
  TOMATO ("tomato", 255, 99, 71),
  TURQUOISE ("turquoise", 64, 224, 208),
  VIOLET ("violet", 238, 130, 238),
  WHEAT ("wheat", 245, 222, 179),
  WHITE ("white", 255, 255, 255),
  WHITESMOKE ("whitesmoke", 245, 245, 245),
  YELLOW ("yellow", 255, 255, 0),
  YELLOWGREEN ("yellowgreen", 154, 205, 50);

  private final String m_sName;
  private final int m_nRed;
  private final int m_nGreen;
  private final int m_nBlue;
  private final float m_fHue;
  private final float m_fSaturation;
  private final float m_fLightness;

  private ECSSColor (@Nonnull @Nonempty final String sName,
                     @Nonnegative final int nRed,
                     @Nonnegative final int nGreen,
                     @Nonnegative final int nBlue)
  {
    ValueEnforcer.isBetweenInclusive (nRed, "Red", CSSColorHelper.RGB_MIN, CSSColorHelper.RGB_MAX);
    ValueEnforcer.isBetweenInclusive (nGreen, "Green", CSSColorHelper.RGB_MIN, CSSColorHelper.RGB_MAX);
    ValueEnforcer.isBetweenInclusive (nBlue, "Blue", CSSColorHelper.RGB_MIN, CSSColorHelper.RGB_MAX);

    m_sName = sName;
    m_nRed = nRed;
    m_nGreen = nGreen;
    m_nBlue = nBlue;

    // Convert RGB to HSL
    final float [] aHSL = CSSColorHelper.getRGBAsHSLValue (nRed, nGreen, nBlue);
    m_fHue = aHSL[0];
    m_fSaturation = aHSL[1];
    m_fLightness = aHSL[2];
  }

  /**
   * @return The name of the color as to be used in CSS. Neither
   *         <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  /**
   * @return The Red part of the RGB value. Is within the range 0-255.
   */
  @Nonnegative
  public int getRed ()
  {
    return m_nRed;
  }

  /**
   * @return The Green part of the RGB value. Is within the range 0-255.
   */
  @Nonnegative
  public int getGreen ()
  {
    return m_nGreen;
  }

  /**
   * @return The Blue part of the RGB value. Is within the range 0-255.
   */
  @Nonnegative
  public int getBlue ()
  {
    return m_nBlue;
  }

  /**
   * @return The Hue part of the HSL value. Is within the range 0-359.
   */
  @Nonnegative
  public float getHue ()
  {
    return m_fHue;
  }

  /**
   * @return The Saturation part of the HSL value. Is within the range 0-100.
   */
  @Nonnegative
  public float getSaturation ()
  {
    return m_fSaturation;
  }

  /**
   * @return The Lightness part of the HSL value. Is within the range 0-100.
   */
  @Nonnegative
  public float getLightness ()
  {
    return m_fLightness;
  }

  /**
   * @return This CSS color as a java.awt.Color object. Never <code>null</code>.
   */
  @Nonnull
  public Color getAsAwtColor ()
  {
    return new Color (m_nRed, m_nGreen, m_nBlue);
  }

  /**
   * @return The CSS hex color representation of this color (e.g. #ff0000)
   */
  @Nonnull
  @Nonempty
  public String getAsHexColorValue ()
  {
    return CSSColorHelper.getHexColorValue (m_nRed, m_nGreen, m_nBlue);
  }

  /**
   * @return This color as an CSS RGB color value.
   */
  @Nonnull
  @Nonempty
  public String getAsRGBColorValue ()
  {
    return CSSColorHelper.getRGBColorValue (m_nRed, m_nGreen, m_nBlue);
  }

  /**
   * @return This color as an CSS RGB color object.
   */
  @Nonnull
  @Nonempty
  public CSSRGB getAsRGB ()
  {
    return new CSSRGB (m_nRed, m_nGreen, m_nBlue);
  }

  /**
   * @param fOpacity
   *        The opacity to be used. Is scaled to 0-1.
   * @return This color as an CSS RGBA color value.
   */
  @Nonnull
  @Nonempty
  public String getAsRGBAColorValue (@Nonnegative final float fOpacity)
  {
    return CSSColorHelper.getRGBAColorValue (m_nRed, m_nGreen, m_nBlue, fOpacity);
  }

  /**
   * @param fOpacity
   *        The opacity to be used. Is scaled to 0-1.
   * @return This color as an CSS RGBA color object.
   */
  @Nonnull
  @Nonempty
  public CSSRGBA getAsRGBA (@Nonnegative final float fOpacity)
  {
    return new CSSRGBA (m_nRed, m_nGreen, m_nBlue, fOpacity);
  }

  /**
   * @return This color as an CSS HSL color value.
   */
  @Nonnull
  @Nonempty
  public String getAsHSLColorValue ()
  {
    return CSSColorHelper.getHSLColorValue (m_fHue, m_fSaturation, m_fLightness);
  }

  /**
   * @return This color as an CSS HSL color object.
   */
  @Nonnull
  @Nonempty
  public CSSHSL getAsHSL ()
  {
    return new CSSHSL (m_fHue, m_fSaturation, m_fLightness);
  }

  /**
   * @param fOpacity
   *        The opacity to be used. Is scaled to 0-1.
   * @return This color as an CSS HSLA color value.
   */
  @Nonnull
  @Nonempty
  public String getAsHSLAColorValue (@Nonnegative final float fOpacity)
  {
    return CSSColorHelper.getHSLAColorValue (m_fHue, m_fSaturation, m_fLightness, fOpacity);
  }

  /**
   * @param fOpacity
   *        The opacity to be used. Is scaled to 0-1.
   * @return This color as an CSS HSL color object.
   */
  @Nonnull
  @Nonempty
  public CSSHSLA getAsHSLA (@Nonnegative final float fOpacity)
  {
    return new CSSHSLA (m_fHue, m_fSaturation, m_fLightness, fOpacity);
  }

  @Nullable
  public static ECSSColor getFromNameCaseInsensitiveOrNull (@Nullable final String sName)
  {
    return EnumHelper.getFromNameCaseInsensitiveOrNull (ECSSColor.class, sName);
  }

  /**
   * Check if the passed color name is a default color name.
   *
   * @param sName
   *        The color name to check.
   * @return <code>true</code> if the passed color name is a default color name,
   *         <code>false</code> if not.
   */
  public static boolean isDefaultColorName (@Nullable final String sName)
  {
    return getFromNameCaseInsensitiveOrNull (sName) != null;
  }
}
