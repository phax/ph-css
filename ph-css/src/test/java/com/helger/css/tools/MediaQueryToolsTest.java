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
package com.helger.css.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.helger.css.decl.CSSMediaQuery;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.css.writer.CSSWriter;

/**
 * Test class for class {@link MediaQueryTools}.
 *
 * @author Philip Helger
 */
public final class MediaQueryToolsTest
{
  @Test
  public void testParseMediaQuery ()
  {
    List <CSSMediaQuery> aMQs = MediaQueryTools.parseToMediaQuery ("screen");
    assertNotNull (aMQs);
    assertEquals (1, aMQs.size ());

    aMQs = MediaQueryTools.parseToMediaQuery ("screen and (color)");
    assertNotNull (aMQs);
    assertEquals (1, aMQs.size ());

    aMQs = MediaQueryTools.parseToMediaQuery ("not screen and (color)");
    assertNotNull (aMQs);
    assertEquals (1, aMQs.size ());

    aMQs = MediaQueryTools.parseToMediaQuery ("only screen and (color)");
    assertNotNull (aMQs);
    assertEquals (1, aMQs.size ());

    aMQs = MediaQueryTools.parseToMediaQuery ("screen and (color), projection and (color)");
    assertNotNull (aMQs);
    assertEquals (2, aMQs.size ());

    aMQs = MediaQueryTools.parseToMediaQuery ("aural and (device-aspect-ratio: 16/9)");
    assertNotNull (aMQs);
    assertEquals (1, aMQs.size ());

    aMQs = MediaQueryTools.parseToMediaQuery ("speech and (min-device-width: 800px)");
    assertNotNull (aMQs);
    assertEquals (1, aMQs.size ());

    aMQs = MediaQueryTools.parseToMediaQuery ("screen and (max-weight: 3kg) and (color), (color)");
    assertNotNull (aMQs);
    assertEquals (2, aMQs.size ());

    aMQs = MediaQueryTools.parseToMediaQuery ("print and (min-width: 25cm)");
    assertNotNull (aMQs);
    assertEquals (1, aMQs.size ());
  }

  @Test
  public void testGetWrapped ()
  {
    // Read and arbitrary CSS
    final CascadingStyleSheet aBaseCSS = CSSReader.readFromString ("p { color:red;}");
    assertNotNull (aBaseCSS);

    // Create structured media queries
    final List <CSSMediaQuery> aMQs = MediaQueryTools.parseToMediaQuery ("screen");
    assertNotNull (aMQs);

    // Wrap the source CSS with the specified media queries
    final CascadingStyleSheet aWrappedCSS = MediaQueryTools.getWrappedInMediaQuery (aBaseCSS, aMQs, false);
    assertNotNull (aWrappedCSS);
    assertEquals ("@media screen{p{color:red}}", new CSSWriter (true).getCSSAsString (aWrappedCSS));
  }
}
