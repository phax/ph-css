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
package com.helger.css.supplementary.wiki;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.charset.CCharset;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;

/**
 * This is example code to read a CSS declaration from a {@link File}.
 *
 * @author Philip Helger
 */
public final class WikiReadCSS
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (WikiReadCSS.class);

  /**
   * Read a CSS 3.0 declaration from a file using UTF-8 encoding.
   *
   * @param aFile
   *        The file to be read. May not be <code>null</code>.
   * @return <code>null</code> if the file has syntax errors.
   */
  public static CascadingStyleSheet readCSS30 (final File aFile)
  {
    // UTF-8 is the fallback if neither a BOM nor @charset rule is present
    final CascadingStyleSheet aCSS = CSSReader.readFromFile (aFile, CCharset.CHARSET_UTF_8_OBJ, ECSSVersion.CSS30);
    if (aCSS == null)
    {
      // Most probably a syntax error
      s_aLogger.warn ("Failed to read CSS - please see previous logging entries!");
      return null;
    }
    return aCSS;
  }
}
