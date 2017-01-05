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
package com.helger.css.decl.visit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.charset.CCharset;
import com.helger.commons.io.file.filter.IFileFilter;
import com.helger.commons.io.file.iterate.FileSystemRecursiveIterator;
import com.helger.css.AbstractCSS30TestCase;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.reader.errorhandler.LoggingCSSParseErrorHandler;

/**
 * Test class for class {@link CSSVisitor}.
 *
 * @author Philip Helger
 */
public final class CSSVisitor30FuncTest extends AbstractCSS30TestCase
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (CSSVisitor30FuncTest.class);

  @Test
  public void testVisitContent30 ()
  {
    for (final File aFile : new FileSystemRecursiveIterator (new File ("src/test/resources/testfiles/css30/good")).withFilter (IFileFilter.filenameEndsWith (".css")))
    {
      final String sKey = aFile.getAbsolutePath ();
      if (true)
        s_aLogger.info (sKey);
      final CascadingStyleSheet aCSS = CSSReader.readFromFile (aFile,
                                                               new CSSReaderSettings ().setFallbackCharset (CCharset.CHARSET_UTF_8_OBJ)
                                                                                       .setCSSVersion (ECSSVersion.CSS30)
                                                                                       .setCustomErrorHandler (new LoggingCSSParseErrorHandler ())
                                                                                       .setBrowserCompliantMode (true));
      assertNotNull (sKey, aCSS);
      CSSVisitor.visitCSSUrl (aCSS, new MockUrlVisitor (sKey));
    }
  }

  @Test
  public void testVisitConstantCSS ()
  {
    // CSS 1
    CascadingStyleSheet aCSS = CSSReader.readFromString (CSS1, ECSSVersion.CSS30);
    assertNotNull (aCSS);
    MockCountingUrlVisitor aVisitor = new MockCountingUrlVisitor ();
    CSSVisitor.visitCSSUrl (aCSS, aVisitor);
    assertEquals (4, aVisitor.getCount ());

    // CSS 2
    aCSS = CSSReader.readFromString (CSS2, ECSSVersion.CSS30);
    assertNotNull (aCSS);
    aVisitor = new MockCountingUrlVisitor ();
    CSSVisitor.visitCSSUrl (aCSS, aVisitor);
    assertEquals (18, aVisitor.getCount ());

    // CSS 3
    aCSS = CSSReader.readFromString (CSS3, ECSSVersion.CSS30);
    assertNotNull (aCSS);
    aVisitor = new MockCountingUrlVisitor ();
    CSSVisitor.visitCSSUrl (aCSS, aVisitor);
    assertEquals (1, aVisitor.getCount ());

    // CSS 4
    aCSS = CSSReader.readFromString (CSS4, ECSSVersion.CSS30);
    assertNotNull (aCSS);
    aVisitor = new MockCountingUrlVisitor ();
    CSSVisitor.visitCSSUrl (aCSS, aVisitor);
    assertEquals (1, aVisitor.getCount ());

    // CSS 5
    aCSS = CSSReader.readFromString (CSS5, ECSSVersion.CSS30);
    assertNotNull (aCSS);
    aVisitor = new MockCountingUrlVisitor ();
    CSSVisitor.visitCSSUrl (aCSS, aVisitor);
    assertEquals (0, aVisitor.getCount ());

    // CSS 6
    aCSS = CSSReader.readFromString (CSS6, ECSSVersion.CSS30);
    assertNotNull (aCSS);
    aVisitor = new MockCountingUrlVisitor ();
    CSSVisitor.visitCSSUrl (aCSS, aVisitor);
    assertEquals (3, aVisitor.getCount ());
  }
}
