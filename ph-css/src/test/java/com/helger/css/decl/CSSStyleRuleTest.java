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

import org.jspecify.annotations.NonNull;
import org.junit.Test;

import com.helger.css.reader.CSSReader;
import com.helger.unittest.support.TestHelper;

/**
 * Test class for {@link CSSStyleRule}.
 *
 * @author Philip Helger
 */
public final class CSSStyleRuleTest
{
  @NonNull
  private static CSSStyleRule _parse (@NonNull final String sCSS)
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sCSS);
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
    CSSStyleRule aSR = _parse ("div { color: red; }");
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
                  ((CSSExpressionMemberTermSimple) aSR.getDeclarationAtIndex (0).getExpression ().getMemberAtIndex (0))
                                                                                                                       .getValue ());

    // Create the same rule by application
    final CSSStyleRule aCreated = new CSSStyleRule ();
    aCreated.addSelector (new CSSSelectorSimpleMember ("div"));
    aCreated.addDeclaration ("color", CSSExpression.createSimple ("red"), false);
    TestHelper.testDefaultImplementationWithEqualContentObject (aSR, aCreated);
  }

  @Test
  public void testRead2 ()
  {
    CSSStyleRule aSR = _parse ("div, .colored, #my-red, #menu > .active, a[href^=red i] { }");
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
    assertEquals (ECSSAttributeCase.CASE_INSENSITIVE, ((CSSSelectorAttribute) aSR.getSelectorAtIndex (4).getMemberAtIndex (1)).getCaseSensitivityFlag ());

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
                                                                                  "red",
                                                                                  ECSSAttributeCase.CASE_INSENSITIVE)));
    TestHelper.testDefaultImplementationWithEqualContentObject (aSR, aCreated);
  }

  @Test
  public void testRead3 ()
  {
    CSSStyleRule aSR = _parse ("div { p { color: red; } }");
    assertEquals (1, aSR.getSelectorCount ());
    assertEquals (0, aSR.getDeclarationCount ());
    assertEquals (1, aSR.getRuleCount ());

    assertEquals("div", aSR.getSelectorAtIndex(0).getAsCSSString());

    assertTrue (aSR.getRuleAtIndex (0) instanceof CSSStyleRule);
    assertEquals (1, ((CSSStyleRule)aSR.getRuleAtIndex (0)).getSelectorCount());
    assertEquals (1, ((CSSStyleRule)aSR.getRuleAtIndex (0)).getDeclarationCount());
    assertEquals (0, ((CSSStyleRule)aSR.getRuleAtIndex (0)).getRuleCount());

    assertEquals (1, ((CSSStyleRule)aSR.getRuleAtIndex (0)).getSelectorCount());
    assertEquals (1, ((CSSStyleRule)aSR.getRuleAtIndex (0)).getDeclarationCount());
    assertEquals (0, ((CSSStyleRule)aSR.getRuleAtIndex (0)).getRuleCount());
    assertEquals ("p", ((CSSStyleRule)aSR.getRuleAtIndex (0)).getSelectorAtIndex(0).getAsCSSString());
    assertEquals ("color:red", ((CSSStyleRule)aSR.getRuleAtIndex (0)).getDeclarationAtIndex(0).getAsCSSString());
  }

  @Test
  public void testRead4 ()
  {
    CSSStyleRule aSR = _parse ("""
      div {
        color: red;
        p: dummy;
        p {
          color: dummy;
        }
        .foobar {
          color: green;
          #id {
            color: blue
          }
          color: white
        }
        color: yellow;
        @media print {
          .print {
            color: black;
            &:hover {
              color: orange;
              font-size: 20px;
            }
          }
        }
        @layer state {
          .alert {
            background-color: brown;
            p {
              border: medium solid limegreen;
            }
          }
        }
      }""");
    assertEquals (2, aSR.getDeclarationCount ());
    assertEquals (5, aSR.getRuleCount ());

    assertEquals ("color:red", aSR.getDeclarationAtIndex(0).getAsCSSString());
    assertEquals ("p:dummy", aSR.getDeclarationAtIndex(1).getAsCSSString());

    assertTrue (aSR.getRuleAtIndex (0) instanceof CSSStyleRule);
    assertEquals (1, ((CSSStyleRule)aSR.getRuleAtIndex (0)).getSelectorCount());
    assertEquals (1, ((CSSStyleRule)aSR.getRuleAtIndex (0)).getDeclarationCount());
    assertEquals (0, ((CSSStyleRule)aSR.getRuleAtIndex (0)).getRuleCount());
    assertEquals ("p", ((CSSStyleRule)aSR.getRuleAtIndex (0)).getSelectorAtIndex(0).getAsCSSString());
    assertEquals ("color:dummy", ((CSSStyleRule)aSR.getRuleAtIndex (0)).getDeclarationAtIndex(0).getAsCSSString());

    assertTrue (aSR.getRuleAtIndex (1) instanceof CSSStyleRule);
    assertEquals (1, ((CSSStyleRule)aSR.getRuleAtIndex (1)).getSelectorCount());
    assertEquals (1, ((CSSStyleRule)aSR.getRuleAtIndex (1)).getDeclarationCount());
    assertEquals (2, ((CSSStyleRule)aSR.getRuleAtIndex (1)).getRuleCount());
    assertEquals (".foobar", ((CSSStyleRule)aSR.getRuleAtIndex (1)).getSelectorAtIndex(0).getAsCSSString());
    assertEquals ("color:green", ((CSSStyleRule)aSR.getRuleAtIndex (1)).getDeclarationAtIndex(0).getAsCSSString());
    assertTrue (((CSSStyleRule)aSR.getRuleAtIndex (1)).getRuleAtIndex(0) instanceof CSSStyleRule);
    assertEquals (1, ((CSSStyleRule)((CSSStyleRule)aSR.getRuleAtIndex (1)).getRuleAtIndex(0)).getDeclarationCount());
    assertEquals (0, ((CSSStyleRule)((CSSStyleRule)aSR.getRuleAtIndex (1)).getRuleAtIndex(0)).getRuleCount());
    assertEquals ("color:blue", ((CSSStyleRule)((CSSStyleRule)aSR.getRuleAtIndex (1)).getRuleAtIndex(0)).getDeclarationAtIndex(0).getAsCSSString());
    assertTrue (((CSSStyleRule)aSR.getRuleAtIndex (1)).getRuleAtIndex(1) instanceof CSSNestedDeclarations);
    assertEquals (1, ((CSSNestedDeclarations)((CSSStyleRule)aSR.getRuleAtIndex (1)).getRuleAtIndex(1)).getDeclarationCount());
    assertEquals ("color:white", ((CSSNestedDeclarations)((CSSStyleRule)aSR.getRuleAtIndex (1)).getRuleAtIndex(1)).getDeclarationAtIndex(0).getAsCSSString());

    assertTrue (aSR.getRuleAtIndex (2) instanceof CSSNestedDeclarations);
    assertEquals (1, ((CSSNestedDeclarations)aSR.getRuleAtIndex (2)).getDeclarationCount());
    assertEquals ("color:yellow", ((CSSNestedDeclarations)aSR.getRuleAtIndex (2)).getDeclarationAtIndex(0).getAsCSSString());

    assertTrue (aSR.getRuleAtIndex (3) instanceof CSSMediaRule);
    assertEquals (1, ((CSSMediaRule)aSR.getRuleAtIndex (3)).getRuleCount());
    assertTrue (((CSSMediaRule)aSR.getRuleAtIndex (3)).getRuleAtIndex(0) instanceof CSSStyleRule);
    assertEquals (1, ((CSSStyleRule)((CSSMediaRule)aSR.getRuleAtIndex (3)).getRuleAtIndex(0)).getSelectorCount());
    assertEquals (1, ((CSSStyleRule)((CSSMediaRule)aSR.getRuleAtIndex (3)).getRuleAtIndex(0)).getDeclarationCount());
    assertEquals (1, ((CSSStyleRule)((CSSMediaRule)aSR.getRuleAtIndex (3)).getRuleAtIndex(0)).getRuleCount());
    assertEquals (".print", ((CSSStyleRule)((CSSMediaRule)aSR.getRuleAtIndex (3)).getRuleAtIndex(0)).getSelectorAtIndex(0).getAsCSSString());
    assertEquals ("color:black", ((CSSStyleRule)((CSSMediaRule)aSR.getRuleAtIndex (3)).getRuleAtIndex(0)).getDeclarationAtIndex(0).getAsCSSString());
    assertTrue (((CSSStyleRule)((CSSMediaRule)aSR.getRuleAtIndex (3)).getRuleAtIndex(0)).getRuleAtIndex(0) instanceof CSSStyleRule);
    assertEquals (1, ((CSSStyleRule)((CSSStyleRule)((CSSMediaRule)aSR.getRuleAtIndex (3)).getRuleAtIndex(0)).getRuleAtIndex(0)).getSelectorCount());
    assertEquals (2, ((CSSStyleRule)((CSSStyleRule)((CSSMediaRule)aSR.getRuleAtIndex (3)).getRuleAtIndex(0)).getRuleAtIndex(0)).getDeclarationCount());
    assertEquals (0, ((CSSStyleRule)((CSSStyleRule)((CSSMediaRule)aSR.getRuleAtIndex (3)).getRuleAtIndex(0)).getRuleAtIndex(0)).getRuleCount());
    assertEquals ("&:hover", ((CSSStyleRule)((CSSStyleRule)((CSSMediaRule)aSR.getRuleAtIndex (3)).getRuleAtIndex(0)).getRuleAtIndex(0)).getSelectorAtIndex(0).getAsCSSString());
    assertEquals ("color:orange", ((CSSStyleRule)((CSSStyleRule)((CSSMediaRule)aSR.getRuleAtIndex (3)).getRuleAtIndex(0)).getRuleAtIndex(0)).getDeclarationAtIndex(0).getAsCSSString());
    assertEquals ("font-size:20px", ((CSSStyleRule)((CSSStyleRule)((CSSMediaRule)aSR.getRuleAtIndex (3)).getRuleAtIndex(0)).getRuleAtIndex(0)).getDeclarationAtIndex(1).getAsCSSString());

    assertTrue (aSR.getRuleAtIndex (4) instanceof CSSLayerRule);
    assertEquals (1, ((CSSLayerRule)aSR.getRuleAtIndex (4)).getSelectorCount());
    assertEquals (1, ((CSSLayerRule)aSR.getRuleAtIndex (4)).getRuleCount());
    assertEquals ("state", ((CSSLayerRule)aSR.getRuleAtIndex (4)).getSelectorAtIndex(0));
    assertTrue (((CSSLayerRule)aSR.getRuleAtIndex (4)).getRuleAtIndex(0) instanceof CSSStyleRule);
    assertEquals (1, ((CSSStyleRule)((CSSLayerRule)aSR.getRuleAtIndex (4)).getRuleAtIndex(0)).getSelectorCount());
    assertEquals (1, ((CSSStyleRule)((CSSLayerRule)aSR.getRuleAtIndex (4)).getRuleAtIndex(0)).getDeclarationCount());
    assertEquals (1, ((CSSStyleRule)((CSSLayerRule)aSR.getRuleAtIndex (4)).getRuleAtIndex(0)).getRuleCount());
    assertEquals (".alert", ((CSSStyleRule)((CSSLayerRule)aSR.getRuleAtIndex (4)).getRuleAtIndex(0)).getSelectorAtIndex(0).getAsCSSString());
    assertEquals ("background-color:brown", ((CSSStyleRule)((CSSLayerRule)aSR.getRuleAtIndex (4)).getRuleAtIndex(0)).getDeclarationAtIndex(0).getAsCSSString());
    assertTrue (((CSSStyleRule)((CSSLayerRule)aSR.getRuleAtIndex (4)).getRuleAtIndex(0)).getRuleAtIndex(0) instanceof CSSStyleRule);
    assertEquals (1, ((CSSStyleRule)((CSSStyleRule)((CSSLayerRule)aSR.getRuleAtIndex (4)).getRuleAtIndex(0)).getRuleAtIndex(0)).getSelectorCount());
    assertEquals (1, ((CSSStyleRule)((CSSStyleRule)((CSSLayerRule)aSR.getRuleAtIndex (4)).getRuleAtIndex(0)).getRuleAtIndex(0)).getDeclarationCount());
    assertEquals (0, ((CSSStyleRule)((CSSStyleRule)((CSSLayerRule)aSR.getRuleAtIndex (4)).getRuleAtIndex(0)).getRuleAtIndex(0)).getRuleCount());
    assertEquals ("p", ((CSSStyleRule)((CSSStyleRule)((CSSLayerRule)aSR.getRuleAtIndex (4)).getRuleAtIndex(0)).getRuleAtIndex(0)).getSelectorAtIndex(0).getAsCSSString());
    assertEquals ("border:medium solid limegreen", ((CSSStyleRule)((CSSStyleRule)((CSSLayerRule)aSR.getRuleAtIndex (4)).getRuleAtIndex(0)).getRuleAtIndex(0)).getDeclarationAtIndex(0).getAsCSSString());
  }

  @Test
  public void testRead5 ()
  {
    CSSStyleRule aSR = _parse ("""
    div {
      color: red;
      .a1 { color: green; }
      color: blue;
      .a2 { color: orange; }
      color: yellow;
      .a3 { color: white; }
      color: cyan;
    }
    """);
    assertEquals (1, aSR.getSelectorCount ());
    assertEquals (1, aSR.getDeclarationCount ());
    assertEquals (6, aSR.getRuleCount ());

    assertEquals ("div", aSR.getSelectorAtIndex(0).getAsCSSString());
    assertEquals ("color:red", aSR.getDeclarationAtIndex(0).getAsCSSString());

    assertTrue (aSR.getRuleAtIndex (0) instanceof CSSStyleRule);
    assertEquals (1, ((CSSStyleRule)aSR.getRuleAtIndex (0)).getSelectorCount());
    assertEquals (1, ((CSSStyleRule)aSR.getRuleAtIndex (0)).getDeclarationCount());
    assertEquals (0, ((CSSStyleRule)aSR.getRuleAtIndex (0)).getRuleCount());
    assertEquals (".a1", ((CSSStyleRule)aSR.getRuleAtIndex (0)).getSelectorAtIndex(0).getAsCSSString());
    assertEquals ("color:green", ((CSSStyleRule)aSR.getRuleAtIndex (0)).getDeclarationAtIndex(0).getAsCSSString());

    assertTrue (aSR.getRuleAtIndex (1) instanceof CSSNestedDeclarations);
    assertEquals (1, ((CSSNestedDeclarations)aSR.getRuleAtIndex (1)).getDeclarationCount());
    assertEquals ("color:blue", ((CSSNestedDeclarations)aSR.getRuleAtIndex (1)).getDeclarationAtIndex(0).getAsCSSString());

    assertTrue (aSR.getRuleAtIndex (2) instanceof CSSStyleRule);
    assertEquals (1, ((CSSStyleRule)aSR.getRuleAtIndex (2)).getSelectorCount());
    assertEquals (1, ((CSSStyleRule)aSR.getRuleAtIndex (2)).getDeclarationCount());
    assertEquals (0, ((CSSStyleRule)aSR.getRuleAtIndex (2)).getRuleCount());
    assertEquals (".a2", ((CSSStyleRule)aSR.getRuleAtIndex (2)).getSelectorAtIndex(0).getAsCSSString());
    assertEquals ("color:orange", ((CSSStyleRule)aSR.getRuleAtIndex (2)).getDeclarationAtIndex(0).getAsCSSString());

    assertTrue (aSR.getRuleAtIndex (3) instanceof CSSNestedDeclarations);
    assertEquals (1, ((CSSNestedDeclarations)aSR.getRuleAtIndex (3)).getDeclarationCount());
    assertEquals ("color:yellow", ((CSSNestedDeclarations)aSR.getRuleAtIndex (3)).getDeclarationAtIndex(0).getAsCSSString());

    assertTrue (aSR.getRuleAtIndex (4) instanceof CSSStyleRule);
    assertEquals (1, ((CSSStyleRule)aSR.getRuleAtIndex (4)).getSelectorCount());
    assertEquals (1, ((CSSStyleRule)aSR.getRuleAtIndex (4)).getDeclarationCount());
    assertEquals (0, ((CSSStyleRule)aSR.getRuleAtIndex (4)).getRuleCount());
    assertEquals (".a3", ((CSSStyleRule)aSR.getRuleAtIndex (4)).getSelectorAtIndex(0).getAsCSSString());
    assertEquals ("color:white", ((CSSStyleRule)aSR.getRuleAtIndex (4)).getDeclarationAtIndex(0).getAsCSSString());

    assertTrue (aSR.getRuleAtIndex (5) instanceof CSSNestedDeclarations);
    assertEquals (1, ((CSSNestedDeclarations)aSR.getRuleAtIndex (5)).getDeclarationCount());
    assertEquals ("color:cyan", ((CSSNestedDeclarations)aSR.getRuleAtIndex (5)).getDeclarationAtIndex(0).getAsCSSString());
  }
}
