/**
 * Copyright (C) 2014-2015 Philip Helger (www.helger.com)
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

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.helger.commons.charset.CCharset;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.writer.CSSWriter;

/**
 * Test reading CSS 2.1 files
 *
 * @author Philip Helger
 */
@RunWith (Parameterized.class)
public final class CSSReader21FuncTest extends AbstractFuncTestCSSReader
{
  @Parameters (name = "{index}: browserCompliant={0}")
  public static List <Object> data ()
  {
    return Arrays.asList (new Object [] { true, false });
  }

  public CSSReader21FuncTest (final boolean bBrowserCompliant)
  {
    super (ECSSVersion.CSS21, CCharset.CHARSET_UTF_8_OBJ, false, bBrowserCompliant);
  }

  @Test
  public void testReadAll21Good ()
  {
    testReadGood ("src/test/resources/testfiles/css21/good");
    testReadGood ("src/test/resources/testfiles/css21/bad_but_succeeding");
  }

  @Test
  public void testReadAll21Bad ()
  {
    testReadBad ("src/test/resources/testfiles/css21/bad");
    testReadBad ("src/test/resources/testfiles/css21/good_but_failing");
  }

  @Test
  public void testReadAll21BadButRecoverable ()
  {
    testReadBadButRecoverable ("src/test/resources/testfiles/css21/bad_but_recoverable");
  }

  @Test
  public void testReadAll21BadButBrowserCompliant ()
  {
    testReadBadButBrowserCompliant ("src/test/resources/testfiles/css21/bad_but_browsercompliant");
  }

  @Test
  public void testReadSpecialGood ()
  {
    final ECSSVersion eVersion = ECSSVersion.CSS30;
    final Charset aCharset = CCharset.CHARSET_UTF_8_OBJ;
    final File aFile = new File ("src/test/resources/testfiles/css21/good/artificial/test-url.css");
    final CascadingStyleSheet aCSS = CSSReader.readFromFile (aFile, aCharset, eVersion);
    assertNotNull (aCSS);

    final String sCSS = new CSSWriter (eVersion, false).getCSSAsString (aCSS);
    m_aLogger.info (sCSS);
  }
}
