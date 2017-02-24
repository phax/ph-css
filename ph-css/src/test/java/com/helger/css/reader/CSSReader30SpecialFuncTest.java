/**
 * Copyright (C) 2014-2017 Philip Helger (www.helger.com)
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.charset.CharsetManager;
import com.helger.commons.charset.EUnicodeBOM;
import com.helger.commons.collection.ArrayHelper;
import com.helger.commons.io.streamprovider.ByteArrayInputStreamProvider;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpressionMemberFunction;
import com.helger.css.decl.CSSExpressionMemberMath;
import com.helger.css.decl.CSSExpressionMemberTermSimple;
import com.helger.css.decl.CSSExpressionMemberTermURI;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.ICSSExpressionMember;
import com.helger.css.reader.errorhandler.CollectingCSSParseErrorHandler;
import com.helger.css.reader.errorhandler.DoNothingCSSParseErrorHandler;
import com.helger.css.reader.errorhandler.LoggingCSSParseErrorHandler;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Test reading CSS 3.0 stuff
 *
 * @author Philip Helger
 */
public final class CSSReader30SpecialFuncTest
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (CSSReader30SpecialFuncTest.class);

  @Test
  public void testReadSpecialGood ()
  {
    final ECSSVersion eVersion = ECSSVersion.CSS30;
    final Charset aCharset = StandardCharsets.UTF_8;
    final File aFile = new File ("src/test/resources/testfiles/css30/good/artificial/test-postcss-cssnext.css");
    final CascadingStyleSheet aCSS = CSSReader.readFromFile (aFile, aCharset, eVersion);
    assertNotNull (aCSS);

    final String sCSS = new CSSWriter (eVersion, false).getCSSAsString (aCSS);
    assertNotNull (sCSS);
    if (false)
      s_aLogger.info (sCSS);
  }

  @Test
  public void testReadExpressions ()
  {
    final ECSSVersion eVersion = ECSSVersion.CSS30;
    final CSSWriterSettings aCSSWS = new CSSWriterSettings (eVersion, false);
    final Charset aCharset = StandardCharsets.UTF_8;
    final File aFile = new File ("src/test/resources/testfiles/css30/good/artificial/test-expression.css");
    final CascadingStyleSheet aCSS = CSSReader.readFromFile (aFile, aCharset, eVersion);
    assertNotNull (aCSS);
    assertEquals (1, aCSS.getRuleCount ());
    assertEquals (1, aCSS.getStyleRuleCount ());
    final CSSStyleRule aSR = aCSS.getStyleRuleAtIndex (0);
    assertEquals ("div", aSR.getSelectorsAsCSSString (aCSSWS, 0));
    assertEquals (23, aSR.getDeclarationCount ());
    int i = 0;
    for (final CSSDeclaration aDecl : aSR.getAllDeclarations ())
    {
      final String sExpectedName = Character.toString ((char) ('a' + i));
      assertEquals (sExpectedName, aDecl.getProperty ());
      ++i;
    }

    CSSDeclaration aDecl;
    ICSSExpressionMember aMember;

    // a: -5
    aDecl = aSR.getDeclarationOfPropertyName ("a");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("-5", aMember.getAsCSSString (aCSSWS, 0));

    // b: +5
    aDecl = aSR.getDeclarationOfPropertyName ("b");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("+5", aMember.getAsCSSString (aCSSWS, 0));

    // c: 5
    aDecl = aSR.getDeclarationOfPropertyName ("c");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("5", aMember.getAsCSSString (aCSSWS, 0));

    // d: -5.12
    aDecl = aSR.getDeclarationOfPropertyName ("d");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("-5.12", aMember.getAsCSSString (aCSSWS, 0));

    // e: +5.12
    aDecl = aSR.getDeclarationOfPropertyName ("e");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("+5.12", aMember.getAsCSSString (aCSSWS, 0));

    // f: 5.12
    aDecl = aSR.getDeclarationOfPropertyName ("f");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("5.12", aMember.getAsCSSString (aCSSWS, 0));

    // g: -5.12%
    aDecl = aSR.getDeclarationOfPropertyName ("g");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("-5.12%", aMember.getAsCSSString (aCSSWS, 0));

    // h: +5.12%
    aDecl = aSR.getDeclarationOfPropertyName ("h");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("+5.12%", aMember.getAsCSSString (aCSSWS, 0));

    // i: 5.12%
    aDecl = aSR.getDeclarationOfPropertyName ("i");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("5.12%", aMember.getAsCSSString (aCSSWS, 0));

    // j: -5px
    aDecl = aSR.getDeclarationOfPropertyName ("j");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("-5px", aMember.getAsCSSString (aCSSWS, 0));

    // k: +5px
    aDecl = aSR.getDeclarationOfPropertyName ("k");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("+5px", aMember.getAsCSSString (aCSSWS, 0));

    // l: 5px
    aDecl = aSR.getDeclarationOfPropertyName ("l");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("5px", aMember.getAsCSSString (aCSSWS, 0));

    // m: 'string1'
    aDecl = aSR.getDeclarationOfPropertyName ("m");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("'string1'", aMember.getAsCSSString (aCSSWS, 0));

    // n: "string2"
    aDecl = aSR.getDeclarationOfPropertyName ("n");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("\"string2\"", aMember.getAsCSSString (aCSSWS, 0));

    // o: abc
    aDecl = aSR.getDeclarationOfPropertyName ("o");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("abc", aMember.getAsCSSString (aCSSWS, 0));

    // p: from
    aDecl = aSR.getDeclarationOfPropertyName ("p");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("from", aMember.getAsCSSString (aCSSWS, 0));

    // q: to
    aDecl = aSR.getDeclarationOfPropertyName ("q");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("to", aMember.getAsCSSString (aCSSWS, 0));

    // r: url(a.gif)
    aDecl = aSR.getDeclarationOfPropertyName ("r");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermURI);
    assertEquals ("url(a.gif)", aMember.getAsCSSString (aCSSWS, 0));

    // s: #123
    aDecl = aSR.getDeclarationOfPropertyName ("s");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("#123", aMember.getAsCSSString (aCSSWS, 0));

    // t: function(5,6,abc)
    aDecl = aSR.getDeclarationOfPropertyName ("t");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberFunction);
    assertEquals ("function", ((CSSExpressionMemberFunction) aMember).getFunctionName ());
    // 3 parameters and 2 commas
    assertEquals (5, ((CSSExpressionMemberFunction) aMember).getExpression ().getMemberCount ());
    assertEquals ("function(5,6,abc)", aMember.getAsCSSString (aCSSWS, 0));

    // u: calc(4 + 5)
    aDecl = aSR.getDeclarationOfPropertyName ("u");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberMath);
    // 2 values and 1 operator
    assertEquals (3, ((CSSExpressionMemberMath) aMember).getMemberCount ());
    assertEquals ("calc(4 + 5)", aMember.getAsCSSString (aCSSWS, 0));

    // v: inherit
    aDecl = aSR.getDeclarationOfPropertyName ("v");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("inherit", aMember.getAsCSSString (aCSSWS, 0));

    // w: u+1234
    aDecl = aSR.getDeclarationOfPropertyName ("w");
    assertNotNull (aDecl);
    assertEquals (1, aDecl.getExpression ().getMemberCount ());
    aMember = aDecl.getExpression ().getMemberAtIndex (0);
    assertTrue (aMember instanceof CSSExpressionMemberTermSimple);
    assertEquals ("u+1234", aMember.getAsCSSString (aCSSWS, 0));

    // Write result
    final String sCSS = new CSSWriter (aCSSWS).getCSSAsString (aCSS);
    assertNotNull (sCSS);
    if (false)
      s_aLogger.info (sCSS);
  }

  @Test
  public void testReadSpecialBadButRecoverable ()
  {
    final CollectingCSSParseErrorHandler aErrors = new CollectingCSSParseErrorHandler ();
    final ECSSVersion eVersion = ECSSVersion.CSS30;
    final Charset aCharset = StandardCharsets.UTF_8;
    final File aFile = new File ("src/test/resources/testfiles/css30/bad_but_recoverable_and_browsercompliant/test-string.css");
    final CascadingStyleSheet aCSS = CSSReader.readFromFile (aFile,
                                                             aCharset,
                                                             eVersion,
                                                             aErrors.and (new LoggingCSSParseErrorHandler ()));
    assertNotNull (aFile.getAbsolutePath (), aCSS);
  }

  @Test
  public void testReadWithBOM ()
  {
    final String sCSSBase = "/* comment */.class{color:red}.class{color:blue}";
    for (final EUnicodeBOM eBOM : EUnicodeBOM.values ())
    {
      final Charset aDeterminedCharset = eBOM.getCharset ();
      if (aDeterminedCharset != null)
      {
        final CascadingStyleSheet aCSS = CSSReader.readFromStream (new ByteArrayInputStreamProvider (ArrayHelper.getConcatenated (eBOM.getAllBytes (),
                                                                                                                                  CharsetManager.getAsBytes (sCSSBase,
                                                                                                                                                             aDeterminedCharset))),
                                                                   aDeterminedCharset,
                                                                   ECSSVersion.CSS30,
                                                                   new DoNothingCSSParseErrorHandler ());
        assertNotNull ("Failed to read with BOM " + eBOM, aCSS);
        assertEquals (".class{color:red}.class{color:blue}",
                      new CSSWriter (ECSSVersion.CSS30, true).getCSSAsString (aCSS));
      }
    }
  }

  @Test
  public void testReadSingleLineComments ()
  {
    final ECSSVersion eVersion = ECSSVersion.CSS30;
    final Charset aCharset = StandardCharsets.UTF_8;
    final File aFile = new File ("src/test/resources/testfiles/css30/good/artificial/test-singleline-comments.css");
    final CascadingStyleSheet aCSS = CSSReader.readFromFile (aFile, aCharset, eVersion);
    assertNotNull (aCSS);
    assertEquals (13, aCSS.getRuleCount ());
    assertEquals (13, aCSS.getStyleRuleCount ());

    // #any1 - #any5
    assertEquals (2, aCSS.getStyleRuleAtIndex (1).getDeclarationCount ());
    assertEquals (1, aCSS.getStyleRuleAtIndex (2).getDeclarationCount ());
    assertEquals (1, aCSS.getStyleRuleAtIndex (3).getDeclarationCount ());
    assertEquals (0, aCSS.getStyleRuleAtIndex (4).getDeclarationCount ());
    assertEquals (0, aCSS.getStyleRuleAtIndex (5).getDeclarationCount ());
    // .test1 - .test7
    assertEquals (2, aCSS.getStyleRuleAtIndex (6).getDeclarationCount ());
    assertEquals (3, aCSS.getStyleRuleAtIndex (7).getDeclarationCount ());
    assertEquals (1, aCSS.getStyleRuleAtIndex (8).getDeclarationCount ());
    assertEquals (1, aCSS.getStyleRuleAtIndex (9).getDeclarationCount ());
    assertEquals (2, aCSS.getStyleRuleAtIndex (10).getDeclarationCount ());
    assertEquals (2, aCSS.getStyleRuleAtIndex (11).getDeclarationCount ());
    assertEquals (1, aCSS.getStyleRuleAtIndex (12).getDeclarationCount ());
  }
}
