/**
 * Copyright (C) 2014-2019 Philip Helger (www.helger.com)
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
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.helger.commons.system.ENewLineMode;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.handler.LoggingCSSParseExceptionCallback;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.reader.errorhandler.LoggingCSSInterpretErrorHandler;
import com.helger.css.reader.errorhandler.LoggingCSSParseErrorHandler;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Test for issue 41: https://github.com/phax/ph-css/issues/41
 *
 * @author Philip Helger
 */
public final class Issue41Test
{
  @Test
  public void testIssue ()
  {
    final String css = "@media (min--moz-device-pixel-ratio: 1.3),(-o-min-device-pixel-ratio: 2.6/2),(-webkit-min-device-pixel-ratio: 1.3),(min-device-pixel-ratio: 1.3),(min-resolution: 1.3dppx) {\r\n" +
                       " .social .facebook a, .social .twitter a {\r\n" +
                       "  background-size: -webkit-background-size: 652px 295px;\r\n" +
                       "  background-size: 652px 295px;\r\n" +
                       " }\r\n" +
                       "}";
    final CSSReaderSettings aSettings = new CSSReaderSettings ().setCSSVersion (ECSSVersion.LATEST)
                                                                .setBrowserCompliantMode (true)
                                                                .setCustomExceptionHandler (new LoggingCSSParseExceptionCallback ())
                                                                .setCustomErrorHandler (new LoggingCSSParseErrorHandler ())
                                                                .setInterpretErrorHandler (new LoggingCSSInterpretErrorHandler ());
    final CascadingStyleSheet aCSS = CSSReader.readFromStringStream (css, aSettings);
    assertNotNull (aCSS);

    final CSSWriterSettings aCWS = new CSSWriterSettings (ECSSVersion.LATEST).setNewLineMode (ENewLineMode.WINDOWS)
                                                                             .setIndent (" ");
    assertEquals ("@media (min--moz-device-pixel-ratio:1.3), (-o-min-device-pixel-ratio:2.6/2), (-webkit-min-device-pixel-ratio:1.3), (min-device-pixel-ratio:1.3), (min-resolution:1.3dppx) {\r\n" +
                  " .social .facebook a,\r\n" +
                  " .social .twitter a { background-size:-webkit-background-size; }\r\n" +
                  "}\r\n",
                  new CSSWriter (aCWS).setWriteHeaderText (false).setWriteFooterText (false).getCSSAsString (aCSS));
  }
}
