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
package com.helger.css.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.charset.CCharset;
import com.helger.commons.io.file.filter.IFileFilter;
import com.helger.commons.io.file.iterate.FileSystemRecursiveIterator;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.stream.NonBlockingStringWriter;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;

public final class CSSWriterFuncTest
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (CSSWriterFuncTest.class);

  private void _testMe (@Nonnull final File aFile, @Nonnull final ECSSVersion eVersion)
  {
    if (false)
      s_aLogger.info (aFile.getAbsolutePath ());

    // read and interpret
    final CascadingStyleSheet aCSS = CSSReader.readFromFile (aFile, CCharset.CHARSET_UTF_8_OBJ, eVersion);
    assertNotNull (aFile.getAbsolutePath (), aCSS);

    // Both normal and optimized!
    for (int i = 0; i < 2; ++i)
    {
      // write to buffer
      final String sCSS = new CSSWriter (eVersion, i == 1).getCSSAsString (aCSS);
      if (true)
        System.out.println ("--" + i + "--\n" + sCSS);

      // read again from buffer
      assertEquals (aFile.getAbsolutePath () +
                    (i == 0 ? " unoptimized" : " optimized"),
                    aCSS,
                    CSSReader.readFromString (sCSS, eVersion));
    }
  }

  @Test
  public void testScanTestResourcesHandler21 ()
  {
    for (final File aFile : new FileSystemRecursiveIterator (new File ("src/test/resources/testfiles/css21/good/artificial")).withFilter (IFileFilter.filenameEndsWith (".css")))
    {
      _testMe (aFile, ECSSVersion.CSS30);
    }
  }

  @Test
  public void testScanTestResourcesHandler30 ()
  {
    for (final File aFile : new FileSystemRecursiveIterator (new File ("src/test/resources/testfiles/css30/good/artificial")).withFilter (IFileFilter.filenameEndsWith (".css")))
    {
      _testMe (aFile, ECSSVersion.CSS30);
    }
  }

  @Test
  public void testRead30Write21 () throws IOException
  {
    for (final File aFile : new FileSystemRecursiveIterator (new File ("src/test/resources/testfiles/css30/good/artificial")).withFilter (IFileFilter.filenameEndsWith (".css")))
    {
      final String sKey = aFile.getAbsolutePath ();
      try
      {
        // read and interpret CSS 3.0
        final CascadingStyleSheet aCSS = CSSReader.readFromFile (aFile, CCharset.CHARSET_UTF_8_OBJ, ECSSVersion.CSS30);
        assertNotNull (sKey, aCSS);

        // write to CSS 2.1
        final NonBlockingStringWriter aSW = new NonBlockingStringWriter ();
        new CSSWriter (ECSSVersion.CSS21).writeCSS (aCSS, aSW);

        // This should throw an error
        fail (sKey + " should have thrown an exception but got: " + aSW.getAsString ());
      }
      catch (final IllegalStateException ex)
      {}
    }
  }

  @Test
  public void testCompressCSS_Size ()
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromStream (new ClassPathResource ("/testfiles/css21/good/phloc/test/content.css"),
                                                               CCharset.CHARSET_UTF_8_OBJ,
                                                               ECSSVersion.CSS30);
    assertNotNull (aCSS);

    // Only whitespace optimization
    final CSSWriterSettings aSettings = new CSSWriterSettings (ECSSVersion.CSS21, true);
    String sContent = new CSSWriter (aSettings).getCSSAsString (aCSS);
    assertEquals (2846, sContent.length ());

    // Also remove empty declarations
    aSettings.setRemoveUnnecessaryCode (true);
    sContent = new CSSWriter (aSettings).getCSSAsString (aCSS);
    assertEquals (2839, sContent.length ());
  }
}
