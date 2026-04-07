package com.helger.css.decl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jspecify.annotations.NonNull;
import org.junit.Test;

import com.helger.css.reader.CSSReader;

/**
 * Test class for {@link CSSMediaRule}.
 *
 * @author Philip Helger
 */
public class CSSMediaRuleTest
{
  @NonNull
  private static CSSMediaRule _parse (@NonNull final String sCSS)
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sCSS);
    assertNotNull (sCSS, aCSS);
    assertTrue (aCSS.hasMediaRules ());
    assertEquals (1, aCSS.getMediaRuleCount ());
    final CSSMediaRule ret = aCSS.getAllMediaRules ().get (0);
    assertNotNull (ret);
    return ret;
  }

  @Test
  public void testRead1 ()
  {
    final CSSMediaRule aSR = _parse ("""
        @media print {
          .foo {
            color: white;
            .bar {
              color: orange
            }
            color: black;
          }
        }
        """);
    assertEquals (1, aSR.getMediaQueryCount ());
    assertEquals (1, aSR.getRuleCount ());

    final CSSMediaQuery mediaQuery = aSR.getMediaQueryAtIndex (0);
    assertEquals ("print", mediaQuery.getAsCSSString ());

    final CSSStyleRule rule1 = (CSSStyleRule) aSR.getRuleAtIndex (0);
    assertEquals (1, rule1.getSelectorCount ());
    assertEquals (1, rule1.getDeclarationCount ());
    assertEquals (2, rule1.getRuleCount ());

    final CSSSelector selector1 = rule1.getSelectorAtIndex (0);
    assertEquals (".foo", selector1.getAsCSSString ());

    final CSSDeclaration declaration11 = rule1.getDeclarationAtIndex (0);
    assertEquals ("color:white", declaration11.getAsCSSString ());

    final CSSStyleRule rule11 = (CSSStyleRule) rule1.getRuleAtIndex (0);
    assertEquals (1, rule11.getSelectorCount ());
    assertEquals (1, rule11.getDeclarationCount ());
    assertEquals (0, rule11.getRuleCount ());
    assertEquals (".bar", rule11.getSelectorAtIndex (0).getAsCSSString ());
    assertEquals ("color:orange", rule11.getDeclarationAtIndex (0).getAsCSSString ());

    final CSSNestedDeclarations rule12 = (CSSNestedDeclarations) rule1.getRuleAtIndex (1);
    assertEquals (1, rule12.getDeclarationCount ());
    assertEquals ("color:black", rule12.getDeclarationAtIndex (0).getAsCSSString ());
  }
}
