/*
 * Copyright (C) 2014-2023 Philip Helger (www.helger.com)
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
package com.helger.css.supplementary.wiki;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.css.writer.CSSWriter;

/**
 * This is example code to create a font-face rule from scratch
 *
 * @author Philip Helger
 */
public final class WikiCreateFontFaceRuleFuncTest
{
  @Test
  public void testBasic ()
  {
    final CascadingStyleSheet aCSS = WikiCreateFontFaceRule.createFontFace ("Your \"typeface\"", "local font name", "folder/", "myfont");
    final String sCSS = new CSSWriter (ECSSVersion.CSS30).getCSSAsString (aCSS);
    System.out.println (sCSS);

    final CascadingStyleSheet aCSS2 = CSSReader.readFromString (sCSS, ECSSVersion.CSS30);
    assertNotNull (aCSS2);
    assertEquals (aCSS, aCSS2);
  }
}
