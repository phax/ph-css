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
package com.helger.css.decl.visit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpressionMemberTermURI;
import com.helger.css.decl.CSSImportRule;
import com.helger.css.decl.ICSSTopLevelRule;

/**
 * Abstract {@link ICSSUrlVisitor} that modifies all URLs according to an
 * abstract rule
 *
 * @author Philip Helger
 */
@NotThreadSafe
public abstract class AbstractModifyingCSSUrlVisitor extends DefaultCSSUrlVisitor
{
  @Nonnull
  protected abstract String getModifiedURI (@Nonnull String sURI);

  @Override
  public void onImport (@Nonnull final CSSImportRule aImportRule)
  {
    // Get the source URL
    final String sURI = aImportRule.getLocationString ();
    // Modify the URL
    final String sModifiedURI = getModifiedURI (sURI);
    // And set the new URL
    aImportRule.setLocationString (sModifiedURI);
  }

  @Override
  public void onUrlDeclaration (@Nullable final ICSSTopLevelRule aTopLevelRule,
                                @Nonnull final CSSDeclaration aDeclaration,
                                @Nonnull final CSSExpressionMemberTermURI aExprTerm)
  {
    // Get the source URL
    final String sURI = aExprTerm.getURIString ();
    // Modify the URL
    final String sModifiedURI = getModifiedURI (sURI);
    // And set the new URL
    aExprTerm.setURIString (sModifiedURI);
  }
}
