/*
 * Copyright (C) 2014-2022 Philip Helger (www.helger.com)
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
 * Test class for class {@link CSSSelector}.
 *
 * @author Philip Helger
 */
public final class CSSSelectorTest
{
  @Nonnull
  private static CSSSelector _parse (@Nonnull final String sCSS)
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sCSS, ECSSVersion.CSS30);
    assertNotNull (sCSS, aCSS);
    assertTrue (aCSS.hasStyleRules ());
    assertEquals (1, aCSS.getStyleRuleCount ());
    final CSSStyleRule aStyle = aCSS.getAllStyleRules ().get (0);
    assertNotNull (sCSS, aStyle);
    assertTrue (aStyle.hasSelectors ());
    assertEquals (1, aStyle.getSelectorCount ());
    final CSSSelector aSel = aStyle.getSelectorAtIndex (0);
    assertNotNull (sCSS, aSel);
    return aSel;
  }

  @Test
  public void testRead ()
  {
    CSSSelector aSel;
    aSel = _parse ("div { color:red }");
    assertEquals (1, aSel.getMemberCount ());
    assertTrue (aSel.getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);
    assertEquals ("div", aSel.getMemberAtIndex (0).getAsCSSString ());

    aSel = _parse ("#id { color:red }");
    assertEquals (1, aSel.getMemberCount ());
    assertTrue (aSel.getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);
    assertEquals ("#id", aSel.getMemberAtIndex (0).getAsCSSString ());
    assertEquals ("#id", aSel.getAsCSSString ());

    aSel = _parse ("#id div { color:red }");
    assertEquals (3, aSel.getMemberCount ());
    assertTrue (aSel.getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);
    assertEquals ("#id", aSel.getMemberAtIndex (0).getAsCSSString ());
    assertTrue (aSel.getMemberAtIndex (1) instanceof ECSSSelectorCombinator);
    assertEquals (" ", aSel.getMemberAtIndex (1).getAsCSSString ());
    assertTrue (aSel.getMemberAtIndex (2) instanceof CSSSelectorSimpleMember);
    assertEquals ("div", aSel.getMemberAtIndex (2).getAsCSSString ());
    assertEquals ("#id div", aSel.getAsCSSString ());

    aSel = _parse ("#id ~ div { color:red }");
    assertEquals (3, aSel.getMemberCount ());
    assertTrue (aSel.getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);
    assertEquals ("#id", aSel.getMemberAtIndex (0).getAsCSSString ());
    assertTrue (aSel.getMemberAtIndex (1) instanceof ECSSSelectorCombinator);
    assertEquals ("~", aSel.getMemberAtIndex (1).getAsCSSString ());
    assertTrue (aSel.getMemberAtIndex (2) instanceof CSSSelectorSimpleMember);
    assertEquals ("div", aSel.getMemberAtIndex (2).getAsCSSString ());
    assertEquals ("#id~div", aSel.getAsCSSString ());
  }
}
