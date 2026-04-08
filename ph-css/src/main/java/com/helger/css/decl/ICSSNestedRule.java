/*
 * Copyright (C) 2014-2026 Philip Helger (www.helger.com)
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

import com.helger.annotation.style.MustImplementEqualsAndHashcode;
import com.helger.css.ICSSWriteable;

/**
 * Marker interface for all nested CSS elements that can occur in any order
 *
 * <ul>
 * <li>layer rules - {@link CSSLayerRule}
 * <li>media rules - {@link CSSMediaRule}
 * <li>nested declarations - {@link CSSNestedDeclarations}
 * <li>style rules - {@link CSSStyleRule}
 * <li>supports rules - {@link CSSSupportsRule}
 * <li>unknown rules - {@link CSSUnknownRule}
 * </ul>
 *
 * To easily iterate over all rules contained in a {@link CascadingStyleSheet}
 * you can use the
 * {@link com.helger.css.decl.visit.CSSVisitor#visitCSS(CascadingStyleSheet, com.helger.css.decl.visit.ICSSVisitor) CSSVisitor#visitCSS(sheet, visitor)}
 * method. An empty stub implementation of
 * {@link com.helger.css.decl.visit.ICSSVisitor ICSSVisitor} is the class
 * {@link com.helger.css.decl.visit.DefaultCSSVisitor DefaultCSSVisitor} which is a good basis for
 * your own implementations.
 *
 * @author Philip Helger
 * @since 8.2.0
 */
@MustImplementEqualsAndHashcode
public interface ICSSNestedRule extends ICSSWriteable
{
  /* empty */
}
