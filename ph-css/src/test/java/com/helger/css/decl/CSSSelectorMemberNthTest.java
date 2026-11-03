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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jspecify.annotations.NonNull;
import org.junit.Test;

import com.helger.css.reader.CSSReader;
import com.helger.css.writer.CSSWriterSettings;
import com.helger.unittest.support.TestHelper;

/**
 * Test class for {@link CSSSelectorMemberNth}.
 *
 * @author Philip Helger
 */
public final class CSSSelectorMemberNthTest
{
  @NonNull
  private static CSSSelectorMemberNth _parse (@NonNull final String sSelector)
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sSelector + " {}");
    assertNotNull (sSelector, aCSS);
    final CSSStyleRule aRule = aCSS.getAllStyleRules ().getFirstOrNull ();
    assertNotNull (aRule);
    final ICSSSelectorMember aMember = aRule.getSelectorAtIndex (0).getMemberAtIndex (0);
    if (aMember instanceof final CSSSelectorMemberNth aNth)
      return aNth;
    fail ("Unexpected member " + aMember);
    return null;
  }

  @Test
  public void testRead ()
  {
    CSSSelectorMemberNth aNth = _parse (":nth-child(odd)");
    assertSame (ECSSNthPseudoClass.NTH_CHILD, aNth.getPseudoClass ());
    assertEquals ("odd", aNth.getExpression ());
    assertEquals (2, aNth.getA ());
    assertEquals (1, aNth.getB ());
    assertFalse (aNth.hasOfSelectors ());
    assertEquals (":nth-child(odd)", aNth.getAsCSSString ());

    aNth = _parse (":NTH-LAST-CHILD(EVEN)");
    assertSame (ECSSNthPseudoClass.NTH_LAST_CHILD, aNth.getPseudoClass ());
    assertEquals (2, aNth.getA ());
    assertEquals (0, aNth.getB ());

    aNth = _parse (":nth-of-type( 2n + 7 )");
    assertSame (ECSSNthPseudoClass.NTH_OF_TYPE, aNth.getPseudoClass ());
    assertEquals ("2n+7", aNth.getExpression ());
    assertEquals (2, aNth.getA ());
    assertEquals (7, aNth.getB ());
    assertEquals (":nth-of-type(2n+7)", aNth.getAsCSSString ());

    aNth = _parse (":nth-last-of-type(-n+3)");
    assertSame (ECSSNthPseudoClass.NTH_LAST_OF_TYPE, aNth.getPseudoClass ());
    assertEquals (-1, aNth.getA ());
    assertEquals (3, aNth.getB ());

    aNth = _parse (":nth-child(2)");
    assertEquals (0, aNth.getA ());
    assertEquals (2, aNth.getB ());

    aNth = _parse (":nth-child(n)");
    assertEquals (1, aNth.getA ());
    assertEquals (0, aNth.getB ());

    aNth = _parse (":nth-child(-2n-1)");
    assertEquals (-2, aNth.getA ());
    assertEquals (-1, aNth.getB ());
  }

  @Test
  public void testReadOf ()
  {
    final CSSSelectorMemberNth aNth = _parse (":nth-child(2n+1 of .important, li > a)");
    assertEquals (2, aNth.getA ());
    assertEquals (1, aNth.getB ());
    assertTrue (aNth.hasOfSelectors ());
    assertEquals (2, aNth.getOfSelectorCount ());
    assertEquals (".important", aNth.getOfSelectorAtIndex (0).getAsCSSString ());
    assertEquals ("li>a", aNth.getOfSelectorAtIndex (1).getAsCSSString ());
    assertEquals (":nth-child(2n+1 of .important, li>a)", aNth.getAsCSSString ());
    assertEquals (":nth-child(2n+1 of .important,li>a)", aNth.getAsCSSString (new CSSWriterSettings (true), 0));

    final CSSSelectorMemberNth aCreated = new CSSSelectorMemberNth (ECSSNthPseudoClass.NTH_CHILD, 2, 1);
    aCreated.addOfSelector (new CSSSelector ().addMember (new CSSSelectorSimpleMember (".important")));
    aCreated.addOfSelector (new CSSSelector ().addMember (new CSSSelectorSimpleMember ("li"))
                                              .addMember (ECSSSelectorCombinator.GREATER)
                                              .addMember (new CSSSelectorSimpleMember ("a")));
    TestHelper.testDefaultImplementationWithEqualContentObject (aNth, aCreated);

    // The rest of the selector after the closing parenthesis is read normally
    final CascadingStyleSheet aCSS = CSSReader.readFromString ("li:nth-child(odd of .a) > span { color: red }");
    assertNotNull (aCSS);
    assertEquals ("li:nth-child(odd of .a)>span", aCSS.getStyleRuleAtIndex (0).getSelectorAtIndex (0).getAsCSSString ());
  }

  @Test
  public void testCreate ()
  {
    assertEquals ("2n+1", new CSSSelectorMemberNth (ECSSNthPseudoClass.NTH_CHILD, 2, 1).getExpression ());
    assertEquals ("-n+3", new CSSSelectorMemberNth (ECSSNthPseudoClass.NTH_CHILD, -1, 3).getExpression ());
    assertEquals ("n", new CSSSelectorMemberNth (ECSSNthPseudoClass.NTH_CHILD, 1, 0).getExpression ());
    assertEquals ("3n-2", new CSSSelectorMemberNth (ECSSNthPseudoClass.NTH_CHILD, 3, -2).getExpression ());
    assertEquals ("5", new CSSSelectorMemberNth (ECSSNthPseudoClass.NTH_CHILD, 0, 5).getExpression ());
    assertEquals ("-5", new CSSSelectorMemberNth (ECSSNthPseudoClass.NTH_CHILD, 0, -5).getExpression ());

    TestHelper.testDefaultImplementationWithEqualContentObject (new CSSSelectorMemberNth (ECSSNthPseudoClass.NTH_CHILD,
                                                                                          "2n+1"),
                                                                new CSSSelectorMemberNth (ECSSNthPseudoClass.NTH_CHILD,
                                                                                          2,
                                                                                          1));

    try
    {
      new CSSSelectorMemberNth (ECSSNthPseudoClass.NTH_CHILD, "foo");
      fail ();
    }
    catch (final IllegalArgumentException ex)
    {
      // expected
    }
  }
}
