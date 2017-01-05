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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.css.decl.CSSHSL;
import com.helger.css.decl.CSSHSLA;
import com.helger.css.decl.CSSRGB;
import com.helger.css.decl.CSSRGBA;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Test class for class {@link CSSColorHelper}.
 *
 * @author Philip Helger
 */
public final class CSSColorHelperTest
{
  @Test
  @SuppressFBWarnings ("TQ_NEVER_VALUE_USED_WHERE_ALWAYS_REQUIRED")
  public void testColorRGB ()
  {
    assertEquals ("rgb(0,0,0)", CSSColorHelper.getRGBColorValue (0, 0, 0));
    assertEquals ("rgb(13,123,145)", CSSColorHelper.getRGBColorValue (13, 123, 145));
    assertEquals ("rgb(255,0,0)", CSSColorHelper.getRGBColorValue (-1, 0, 0));
    assertEquals ("rgb(255,0,0)", CSSColorHelper.getRGBColorValue (-257, 0, 0));
    assertEquals ("rgb(255,0,0)", CSSColorHelper.getRGBColorValue (-513, 0, 0));
    assertEquals ("rgb(0,0,0)", CSSColorHelper.getRGBColorValue (256, 0, 0));
    assertEquals ("rgb(0,255,0)", CSSColorHelper.getRGBColorValue (0, -1, 0));
    assertEquals ("rgb(0,0,0)", CSSColorHelper.getRGBColorValue (0, 256, 0));
    assertEquals ("rgb(0,0,255)", CSSColorHelper.getRGBColorValue (0, 0, -1));
    assertEquals ("rgb(0,0,0)", CSSColorHelper.getRGBColorValue (0, 0, 256));
  }

  @Test
  public void testParsedRGBColor ()
  {
    CSSRGB aRGB = CSSColorHelper.getParsedRGBColorValue ("rgb(1,2,3)");
    assertNotNull (aRGB);
    assertEquals ("1", aRGB.getRed ());
    assertEquals ("2", aRGB.getGreen ());
    assertEquals ("3", aRGB.getBlue ());

    aRGB = CSSColorHelper.getParsedRGBColorValue (" rgb ( 1 , 2 , 3 ) ");
    assertNotNull (aRGB);
    assertEquals ("1", aRGB.getRed ());
    assertEquals ("2", aRGB.getGreen ());
    assertEquals ("3", aRGB.getBlue ());

    assertNull (CSSColorHelper.getParsedRGBColorValue (null));
    assertNull (CSSColorHelper.getParsedRGBColorValue (""));
    assertNull (CSSColorHelper.getParsedRGBColorValue ("rgb(1,2,3"));
  }

  @Test
  public void testParsedRGBAColor ()
  {
    CSSRGBA aRGB = CSSColorHelper.getParsedRGBAColorValue ("rgba(1,2,3,0.4)");
    assertNotNull (aRGB);
    assertEquals ("1", aRGB.getRed ());
    assertEquals ("2", aRGB.getGreen ());
    assertEquals ("3", aRGB.getBlue ());
    assertEquals ("0.4", aRGB.getOpacity ());

    aRGB = CSSColorHelper.getParsedRGBAColorValue (" rgba ( 1 , 2 , 3 , 0.4 ) ");
    assertNotNull (aRGB);
    assertEquals ("1", aRGB.getRed ());
    assertEquals ("2", aRGB.getGreen ());
    assertEquals ("3", aRGB.getBlue ());
    assertEquals ("0.4", aRGB.getOpacity ());

    assertNull (CSSColorHelper.getParsedRGBAColorValue (null));
    assertNull (CSSColorHelper.getParsedRGBAColorValue (""));
    assertNull (CSSColorHelper.getParsedRGBAColorValue ("rgba(1,2,3"));
  }

  @Test
  public void testParsedHSLColor ()
  {
    CSSHSL aHSL = CSSColorHelper.getParsedHSLColorValue ("hsl(1,2%,3%)");
    assertNotNull (aHSL);
    assertEquals ("1", aHSL.getHue ());
    assertEquals ("2%", aHSL.getSaturation ());
    assertEquals ("3%", aHSL.getLightness ());

    aHSL = CSSColorHelper.getParsedHSLColorValue (" hsl ( 1 , 2% , 3% ) ");
    assertNotNull (aHSL);
    assertEquals ("1", aHSL.getHue ());
    assertEquals ("2%", aHSL.getSaturation ());
    assertEquals ("3%", aHSL.getLightness ());

    assertNull (CSSColorHelper.getParsedHSLColorValue (null));
    assertNull (CSSColorHelper.getParsedHSLColorValue (""));
    assertNull (CSSColorHelper.getParsedHSLColorValue ("hsl(1,2,3)"));
    assertNull (CSSColorHelper.getParsedHSLColorValue ("hsl(1,2,3"));
  }

