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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.reader.errorhandler.LoggingCSSParseErrorHandler;
import com.helger.css.writer.CSSWriter;

/**
 * Test for issue 19: https://github.com/phax/ph-css/issues/19
 *
 * @author Philip Helger
 */
public final class Issue19Test
{
  @Test
  public void testIssue ()
  {
    // Multiple errors contained
    final IReadableResource aRes = new ClassPathResource ("testfiles/css30/bad_but_browsercompliant/issue19.css");
    assertTrue (aRes.exists ());
    final CascadingStyleSheet aCSS = CSSReader.readFromStream (aRes,
                                                               new CSSReaderSettings ().setFallbackCharset (StandardCharsets.UTF_8)
                                                                                       .setCSSVersion (ECSSVersion.CSS30)
                                                                                       .setCustomErrorHandler (new LoggingCSSParseErrorHandler ())
                                                                                       .setBrowserCompliantMode (true));
    assertNotNull (aCSS);
    if (false)
      System.out.println (new CSSWriter (ECSSVersion.CSS30).getCSSAsString (aCSS));
    assertEquals (1, aCSS.getRuleCount ());
    assertEquals (1, aCSS.getStyleRuleCount ());
  }
}
