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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.commons.mock.CommonsTestHelper;
import com.helger.css.ECSSVersion;
import com.helger.css.utils.CSSDataURL;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Test class for class {@link CSSURI}.
 *
 * @author Philip Helger
 */
public final class CSSURITest
{
  @Test
  public void testBasic ()
  {
    final CSSURI aURI = new CSSURI ("a.gif");
    assertEquals ("a.gif", aURI.getURI ());
    final CSSWriterSettings aSettings = new CSSWriterSettings (ECSSVersion.CSS30, false);
    assertEquals ("url(a.gif)", aURI.getAsCSSString (aSettings, 0));
    aSettings.setQuoteURLs (true);
    assertEquals ("url('a.gif')", aURI.getAsCSSString (aSettings, 0));
    assertFalse (aURI.isDataURL ());
    assertNull (aURI.getAsDataURL ());

    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aURI, new CSSURI ("a.gif"));
    CommonsTestHelper.testDefaultImplementationWithDifferentContentObject (aURI, new CSSURI ("b.gif"));
  }

  @Test
  public void testDataURL ()
  {
    final CSSURI aURI = new CSSURI ("data:text/plain,foobar");
    assertEquals ("data:text/plain,foobar", aURI.getURI ());
    assertTrue (aURI.isDataURL ());
    final CSSDataURL aDataURL = aURI.getAsDataURL ();
    assertNotNull (aDataURL);
    assertEquals ("foobar", aDataURL.getContentAsString ());
  }
}
