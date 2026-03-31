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
package com.helger.css.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.ICommonsList;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.decl.CSSExpressionMemberTermSimple;
import com.helger.css.handler.DoNothingCSSParseExceptionCallback;
import com.helger.css.handler.ICSSParseExceptionCallback;

/**
 * Test class for class {@link CSSReaderDeclarationList}
 *
 * @author Philip Helger
 */
public final class CSSReaderDeclarationListTest
{
  private static final ICommonsList <String> VALID = new CommonsArrayList <> ("",
                                                                              "    ",
                                                                              ";",
                                                                              ";;",
                                                                              "  ;     ;     ;   ",
                                                                              "color:red; background:fixed;",
                                                                              "  color:red; background:fixed;  ",
                                                                              "color:red; background:fixed",
                                                                              "color:red; background:fixed !important");
  private static final ICommonsList <String> INVALID = new CommonsArrayList <> ("color",
                                                                                " color ",
                                                                                // previously
                                                                                // parsed:
                                                                                " color : ",
                                                                                " color :  !important ",
                                                                                " color :  !  important ");

  @Test
  public void testIsValid ()
  {
    for (final String sCSS : VALID)
      assertTrue (sCSS, CSSReaderDeclarationList.isValidCSS (sCSS));
    for (final String sCSS : INVALID)
      assertFalse (sCSS, CSSReaderDeclarationList.isValidCSS (sCSS));
  }

  @Test
  public void testRead ()
  {
    final ICSSParseExceptionCallback aHdl = new DoNothingCSSParseExceptionCallback ();
    for (final String sCSS : VALID)
      assertNotNull (sCSS,
                     CSSReaderDeclarationList.readFromString (sCSS,
                                                              new CSSReaderSettings ().setCustomExceptionHandler (aHdl)));
    for (final String sCSS : INVALID)
      assertNull (sCSS,
                  CSSReaderDeclarationList.readFromString (sCSS,
                                                           new CSSReaderSettings ().setCustomExceptionHandler (aHdl)));
  }

  @Test
  public void testReadAndValidate ()
  {
    final CSSDeclarationList aList = CSSReaderDeclarationList.readFromString ("color:red; background:fixed;");
    assertNotNull (aList);
    assertEquals (2, aList.getDeclarationCount ());
    CSSDeclaration aDecl = aList.getDeclarationAtIndex (0);
    assertNotNull (aDecl);
    assertEquals ("color", aDecl.getProperty ());
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    assertEquals ("red", ((CSSExpressionMemberTermSimple) aDecl.getExpression ().getMemberAtIndex (0)).getValue ());

    aDecl = aList.getDeclarationAtIndex (1);
    assertNotNull (aDecl);
    assertEquals ("background", aDecl.getProperty ());
  }
}
