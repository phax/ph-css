package com.helger.css.decl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jspecify.annotations.NonNull;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.collection.commons.ICommonsList;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.reader.errorhandler.CollectingCSSParseErrorHandler;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Test class for {@link CSSPropertyRule}.
 *
 * @author Philip Helger
 */
public class CSSPropertyRuleTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (CSSPropertyRuleTest.class);
  private final CollectingCSSParseErrorHandler m_aPEH = new CollectingCSSParseErrorHandler ();

  @NonNull
  private CSSPropertyRule _parse (final boolean bBrowserCompliant, @NonNull final String sCSS)
  {
    final CSSReaderSettings aSettings = new CSSReaderSettings ().setCustomErrorHandler (m_aPEH).setBrowserCompliantMode (bBrowserCompliant);
    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader (sCSS, aSettings);
    assertNotNull (sCSS, aCSS);
    assertTrue (aCSS.hasPropertyRules ());
    assertEquals (1, aCSS.getPropertyRuleCount ());
    final CSSPropertyRule ret = aCSS.getAllPropertyRules ().get (0);
    assertNotNull (ret);
    return ret;
  }

  @NonNull
  private ICommonsList<CSSStyleRule> _parseStyleRules (@NonNull final String sCSS)
  {
    final CSSReaderSettings aSettings = new CSSReaderSettings ().setCustomErrorHandler (m_aPEH);
    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader (sCSS, aSettings);
    assertNotNull (sCSS, aCSS);
    assertTrue (aCSS.hasStyleRules ());
    final ICommonsList<CSSStyleRule> ret = aCSS.getAllStyleRules ();
    assertTrue (!ret.isEmpty());
    return ret;
  }

  @Test
  public void testReadNoWhitespace ()
  {
    CSSPropertyRule aPR = _parse (false, "@property --rotation{syntax:\"<angle>\";inherits: false;initial-value:45deg;}");
    assertEquals (0, m_aPEH.getParseErrorCount ());
    assertEquals (3, aPR.getDeclarationCount ());
    assertEquals ("syntax", aPR.getDeclarationAtIndex (0).getDescriptor ());
    assertEquals ("\"<angle>\"", aPR.getDeclarationAtIndex (0).getExpression ().getAsCSSString ());
    assertEquals ("inherits", aPR.getDeclarationAtIndex (1).getDescriptor ());
    assertEquals ("false", aPR.getDeclarationAtIndex (1).getExpression ().getAsCSSString ());
    assertEquals ("initial-value", aPR.getDeclarationAtIndex (2).getDescriptor ());
    assertEquals ("45deg", aPR.getDeclarationAtIndex (2).getExpression ().getAsCSSString ());
  }

  @Test
  public void testReadMultipleWhitespace ()
  {
    CSSPropertyRule aPR = _parse (false, "  @property   --rotation   {  syntax:   \"<angle>\"  ;   inherits  : false   ;  initial-value  : 45deg   ;   }   ");
    assertEquals (0, m_aPEH.getParseErrorCount ());
    assertEquals (3, aPR.getDeclarationCount ());
    assertEquals ("syntax", aPR.getDeclarationAtIndex (0).getDescriptor ());
    assertEquals ("\"<angle>\"", aPR.getDeclarationAtIndex (0).getExpression ().getAsCSSString ());
    assertEquals ("inherits", aPR.getDeclarationAtIndex (1).getDescriptor ());
    assertEquals ("false", aPR.getDeclarationAtIndex (1).getExpression ().getAsCSSString ());
    assertEquals ("initial-value", aPR.getDeclarationAtIndex (2).getDescriptor ());
    assertEquals ("45deg", aPR.getDeclarationAtIndex (2).getExpression ().getAsCSSString ());
  }

  @Test
  public void testReadMultipleWhitespaceAndInvalidDeclaration ()
  {
    CSSPropertyRule aPR = _parse (false, "  @property   --rotation   {  syntax:   \"<angle>\"  ;   inherits  : false   ;   color   :  red  ;   }  ");
    assertEquals (1, m_aPEH.getParseErrorCount ());
    assertEquals (2, aPR.getDeclarationCount ());
    assertEquals ("syntax", aPR.getDeclarationAtIndex (0).getDescriptor ());
    assertEquals ("\"<angle>\"", aPR.getDeclarationAtIndex (0).getExpression ().getAsCSSString ());
    assertEquals ("inherits", aPR.getDeclarationAtIndex (1).getDescriptor ());
    assertEquals ("false", aPR.getDeclarationAtIndex (1).getExpression ().getAsCSSString ());
  }

  @Test
  public void testReadMultipleDeclarations ()
  {
    CSSPropertyRule aPR = _parse (false, """
    @property --rotation {
      syntax: "<angle>";
      inherits: false;
      initial-value: 45deg;
    }
    """);
    assertEquals (0, m_aPEH.getParseErrorCount ());
    assertEquals (3, aPR.getDeclarationCount ());
    assertEquals ("syntax", aPR.getDeclarationAtIndex (0).getDescriptor ());
    assertEquals ("\"<angle>\"", aPR.getDeclarationAtIndex (0).getExpression ().getAsCSSString ());
    assertEquals ("inherits", aPR.getDeclarationAtIndex (1).getDescriptor ());
    assertEquals ("false", aPR.getDeclarationAtIndex (1).getExpression ().getAsCSSString ());
    assertEquals ("initial-value", aPR.getDeclarationAtIndex (2).getDescriptor ());
    assertEquals ("45deg", aPR.getDeclarationAtIndex (2).getExpression ().getAsCSSString ());
  }

  @Test
  public void testReadNoDeclarations ()
  {
    CSSPropertyRule aPR = _parse (false, "@property --rotation {}");
    assertEquals (0, m_aPEH.getParseErrorCount ());
    assertEquals (0, aPR.getDeclarationCount ());
  }

  @Test
  public void testReadWithMissingValue ()
  {
    CSSPropertyRule aPR = _parse (false, "@property --rotation { syntax: \"<angle>\"; inherits: false; initial-value:}");
    assertEquals (1, m_aPEH.getParseErrorCount ());
    assertTrue (m_aPEH.getAllParseErrors ().get (0).getErrorMessage ().contains("Encountered text '}' corresponding to token \"}\". Skipped until token }"));
    assertEquals (2, aPR.getDeclarationCount ());
    assertEquals ("syntax", aPR.getDeclarationAtIndex (0).getDescriptor ());
    assertEquals ("\"<angle>\"", aPR.getDeclarationAtIndex (0).getExpression ().getAsCSSString ());
    assertEquals ("inherits", aPR.getDeclarationAtIndex (1).getDescriptor ());
    assertEquals ("false", aPR.getDeclarationAtIndex (1).getExpression ().getAsCSSString ());
  }

  @Test
  public void testReadWithIEHack ()
  {
    CSSPropertyRule aPR = _parse (false, "@property --rotation { syntax: \"<angle>\"; inherits: false; *zoom:1; }");
    assertEquals (1, m_aPEH.getParseErrorCount ());
    assertTrue (m_aPEH.getAllParseErrors ().get (0).getErrorMessage ().contains("Encountered text 'zoom' corresponding to token <IDENT>. Skipped until token ;"));
    assertEquals (2, aPR.getDeclarationCount ());
    assertEquals ("syntax", aPR.getDeclarationAtIndex (0).getDescriptor ());
    assertEquals ("\"<angle>\"", aPR.getDeclarationAtIndex (0).getExpression ().getAsCSSString ());
    assertEquals ("inherits", aPR.getDeclarationAtIndex (1).getDescriptor ());
    assertEquals ("false", aPR.getDeclarationAtIndex (1).getExpression ().getAsCSSString ());
  }

  @Test
  public void testReadSingleDeclaration ()
  {
    CSSPropertyRule aPR = _parse (false, "@property --rotation { syntax: \"<angle>\";}");
    assertEquals (0, m_aPEH.getParseErrorCount ());
    assertEquals (1, aPR.getDeclarationCount ());
    assertEquals ("syntax", aPR.getDeclarationAtIndex (0).getDescriptor ());
    assertEquals ("\"<angle>\"", aPR.getDeclarationAtIndex (0).getExpression ().getAsCSSString ());
  }

  @Test
  public void testReadWithoutSemicolonAfterFinalDeclaration ()
  {
    CSSPropertyRule aPR = _parse (true, "@property --rotation { syntax: \"<angle>\"; inherits: false }");
    assertEquals (0, m_aPEH.getAllParseErrors ().size ());
    assertEquals (2, aPR.getDeclarationCount ());
    assertEquals ("syntax", aPR.getDeclarationAtIndex (0).getDescriptor ());
    assertEquals ("\"<angle>\"", aPR.getDeclarationAtIndex (0).getExpression ().getAsCSSString ());
    assertEquals ("inherits", aPR.getDeclarationAtIndex (1).getDescriptor ());
    assertEquals ("false", aPR.getDeclarationAtIndex (1).getExpression ().getAsCSSString ());
  }

  @Test
  public void testReadInvalidDeclarationWithoutBrowserCompliance ()
  {
    // Unknown descriptors are invalid and ignored, but do not invalidate the @property rule.
    CSSPropertyRule aPR = _parse (false, "@property --rotation { color: red; }");
    assertEquals (1, m_aPEH.getAllParseErrors ().size ());
    assertTrue (m_aPEH.getAllParseErrors ().get (0).getErrorMessage ().contains("Encountered text 'color' corresponding to token <IDENT>. Skipped until token ;"));
    assertEquals (0, aPR.getDeclarationCount ());
  }

  @Test
  public void testReadInvalidDeclarationWithBrowserCompliance ()
  {
    // Unknown descriptors are invalid and ignored, but do not invalidate the @property rule.
    CSSPropertyRule aPR = _parse (true, "@property --rotation { color: red;}");
    assertEquals (1, m_aPEH.getAllParseErrors ().size ());
    assertTrue (m_aPEH.getAllParseErrors ().get (0).getErrorMessage().contains ("Browser compliant mode skipped CSS"));
    assertEquals (0, aPR.getDeclarationCount ());
  }

  @Test
  public void testReadWithInvalidDeclarationInsideWithoutBrowserCompliance ()
  {
    // Unknown descriptors are invalid and ignored, but do not invalidate the @property rule.
    CSSPropertyRule aPR = _parse (false, "@property --rotation { syntax: \"*\"; color: red; inherits: true; }");
    assertEquals (1, m_aPEH.getAllParseErrors ().size ());
    assertTrue (m_aPEH.getAllParseErrors ().get (0).getErrorMessage ().contains("Encountered text 'color' corresponding to token <IDENT>. Skipped until token ;"));
    assertEquals (2, aPR.getDeclarationCount ());
    assertEquals ("syntax", aPR.getDeclarationAtIndex (0).getDescriptor ());
    assertEquals ("\"*\"", aPR.getDeclarationAtIndex (0).getExpression ().getAsCSSString ());
    assertEquals ("inherits", aPR.getDeclarationAtIndex (1).getDescriptor ());
    assertEquals ("true", aPR.getDeclarationAtIndex (1).getExpression ().getAsCSSString ());
  }

  @Test
  public void testReadWithInvalidDeclarationInsideWithBrowserCompliance ()
  {
    // Unknown descriptors are invalid and ignored, but do not invalidate the @property rule.
    CSSPropertyRule aPR = _parse (true, "@property --rotation { syntax: \"*\"; color: red; inherits: true; }");
    assertEquals (1, m_aPEH.getAllParseErrors ().size ());
    assertTrue (m_aPEH.getAllParseErrors ().get (0).getErrorMessage().contains ("Browser compliant mode skipped CSS"));
    assertEquals (2, aPR.getDeclarationCount ());
    assertEquals ("syntax", aPR.getDeclarationAtIndex (0).getDescriptor ());
    assertEquals ("\"*\"", aPR.getDeclarationAtIndex (0).getExpression ().getAsCSSString ());
    assertEquals ("inherits", aPR.getDeclarationAtIndex (1).getDescriptor ());
    assertEquals ("true", aPR.getDeclarationAtIndex (1).getExpression ().getAsCSSString ());
  }

  @Test
  public void testReadWithAllValidDeclarationsAndAdditionalInvalidDeclarationsWithoutBrowserCompliance ()
  {
    // Unknown descriptors are invalid and ignored, but do not invalidate the @property rule.
    CSSPropertyRule aPR = _parse (false, "@property --rotation { syntax: \"*\"; color: red; inherits: true; initial-value: 45deg; }");
    assertEquals (1, m_aPEH.getAllParseErrors ().size ());
    assertTrue (m_aPEH.getAllParseErrors ().get (0).getErrorMessage ().contains("Encountered text 'color' corresponding to token <IDENT>. Skipped until token ;"));
    assertEquals (3, aPR.getDeclarationCount ());
    assertEquals ("syntax", aPR.getDeclarationAtIndex (0).getDescriptor ());
    assertEquals ("\"*\"", aPR.getDeclarationAtIndex (0).getExpression ().getAsCSSString ());
    assertEquals ("inherits", aPR.getDeclarationAtIndex (1).getDescriptor ());
    assertEquals ("true", aPR.getDeclarationAtIndex (1).getExpression ().getAsCSSString ());
    assertEquals ("initial-value", aPR.getDeclarationAtIndex (2).getDescriptor ());
    assertEquals ("45deg", aPR.getDeclarationAtIndex (2).getExpression ().getAsCSSString ());
  }

  @Test
  public void testReadWithAllValidDeclarationsAndAdditionalInvalidDeclarationsWithBrowserCompliance ()
  {
    // Unknown descriptors are invalid and ignored, but do not invalidate the @property rule.
    CSSPropertyRule aPR = _parse (true, "@property --rotation { syntax: \"*\"; color: red; inherits: true; initial-value: 45deg; }");
    assertEquals (1, m_aPEH.getAllParseErrors ().size ());
    assertTrue (m_aPEH.getAllParseErrors ().get (0).getErrorMessage().contains ("Browser compliant mode skipped CSS"));
    assertEquals (3, aPR.getDeclarationCount ());
    assertEquals ("syntax", aPR.getDeclarationAtIndex (0).getDescriptor ());
    assertEquals ("\"*\"", aPR.getDeclarationAtIndex (0).getExpression ().getAsCSSString ());
    assertEquals ("inherits", aPR.getDeclarationAtIndex (1).getDescriptor ());
    assertEquals ("true", aPR.getDeclarationAtIndex (1).getExpression ().getAsCSSString ());
    assertEquals ("initial-value", aPR.getDeclarationAtIndex (2).getDescriptor ());
    assertEquals ("45deg", aPR.getDeclarationAtIndex (2).getExpression ().getAsCSSString ());
  }

  @Test
  public void testReadSelectorWithPropertyRuleKeywords ()
  {
    ICommonsList<CSSStyleRule> aRules = _parseStyleRules("""
      syntax { color : red }
      inherits { color : green }
      .initial-value { color : blue }
    """);
    assertEquals(0, m_aPEH.getAllParseErrors ().size ());
    assertEquals (3, aRules.size ());

    assertEquals ("syntax", aRules.get (0).getSelectorAtIndex (0) .getAsCSSString ());
    assertEquals ("inherits", aRules.get (1).getSelectorAtIndex (0).getAsCSSString ());
    assertEquals (".initial-value", aRules.get (2).getSelectorAtIndex (0).getAsCSSString ());

    assertEquals ("color:red", aRules.get (0).getDeclarationAtIndex (0) .getAsCSSString ());
    assertEquals ("color:green", aRules.get (1).getDeclarationAtIndex (0).getAsCSSString ());
    assertEquals ("color:blue", aRules.get (2).getDeclarationAtIndex (0).getAsCSSString ());
  }

  @Test
  public void testWriteMultipleDeclarations ()
  {
    CSSPropertyRule aPR = _parse (false, """
    @property --rotation {
      syntax: "<angle>";
      inherits: false;
      initial-value: 45deg;
    }""");
    assertEquals ("""
      @property --rotation {
        syntax:"<angle>";
        inherits:false;
        initial-value:45deg;
      }""", aPR.getAsCSSString (new CSSWriterSettings (false)));
    assertEquals ("""
      @property --rotation{syntax:"<angle>";inherits:false;initial-value:45deg}""", aPR.getAsCSSString (new CSSWriterSettings (true)));
  }

  @Test
  public void testWriteSingleDeclaration ()
  {
    CSSPropertyRule aPR = _parse (false, "@property --rotation { inherits:false; }");
    assertEquals ("@property --rotation { inherits:false; }", aPR.getAsCSSString (new CSSWriterSettings (false)));
    assertEquals ("@property --rotation{inherits:false}", aPR.getAsCSSString (new CSSWriterSettings (true)));
  }

  @Test
  public void testWriteNoDeclarations ()
  {
    CSSPropertyRule aPR = _parse (false, "@property --rotation {}");
    assertEquals ("@property --rotation {}", aPR.getAsCSSString (new CSSWriterSettings (false)));
    assertEquals ("@property --rotation{}", aPR.getAsCSSString (new CSSWriterSettings (true)));
  }

  @Test
  public void testWriteWithInvalidDeclaration ()
  {
    CSSPropertyRule aPR = _parse (false, """
    @property --rotation {
      syntax: "<angle>";
      inherits: false;
      color: red;
    }""");
    assertEquals ("""
      @property --rotation {
        syntax:"<angle>";
        inherits:false;
      }""", aPR.getAsCSSString (new CSSWriterSettings (false)));
    assertEquals ("""
      @property --rotation{syntax:"<angle>";inherits:false}""", aPR.getAsCSSString (new CSSWriterSettings (true)));
  }
}
