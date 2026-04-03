package com.helger.css.decl;

import com.helger.css.reader.CSSReader;
import org.jspecify.annotations.NonNull;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link CSSLayerRule}.
 *
 * @author Philip Helger
 */
public class CSSPropertyRuleTest {
  @NonNull
  private static CSSPropertyRule _parse (@NonNull final String sCSS)
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sCSS);
    assertNotNull (sCSS, aCSS);
    assertTrue (aCSS.hasPropertyRules ());
    assertEquals (1, aCSS.getPropertyRuleCount ());
    final CSSPropertyRule ret = aCSS.getAllPropertyRules ().get (0);
    assertNotNull (ret);
    return ret;
  }

  @Test
  public void testRead1 ()
  {
    CSSPropertyRule aSR = _parse ("""
    @property --rotation {
      syntax: "<angle>";
      inherits: false;
      initial-value: 45deg;
    }
    """);
    assertEquals ("--rotation", aSR.getPropertyName ());
    assertEquals (3, aSR.getDeclarationCount ());
    assertEquals ("syntax", aSR.getDeclarationAtIndex (0).getProperty ());
    assertEquals ("\"<angle>\"", aSR.getDeclarationAtIndex (0).getExpression ().getAsCSSString ());
    assertEquals ("inherits", aSR.getDeclarationAtIndex (1).getProperty ());
    assertEquals ("false", aSR.getDeclarationAtIndex (1).getExpression().getAsCSSString ());
    assertEquals ("initial-value", aSR.getDeclarationAtIndex (2).getProperty ());
    assertEquals ("45deg", aSR.getDeclarationAtIndex (2).getExpression ().getAsCSSString ());
  }

  @Test
  public void testRead2 ()
  {
    CSSPropertyRule aSR = _parse ("@property --rotation {}");
    assertEquals ("--rotation", aSR.getPropertyName ());
    assertEquals (0, aSR.getDeclarationCount ());
  }

  @Test
  public void testRead3 ()
  {
    // Unknown descriptors are invalid and ignored, but do not invalidate the @property rule.
    CSSPropertyRule aSR = _parse ("@property --rotation { color: red;}");
    assertEquals ("--rotation", aSR.getPropertyName ());
    assertEquals (1, aSR.getDeclarationCount ());
    assertEquals ("color", aSR.getDeclarationAtIndex (0).getProperty ());
    assertEquals ("red", aSR.getDeclarationAtIndex (0).getExpression ().getAsCSSString ());
  }

  @Test
  public void testWrite1 ()
  {
    CSSPropertyRule aSR = _parse ("""
    @property --rotation {
      syntax: "<angle>";
      inherits: false;
      initial-value: 45deg;
    }
    """);
    assertEquals ("""
      @property --rotation {
        syntax:"<angle>";
        inherits:false;
        initial-value:45deg;
      }""", aSR.getAsCSSString ());
  }

  @Test
  public void testWrite2 ()
  {
    CSSPropertyRule aSR = _parse ("@property --rotation {}");
    assertEquals ("@property --rotation {}", aSR.getAsCSSString ());
  }
}
