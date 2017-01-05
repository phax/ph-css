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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.helger.css.ECSSVersion;
import com.helger.css.reader.CSSReader;

/**
 * Test class for {@link CSSNamespaceRule}.
 *
 * @author Philip Helger
 */
public final class CSSNamespaceRuleTest
{
  @Nonnull
  private static CSSNamespaceRule _parse (@Nonnull final String sCSS)
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sCSS, ECSSVersion.CSS30);
    assertNotNull (sCSS, aCSS);
    assertTrue (aCSS.hasNamespaceRules ());
    assertEquals (1, aCSS.getNamespaceRuleCount ());
    final CSSNamespaceRule ret = aCSS.getAllNamespaceRules ().get (0);
    assertNotNull (ret);
    return ret;
  }

  @Test
  public void testRead ()
  {
    CSSNamespaceRule aNSR;
    aNSR = _parse ("@namespace empty \"\";");
    assertEquals ("empty", aNSR.getNamespacePrefix ());
    assertEquals ("", aNSR.getNamespaceURL ());

    aNSR = _parse ("@namespace \"\";");
    assertNull (aNSR.getNamespacePrefix ());
    assertEquals ("", aNSR.getNamespaceURL ());

    aNSR = _parse (" @namespace  \"http://www.w3.org/1999/xhtml\";");
    assertNull (aNSR.getNamespacePrefix ());
    assertEquals ("http://www.w3.org/1999/xhtml", aNSR.getNamespaceURL ());

    aNSR = _parse ("@namespace     svg    \"http://www.w3.org/2000/svg\"    ;");
    assertEquals ("svg", aNSR.getNamespacePrefix ());
    assertEquals ("http://www.w3.org/2000/svg", aNSR.getNamespaceURL ());

    aNSR = _parse ("@namespace Q \"http://example.com/q-markup\";");
    assertEquals ("Q", aNSR.getNamespacePrefix ());
    assertEquals ("http://example.com/q-markup", aNSR.getNamespaceURL ());

    aNSR = _parse ("@namespace lq \"http://example.com/q-markup\";");
    assertEquals ("lq", aNSR.getNamespacePrefix ());
    assertEquals ("http://example.com/q-markup", aNSR.getNamespaceURL ());

    aNSR = _parse (" @namespace toto url(\"http://toto.example.org\");");
    assertEquals ("toto", aNSR.getNamespacePrefix ());
    assertEquals ("http://toto.example.org", aNSR.getNamespaceURL ());

    aNSR = _parse ("@namespace url(\"http://example.com/foo\");");
    assertNull (aNSR.getNamespacePrefix ());
    assertEquals ("http://example.com/foo", aNSR.getNamespaceURL ());

    aNSR = _parse ("@namespace toto2 url(http://toto.example.org);");
    assertEquals ("toto2", aNSR.getNamespacePrefix ());
    assertEquals ("http://toto.example.org", aNSR.getNamespaceURL ());

    aNSR = _parse ("@namespace url(http://example.com/foo);");
    assertNull (aNSR.getNamespacePrefix ());
    assertEquals ("http://example.com/foo", aNSR.getNamespaceURL ());
  }
}
