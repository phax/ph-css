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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.io.file.FileSystemRecursiveIterator;
import com.helger.io.file.IFileFilter;
import com.helger.io.resource.ClassPathResource;

import jakarta.annotation.Nonnull;

public final class CSSWriterFuncTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (CSSWriterFuncTest.class);

  private void _testMe (@Nonnull final File aFile)
  {
    if (false)
      LOGGER.info (aFile.getAbsolutePath ());

    // read and interpret
    final CascadingStyleSheet aCSS = CSSReader.readFromFile (aFile, StandardCharsets.UTF_8);
    assertNotNull (aFile.getAbsolutePath (), aCSS);

    // Both normal and optimized!
    for (int i = 0; i < 2; ++i)
    {
      // write to buffer
      final String sCSS = new CSSWriter (i == 1).getCSSAsString (aCSS);
      if (false)
        System.out.println ("--" + i + "--\n" + sCSS);

      // read again from buffer
      assertEquals (aFile.getAbsolutePath () + (i == 0 ? " unoptimized" : " optimized"),
                    aCSS,
                    CSSReader.readFromString (sCSS));
    }
  }

  @Test
  public void testScanTestResourcesHandler30 ()
  {
    for (final File aFile : new FileSystemRecursiveIterator (new File ("src/test/resources/testfiles/css30/good/artificial")).withFilter (IFileFilter.filenameEndsWith (".css")))
    {
      _testMe (aFile);
    }
  }

  @Test
  public void testCompressCSS_Size ()
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromStream (new ClassPathResource ("/testfiles/css30/good/phloc/test/content.css"),
                                                               StandardCharsets.UTF_8);
    assertNotNull (aCSS);

    // Only whitespace optimization
    final CSSWriterSettings aSettings = new CSSWriterSettings (true);
    String sContent = new CSSWriter (aSettings).getCSSAsString (aCSS);
    assertEquals (2846, sContent.length ());

    // Also remove empty declarations
    aSettings.setRemoveUnnecessaryCode (true);
    sContent = new CSSWriter (aSettings).getCSSAsString (aCSS);
    assertEquals (2839, sContent.length ());
  }
}
