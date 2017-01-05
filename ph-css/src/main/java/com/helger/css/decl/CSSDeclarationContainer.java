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
package com.helger.css.decl;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.annotation.Nonempty;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a common container of {@link CSSDeclaration} objects. In contrary
 * to {@link CSSDeclarationList} this class emits block level elements around
 * the declarations as used in style rules etc.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSDeclarationContainer extends CSSDeclarationList
{
  public CSSDeclarationContainer ()
  {}

  @Override
  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();

    final StringBuilder aSB = new StringBuilder ();
    final int nDeclCount = getDeclarationCount ();
    if (nDeclCount == 0)
    {
      aSB.append (bOptimizedOutput ? "{}" : " {}");
    }
    else
    {
      if (nDeclCount == 1)
      {
        // A single declaration
        aSB.append (bOptimizedOutput ? "{" : " { ");
        aSB.append (super.getAsCSSString (aSettings, nIndentLevel));
        aSB.append (bOptimizedOutput ? "}" : " }");
      }
      else
      {
        // More than one declaration
        aSB.append (bOptimizedOutput ? "{" : " {" + aSettings.getNewLineString ());
        aSB.append (super.getAsCSSString (aSettings, nIndentLevel));
        if (!bOptimizedOutput)
          aSB.append (aSettings.getIndent (nIndentLevel));
        aSB.append ('}');
      }
    }
    return aSB.toString ();
  }
}
