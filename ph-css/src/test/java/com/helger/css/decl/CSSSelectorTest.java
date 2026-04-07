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
package com.helger.css.decl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jspecify.annotations.NonNull;
import org.junit.Test;

import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.utils.CollectingCSSInterpretErrorHandler;

/**
 * Test class for class {@link CSSSelector}.
 *
 * @author Philip Helger
 */
public final class CSSSelectorTest
{
  private final CollectingCSSInterpretErrorHandler m_aIEH = new CollectingCSSInterpretErrorHandler ();

  @NonNull
  private CSSSelector _parse (@NonNull final String sCSS)
  {
    final CSSReaderSettings aSettings = new CSSReaderSettings ().setInterpretErrorHandler (m_aIEH);
    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader (sCSS, aSettings);
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

  @NonNull
  private static CSSStyleRule _parseRule (@NonNull final String sCSS)
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sCSS);
    assertNotNull (sCSS, aCSS);
    assertTrue (aCSS.hasStyleRules ());
    assertEquals (1, aCSS.getStyleRuleCount ());
    final CSSStyleRule aStyle = aCSS.getAllStyleRules ().get (0);
    assertNotNull (sCSS, aStyle);
    return aStyle;
  }

  @Test
  public void testReadElementSelector ()
  {
    final CSSSelector aSel = _parse ("div { color:red }");
    assertTrue (m_aIEH.getErrors ().isEmpty ());
    assertEquals (1, aSel.getMemberCount ());
    assertTrue (aSel.getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);
    assertEquals ("div", aSel.getMemberAtIndex (0).getAsCSSString ());
  }

  @Test
  public void testReadIdSelector ()
  {
    final CSSSelector aSel = _parse ("#id { color:red }");
    assertTrue (m_aIEH.getErrors ().isEmpty ());
    assertEquals (1, aSel.getMemberCount ());
    assertTrue (aSel.getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);
    assertEquals ("#id", aSel.getMemberAtIndex (0).getAsCSSString ());
    assertEquals ("#id", aSel.getAsCSSString ());
  }

  @Test
  public void testReadIdSpaceCombinator ()
  {
    final CSSSelector aSel = _parse ("#id div { color:red }");
    assertTrue (m_aIEH.getErrors ().isEmpty ());
    assertEquals (3, aSel.getMemberCount ());
    assertTrue (aSel.getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);
    assertEquals ("#id", aSel.getMemberAtIndex (0).getAsCSSString ());
    assertTrue (aSel.getMemberAtIndex (1) instanceof ECSSSelectorCombinator);
    assertEquals (" ", aSel.getMemberAtIndex (1).getAsCSSString ());
    assertTrue (aSel.getMemberAtIndex (2) instanceof CSSSelectorSimpleMember);
    assertEquals ("div", aSel.getMemberAtIndex (2).getAsCSSString ());
    assertEquals ("#id div", aSel.getAsCSSString ());
  }

  @Test
  public void testReadWaveDashCombinator ()
  {
    final CSSSelector aSel = _parse ("#id ~ div { color:red }");
    assertTrue (m_aIEH.getErrors ().isEmpty ());
    assertEquals (3, aSel.getMemberCount ());
    assertTrue (aSel.getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);
    assertEquals ("#id", aSel.getMemberAtIndex (0).getAsCSSString ());
    assertTrue (aSel.getMemberAtIndex (1) instanceof ECSSSelectorCombinator);
    assertEquals ("~", aSel.getMemberAtIndex (1).getAsCSSString ());
    assertTrue (aSel.getMemberAtIndex (2) instanceof CSSSelectorSimpleMember);
    assertEquals ("div", aSel.getMemberAtIndex (2).getAsCSSString ());
    assertEquals ("#id~div", aSel.getAsCSSString ());
  }

  @Test
  public void testReadNestingSelectorAtStartWithoutSpace ()
  {
    final CSSSelector aSel = _parse ("&.foo { color:red }");
    assertTrue (m_aIEH.getErrors ().isEmpty ());
    assertEquals (2, aSel.getMemberCount ());
    assertTrue (aSel.getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);
    assertEquals ("&", aSel.getMemberAtIndex (0).getAsCSSString ());
    assertTrue (aSel.getMemberAtIndex (1) instanceof CSSSelectorSimpleMember);
    assertEquals (".foo", aSel.getMemberAtIndex (1).getAsCSSString ());
  }

  @Test
  public void testReadNestingSelectorAtStartWithSpace ()
  {
    final CSSSelector aSel = _parse ("& .foo { color:red }");
    assertTrue (m_aIEH.getErrors ().isEmpty ());
    assertEquals (3, aSel.getMemberCount ());
    assertTrue (aSel.getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);
    assertEquals ("&", aSel.getMemberAtIndex (0).getAsCSSString ());
    assertTrue (aSel.getMemberAtIndex (1) instanceof ECSSSelectorCombinator);
    assertEquals (" ", aSel.getMemberAtIndex (1).getAsCSSString ());
    assertTrue (aSel.getMemberAtIndex (2) instanceof CSSSelectorSimpleMember);
    assertEquals (".foo", aSel.getMemberAtIndex (2).getAsCSSString ());
  }

  @Test
  public void testReadNestingSelectorAtEnd ()
  {
    final CSSSelector aSel = _parse (".foo & { color:red }");
    assertTrue (m_aIEH.getErrors ().isEmpty ());
    assertEquals (3, aSel.getMemberCount ());
    assertTrue (aSel.getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);
    assertEquals (".foo", aSel.getMemberAtIndex (0).getAsCSSString ());
    assertTrue (aSel.getMemberAtIndex (1) instanceof ECSSSelectorCombinator);
    assertEquals (" ", aSel.getMemberAtIndex (1).getAsCSSString ());
    assertTrue (aSel.getMemberAtIndex (2) instanceof CSSSelectorSimpleMember);
    assertEquals ("&", aSel.getMemberAtIndex (2).getAsCSSString ());
  }

  @Test
  public void testReadRelativeSelectorWithinStyleRuleWithWaveDash ()
  {
    final CSSStyleRule aRule = _parseRule (".foo { ~ .bar { color:red } }");

    assertTrue (m_aIEH.getErrors ().isEmpty ());

    assertEquals (".foo", aRule.getSelectorAtIndex (0).getAsCSSString ());
    assertTrue (aRule.getRuleAtIndex (0) instanceof CSSStyleRule);
    assertEquals (1, ((CSSStyleRule) aRule.getRuleAtIndex (0)).getSelectorCount ());
    final CSSSelector aSel = ((CSSStyleRule) aRule.getRuleAtIndex (0)).getSelectorAtIndex (0);
    assertEquals (2, aSel.getMemberCount ());
    assertTrue (aSel.getMemberAtIndex (0) instanceof ECSSSelectorCombinator);
    assertEquals ("~", ((ECSSSelectorCombinator) aSel.getMemberAtIndex (0)).getName ());

    assertTrue (aSel.getMemberAtIndex (1) instanceof CSSSelector);
    assertTrue (((CSSSelector) aSel.getMemberAtIndex (1)).getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);
    assertEquals (".bar",
                  ((CSSSelectorSimpleMember) ((CSSSelector) aSel.getMemberAtIndex (1)).getMemberAtIndex (0)).getValue ());
  }

  @Test
  public void testReadRelativeSelectorWithinStyleRuleWithPlus ()
  {
    final CSSStyleRule aRule = _parseRule (".foo { + .bar { color:red } }");

    assertTrue (m_aIEH.getErrors ().isEmpty ());

    assertEquals (".foo", aRule.getSelectorAtIndex (0).getAsCSSString ());
    assertTrue (aRule.getRuleAtIndex (0) instanceof CSSStyleRule);
    assertEquals (1, ((CSSStyleRule) aRule.getRuleAtIndex (0)).getSelectorCount ());
    final CSSSelector aSel = ((CSSStyleRule) aRule.getRuleAtIndex (0)).getSelectorAtIndex (0);
    assertEquals (2, aSel.getMemberCount ());
    assertTrue (aSel.getMemberAtIndex (0) instanceof ECSSSelectorCombinator);
    assertEquals ("+", ((ECSSSelectorCombinator) aSel.getMemberAtIndex (0)).getName ());

    assertTrue (aSel.getMemberAtIndex (1) instanceof CSSSelector);
    assertTrue (((CSSSelector) aSel.getMemberAtIndex (1)).getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);
    assertEquals (".bar",
                  ((CSSSelectorSimpleMember) ((CSSSelector) aSel.getMemberAtIndex (1)).getMemberAtIndex (0)).getValue ());
  }

  @Test
  public void testReadRelativeSelectorWithinStyleRuleWithGreater ()
  {
    final CSSStyleRule aRule = _parseRule (".foo { > .bar { color:red } }");

    assertTrue (m_aIEH.getErrors ().isEmpty ());

    assertEquals (".foo", aRule.getSelectorAtIndex (0).getAsCSSString ());
    assertTrue (aRule.getRuleAtIndex (0) instanceof CSSStyleRule);
    assertEquals (1, ((CSSStyleRule) aRule.getRuleAtIndex (0)).getSelectorCount ());
    final CSSSelector aSel = ((CSSStyleRule) aRule.getRuleAtIndex (0)).getSelectorAtIndex (0);
    assertEquals (2, aSel.getMemberCount ());
    assertTrue (aSel.getMemberAtIndex (0) instanceof ECSSSelectorCombinator);
    assertEquals (">", ((ECSSSelectorCombinator) aSel.getMemberAtIndex (0)).getName ());

    assertTrue (aSel.getMemberAtIndex (1) instanceof CSSSelector);
    assertTrue (((CSSSelector) aSel.getMemberAtIndex (1)).getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);
    assertEquals (".bar",
                  ((CSSSelectorSimpleMember) ((CSSSelector) aSel.getMemberAtIndex (1)).getMemberAtIndex (0)).getValue ());
  }

  @Test
  public void testReadRelativeSelectorAtTopLevel ()
  {
    final CSSSelector aSel = _parse ("> .bar { color:red }");

    assertEquals (List.of ("Relative selectors are not allowed at the top level!"), m_aIEH.getErrors ());

    assertEquals (2, aSel.getMemberCount ());
    assertTrue (aSel.getMemberAtIndex (0) instanceof ECSSSelectorCombinator);
    assertEquals (">", ((ECSSSelectorCombinator) aSel.getMemberAtIndex (0)).getName ());

    assertTrue (aSel.getMemberAtIndex (1) instanceof CSSSelector);
    assertTrue (((CSSSelector) aSel.getMemberAtIndex (1)).getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);
    assertEquals (".bar",
                  ((CSSSelectorSimpleMember) ((CSSSelector) aSel.getMemberAtIndex (1)).getMemberAtIndex (0)).getValue ());
  }
}
