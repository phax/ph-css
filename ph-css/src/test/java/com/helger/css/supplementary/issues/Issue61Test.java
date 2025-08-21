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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.collection.commons.ICommonsList;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.ICSSTopLevelRule;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;

/**
 * Test for issue 61: https://github.com/phax/ph-css/issues/61
 *
 * @author Philip Helger
 */
public final class Issue61Test
{
  @Test
  public void testIssue ()
  {
    final String css = ".class10 { bla:foo } .20 { bla2:foo2} .class30 { bla3:foo3 }";

    final ICommonsList <ICSSTopLevelRule> aTLR = CSSReader.readFromStringReader (css,
                                                                                 new CSSReaderSettings ().setCSSVersion (ECSSVersion.LATEST)
                                                                                                         .setBrowserCompliantMode (true))
                                                          .getAllRules ();
    assertEquals (2, aTLR.size ());
    assertTrue (aTLR.get (0) instanceof CSSStyleRule);
    assertEquals (".class10", ((CSSStyleRule) aTLR.get (0)).getAllSelectors ().get (0).getAsCSSString ());
    assertTrue (aTLR.get (1) instanceof CSSStyleRule);
    assertEquals (".class30", ((CSSStyleRule) aTLR.get (1)).getAllSelectors ().get (0).getAsCSSString ());
  }
}