  @Test
  public void testParsedHSLAColor ()
  {
    CSSHSLA aHSL = CSSColorHelper.getParsedHSLAColorValue ("hsla(1,2%,3%,0.4)");
    assertNotNull (aHSL);
    assertEquals ("1", aHSL.getHue ());
    assertEquals ("2%", aHSL.getSaturation ());
    assertEquals ("3%", aHSL.getLightness ());
    assertEquals ("0.4", aHSL.getOpacity ());

    aHSL = CSSColorHelper.getParsedHSLAColorValue (" hsla ( 1 , 2% , 3% , 0.4 ) ");
    assertNotNull (aHSL);
    assertEquals ("1", aHSL.getHue ());
    assertEquals ("2%", aHSL.getSaturation ());
    assertEquals ("3%", aHSL.getLightness ());
    assertEquals ("0.4", aHSL.getOpacity ());

    assertNull (CSSColorHelper.getParsedHSLAColorValue (null));
    assertNull (CSSColorHelper.getParsedHSLAColorValue (""));
    assertNull (CSSColorHelper.getParsedHSLAColorValue ("hsla(1,2,3,4)"));
    assertNull (CSSColorHelper.getParsedHSLAColorValue ("hsla(1,2,3,4"));
    assertNull (CSSColorHelper.getParsedHSLAColorValue ("hsla(1,2,3,"));
    assertNull (CSSColorHelper.getParsedHSLAColorValue ("hsla(1,2,3)"));
    assertNull (CSSColorHelper.getParsedHSLAColorValue ("hsla(1,2,3"));
  }

  @Test
  @SuppressFBWarnings ("TQ_NEVER_VALUE_USED_WHERE_ALWAYS_REQUIRED")
  public void testColorHex ()
  {
    assertEquals ("#000000", CSSColorHelper.getHexColorValue (0, 0, 0));
    assertEquals ("#0f80ff", CSSColorHelper.getHexColorValue (15, 128, 255));

    // Check overflow
    assertEquals ("#000000", CSSColorHelper.getHexColorValue (256, 256, 256));
    assertEquals ("#010101", CSSColorHelper.getHexColorValue (257, 257, 257));
    assertEquals ("#ffffff", CSSColorHelper.getHexColorValue (-1, -1, -1));
  }

  @Test
  public void testIsHexColorValue ()
  {
    final String [] HEX_COLORS = new String [] { "#000000",
                                                 "#99aa00",
                                                 "#9900aa",
                                                 "#aa9900",
                                                 "#ffffff",
                                                 "#aaa",
                                                 "#000",
                                                 "#1234",
                                                 "#000f" };
    for (final String sHexColor : HEX_COLORS)
    {
      assertTrue (sHexColor, CSSColorHelper.isHexColorValue (sHexColor));
      assertTrue (sHexColor, CSSColorHelper.isColorValue (sHexColor));
    }
    assertFalse (CSSColorHelper.isHexColorValue ("123456"));
    assertFalse (CSSColorHelper.isHexColorValue ("#1234567"));
  }

  @Test
  public void testIsRGBColorValue ()
  {
    final String [] RGB_COLORS = new String [] { "rgb(0,0,0)",
                                                 "rgb(255,0,0)",
                                                 "rgb(300,0,0)",
                                                 "rgb(  300  ,  0  ,  0  )  ",
                                                 "rgb(255,-10,0)",
                                                 "rgb(110%, 0%, 0%)",
                                                 "rgb(100%, 0%, 0%)" };
    for (final String sRGBColor : RGB_COLORS)
    {
      assertTrue (sRGBColor, CSSColorHelper.isRGBColorValue (sRGBColor));
      assertTrue (sRGBColor, CSSColorHelper.isColorValue (sRGBColor));
    }
    assertFalse (CSSColorHelper.isRGBColorValue ("rgb(a,0,0)"));
    assertFalse (CSSColorHelper.isRGBColorValue ("rgb(0,0,0,0)"));
  }

