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
package com.helger.css.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.io.file.filter.IFileFilter;
import com.helger.commons.io.file.iterate.FileSystemRecursiveIterator;
import com.helger.commons.mock.CommonsTestHelper;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.errorhandler.CollectingCSSParseErrorHandler;
import com.helger.css.reader.errorhandler.LoggingCSSParseErrorHandler;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Abstract class for reading multiple CSS files
 *
 * @author Philip Helger
 */
public abstract class AbstractFuncTestCSSReader
{
  protected final Logger m_aLogger = LoggerFactory.getLogger (getClass ());
  private final boolean m_bDebug;
  private final CSSReaderSettings m_aReaderSettings;
  private final CSSWriterSettings m_aWriterSettings;

  protected AbstractFuncTestCSSReader (@Nonnull final Charset aCharset,
                                       final boolean bDebug,
                                       final boolean bBrowserCompliantMode)
  {
    m_bDebug = bDebug;
    m_aReaderSettings = new CSSReaderSettings ().setFallbackCharset (aCharset)
                                                .setBrowserCompliantMode (bBrowserCompliantMode);
    m_aWriterSettings = new CSSWriterSettings ();
    if (m_bDebug)
      m_aLogger.info ("Running test in " + (bBrowserCompliantMode ? "browser compliant mode" : "strict mode"));
  }

  protected final boolean isBrowserCompliantMode ()
  {
    return m_aReaderSettings.isBrowserCompliantMode ();
  }

  protected final void testReadGood (@Nonnull final String sBaseDir)
  {
    final File aBaseDir = new File (sBaseDir);
    if (!aBaseDir.exists ())
      throw new IllegalArgumentException ("BaseDir " + sBaseDir + " does not exist!");

    for (final File aFile : new FileSystemRecursiveIterator (aBaseDir).withFilter (IFileFilter.filenameEndsWith (".css")))
    {
      final String sKey = aFile.getAbsolutePath ();
      if (m_bDebug)
        m_aLogger.info ("Filename: " + sKey);
      final CollectingCSSParseErrorHandler aErrorHdl = new CollectingCSSParseErrorHandler ();
      m_aReaderSettings.setCustomErrorHandler (aErrorHdl.and (new LoggingCSSParseErrorHandler ()));
      final CascadingStyleSheet aCSS = CSSReader.readFromFile (aFile, m_aReaderSettings);
      assertNotNull (sKey, aCSS);

      // May have errors or not
      if (m_bDebug)
        m_aLogger.info ("Parse errors: " + aErrorHdl.getAllParseErrors ().toString ());

      CommonsTestHelper.testDefaultSerialization (aCSS);

      // Write optimized version and compare it
      String sCSS = new CSSWriter (m_aWriterSettings.setOptimizedOutput (true)).getCSSAsString (aCSS);
      assertNotNull (sKey, sCSS);
      if (m_bDebug)
        m_aLogger.info ("Created CSS: " + sCSS);

      final CascadingStyleSheet aCSSReRead = CSSReader.readFromStringReader (sCSS, m_aReaderSettings);
      assertNotNull ("Failed to parse " + sKey + ":\n" + sCSS, aCSSReRead);
      assertEquals (sKey + "\n" + sCSS, aCSS, aCSSReRead);

      // Write non-optimized version and compare it
      sCSS = new CSSWriter (m_aWriterSettings.setOptimizedOutput (false)).getCSSAsString (aCSS);
      assertNotNull (sKey, sCSS);
      if (m_bDebug)
        m_aLogger.info ("Read and re-created CSS: " + sCSS);
      assertEquals (sKey, aCSS, CSSReader.readFromStringReader (sCSS, m_aReaderSettings));

      // Write non-optimized and code-removed version and ensure it is not
      // null
      sCSS = new CSSWriter (m_aWriterSettings.setOptimizedOutput (false)
                                             .setRemoveUnnecessaryCode (true)).getCSSAsString (aCSS);
      assertNotNull (sKey, sCSS);
      assertNotNull (sKey, CSSReader.readFromStringReader (sCSS, m_aReaderSettings));

      // Restore value :)
      m_aWriterSettings.setRemoveUnnecessaryCode (false);
    }
  }

  protected final void testReadBad (@Nonnull final String sBaseDir)
  {
    final File aBaseDir = new File (sBaseDir);
    if (!aBaseDir.exists ())
      throw new IllegalArgumentException ("BaseDir " + sBaseDir + " does not exist!");

    for (final File aFile : new FileSystemRecursiveIterator (aBaseDir).withFilter (IFileFilter.filenameEndsWith (".css")))
    {
      final String sKey = aFile.getAbsolutePath ();
      if (m_bDebug)
        m_aLogger.info (sKey);

      // Handle each error as a fatal error!
      final CascadingStyleSheet aCSS = CSSReader.readFromFile (aFile, m_aReaderSettings);
      assertNull (sKey, aCSS);
    }
  }

  protected final void testReadBadButRecoverable (@Nonnull final String sBaseDir)
  {
    final File aBaseDir = new File (sBaseDir);
    if (!aBaseDir.exists ())
      throw new IllegalArgumentException ("BaseDir " + sBaseDir + " does not exist!");

    for (final File aFile : new FileSystemRecursiveIterator (aBaseDir).withFilter (IFileFilter.filenameEndsWith (".css")))
    {
      final String sKey = aFile.getAbsolutePath ();
      if (m_bDebug)
        m_aLogger.info (sKey);

      // Handle each error as a fatal error!
      final CollectingCSSParseErrorHandler aErrorHdl = new CollectingCSSParseErrorHandler ();
      m_aReaderSettings.setCustomErrorHandler (aErrorHdl.and (new LoggingCSSParseErrorHandler ()));
      final CascadingStyleSheet aCSS = CSSReader.readFromFile (aFile, m_aReaderSettings);
      assertNotNull (sKey, aCSS);
      assertTrue (sKey, aErrorHdl.hasParseErrors ());
      assertTrue (sKey, aErrorHdl.getParseErrorCount () > 0);
      if (m_bDebug)
        m_aLogger.info (aErrorHdl.getAllParseErrors ().toString ());

      // Write optimized version and re-read it
      final String sCSS = new CSSWriter (m_aWriterSettings.setOptimizedOutput (true)).getCSSAsString (aCSS);
      assertNotNull (sKey, sCSS);
      if (m_bDebug)
        m_aLogger.info (sCSS);

      final CascadingStyleSheet aCSSReRead = CSSReader.readFromStringReader (sCSS, m_aReaderSettings);
      assertNotNull ("Failed to parse:\n" + sCSS, aCSSReRead);
      assertEquals (sKey, aCSS, aCSSReRead);
    }
  }

  protected final void testReadBadButBrowserCompliant (@Nonnull final String sBaseDir)
  {
    if (m_aReaderSettings.isBrowserCompliantMode ())
      testReadGood (sBaseDir);
    else
      testReadBad (sBaseDir);
  }

  protected final void testReadBadButRecoverableAndBrowserCompliant (@Nonnull final String sBaseDir)
  {
    if (m_aReaderSettings.isBrowserCompliantMode ())
      testReadGood (sBaseDir);
    else
      testReadBadButRecoverable (sBaseDir);
  }
}
