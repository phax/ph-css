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

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.decl.visit.CSSVisitor;
import com.helger.css.decl.visit.DefaultCSSVisitor;
import com.helger.css.decl.visit.ICSSVisitor;
import com.helger.css.reader.CSSReaderDeclarationList;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Example how to read the content of a CSS style attribute, and visit all
 * contained declarations - with the API and with a visitor.
 *
 * @author Philip Helger
 */
public final class WikiVisitFromHtml
{
  public static void readFromStyleAttributeWithAPI ()
  {
    final String sStyle = "color:red; background:fixed !important";
    final CSSDeclarationList aDeclList = CSSReaderDeclarationList.readFromString (sStyle, ECSSVersion.CSS30);
    if (aDeclList == null)
      throw new IllegalStateException ("Failed to parse CSS: " + sStyle);
    // For all contained declarations
    for (final CSSDeclaration aDeclaration : aDeclList.getAllDeclarations ())
      System.out.println (aDeclaration.getProperty () +
                          ": " +
                          aDeclaration.getExpression ().getAsCSSString (new CSSWriterSettings (ECSSVersion.CSS30), 0) +
                          (aDeclaration.isImportant () ? " (important)" : " (not important)"));
  }

  public static void readFromStyleAttributeWithVisitor ()
  {
    final String sStyle = "color:red; background:fixed !important";
    final CSSDeclarationList aDeclList = CSSReaderDeclarationList.readFromString (sStyle, ECSSVersion.CSS30);
    if (aDeclList == null)
      throw new IllegalStateException ("Failed to parse CSS: " + sStyle);
    // Create a custom visitor
    final ICSSVisitor aVisitor = new DefaultCSSVisitor ()
    {
      @Override
      public void onDeclaration (@Nonnull final CSSDeclaration aDeclaration)
      {
        System.out.println (aDeclaration.getProperty () +
                            ": " +
                            aDeclaration.getExpression ().getAsCSSString (new CSSWriterSettings (ECSSVersion.CSS30),
                                                                          0) +
                            (aDeclaration.isImportant () ? " (important)" : " (not important)"));
      }
    };
    CSSVisitor.visitAllDeclarations (aDeclList, aVisitor);
  }
}
