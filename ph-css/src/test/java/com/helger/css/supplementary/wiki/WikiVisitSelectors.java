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

import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.visit.CSSVisitor;
import com.helger.css.decl.visit.DefaultCSSVisitor;
import com.helger.css.decl.visit.ICSSVisitor;
import com.helger.css.reader.CSSReader;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Example how to extract all selectors from a certain CSS file using an
 * {@link ICSSVisitor}.
 *
 * @author Philip Helger
 */
public final class WikiVisitSelectors
{
  public void readAllSelectors ()
  {
    final String sStyle = "blockquote p,\r\n" +
                          "blockquote ul,\r\n" +
                          "blockquote ol {\r\n" +
                          "  line-height:normal;\r\n" +
                          "  font-style:italic;\r\n" +
                          "}\r\n" +
                          "\r\n" +
                          "a { color:#FFEA6F; }\r\n" +
                          "\r\n" +
                          "a:hover { text-decoration:none; }\r\n" +
                          "\r\n" +
                          "img { border:none; }";
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sStyle, ECSSVersion.CSS30);
    final ICommonsList <String> aAllSelectors = new CommonsArrayList <String> ();
    CSSVisitor.visitCSS (aCSS, new DefaultCSSVisitor ()
    {
      @Override
      public void onStyleRuleSelector (@Nonnull final CSSSelector aSelector)
      {
        aAllSelectors.add (aSelector.getAsCSSString (new CSSWriterSettings (ECSSVersion.CSS30), 0));
      }
    });
    System.out.println (aAllSelectors);
  }
}
