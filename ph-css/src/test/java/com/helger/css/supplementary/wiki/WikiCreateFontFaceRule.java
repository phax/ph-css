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
package com.helger.css.supplementary.wiki;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.string.StringHelper;
import com.helger.css.decl.CSSExpression;
import com.helger.css.decl.CSSExpressionMemberFunction;
import com.helger.css.decl.CSSFontFaceRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.ECSSExpressionOperator;

/**
 * This is example code to create a font-face rule from scratch
 *
 * @author Philip Helger
 */
public final class WikiCreateFontFaceRule
{
  @Nonnull
  private static CSSExpressionMemberFunction _createFormatFct (@Nonnull @Nonempty final String sFormatName)
  {
    return new CSSExpressionMemberFunction ("format", CSSExpression.createString (sFormatName));
  }

  /**
   * Create a single font-face rule.
   *
   * <pre>
   * &#64;font-face {
   *   font-family: "Your typeface";
   *   src: url("path/basename.eot");
   *   src: local("local font name"),
   *        url("path/basename.woff") format("woff"),
   *        url("path/basename.otf") format("opentype"),
   *        url("path/basename.svg#filename") format("svg");
   * }
   * </pre>
   *
   * @param sTypefaceName
   *        The name of the font-face in CSS. May neither be <code>null</code>
   *        nor empty.
   * @param sLocalName
   *        The name of the local font to be used. May be <code>null</code>.
   * @param sPath
   *        The server-relative path, where the font files reside. May not be
   *        <code>null</code>.
   * @param sBasename
   *        the base name of the font-files (without extension). May neither be
   *        <code>null</code> nor empty
   * @return The created {@link CascadingStyleSheet}.
   */
  @Nonnull
  public static CascadingStyleSheet createFontFace (@Nonnull @Nonempty final String sTypefaceName,
                                                    @Nullable final String sLocalName,
                                                    @Nonnull final String sPath,
                                                    @Nonnull final String sBasename)
  {
    final CascadingStyleSheet aCSS = new CascadingStyleSheet ();
    final CSSFontFaceRule aFFR = new CSSFontFaceRule ();

    // The font-family
    aFFR.addDeclaration ("font-family", CSSExpression.createString (sTypefaceName), false);

    // The special EOT file
    aFFR.addDeclaration ("src", CSSExpression.createURI (sPath + sBasename + ".eot"), false);

    // The generic rules
    final CSSExpression aExpr = new CSSExpression ();
    if (StringHelper.hasText (sLocalName))
      aExpr.addMember (new CSSExpressionMemberFunction ("local", CSSExpression.createString (sLocalName)))
           .addMember (ECSSExpressionOperator.COMMA);
    aExpr.addURI (sPath + sBasename + ".woff")
         .addMember (_createFormatFct ("woff"))
         .addMember (ECSSExpressionOperator.COMMA)
         .addURI (sPath + sBasename + ".otf")
         .addMember (_createFormatFct ("opentype"))
         .addMember (ECSSExpressionOperator.COMMA)
         .addURI (sPath + sBasename + ".svg#" + sBasename)
         .addMember (_createFormatFct ("svg"));
    aFFR.addDeclaration ("src", aExpr, false);

    // Add the font-face rule to the main CSS
    aCSS.addRule (aFFR);
    return aCSS;
  }
}
