/*
 * Copyright (C) 2014-2026 Philip Helger (www.helger.com)
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
import static org.junit.Assert.assertNull;

import org.jspecify.annotations.NonNull;
import org.junit.Test;

import com.helger.css.decl.CSSPageRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Test for issue 65: https://github.com/phax/ph-css/issues/65<br>
 * <code>@page :nth()</code> from CSS GCPM 3
 *
 * @author Philip Helger
 */
public final class Issue65Test
{
  @NonNull
  private static CSSPageRule _parse (@NonNull final String sCSS)
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader (sCSS,
                                                                     new CSSReaderSettings ().setBrowserCompliantMode (false));
    assertNotNull (sCSS, aCSS);
    assertEquals (1, aCSS.getRuleCount ());
    return (CSSPageRule) aCSS.getRuleAtIndex (0);
  }

  @Test
  public void testPageNth ()
  {
    assertEquals ("[:nth(1)]", _parse ("@page :nth(1) {}").getAllSelectors ().toString ());
    assertEquals ("[:nth(2n)]", _parse ("@page :nth(2n) {}").getAllSelectors ().toString ());
    assertEquals ("[:nth(-n+3)]", _parse ("@page :nth( -n + 3 ) {}").getAllSelectors ().toString ());
    assertEquals ("[:nth(odd)]", _parse ("@page :NTH(odd) {}").getAllSelectors ().toString ());
    assertEquals ("[:nth(5 of A)]", _parse ("@page :nth(5 of A) {}").getAllSelectors ().toString ());
    assertEquals ("[chapter:nth(2n+1 of chapter):left]",
                  _parse ("@page chapter:nth( 2n+1  of  chapter ):left {}").getAllSelectors ().toString ());
    assertEquals ("[:first, :nth(3)]", _parse ("@page :first, :nth(3) {}").getAllSelectors ().toString ());

    // Rest of the rule is read normally
    final CascadingStyleSheet aCSS = CSSReader.readFromString ("@page :nth(2n of A) { margin: 1in } div { color: red }");
    assertNotNull (aCSS);
    assertEquals ("@page :nth(2n of A){margin:1in}div{color:red}",
                  new CSSWriter (new CSSWriterSettings ().setOptimizedOutput (true)).setWriteHeaderText (false)
                                                                                    .getCSSAsString (aCSS));
  }

  @Test
  public void testInvalid ()
  {
    final CSSReaderSettings aSettings = new CSSReaderSettings ().setBrowserCompliantMode (false);
    assertNull (CSSReader.readFromStringReader ("@page :nth() {}", aSettings));
    assertNull (CSSReader.readFromStringReader ("@page :nth(foo) {}", aSettings));
    assertNull (CSSReader.readFromStringReader ("@page :nth(1 of) {}", aSettings));
  }
}
