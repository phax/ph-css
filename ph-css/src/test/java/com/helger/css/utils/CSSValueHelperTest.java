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
package com.helger.css.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link CSSValueHelper}.
 *
 * @author Philip Helger
 */
public final class CSSValueHelperTest
{
  @Test
  public void testIsFunctionCall ()
  {
    assertTrue (CSSValueHelper.isFunctionCall ("calc(1px)", "calc"));
    assertTrue (CSSValueHelper.isFunctionCall ("  CALC(1px) ", "min", "calc"));
    assertFalse (CSSValueHelper.isFunctionCall ("calc(1px", "calc"));
    assertFalse (CSSValueHelper.isFunctionCall ("calcx(1px)", "calc"));
    assertFalse (CSSValueHelper.isFunctionCall ("", "calc"));
    assertFalse (CSSValueHelper.isFunctionCall (null, "calc"));
  }

  @Test
  public void testContainsSubstitutionFunction ()
  {
    assertTrue (CSSValueHelper.containsSubstitutionFunction ("var(--x)"));
    assertTrue (CSSValueHelper.containsSubstitutionFunction ("1px VAR(--x)"));
    assertTrue (CSSValueHelper.containsSubstitutionFunction ("env(safe-area-inset-top)"));
    assertTrue (CSSValueHelper.containsSubstitutionFunction ("attr(data-x)"));
    assertFalse (CSSValueHelper.containsSubstitutionFunction ("calc(1px)"));
    assertFalse (CSSValueHelper.containsSubstitutionFunction (""));
    assertFalse (CSSValueHelper.containsSubstitutionFunction (null));
  }

  @Test
  public void testGetSplitByWhitespace ()
  {
    assertEquals ("[a, b]", CSSValueHelper.getSplitByWhitespace ("  a \t b  ").toString ());
    assertEquals ("[calc(1px + 2px), 3px]", CSSValueHelper.getSplitByWhitespace ("calc(1px + 2px) 3px").toString ());
    assertEquals ("[calc((1px + 2px) * 2), a]",
                  CSSValueHelper.getSplitByWhitespace ("calc((1px + 2px) * 2) a").toString ());
    assertEquals ("[\"a b\", 'c d']", CSSValueHelper.getSplitByWhitespace ("\"a b\" 'c d'").toString ());
    assertTrue (CSSValueHelper.getSplitByWhitespace ("").isEmpty ());
    assertTrue (CSSValueHelper.getSplitByWhitespace ("   ").isEmpty ());
  }

  @Test
  public void testGetSplitByComma ()
  {
    assertEquals ("[a, b]", CSSValueHelper.getSplitByComma ("a , b").toString ());
    assertEquals ("[rgb(0,0,0), red]", CSSValueHelper.getSplitByComma ("rgb(0,0,0), red").toString ());
    assertEquals ("[\"a,b\", c]", CSSValueHelper.getSplitByComma ("\"a,b\", c").toString ());
    assertEquals ("[a, ]", CSSValueHelper.getSplitByComma ("a,").toString ());
    assertEquals (1, CSSValueHelper.getSplitByComma ("").size ());
  }
}
