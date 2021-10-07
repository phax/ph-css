/*
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
import com.helger.css.decl.CSSMediaRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;

/**
 * Test for issue 67: https://github.com/phax/ph-css/issues/67
 *
 * @author Philip Helger
 */
public final class Issue67Test
{
  @Test
  public void testIssue67 ()
  {
    final String sCSS = "@media (max-width: 959px) {\r\n" +
                        "  @-moz-document url-prefix() {\r\n" +
                        "    .test-class {\r\n" +
                        "      height: 1vh;\r\n" +
                        "      visibility: collapse;\r\n" +
                        "    }\r\n" +
                        "  }\r\n" +
                        "}\r\n" +
                        "\r\n" +
                        "@-moz-anything {}";
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sCSS, ECSSVersion.LATEST);
    assertNotNull (aCSS);
    assertEquals (2, aCSS.getRuleCount ());
    assertEquals (1, aCSS.getMediaRuleCount ());
    assertEquals (1, aCSS.getUnknownRuleCount ());

    final CSSMediaRule aMR = aCSS.getMediaRuleAtIndex (0);
    assertNotNull (aMR);

    assertEquals (1, aMR.getRuleCount ());
    assertEquals (1, aMR.getUnknownRuleCount ());
  }
}
