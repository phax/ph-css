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

import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.reader.errorhandler.CollectingCSSParseErrorHandler;
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
  private CollectingCSSParseErrorHandler m_aCEH = new CollectingCSSParseErrorHandler ();

  @NonNull
  private CSSStyleRule _parse (@NonNull final String sCSS)
  {
    CSSReaderSettings settings = new CSSReaderSettings ().setCustomErrorHandler (m_aCEH);
    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader (sCSS, settings);
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
    final CSSStyleRule aSR = _parse ("div { color: red; }");
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
    final CSSStyleRule aSR = _parse ("div, .colored, #my-red, #menu > .active, a[href^=red i] { }");
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
    assertEquals (ECSSAttributeCase.CASE_INSENSITIVE,
                  ((CSSSelectorAttribute) aSR.getSelectorAtIndex (4).getMemberAtIndex (1)).getCaseSensitivityFlag ());

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
    final CSSStyleRule aSR = _parse ("div { p { color: red; } }");
    assertEquals (1, aSR.getSelectorCount ());
    assertEquals (0, aSR.getDeclarationCount ());
    assertEquals (1, aSR.getRuleCount ());

    assertEquals ("div", aSR.getSelectorAtIndex (0).getAsCSSString ());

    assertTrue (aSR.getRuleAtIndex (0) instanceof CSSStyleRule);
    assertEquals (1, ((CSSStyleRule) aSR.getRuleAtIndex (0)).getSelectorCount ());
    assertEquals (1, ((CSSStyleRule) aSR.getRuleAtIndex (0)).getDeclarationCount ());
    assertEquals (0, ((CSSStyleRule) aSR.getRuleAtIndex (0)).getRuleCount ());

    assertEquals (1, ((CSSStyleRule) aSR.getRuleAtIndex (0)).getSelectorCount ());
    assertEquals (1, ((CSSStyleRule) aSR.getRuleAtIndex (0)).getDeclarationCount ());
    assertEquals (0, ((CSSStyleRule) aSR.getRuleAtIndex (0)).getRuleCount ());
    assertEquals ("p", ((CSSStyleRule) aSR.getRuleAtIndex (0)).getSelectorAtIndex (0).getAsCSSString ());
    assertEquals ("color:red", ((CSSStyleRule) aSR.getRuleAtIndex (0)).getDeclarationAtIndex (0).getAsCSSString ());
  }

  @Test
  public void testRead4 ()
  {
    final CSSStyleRule aSR = _parse ("""
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

    assertEquals ("color:red", aSR.getDeclarationAtIndex (0).getAsCSSString ());
    assertEquals ("p:dummy", aSR.getDeclarationAtIndex (1).getAsCSSString ());

    final CSSStyleRule rule1 = (CSSStyleRule) aSR.getRuleAtIndex (0);
    assertEquals (1, rule1.getSelectorCount ());
    assertEquals (1, rule1.getDeclarationCount ());
    assertEquals (0, rule1.getRuleCount ());
    assertEquals ("p", rule1.getSelectorAtIndex (0).getAsCSSString ());
    assertEquals ("color:dummy", rule1.getDeclarationAtIndex (0).getAsCSSString ());

    final CSSStyleRule rule2 = (CSSStyleRule) aSR.getRuleAtIndex (1);
    assertEquals (1, rule2.getSelectorCount ());
    assertEquals (1, rule2.getDeclarationCount ());
    assertEquals (2, rule2.getRuleCount ());
    assertEquals (".foobar", rule2.getSelectorAtIndex (0).getAsCSSString ());
    assertEquals ("color:green", rule2.getDeclarationAtIndex (0).getAsCSSString ());
    final CSSStyleRule rule21 = (CSSStyleRule) rule2.getRuleAtIndex (0);
    assertEquals (1, rule21.getDeclarationCount ());
    assertEquals (0, rule21.getRuleCount ());
    assertEquals ("color:blue", rule21.getDeclarationAtIndex (0).getAsCSSString ());
    final CSSNestedDeclarations rule22 = (CSSNestedDeclarations) rule2.getRuleAtIndex (1);
    assertEquals (1, rule22.getDeclarationCount ());
    assertEquals ("color:white", rule22.getDeclarationAtIndex (0).getAsCSSString ());

    final CSSNestedDeclarations rule3 = (CSSNestedDeclarations) aSR.getRuleAtIndex (2);
    assertEquals (1, rule3.getDeclarationCount ());
    assertEquals ("color:yellow", rule3.getDeclarationAtIndex (0).getAsCSSString ());

    final CSSMediaRule rule4 = (CSSMediaRule) aSR.getRuleAtIndex (3);
    assertEquals (1, rule4.getRuleCount ());
    final CSSStyleRule rule41 = (CSSStyleRule) rule4.getRuleAtIndex (0);
    assertEquals (1, rule41.getSelectorCount ());
    assertEquals (1, rule41.getDeclarationCount ());
    assertEquals (1, rule41.getRuleCount ());
    assertEquals (".print", rule41.getSelectorAtIndex (0).getAsCSSString ());
    assertEquals ("color:black", rule41.getDeclarationAtIndex (0).getAsCSSString ());
    final CSSStyleRule rule411 = (CSSStyleRule) rule41.getRuleAtIndex (0);
    assertEquals (1, rule411.getSelectorCount ());
    assertEquals (2, rule411.getDeclarationCount ());
    assertEquals (0, rule411.getRuleCount ());
    assertEquals ("&:hover", rule411.getSelectorAtIndex (0).getAsCSSString ());
    assertEquals ("color:orange", rule411.getDeclarationAtIndex (0).getAsCSSString ());
    assertEquals ("font-size:20px", rule411.getDeclarationAtIndex (1).getAsCSSString ());

    final CSSLayerRule rule5 = (CSSLayerRule) aSR.getRuleAtIndex (4);
    assertEquals (1, rule5.getSelectorCount ());
    assertEquals (1, rule5.getRuleCount ());
    assertEquals ("state", rule5.getSelectorAtIndex (0));
    assertTrue (rule5.getRuleAtIndex (0) instanceof CSSStyleRule);
    final CSSStyleRule rule51 = (CSSStyleRule) rule5.getRuleAtIndex (0);
    assertEquals (1, rule51.getSelectorCount ());
    assertEquals (1, rule51.getDeclarationCount ());
    assertEquals (1, rule51.getRuleCount ());
    assertEquals (".alert", rule51.getSelectorAtIndex (0).getAsCSSString ());
    assertEquals ("background-color:brown", rule51.getDeclarationAtIndex (0).getAsCSSString ());
    assertEquals (1, rule51.getRuleCount ());
    final CSSStyleRule rule511 = (CSSStyleRule) rule51.getRuleAtIndex (0);
    assertEquals (1, rule511.getSelectorCount ());
    assertEquals (1, rule511.getDeclarationCount ());
    assertEquals (0, rule511.getRuleCount ());
    assertEquals ("p", rule511.getSelectorAtIndex (0).getAsCSSString ());
    assertEquals ("border:medium solid limegreen", rule511.getDeclarationAtIndex (0).getAsCSSString ());
  }

  @Test
  public void testRead5 ()
  {
    final CSSStyleRule aSR = _parse ("""
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

    assertEquals ("div", aSR.getSelectorAtIndex (0).getAsCSSString ());
    assertEquals ("color:red", aSR.getDeclarationAtIndex (0).getAsCSSString ());

    final CSSStyleRule rule1 = (CSSStyleRule) aSR.getRuleAtIndex (0);
    assertEquals (1, rule1.getSelectorCount ());
    assertEquals (1, rule1.getDeclarationCount ());
    assertEquals (0, rule1.getRuleCount ());
    assertEquals (".a1", rule1.getSelectorAtIndex (0).getAsCSSString ());
    assertEquals ("color:green", rule1.getDeclarationAtIndex (0).getAsCSSString ());

    final CSSNestedDeclarations rule2 = (CSSNestedDeclarations) aSR.getRuleAtIndex (1);
    assertEquals (1, rule2.getDeclarationCount ());
    assertEquals ("color:blue", rule2.getDeclarationAtIndex (0).getAsCSSString ());

    final CSSStyleRule rule3 = (CSSStyleRule) aSR.getRuleAtIndex (2);
    assertEquals (1, rule3.getSelectorCount ());
    assertEquals (1, rule3.getDeclarationCount ());
    assertEquals (0, rule3.getRuleCount ());
    assertEquals (".a2", rule3.getSelectorAtIndex (0).getAsCSSString ());
    assertEquals ("color:orange", rule3.getDeclarationAtIndex (0).getAsCSSString ());

    final CSSNestedDeclarations rule4 = (CSSNestedDeclarations) aSR.getRuleAtIndex (3);
    assertEquals (1, rule4.getDeclarationCount ());
    assertEquals ("color:yellow", rule4.getDeclarationAtIndex (0).getAsCSSString ());

    final CSSStyleRule rule5 = (CSSStyleRule) aSR.getRuleAtIndex (4);
    assertEquals (1, rule5.getSelectorCount ());
    assertEquals (1, rule5.getDeclarationCount ());
    assertEquals (0, rule5.getRuleCount ());
    assertEquals (".a3", rule5.getSelectorAtIndex (0).getAsCSSString ());
    assertEquals ("color:white", rule5.getDeclarationAtIndex (0).getAsCSSString ());

    final CSSNestedDeclarations rule6 = (CSSNestedDeclarations) aSR.getRuleAtIndex (5);
    assertEquals (1, rule6.getDeclarationCount ());
    assertEquals ("color:cyan", rule6.getDeclarationAtIndex (0).getAsCSSString ());
  }

  @Test
  public void testReadPropertyRuleCannotBeNested ()
  {
    final CSSStyleRule aSR = _parse ("""
        div {
          @property --rotation {
            syntax: "<angle>";
            inherits: false;
            initial-value: 45deg;
          }
        }
        """);
    assertEquals (1, m_aCEH.getParseErrorCount ());
    assertTrue (m_aCEH.getAllParseErrors ().get (0).getErrorMessage ().contains ("Unexpected rule '@property': property rule is not allowed as a nested rule!"));
    assertEquals (1, aSR.getSelectorCount());
    assertEquals (0, aSR.getDeclarationCount());
    assertEquals (0, aSR.getRuleCount());
  }

  @Test
  public void testReadPageRuleCannotBeNested ()
  {
    final CSSStyleRule aSR = _parse ("""
        div {
          @page :left {
            margin-top: 4in;
          }
        }
        """);
    assertEquals (1, m_aCEH.getParseErrorCount ());
    assertTrue (m_aCEH.getAllParseErrors ().get (0).getErrorMessage ().contains ("Unexpected rule '@page': page rule is not allowed as a nested rule!"));
    assertEquals (1, aSR.getSelectorCount());
    assertEquals (0, aSR.getDeclarationCount());
    assertEquals (0, aSR.getRuleCount());
  }

  @Test
  public void testReadFontFaceRuleCannotBeNested ()
  {
    final CSSStyleRule aSR = _parse ("""
        div {
          @font-face {
            font-family: "Trickster";
            src:
              local("Trickster"),
              url("trickster-COLRv1.otf") format("opentype") tech(color-COLRv1),
              url("trickster-outline.otf") format("opentype"),
              url("trickster-outline.woff2") format("woff2");
          }
        }
        """);
    assertEquals (1, m_aCEH.getParseErrorCount ());
    assertTrue (m_aCEH.getAllParseErrors ().get (0).getErrorMessage ().contains ("Unexpected rule '@font-face': font-face rule is not allowed as a nested rule!"));
    assertEquals (1, aSR.getSelectorCount());
    assertEquals (0, aSR.getDeclarationCount());
    assertEquals (0, aSR.getRuleCount());
  }

  @Test
  public void testReadKeyframesRuleCannotBeNested ()
  {
    final CSSStyleRule aSR = _parse ("""
        div {
          @keyframes slide-in {
           from {
             transform: translateX(0%);
           }
    
           to {
             transform: translateX(100%);
           }
         }
        }
        """);
    assertEquals (1, m_aCEH.getParseErrorCount ());
    assertTrue (m_aCEH.getAllParseErrors ().get (0).getErrorMessage ().contains ("Unexpected rule '@keyframes': keyframes rule is not allowed as a nested rule!"));
    assertEquals (1, aSR.getSelectorCount());
    assertEquals (0, aSR.getDeclarationCount());
    assertEquals (0, aSR.getRuleCount());
  }

  @Test
  public void testReadViewportRuleCannotBeNested ()
  {
    final CSSStyleRule aSR = _parse ("""
        div {
          @viewport {
            width: device-width;
          }
        }
        """);
    assertEquals (1, m_aCEH.getParseErrorCount ());
    assertTrue (m_aCEH.getAllParseErrors ().get (0).getErrorMessage ().contains ("Unexpected rule '@viewport': viewport rule is not allowed as a nested rule!"));
    assertEquals (1, aSR.getSelectorCount());
    assertEquals (0, aSR.getDeclarationCount());
    assertEquals (0, aSR.getRuleCount());
  }

  @Test
  public void testReadCharsetRuleCannotBeNested ()
  {
    final CSSStyleRule aSR = _parse ("""
        div {
          @charset "UTF-8";
        }
        """);
    assertEquals (1, m_aCEH.getParseErrorCount ());
    assertTrue (m_aCEH.getAllParseErrors ().get (0).getErrorMessage ().contains ("Unexpected rule '@charset': charset rule is not allowed as a nested rule!"));
    assertEquals (1, aSR.getSelectorCount());
    assertEquals (0, aSR.getDeclarationCount());
    assertEquals (0, aSR.getRuleCount());
  }

  @Test
  public void testReadImportRuleCannotBeNested ()
  {
    final CSSStyleRule aSR = _parse ("""
        div {
          @import "my-imported-styles.css";
        }
        """);
    assertEquals (1, m_aCEH.getParseErrorCount ());
    assertTrue (m_aCEH.getAllParseErrors ().get (0).getErrorMessage ().contains ("Unexpected rule '@import': import rule is not allowed as a nested rule!"));
    assertEquals (1, aSR.getSelectorCount());
    assertEquals (0, aSR.getDeclarationCount());
    assertEquals (0, aSR.getRuleCount());
  }

  @Test
  public void testReadNamespaceRuleCannotBeNested ()
  {
    final CSSStyleRule aSR = _parse ("""
        div {
          @namespace svg url("http://www.w3.org/2000/svg");
        }
        """);
    assertEquals (1, m_aCEH.getParseErrorCount ());
    assertTrue (m_aCEH.getAllParseErrors ().get (0).getErrorMessage ().contains ("Unexpected rule '@namespace': namespace rule is not allowed as a nested rule!"));
    assertEquals (1, aSR.getSelectorCount());
    assertEquals (0, aSR.getDeclarationCount());
    assertEquals (0, aSR.getRuleCount());
  }
}
