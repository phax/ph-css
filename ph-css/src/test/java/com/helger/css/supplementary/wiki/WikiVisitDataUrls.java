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

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpressionMemberTermURI;
import com.helger.css.decl.CSSImportRule;
import com.helger.css.decl.CSSURI;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.ICSSTopLevelRule;
import com.helger.css.decl.visit.CSSVisitor;
import com.helger.css.decl.visit.DefaultCSSUrlVisitor;
import com.helger.css.decl.visit.ICSSUrlVisitor;
import com.helger.css.reader.CSSReader;
import com.helger.css.utils.CSSDataURL;

/**
 * Example how to extract all URLs from a certain CSS file using an
 * {@link ICSSUrlVisitor} and handle them as data URLs.
 *
 * @author Philip Helger
 */
public final class WikiVisitDataUrls
{
  public void readFromStyleAttributeWithAPI ()
  {
    final String sStyle = "@import '/folder/foobar.css';\n" +
                          "div{background:fixed url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAIAAAACUFjqAAAABGdBTUEAALGPC/xhBQAAAAlwSFlzAAALEgAACxIB0t1+/AAAAAd0SU1FB9EFBAoYMhVvMQIAAAAtSURBVHicY/z//z8DHoBH+v///yy4FDEyMjIwMDDhM3lgpaEuh7gTEzDiDxYA9HEPDF90e5YAAAAASUVORK5CYII=) !important;}\n" +
                          "span { background-image:url('/my/folder/b.gif');}";
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sStyle, ECSSVersion.CSS30);
    CSSVisitor.visitCSSUrl (aCSS, new DefaultCSSUrlVisitor ()
    {
      // Called for each import
      @Override
      public void onImport (@Nonnull final CSSImportRule aImportRule)
      {
        System.out.println ("Import: " + aImportRule.getLocationString ());
      }

      // Call for URLs outside of URLs
      @Override
      public void onUrlDeclaration (@Nullable final ICSSTopLevelRule aTopLevelRule,
                                    @Nonnull final CSSDeclaration aDeclaration,
                                    @Nonnull final CSSExpressionMemberTermURI aURITerm)
      {
        final CSSURI aURI = aURITerm.getURI ();

        if (aURI.isDataURL ())
        {
          final CSSDataURL aDataURL = aURI.getAsDataURL ();
          System.out.println (aDeclaration.getProperty () +
                              " - references data URL with " +
                              aDataURL.getContentLength () +
                              " bytes of content");
        }
        else
          System.out.println (aDeclaration.getProperty () + " - references regular URL: " + aURI.getURI ());
      }
    });
  }
}
