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

/**
 * Test class for {@link CSSStyleRule}.
 *
 * @author Philip Helger
 */
public final class CSSStyleRuleTest
{
  @Nonnull
  private static CSSStyleRule _parse (@Nonnull final String sCSS)
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sCSS, ECSSVersion.CSS30);
    assertNotNull (sCSS, aCSS);
    assertTrue (aCSS.hasStyleRules ());
    assertEquals (1, aCSS.getStyleRuleCount ());
    final CSSStyleRule ret = aCSS.getAllStyleRules ().get (0);
    assertNotNull (ret);
    return ret;
  }

  @Test
  public void testRead1 ()
  {
    CSSStyleRule aSR;
    aSR = _parse ("div { color: red; }");
    assertEquals (1, aSR.getSelectorCount ());
    assertEquals (1, aSR.getSelectorAtIndex (0).getMemberCount ());
    assertTrue (aSR.getSelectorAtIndex (0).getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);
    assertEquals ("div", ((CSSSelectorSimpleMember) aSR.getSelectorAtIndex (0).getMemberAtIndex (0)).getValue ());
    assertEquals (1, aSR.getDeclarationCount ());
    assertEquals ("color", aSR.getDeclarationAtIndex (0).getProperty ());
    assertEquals (1, aSR.getDeclarationAtIndex (0).getExpression ().getMemberCount ());
    assertTrue (aSR.getDeclarationAtIndex (0)
                   .getExpression ()
                   .getMemberAtIndex (0) instanceof CSSExpressionMemberTermSimple);
    assertEquals ("red",
                  ((CSSExpressionMemberTermSimple) aSR.getDeclarationAtIndex (0)
                                                      .getExpression ()
                                                      .getMemberAtIndex (0)).getValue ());

    // Create the same rule by application
    final CSSStyleRule aCreated = new CSSStyleRule ();
    aCreated.addSelector (new CSSSelectorSimpleMember ("div"));
    aCreated.addDeclaration ("color", CSSExpression.createSimple ("red"), false);
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aSR, aCreated);
  }

  @Test
  public void testRead2 ()
  {
    CSSStyleRule aSR;
    aSR = _parse ("div, .colored, #my-red, #menu > .active, a[href^=red] { }");
    assertEquals (5, aSR.getSelectorCount ());

    assertEquals (1, aSR.getSelectorAtIndex (0).getMemberCount ());
    assertTrue (aSR.getSelectorAtIndex (0).getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);

    assertEquals (1, aSR.getSelectorAtIndex (1).getMemberCount ());
    assertTrue (aSR.getSelectorAtIndex (1).getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);

    assertEquals (1, aSR.getSelectorAtIndex (2).getMemberCount ());
    assertTrue (aSR.getSelectorAtIndex (2).getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);

    assertEquals (3, aSR.getSelectorAtIndex (3).getMemberCount ());
    assertTrue (aSR.getSelectorAtIndex (3).getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);
    assertTrue (aSR.getSelectorAtIndex (3).getMemberAtIndex (1) instanceof ECSSSelectorCombinator);
    assertTrue (aSR.getSelectorAtIndex (3).getMemberAtIndex (2) instanceof CSSSelectorSimpleMember);

    assertEquals (2, aSR.getSelectorAtIndex (4).getMemberCount ());
    assertTrue (aSR.getSelectorAtIndex (4).getMemberAtIndex (0) instanceof CSSSelectorSimpleMember);
    assertTrue (aSR.getSelectorAtIndex (4).getMemberAtIndex (1) instanceof CSSSelectorAttribute);

    // Create the same rule by application
    final CSSStyleRule aCreated = new CSSStyleRule ();
    aCreated.addSelector (new CSSSelectorSimpleMember ("div"));
    aCreated.addSelector (new CSSSelectorSimpleMember (".colored"));
    aCreated.addSelector (new CSSSelectorSimpleMember ("#my-red"));
    aCreated.addSelector (new CSSSelector ().addMember (new CSSSelectorSimpleMember ("#menu"))
                                            .addMember (ECSSSelectorCombinator.GREATER)
                                            .addMember (new CSSSelectorSimpleMember (".active")));
    aCreated.addSelector (new CSSSelector ().addMember (new CSSSelectorSimpleMember ("a"))
                                            .addMember (new CSSSelectorAttribute (null,
                                                                                  "href",
                                                                                  ECSSAttributeOperator.BEGINMATCH,
                                                                                  "red")));
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aSR, aCreated);
  }
}
