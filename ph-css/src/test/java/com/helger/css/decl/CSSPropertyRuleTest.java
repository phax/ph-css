package com.helger.css.decl;

import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.utils.CollectingCSSInterpretErrorHandler;
import com.helger.css.writer.CSSWriterSettings;
import org.jspecify.annotations.NonNull;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link CSSLayerRule}.
 *
 * @author Philip Helger
 */
public class CSSPropertyRuleTest {
  private final CollectingCSSInterpretErrorHandler m_aPEH = new CollectingCSSInterpretErrorHandler();

  @NonNull
  private CSSPropertyRule _parse (@NonNull final String sCSS)
  {
    final CSSReaderSettings aSettings = new CSSReaderSettings().setInterpretErrorHandler(m_aPEH);
    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader (sCSS, aSettings);
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
    assertEquals ("--rotation", aSR.getName ());
    assertEquals ("\"<angle>\"", aSR.getSyntax());
    assertFalse (aSR.isInherits());
    assertEquals ("45deg", aSR.getInitialValue());
  }

  @Test
  public void testRead2 ()
  {
    CSSPropertyRule aPR = _parse ("@property --rotation {}");
    assertEquals ("", aPR.getSyntax());
    assertFalse (aPR.isInherits());
    assertEquals ("", aPR.getInitialValue ());
  }

  @Test
  public void testRead3 ()
  {
    CSSPropertyRule aPR = _parse ("@property --rotation { syntax: \"<angle>\";}");
    assertEquals ("--rotation", aPR.getName ());
    assertEquals ("\"<angle>\"", aPR.getSyntax());
    assertFalse (aPR.isInherits());
    assertEquals ("", aPR.getInitialValue ());
  }

  @Test
  public void testRead4 ()
  {
    // Unknown descriptors are invalid and ignored, but do not invalidate the @property rule.
    CSSPropertyRule aPR = _parse ("@property --rotation { color: red;}");
    assertEquals(List.of("Unsupported property rule declaration: color:red"), m_aPEH.getErrors());
    assertEquals ("--rotation", aPR.getName ());
    assertEquals ("", aPR.getSyntax());
    assertFalse (aPR.isInherits());
    assertEquals ("", aPR.getInitialValue ());
  }

  @Test
  public void testWrite1 ()
  {
    CSSPropertyRule aPR = _parse ("""
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
      }""", aPR.getAsCSSString (new CSSWriterSettings (false)));
    assertEquals ("""
      @property --rotation{syntax:"<angle>";inherits:false;initial-value:45deg}""", aPR.getAsCSSString (new CSSWriterSettings (true)));
  }

  @Test
  public void testWrite2 ()
  {
    CSSPropertyRule aPR = _parse ("@property --rotation { inherits:false; }");
    assertEquals ("@property --rotation { inherits:false; }", aPR.getAsCSSString (new CSSWriterSettings (false)));
    assertEquals ("@property --rotation{inherits:false}", aPR.getAsCSSString (new CSSWriterSettings (true)));
  }

  @Test
  public void testWrite3 ()
  {
    CSSPropertyRule aPR = _parse ("@property --rotation {}");
    assertEquals ("@property --rotation {}", aPR.getAsCSSString (new CSSWriterSettings (false)));
    assertEquals ("@property --rotation{}", aPR.getAsCSSString (new CSSWriterSettings (true)));
  }
}
