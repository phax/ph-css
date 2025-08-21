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
package com.helger.css.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.annotation.concurrent.Immutable;
import com.helger.annotation.style.PresentForCodeCoverage;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;

import jakarta.annotation.Nonnull;

/**
 * Utility class to compress CSS content
 *
 * @author Philip Helger
 */
@Immutable
public final class CSSCompressor
{
  private static final Logger LOGGER = LoggerFactory.getLogger (CSSCompressor.class);

  @PresentForCodeCoverage
  private static final CSSCompressor INSTANCE = new CSSCompressor ();

  private CSSCompressor ()
  {}

  /**
   * Get the compressed version of the passed CSS code.
   *
   * @param sOriginalCSS
   *        The original CSS code to be compressed.
   * @return If compression failed because the CSS is invalid or whatsoever, the original CSS is
   *         returned, else the compressed version is returned.
   */
  @Nonnull
  public static String getCompressedCSS (@Nonnull final String sOriginalCSS)
  {
    return getCompressedCSS (sOriginalCSS, false);
  }

  /**
   * Get the compressed version of the passed CSS code.
   *
   * @param sOriginalCSS
   *        The original CSS code to be compressed.
   * @param bRemoveUnnecessaryCode
   *        if <code>true</code> unnecessary empty declarations are omitted
   * @return If compression failed because the CSS is invalid or whatsoever, the original CSS is
   *         returned, else the compressed version is returned.
   */
  @Nonnull
  public static String getCompressedCSS (@Nonnull final String sOriginalCSS, final boolean bRemoveUnnecessaryCode)
  {
    final CSSWriterSettings aSettings = new CSSWriterSettings (true);
    aSettings.setRemoveUnnecessaryCode (bRemoveUnnecessaryCode);
    return getRewrittenCSS (sOriginalCSS, aSettings);
  }

  /**
   * Get the rewritten version of the passed CSS code. This is done by interpreting the CSS and than
   * writing it again with the passed settings. This can e.g. be used to create a compressed version
   * of a CSS.
   *
   * @param sOriginalCSS
   *        The original CSS code to be compressed.
   * @param aSettings
   *        The CSS writer settings to use. The version is used to read the original CSS.
   * @return If compression failed because the CSS is invalid or whatsoever, the original CSS is
   *         returned, else the rewritten version is returned.
   */
  @Nonnull
  public static String getRewrittenCSS (@Nonnull final String sOriginalCSS, @Nonnull final CSSWriterSettings aSettings)
  {
    ValueEnforcer.notNull (sOriginalCSS, "OriginalCSS");
    ValueEnforcer.notNull (aSettings, "Settings");

    final CascadingStyleSheet aCSS = CSSReader.readFromString (sOriginalCSS);
    if (aCSS != null)
    {
      try
      {
        return new CSSWriter (aSettings).getCSSAsString (aCSS);
      }
      catch (final Exception ex)
      {
        LOGGER.warn ("Failed to write optimized CSS!", ex);
      }
    }
    return sOriginalCSS;
  }
}
