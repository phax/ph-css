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
import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotation.OverrideOnDemand;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSFontFaceRule;
import com.helger.css.decl.CSSImportRule;
import com.helger.css.decl.CSSKeyframesBlock;
import com.helger.css.decl.CSSKeyframesRule;
import com.helger.css.decl.CSSMediaRule;
import com.helger.css.decl.CSSNamespaceRule;
import com.helger.css.decl.CSSPageMarginBlock;
import com.helger.css.decl.CSSPageRule;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CSSSupportsRule;
import com.helger.css.decl.CSSUnknownRule;
import com.helger.css.decl.CSSViewportRule;

/**
 * Default implementation of the {@link ICSSVisitor} interface. Use as base
 * class for your own implementations since this class does nothing on its own.
 *
 * @author Philip Helger
 */
@Immutable
public class DefaultCSSVisitor implements ICSSVisitor
{
  public DefaultCSSVisitor ()
  {}

  @OverrideOnDemand
  public void begin ()
  {}

  @OverrideOnDemand
  public void onImport (@Nonnull final CSSImportRule aImportRule)
  {}

  @OverrideOnDemand
  public void onNamespace (@Nonnull final CSSNamespaceRule aNamespaceRule)
  {}

  @OverrideOnDemand
  public void onDeclaration (@Nonnull final CSSDeclaration aDeclaration)
  {}

  @OverrideOnDemand
  public void onBeginStyleRule (@Nonnull final CSSStyleRule aStyleRule)
  {}

  @OverrideOnDemand
  public void onStyleRuleSelector (@Nonnull final CSSSelector aSelector)
  {}

  @OverrideOnDemand
  public void onEndStyleRule (@Nonnull final CSSStyleRule aStyleRule)
  {}

  @OverrideOnDemand
  public void onBeginPageRule (@Nonnull final CSSPageRule aPageRule)
  {}

  @OverrideOnDemand
  public void onBeginPageMarginBlock (@Nonnull final CSSPageMarginBlock aPageMarginBlock)
  {}

  @OverrideOnDemand
  public void onEndPageMarginBlock (@Nonnull final CSSPageMarginBlock aPageMarginBlock)
  {}

  @OverrideOnDemand
  public void onEndPageRule (@Nonnull final CSSPageRule aPageRule)
  {}

  @OverrideOnDemand
  public void onBeginFontFaceRule (@Nonnull final CSSFontFaceRule aFontFaceRule)
  {}

  @OverrideOnDemand
  public void onEndFontFaceRule (@Nonnull final CSSFontFaceRule aFontFaceRule)
  {}

  @OverrideOnDemand
  public void onBeginMediaRule (@Nonnull final CSSMediaRule aMediaRule)
  {}

  @OverrideOnDemand
  public void onEndMediaRule (@Nonnull final CSSMediaRule aMediaRule)
  {}

  @OverrideOnDemand
  public void onBeginKeyframesRule (@Nonnull final CSSKeyframesRule aKeyframesRule)
  {}

  @OverrideOnDemand
  public void onBeginKeyframesBlock (@Nonnull final CSSKeyframesBlock aKeyframesBlock)
  {}

  @OverrideOnDemand
  public void onEndKeyframesBlock (@Nonnull final CSSKeyframesBlock aKeyframesBlock)
  {}

  @OverrideOnDemand
  public void onEndKeyframesRule (@Nonnull final CSSKeyframesRule aKeyframesRule)
  {}

  @OverrideOnDemand
  public void onBeginViewportRule (@Nonnull final CSSViewportRule aViewportRule)
  {}

  @OverrideOnDemand
  public void onEndViewportRule (@Nonnull final CSSViewportRule aViewportRule)
  {}

  @OverrideOnDemand
  public void onBeginSupportsRule (@Nonnull final CSSSupportsRule aSupportsRule)
  {}

  @OverrideOnDemand
  public void onEndSupportsRule (@Nonnull final CSSSupportsRule aSupportsRule)
  {}

  @OverrideOnDemand
  public void onUnknownRule (@Nonnull final CSSUnknownRule aUnknownRule)
  {}

  @OverrideOnDemand
  public void end ()
  {}
}
