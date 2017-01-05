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
package com.helger.css.supplementary.issues;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.reader.errorhandler.DoNothingCSSParseErrorHandler;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Test for issue 36: https://github.com/phax/ph-css/issues/36
 *
 * @author Philip Helger
 */
public final class Issue36Test
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (Issue36Test.class);

  @Test
  public void testIssue ()
  {
    final String css = "@media screen and (min-width: 768px) {.section {.\r\n" +
                       "    padding: 40px\r\n" +
                       "}\r\n" +
                       "\r\n" +
                       "}";
    final CSSReaderSettings aSettings = new CSSReaderSettings ().setCSSVersion (ECSSVersion.LATEST)
                                                                .setBrowserCompliantMode (true)
                                                                .setCustomErrorHandler (new DoNothingCSSParseErrorHandler ());
    final CascadingStyleSheet cascadingStyleSheet = CSSReader.readFromStringStream (css, aSettings);
    final CSSWriter writer = new CSSWriter (new CSSWriterSettings (ECSSVersion.LATEST, true));
    assertEquals ("@media screen and (min-width:768px){.section{}}", writer.getCSSAsString (cascadingStyleSheet));
  }
}
