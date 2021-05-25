/**
 * Copyright (C) 2014-2021 Philip Helger (www.helger.com)
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

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.writer.CSSWriter;

/**
 * Test for issue 66: https://github.com/phax/ph-css/issues/66
 *
 * @author Philip Helger
 */
public final class Issue66Test
{
  @Test
  public void testIssue ()
  {
    final String css = "body:not(foo, .bar) {}\n";

    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader (css,
                                                                     new CSSReaderSettings ().setCSSVersion (ECSSVersion.LATEST)
                                                                                             .setBrowserCompliantMode (true));
    assertNotNull (aCSS);
    assertEquals (css, new CSSWriter ().setWriteHeaderText (false).getCSSAsString (aCSS));
  }
}