  @Test
  public void testIsRGBAColorValue ()
  {
    final String [] RGBA_COLORS = new String [] { "rgba(0,0,0,0)",
                                                  "rgba(0,0,0,1)",
                                                  "rgba(255,0,0, 0.1)",
                                                  "rgba(255,0,0, 0.875)",
                                                  "rgba(300,0,0, 0.999999)",
                                                  "rgba(255,-10,0, 0)",
                                                  "rgba(110%, 0%, 0%, 1.)",
                                                  "rgba(100%, 0%, 0%, 1.  )" };
    for (final String sRGBAColor : RGBA_COLORS)
    {
      assertTrue (sRGBAColor, CSSColorHelper.isRGBAColorValue (sRGBAColor));
      assertTrue (sRGBAColor, CSSColorHelper.isColorValue (sRGBAColor));
    }
    assertFalse (CSSColorHelper.isRGBAColorValue ("rgba(a,0,0)"));
    assertFalse (CSSColorHelper.isRGBAColorValue ("rgba(0,0,0,5%)"));
  }

  @Test
  public void testIsHSLColorValue ()
  {
    final String [] HSL_COLORS = new String [] { "hsl(0,0%,0%)",
                                                 "hsl(255,0%,0%)",
                                                 "hsl(300,0%,0%)",
                                                 "hsl(  300  ,  0%  ,  0%  )  ",
                                                 "hsl(255,-10%,0%)",
                                                 "hsl(110%, 0%, 0%)",
                                                 "hsl(100%, 0%, 0%)" };
    for (final String sHSLColor : HSL_COLORS)
    {
      assertTrue (sHSLColor, CSSColorHelper.isHSLColorValue (sHSLColor));
      assertTrue (sHSLColor, CSSColorHelper.isColorValue (sHSLColor));
    }
    assertFalse (CSSColorHelper.isHSLColorValue ("hsl(0,0,1)"));
    assertFalse (CSSColorHelper.isHSLColorValue ("hsl(a,0,0)"));
    assertFalse (CSSColorHelper.isHSLColorValue ("hsl(0,0,0,0)"));
  }

  @Test
  public void testIsHSLAColorValue ()
  {
    final String [] HSLA_COLORS = new String [] { "hsla(0,0%,0%,0)",
                                                  "hsla(0,0%,0%,1)",
                                                  "hsla(0,0,0%,1)",
                                                  "hsla(0,0%,0,1)",
                                                  "hsla(0,0,0,1)",
                                                  "hsla(255,0%,0%, 0.1)",
                                                  "hsla(255,0%,0%, 0.875)",
                                                  "hsla(300,0%,0%, 0.999999)",
                                                  "hsla(255,-10%,0%, 0)",
                                                  "hsla(110%, 0%, 0%, 1.)",
                                                  "hsla(100%, 0%, 0%, 1.  )" };
    for (final String sHSLAColor : HSLA_COLORS)
    {
      assertTrue (sHSLAColor, CSSColorHelper.isHSLAColorValue (sHSLAColor));
      assertTrue (sHSLAColor, CSSColorHelper.isColorValue (sHSLAColor));
    }
    assertFalse (CSSColorHelper.isHSLAColorValue ("hsla(a,0,0)"));
    assertFalse (CSSColorHelper.isHSLAColorValue ("hsla(0,0,1,0)"));
    assertFalse (CSSColorHelper.isHSLAColorValue ("hsla(0,0,0,5%)"));
  }

  @Test
  public void testRGBToHSLAndBack ()
  {
    for (int nRed = 0; nRed < 256; ++nRed)
      for (int nGreen = 0; nGreen < 256; ++nGreen)
        for (int nBlue = 0; nBlue < 256; ++nBlue)
        {
          // RGB to HSL
          final float [] f = CSSColorHelper.getRGBAsHSLValue (nRed, nGreen, nBlue);
          assertNotNull (f);
          assertEquals (3, f.length);
          assertTrue (f[0] >= 0);
          assertTrue (f[0] < 360);
          assertTrue (f[1] >= 0);
          assertTrue (f[1] <= 100);
          assertTrue (f[2] >= 0);
          assertTrue (f[2] <= 100);

          // HSL to RGB
          final int [] aRGB = CSSColorHelper.getHSLAsRGBValue (f[0], f[1], f[2]);
          assertNotNull (aRGB);
          assertEquals (3, aRGB.length);
          assertTrue (aRGB[0] >= 0);
          assertTrue (aRGB[0] < 256);
          assertTrue (aRGB[1] >= 0);
          assertTrue (aRGB[1] < 256);
          assertTrue (aRGB[2] >= 0);
          assertTrue (aRGB[2] < 256);

          final String sExpected = CSSColorHelper.getRGBColorValue (nRed, nGreen, nBlue);
          assertEquals (sExpected, nRed, aRGB[0]);
          assertEquals (sExpected, nGreen, aRGB[1]);
          assertEquals (sExpected, nBlue, aRGB[2]);
        }
  }
}
