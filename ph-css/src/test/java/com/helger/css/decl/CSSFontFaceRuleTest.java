package com.helger.css.decl;

import com.helger.css.reader.CSSReader;
import org.jspecify.annotations.NonNull;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link CSSFontFaceRule}.
 *
 * @author Philip Helger
 */
public class CSSFontFaceRuleTest {
  @NonNull
  private static CSSFontFaceRule _parse (@NonNull final String sCSS)
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sCSS);
    assertNotNull (sCSS, aCSS);
    assertTrue (aCSS.hasFontFaceRules ());
    assertEquals (1, aCSS.getFontFaceRuleCount ());
    final CSSFontFaceRule ret = aCSS.getAllFontFaceRules ().get (0);
    assertNotNull (ret);
    return ret;
  }

  @Test
  public void testReadUnicodeRangeSingle() {
    CSSFontFaceRule aRule = _parse ("@font-face { unicode-range: U+26; }");

    assertEquals (1, aRule.getDeclarationCount ());
    assertEquals ("unicode-range", aRule.getDeclarationAtIndex(0).getProperty());
    assertEquals ("U+26", aRule.getDeclarationAtIndex(0).getExpression().getAsCSSString());
  }

  @Test
  public void testReadUnicodeRangeFromTo() {
      CSSFontFaceRule aRule = _parse ("@font-face { unicode-range: U+0025-00FF; }");

      assertEquals (1, aRule.getDeclarationCount ());
      assertEquals ("unicode-range", aRule.getDeclarationAtIndex(0).getProperty());
      assertEquals ("U+0025-00FF", aRule.getDeclarationAtIndex(0).getExpression().getAsCSSString());
  }
}
