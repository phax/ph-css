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
package com.helger.css.supplementary.issues;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.reader.errorhandler.DoNothingCSSInterpretErrorHandler;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Test for issue 33: https://github.com/phax/ph-css/issues/33
 *
 * @author Philip Helger
 */
public final class Issue33Test
{
  @Test
  public void testIssue ()
  {
    // No log message may be issued in this test!

    final String css = "@media \\0screen\\,screen\\9 {.test {margin-left: 0px}}";
    final CSSReaderSettings aSettings = new CSSReaderSettings ().setCSSVersion (ECSSVersion.LATEST)
                                                                .setBrowserCompliantMode (true)
                                                                .setInterpretErrorHandler (new DoNothingCSSInterpretErrorHandler ());
    final CascadingStyleSheet cascadingStyleSheet = CSSReader.readFromStringStream (css, aSettings);
    final CSSWriter writer = new CSSWriter (new CSSWriterSettings (ECSSVersion.LATEST, true));
    assertEquals ("@media \\0screen\\,screen\\9 {.test{margin-left:0}}", writer.getCSSAsString (cascadingStyleSheet));
  }
}
