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
package com.helger.css;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.concurrent.Immutable;
import com.helger.annotation.style.PresentForCodeCoverage;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.string.StringHelper;

/**
 * Utility methods to deal with CSS filenames.
 *
 * @author Philip Helger
 */
@Immutable
public final class CSSFilenameHelper
{
  @PresentForCodeCoverage
  private static final CSSFilenameHelper INSTANCE = new CSSFilenameHelper ();

  private CSSFilenameHelper ()
  {}

  /**
   * Check if the passed filename is a CSS filename (independent if regular or
   * minified)
   *
   * @param sFilename
   *        The filename to check.
   * @return <code>true</code> if the passed filename is a CSS filename.
   */
  public static boolean isCSSFilename (@Nullable final String sFilename)
  {
    return StringHelper.endsWith (sFilename, CCSS.FILE_EXTENSION_CSS);
  }

  /**
   * Check if the passed filename is a minified CSS filename
   *
   * @param sFilename
   *        The filename to check.
   * @return <code>true</code> if the passed filename is a minified CSS
   *         filename.
   */
  public static boolean isMinifiedCSSFilename (@Nullable final String sFilename)
  {
    return StringHelper.endsWith (sFilename, CCSS.FILE_EXTENSION_MIN_CSS);
  }

  /**
   * Check if the passed filename is a regular (not minified) CSS filename
   *
   * @param sFilename
   *        The filename to check.
   * @return <code>true</code> if the passed filename is a regular CSS filename.
   */
  public static boolean isRegularCSSFilename (@Nullable final String sFilename)
  {
    return isCSSFilename (sFilename) && !isMinifiedCSSFilename (sFilename);
  }

  /**
   * Get the minified CSS filename from the passed filename. If the passed
   * filename is already minified, it is returned as is.
   *
   * @param sCSSFilename
   *        The filename to get minified. May not be <code>null</code>.
   * @return The minified filename
   */
  @NonNull
  public static String getMinifiedCSSFilename (@NonNull final String sCSSFilename)
  {
    ValueEnforcer.isTrue (isCSSFilename (sCSSFilename), "Passed file name '" + sCSSFilename + "' is not a CSS file name!");
    if (isMinifiedCSSFilename (sCSSFilename))
      return sCSSFilename;
    return StringHelper.trimEnd (sCSSFilename, CCSS.FILE_EXTENSION_CSS) + CCSS.FILE_EXTENSION_MIN_CSS;
  }
}
