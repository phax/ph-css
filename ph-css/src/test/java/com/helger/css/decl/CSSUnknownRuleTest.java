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
package com.helger.css.decl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.helger.css.ECSSVersion;
import com.helger.css.reader.CSSReader;

/**
 * Test class for {@link CSSUnknownRule}.
 *
 * @author Philip Helger
 */
public final class CSSUnknownRuleTest
{
  @Nonnull
  private static CSSUnknownRule _parse (@Nonnull final String sCSS)
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sCSS, ECSSVersion.CSS30);
    assertNotNull (sCSS, aCSS);
    assertTrue (aCSS.hasUnknownRules ());
    assertEquals (1, aCSS.getUnknownRuleCount ());
    final CSSUnknownRule ret = aCSS.getAllUnknownRules ().get (0);
    assertNotNull (ret);
    return ret;
  }

  @Test
  public void testRead ()
  {
    CSSUnknownRule aUR;
    aUR = _parse ("@-moz-document {}");
    assertEquals ("@-moz-document", aUR.getDeclaration ());
    assertEquals ("", aUR.getParameterList ());
    assertEquals ("", aUR.getBody ());

    aUR = _parse ("@-moz-document    anything else or whatever 4711    {   }");
    assertEquals ("@-moz-document", aUR.getDeclaration ());
    assertEquals ("anything else or whatever 4711", aUR.getParameterList ());
    assertEquals ("", aUR.getBody ());

    aUR = _parse ("@-moz-document { color: red; }");
    assertEquals ("@-moz-document", aUR.getDeclaration ());
    assertEquals ("", aUR.getParameterList ());
    assertEquals ("color: red;", aUR.getBody ());

    aUR = _parse ("@three-dee {\n" +
                  "  @background-lighting {\n" +
                  "    azimuth: 30deg;\n" +
                  "    elevation: 190deg;\n" +
                  "  }\n" +
                  "  h1 { color: red }\n" +
                  "}");
    assertEquals ("@three-dee", aUR.getDeclaration ());
    assertEquals ("", aUR.getParameterList ());
    assertEquals ("@background-lighting {\n" +
                  "    azimuth: 30deg;\n" +
                  "    elevation: 190deg;\n" +
                  "  }\n" +
                  "  h1 { color: red }",
                  aUR.getBody ());

  }
}
