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

import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpressionMemberTermURI;
import com.helger.css.decl.CSSImportRule;
import com.helger.css.decl.ICSSTopLevelRule;

/**
 * Interface for visiting all URLs in a CSS document.
 *
 * @author Philip Helger
 * @see DefaultCSSUrlVisitor DefaultCSSUrlVisitor for a default empty
 *      implementation
 * @see AbstractModifyingCSSUrlVisitor AbstractModifyingCSSUrlVisitor for an
 *      abstract URL visitor that modifies URLs according to a rule
 */
public interface ICSSUrlVisitor
{
  /**
   * Before visiting starts.
   */
  default void begin ()
  {}

  /**
   * Called on CSS import statement. Use <code>aImportRule.getLocation()</code>
   * to retrieve the imported URL.
   *
   * @param aImportRule
   *        Other imported CSS. Never <code>null</code>.
   */
  void onImport (@Nonnull CSSImportRule aImportRule);

  /**
   * Called on a CSS declaration value that contains an URL.<br>
   * Note: for keyframes it is currently not possible to retrieve the keyframes
   * block to which the declaration belongs.
   *
   * @param aTopLevelRule
   *        Top level rule of the URL. May be <code>null</code> when a
   *        declaration list is handled.
   * @param aDeclaration
   *        Declaration of the URL. Never <code>null</code>.
   * @param aURITerm
   *        The URI term from the current expression. Never <code>null</code>.
   */
  void onUrlDeclaration (@Nullable ICSSTopLevelRule aTopLevelRule,
                         @Nonnull CSSDeclaration aDeclaration,
                         @Nonnull CSSExpressionMemberTermURI aURITerm);

  /**
   * After visiting is done.
   */
  default void end ()
  {}

}
