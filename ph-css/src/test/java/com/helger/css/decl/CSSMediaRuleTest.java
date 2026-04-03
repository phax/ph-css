package com.helger.css.decl;

import com.helger.css.reader.CSSReader;
import org.jspecify.annotations.NonNull;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link CSSMediaRule}.
 *
 * @author Philip Helger
 */
public class CSSMediaRuleTest {
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
        CSSMediaRule aSR = _parse (String.join("\n", List.of(
                "@media print {",
                "  .foo {",
                "    color: white;",
                "    .bar {",
                "      color: orange",
                "    }",
                "    color: black;",
                "  }",
                "}")));
        assertEquals(1, aSR.getMediaQueryCount());
        assertEquals (1, aSR.getRuleCount ());

        assertEquals("print", aSR.getMediaQueryAtIndex(0).getAsCSSString());

        assertTrue (aSR.getRuleAtIndex (0) instanceof CSSStyleRule);
        assertEquals (1, ((CSSStyleRule)aSR.getRuleAtIndex (0)).getSelectorCount());
        assertEquals (1, ((CSSStyleRule)aSR.getRuleAtIndex (0)).getDeclarationCount());
        assertEquals (2, ((CSSStyleRule)aSR.getRuleAtIndex (0)).getRuleCount());

        assertEquals (".foo", ((CSSStyleRule)aSR.getRuleAtIndex (0)).getSelectorAtIndex(0).getAsCSSString());
        assertEquals ("color:white", ((CSSStyleRule)aSR.getRuleAtIndex (0)).getDeclarationAtIndex(0).getAsCSSString());

        assertTrue (((CSSStyleRule)aSR.getRuleAtIndex (0)).getRuleAtIndex(0) instanceof CSSStyleRule);
        assertEquals (1, ((CSSStyleRule)((CSSStyleRule)aSR.getRuleAtIndex (0)).getRuleAtIndex(0)).getSelectorCount());
        assertEquals (1, ((CSSStyleRule)((CSSStyleRule)aSR.getRuleAtIndex (0)).getRuleAtIndex(0)).getDeclarationCount());
        assertEquals (0, ((CSSStyleRule)((CSSStyleRule)aSR.getRuleAtIndex (0)).getRuleAtIndex(0)).getRuleCount());
        assertEquals (".bar", ((CSSStyleRule)((CSSStyleRule)aSR.getRuleAtIndex (0)).getRuleAtIndex(0)).getSelectorAtIndex(0).getAsCSSString());
        assertEquals ("color:orange", ((CSSStyleRule)((CSSStyleRule)aSR.getRuleAtIndex (0)).getRuleAtIndex(0)).getDeclarationAtIndex(0).getAsCSSString());

        assertTrue (((CSSStyleRule)aSR.getRuleAtIndex (0)).getRuleAtIndex(1) instanceof CSSNestedDeclarations);
        assertEquals (1, ((CSSNestedDeclarations)((CSSStyleRule)aSR.getRuleAtIndex (0)).getRuleAtIndex(1)).getDeclarationCount());
        assertEquals ("color:black", ((CSSNestedDeclarations)((CSSStyleRule)aSR.getRuleAtIndex (0)).getRuleAtIndex(1)).getDeclarationAtIndex(0).getAsCSSString());
    }
}
