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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.commons.string.StringHelper;
import com.helger.css.ECSSVersion;
import com.helger.css.writer.CSSWriter;

/**
 * Test class for class {@link ECSSColor}.
 *
 * @author Philip Helger
 */
public final class ECSSColorTest
{
  @Test
  public void testAll ()
  {
    final CSSWriter aWriter = new CSSWriter (ECSSVersion.CSS30);
    for (final ECSSColor eColor : ECSSColor.values ())
    {
      assertTrue (StringHelper.hasText (eColor.getName ()));
      assertTrue (CSSColorHelper.isColorValue (eColor.getName ()));

      final String sHex = eColor.getAsHexColorValue ();
      assertTrue (sHex, CSSColorHelper.isHexColorValue (sHex));

      final String sRGB = eColor.getAsRGBColorValue ();
      assertTrue (sRGB, CSSColorHelper.isRGBColorValue (sRGB));
      assertNotNull (eColor.getAsRGB ());
      assertEquals (sRGB, aWriter.getCSSAsString (eColor.getAsRGB ()));

      final String sRGBA = eColor.getAsRGBAColorValue (1f);
      assertTrue (sRGBA, CSSColorHelper.isRGBAColorValue (sRGBA));
      assertNotNull (eColor.getAsRGBA (1));
      assertEquals (sRGBA, aWriter.getCSSAsString (eColor.getAsRGBA (1)));

      final String sHSL = eColor.getAsHSLColorValue ();
      assertTrue (sHSL, CSSColorHelper.isHSLColorValue (sHSL));
      assertNotNull (eColor.getAsHSL ());
      assertEquals (sHSL, aWriter.getCSSAsString (eColor.getAsHSL ()));

      final String sHSLA = eColor.getAsHSLAColorValue (1f);
      assertTrue (sHSLA, CSSColorHelper.isHSLAColorValue (sHSLA));
      assertNotNull (eColor.getAsHSLA (1));
      assertEquals (sHSLA, aWriter.getCSSAsString (eColor.getAsHSLA (1)));

      assertSame (eColor, ECSSColor.getFromNameCaseInsensitiveOrNull (eColor.getName ()));
      assertTrue (ECSSColor.isDefaultColorName (eColor.getName ()));
    }
  }
}
