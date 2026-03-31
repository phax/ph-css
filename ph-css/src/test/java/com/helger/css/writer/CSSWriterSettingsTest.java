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
package com.helger.css.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jspecify.annotations.NonNull;
import org.junit.Test;

import com.helger.css.AbstractCSSTestCase;
import com.helger.css.ICSSWriterSettings;

/**
 * Test class for class {@link CSSWriterSettings}.
 *
 * @author Philip Helger
 */
public final class CSSWriterSettingsTest extends AbstractCSSTestCase
{
  private static void _checkDefault (@NonNull final ICSSWriterSettings aSettings)
  {
    assertTrue (CSSWriterSettings.DEFAULT_OPTIMIZED_OUTPUT == aSettings.isOptimizedOutput ());
    assertTrue (CSSWriterSettings.DEFAULT_REMOVE_UNNECESSARY_CODE == aSettings.isRemoveUnnecessaryCode ());
    assertSame (CSSWriterSettings.DEFAULT_NEW_LINE_MODE, aSettings.getNewLineMode ());
    assertEquals (CSSWriterSettings.DEFAULT_INDENT, aSettings.getIndent (1));
    assertTrue (CSSWriterSettings.DEFAULT_QUOTE_URLS == aSettings.isQuoteURLs ());
    assertTrue (CSSWriterSettings.DEFAULT_WRITE_NAMESPACE_RULES == aSettings.isWriteNamespaceRules ());
    assertTrue (CSSWriterSettings.DEFAULT_WRITE_FONT_FACE_RULES == aSettings.isWriteFontFaceRules ());
    assertTrue (CSSWriterSettings.DEFAULT_WRITE_KEYFRAMES_RULES == aSettings.isWriteKeyframesRules ());
    assertTrue (CSSWriterSettings.DEFAULT_WRITE_MEDIA_RULES == aSettings.isWriteMediaRules ());
    assertTrue (CSSWriterSettings.DEFAULT_WRITE_PAGE_RULES == aSettings.isWritePageRules ());
    assertTrue (CSSWriterSettings.DEFAULT_WRITE_VIEWPORT_RULES == aSettings.isWriteViewportRules ());
    assertTrue (CSSWriterSettings.DEFAULT_WRITE_SUPPORTS_RULES == aSettings.isWriteSupportsRules ());
    assertTrue (CSSWriterSettings.DEFAULT_WRITE_UNKNOWN_RULES == aSettings.isWriteUnknownRules ());
  }

  @Test
  public void testDefault ()
  {
    final ICSSWriterSettings aSettings = CSSWriterSettings.DEFAULT_SETTINGS;
    assertNotNull (aSettings);
    _checkDefault (aSettings);
    _checkDefault (new CSSWriterSettings (aSettings));
  }
}
