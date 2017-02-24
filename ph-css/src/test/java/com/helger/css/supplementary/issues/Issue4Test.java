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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.errorhandler.LoggingCSSParseErrorHandler;
import com.helger.css.writer.CSSWriter;

/**
 * Test for issue 4: https://github.com/phax/ph-css/issues/4
 *
 * @author Philip Helger
 */
public final class Issue4Test
{
  @Test
  public void testIssue4 ()
  {
    final IReadableResource aRes = new ClassPathResource ("testfiles/css30/good/issue4.css");
    assertTrue (aRes.exists ());
    final CascadingStyleSheet aCSS = CSSReader.readFromStream (aRes,
                                                               StandardCharsets.UTF_8,
                                                               ECSSVersion.CSS30,
                                                               new LoggingCSSParseErrorHandler ());
    assertNotNull (aCSS);
    if (false)
      System.out.println (new CSSWriter (ECSSVersion.CSS30).getCSSAsString (aCSS));
  }
}
