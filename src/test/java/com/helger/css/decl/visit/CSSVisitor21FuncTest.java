/**
 * Copyright (C) 2014-2016 Philip Helger (www.helger.com)
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
import com.helger.css.AbstractCSS21TestCase;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;

/**
 * Test class for class {@link CSSVisitor}.
 *
 * @author Philip Helger
 */
public final class CSSVisitor21FuncTest extends AbstractCSS21TestCase
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (CSSVisitor21FuncTest.class);

  @Test
  public void testVisitContent21 ()
  {
    for (final File aFile : new FileSystemRecursiveIterator (new File ("src/test/resources/testfiles/css21/good")).withFilter (IFileFilter.filenameEndsWith (".css")))
    {
      final String sKey = aFile.getAbsolutePath ();
      if (false)
        s_aLogger.info (sKey);
      final CascadingStyleSheet aCSS = CSSReader.readFromFile (aFile, CCharset.CHARSET_UTF_8_OBJ, ECSSVersion.CSS21);
      assertNotNull (sKey, aCSS);
      CSSVisitor.visitCSSUrl (aCSS, new MockUrlVisitor (sKey));
    }
  }

  @Test
  public void testVisitConstantCSS ()
  {
    // CSS 1
    CascadingStyleSheet aCSS = CSSReader.readFromString (CSS1, ECSSVersion.CSS21);
    assertNotNull (aCSS);
    MockCountingUrlVisitor aVisitor = new MockCountingUrlVisitor ();
    CSSVisitor.visitCSSUrl (aCSS, aVisitor);
    assertEquals (4, aVisitor.getCount ());

    // CSS 2
    aCSS = CSSReader.readFromString (CSS2, ECSSVersion.CSS21);
    assertNotNull (aCSS);
    aVisitor = new MockCountingUrlVisitor ();
    CSSVisitor.visitCSSUrl (aCSS, aVisitor);
    assertEquals (2, aVisitor.getCount ());
  }
}
