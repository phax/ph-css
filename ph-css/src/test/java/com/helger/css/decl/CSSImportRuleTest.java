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

import com.helger.commons.mock.CommonsTestHelper;
import com.helger.css.ECSSVersion;
import com.helger.css.reader.CSSReader;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Test class for class {@link CSSImportRule}.
 *
 * @author Philip Helger
 */
public final class CSSImportRuleTest
{
  @Nonnull
  private static CSSImportRule _parse (@Nonnull final String sCSS)
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sCSS, ECSSVersion.CSS30);
    assertNotNull (sCSS, aCSS);
    assertTrue (aCSS.hasImportRules ());
    assertEquals (1, aCSS.getImportRuleCount ());
    final CSSImportRule ret = aCSS.getAllImportRules ().get (0);
    assertNotNull (sCSS, ret);
    return ret;
  }

  @Test
  public void testRead ()
  {
    CSSImportRule aIR;
    aIR = _parse ("@import url(a.gif);\n");
    assertEquals (0, aIR.getMediaQueryCount ());
    assertTrue (aIR.getAllMediaQueries ().isEmpty ());
    assertEquals ("a.gif", aIR.getLocationString ());

    aIR = _parse ("@import url(\"a.gif\") print;\n");
    assertEquals (1, aIR.getMediaQueryCount ());
    assertEquals (new CSSMediaQuery ("print"), aIR.getAllMediaQueries ().get (0));
    assertEquals ("a.gif", aIR.getLocationString ());

    aIR = _parse ("@import url('a.gif') print, screen;\n");
    assertEquals (2, aIR.getMediaQueryCount ());
    assertEquals (new CSSMediaQuery ("print"), aIR.getAllMediaQueries ().get (0));
    assertEquals (new CSSMediaQuery ("screen"), aIR.getAllMediaQueries ().get (1));
    assertEquals ("a.gif", aIR.getLocationString ());

    // Create the same rule by application
    final CSSImportRule aCreated = new CSSImportRule ("a.gif");
    aCreated.addMediaQuery (new CSSMediaQuery ("print")).addMediaQuery (new CSSMediaQuery ("screen"));
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aIR, aCreated);
  }

  @Test
  public void testCreate ()
  {
    final CSSImportRule aImportRule = new CSSImportRule ("a.gif");
    final CSSWriterSettings aSettings = new CSSWriterSettings (ECSSVersion.CSS30, false);
    assertEquals ("@import url(a.gif);\n", aImportRule.getAsCSSString (aSettings, 0));
    aSettings.setQuoteURLs (true);
    assertEquals ("@import url('a.gif');\n", aImportRule.getAsCSSString (aSettings, 0));
  }
}
