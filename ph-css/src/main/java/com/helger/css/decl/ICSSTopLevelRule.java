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

import com.helger.commons.annotation.MustImplementEqualsAndHashcode;
import com.helger.css.ICSSWriteable;

/**
 * <p>
 * Marker interface for all top level CSS elements that can occur in any order
 * </p>
 * <ul>
 * <li>style rules - {@link CSSStyleRule}</li>
 * <li>page rules - {@link CSSPageRule}</li>
 * <li>media rules - {@link CSSMediaRule}</li>
 * <li>font face rules - {@link CSSFontFaceRule}</li>
 * <li>keyframes rules - {@link CSSKeyframesRule}</li>
 * <li>viewport rules - {@link CSSViewportRule}</li>
 * <li>supports rules - {@link CSSSupportsRule}</li>
 * <li>unknown rules - {@link CSSUnknownRule}</li>
 * </ul>
 * <p>
 * To easily iterate over all rules contained in a {@link CascadingStyleSheet}
 * you can use the
 * {@link com.helger.css.decl.visit.CSSVisitor#visitCSS(CascadingStyleSheet, com.helger.css.decl.visit.ICSSVisitor)}
 * method. An empty stub implementation of
 * {@link com.helger.css.decl.visit.ICSSVisitor} is the class
 * {@link com.helger.css.decl.visit.DefaultCSSVisitor} which is a good basis for
 * your own implementations.
 * </p>
 *
 * @author Philip Helger
 */
@MustImplementEqualsAndHashcode
public interface ICSSTopLevelRule extends ICSSWriteable
{
  /* empty */
}
