/**
 * Copyright (C) 2014-2015 Philip Helger (www.helger.com)
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

import java.util.List;

import org.junit.Test;

import com.helger.commons.collections.CollectionHelper;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.decl.CSSExpressionMemberTermSimple;
import com.helger.css.handler.DoNothingCSSParseExceptionHandler;
import com.helger.css.handler.ICSSParseExceptionHandler;
import com.helger.css.reader.CSSReaderDeclarationList;

/**
 * Test class for class {@link CSSReaderDeclarationList}
 *
 * @author Philip Helger
 */
public final class CSSReaderDeclarationListTest
{
  private static final List <String> VALID = CollectionHelper.newList ("",
                                                                      "    ",
                                                                      ";",
                                                                      ";;",
                                                                      "  ;     ;     ;   ",
                                                                      "color:red; background:fixed;",
                                                                      "  color:red; background:fixed;  ",
                                                                      "color:red; background:fixed",
                                                                      "color:red; background:fixed !important",
                                                                      /*
                                                                       * invalid
                                                                       * but
                                                                       * parsed
                                                                       */
                                                                      " color : ",
                                                                      " color :  !important ",
                                                                      " color :  !  important ");
  private static final List <String> INVALID = CollectionHelper.newList ("color", " color ");

  @Test
  public void testIsValidCSS21 ()
  {
    for (final String sCSS : VALID)
      assertTrue (sCSS, CSSReaderDeclarationList.isValidCSS (sCSS, ECSSVersion.CSS21));
    for (final String sCSS : INVALID)
      assertFalse (sCSS, CSSReaderDeclarationList.isValidCSS (sCSS, ECSSVersion.CSS21));
  }

  @Test
  public void testIsValidCSS30 ()
  {
    for (final String sCSS : VALID)
      assertTrue (sCSS, CSSReaderDeclarationList.isValidCSS (sCSS, ECSSVersion.CSS30));
    for (final String sCSS : INVALID)
      assertFalse (sCSS, CSSReaderDeclarationList.isValidCSS (sCSS, ECSSVersion.CSS30));
  }

  @Test
  public void testRead21 ()
  {
    final ICSSParseExceptionHandler aHdl = DoNothingCSSParseExceptionHandler.getInstance ();
    for (final String sCSS : VALID)
    {
      final CSSDeclarationList aDL = CSSReaderDeclarationList.readFromString (sCSS, ECSSVersion.CSS21, aHdl);
      assertNotNull (aDL);
    }
    for (final String sCSS : INVALID)
      assertNull (sCSS, CSSReaderDeclarationList.readFromString (sCSS, ECSSVersion.CSS21, aHdl));
  }

  @Test
  public void testRead30 ()
  {
    final ICSSParseExceptionHandler aHdl = DoNothingCSSParseExceptionHandler.getInstance ();
    for (final String sCSS : VALID)
      assertNotNull (sCSS, CSSReaderDeclarationList.readFromString (sCSS, ECSSVersion.CSS30, aHdl));
    for (final String sCSS : INVALID)
      assertNull (sCSS, CSSReaderDeclarationList.readFromString (sCSS, ECSSVersion.CSS30, aHdl));
  }

  @Test
  public void testReadAndValidate ()
  {
    final CSSDeclarationList aList = CSSReaderDeclarationList.readFromString ("color:red; background:fixed;",
                                                                              ECSSVersion.CSS30);
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
