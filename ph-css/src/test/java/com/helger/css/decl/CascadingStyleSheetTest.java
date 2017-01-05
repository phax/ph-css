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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.helger.css.ECSSVersion;
import com.helger.css.reader.CSSReader;

/**
 * Test class for class {@link CascadingStyleSheet}.
 *
 * @author Philip Helger
 */
public final class CascadingStyleSheetTest
{
  @Nonnull
  private static CascadingStyleSheet _parse (@Nonnull final String sCSS)
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sCSS, ECSSVersion.CSS30);
    assertNotNull (sCSS, aCSS);
    assertNotNull (aCSS.getSourceLocation ());
    return aCSS;
  }

  @Test
  public void testReadEmpty ()
  {
    CascadingStyleSheet aCSS;
    aCSS = _parse ("");

    assertFalse (aCSS.hasImportRules ());
    assertEquals (0, aCSS.getImportRuleCount ());
    assertTrue (aCSS.getAllImportRules ().isEmpty ());

    assertFalse (aCSS.hasNamespaceRules ());
    assertEquals (0, aCSS.getNamespaceRuleCount ());
    assertTrue (aCSS.getAllNamespaceRules ().isEmpty ());

    assertFalse (aCSS.hasStyleRules ());
    assertEquals (0, aCSS.getStyleRuleCount ());
    assertTrue (aCSS.getAllStyleRules ().isEmpty ());

    assertFalse (aCSS.hasPageRules ());
    assertEquals (0, aCSS.getPageRuleCount ());
    assertTrue (aCSS.getAllPageRules ().isEmpty ());

    assertFalse (aCSS.hasMediaRules ());
    assertEquals (0, aCSS.getMediaRuleCount ());
    assertTrue (aCSS.getAllMediaRules ().isEmpty ());

    assertFalse (aCSS.hasFontFaceRules ());
    assertEquals (0, aCSS.getFontFaceRuleCount ());
    assertTrue (aCSS.getAllFontFaceRules ().isEmpty ());

    assertFalse (aCSS.hasKeyframesRules ());
    assertEquals (0, aCSS.getKeyframesRuleCount ());
    assertTrue (aCSS.getAllKeyframesRules ().isEmpty ());

    assertFalse (aCSS.hasViewportRules ());
    assertEquals (0, aCSS.getViewportRuleCount ());
    assertTrue (aCSS.getAllViewportRules ().isEmpty ());

    assertFalse (aCSS.hasSupportsRules ());
    assertEquals (0, aCSS.getSupportsRuleCount ());
    assertTrue (aCSS.getAllSupportsRules ().isEmpty ());

    assertFalse (aCSS.hasUnknownRules ());
    assertEquals (0, aCSS.getUnknownRuleCount ());
    assertTrue (aCSS.getAllUnknownRules ().isEmpty ());
  }

  @Test
  public void testReadImportOnly ()
  {
    CascadingStyleSheet aCSS;
    aCSS = _parse ("@import url(a.gif);\n");

    assertTrue (aCSS.hasImportRules ());
    assertEquals (1, aCSS.getImportRuleCount ());
    assertNotNull (aCSS.getAllImportRules ().get (0));

    assertFalse (aCSS.hasNamespaceRules ());
    assertEquals (0, aCSS.getNamespaceRuleCount ());
    assertTrue (aCSS.getAllNamespaceRules ().isEmpty ());

    assertFalse (aCSS.hasStyleRules ());
    assertEquals (0, aCSS.getStyleRuleCount ());
    assertTrue (aCSS.getAllStyleRules ().isEmpty ());

    assertFalse (aCSS.hasPageRules ());
    assertEquals (0, aCSS.getPageRuleCount ());
    assertTrue (aCSS.getAllPageRules ().isEmpty ());

    assertFalse (aCSS.hasMediaRules ());
    assertEquals (0, aCSS.getMediaRuleCount ());
    assertTrue (aCSS.getAllMediaRules ().isEmpty ());

    assertFalse (aCSS.hasFontFaceRules ());
    assertEquals (0, aCSS.getFontFaceRuleCount ());
    assertTrue (aCSS.getAllFontFaceRules ().isEmpty ());

    assertFalse (aCSS.hasKeyframesRules ());
    assertEquals (0, aCSS.getKeyframesRuleCount ());
    assertTrue (aCSS.getAllKeyframesRules ().isEmpty ());

    assertFalse (aCSS.hasViewportRules ());
    assertEquals (0, aCSS.getViewportRuleCount ());
    assertTrue (aCSS.getAllViewportRules ().isEmpty ());

    assertFalse (aCSS.hasSupportsRules ());
    assertEquals (0, aCSS.getSupportsRuleCount ());
    assertTrue (aCSS.getAllSupportsRules ().isEmpty ());

    assertFalse (aCSS.hasUnknownRules ());
    assertEquals (0, aCSS.getUnknownRuleCount ());
    assertTrue (aCSS.getAllUnknownRules ().isEmpty ());
  }

  @Test
  public void testReadNamespaceOnly ()
  {
    CascadingStyleSheet aCSS;
    aCSS = _parse ("@namespace toto2 url(http://toto.example.org);");

    assertFalse (aCSS.hasImportRules ());
    assertEquals (0, aCSS.getImportRuleCount ());
    assertTrue (aCSS.getAllImportRules ().isEmpty ());

    assertTrue (aCSS.hasNamespaceRules ());
    assertEquals (1, aCSS.getNamespaceRuleCount ());
    assertNotNull (aCSS.getAllNamespaceRules ().get (0));

    assertFalse (aCSS.hasStyleRules ());
    assertEquals (0, aCSS.getStyleRuleCount ());
    assertTrue (aCSS.getAllStyleRules ().isEmpty ());

    assertFalse (aCSS.hasPageRules ());
    assertEquals (0, aCSS.getPageRuleCount ());
    assertTrue (aCSS.getAllPageRules ().isEmpty ());

    assertFalse (aCSS.hasMediaRules ());
    assertEquals (0, aCSS.getMediaRuleCount ());
    assertTrue (aCSS.getAllMediaRules ().isEmpty ());

    assertFalse (aCSS.hasFontFaceRules ());
    assertEquals (0, aCSS.getFontFaceRuleCount ());
    assertTrue (aCSS.getAllFontFaceRules ().isEmpty ());

    assertFalse (aCSS.hasKeyframesRules ());
    assertEquals (0, aCSS.getKeyframesRuleCount ());
    assertTrue (aCSS.getAllKeyframesRules ().isEmpty ());

    assertFalse (aCSS.hasViewportRules ());
    assertEquals (0, aCSS.getViewportRuleCount ());
    assertTrue (aCSS.getAllViewportRules ().isEmpty ());

    assertFalse (aCSS.hasSupportsRules ());
    assertEquals (0, aCSS.getSupportsRuleCount ());
    assertTrue (aCSS.getAllSupportsRules ().isEmpty ());

    assertFalse (aCSS.hasUnknownRules ());
    assertEquals (0, aCSS.getUnknownRuleCount ());
    assertTrue (aCSS.getAllUnknownRules ().isEmpty ());
  }

  @Test
  public void testReadStyleOnly ()
  {
    CascadingStyleSheet aCSS;
    aCSS = _parse ("div { color: red; }");

    assertFalse (aCSS.hasImportRules ());
    assertEquals (0, aCSS.getImportRuleCount ());
    assertTrue (aCSS.getAllImportRules ().isEmpty ());

    assertFalse (aCSS.hasNamespaceRules ());
    assertEquals (0, aCSS.getNamespaceRuleCount ());
    assertTrue (aCSS.getAllNamespaceRules ().isEmpty ());

    assertTrue (aCSS.hasStyleRules ());
    assertEquals (1, aCSS.getStyleRuleCount ());
    assertNotNull (aCSS.getAllStyleRules ().get (0));

    assertFalse (aCSS.hasPageRules ());
    assertEquals (0, aCSS.getPageRuleCount ());
    assertTrue (aCSS.getAllPageRules ().isEmpty ());

    assertFalse (aCSS.hasMediaRules ());
    assertEquals (0, aCSS.getMediaRuleCount ());
    assertTrue (aCSS.getAllMediaRules ().isEmpty ());

    assertFalse (aCSS.hasFontFaceRules ());
    assertEquals (0, aCSS.getFontFaceRuleCount ());
    assertTrue (aCSS.getAllFontFaceRules ().isEmpty ());

    assertFalse (aCSS.hasKeyframesRules ());
    assertEquals (0, aCSS.getKeyframesRuleCount ());
    assertTrue (aCSS.getAllKeyframesRules ().isEmpty ());

    assertFalse (aCSS.hasViewportRules ());
    assertEquals (0, aCSS.getViewportRuleCount ());
    assertTrue (aCSS.getAllViewportRules ().isEmpty ());

    assertFalse (aCSS.hasSupportsRules ());
    assertEquals (0, aCSS.getSupportsRuleCount ());
    assertTrue (aCSS.getAllSupportsRules ().isEmpty ());

    assertFalse (aCSS.hasUnknownRules ());
    assertEquals (0, aCSS.getUnknownRuleCount ());
    assertTrue (aCSS.getAllUnknownRules ().isEmpty ());
  }

  @Test
  public void testReadPageOnly ()
  {
    CascadingStyleSheet aCSS;
    aCSS = _parse ("@page { size: 8.5in 11in; }");

    assertFalse (aCSS.hasImportRules ());
    assertEquals (0, aCSS.getImportRuleCount ());
    assertTrue (aCSS.getAllImportRules ().isEmpty ());

    assertFalse (aCSS.hasNamespaceRules ());
    assertEquals (0, aCSS.getNamespaceRuleCount ());
    assertTrue (aCSS.getAllNamespaceRules ().isEmpty ());

    assertFalse (aCSS.hasStyleRules ());
    assertEquals (0, aCSS.getStyleRuleCount ());
    assertTrue (aCSS.getAllStyleRules ().isEmpty ());

    assertTrue (aCSS.hasPageRules ());
    assertEquals (1, aCSS.getPageRuleCount ());
    assertNotNull (aCSS.getAllPageRules ().get (0));

    assertFalse (aCSS.hasMediaRules ());
    assertEquals (0, aCSS.getMediaRuleCount ());
    assertTrue (aCSS.getAllMediaRules ().isEmpty ());

    assertFalse (aCSS.hasFontFaceRules ());
    assertEquals (0, aCSS.getFontFaceRuleCount ());
    assertTrue (aCSS.getAllFontFaceRules ().isEmpty ());

    assertFalse (aCSS.hasKeyframesRules ());
    assertEquals (0, aCSS.getKeyframesRuleCount ());
    assertTrue (aCSS.getAllKeyframesRules ().isEmpty ());

    assertFalse (aCSS.hasViewportRules ());
    assertEquals (0, aCSS.getViewportRuleCount ());
    assertTrue (aCSS.getAllViewportRules ().isEmpty ());

    assertFalse (aCSS.hasSupportsRules ());
    assertEquals (0, aCSS.getSupportsRuleCount ());
    assertTrue (aCSS.getAllSupportsRules ().isEmpty ());

    assertFalse (aCSS.hasUnknownRules ());
    assertEquals (0, aCSS.getUnknownRuleCount ());
    assertTrue (aCSS.getAllUnknownRules ().isEmpty ());
  }

  @Test
  public void testReadMediaOnly ()
  {
    CascadingStyleSheet aCSS;
    aCSS = _parse ("@media ( min-width :450px) and (max-width:950px) { }");

    assertFalse (aCSS.hasImportRules ());
    assertEquals (0, aCSS.getImportRuleCount ());
    assertTrue (aCSS.getAllImportRules ().isEmpty ());

    assertFalse (aCSS.hasNamespaceRules ());
    assertEquals (0, aCSS.getNamespaceRuleCount ());
    assertTrue (aCSS.getAllNamespaceRules ().isEmpty ());

    assertFalse (aCSS.hasStyleRules ());
    assertEquals (0, aCSS.getStyleRuleCount ());
    assertTrue (aCSS.getAllStyleRules ().isEmpty ());

    assertFalse (aCSS.hasPageRules ());
    assertEquals (0, aCSS.getPageRuleCount ());
    assertTrue (aCSS.getAllPageRules ().isEmpty ());

    assertTrue (aCSS.hasMediaRules ());
    assertEquals (1, aCSS.getMediaRuleCount ());
    assertNotNull (aCSS.getAllMediaRules ().get (0));

    assertFalse (aCSS.hasFontFaceRules ());
    assertEquals (0, aCSS.getFontFaceRuleCount ());
    assertTrue (aCSS.getAllFontFaceRules ().isEmpty ());

    assertFalse (aCSS.hasKeyframesRules ());
    assertEquals (0, aCSS.getKeyframesRuleCount ());
    assertTrue (aCSS.getAllKeyframesRules ().isEmpty ());

    assertFalse (aCSS.hasViewportRules ());
    assertEquals (0, aCSS.getViewportRuleCount ());
    assertTrue (aCSS.getAllViewportRules ().isEmpty ());

    assertFalse (aCSS.hasSupportsRules ());
    assertEquals (0, aCSS.getSupportsRuleCount ());
    assertTrue (aCSS.getAllSupportsRules ().isEmpty ());

    assertFalse (aCSS.hasUnknownRules ());
    assertEquals (0, aCSS.getUnknownRuleCount ());
    assertTrue (aCSS.getAllUnknownRules ().isEmpty ());
  }

  @Test
  public void testReadFontFaceOnly ()
  {
    CascadingStyleSheet aCSS;
    aCSS = _parse ("@font-face { font-family: JapaneseWithGentium; src: local(MSMincho); }");

    assertFalse (aCSS.hasImportRules ());
    assertEquals (0, aCSS.getImportRuleCount ());
    assertTrue (aCSS.getAllImportRules ().isEmpty ());

    assertFalse (aCSS.hasNamespaceRules ());
    assertEquals (0, aCSS.getNamespaceRuleCount ());
    assertTrue (aCSS.getAllNamespaceRules ().isEmpty ());

    assertFalse (aCSS.hasStyleRules ());
    assertEquals (0, aCSS.getStyleRuleCount ());
    assertTrue (aCSS.getAllStyleRules ().isEmpty ());

    assertFalse (aCSS.hasPageRules ());
    assertEquals (0, aCSS.getPageRuleCount ());
    assertTrue (aCSS.getAllPageRules ().isEmpty ());

    assertFalse (aCSS.hasMediaRules ());
    assertEquals (0, aCSS.getMediaRuleCount ());
    assertTrue (aCSS.getAllMediaRules ().isEmpty ());

    assertTrue (aCSS.hasFontFaceRules ());
    assertEquals (1, aCSS.getFontFaceRuleCount ());
    assertNotNull (aCSS.getAllFontFaceRules ().get (0));

    assertFalse (aCSS.hasKeyframesRules ());
    assertEquals (0, aCSS.getKeyframesRuleCount ());
    assertTrue (aCSS.getAllKeyframesRules ().isEmpty ());

    assertFalse (aCSS.hasViewportRules ());
    assertEquals (0, aCSS.getViewportRuleCount ());
    assertTrue (aCSS.getAllViewportRules ().isEmpty ());

    assertFalse (aCSS.hasSupportsRules ());
    assertEquals (0, aCSS.getSupportsRuleCount ());
    assertTrue (aCSS.getAllSupportsRules ().isEmpty ());

    assertFalse (aCSS.hasUnknownRules ());
    assertEquals (0, aCSS.getUnknownRuleCount ());
    assertTrue (aCSS.getAllUnknownRules ().isEmpty ());
  }

  @Test
  public void testReadKeyframesOnly ()
  {
    CascadingStyleSheet aCSS;
    aCSS = _parse ("@-webkit-keyframes travel { from { } to { left: 640px; } }");

    assertFalse (aCSS.hasImportRules ());
    assertEquals (0, aCSS.getImportRuleCount ());
    assertTrue (aCSS.getAllImportRules ().isEmpty ());

    assertFalse (aCSS.hasNamespaceRules ());
    assertEquals (0, aCSS.getNamespaceRuleCount ());
    assertTrue (aCSS.getAllNamespaceRules ().isEmpty ());

    assertFalse (aCSS.hasStyleRules ());
    assertEquals (0, aCSS.getStyleRuleCount ());
    assertTrue (aCSS.getAllStyleRules ().isEmpty ());

    assertFalse (aCSS.hasPageRules ());
    assertEquals (0, aCSS.getPageRuleCount ());
    assertTrue (aCSS.getAllPageRules ().isEmpty ());

    assertFalse (aCSS.hasMediaRules ());
    assertEquals (0, aCSS.getMediaRuleCount ());
    assertTrue (aCSS.getAllMediaRules ().isEmpty ());

    assertFalse (aCSS.hasFontFaceRules ());
    assertEquals (0, aCSS.getFontFaceRuleCount ());
    assertTrue (aCSS.getAllFontFaceRules ().isEmpty ());

    assertTrue (aCSS.hasKeyframesRules ());
    assertEquals (1, aCSS.getKeyframesRuleCount ());
    assertNotNull (aCSS.getAllKeyframesRules ().get (0));

    assertFalse (aCSS.hasViewportRules ());
    assertEquals (0, aCSS.getViewportRuleCount ());
    assertTrue (aCSS.getAllViewportRules ().isEmpty ());

    assertFalse (aCSS.hasSupportsRules ());
    assertEquals (0, aCSS.getSupportsRuleCount ());
    assertTrue (aCSS.getAllSupportsRules ().isEmpty ());

    assertFalse (aCSS.hasUnknownRules ());
    assertEquals (0, aCSS.getUnknownRuleCount ());
    assertTrue (aCSS.getAllUnknownRules ().isEmpty ());
  }

  @Test
  public void testReadViewportOnly ()
  {
    CascadingStyleSheet aCSS;
    aCSS = _parse ("@viewport { width: device-width; }");

    assertFalse (aCSS.hasImportRules ());
    assertEquals (0, aCSS.getImportRuleCount ());
    assertTrue (aCSS.getAllImportRules ().isEmpty ());

    assertFalse (aCSS.hasNamespaceRules ());
    assertEquals (0, aCSS.getNamespaceRuleCount ());
    assertTrue (aCSS.getAllNamespaceRules ().isEmpty ());

    assertFalse (aCSS.hasStyleRules ());
    assertEquals (0, aCSS.getStyleRuleCount ());
    assertTrue (aCSS.getAllStyleRules ().isEmpty ());

    assertFalse (aCSS.hasPageRules ());
    assertEquals (0, aCSS.getPageRuleCount ());
    assertTrue (aCSS.getAllPageRules ().isEmpty ());

    assertFalse (aCSS.hasMediaRules ());
    assertEquals (0, aCSS.getMediaRuleCount ());
    assertTrue (aCSS.getAllMediaRules ().isEmpty ());

    assertFalse (aCSS.hasFontFaceRules ());
    assertEquals (0, aCSS.getFontFaceRuleCount ());
    assertTrue (aCSS.getAllFontFaceRules ().isEmpty ());

    assertFalse (aCSS.hasKeyframesRules ());
    assertEquals (0, aCSS.getKeyframesRuleCount ());
    assertTrue (aCSS.getAllKeyframesRules ().isEmpty ());

    assertTrue (aCSS.hasViewportRules ());
    assertEquals (1, aCSS.getViewportRuleCount ());
    assertNotNull (aCSS.getAllViewportRules ().get (0));

    assertFalse (aCSS.hasSupportsRules ());
    assertEquals (0, aCSS.getSupportsRuleCount ());
    assertTrue (aCSS.getAllSupportsRules ().isEmpty ());

    assertFalse (aCSS.hasUnknownRules ());
    assertEquals (0, aCSS.getUnknownRuleCount ());
    assertTrue (aCSS.getAllUnknownRules ().isEmpty ());
  }

  @Test
  public void testReadSupportsOnly ()
  {
    CascadingStyleSheet aCSS;
    aCSS = _parse ("@supports (column-count: 1) and (background-image: linear-gradient(#f00,#00f)) { }");

    assertFalse (aCSS.hasImportRules ());
    assertEquals (0, aCSS.getImportRuleCount ());
    assertTrue (aCSS.getAllImportRules ().isEmpty ());

    assertFalse (aCSS.hasNamespaceRules ());
    assertEquals (0, aCSS.getNamespaceRuleCount ());
    assertTrue (aCSS.getAllNamespaceRules ().isEmpty ());

    assertFalse (aCSS.hasStyleRules ());
    assertEquals (0, aCSS.getStyleRuleCount ());
    assertTrue (aCSS.getAllStyleRules ().isEmpty ());

    assertFalse (aCSS.hasPageRules ());
    assertEquals (0, aCSS.getPageRuleCount ());
    assertTrue (aCSS.getAllPageRules ().isEmpty ());

    assertFalse (aCSS.hasMediaRules ());
    assertEquals (0, aCSS.getMediaRuleCount ());
    assertTrue (aCSS.getAllMediaRules ().isEmpty ());

    assertFalse (aCSS.hasFontFaceRules ());
    assertEquals (0, aCSS.getFontFaceRuleCount ());
    assertTrue (aCSS.getAllFontFaceRules ().isEmpty ());

    assertFalse (aCSS.hasKeyframesRules ());
    assertEquals (0, aCSS.getKeyframesRuleCount ());
    assertTrue (aCSS.getAllKeyframesRules ().isEmpty ());

    assertFalse (aCSS.hasViewportRules ());
    assertEquals (0, aCSS.getViewportRuleCount ());
    assertTrue (aCSS.getAllViewportRules ().isEmpty ());

    assertTrue (aCSS.hasSupportsRules ());
    assertEquals (1, aCSS.getSupportsRuleCount ());
    assertNotNull (aCSS.getAllSupportsRules ().get (0));

    assertFalse (aCSS.hasUnknownRules ());
    assertEquals (0, aCSS.getUnknownRuleCount ());
    assertTrue (aCSS.getAllUnknownRules ().isEmpty ());
  }

  @Test
  public void testReadUnknownOnly ()
  {
    CascadingStyleSheet aCSS;
    aCSS = _parse ("@-moz-document    anything else or whatever 4711    {   }");

    assertFalse (aCSS.hasImportRules ());
    assertEquals (0, aCSS.getImportRuleCount ());
    assertTrue (aCSS.getAllImportRules ().isEmpty ());

    assertFalse (aCSS.hasNamespaceRules ());
    assertEquals (0, aCSS.getNamespaceRuleCount ());
    assertTrue (aCSS.getAllNamespaceRules ().isEmpty ());

    assertFalse (aCSS.hasStyleRules ());
    assertEquals (0, aCSS.getStyleRuleCount ());
    assertTrue (aCSS.getAllStyleRules ().isEmpty ());

    assertFalse (aCSS.hasPageRules ());
    assertEquals (0, aCSS.getPageRuleCount ());
    assertTrue (aCSS.getAllPageRules ().isEmpty ());

    assertFalse (aCSS.hasMediaRules ());
    assertEquals (0, aCSS.getMediaRuleCount ());
    assertTrue (aCSS.getAllMediaRules ().isEmpty ());

    assertFalse (aCSS.hasFontFaceRules ());
    assertEquals (0, aCSS.getFontFaceRuleCount ());
    assertTrue (aCSS.getAllFontFaceRules ().isEmpty ());

    assertFalse (aCSS.hasKeyframesRules ());
    assertEquals (0, aCSS.getKeyframesRuleCount ());
    assertTrue (aCSS.getAllKeyframesRules ().isEmpty ());

    assertFalse (aCSS.hasViewportRules ());
    assertEquals (0, aCSS.getViewportRuleCount ());
    assertTrue (aCSS.getAllViewportRules ().isEmpty ());

    assertFalse (aCSS.hasSupportsRules ());
    assertEquals (0, aCSS.getSupportsRuleCount ());
    assertTrue (aCSS.getAllSupportsRules ().isEmpty ());

    assertTrue (aCSS.hasUnknownRules ());
    assertEquals (1, aCSS.getUnknownRuleCount ());
    assertNotNull (aCSS.getAllUnknownRules ().get (0));
  }

  @Test
  public void testReadOneOfAll ()
  {
    CascadingStyleSheet aCSS;
    aCSS = _parse ("@import url(a.gif);\n" +
                   "@namespace toto2 url(http://toto.example.org);\n" +
                   "div { color: red; }\n" +
                   "@page { size: 8.5in 11in; }\n" +
                   "@media ( min-width :450px) and (max-width:950px) { }\n" +
                   "@font-face { font-family: JapaneseWithGentium; src: local(MSMincho); }\n" +
                   "@keyframes travel { from { } to { left: 640px; } }\n" +
                   "@viewport { width: device-width; }\n" +
                   "@supports (column-count: 1) and (background-image: linear-gradient(#f00,#00f)) { }\n" +
                   "@document    anything else or whatever 4711    {   }\n");

    assertTrue (aCSS.hasImportRules ());
    assertEquals (1, aCSS.getImportRuleCount ());
    assertNotNull (aCSS.getAllImportRules ().get (0));

    assertTrue (aCSS.hasNamespaceRules ());
    assertEquals (1, aCSS.getNamespaceRuleCount ());
    assertNotNull (aCSS.getAllNamespaceRules ().get (0));

    assertTrue (aCSS.hasStyleRules ());
    assertEquals (1, aCSS.getStyleRuleCount ());
    assertNotNull (aCSS.getAllStyleRules ().get (0));

    assertTrue (aCSS.hasPageRules ());
    assertEquals (1, aCSS.getPageRuleCount ());
    assertNotNull (aCSS.getAllPageRules ().get (0));

    assertTrue (aCSS.hasMediaRules ());
    assertEquals (1, aCSS.getMediaRuleCount ());
    assertNotNull (aCSS.getAllMediaRules ().get (0));

    assertTrue (aCSS.hasFontFaceRules ());
    assertEquals (1, aCSS.getFontFaceRuleCount ());
    assertNotNull (aCSS.getAllFontFaceRules ().get (0));

    assertTrue (aCSS.hasKeyframesRules ());
    assertEquals (1, aCSS.getKeyframesRuleCount ());
    assertNotNull (aCSS.getAllKeyframesRules ().get (0));

    assertTrue (aCSS.hasViewportRules ());
    assertEquals (1, aCSS.getViewportRuleCount ());
    assertNotNull (aCSS.getAllViewportRules ().get (0));

    assertTrue (aCSS.hasSupportsRules ());
    assertEquals (1, aCSS.getSupportsRuleCount ());
    assertNotNull (aCSS.getAllSupportsRules ().get (0));

    assertTrue (aCSS.hasUnknownRules ());
    assertEquals (1, aCSS.getUnknownRuleCount ());
    assertNotNull (aCSS.getAllUnknownRules ().get (0));
  }
}
