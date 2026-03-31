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
package com.helger.css.supplementary.issues;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;

/**
 * Test for issue: https://github.com/phax/ph-css/issues/105
 *
 * @author Philip Helger
 */
public final class Issue105Test
{
  @Test
  public void testIssue ()
  {
    final String sCSS = "div {\n" + "  color:red;\n" + "width: 100%;\n" + "}";
    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader (sCSS,
                                                                     new CSSReaderSettings ().setUseSourceLocation (true));
    assertNotNull (aCSS);

    final CSSStyleRule aRule = aCSS.getAllStyleRules ().get (0);
    assertNotNull (aRule);

    // This is the "div"
    assertEquals (1, aRule.getSourceLocation ().getFirstTokenBeginLineNumber ());
    assertEquals (1, aRule.getSourceLocation ().getFirstTokenBeginColumnNumber ());
    assertEquals (1, aRule.getSourceLocation ().getFirstTokenEndLineNumber ());
    assertEquals (3, aRule.getSourceLocation ().getFirstTokenEndColumnNumber ());

    // This is the "}"
    assertEquals (4, aRule.getSourceLocation ().getLastTokenBeginLineNumber ());
    assertEquals (1, aRule.getSourceLocation ().getLastTokenBeginColumnNumber ());
    assertEquals (4, aRule.getSourceLocation ().getLastTokenEndLineNumber ());
    assertEquals (1, aRule.getSourceLocation ().getLastTokenEndColumnNumber ());

    {
      final CSSDeclaration aDecl = aRule.getAllDeclarations ().get (0);
      // This is the "color"
      assertEquals (2, aDecl.getSourceLocation ().getFirstTokenBeginLineNumber ());
      // It's 3, because of the 2 spaces for indent
      assertEquals (3, aDecl.getSourceLocation ().getFirstTokenBeginColumnNumber ());
      assertEquals (2, aDecl.getSourceLocation ().getFirstTokenEndLineNumber ());
      assertEquals (7, aDecl.getSourceLocation ().getFirstTokenEndColumnNumber ());

      // This is the "red"
      assertEquals (2, aDecl.getSourceLocation ().getLastTokenBeginLineNumber ());
      assertEquals (9, aDecl.getSourceLocation ().getLastTokenBeginColumnNumber ());
      assertEquals (2, aDecl.getSourceLocation ().getLastTokenEndLineNumber ());
      assertEquals (11, aDecl.getSourceLocation ().getLastTokenEndColumnNumber ());
    }

    {
      final CSSDeclaration aDecl = aRule.getAllDeclarations ().get (1);
      // This is the "width"
      assertEquals (3, aDecl.getSourceLocation ().getFirstTokenBeginLineNumber ());
      assertEquals (1, aDecl.getSourceLocation ().getFirstTokenBeginColumnNumber ());
      assertEquals (3, aDecl.getSourceLocation ().getFirstTokenEndLineNumber ());
      assertEquals (5, aDecl.getSourceLocation ().getFirstTokenEndColumnNumber ());

      // This is the "100%"
      assertEquals (3, aDecl.getSourceLocation ().getLastTokenBeginLineNumber ());
      assertEquals (8, aDecl.getSourceLocation ().getLastTokenBeginColumnNumber ());
      assertEquals (3, aDecl.getSourceLocation ().getLastTokenEndLineNumber ());
      assertEquals (11, aDecl.getSourceLocation ().getLastTokenEndColumnNumber ());
    }
  }
}
