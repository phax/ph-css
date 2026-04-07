package com.helger.css.decl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jspecify.annotations.NonNull;
import org.junit.Test;

import com.helger.css.reader.CSSReader;

/**
 * Test class for {@link CSSLayerRule}.
 *
 * @author Philip Helger
 */
public class CSSLayerRuleTest
{
  @NonNull
  private static CSSLayerRule _parse (@NonNull final String sCSS)
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sCSS);
    assertNotNull (sCSS, aCSS);
    assertTrue (aCSS.hasLayerRules ());
    assertEquals (1, aCSS.getLayerRuleCount ());
    final CSSLayerRule ret = aCSS.getAllLayerRules ().get (0);
    assertNotNull (ret);
    return ret;
  }

  @Test
  public void testRead1 ()
  {
    final CSSLayerRule aSR = _parse ("""
        @layer state {
          .foo {
            color: white;
            .bar {
              color: orange
            }
            color: black;
          }
        }
        """);
    assertEquals (1, aSR.getSelectorCount ());
    assertEquals (1, aSR.getRuleCount ());

    assertEquals ("state", aSR.getSelectorAtIndex (0));

    final CSSStyleRule rule1 = (CSSStyleRule) aSR.getRuleAtIndex (0);
    assertEquals (1, rule1.getSelectorCount ());
    assertEquals (1, rule1.getDeclarationCount ());
    assertEquals (2, rule1.getRuleCount ());

    assertEquals (".foo", rule1.getSelectorAtIndex (0).getAsCSSString ());
    assertEquals ("color:white", rule1.getDeclarationAtIndex (0).getAsCSSString ());

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
